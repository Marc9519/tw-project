package org.tw.intface.bl;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResolvePointsBlTest {

	/**
	 * Resolve low point numbers (21 -100 Points)
	 */
	@Test
	public void testResolveByPointsNrRange1(){
		// Given
		List<Integer> points = IntStream.range(21, 100).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve low point numbers (100 -300 Points)
	 */
	@Test
	public void testResolveByPointsNrRange2(){
		// Given
		List<Integer> points = IntStream.range(100, 300).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve low point numbers (300 -500 Points)
	 */
	@Test
	public void testResolveByPointsNrRange3(){
		// Given
		List<Integer> points = IntStream.range(300, 500).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve low point numbers (500 -1000 Points)
	 */
	@Test
	public void testResolveByPointsNrRange4(){
		// Given
		List<Integer> points = IntStream.range(500, 1000).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}


	/**
	 * Resolve middle point numbers (1000 -1500 Points)
	 */
	@Test
	public void testResolveByPointsNrRange5(){
		// Given
		List<Integer> points = IntStream.range(1000, 1500).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}


	/**
	 * Resolve middle point numbers (1500 -2000 Points)
	 */
	@Test
	public void testResolveByPointsNrRange6(){
		// Given
		List<Integer> points = IntStream.range(1500, 2000).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}


	/**
	 * Resolve middle point numbers (2000 -2500 Points)
	 */
	@Test
	public void testResolveByPointsNrRange7(){
		// Given
		List<Integer> points = IntStream.range(2000, 2500).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve middle point numbers (2500 - 3000 Points)
	 */
	@Test
	public void testResolveByPointsNrRange8(){
		// Given
		List<Integer> points = IntStream.range(2500, 3000).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}


	/**
	 * Resolve high point numbers (3000 - 3500 Points)
	 */
	@Test
	public void testResolveByPointsNrRange9(){
		// Given
		List<Integer> points = IntStream.range(3000, 3500).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve high point numbers (3500 - 4000 Points)
	 */
	@Test
	public void testResolveByPointsNrRange10(){
		// Given
		List<Integer> points = IntStream.range(3500, 4000).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve high point numbers (4000 - 4500 Points)
	 */
	@Test
	public void testResolveByPointsNrRange11(){
		// Given
		List<Integer> points = IntStream.range(4000, 4500).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve high point numbers (4500 - 5000 Points)
	 */
	@Test
	public void testResolveByPointsNrRange12(){
		// Given
		List<Integer> points = IntStream.range(4500, 5000).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve high point numbers (5000 - 5500 Points)
	 */
	@Test
	public void testResolveByPointsNrRange13(){
		// Given
		List<Integer> points = IntStream.range(5000, 5500).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve high point numbers (5500 - 6000 Points)
	 */
	@Test
	public void testResolveByPointsNrRange14(){
		// Given
		List<Integer> points = IntStream.range(5500, 6000).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}


	/**
	 * Resolve high point numbers (6000 - 6500 Points)
	 */
	@Test
	public void testResolveByPointsNrRange15(){
		// Given
		List<Integer> points = IntStream.range(6000, 6500).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve high point numbers (6500 - 7000 Points)
	 */
	@Test
	public void testResolveByPointsNrRange16(){
		// Given
		List<Integer> points = IntStream.range(6500, 7000).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve high point numbers (7000 - 7500 Points)
	 */
	@Test
	public void testResolveByPointsNrRange17(){
		// Given
		List<Integer> points = IntStream.range(7000, 7500).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve high point numbers (7500 - 8000 Points)
	 */
	@Test
	public void testResolveByPointsNrRange18(){
		// Given
		List<Integer> points = IntStream.range(7500, 8000).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve high point numbers ( 8000 - 8500 Points)
	 */
	@Test
	public void testResolveByPointsNrRange19(){
		// Given
		List<Integer> points = IntStream.range(8000, 8500).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve high point numbers ( 8500 - 9000 Points)
	 */
	@Test
	public void testResolveByPointsNrRange20(){
		// Given
		List<Integer> points = IntStream.range(8500, 9000).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve top point numbers ( 9000 - 9500 Points)
	 */
	@Test
	public void testResolveByPointsNrRange21(){
		// Given
		List<Integer> points = IntStream.range(9000, 9500).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve top point numbers ( 9500 - 10000 Points)
	 */
	@Test
	public void testResolveByPointsNrRange22(){
		// Given
		List<Integer> points = IntStream.range(9500, 10000).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}


	/**
	 * Resolve top point numbers ( 10000 - 10500 Points)
	 */
	@Test
	public void testResolveByPointsNrRange23(){
		// Given
		List<Integer> points = IntStream.range(10000, 10500).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}

	/**
	 * Resolve top point numbers ( 10500 - 11000 Points)
	 */
	@Test
	public void testResolveByPointsNrRange24(){
		// Given
		List<Integer> points = IntStream.range(10500, 11000).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}


	/**
	 * Resolve end point numbers ( 11000  -12128 Points)
	 */
	@Test
	public void testResolveByPointsNrRange25(){
		// Given
		List<Integer> points = IntStream.range(11000, 12129).boxed().collect(Collectors.toList());
		// When
		ResolvePointsBl bl = new ResolvePointsBl(points);
		String insertSql = bl.getInsertSql();
		// Then
		assertNotNull(bl);
		assertNotNull(insertSql);
	}


}