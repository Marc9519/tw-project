package org.tw.intface.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

public class UrlConnectorTest {

	@Test
	public void testGetMapDataRequestInputStreams() {
		// Given
		UrlConnector testInstance = new UrlConnector();
		String worldPrefix = "de197";
		// When
		List<InputStream> result = testInstance.getMapDataRequestInputStreams(worldPrefix);
		// Then
		assertNotNull(result);
		assertFalse(result.isEmpty());
	}

	@Test
	public void testGetMapDataRequestInputStreamsInvalid() {
		// Given
		UrlConnector testInstance = new UrlConnector();
		String worldPrefix = "";
		// When
		List<InputStream> result = testInstance.getMapDataRequestInputStreams(worldPrefix);
		// Then
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

}
