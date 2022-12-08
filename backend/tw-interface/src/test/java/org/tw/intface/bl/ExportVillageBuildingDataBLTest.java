package org.tw.intface.bl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.tw.intface.util.UrlConnector;

@RunWith(MockitoJUnitRunner.class)
public class ExportVillageBuildingDataBLTest {

	@Test
	public void testExportVillageBuildingDataBL() throws Exception {
		// Given
		String worldPrefix = "de197";
		UrlConnector connectorMock = Mockito.mock(UrlConnector.class);
		when(connectorMock.getMapDataRequestInputStreams(worldPrefix)).thenReturn(ExportPlayerDataBLTest.getConnectionResponses());
		// When
		ExportVillageBuildingDataBL bl = new ExportVillageBuildingDataBL(connectorMock, worldPrefix);
		String insertScript = bl.getInsertScript();
		// Then
		assertNotNull(bl);
		assertNotNull(insertScript);
	}

}