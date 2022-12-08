package org.tw.intface.util;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class which contains logic on pruning the search tree
 */
public class Pruner {

	/*
	 * The anchor buildings
	 */
	private static final List<String> anchorBuildings = Arrays.asList("MAIN", "WAREHOUSE", "FARM");

	/*
	 * The maximum number of allowed results
	 */
	private static final int MAX_VALUES = 1000;
	private static final int MAX_SEARCH_DEPTH = 7;

	/*
	 * The total points to be resolved
	 */
	private int totalPoints;

	/*
	 * The maps
	 */
	private Map<String, Double[]> controlParams;
	private Map<String, List<Integer>> searchMap;
	private Map<Integer, Map<Integer, Double>> stackIndexProbabilityMap;
	private Map<Integer, Integer[]> offSetMap;
	private Map<Integer, List<Integer>> distributionMap;
	private Map<String, List<String>> buildConditionMap;

	/*
	 * The search matrices
	 */
	private List<List<Integer>> pointsMatrix;
	private List<List<Double>> probMatrix;

	/*
	 * Max stack probability on MAX_SEARCH_DEPTH
	 */
	private Double maxProbForDeepestStack;

	/*
	 * The result List
	 */
	private List<List<Integer>> resultList;

	public Pruner(Map<String, Map<Integer, Integer>> pointsMap, int totalPoints) {
		this(pointsMap, totalPoints, null);
	}

	public Pruner(Map<String, Map<Integer, Integer>> pointsMap, int totalPoints, Map<String, Double[]> controlParams) {
		this.totalPoints = totalPoints;
		this.controlParams = controlParams;
		this.resultList = new ArrayList<>();
		this.pointsMatrix = new ArrayList<>();
		this.recursionStack = new ArrayList<>();
		this.searchMap = new LinkedHashMap<>();
		this.stackIndexProbabilityMap = new LinkedHashMap<>();
		this.offSetMap = new HashMap<>();
		this.buildConditionMap = generateBuildConditionMap();
		// Resolve the search Map, create probability matrix and create the target stack distribution
		this.resolveSearchMap(prepareSearchMap(pointsMap));
		this.probMatrix = convertProbMapToMatrix();
		this.maxProbForDeepestStack = calculateMaxProbForDepth();
		this.distributionMap = prepareStackDistributionMap();
	}

	/**
	 * Public API entry point for the validation of the current stack
	 * @param currentStack the current call stack
	 * @return True if branch should be pruned, false otherwise
	 */
	public boolean pruneBranch(List<Integer> currentStack) {
		// Prune if totalPoints not reachable for current stack
		if (!totalPointsReachable(currentStack)) {
			return true;
		}
		// Prune if stack - config not valid
		if (!validateConfig(currentStack)) {
			return true;
		}
		// Now we can validate the stack
		return stackValidationImpl(currentStack);
	}

	private boolean validateConfig(List<Integer> currentStack) {
		List<String> buildingList = generateBuildingList();
		for (int depth = 0; depth < currentStack.size(); depth++) {
			String currentKey = buildingList.get(depth);
			List<String> conditions = buildConditionMap.get(currentKey);
			if (conditions == null) {
				continue;
			}
			for (String condition : conditions) {
				String[] split = condition.split("_");
				String targetBld = split[0];
				int targetIndex = buildingList.indexOf(targetBld);
				if (targetIndex < 0 || targetIndex > currentStack.size()-1) {
					break;
				}
				Integer[] targetOffsets = offSetMap.get(targetIndex);
				Integer offset = targetOffsets[0];
				Integer actualLvl = currentStack.get(targetIndex) + offset;
				if (anchorBuildings.contains(targetBld)) {
					actualLvl += 1;
				}
				Integer targetLvl = Integer.parseInt(split[1]);
				if (actualLvl < targetLvl) {
					return false;
				}
			}
		}
		return true;
	}

	private Map<String, List<String>> generateBuildConditionMap() {
		Map<String, List<String>> result = new HashMap<>();
		result.put("BARRACKS", Arrays.asList("MAIN_3"));
		result.put("STABLE", Arrays.asList("MAIN_10", "BARRACKS_5", "SMITHY_5"));
		result.put("GARAGE", Arrays.asList("MAIN_10", "SMITHY_10"));
		result.put("SMITHY", Arrays.asList("MAIN_5", "BARRACKS_1"));
		result.put("MARKET", Arrays.asList("MAIN_3", "WAREHOUSE_2"));
		result.put("WALL", Arrays.asList("BARRACKS_1"));
		result.put("CHURCH", Arrays.asList("MAIN_5", "FARM_5"));
		result.put("WATCHTOWER", Arrays.asList("MAIN_5", "FARM_5"));
		result.put("ACADEMY", Arrays.asList("MAIN_20", "SMITHY_20", "MARKET_10"));
		return result;
	}

