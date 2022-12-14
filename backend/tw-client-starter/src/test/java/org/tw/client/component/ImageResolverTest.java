package org.tw.client.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.junit.Test;

public class ImageResolverTest {

	@Test
	public void testResolveImage() throws IOException {
		// Given
		ImageResolver res = new ImageResolver();
		// When
		Map<String, BufferedImage> result = res.resolveImage();
		// Then
		BufferedImage topRight = result.get("TOP_RIGHT");
		BufferedImage bottomRight = result.get("BOTTOM_RIGHT");
		BufferedImage topLeft = result.get("TOP_LEFT");
		BufferedImage bottomLeft = result.get("BOTTOM_LEFT");
		assertNotNull(topRight);
		assertNotNull(bottomRight);
		assertNotNull(topLeft);
		assertNotNull(bottomLeft);
		assertEquals(topRight.getHeight(), topLeft.getHeight());
		assertEquals(bottomRight.getHeight(), bottomLeft.getHeight());
		assertEquals(topRight.getWidth(), bottomRight.getWidth());
		assertEquals(topLeft.getWidth(), bottomLeft.getWidth());
		writeToFile(topRight, "top-right.png");
		writeToFile(bottomRight, "bottom-right.png");
		writeToFile(topLeft, "top-left.png");
		writeToFile(bottomLeft, "bottom-left.png");
	}

	@Test
	public void testMergeTop() throws IOException {
		// Given
		ImageResolver resolver = new ImageResolver();
		Map<String, BufferedImage> subImageMap = resolver.resolveImage();
		BufferedImage topLeft = subImageMap.get("TOP_LEFT");
		BufferedImage topRight = subImageMap.get("TOP_RIGHT");
		// When
		BufferedImage top = resolver.mergeTop(topLeft, topRight);
		// Then
		assertNotNull(top);
		writeToFile(top, "top.png");
	}
	
	@Test
	public void testMergeBottom() throws Exception {
		// Given
		ImageResolver resolver = new ImageResolver();
		Map<String, BufferedImage> subImageMap = resolver.resolveImage();
		BufferedImage bottomLeft = subImageMap.get("BOTTOM_LEFT");
		BufferedImage bottomRight = subImageMap.get("BOTTOM_RIGHT");
		// When
		BufferedImage merged = resolver.mergeBottom(bottomLeft, bottomRight);
		// Then
		assertNotNull(merged);
		writeToFile(merged, "bottom.png");
	}
	
	private void writeToFile(BufferedImage image, String name) throws IOException {
		File file = new File("./target/test-classes/" + name);
		ImageIO.write(image, "png", file);
		
	}

}
