package org.tw.data.service.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Utility class for handling db - interactions
 */
public class DBTool {

	/*
	 * The data source
	 */
	@Autowired
	private DataSource dataSource;

	/*
	 * The logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(DBTool.class);

	/**
	 * @param sql
	 * @return The object array
	 */
	public Object[][] getResultMatrixFromSql(String sql) {
		List<Object[]> result = new ArrayList<>();
		int numColumns = 0;
		try (Connection conn = dataSource.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			numColumns = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				Object[] row = new Object[numColumns];
				for (int i = 0; i < numColumns; i++) {
					row[i] = rs.getObject(i + 1);
				}
				result.add(row);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			return new Object[][] {};
		}
		return toObjArray(result, numColumns);
	}

	private Object[][] toObjArray(List<Object[]> result, int numColumns) {
		if (result.isEmpty()) {
			return new Object[][] {};
		}
		int numRows = result.size();
		Object[][] ret = new Object[numRows][numColumns];
		int i = 0;
		for (Object[] row : result) {
			ret[i] = row;
			++i;
		}
		return ret;
	}

}
