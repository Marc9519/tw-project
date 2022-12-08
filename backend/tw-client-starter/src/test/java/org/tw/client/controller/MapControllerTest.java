package org.tw.client.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MapControllerTest {

	@Test
	public void testMapController() throws IOException {
		// Given
		MapController testInstance = new MapController();
		// When
		Map<String, BufferedImage> result = testInstance.getAssetImageMap();
		// Then
		assertNotNull(result);
		assertEquals(49, result.size());
		BufferedImage img = testInstance.getImageByKey("v1");
		assertNotNull(img);
		validateResult(result);
		Map<Integer, Map<Integer, Object>> mapDataCache = testInstance.getMapDataCache();
		assertNotNull(mapDataCache);
		serializeMapDataToFile(mapDataCache);
	}

	private void serializeMapDataToFile(Map<Integer, Map<Integer, Object>> mapDataCache) {
		try (FileOutputStream fout = new FileOutputStream("./target/test-classes/ctrl/cache.ser");
				ObjectOutputStream oos = new ObjectOutputStream(fout)) {
			oos.writeObject(mapDataCache);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	@Test
	@SuppressWarnings("unchecked")
	public void deSerializeMapDataFromFile() {
		try(InputStream in = getClass().getClassLoader().getResourceAsStream("cache.ser");
			ObjectInputStream oin = new ObjectInputStream(in)){
			Map<Integer, Map<Integer, Object>> mapDataCache = (Map<Integer, Map<Integer, Object>>) oin.readObject();
			assertNotNull(mapDataCache);
		} catch (IOException | ClassNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Test case for the drag event handling.
	 * @throws Exception
	 */
	@Test
	public void testHandleDragEventData() throws Exception {
		/*
		 * Given
		 */
		MapController testInstance = new MapController();
		int dragX = 1;
		int dragY = 1;
		Point dragStartPoint = new Point(120,280);
		/*
		 * When
		 */
		testInstance.handleDragEventData(dragX, dragY, dragStartPoint);
		/*
		 * Then
		 */
		assertNotNull(testInstance);
	}

	@Test
	public void testCreateCurrentMapExcerpt() throws Exception {
		// Given
		MapController testInstance = new MapController();
		// When
		BufferedImage mapExcerpt = testInstance.createCurrentMapExcerpt();
		// Then
		assertNotNull(mapExcerpt);
		File outputfile = new File("./target/test-classes/ctrl/excerpt.png");
		ImageIO.write(mapExcerpt, "png", outputfile);
	}


	/*
	 * TestUtil
	 */

	private void validateResult(Map<String, BufferedImage> result) throws IOException {
		for (Entry<String, BufferedImage> entry : result.entrySet()) {
			String key = entry.getKey();
			BufferedImage image = entry.getValue();
			File outputfile = new File("./target/test-classes/"+key + ".png");
			ImageIO.write(image, "png", outputfile);
		}
	}

}