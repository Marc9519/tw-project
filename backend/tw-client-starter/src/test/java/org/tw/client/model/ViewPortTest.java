package org.tw.client.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Test;

public class ViewPortTest {

	private static final int tileWidthPx = 53;
	private static final int tileHeightPx = 38;


	/**
	 * Test case for Instantiation by center Point. The outer frame width/height is odd.
	 * @throws Exception
	 */
	@Test
	public void testViewPortByCenterPointOdd() throws Exception {
		//Given
		int outerFrameWidth = 15;
		int outerFrameHeight = 15;
		Point centerPoint = new Point(500,500);
		//When
		ViewPort vp = new ViewPort(tileWidthPx, tileHeightPx, outerFrameWidth, outerFrameHeight, centerPoint);
		//Then
		assertNotNull(vp);
		int outerFrameLocationX = vp.getOuterFrameLocationX();
		int outerFrameLocationY = vp.getOuterFrameLocationY();
		assertEquals(493, outerFrameLocationX);
		assertEquals(493, outerFrameLocationY);
		int innerFrameLocationXPx = vp.getInnerFrameLocationXPx();
		int innerFrameLocationYPx = vp.getInnerFrameLocationYPx();
		assertEquals(3*tileWidthPx, innerFrameLocationXPx);
		assertEquals(3*tileHeightPx, innerFrameLocationYPx);
		assertEquals(new Point(27,19), vp.getInnerFrameCenterPos());
	}

	/**
	 * Test case for Instantiation by center Point. The outer frame width/height is even.
	 * @throws Exception
	 */
	@Test
	public void testViewPortByCenterPointEven() throws Exception {
		int outerFrameWidth = 16;
		int outerFrameHeight = 16;
		Point centerPoint = new Point(500,500);
		//When
		ViewPort vp = new ViewPort(tileWidthPx, tileHeightPx, outerFrameWidth, outerFrameHeight, centerPoint);
		//Then
		assertNotNull(vp);
		int outerFrameLocationX = vp.getOuterFrameLocationX();
		int outerFrameLocationY = vp.getOuterFrameLocationY();
		assertEquals(492, outerFrameLocationX);
		assertEquals(492, outerFrameLocationY);
		int innerFrameLocationXPx = vp.getInnerFrameLocationXPx();
		int innerFrameLocationYPx = vp.getInnerFrameLocationYPx();
		int expectedX = 4*tileWidthPx;
		int expectedY = 4*tileHeightPx;
		assertEquals(expectedX, innerFrameLocationXPx);
		assertEquals(expectedY, innerFrameLocationYPx);
		assertEquals(new Point(27,19), vp.getInnerFrameCenterPos());
	}

	/**
	 * General Test Case for horizontal Shifting of the ViewPort.
	 * @throws Exception
	 */
	@Test
	public void testExecuteShiftHorizontally() throws Exception {
		// Given
		ViewPort vp = getTestViewPort();
		Map<Integer, List<Integer>> assertionMap = prepareDxAssertionMap();
		for (Entry<Integer, List<Integer>> e : assertionMap.entrySet()) {
			Integer dX = e.getKey();
			int prevX = vp.getOuterFrameLocationX();
			// When
			int shiftVal = vp.executeShiftHorizontally(dX);
			// Then
			List<Integer> assertions = e.getValue();
			int expectedXVal = assertions.get(0);
			int expectedDiff = assertions.get(1);
			assertEquals(expectedXVal, vp.getInnerFrameCenterPos().x);
			assertEquals(expectedDiff, shiftVal);
			assertEquals(expectedDiff, vp.getOuterFrameLocationX() - prevX);
			// Check innerFrameLocation
			validateInnerFrameLocation(expectedXVal , vp, true);
			// Undo
			vp.executeShiftHorizontally(-dX);
		}
	}

