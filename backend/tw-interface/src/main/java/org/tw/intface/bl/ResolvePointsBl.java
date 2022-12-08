package org.tw.intface.bl;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.tw.intface.util.Pruner;
import org.tw.intface.util.Searcher;

/**
 * Resolves the building points
 */
public class ResolvePointsBl {

	private static final String LINE_SEPARATOR = System.lineSeparator();

	private Map<String, Map<Integer, Integer>> pointsMap;

	private String insertSql;

	/**
	 * Instantiates the BL
	 */
	public ResolvePointsBl(List<Integer> toBeResolvedPoints) {
		ExportBuildingPropBl buildingPropBl = new ExportBuildingPropBl(generateDefaultConfigMap());
		this.pointsMap =  buildingPropBl.getPointsMap();
		executeExport(toBeResolvedPoints);
	}

	private void executeExport(List<Integer> toBeResolvedPoints) {
		Map<Integer, Map<Boolean, List<Map<String, Integer>> >> pointsConfigMap = new LinkedHashMap<>();
		for (Integer points : toBeResolvedPoints) {
			List<Map<String, Integer>> statReps = resolveByPointsNr(points, true);
			List<Map<String, Integer>> noStatReps = resolveByPointsNr(points, false);
			Map<Boolean, List<Map<String, Integer>>> t = new HashMap<>();
			t.put(Boolean.TRUE, statReps);
			t.put(Boolean.FALSE, noStatReps);
			pointsConfigMap.put(points, t);
		}
		this.insertSql = convertToScript(pointsConfigMap);
	}

	private String convertToScript(Map<Integer, Map<Boolean, List<Map<String, Integer>>>> pointsConfigMap) {
		StringBuilder sql = new StringBuilder();
		for (Entry<Integer, Map<Boolean, List<Map<String, Integer>>>> e : pointsConfigMap.entrySet()) {
			Integer points = e.getKey();
			Map<Boolean, List<Map<String, Integer>>> configs = e.getValue();
			List<Map<String, Integer>> noStatueConfigs = configs.get(Boolean.FALSE);
			List<Map<String, Integer>> statueConfigs = configs.get(Boolean.TRUE);
			sql.append("-- POINT NUM " + points );
			sql.append(LINE_SEPARATOR + LINE_SEPARATOR);

			boolean hasStatueConfigs = ! statueConfigs.isEmpty();
			if (hasStatueConfigs) {
				sql.append("INSERT INTO GENERAL.POINTS_CONFIG( POINT_NUM, IS_STATUE, CONFIG) VALUES");
				sql.append(LINE_SEPARATOR);
			}
			for (int i = 0; i < statueConfigs.size(); i++) {
				boolean isLast = i == statueConfigs.size() -1;
				String suffix = isLast ? ";"+LINE_SEPARATOR : ",";
				Map<String, Integer> buildConfigMap = statueConfigs.get(i);
				sql.append("(");
				sql.append( points + ", 1, '" + getConfigExpression(buildConfigMap) + "'");
				sql.append(")" + suffix);
				sql.append(LINE_SEPARATOR);
			}
			boolean hasNoStatueConfigs = ! noStatueConfigs.isEmpty();
			if (hasNoStatueConfigs) {
				sql.append("INSERT INTO GENERAL.POINTS_CONFIG( POINT_NUM, IS_STATUE, CONFIG) VALUES");
				sql.append(LINE_SEPARATOR);
			}
			for (int i = 0; i < noStatueConfigs.size(); i++) {
				boolean isLast = i == noStatueConfigs.size() -1;
				String suffix = isLast ? ";"+LINE_SEPARATOR : ",";
				Map<String, Integer> buildConfigMap = noStatueConfigs.get(i);
				sql.append("(");
				sql.append( points + ", 0, '" + getConfigExpression(buildConfigMap) + "'");
				sql.append(")" + suffix);
				sql.append(LINE_SEPARATOR);
			}
		}
		return sql.toString();
	}

	private String getConfigExpression(Map<String, Integer> buildConfigMap) {
		Set<String> searchedBuildings = buildConfigMap.keySet();
		Set<String> notSearchedBuildings = pointsMap.keySet();
		notSearchedBuildings.removeAll(searchedBuildings);
		StringBuilder sql = new StringBuilder();
		for (String searchedBuilding : searchedBuildings) {
			Integer lvl = buildConfigMap.get(searchedBuilding);
			String exp = searchedBuilding + "_" + lvl;
			sql.append(exp + ", ");
		}
		for (String notSearchedBuilding : notSearchedBuildings) {
			Integer lvl = 0;
			String exp = notSearchedBuilding + "_" + lvl;
			sql.append(exp + ", ");
		}
		String s = sql.toString();
		return s.substring(0, s.length()-2);
	}

	/**
	 * Resolves the given point number
	 * @param points
	 * @param b
	 * @return the representations
	 */
	private List<Map<String, Integer>> resolveByPointsNr(int points, boolean useStatue) {
		Map<String, Double[]> controlParams = Pruner.generateDefaultControlParams(points);
		controlParams.put("STATUE", new Double[] {useStatue ? 1d : 0d , null});
		Pruner pruner = new Pruner(pointsMap, points, controlParams);
		List<List<Integer>> pointsMatrix = pruner.getPointsMatrix();
		Searcher searcher = new Searcher();
		searcher.executeDiffSearch(pruner, pointsMatrix, points, 0);
		List<List<Integer>> resultList = pruner.getResultList();
		List<Entry<List<Integer>, Double>> entryList = new ArrayList<>();
		for (List<Integer> result : resultList) {
			Double prob = pruner.calculateStackProb(result);
			entryList.add(new AbstractMap.SimpleEntry<>(result, prob));
		}
		entryList.sort(Entry.comparingByValue(Collections.reverseOrder()));
		List<List<Integer>> topResults =  getTopResults(entryList);
		List<Map<String, Integer>> reps = new ArrayList<>() ;
		for (List<Integer> result : topResults) {
			Map<String, Integer> repMap = pruner.generateStackIndexRepMap(result);
			reps.add(repMap);
		}
		return reps;
	}

	private List<List<Integer>> getTopResults(List<Entry<List<Integer>, Double>> entryList) {
		// Take the best 10
		return entryList.subList(0, Math.min(entryList.size(), 10)).stream().map(Entry::getKey).collect(Collectors.toList());
	}

	private Map<String, Boolean> generateDefaultConfigMap() {
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

	public String getInsertSql() {
		return insertSql;
	}

}