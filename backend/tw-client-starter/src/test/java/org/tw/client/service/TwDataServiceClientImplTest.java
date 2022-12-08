package org.tw.client.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.tw.data.service.model.MapComponent;
import org.tw.data.service.model.Village;

public class TwDataServiceClientImplTest {


	@Test
	public void testLoadAllMapAssetsAsync() throws Exception {
		// Given
		String baseUrl = "http://localhost:1138/tw/rest/data/twinfo/";
		TwDataServiceClientImpl client = new TwDataServiceClientImpl(baseUrl);
		// Given
		Map<Integer, Map<Integer, Object>> assets = client.loadAllMapAssetsAsync();
		//Then
		assertNotNull(assets);
		assertEquals(1000, assets.size());
		for (Entry<Integer, Map<Integer, Object>> e : assets.entrySet()) {
			Map<Integer, Object> inner = e.getValue();
			assertEquals(1000, inner.size());
		}
		assets.entrySet().forEach(e ->  assertEquals(1000, e.getValue().size()));
	}

	@Test
	public void testfindComponentsByRange() throws Exception {
		// Given
		String baseUrl = "http://localhost:1138/tw/rest/data/twinfo/";
		TwDataServiceClientImpl client = new TwDataServiceClientImpl(baseUrl);
		// When
		List<MapComponent> tileComponents = client.findTileComponentsByRange(500, 505, 500, 505);
		List<Village> villages= client.findVillagesByRange(500, 505, 500, 505);
		// Then
		assertNotNull(tileComponents);
		assertFalse(tileComponents.isEmpty());
		assertNotNull(villages);
		assertFalse(villages.isEmpty());
	}

}