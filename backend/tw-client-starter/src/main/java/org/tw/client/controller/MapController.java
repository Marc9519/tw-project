package org.tw.client.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tw.client.model.ViewPort;
import org.tw.client.service.TwDataServiceClientImpl;
import org.tw.data.service.model.MapAsset;
import org.tw.data.service.model.Village;

/**
 * The Map Controller
 */
public class MapController implements Serializable {

	private static final long serialVersionUID = 1L;
	private final int tileWidthPx;
	private final int tileHeightPx;

	// xPos , yPos --> MapComponent
	private transient Map<String, BufferedImage> assetImageBufferMap;

	private ViewPort viewPort;
	private transient TwDataServiceClientImpl dataService;

	private transient Map<Integer, Map<Integer, Object>> mapDataCache;

	/*
	 * The logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(MapController.class);

	public MapController() {
		this.assetImageBufferMap = generateAssetImageBufferMap();
		BufferedImage ref = assetImageBufferMap.entrySet().iterator().next().getValue();
		this.tileWidthPx = ref.getWidth();
		this.tileHeightPx = ref.getHeight();
		this.viewPort = new ViewPort(tileWidthPx, tileHeightPx, 15, 15, new Point(500,500));
		this.dataService = initDataService();
		long start = System.currentTimeMillis();
		this.mapDataCache = dataService.loadAllMapAssetsAsync();
		long end = System.currentTimeMillis();
		logger.info("DURATION : {} ms", end - start);
	}

	/**
	 *  Public API for handling the drag event
	 * @param dragX
	 * @param dragY
	 * @param dragStartPoint
	 * @return
	 */
	public Map<String, Object> handleDragEventData(int dragX, int dragY, Point dragStartPoint) {
		int dX = dragStartPoint.x - dragX;
		int dY = dragStartPoint.y - dragY;
		int shiftX = viewPort.executeShiftHorizontally(dX);
		int shiftY = viewPort.executeShiftVertically(dY);
		boolean isShiftX = shiftX != 0;
		boolean isShiftY = shiftY != 0;
		BufferedImage currentMapExcerpt = null;
		if (isShiftX || isShiftY) {
			currentMapExcerpt = createCurrentMapExcerpt();
		}
		Map<String, Object> result = new HashMap<>();
		int locX = -1 * viewPort.getInnerFrameLocationXPx();
		int locY = -1 * viewPort.getInnerFrameLocationYPx();
		result.put("LOC_X", locX);
		result.put("LOC_Y", locY);
		result.put("IMAGE", currentMapExcerpt);
		return result;
	}

	public Village handleMoveEventData(int x, int y) {
		Point currentPoint = viewPort.getMapLocByInnerFrameOffsets(x, y);
		int xPos = currentPoint.x;
		int yPos = currentPoint.y;
		Object currentObject = mapDataCache.get(xPos).get(yPos);
		if (currentObject instanceof Village) {
			Village currentVillage = (Village) currentObject;
			logger.info("Current Village: {} {}|{}", currentVillage.getName(), currentVillage.getxPos(), currentVillage.getyPos());
			return currentVillage;
		}
		return null;
	}

	public BufferedImage createCurrentMapExcerpt() {
		int fromX = viewPort.getOuterFrameLocationX();
		int toX = fromX + viewPort.getOuterFrameWidth();
		int fromY = viewPort.getOuterFrameLocationY();
		int toY = fromY + + viewPort.getOuterFrameHeight();
		return createMapExcerpt(fromX, toX, fromY, toY);
	}

	public BufferedImage getImageByKey(String key) {
		return assetImageBufferMap.get(key);
	}