	@Test
	public void testExecuteShiftVertically() throws Exception {
		// Given
		ViewPort vp = getTestViewPort();
		Map<Integer, List<Integer>> assertionMap = prepareDyAssertionMap();
		for (Entry<Integer, List<Integer>> e : assertionMap.entrySet()) {
			Integer dY = e.getKey();
			int prevY = vp.getOuterFrameLocationY();
			// When
			int shiftVal = vp.executeShiftVertically(dY);
			// Then
			List<Integer> assertions = e.getValue();
			int expectedYVal = assertions.get(0);
			int expectedDiff = assertions.get(1);
			assertEquals(expectedYVal, vp.getInnerFrameCenterPos().y);
			assertEquals(expectedDiff, shiftVal);
			assertEquals(expectedDiff, vp.getOuterFrameLocationY() - prevY);
			// Check innerFrameLocation
			validateInnerFrameLocation(expectedYVal , vp, false);
			// Undo
			vp.executeShiftVertically(-dY);
		}
	}

	@Test
	public void testGetMapLocByInnerFrameOffsets() throws Exception {
		//Given
		ViewPort testInstance = getTestViewPort();
		//When
		Point r1 = testInstance.getMapLocByInnerFrameOffsets(0, 0);
		Point r2 = testInstance.getMapLocByInnerFrameOffsets(53, 0);
		//Then
		assertNotNull(r1);
		assertNotNull(r2);
	}


	/*
	 * UtilCode
	 */

	private void validateInnerFrameLocation(int centerValue, ViewPort vp, boolean isHorizontal) {
		int expectedLocation = 0;
		int dist = (int) Math.ceil(((double) vp.getOuterFrameWidth() - vp.getInnerFrameSize()) / 2);
		if (isHorizontal) {
			expectedLocation = dist * tileWidthPx + centerValue - 27;
			int actualLocation = vp.getInnerFrameLocationXPx();
			assertEquals(expectedLocation, actualLocation);
		}else {
			expectedLocation = dist * tileHeightPx + centerValue - 19;
			assertEquals(expectedLocation, vp.getInnerFrameLocationYPx());
		}
	}

	private Map<Integer, List<Integer>> prepareDxAssertionMap() {
		Map<Integer, List<Integer>> assertions = new TreeMap<>();
		// (-186, -159 , -134 : -3 ) --> (132, 159, 184 : +3)
		Map<Integer, Integer> indexMap = new HashMap<>();
		indexMap.put(0, 0);
		indexMap.put(1, 27);
		indexMap.put(2, 52);
		// Initialize stepValues
		int from = -186;
		int mid = from + 27;
		int to = from + tileWidthPx - 1;
		for (int steps = -3; steps <= 3; steps++) {
			int[] stepValues = new int[] { from, mid, to };
			from += tileWidthPx;
			mid += tileWidthPx;
			to += tileWidthPx;
			for (int index = 0; index < stepValues.length; index++) {
				int currentValue = stepValues[index];
				assertions.put(currentValue, Arrays.asList(indexMap.get(index), steps));
			}
		}
		return assertions;
	}

	private Map<Integer, List<Integer>> prepareDyAssertionMap() {
		Map<Integer, List<Integer>> assertions = new TreeMap<>();
		// (-133, -114 , -96 : -3 ) --> (95, 114, 132 : +3)
		Map<Integer, Integer> indexMap = new HashMap<>();
		indexMap.put(0, 0);
		indexMap.put(1, 19);
		indexMap.put(2, 37);
		// Initialize stepValues
		int from = -133;
		int mid = from + 19;
		int to = from + tileHeightPx - 1;
		for (int steps = -3; steps <= 3; steps++) {
			int[] stepValues = new int[] { from, mid, to };
			from += tileHeightPx;
			mid += tileHeightPx;
			to += tileHeightPx;
			for (int index = 0; index < stepValues.length; index++) {
				int currentValue = stepValues[index];
				assertions.put(currentValue, Arrays.asList(indexMap.get(index), steps));
			}
		}
		return assertions;
	}

	private ViewPort getTestViewPort() {
		int outerFrameWidth = 15;
		int outerFrameHeight = 15;
		Point centerPoint = new Point(500,500);
		return new ViewPort(tileWidthPx, tileHeightPx, outerFrameWidth, outerFrameHeight, centerPoint);
	}

}