	/**
	 * Validates the stack
	 * @param currentStack
	 * @param currentDiff
	 * @return true if invalid, false otherwise
	 */
	private boolean stackValidationImpl(List<Integer> currentStack) {
		Integer currentIndex = resultList.size();
		List<Integer> targetStack = distributionMap.get(currentIndex);
		if (targetStack == null) {
			return true;
		}
		int min = Math.min(currentStack.size(), targetStack.size());
		boolean valid = compare(targetStack.subList(0, min), currentStack.subList(0, min));
		return ! valid;
	}

	private boolean compare(List<Integer> targetStack, List<Integer> stack) {
		for (int i = 0; i < targetStack.size(); i++) {
			Integer targetVal = targetStack.get(i);
			Integer stackVal = stack.get(i);
			if (stackVal < targetVal) {
				return true;
			}
			if (stackVal > targetVal) {
				return false;
			}
		}
		return true;
	}

	private Map<Integer, Double> calculateDefaultProps(double mu, double sigma, int minVal, int maxVal) {
		Map<Integer, Double> result = new LinkedHashMap<>();
		double d1 =  (mu - 3*sigma);
		double d2 =  (mu + 3*sigma);
		int from = d1 < minVal ? minVal : (int) d1;
		int to = d2 > maxVal ? maxVal : (int) d2;
		for (int i = from; i <= to; i++) {

			double prop = calculateProbability(mu, sigma, i);
			result.put(i, prop);
		}
		return result;
	}

	private double calculateProbability(double mu, double sigma, double value) {
		double factor1 = 1/(sigma * Math.sqrt(2*Math.PI));
		double num = -1 * Math.pow(value - mu,2);
		double d = 2*Math.pow(sigma, 2);
		double factor2 = Math.pow(Math.E, num/d);
		return factor1 * factor2;
	}

	private List<List<Double>> convertProbMapToMatrix() {
		List<List<Double>> result = new ArrayList<>();
		for (int depth = 0; depth < stackIndexProbabilityMap.size(); depth++) {
			Map<Integer, Double> map = stackIndexProbabilityMap.get(depth);
			result.add(new ArrayList<>(map.values()));
		}
		return result;
	}

