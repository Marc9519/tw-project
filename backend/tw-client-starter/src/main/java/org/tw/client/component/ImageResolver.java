package org.tw.client.component;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageResolver {
	
	public Map<String, BufferedImage> resolveImage() throws IOException {
		BufferedImage right = ImageIO.read(new File(
				"C:\\Users\\a890202\\dev\\repos\\tw-project\\frontend\\tw-app\\src\\assets\\topbutton-right.png"));
		int height = right.getHeight() / 2;
		BufferedImage topRight = new BufferedImage(right.getWidth(), height, BufferedImage.TYPE_INT_ARGB);
		BufferedImage bottomRight = new BufferedImage(right.getWidth(), height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = topRight.getGraphics();
		g.drawImage(right, 0, 0, null);
		g = bottomRight.getGraphics();
		g.drawImage(right.getSubimage(0, height, right.getWidth(), height), 0, 0, null);
		Map<String, BufferedImage> result = new HashMap<>();
		result.put("TOP_RIGHT", topRight);
		result.put("BOTTOM_RIGHT", bottomRight);
		processLeftImage(result);
		return result;
	}
	
	public BufferedImage mergeTop(BufferedImage left, BufferedImage right) {
		BufferedImage result = new BufferedImage(left.getWidth() + right.getWidth(), left.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = result.getGraphics();
		g.drawImage(left, 0, 0, null);
		g.drawImage(right, left.getWidth() - 2, 0, null);
		return result;
	}
	
	public BufferedImage mergeBottom(BufferedImage left, BufferedImage right) {
		BufferedImage result = new BufferedImage(left.getWidth() + right.getWidth(), left.getHeight(),BufferedImage.TYPE_INT_ARGB);
		Graphics g = result.getGraphics();
		g.drawImage(left, 0, 0, null);
		g.drawImage(right, left.getWidth() - 2, 0, null);
		return result;
	}

	private void processLeftImage(Map<String, BufferedImage> result) throws IOException {
		BufferedImage left = ImageIO.read(new File(
				"C:\\Users\\a890202\\dev\\repos\\tw-project\\frontend\\tw-app\\src\\assets\\topbutton-left.png"));
		int height = left.getHeight() / 2;
		BufferedImage topLeft = new BufferedImage(left.getWidth(), height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = topLeft.getGraphics();
		g.drawImage(left, 0, 0, null);
		BufferedImage bottomLeft = new BufferedImage(left.getWidth(), height, BufferedImage.TYPE_INT_ARGB);
		g = bottomLeft.getGraphics();
		g.drawImage(left.getSubimage(0, height, left.getWidth(), height), 0, 0, null);
		
		result.put("TOP_LEFT", topLeft);
		result.put("BOTTOM_LEFT", bottomLeft);
	}

}
