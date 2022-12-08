package org.tw.intface.bl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tw.intface.model.Property;

/**
 * The ExportBuildingPropBl
 */
public class ExportBuildingPropBl {

	/*
	 * Constatns
	 */
	private static final String LINE_SEPARATOR = System.lineSeparator();
	private static final String ACADEMY = "ACADEMY";
	private static final String FARM_5 = "FARM_5";
	private static final String MAIN_5 = "MAIN_5";
	private static final String BARRACKS_1 = "BARRACKS_1";
	private static final String MAIN_10 = "MAIN_10";
	private static final String MAIN_3 = "MAIN_3";

	/*
	 * The logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(ExportBuildingPropBl.class);


	/*
	 * The building lvl properties map (Name, Level, property Key) --> property Value
	 */
	private Map<String, Map<Integer, Map<String, Object>>> buildingLvlPropertiesMap;
	private Map<String, List<String>> buildConditionMap;
	private Map<String, Map<Integer, Integer>> buildingPointsMap;
	private Map<String, Map<Integer, Integer>> pointsMap;
	private Map<String, Long> buildingTranslationMap;

	/*
	 * The generated insert statements
	 */
	private String buildingInsertSql;
	private String buildingPointsSumInsertSql;
	private String propertyInsertSql;
	private String buildingPropertyInsertSql;
	private String buildingLevelPropertyInsertSql;
	private String insertScript;

	/**
	 * Instantiates the BL
	 * @param configMap
	 */
	public ExportBuildingPropBl(Map<String, Boolean> configMap) {
		this.buildingTranslationMap = new HashMap<>();
		this.buildingLvlPropertiesMap = new LinkedHashMap<>();
		this.buildConditionMap = generateBuildConditionMap();
		this.parseBuildingPropertiesData();
		this.pointsMap = generatePointsMapByConfigMap(configMap);
		this.executeExport();
	}

	private void executeExport() {
		this.buildingInsertSql = generateBuildingInsertSql();
		this.buildingPointsSumInsertSql = generateBuildingPointSumInsertSql();
		this.propertyInsertSql = generatePropertyInsertSql();
		this.buildingPropertyInsertSql = generateBuildingPropertyInsertSql();
		this.buildingLevelPropertyInsertSql = generateBuildinLevelPropertyInsertSql();
		this.insertScript = buildInsertScript();
	}

	private String generateBuildingPointSumInsertSql() {
		StringBuilder sql = new StringBuilder();

		for (Entry<String, Map<Integer, Integer>> e : pointsMap.entrySet()) {
			sql.append("INSERT INTO GENERAL.BUILDING_POINT_SUM (BUILDING_ID, LEVEL, SUM) VALUES ");
			sql.append(LINE_SEPARATOR);
			String building = e.getKey();
			Map<Integer, Integer> value = e.getValue();
			for (Entry<Integer, Integer> innerEntry : value.entrySet()) {
				Integer lvl = innerEntry.getKey();
				Integer sum = innerEntry.getValue();
				Long buildingId = buildingTranslationMap.get(building);
				sql.append("(");
				sql.append(buildingId + ", " + lvl +", " + sum);
				sql.append("),");
				sql.append(LINE_SEPARATOR);
			}
			sql = new StringBuilder(sql.substring(0, sql.length()-3));
			sql.append(";");
			sql.append(LINE_SEPARATOR + LINE_SEPARATOR);
		}
		return sql.toString();
	}

	/**
	 * Generates the points map
	 * @param configMap the worlds config
	 * @return The points map containing accumulated points per building and level
	 */
	private Map<String, Map<Integer, Integer>> generatePointsMapByConfigMap(Map<String, Boolean> configMap) {
		buildingLvlPropertiesMap.remove("RALLY_POINT");
		Map<String, Map<Integer, Integer>> targetMap = new LinkedHashMap<>();
		for (Entry<String, Map<Integer, Map<String, Object>>> e : buildingLvlPropertiesMap.entrySet()) {
			String key = e.getKey();
			Boolean isActive = configMap.get(key);
			if (Boolean.FALSE.equals(isActive)) {
				continue;
			}
			Map<Integer, Integer> map = new LinkedHashMap<>();
			targetMap.put(key, map);
			Map<Integer, Map<String, Object>> value = e.getValue();
			Integer currentSum = 0;
			for (Entry<Integer, Map<String, Object>> e2 : value.entrySet()) {
				Integer lvl = e2.getKey();
				int numPoints = Integer.parseInt(e2.getValue().get("POINTS").toString());
				currentSum += numPoints;
				map.put(lvl, currentSum);
				boolean isGoldCoin = ACADEMY.equals(key) && Boolean.TRUE.equals(configMap.get("GOLD_COIN"));
				if (isGoldCoin) {
					break;
				}
			}
		}
		return targetMap;
	}