	/**
	 *
	 * Resolves the search Map by generating a mapping of a stack index and its value to its corresponding probability.<br>
	 * Updates the searchMatrix to only contain valid indices (i.e. indices having  a probability)
	 * @param tmpSearchMap
	 * @return The StackIndexProbMap
	 *  (Depth, Stack-Value) --> probability
	 */
	private void resolveSearchMap(Map<String, List<Integer>> tmpSearchMap) {
		Map<String, Map<Integer, Double>> buildingProbMap = new LinkedHashMap<>();
		Map<String, Integer[]> offSetMapTmp = new HashMap<>();
		if (controlParams == null) {
			controlParams = generateDefaultControlParams(totalPoints);
		}
		for (Entry<String, List<Integer>> e : tmpSearchMap.entrySet()) {
			String building = e.getKey();
			Double[] params = controlParams.get(building);
			boolean isAnchor = anchorBuildings.contains(building);
			Double expectancyVal = isAnchor ? params[0] -1d : params[0];
			Double sigma = params[1];
			List<Integer> currentRow = tmpSearchMap.get(building);
			int maxVal = currentRow.size()-1;
			Map<Integer, Double> probMapShifted = null;
			if (sigma == null) {
				//There is only the expectancy value
				int lvl =expectancyVal.intValue();
				probMapShifted = Stream.of(new AbstractMap.SimpleEntry<>(lvl,1d)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			}else {
				probMapShifted =calculateDefaultProps(expectancyVal, sigma, 0, maxVal);
			}
			List<Integer> validIndices = new ArrayList<>(probMapShifted.keySet());
			int fromIndex = validIndices.get(0);
			int toIndex = fromIndex + validIndices.size();
			List<Integer> subList = new ArrayList<>(currentRow.subList(fromIndex, toIndex));
			tmpSearchMap.put(building, subList);
			offSetMapTmp.put(building, new Integer[] {fromIndex, toIndex});
			// Shift the keys
			Map<Integer, Double> stackIndexMap= new LinkedHashMap<>();
			probMapShifted.entrySet().forEach(entry -> stackIndexMap.put(entry.getKey() - fromIndex, entry.getValue()));
			buildingProbMap.put(building, stackIndexMap);
		}
		reOrderStack(tmpSearchMap, buildingProbMap, offSetMapTmp);
	}

	private void reOrderStack(Map<String, List<Integer>> tmpSearchMap, Map<String, Map<Integer, Double>> tmpStackIndexProbMap, Map<String, Integer[]> tmpOffSetMap) {
		// Convert maps to list of entries
		List<Entry<String, List<Integer>>> searchMapEntryList = new ArrayList<>();
		List<Entry<String, Map<Integer, Double>>> stackIndexProbMapEntryList = new ArrayList<>();
		List<Entry<String, Integer[]>> offSetMapEntryList = new ArrayList<>();
		tmpSearchMap.entrySet().stream().forEach(searchMapEntryList::add);
		searchMapEntryList.removeIf(e -> {
			List<Integer> row = e.getValue();
			return row.size() == 1 && row.iterator().next() == 0;
		});
		// Sort search map entries by AVG - value rated by standard deviation, descending
		searchMapEntryList.sort( (e1,e2) -> {
			String key1 = e1.getKey();
			Double sigma1 = controlParams.get(key1)[1];
			sigma1 = sigma1 == null ? 0d : sigma1;
			List<Integer> list1 = e1.getValue();
			String key2 = e2.getKey();
			Double sigma2 = controlParams.get(key2)[1];
			sigma2 = sigma2 == null ? 0d : sigma2;
			List<Integer> list2 = e2.getValue();
			OptionalDouble opt1 = list1.stream().mapToInt(i -> i).average();
			OptionalDouble opt2 = list2.stream().mapToInt(i -> i).average();
			Double avg1 = opt1.isPresent() ? opt1.getAsDouble() : 0d;
			Double avg2 = opt2.isPresent() ? opt2.getAsDouble() : 0d;
			avg1 *= sigma1;
			avg2 *= sigma2;
			return -1 * avg1.compareTo(avg2);
		});

		searchMapEntryList.stream().forEach(e -> {
			stackIndexProbMapEntryList.add(new AbstractMap.SimpleEntry<>(e.getKey(), tmpStackIndexProbMap.get(e.getKey())));
			offSetMapEntryList.add(new AbstractMap.SimpleEntry<>(e.getKey(), tmpOffSetMap.get(e.getKey())));
		});
		// Fill the search Map
		searchMapEntryList.stream().forEach(entry -> searchMap.put(entry.getKey(), entry.getValue()));

		for (int depth = 0; depth < stackIndexProbMapEntryList.size(); depth++) {
			stackIndexProbabilityMap.put(depth, stackIndexProbMapEntryList.get(depth).getValue());
			String key = stackIndexProbMapEntryList.get(depth).getKey();
			Integer[] value = tmpOffSetMap.get(key);
			offSetMap.put(depth, value);
		}
		// Fill the search Matrix
		searchMapEntryList.stream().forEach(e -> pointsMatrix.add(e.getValue()));
	}


	/**
	 *
	 * @return Collection of arrays with ARR[0]= MU ARR[1]= SIGMA
	 */
	public static Map<String, Double[]> generateDefaultControlParams(int points) {
		Map<String, Double[]> result = new HashMap<>();
		String main = "MAIN";
		String wareHouse = "WAREHOUSE";
		String farm = "FARM";
		String timberCamp = "TIMBERCAMP";
		String clayPit = "CLAYPIT";
		String ironMine = "IRONMINE";
		String statue = "STATUE";
		String wall = "WALL";
		String garage = "GARAGE";
		String smithy = "SMITHY";
		String stable = "STABLE";
		String market = "MARKET";
		String barracks = "BARRACKS";
		String academy = "ACADEMY";
		if (points < 60) {
			// SUM (MU) = 43
			result.put(main, new Double[] { 2d, 1d });
			result.put(timberCamp, new Double[] { 2d, 1d });
			result.put(clayPit, new Double[] { 2d, 1d });
			result.put(ironMine, new Double[] { 1d, 1d });
			result.put(wareHouse, new Double[] { 1d, 1d });
			result.put(farm, new Double[] { 1d, 1d });
			result.put(wall, new Double[] { 0d, 1d });
			result.put(statue, new Double[] { 0d, null });
			result.put(smithy, new Double[] { 0d, null });
			result.put(stable, new Double[] { 0d, null });
			result.put(market, new Double[] { 0d, null});
			result.put(barracks, new Double[] { 0d, null});
			result.put(garage, new Double[] { 0d, null });
			result.put(academy, new Double[] { 0d, null });
		}else if (points < 100) {
			// SUM (MU) = 82
			result.put(main, new Double[] { 4d, 1d });
			result.put(timberCamp, new Double[] { 4d, 1d });
			result.put(clayPit, new Double[] { 4d, 1d });
			result.put(ironMine, new Double[] { 2d, 1d });
			result.put(barracks, new Double[] { 2d, 1d});
			result.put(wareHouse, new Double[] { 3d, 1d });
			result.put(farm, new Double[] { 2d, 1d });
			result.put(wall, new Double[] { 0d, 1d });
			result.put(market, new Double[] { 0d, 1d});
			result.put(statue, new Double[] { 0d, null });
			result.put(garage, new Double[] { 0d, null });
			result.put(smithy, new Double[] { 0d, null });
			result.put(stable, new Double[] { 0d, null });
			result.put(academy, new Double[] { 0d, null });
		} else if (points < 200) {
			// SUM (MU) = 155
			result.put(main, new Double[] { 5d, 1d });
			result.put(timberCamp, new Double[] { 5d, 1.25d });
			result.put(clayPit, new Double[] { 5d, 1.25d });
			result.put(ironMine, new Double[] { 3d, 1.25d });
			result.put(wareHouse, new Double[] { 3d, 1d });
			result.put(barracks, new Double[] { 3d, 1d});
			result.put(farm, new Double[] { 2d, 1d });
			result.put(wall, new Double[] { 1d, 1d });
			result.put(smithy, new Double[] { 1d, 1d });
			result.put(market, new Double[] { 2d, 1d});
			result.put(statue, new Double[] { 1d, null });
			result.put(garage, new Double[] { 0d, null });
			result.put(stable, new Double[] { 0d, null });
			result.put(academy, new Double[] { 0d, null });
		} else if (points < 300) {
			// SUM (MU) = 247
			result.put(main, new Double[] { 7d, 1d });
			result.put(timberCamp, new Double[] { 9d, 2d });
			result.put(clayPit, new Double[] { 9d, 2d });
			result.put(ironMine, new Double[] { 7d, 2d });
			result.put(wall, new Double[] { 5d, 1d });
			result.put(wareHouse, new Double[] { 3d, 1d });
			result.put(farm, new Double[] { 3d, 1d });
			result.put(smithy, new Double[] { 5d, 1d });
			result.put(market, new Double[] { 5d, 1d});
			result.put(barracks, new Double[] { 5d, 1d});
			result.put(statue, new Double[] { 1d, null });
			result.put(garage, new Double[] { 0d, null });
			result.put(stable, new Double[] { 0d, null });
			result.put(academy, new Double[] { 0d, null });
		} else if (points < 400) {
			// SUM (MU) = 318
			result.put(main, new Double[] { 8d, 1d });
			result.put(timberCamp, new Double[] { 12d, 1d });
			result.put(clayPit, new Double[] { 12d, 1d });
			result.put(ironMine, new Double[] { 10d, 1d });
			result.put(wareHouse, new Double[] { 6d, 1d });
			result.put(farm, new Double[] { 6d, 1d });
			result.put(smithy, new Double[] { 5d, 1d });
			result.put(market, new Double[] { 5d, 1d });
			result.put(stable, new Double[] { 0d, 1d });
			result.put(barracks, new Double[] { 5d, null });
			result.put(wall, new Double[] { 5d, null });
			result.put(statue, new Double[] { 1d, null });
			result.put(garage, new Double[] { 0d, null });
			result.put(academy, new Double[] { 0d, null });
		} else if (points < 500) {
			// SUM (MU) = 417
			result.put(main, new Double[] { 10d, 1d });
			result.put(timberCamp, new Double[] { 12d, 2d });
			result.put(clayPit, new Double[] { 12d, 2d });
			result.put(ironMine, new Double[] { 10d, 2d });
			result.put(wareHouse, new Double[] { 10d, 1d });
			result.put(farm, new Double[] { 10d, 1d });
			result.put(smithy, new Double[] { 6d, null });
			result.put(stable, new Double[] { 2d, 1d });
			result.put(barracks, new Double[] { 8d, null });
			result.put(market, new Double[] { 5d, null });
			result.put(wall, new Double[] { 5d, null });
			result.put(statue, new Double[] { 1d, null });
			result.put(garage, new Double[] { 0d, null });
			result.put(academy, new Double[] { 0d, null });
		} else if (points < 650) {
			// SUM (MU) = 574
			result.put(main, new Double[] { 12d, 1d });
			result.put(timberCamp, new Double[] { 15d, 1d });
			result.put(clayPit, new Double[] {15d, 1d });
			result.put(ironMine, new Double[] { 13d, 1d });
			result.put(wareHouse, new Double[] { 13d, 1d });
			result.put(farm, new Double[] { 14d, 1d });
			result.put(wall, new Double[] { 5d, 1d });
			result.put(smithy, new Double[] { 5d, 1d });
			result.put(stable, new Double[] { 3d, 1d });
			result.put(barracks, new Double[] { 8d, 1d});
			result.put(market, new Double[] { 5d, null});
			result.put(statue, new Double[] { 1d, null });
			result.put(garage, new Double[] { 0d, null });
			result.put(academy, new Double[] { 0d, null });
		}else if (points < 825) {
			// SUM (MU) = 732
			result.put(main, new Double[] { 13d, 1d });
			result.put(timberCamp, new Double[] { 16d, 1d });
			result.put(clayPit, new Double[] {16d, 1d });
			result.put(ironMine, new Double[] { 14d, 1d });
			result.put(wareHouse, new Double[] { 15d, 1d });
			result.put(farm, new Double[] { 15d, 1d });
			result.put(wall, new Double[] { 5d, 1d });
			result.put(smithy, new Double[] { 8d, 1d });
			result.put(stable, new Double[] { 5d, 1d });
			result.put(barracks, new Double[] { 10d, 1d});
			result.put(garage, new Double[] { 0d, null });
			result.put(market, new Double[] { 5d, null});
			result.put(statue, new Double[] { 1d, null });
			result.put(academy, new Double[] { 0d, null });
		}
		else if (points < 1000) {
			// SUM (MU) = 880
			result.put(main, new Double[] { 14d, 1d });
			result.put(timberCamp, new Double[] { 17d, 1d });
			result.put(clayPit, new Double[] {17d, 1d });
			result.put(ironMine, new Double[] { 15d, 1d });
			result.put(wareHouse, new Double[] { 15d, 1d });
			result.put(farm, new Double[] { 15d, 1d });
			result.put(wall, new Double[] { 7d, 1d });
			result.put(stable, new Double[] { 5d, 1d });
			result.put(barracks, new Double[] { 10d, 1d});
			result.put(smithy, new Double[] { 11d, 1d });
			result.put(garage, new Double[] { 2d, null });
			result.put(market, new Double[] { 5d, null});
			result.put(statue, new Double[] { 1d, null });
			result.put(academy, new Double[] { 0d, null });
		} else if (points < 1500) {
			// SUM (MU) = 1184
			result.put(main, new Double[] { 17d, 1.5d });
			result.put(timberCamp, new Double[] { 19d, 1d });
			result.put(clayPit, new Double[] {19d, 1d });
			result.put(ironMine, new Double[] { 17d, 1d });
			result.put(wareHouse, new Double[] { 16d, 1d });
			result.put(farm, new Double[] { 17d, 1d });
			result.put(barracks, new Double[] { 10d, 1d});
			result.put(smithy, new Double[] { 13d, 1d });
			result.put(stable, new Double[] { 7d, null });
			result.put(wall, new Double[] { 10d, null });
			result.put(garage, new Double[] { 2d, null });
			result.put(market, new Double[] { 5d, null});
			result.put(statue, new Double[] { 1d, null });
			result.put(academy, new Double[] { 0d, null });
		} else if (points < 2000) {
			// SUM (MU) = 1536
			result.put(main, new Double[] { 19d, 1d });
			result.put(timberCamp, new Double[] { 21d, 1d });
			result.put(clayPit, new Double[] {21d, 1d });
			result.put(ironMine, new Double[] { 19d, 1d });
			result.put(wareHouse, new Double[] { 18d, 1d });
			result.put(farm, new Double[] { 19d, 1d });
			result.put(barracks, new Double[] { 10d, 1d});
			result.put(smithy, new Double[] { 14d, 1d });
			result.put(stable, new Double[] { 8d, null });
			result.put(wall, new Double[] { 12d, null });
			result.put(garage, new Double[] { 2d, null });
			result.put(market, new Double[] { 5d, null});
			result.put(statue, new Double[] { 1d, null });
			result.put(academy, new Double[] { 0d, null });
		} else if (points < 2500) {
			// SUM (MU) = 2132
			result.put(timberCamp, new Double[] { 22d, 1d });
			result.put(clayPit, new Double[] {22d, 1d });
			result.put(ironMine, new Double[] { 20d, 1d });
			result.put(wareHouse, new Double[] { 20d, 1d });
			result.put(farm, new Double[] { 21d, 1d });
			result.put(barracks, new Double[] { 13d, 1d});
			result.put(smithy, new Double[] { 16d, 1d });
			result.put(main, new Double[] { 20d, null });
			result.put(stable, new Double[] { 10d, null });
			result.put(wall, new Double[] { 13d, null });
			result.put(garage, new Double[] { 2d, null });
			result.put(market, new Double[] { 10d, null});
			result.put(statue, new Double[] { 1d, null });
			result.put(academy, new Double[] { 0d, null });
		} else if (points < 3000) {
			// SUM (MU) =
			result.put(timberCamp, new Double[] { 23d, 1d });
			result.put(clayPit, new Double[] {23d, 1d });
			result.put(ironMine, new Double[] { 22d, 1d });
			result.put(wareHouse, new Double[] { 21d, 1d });
			result.put(farm, new Double[] { 22d, 1d });
			result.put(barracks, new Double[] { 15d, 1d});
			result.put(smithy, new Double[] { 19d, 1d });
			result.put(main, new Double[] { 20d, null });
			result.put(stable, new Double[] { 10d, null });
			result.put(wall, new Double[] { 14d, null });
			result.put(garage, new Double[] { 2d, null });
			result.put(market, new Double[] { 10d, null});
			result.put(statue, new Double[] { 1d, null });
			result.put(academy, new Double[] { 0d, null });
		} else if (points < 3500) {
			// SUM (MU) =
			result.put(timberCamp, new Double[] { 23d, 1d });
			result.put(clayPit, new Double[] {23d, 1d });
			result.put(ironMine, new Double[] { 22d, 1d });
			result.put(wareHouse, new Double[] { 21d, 1d });
			result.put(farm, new Double[] { 22d, 1d });
			result.put(barracks, new Double[] { 15d, 1d});
			result.put(stable, new Double[] { 10d, 1d });
			result.put(wall, new Double[] { 15d, 1d });
			result.put(market, new Double[] { 10d, null});
			result.put(smithy, new Double[] { 20d, null });
			result.put(main, new Double[] { 20d, null });
			result.put(garage, new Double[] { 2d, null });
			result.put(statue, new Double[] { 1d, null });
			result.put(academy, new Double[] { 1d, null });
		} else if (points < 4000) {
			// SUM (MU) =
			result.put(timberCamp, new Double[] { 24d, 1d });
			result.put(clayPit, new Double[] {24d, 1d });
			result.put(ironMine, new Double[] { 23d, 1d });
			result.put(wareHouse, new Double[] { 21d, 1d });
			result.put(farm, new Double[] { 23d, 1d });
			result.put(barracks, new Double[] { 16d, 1d});
			result.put(stable, new Double[] { 12d, 1d });
			result.put(wall, new Double[] { 17d, 1d });
			result.put(market, new Double[] { 10d, null});
			result.put(main, new Double[] { 20d, null });
			result.put(smithy, new Double[] { 20d, null });
			result.put(garage, new Double[] { 2d, null });
			result.put(statue, new Double[] { 1d, null });
			result.put(academy, new Double[] { 1d, null });
		} else if (points < 5000) {
			// SUM (MU) =
			result.put(timberCamp, new Double[] { 25d, 1d });
			result.put(clayPit, new Double[] {25d, 1d });
			result.put(ironMine, new Double[] { 24d, 1d });
			result.put(wareHouse, new Double[] { 22d, 1d });
			result.put(farm, new Double[] { 24d, 1d });
			result.put(barracks, new Double[] { 19d, 1d});
			result.put(stable, new Double[] { 14d, 1d });
			result.put(market, new Double[] { 11d, 1d});
			result.put(wall, new Double[] { 20d, null });
			result.put(main, new Double[] { 20d, null });
			result.put(smithy, new Double[] { 20d, null });
			result.put(garage, new Double[] { 2d, null });
			result.put(statue, new Double[] { 1d, null });
			result.put(academy, new Double[] { 1d, null });
		} else if (points < 6000) {
			// SUM (MU) =
			result.put(timberCamp, new Double[] { 26d, 1d });
			result.put(clayPit, new Double[] {26d, 1d });
			result.put(ironMine, new Double[] { 25d, 1d });
			result.put(wareHouse, new Double[] { 23d, 1d });
			result.put(farm, new Double[] { 24d, 1d });
			result.put(barracks, new Double[] { 22d, 1d});
			result.put(stable, new Double[] { 16d, 1d });
			result.put(market, new Double[] { 11d, 1d});
			result.put(wall, new Double[] { 20d, null });
			result.put(main, new Double[] { 20d, null });
			result.put(smithy, new Double[] { 20d, null });
			result.put(garage, new Double[] { 5d, null });
			result.put(statue, new Double[] { 1d, null });
			result.put(academy, new Double[] { 1d, null });
		} else if (points < 7000) {
			// SUM (MU) =
			result.put(timberCamp, new Double[] { 28d, 1d });
			result.put(clayPit, new Double[] {28d, 1d });
			result.put(ironMine, new Double[] { 28d, 1d });
			result.put(wareHouse, new Double[] { 25d, 1d });
			result.put(farm, new Double[] { 26d, 1d });
			result.put(barracks, new Double[] { 23d, 1d});
			result.put(stable, new Double[] { 17d, 1d });
			result.put(market, new Double[] { 11d, 1d});
			result.put(wall, new Double[] { 20d, null });
			result.put(main, new Double[] { 20d, null });
			result.put(smithy, new Double[] { 20d, null });
			result.put(garage, new Double[] { 5d, null });
			result.put(statue, new Double[] { 1d, null });
			result.put(academy, new Double[] { 1d, null });
		} else if (points < 8000) {
			// SUM (MU) =
			result.put(timberCamp, new Double[] { 29d, 1d });
			result.put(clayPit, new Double[] {29d, 1d });
			result.put(ironMine, new Double[] { 29d, 1d });
			result.put(wareHouse, new Double[] { 25d, 1d });
			result.put(farm, new Double[] { 27d, 1d });
			result.put(barracks, new Double[] { 23d, 1d});
			result.put(stable, new Double[] { 18d, 1d });
			result.put(market, new Double[] { 11d, 1d});
			result.put(wall, new Double[] { 20d, null });
			result.put(main, new Double[] { 20d, null });
			result.put(smithy, new Double[] { 20d, null });
			result.put(garage, new Double[] { 5d, null });
			result.put(statue, new Double[] { 1d, null });
			result.put(academy, new Double[] { 1d, null });
		} else if (points < 9000) {
			// SUM (MU) =
			result.put(timberCamp, new Double[] { 30d, 1d });
			result.put(clayPit, new Double[] {30d, 1d });
			result.put(ironMine, new Double[] { 30d, 1d });
			result.put(wareHouse, new Double[] { 27d, 1d });
			result.put(farm, new Double[] { 28d, 1d });
			result.put(barracks, new Double[] { 23d, 1d});
			result.put(stable, new Double[] { 18d, 1d });
			result.put(market, new Double[] { 11d, 1d});
			result.put(garage, new Double[] { 7d, 1d });
			result.put(wall, new Double[] { 20d, null });
			result.put(main, new Double[] { 20d, null });
			result.put(smithy, new Double[] { 20d, null });
			result.put(statue, new Double[] { 1d, null });
			result.put(academy, new Double[] { 1d, null });
		} else if (points < 10000) {
			// SUM (MU) =
			result.put(main, new Double[] { 22d, 1d });
			result.put(wareHouse, new Double[] { 30d, 1d });
			result.put(farm, new Double[] { 30d, 1d });
			result.put(barracks, new Double[] { 25d, 1d});
			result.put(stable, new Double[] { 20d, 1d });
			result.put(market, new Double[] { 15d, 3d});
			result.put(garage, new Double[] { 8d, 2d });
			result.put(timberCamp, new Double[] { 30d, null });
			result.put(clayPit, new Double[] {30d, null });
			result.put(ironMine, new Double[] { 30d, null });
			result.put(wall, new Double[] { 20d, null });
			result.put(smithy, new Double[] { 20d, null });
			result.put(statue, new Double[] { 1d, null });
			result.put(academy, new Double[] { 1d, null });
		} else if (points < 11000) {
			// SUM (MU) =
			result.put(main, new Double[] { 25d, 2d });
			result.put(barracks, new Double[] { 25d, 1d});
			result.put(stable, new Double[] { 20d, 1d });
			result.put(market, new Double[] { 20d, 3d});
			result.put(garage, new Double[] { 10d, 3d });
			result.put(wareHouse, new Double[] { 30d, null });
			result.put(farm, new Double[] { 30d, null });
			result.put(timberCamp, new Double[] { 30d, null });
			result.put(clayPit, new Double[] {30d, null });
			result.put(ironMine, new Double[] { 30d, null });
			result.put(wall, new Double[] { 20d, null });
			result.put(smithy, new Double[] { 20d, null });
			result.put(statue, new Double[] { 1d, null });
			result.put(academy, new Double[] { 1d, null });
		} else {
			// SUM (MU) =
			result.put(main, new Double[] { 27d, 2d });
			result.put(barracks, new Double[] { 25d, 2d });
			result.put(stable, new Double[] { 20d, 2d });
			result.put(market, new Double[] { 22d, 3d });
			result.put(garage, new Double[] { 12d, 3d });
			result.put(wareHouse, new Double[] { 30d, 2d});
			result.put(wall, new Double[] { 20d, 2d });
			result.put(farm, new Double[] { 30d, 2d });
			result.put(timberCamp, new Double[] { 30d, 2d });
			result.put(clayPit, new Double[] { 30d, 2d });
			result.put(ironMine, new Double[] { 30d, 2d });
			result.put(smithy, new Double[] { 20d, 2d });
			result.put(statue, new Double[] { 1d, 1d });
			result.put(academy, new Double[] { 1d, null });
		}
		return result;
	}

	private Map<Integer, List<Integer>> prepareStackDistributionMap() {
		// Calculate reference values
		Double mu = (double) (MAX_VALUES)/2;
		Double sigma = (double) (MAX_VALUES)/6;
		Double maxRef = this.calculateProbability(mu, sigma, mu);
		Double minRef= this.calculateProbability(mu, sigma, 0);
		Double lowerBound = minRef / maxRef;

		// Search
		this.searchRecursively(lowerBound, 1d, 0);

		int numStackEntries = recursionResult.size();
		int targetSize = Math.min(MAX_VALUES, numStackEntries);
		int partitionSize = Math.round((float) numStackEntries / targetSize);

		// Create partitions
		List<List<Entry<List<Integer>, Double>>> partitions = new ArrayList<>();
		for (int i = 0; i < recursionResult.size(); i += partitionSize) {
		    partitions.add(recursionResult.subList(i,Math.min(i + partitionSize, recursionResult.size())));
		}

		// Create distribution Map
		Map<Integer, List<Integer>> result = new LinkedHashMap<>();
		for (int index = 0; index < partitions.size(); index++) {
			List<Entry<List<Integer>, Double>> partition = partitions.get(index);
			Entry<List<Integer>, Double> max = Collections.max(partition, Entry.comparingByValue());
			result.put(index, max.getKey());
		}
		return result;
	}

	private List<Integer> recursionStack;
	private List<Entry<List<Integer>, Double>> recursionResult  = new ArrayList<>();

	private void searchRecursively(Double lowerBound, Double prob, int depth) {
		int currentDepth = depth;
		Double currentProb = prob;
		List<Double> currentRow = probMatrix.get(currentDepth);
		for (int index = currentRow.size() - 1; index >= 0; index--) {
			Double currentValue = currentRow.get(index);
			currentProb *= currentValue;
			Double currentRatio = currentProb / maxProbForDeepestStack;
			boolean valid = currentRatio > lowerBound;
			if (currentDepth == MAX_SEARCH_DEPTH -1 && valid) {
				recursionStack.add(index);
				List<Integer> currentStack = new ArrayList<>(recursionStack);
				recursionStack.remove(recursionStack.size()-1);
				recursionResult.add(new AbstractMap.SimpleEntry<>(currentStack, currentProb));
				return;
			}
			if (!valid) {
				currentProb /= currentValue;
				continue;
			}
			++currentDepth;
			recursionStack.add(index);
			searchRecursively(lowerBound, currentProb, currentDepth);
			--currentDepth;
			recursionStack.remove(recursionStack.size()-1);
			currentProb /= currentValue;
		}
	}


	private Double calculateMaxProbForDepth() {
		Double maxProb = Double.valueOf(1);
		for (int i = 0; i < MAX_SEARCH_DEPTH; i++) {
			maxProb *= Collections.max(probMatrix.get(i));
		}
		return maxProb;
	}


	private Map<String, List<Integer>> prepareSearchMap(Map<String, Map<Integer, Integer>> pointsMap) {
		Map<String, List<Integer>> result = new HashMap<>();
		for (Entry<String, Map<Integer, Integer>> e : pointsMap.entrySet()) {
			String building = e.getKey();
			Map<Integer, Integer> buildingPointsMap = e.getValue();
			List<Integer> points = new ArrayList<>();
			if (! anchorBuildings.contains(building)) {
				points.add(0);
			}
			points.addAll(buildingPointsMap.values());
			result.put(building, points);
		}
		return result;
	}


	public Double calculateStackProb(List<Integer> stack) {
		List<Double> probabilities = new ArrayList<>();
		int min = Math.min(stack.size(), stackIndexProbabilityMap.size());
		for (int depth = 0; depth < min; depth++) {
			Integer value = stack.get(depth);
			Map<Integer, Double> map = stackIndexProbabilityMap.get(depth);
			Double propability = map.get(value);
			probabilities.add(propability);
		}
		Optional<Double> opt = probabilities.stream().reduce((a, b) -> a * b);
		return opt.isPresent() ? opt.get() : Double.valueOf(0);
	}

	public boolean totalPointsReachable(List<Integer> currentStack) {
		int currentSum = calculateStackPointSum(currentStack);
		int minSum = 0;
		int maxSum = 0;
		for (int depth = currentStack.size(); depth < pointsMatrix.size(); depth++) {
			List<Integer> row = pointsMatrix.get(depth);
			int first = row.get(0);
			int last = row.get(row.size()-1);
			minSum += first;
			maxSum += last;
		}
		boolean tooHigh = currentSum + minSum > totalPoints;
		boolean tooLow = currentSum + maxSum < totalPoints;
		return !tooHigh && !tooLow;
	}


	/**
	 * Adds a stack to the result list
	 * @param currentStack the stack to add
	 */
	public void addStackToResult(List<Integer> currentStack) {
		resultList.add(new ArrayList<>(currentStack));
	}

	/**
	 * Generates the representation Map for a given stack
	 * @param stack
	 * @return the representation Map
	 * 		Key:   the building
	 * 		Value: the lvl
	 */
	public Map<String, Integer> generateStackIndexRepMap(List<Integer> stack) {
		Map<String, Integer> result = new LinkedHashMap<>();
		List<String> buildings = generateBuildingList();
		for (int depth = 0; depth < stack.size(); depth++) {
			String building = buildings.get(depth);
			boolean isAnchor = anchorBuildings.contains(building);
			Integer stackVal = stack.get(depth);
			Integer[] offsets = offSetMap.get(depth);
			Integer level = offsets[0] + stackVal;
			if (isAnchor) {
				level += 1;
			}
			result.put(building, level);
		}
		return result;
	}

	private List<String> generateBuildingList() {
		return new ArrayList<>(searchMap.keySet());
	}

	public List<Integer> calculateMostLikelyStack(){
		List<Integer> result = new ArrayList<>();
		for (Entry<Integer, Map<Integer, Double>> e : stackIndexProbabilityMap.entrySet()) {
			Map<Integer, Double> probs = e.getValue();
			List<Entry<Integer, Double>> list = new ArrayList<>();
			probs.entrySet().stream().forEach(list::add);
			Entry<Integer, Double> max = Collections.max(list, Entry.comparingByValue());
			result.add(max.getKey());
		}
		return result;
	}

	public int calculateStackPointSum(List<Integer> stack) {
		int sum = 0;
		for (int depth = 0; depth < stack.size(); depth++) {
			Integer value = stack.get(depth);
			List<Integer> row = pointsMatrix.get(depth);
			Integer points = row.get(value);
			sum += points;
		}
		return sum;
	}

	/*
	 * Getters and Setters
	 */

	public List<List<Integer>> getResultList() {
		return resultList;
	}

	public List<List<Integer>> getPointsMatrix() {
		return pointsMatrix;
	}

	public List<List<Double>> getProbMatrix() {
		return probMatrix;
	}

	public Map<Integer, List<Integer>> getDistributionMap() {
		return distributionMap;
	}

	public Map<String, List<Integer>> getSearchMap() {
		return searchMap;
	}

	public Map<Integer, Map<Integer, Double>> getStackIndexProbabilityMap() {
		return stackIndexProbabilityMap;
	}

	public Map<Integer, Integer[]> getOffSetMap() {
		return offSetMap;
	}

}