	private BufferedImage createMapExcerpt(int fromX,int toX, int fromY, int toY) {
		int totalWidth = (toX - fromX) * tileWidthPx;
		int totalHeight = (toY - fromY) * tileHeightPx;
		BufferedImage result = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
		int currentX = 0;
		int currentY = 0;
		Graphics rg = result.getGraphics();
		for (int x = fromX; x <= toX; x++) {
			for (int y = fromY; y <= toY; y++) {
				Object object = mapDataCache.get(x).get(y);
				String assetName = null;
				if (object instanceof Village) {
					assetName = getVillageAssetName((Village) object);
				}else {
					MapAsset asset  = (MapAsset) object;
					assetName = asset.getName();
				}
				BufferedImage image = copyImage(assetImageBufferMap.get(assetName));
				Graphics imgGr = image.getGraphics();
				imgGr.setColor(new Color(33, 75, 24));
				if (x % 5 == 0) {
					//DrawSectionLineLeft
					imgGr.fillRect(0, 0, 1, tileHeightPx);
				}
				if (y % 5 == 0) {
					//DrawSectionLineUp
					imgGr.fillRect(0, 0, tileWidthPx, 1);
				}
				imgGr.setColor(Color.BLACK);
				if (x%100 == 0) {
					//DrawContiLineLeft
					imgGr.fillRect(0, 0, 3, tileHeightPx);
				}
				if (y % 100 == 0) {
					//DrawContiLineUp
					imgGr.fillRect(0, 0, tileWidthPx, 3);
				}
				rg.drawImage(image, currentX, currentY, null);
				currentY += tileHeightPx;
				imgGr.dispose();
			}
			currentY = 0;
			currentX += tileWidthPx;
		}
		rg.dispose();
		return result;
	}

	private BufferedImage copyImage(BufferedImage source){
	    BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    Graphics g = result.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return result;
	}

	private TwDataServiceClientImpl initDataService() {
		PropertiesConfiguration config = new PropertiesConfiguration();
		String baseUrl;
		try {
			config.load("application.properties");
			baseUrl = config.getString("data.service.url");
		} catch (ConfigurationException e) {
			baseUrl = "http://localhost:1138/tw/rest/data/twinfo/";
			logger.error(e.getMessage(),e);
		}
		return new TwDataServiceClientImpl(baseUrl);
	}

	private String getVillageAssetName(Village village) {
		boolean isBonus = village.getBonus() != null;
		boolean isPlayer = village.getPlayer() != null;
		String flag = isBonus ? "b" : "v";
		String suffix = isPlayer ? "" : "_left";
		Integer points = village.getPoints();
		String number = getAssetNumByPoints(points);
		return flag.concat(number).concat(suffix);
	}

	private String getAssetNumByPoints(Integer points) {
		if (points < 300) {
			return "1";
		}
		if (points < 1000) {
			return "2";
		}
		if (points < 2999) {
			return "3";
		}
		if (points < 8999) {
			return "4";
		}
		if (points < 10999) {
			return "5";
		}
		return "6";
	}