	private void parseBuildingPropertiesData() {
		buildingPointsMap = parseBuildingPointsData();
		for (String key :  buildingPointsMap.keySet()) {
			try (InputStream in = getClass().getClassLoader().getResourceAsStream("building-data/" + key.toLowerCase() + ".csv")) {
				String csv = IOUtils.toString(in, StandardCharsets.UTF_8);
				processBuildingProperties(csv, key);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Parses the building points data
	 * @return Map which maps (Name , Level) --> Points
	 */
	private Map<String, Map<Integer, Integer>> parseBuildingPointsData() {
		Map<String, Map<Integer, Integer>> result = new LinkedHashMap<>();
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("building-data/points.csv")) {
			String csv = IOUtils.toString(in, StandardCharsets.UTF_8);
			String[] lines = csv.split("\\r?\\n");
			String header = lines[0];
			String[] buildings = header.split(";");
			Map<Integer, String> indexMap = prepareIndexMap(buildings);
			for (int row = 1; row < lines.length; row++) {
				String currentRow = lines[row];
				String[] values = currentRow.split(";");
				for (int col = 1; col < values.length; col++) {
					String building = indexMap.get(col);
					String v = values[col];
					boolean numeric = StringUtils.isNumeric(v);
					Integer points = numeric ? Integer.parseInt(v) : null;
					//(Building , Level) --> Points
					Map<Integer, Integer> innerMap = result.get(building);
					if (innerMap == null) {
						innerMap = new LinkedHashMap<>();
						innerMap.put(row, points);
						result.put(building, innerMap);
					}else if (numeric) {
						innerMap.put(row, points);
					}
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	private Map<Integer, String> prepareIndexMap(String[] buildings) {
		Map<Integer, String> indexMap = new HashMap<>();
		for (int column = 1; column < buildings.length; column++) {
			String building = buildings[column];
			indexMap.put(column, building);
		}
		return indexMap;
	}

	private void processBuildingProperties( String csv, String building) {
		Map<Integer, Map<String, Object>> map = new LinkedHashMap<>();
		buildingLvlPropertiesMap.put(building, map);
		String[] lines = csv.split("\\r?\\n");
		String header = lines[0];
		String[] props = header.split(";");
		Map<Integer, String> propMap = new HashMap<>();
		for (int i = 1; i < props.length; i++) {
			String prop = props[i];
			propMap.put(i, prop);
		}
		for (int row = 1; row < lines.length; row++) {
			String line = lines[row];
			Map<String, Object> innerMap = map.get(row);
			if (innerMap == null) {
				innerMap = new LinkedHashMap<>();
				map.put(row, innerMap);
			}
			int points = buildingPointsMap.get(building).get(row);
			innerMap.put("POINTS", points);
			String[] cols = line.split(";");
			for (int col = 1; col < cols.length; col++) {
				String property = propMap.get(col);
				String value = cols[col];
				innerMap.put(property, value);
				logger.debug("{} {} {} {}", building, row , property , value);
			}
		}
	}

	private String buildInsertScript() {
		StringBuilder sql = new StringBuilder();
		sql.append("-- GENERAL.BUILDING" + LINE_SEPARATOR);
		sql.append(buildingInsertSql);
		sql.append("-- GENERAL.BUILDING_POINT_SUM" + LINE_SEPARATOR);
		sql.append(buildingPointsSumInsertSql);
		sql.append("-- GENERAL.PROPERTY" + LINE_SEPARATOR);
		sql.append(propertyInsertSql);
		sql.append("-- GENERAL.BUILDING_PROPERTY" + LINE_SEPARATOR);
		sql.append(buildingPropertyInsertSql);
		sql.append("-- GENERAL.BUILDING_LVL_PROPERTY" + LINE_SEPARATOR);
		sql.append(buildingLevelPropertyInsertSql);
		return sql.toString();
	}

	private String generateBuildinLevelPropertyInsertSql() {
		StringBuilder sql = new StringBuilder();
		int buildingId = 0;
		for (Entry<String, Map<Integer, Map<String, Object>>> e : buildingLvlPropertiesMap.entrySet()) {
			Map<Integer, Map<String, Object>> levelMap = e.getValue();
			for (Entry<Integer, Map<String, Object>> e2 : levelMap.entrySet()) {
				Integer level = e2.getKey();
				Map<String, Object> innerMap = e2.getValue();
				for (Entry<String, Object> e3 : innerMap.entrySet()) {
					String propKey = e3.getKey();
					Object propVal = e3.getValue();
					sql.append("INSERT INTO GENERAL.Building_lvl_property (Building_id, Prop_key,Prop_val, Level) VALUES(");
					sql.append(buildingId+", ");
					sql.append(getSqlValue(propKey)+", ");
					sql.append(getSqlValue(propVal)+", ");
					sql.append(level+"); ");
					sql.append(LINE_SEPARATOR);
					logger.info("{} {}" , propKey , propVal);
				}
			}
			++buildingId;
		}
		return sql.toString();
	}

	private String generatePropertyInsertSql() {
		StringBuilder sql = new StringBuilder();
		List<String> allConditions = getAllBuildConditions();
		for (String condition : allConditions) {
			sql.append("INSERT INTO GENERAL.PROPERTY(Prop_key, Prop_val) VALUES ('BUILD_CONDITION' , '" + condition + "');");
			sql.append(LINE_SEPARATOR);
		}
		sql.append(LINE_SEPARATOR);
		List<Property> properties = new ArrayList<>();
		for (Entry<String, Map<Integer, Map<String, Object>>> e : buildingLvlPropertiesMap.entrySet()) {
			Map<Integer, Map<String, Object>> levelMap = e.getValue();
			for (Entry<Integer, Map<String, Object>> e2 : levelMap.entrySet()) {
				Map<String, Object> innerMap = e2.getValue();
				for (Entry<String, Object> e3 : innerMap.entrySet()) {
					String propKey = e3.getKey();
					Object propVal = e3.getValue();
					Property currentProperty = new Property();
					currentProperty.setKey(propKey);
					currentProperty.setValue(propVal.toString());
					if (! properties.contains(currentProperty)) {
						sql.append("INSERT INTO GENERAL.Property (Prop_key, Prop_val) VALUES (");
						sql.append(getSqlValue(propKey)+", ");
						sql.append(getSqlValue(propVal)+"); ");
						sql.append(LINE_SEPARATOR);
						properties.add(currentProperty);
					}
				}
			}
		}
		return sql.toString();
	}

	private String generateBuildingPropertyInsertSql() {
		StringBuilder sql = new StringBuilder();
		int id = 0;
		for (String key : buildingLvlPropertiesMap.keySet()) {
			List<String> conditions = buildConditionMap.get(key);
			if (conditions != null) {
				for (String condition : conditions) {
					sql.append("INSERT INTO general.building_property(Building_id,Prop_key,Prop_val ) VALUES (");
					sql.append(id + ", ");
					sql.append("'BUILD_CONDITION', ");
					sql.append(getSqlValue(condition)+");");
					sql.append(LINE_SEPARATOR);
					logger.info("{} {}", id  , condition);
				}
			}
			++id;
		}
		return sql.toString();
	}

	private String generateBuildingInsertSql() {
		StringBuilder sql = new StringBuilder();
		Map<String, String> translationMap = getTranslationMap();
		Long id = 0L;
		for (String key : buildingLvlPropertiesMap.keySet()) {
			sql.append("INSERT INTO GENERAL.BUILDING (Building_id, Building_key, Expression) VALUES( ");
			sql.append(id+ ", ");
			sql.append(getSqlValue(key)+", ");
			sql.append(getSqlValue(translationMap.get(key))+");");
			sql.append(LINE_SEPARATOR);
			buildingTranslationMap.put(key, id);
			++id;
		}
		sql.append(LINE_SEPARATOR);
		return sql.toString();
	}

	private Map<String, String> getTranslationMap() {
		Map<String, String> translationMap = new HashMap<>();
		translationMap.put("MAIN", "Hauptgebäude");
		translationMap.put("BARRACKS", "Kaserne");
		translationMap.put("STABLE", "Stall");
		translationMap.put("GARAGE", "Werkstatt");
		translationMap.put("CHURCH", "Kirche");
		translationMap.put("CHURCH_FIRST", "Erste Kirche");
		translationMap.put("WATCHTOWER", "Wachturm");
		translationMap.put(ACADEMY, "Adelshof");
		translationMap.put("SMITHY", "Schmiede");
		translationMap.put("RALLY_POINT", "Versammlungsplatz");
		translationMap.put("STATUE", "Statue");
		translationMap.put("MARKET", "Marktplatz");
		translationMap.put("TIMBERCAMP", "Holzfällerlager");
		translationMap.put("CLAYPIT", "Lehmgrube");
		translationMap.put("IRONMINE", "Eisenmine");
		translationMap.put("FARM", "Bauernhof");
		translationMap.put("WAREHOUSE", "Speicher");
		translationMap.put("HIDING_PLACE", "Versteck");
		translationMap.put("WALL", "Wall");
		return translationMap;
	}

	private Map<String, List<String>> generateBuildConditionMap() {
		Map<String, List<String>> result = new LinkedHashMap<>();
		result.put("BARRACKS", Arrays.asList(MAIN_3));
		result.put("STABLE", Arrays.asList(MAIN_10, "BARRACKS_5", "SMITHY_5"));
		result.put("GARAGE", Arrays.asList(MAIN_10, "SMITHY_10"));
		result.put("SMITHY", Arrays.asList(MAIN_5, BARRACKS_1));
		result.put("MARKET", Arrays.asList(MAIN_3, "WAREHOUSE_2"));
		result.put("WALL", Arrays.asList(BARRACKS_1));
		result.put(ACADEMY, Arrays.asList("MAIN_20" , "SMITHY_20", "MARKET_10"));
		result.put("CHURCH", Arrays.asList(MAIN_5 , FARM_5));
		result.put("WATCHTOWER", Arrays.asList(MAIN_5 , FARM_5));
		return result;
	}


	private List<String> getAllBuildConditions(){
		List<String> allConditions = new ArrayList<>();
		allConditions.addAll(Arrays.asList(MAIN_3,MAIN_5,MAIN_10,"MAIN_20"));
		allConditions.addAll(Arrays.asList(BARRACKS_1, "BARRACKS_5","SMITHY_5","SMITHY_10","SMITHY_20","WAREHOUSE_2","MARKET_10",FARM_5));
		return allConditions;
	}

	private String getSqlValue(Object value) {
		if (value == null) {
			return "null";
		}
		String s = value.toString();
		return "'" + s.replace("'", "''") + "'";
	}


	/*
	 * Getters and setters
	 */

	public Map<String, List<String>> getBuildConditionMap() {
		return buildConditionMap;
	}

	public Map<String, Map<Integer, Integer>> getBuildingPointsMap() {
		return buildingPointsMap;
	}

	public Map<String, Map<Integer, Map<String, Object>>> getBuildingLvlPropertiesMap() {
		return buildingLvlPropertiesMap;
	}

	public Map<String, Long> getBuildingTranslationMap() {
		return buildingTranslationMap;
	}

	public String getBuildingInsertSql() {
		return buildingInsertSql;
	}

	public String getBuildingPropertyInsertSql() {
		return buildingPropertyInsertSql;
	}

	public String getBuildingLevelPropertyInsertSql() {
		return buildingLevelPropertyInsertSql;
	}

	public String getInsertScript() {
		return insertScript;
	}

	public String getPropertyInsertSql() {
		return propertyInsertSql;
	}

	public Map<String, Map<Integer, Integer>> getPointsMap() {
		return pointsMap;
	}

}