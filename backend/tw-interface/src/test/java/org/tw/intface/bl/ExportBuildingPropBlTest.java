package org.tw.intface.bl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExportBuildingPropBlTest {

	@Test
	public void testGenerateInsertSqlStatements() {
		//Given
		Map<String, Boolean> configMap = getConfigMap();
		//When
		ExportBuildingPropBl bl = new ExportBuildingPropBl(configMap);
		Map<String, Long> buildingTranslationMap = bl.getBuildingTranslationMap();
		Map<String, Map<Integer, Integer>> pointsMap = bl.getPointsMap();
		String buildingInsertSql = bl.getBuildingInsertSql();
		String propertyInsertSql = bl.getPropertyInsertSql();
		String buildingPropertyInsertSql = bl.getBuildingPropertyInsertSql();
		String buildingLevelPropertyInsertSql = bl.getBuildingLevelPropertyInsertSql();
		String insertScript = bl.getInsertScript();
		//Then
		assertNotNull(buildingTranslationMap);
		assertFalse(buildingTranslationMap.isEmpty());
		assertNotNull(pointsMap);
		assertFalse(pointsMap.isEmpty());
		assertNotNull(buildingInsertSql);
		assertNotNull(propertyInsertSql);
		assertNotNull(buildingPropertyInsertSql);
		assertNotNull(buildingLevelPropertyInsertSql);
		assertNotNull(insertScript);
	}

	/*
	 *
	 */

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