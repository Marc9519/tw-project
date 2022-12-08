package org.tw.intface.bl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tw.intface.factory.MapAssetCr;
import org.tw.intface.model.MapAsset;
import org.tw.intface.util.UrlConnector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * BL for exporting map tile data (--> Refers to asset tiles)
 */
public class ExportMapDataBl {

	/*
	 * The logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ExportMapDataBl.class);

	/*
	 * The urlConnector
	 */
	private UrlConnector urlConnector;

	/*
	 * The info List
	 */
	private List<Map<Integer, Map<Integer, Long>>> componentInfoList;


	public ExportMapDataBl(UrlConnector urlConnector) {
		this.urlConnector = urlConnector;
		this.componentInfoList = new ArrayList<>();
	}

	/**
	 * The BLs Public API
	 *
	 * @param worldPrefix
	 * @return The insert statements
	 */
	public String generateInsertSqlStatements(String worldPrefix) {
		fetchAndProcessWorldData(worldPrefix);
		StringBuilder sql = new StringBuilder();
		String mapAssetInsertSql = getMapAssetInsertSql();
		String mapComponentInsertSql = getMapComponentInsertSql();
		sql.append(mapAssetInsertSql);
		sql.append(mapComponentInsertSql);
		return sql.toString();
	}

	/*
	 * Private methods
	 */


	private String getMapAssetInsertSql() {
		StringBuilder sql = new StringBuilder();
		String lineSeparator = System.lineSeparator();
		sql.append("-- MapAsset" + lineSeparator + lineSeparator);
		MapAssetCr cr = new MapAssetCr();
		List<MapAsset> assets = cr.getAllMapAssets();
		for (MapAsset mapAsset : assets) {
			sql.append("INSERT INTO TW.MapAsset (mapAsset_id, NAME, ISOBSTACLE) VALUES (");
			sql.append(mapAsset.getId() + ", ");
			sql.append(getSqlValue(mapAsset.getName()) + ", ");
			sql.append(booleanToString(mapAsset.getObstacle()) + ");");
			sql.append(lineSeparator);
		}
		sql.append(lineSeparator);
		return sql.toString();
	}


	private String getMapComponentInsertSql() {
		StringBuilder sql = new StringBuilder();
		String lineSeparator = System.lineSeparator();
		sql.append("-- MapComponent" + lineSeparator + lineSeparator);
		for (Map<Integer, Map<Integer, Long>> componentMap : componentInfoList) {
			String currentInserts = processComponentMap(componentMap);
			sql.append(currentInserts);
			sql.append(lineSeparator);
		}
		return sql.toString();
	}

	private String processComponentMap(Map<Integer, Map<Integer, Long>> componentMap) {
		StringBuilder sql = new StringBuilder();
		String lineSeparator = System.lineSeparator();
		sql .append("INSERT INTO TW.MAPCOMPONENT (XPOS, YPOS, MAPASSET_ID) VALUES ");
		sql.append(lineSeparator);
		for (Entry<Integer, Map<Integer, Long>> e : componentMap.entrySet()) {
			Integer x = e.getKey();
			Map<Integer, Long> innerMap = e.getValue();
			for (Entry<Integer, Long> innerEntry : innerMap.entrySet()) {
				Integer y = innerEntry.getKey();
				Long assetId = innerEntry.getValue();
				sql.append("( ");
				sql.append(x+", ");
				sql.append(y+", ");
				sql.append(assetId+"),");
				sql.append(lineSeparator);
			}
		}
		String tmp = sql.toString().trim();
		return tmp.substring(0, tmp.length()-1) + ";";
	}

	private void fetchAndProcessWorldData(String worldPrefix) {
		List<InputStream> ins = urlConnector.getMapDataRequestInputStreams(worldPrefix);
		ObjectMapper mapper = new ObjectMapper();
		for (InputStream in : ins) {
			try {
				JsonNode currentNode = mapper.readTree(in);
				resolveJsonNode(currentNode);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void resolveJsonNode(JsonNode node) {
		Map<Integer, Map<Integer, Long>> componentMap = new LinkedHashMap<>();
		componentInfoList.add(componentMap);
		for (int i = 0; i < node.size(); i++) {
			JsonNode currentNode = node.get(i);
			int xOffset = currentNode.get("x").intValue();
			int yOffset = currentNode.get("y").intValue();
			JsonNode tiles = currentNode.get("tiles");
			for (int j = 0; j < 20; j++) {
				JsonNode currentTileNode = tiles.get(j);
				Integer xPos = xOffset + j;
				for (int k = 0; k < 20; k++) {
					JsonNode child = currentTileNode.get(k);
					int tileValue = child.intValue();
					Long v = Long.valueOf(tileValue);
					Integer yPos = yOffset + k;
					Map<Integer, Long> innerMap = componentMap.get(xPos);
					if (innerMap == null) {
						innerMap = new LinkedHashMap<>();
						innerMap.put(yPos, v);
						componentMap.put(xPos, innerMap);
					} else {
						innerMap.put(yPos, v);
					}
				}
			}
		}
	}


	private String booleanToString(Boolean value) {
		return Boolean.TRUE.equals(value) ? "1" : "0";
	}

	private String getSqlValue(Object value) {
		if (value == null) {
			return "null";
		}
		String s = value.toString();
		return "'" + s.replace("'", "''") + "'";
	}

}