package org.tw.intface.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.cj.jdbc.MysqlDataSource;

public class DBConnector {

	/*
	 * The DB Connector
	 */
	private static final Logger logger = LoggerFactory.getLogger(DBConnector.class);

	private DataSource dataSource;

	public DBConnector() {
		this.dataSource = getDataSource();
	}

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

	private DataSource getDataSource() {
		MysqlDataSource result = new MysqlDataSource();
		result.setServerName("localhost");
		result.setPortNumber(3306);
		result.setUser("root");
		result.setPassword("root");
		return result;
	}

}