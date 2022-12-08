package org.tw.intface.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DBConnectorTest {

	@Test
	public void testGetResultMatrixFromSql() throws Exception {
		// Given
		String sql = "SELECT * FROM GENERAL.POINTS_CONFIG WHERE POINT_NUM = 213";
		// When
		DBConnector con = new DBConnector();
		Object[][] resultMatrixFromSql = con.getResultMatrixFromSql(sql);
		// Then
		assertNotNull(resultMatrixFromSql);
	}

}
