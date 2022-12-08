package org.tw.intface.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.tw.intface.bl.ExportBuildingPropBl;

public class PrunerTest {

	private Map<String, Map<Integer, Integer>> pointsMap;

	@Before
	public void setUp() {
		this.pointsMap = new ExportBuildingPropBl(getConfigMap()).getPointsMap();
	}

	/**
	 * Test case for the instantiation
	 * @throws Exception
	 */
	@Test
	public void testPruner() throws Exception {
		// Given
		int totalPoints = 2550;
		// When
		Pruner pruner = new Pruner(pointsMap, totalPoints);
		Map<String, List<Integer>> searchMap = pruner.getSearchMap();
		List<List<Integer>> pointsMatrix = pruner.getPointsMatrix();
		List<List<Double>> probMatrix = pruner.getProbMatrix();
		Map<Integer, List<Integer>> distributionMap = pruner.getDistributionMap();
		// Then
		assertNotNull(pruner);
		assertNotNull(searchMap);
		assertNotNull(pointsMatrix);
		assertNotNull(probMatrix);
		assertNotNull(distributionMap);
		List<Integer> mostLikelyStack = pruner.calculateMostLikelyStack();
		assertNotNull(mostLikelyStack);
		Map<String, Integer> repMap = pruner.generateStackIndexRepMap(mostLikelyStack);
		assertNotNull(repMap);
	}

