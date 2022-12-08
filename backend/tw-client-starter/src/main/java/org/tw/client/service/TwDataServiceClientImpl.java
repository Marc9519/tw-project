package org.tw.client.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.tw.data.service.model.MapComponent;
import org.tw.data.service.model.MapComponentId;
import org.tw.data.service.model.Village;

public class TwDataServiceClientImpl {

	private static final Logger logger = LoggerFactory.getLogger(TwDataServiceClientImpl.class);

	private String baseUrl;
	private RestTemplate template;

	public TwDataServiceClientImpl(String baseUrl) {
		this.baseUrl = baseUrl;
		this.template = new RestTemplate();
	}

	public Map<Integer, Map<Integer, Object>> loadAllMapAssetsAsync(){
		List<Callable<Map<Integer, Map<Integer, Object>>>> callables = prepareCallables();
		return executeNodeThreads(callables);
	}


	private List<Callable<Map<Integer, Map<Integer, Object>>>> prepareCallables() {
		Map<Integer, List<Integer>> threadDistribMap = generateThreadDistMap();
		List<Callable<Map<Integer, Map<Integer, Object>>>> callables = new ArrayList<>();
		for (Entry<Integer, List<Integer>> e: threadDistribMap.entrySet()) {
			List<Integer> values = e.getValue();
			int fromX = values.get(0);
			int toX = values.get(1);
			int fromY = values.get(2);
			int toY = values.get(3);
			Callable<Map<Integer, Map<Integer, Object>>> c = () -> loadMapDataByRange(fromX, toX, fromY, toY);
			callables.add(c);
		}
		return callables;
	}

	private Map<Integer, List<Integer>> generateThreadDistMap() {
		Map<Integer, List<Integer>> resultMap = new LinkedHashMap<>();
		resultMap.put(0, Arrays.asList(0, 499, 0, 499));
		resultMap.put(1, Arrays.asList(500, 999, 0, 499));
		resultMap.put(2, Arrays.asList(0, 499, 500, 999));
		resultMap.put(3, Arrays.asList(500, 999, 500, 999));
		return resultMap;
	}

	private Map<Integer, Map<Integer, Object>> executeNodeThreads(List<Callable<Map<Integer, Map<Integer, Object>>>> callables) {
		Map<Integer, Map<Integer, Object>> result = new HashMap<>();
		ExecutorService executor = Executors.newFixedThreadPool(10);
		try {
			List<Future<Map<Integer, Map<Integer, Object>>>> futures = executor.invokeAll(callables);
			// Get Results
			for (Future<Map<Integer, Map<Integer, Object>>> future : futures) {
				Map<Integer, Map<Integer, Object>> subResult = future.get();
				// Merge
				this.merge(result, subResult);
			}
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage(),e);
			Thread.currentThread().interrupt();
		}finally {
			executor.shutdown();
		}
		return result;
	}

	private void merge(Map<Integer, Map<Integer, Object>> result, Map<Integer, Map<Integer, Object>> subResult) {
		for (Entry<Integer, Map<Integer, Object>> outerEntry : subResult.entrySet()) {
			Integer xPos = outerEntry.getKey();
			Map<Integer, Object> innerMap = outerEntry.getValue();
			if (!result.containsKey(xPos)) {
				result.put(xPos, innerMap);
				continue;
			}
			Map<Integer, Object> resultMap = result.get(xPos);
			for (Entry<Integer, Object> innerEntry : innerMap.entrySet()) {
				Integer yPos = innerEntry.getKey();
				Object subResultVal = innerEntry.getValue();
				if (resultMap.containsKey(yPos)) {
					logger.info("INVALID STATE! {}|{}", xPos , yPos);
				}else {
					resultMap.put(yPos, subResultVal);
				}
			}
		}
	}

	private Map<Integer, Map<Integer, Object>> loadMapDataByRange(int fromX, int toX, int fromY, int toY){
		Map<Integer, Map<Integer, Object>> result = new HashMap<>();
		// Search for villages
		List<Village> villages = findVillagesByRange(fromX, toX, fromY, toY);
		for (Village village : villages) {
			Integer xPos = village.getxPos();
			Integer yPos = village.getyPos();
			Map<Integer, Object> innerMap = result.get(xPos);
			if (innerMap == null) {
				innerMap = new HashMap<>();
				innerMap.put(yPos, village);
				result.put(xPos, innerMap);
			}else {
				innerMap.put(yPos, village);
			}
		}
		// Search for map tiles
		List<MapComponent> tiles = findTileComponentsByRange(fromX, toX, fromY, toY);
		for (MapComponent tile : tiles) {
			MapComponentId id = tile.getId();
			Integer xPos = id.getxPos();
			Integer yPos = id.getyPos();
			Map<Integer, Object> innerMap = result.get(xPos);
			if (innerMap == null) {
				innerMap = new TreeMap<>();
				innerMap.put(yPos, tile.getMapAsset());
				result.put(xPos, innerMap);
			}else if (innerMap.get(yPos) != null) {
				continue;
			}
			innerMap.put(yPos, tile.getMapAsset());
		}
		return result;
	}


	public List<Village> findVillagesByRange(int fromX, int toX, int fromY, int toY) {
		String requestUrl = baseUrl.concat("map/villages/search");
		String uriString = UriComponentsBuilder.fromHttpUrl(requestUrl)
		.queryParam("fromX", fromX)
		.queryParam("toX", toX)
		.queryParam("fromY", fromY)
		.queryParam("toY", toY)
		.encode().toUriString();
		ResponseEntity<Village[]> responseEntity = template.getForEntity(uriString, Village[].class);
		return Arrays.asList(responseEntity.getBody());
	}


	public List<MapComponent> findTileComponentsByRange(int fromX, int toX, int fromY, int toY) {
		String requestUrl = baseUrl.concat("map/tiles/search");
		String uriString = UriComponentsBuilder.fromHttpUrl(requestUrl)
		.queryParam("fromX", fromX)
		.queryParam("toX", toX)
		.queryParam("fromY", fromY)
		.queryParam("toY", toY)
		.encode().toUriString();
		ResponseEntity<MapComponent[]> responseEntity = template.getForEntity(uriString, MapComponent[].class);
		return Arrays.asList(responseEntity.getBody());
	}

}