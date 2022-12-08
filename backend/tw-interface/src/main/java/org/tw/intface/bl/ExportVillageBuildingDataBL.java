package org.tw.intface.bl;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.tw.intface.model.Village;
import org.tw.intface.util.DBConnector;
import org.tw.intface.util.UrlConnector;

/**
 * BL for exporting village building data. Prepares a mapping of each Village to
 * its points, which can be uses by the ResolvePointsBL
 */
public class ExportVillageBuildingDataBL {

	/*
	 * Constants
	 */
	private static final Integer MAX_POINTS = 12128;
	private static final String LINE_SEPARATOR = System.lineSeparator();


	/*
	 * BLs
	 */
	private ExportPlayerDataBL playerDataBL;
	private DBConnector dbConnector;

	/*
	 * Maps
	 */
	private Map<Village, Integer> villagePointsMap;
	private Map<String, Long> buildingTranslationMap;

	/*
	 * SQL
	 */
	private String playerVillageSql;
	private String nonPlayerSql;
	private String insertScript;


	/**
	 * Instantiates the BL
	 *
	 * @param urlConnector
	 * @param worldPrefix
	 */
	public ExportVillageBuildingDataBL(UrlConnector urlConnector, String worldPrefix) {
		this.dbConnector = new DBConnector();
		this.buildingTranslationMap = new ExportBuildingPropBl(getDE197ConfigMap()).getBuildingTranslationMap();
		this.playerDataBL = new ExportPlayerDataBL(urlConnector, worldPrefix);
		this.villagePointsMap = playerDataBL.getVillagePointsMap();
		executeExport();
	}

	private void executeExport() {
		Map<Long, List<Village>> playerIdVillageMap = generatePlayerVillageMap();
		List<Village> toBeProcessed = new ArrayList<>(villagePointsMap.keySet());
		toBeProcessed.removeIf(v -> v.getId() == null);
		// Process player villages
		this.playerVillageSql = processPlayerVillages(playerIdVillageMap, toBeProcessed);
		this.nonPlayerSql = processNonPlayerVillages(toBeProcessed);
		this.insertScript = buildInsertScript();
	}

	private String processNonPlayerVillages(List<Village> toBeProcessed) {
		StringBuilder sql = new StringBuilder();
		for (Village village : toBeProcessed) {
			int points = villagePointsMap.get(village);
			boolean statue = points > 100;
			sql.append(getInsertSql(village.getId(), points, statue));
		}
		return sql.toString();
	}

	private String processPlayerVillages(Map<Long, List<Village>> playerIdVillageMap, List<Village> toBeProcessed) {
		StringBuilder sql = new StringBuilder();
		for (Entry<Long, List<Village>> e : playerIdVillageMap.entrySet()) {
			List<Village> playerVillages = e.getValue();
			if (playerVillages.size() == 1) {
				boolean useStatue  = true;
				Village next = playerVillages.iterator().next();
				Integer points = villagePointsMap.get(next);
				if (points < 100) {
					useStatue = false;
				}
				String insertSql = getInsertSql(next.getId(), points, useStatue);
				sql.append(insertSql);
			}else {
				List<Entry<Village, Integer>> subList = new ArrayList<>();
				playerVillages.stream().forEach(v -> subList.add(new AbstractMap.SimpleEntry<>(v, villagePointsMap.get(v))));
				// Check if max value is present
				Entry<Village, Integer> maxEntry = Collections.max(subList, Entry.comparingByValue());
				Integer maxVal = maxEntry.getValue();
				Entry<Village, Integer> statueEntry = null;
				if (MAX_POINTS.equals(maxVal)) {
					statueEntry = maxEntry;
				}else {
					Optional<Entry<Village, Integer>> opt = subList.stream().filter(entry -> entry.getValue() > 2800 && entry.getValue() < 10000).findFirst();
					statueEntry = opt.isPresent() ? opt.get() : subList.iterator().next();
				}
				String insertSql = getInsertSql(statueEntry.getKey().getId(), statueEntry.getValue(), true);
				sql.append(insertSql);
				subList.remove(statueEntry);
				for (Entry<Village, Integer> entry : subList) {
					Village village = entry.getKey();
					Integer points = entry.getValue();
					sql.append(getInsertSql(village.getId(), points, false));
				}
			}
			toBeProcessed.removeAll(playerVillages);
		}
		return sql.toString();
	}

	private String getInsertSql(Long villageId, Integer points, boolean useStatue) {
		String query = "SELECT CONFIG FROM GENERAL.POINTS_CONFIG WHERE POINT_NUM = " + points + " AND IS_STATUE = " + (useStatue ? "1" : "0");
		Object[][] result = dbConnector.getResultMatrixFromSql(query);
		int length = result.length;
		if (length == 0) {
			query = "SELECT CONFIG FROM GENERAL.POINTS_CONFIG WHERE POINT_NUM = " + points + " AND IS_STATUE = " + (!useStatue ? "1" : "0");
			result = dbConnector.getResultMatrixFromSql(query);
			length = result.length;
			if (length == 0) {
				throw new IllegalStateException("No results found! " + query);
			}
		}
		int picked = ThreadLocalRandom.current().nextInt(length);
		Object row = result[picked][0];
		String config = row.toString();
		return processVillageConfigToSql(villageId, config);
	}

	private String processVillageConfigToSql(Long villageId, String config) {
		StringBuilder sql = new StringBuilder();
		String[] split = config.split(",");
		sql.append("INSERT INTO TW.VILLAGE_BUILDING (VILLAGE_ID, BUILDING_ID, LEVEL) VALUES ");
		sql.append(LINE_SEPARATOR);
		for (String part : split) {
			String[] exp = part.split("_");
			String building = exp[0].trim();
			String lvl = exp[1];
			Long buildingId = buildingTranslationMap.get(building);
			sql.append(" (");
			sql.append(villageId + ",");
			sql.append(buildingId + ",");
			sql.append(lvl + "),");
			sql.append(LINE_SEPARATOR);
		}
		String s = sql.toString();
		int lastIndexOf = s.lastIndexOf(',');
		return s.substring(0, lastIndexOf)+";" + LINE_SEPARATOR;
	}

	private Map<Long, List<Village>> generatePlayerVillageMap() {
		Map<Long, List<Village>> playerIdVillageMap = new HashMap<>();
		for (Entry<Village, Integer> e : villagePointsMap.entrySet()) {
			Village village = e.getKey();
			Long playerId = village.getPlayerId();
			if (playerId != null) {
				List<Village> villages = playerIdVillageMap.get(playerId);
				if (villages == null) {
					villages = new ArrayList<>();
					villages.add(village);
					playerIdVillageMap.put(playerId, villages);
				}else {
					villages.add(village);
				}
			}
		}
		return playerIdVillageMap;
	}

	private String buildInsertScript() {
		StringBuilder sql = new StringBuilder();
		sql.append(playerVillageSql);
		sql.append(nonPlayerSql);
		return sql.toString();
	}

	private Map<String, Boolean> getDE197ConfigMap() {
		Map<String, Boolean> configMap = new HashMap<>();
		configMap.put("WATCHTOWER", Boolean.FALSE);
		configMap.put("CHURCH", Boolean.FALSE);
		configMap.put("CHURCH_FIRST", Boolean.FALSE);
		configMap.put("STATUE", Boolean.TRUE);
		configMap.put("HIDING_PLACE", Boolean.FALSE);
		configMap.put("GOLD_COIN", Boolean.TRUE);
		return configMap;
	}

	/*
	 * Getters and Setters
	 */

	public String getInsertScript() {
		return insertScript;
	}

}