	/**
	 * Tests the pruning mechanism for start points.
	 * Tests start points (21-60) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange1() throws Exception {
		// Given
		List<Integer> startPoints = IntStream.range(21, 60).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : startPoints) {
			//When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}

	/**
	 * Tests the pruning mechanism for start points.
	 * Tests start points (60 - 100) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange2() throws Exception {
		// Given
		List<Integer> startPoints = IntStream.range(60, 100).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : startPoints) {
			//When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}

	/**
	 * Tests the pruning mechanism for start points.
	 * Tests start points (100 - 200) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange3() throws Exception {
		// Given
		List<Integer> startPoints = IntStream.range(100, 200).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer startPoint : startPoints) {
			//When
			Pruner pruner = new Pruner(pointsMap, startPoint);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, startPoint, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, startPoint);
			pointNumResultMap.put(startPoint, results.size());
		}
		assertNotNull(pointNumResultMap);
	}

	/**
	 * Tests the pruning mechanism for low points.
	 * Tests low points (200 - 300) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange4() throws Exception {
		// Given
		List<Integer> points = IntStream.range(200, 300).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			//When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}


	/**
	 * Tests the pruning mechanism for low points.
	 * Tests low points (300 - 400) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange5() throws Exception {
		// Given
		List<Integer> points = IntStream.range(300, 400).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			//When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}


	/**
	 * Tests the pruning mechanism for low points.
	 * Tests low points (400 - 500) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange6() throws Exception {
		// Given
		List<Integer> points = IntStream.range(400, 500).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			//When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}


	/**
	 * Tests the pruning mechanism for low points.
	 * Tests low points (500 - 650) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange7() throws Exception {
		// Given
		List<Integer> points = IntStream.range(500, 650).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			//When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}

	/**
	 * Tests the pruning mechanism for low points.
	 * Tests low points (650 - 825) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange8() throws Exception {
		// Given
		List<Integer> points = IntStream.range(650, 825).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}


	/**
	 * Tests the pruning mechanism for low points.
	 * Tests low points (825 - 1000 ) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange9() throws Exception {
		// Given
		List<Integer> points = IntStream.range(825, 1000).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}


	/**
	 * Tests the pruning mechanism for medium points.
	 * Tests medium points (1000 - 1500) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange10() throws Exception {
		// Given
		List<Integer> points = IntStream.range(1000, 1500).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}

	/**
	 * Tests the pruning mechanism for medium points.
	 * Tests medium points (1500 - 2000) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange11() throws Exception {
		// Given
		List<Integer> points = IntStream.range(1500, 2000).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}


	/**
	 * Tests the pruning mechanism for medium points.
	 * Tests medium points (2000 - 2500) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange12() throws Exception {
		// Given
		List<Integer> points = IntStream.range(2000, 2500).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}


	/**
	 * Tests the pruning mechanism for medium points.
	 * Tests medium points (2500 - 3000) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange13() throws Exception {
		// Given
		List<Integer> points = IntStream.range(2500, 3000).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}

	/**
	 * Tests the pruning mechanism for high points.
	 * Tests high points (3000 - 3500) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange14() throws Exception {
		// Given
		List<Integer> points = IntStream.range(3000, 3500).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}

	/**
	 * Tests the pruning mechanism for high points.
	 * Tests high points (3500 - 4000) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange15() throws Exception {
		// Given
		List<Integer> points = IntStream.range(3500, 4000).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}


	/**
	 * Tests the pruning mechanism for high points.
	 * Tests high points (4000 - 5000) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange16() throws Exception {
		// Given
		List<Integer> points = IntStream.range(4000, 5000).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}

	/**
	 * Tests the pruning mechanism for high points.
	 * Tests high points (5000 - 6000) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange17() throws Exception {
		// Given
		List<Integer> points = IntStream.range(5000, 6000).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}

	/**
	 * Tests the pruning mechanism for high points.
	 * Tests high points (6000 - 7000) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange18() throws Exception {
		// Given
		List<Integer> points = IntStream.range(6000, 7000).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}


	/**
	 * Tests the pruning mechanism for high points.
	 * Tests high points (7000 - 8000) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange19() throws Exception {
		// Given
		List<Integer> points = IntStream.range(7000, 8000).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}


	/**
	 * Tests the pruning mechanism for high points.
	 * Tests high points (8000 - 9000) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange20() throws Exception {
		// Given
		List<Integer> points = IntStream.range(8000, 9000).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}

	/**
	 * Tests the pruning mechanism for top points.
	 * Tests top points (9000 - 10000) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange21() throws Exception {
		// Given
		List<Integer> points = IntStream.range(9000, 10000).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}

	/**
	 * Tests the pruning mechanism for top points.
	 * Tests top points (10000 - 11000) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange22() throws Exception {
		// Given
		List<Integer> points = IntStream.range(10000, 11000).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			assertFalse(results.isEmpty());
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
	}

	/**
	 * Tests the pruning mechanism for end points.
	 * Tests end points ( 11000 - 12128) for having at least 1 result
	 * @throws Exception
	 */
	@Test
	public void testWhenPruneBranchThenResultNotEmptyRange23() throws Exception {
		// Given
		List<Integer> points = Arrays.asList(11010, 11047, 11047, 11048, 11048, 11052, 11052, 11059, 11080, 11104, 11104, 11104, 11104, 11104, 11104, 11104, 11129, 11136, 11137, 11162, 11162, 11209, 11212, 11222, 11292, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11295, 11344, 11376, 11385, 11386, 11394, 11421, 11444, 11518, 11524, 11524, 11524, 11524, 11524, 11524, 11575, 11704, 11711, 11793, 11793, 11793, 11798, 11798, 11885, 11885, 11919, 11958, 12085, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128, 12128);
		points = IntStream.range(11000, 12129).boxed().collect(Collectors.toList());
		Map<Integer, Integer> pointNumResultMap = new LinkedHashMap<>();
		List<Integer> emptyPoints = new ArrayList<>();
		for (Integer point : points) {
			// When
			Pruner pruner = new Pruner(pointsMap, point);
			List<List<Integer>> searchMatrix = pruner.getPointsMatrix();
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, searchMatrix, point, 0);
			// Then
			List<List<Integer>> results = pruner.getResultList();
			assertNotNull(results);
			if (results.isEmpty()) {
				emptyPoints.add(point);
			}
			validateResult(searchMatrix, results, point);
			pointNumResultMap.put(point, results.size());
		}
		assertNotNull(pointNumResultMap);
		assertNotNull(emptyPoints);
	}

	/**
	 * Tests low points (21-100) for having at least 1 result
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void testWhenLowPointsThenResultNotEmpty() throws InterruptedException, ExecutionException {
		List<Integer> toBeResolved = IntStream.range(21, 100).boxed().collect(Collectors.toList());
		// Create partitions
		int partitionSize = 10;
		List<List<Integer>> partitions = new ArrayList<>();
		for (int i = 0; i < toBeResolved.size(); i += partitionSize) {
		    partitions.add(toBeResolved.subList(i,Math.min(i + partitionSize, toBeResolved.size())));
		}
		Map<String, Map<Integer, Integer>> pMap = new ExportBuildingPropBl(getConfigMap()).getPointsMap();
		List<Integer> tmp = partitions.iterator().next();
		for (Integer points : tmp) {
			Pruner pruner = new Pruner(pMap, points);
			Searcher searcher = new Searcher();
			searcher.executeDiffSearch(pruner, pruner.getPointsMatrix(), points, 0);
			List<List<Integer>> resultList = pruner.getResultList();
			assertNotNull(resultList);
		}
		List<Callable<List<Entry<Integer, Integer>>>> callables = new ArrayList<>();
		for (List<Integer> partition : partitions) {
			Callable<List<Entry<Integer, Integer>>> callable = new Callable<List<Entry<Integer, Integer>>>() {
				@Override
				public List<Entry<Integer, Integer>> call() throws Exception {
					Integer from = partition.get(0);
					Integer to = partition.get(partition.size()-1);
					Thread.currentThread().setName("EXEC PART [" + from + "," + to + "]");
					List<Entry<Integer, Integer>> entryList = new ArrayList<>();
						for (Integer points : partition) {
							Pruner pruner = new Pruner(pMap, points);
							Searcher searcher = new Searcher();
							searcher.executeDiffSearch(pruner, pruner.getPointsMatrix(), points, 0);
							List<List<Integer>> resultList = pruner.getResultList();
							entryList.add(new AbstractMap.SimpleEntry<>(points, resultList.size()));
						}
						entryList.sort(Entry.comparingByValue());
					return entryList;
				}
			};
			callables.add(callable);
		}
		ExecutorService executor = Executors.newFixedThreadPool(15);
		List<Future<List<Entry<Integer, Integer>>>> futures = executor.invokeAll(callables);
		for (Future<List<Entry<Integer, Integer>>> future : futures) {
			List<Entry<Integer, Integer>> list = future.get();
			assertNotNull(list);
			assertFalse(list.isEmpty());
			for (Entry<Integer, Integer> entry : list) {
				Integer value = entry.getValue();
				assertNotNull(value);
				assertTrue(value > 0);
			}
		}

	}

	private void validateResult(List<List<Integer>> searchMatrix, List<List<Integer>> result, Integer targetValue) {
		for (List<Integer> list : result) {
			Integer currentSum = 0;
			for (int i = 0; i < list.size(); i++) {
				Integer integer = list.get(i);
				Integer tmp = searchMatrix.get(i).get(integer);
				currentSum += tmp;
			}
			assertEquals(targetValue, currentSum);
		}
	}

	private Map<String, Boolean> getConfigMap() {
		Map<String, Boolean> configMap = new HashMap<>();
		configMap.put("WATCHTOWER", Boolean.FALSE);
		configMap.put("CHURCH", Boolean.FALSE);
		configMap.put("CHURCH_FIRST", Boolean.FALSE);
		configMap.put("STATUE", Boolean.TRUE);
		configMap.put("HIDING_PLACE", Boolean.FALSE);
		configMap.put("GOLD_COIN", Boolean.TRUE);
		return configMap;
	}

}