	/**
	 * Loads the image assets and puts the result into a map.
	 *
	 * @return The map
	 */
	private Map<String, BufferedImage> generateAssetImageBufferMap() {
		Map<String, BufferedImage> result = new HashMap<>();
		ClassLoader loader = getClass().getClassLoader();
		try (InputStream in1 = loader.getResourceAsStream("forest/forest0000.png");
				InputStream in2 = loader.getResourceAsStream("forest/forest0001.png");
				InputStream in3 = loader.getResourceAsStream("forest/forest0010.png");
				InputStream in4 = loader.getResourceAsStream("forest/forest0011.png");
				InputStream in5 = loader.getResourceAsStream("forest/forest0100.png");
				InputStream in6 = loader.getResourceAsStream("forest/forest0101.png");
				InputStream in7 = loader.getResourceAsStream("forest/forest0110.png");
				InputStream in8 = loader.getResourceAsStream("forest/forest0111.png");
				InputStream in9 = loader.getResourceAsStream("forest/forest1000.png");
				InputStream in10 = loader.getResourceAsStream("forest/forest1001.png");
				InputStream in11 = loader.getResourceAsStream("forest/forest1010.png");
				InputStream in12 = loader.getResourceAsStream("forest/forest1011.png");
				InputStream in13 = loader.getResourceAsStream("forest/forest1100.png");
				InputStream in14 = loader.getResourceAsStream("forest/forest1101.png");
				InputStream in15 = loader.getResourceAsStream("forest/forest1110.png");
				InputStream in16 = loader.getResourceAsStream("forest/forest1111.png");
				InputStream in17 = loader.getResourceAsStream("general/berg1.png");
				InputStream in18 = loader.getResourceAsStream("general/berg2.png");
				InputStream in19 = loader.getResourceAsStream("general/berg3.png");
				InputStream in20 = loader.getResourceAsStream("general/berg4.png");
				InputStream in21 = loader.getResourceAsStream("general/gras1.png");
				InputStream in22 = loader.getResourceAsStream("general/gras2.png");
				InputStream in23 = loader.getResourceAsStream("general/gras3.png");
				InputStream in24 = loader.getResourceAsStream("general/gras4.png");
				InputStream in25 = loader.getResourceAsStream("general/see.png");
				InputStream in26 = loader.getResourceAsStream("village/b1_left.png");
				InputStream in27 = loader.getResourceAsStream("village/b1.png");
				InputStream in28 = loader.getResourceAsStream("village/b2_left.png");
				InputStream in29 = loader.getResourceAsStream("village/b2.png");
				InputStream in30 = loader.getResourceAsStream("village/b3_left.png");
				InputStream in31 = loader.getResourceAsStream("village/b3.png");
				InputStream in32 = loader.getResourceAsStream("village/b4_left.png");
				InputStream in33 = loader.getResourceAsStream("village/b4.png");
				InputStream in34 = loader.getResourceAsStream("village/b5_left.png");
				InputStream in35 = loader.getResourceAsStream("village/b5.png");
				InputStream in36 = loader.getResourceAsStream("village/b6_left.png");
				InputStream in37 = loader.getResourceAsStream("village/b6.png");
				InputStream in38 = loader.getResourceAsStream("village/v1_left.png");
				InputStream in39 = loader.getResourceAsStream("village/v1.png");
				InputStream in40 = loader.getResourceAsStream("village/v2_left.png");
				InputStream in41 = loader.getResourceAsStream("village/v2.png");
				InputStream in42 = loader.getResourceAsStream("village/v3_left.png");
				InputStream in43 = loader.getResourceAsStream("village/v3.png");
				InputStream in44 = loader.getResourceAsStream("village/v4_left.png");
				InputStream in45 = loader.getResourceAsStream("village/v4.png");
				InputStream in46 = loader.getResourceAsStream("village/v5_left.png");
				InputStream in47 = loader.getResourceAsStream("village/v5.png");
				InputStream in48 = loader.getResourceAsStream("village/v6_left.png");
				InputStream in49 = loader.getResourceAsStream("village/v6.png");) {
			BufferedImage b1 = ImageIO.read(in1);
			BufferedImage b2 = ImageIO.read(in2);
			BufferedImage b3 = ImageIO.read(in3);
			BufferedImage b4 = ImageIO.read(in4);
			BufferedImage b5 = ImageIO.read(in5);
			BufferedImage b6 = ImageIO.read(in6);
			BufferedImage b7 = ImageIO.read(in7);
			BufferedImage b8 = ImageIO.read(in8);
			BufferedImage b9 = ImageIO.read(in9);
			BufferedImage b10 = ImageIO.read(in10);
			BufferedImage b11 = ImageIO.read(in11);
			BufferedImage b12 = ImageIO.read(in12);
			BufferedImage b13 = ImageIO.read(in13);
			BufferedImage b14 = ImageIO.read(in14);
			BufferedImage b15 = ImageIO.read(in15);
			BufferedImage b16 = ImageIO.read(in16);
			BufferedImage b17 = ImageIO.read(in17);
			BufferedImage b18 = ImageIO.read(in18);
			BufferedImage b19 = ImageIO.read(in19);
			BufferedImage b20 = ImageIO.read(in20);
			BufferedImage b21 = ImageIO.read(in21);
			BufferedImage b22 = ImageIO.read(in22);
			BufferedImage b23 = ImageIO.read(in23);
			BufferedImage b24 = ImageIO.read(in24);
			BufferedImage b25 = ImageIO.read(in25);
			BufferedImage b26 = ImageIO.read(in26);
			BufferedImage b27 = ImageIO.read(in27);
			BufferedImage b28 = ImageIO.read(in28);
			BufferedImage b29 = ImageIO.read(in29);
			BufferedImage b30 = ImageIO.read(in30);
			BufferedImage b31 = ImageIO.read(in31);
			BufferedImage b32 = ImageIO.read(in32);
			BufferedImage b33 = ImageIO.read(in33);
			BufferedImage b34 = ImageIO.read(in34);
			BufferedImage b35 = ImageIO.read(in35);
			BufferedImage b36 = ImageIO.read(in36);
			BufferedImage b37 = ImageIO.read(in37);
			BufferedImage b38 = ImageIO.read(in38);
			BufferedImage b39 = ImageIO.read(in39);
			BufferedImage b40 = ImageIO.read(in40);
			BufferedImage b41 = ImageIO.read(in41);
			BufferedImage b42 = ImageIO.read(in42);
			BufferedImage b43 = ImageIO.read(in43);
			BufferedImage b44 = ImageIO.read(in44);
			BufferedImage b45 = ImageIO.read(in45);
			BufferedImage b46 = ImageIO.read(in46);
			BufferedImage b47 = ImageIO.read(in47);
			BufferedImage b48 = ImageIO.read(in48);
			BufferedImage b49 = ImageIO.read(in49);
			result.put("forest0000", b1);
			result.put("forest0001", b2);
			result.put("forest0010", b3);
			result.put("forest0011", b4);
			result.put("forest0100", b5);
			result.put("forest0101", b6);
			result.put("forest0110", b7);
			result.put("forest0111", b8);
			result.put("forest1000", b9);
			result.put("forest1001", b10);
			result.put("forest1010", b11);
			result.put("forest1011", b12);
			result.put("forest1100", b13);
			result.put("forest1101", b14);
			result.put("forest1110", b15);
			result.put("forest1111", b16);
			result.put("berg1", b17);
			result.put("berg2", b18);
			result.put("berg3", b19);
			result.put("berg4", b20);
			result.put("gras1", b21);
			result.put("gras2", b22);
			result.put("gras3", b23);
			result.put("gras4", b24);
			result.put("see", b25);
			result.put("b1_left", b26);
			result.put("b1", b27);
			result.put("b2_left", b28);
			result.put("b2", b29);
			result.put("b3_left", b30);
			result.put("b3", b31);
			result.put("b4_left", b32);
			result.put("b4", b33);
			result.put("b5_left", b34);
			result.put("b5", b35);
			result.put("b6_left", b36);
			result.put("b6", b37);
			result.put("v1_left", b38);
			result.put("v1", b39);
			result.put("v2_left", b40);
			result.put("v2", b41);
			result.put("v3_left", b42);
			result.put("v3", b43);
			result.put("v4_left", b44);
			result.put("v4", b45);
			result.put("v5_left", b46);
			result.put("v5", b47);
			result.put("v6_left", b48);
			result.put("v6", b49);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	/*
	 * Getters and Setters
	 */

	public Map<String, BufferedImage> getAssetImageMap() {
		return assetImageBufferMap;
	}

	public Map<Integer, Map<Integer, Object>> getMapDataCache() {
		return mapDataCache;
	}

	public int getTileWidth() {
		return tileWidthPx;
	}

	public int getTileHeight() {
		return tileHeightPx;
	}

	public ViewPort getViewPort() {
		return viewPort;
	}

}