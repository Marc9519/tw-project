package org.tw.client.controller;

import static org.junit.Assert.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.Test;

public class MiniMapControllerTest {

	@Test
	public void testCreateCurrentMiniMapExcerpt() throws Exception {
		// Given
		Map<Integer, Map<Integer, Object>> mapData = getTestMapData();
		MiniMapController testInstance = new MiniMapController(mapData);
		// When
		BufferedImage result = testInstance.createCurrentMiniMapExcerpt();
		// Then
		assertNotNull(result);
		File outputfile = new File("./target/test-classes/ctrl/minimap.png");
		ImageIO.write(result, "png", outputfile);
	}

	@SuppressWarnings("unchecked")
	private Map<Integer, Map<Integer, Object>> getTestMapData() {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("cache.ser");
			 ObjectInputStream oin = new ObjectInputStream(in)) {
			return (Map<Integer, Map<Integer, Object>>) oin.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
		return Collections.emptyMap();
	}

}