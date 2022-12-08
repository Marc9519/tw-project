package org.tw.client.controller;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tw.client.model.MiniMapEntity;
import org.tw.data.service.model.MapAsset;
import org.tw.data.service.model.Village;

/**
 * The Map Controller
 */
public class MiniMapController implements Serializable {

	private static final long serialVersionUID = 1L;

	private transient Map<Integer, Map<Integer, Object>> mapDataCache;

	/*
	 * The logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(MiniMapController.class);



	public MiniMapController(Map<Integer, Map<Integer, Object>> mapDataCache) {
		this.mapDataCache = mapDataCache;
	}

	/**
	 *  Public API for handling the drag event
	 * @param dragX
	 * @param dragY
	 * @param dragStartPoint
	 * @return
	 */
	public Map<String, Object> handleDragEventData(int dragX, int dragY, Point dragStartPoint) {
		return Collections.emptyMap();
	}

	public Village handleMoveEventData(int x, int y) {
		return null;
	}

	public BufferedImage createCurrentMiniMapExcerpt() {
		int fromX = 500;
		int toX = 550;
		int fromY = 500;
		int toY = 550;
		int diffX = toX - fromX;
		int diffY = toY - fromY;
		int excerptWidthPx = diffX * 5;
		int excerptHeightPx = diffY * 5;
		BufferedImage result = new BufferedImage(excerptWidthPx, excerptHeightPx , BufferedImage.TYPE_INT_ARGB);
		Graphics g = result.getGraphics();
		// Background
		g.setColor(MiniMapEntity.BACKGROUND.getColor());
		g.fillRect(0, 0, excerptWidthPx, excerptHeightPx);
		// Border
		drawBorder(g, excerptWidthPx, excerptHeightPx);
		// Grass + OBSTACLES
		Map<Point, Village> villageMap = drawGrassAndObstacles(g, fromX, toX, fromY, toY);
		// ContiLine
		drawContiLines(g, fromX, toX, fromY, toY, excerptWidthPx, excerptHeightPx);
		// Villages
		drawVillages(g, fromX, fromY, villageMap);
		g.dispose();
		return result;
	}

	private void drawVillages(Graphics g, int fromX, int fromY, Map<Point, Village> villageMap) {
		for (Entry<Point, Village> e : villageMap.entrySet()) {
			Point key = e.getKey();
			Village village = e.getValue();
			boolean isPlayer = village.getPlayer() != null;
			MiniMapEntity entity = isPlayer ? MiniMapEntity.PLAYER : MiniMapEntity.LEFT;
			int x = key.x;
			int y = key.y;
			int diffX = x - fromX;
			int diffY = y - fromY;
			int xPos = diffX * 5 + 1;
			int yPos = diffY * 5 + 1;
			g.setColor(entity.getColor());
			g.fillRect(xPos, yPos, 4, 4);
		}
	}

	private void drawContiLines(Graphics g, int fromX, int toX, int fromY, int toY, int excerptWidthPx, int excerptHeightPx) {
		List<Integer> yOffsets = IntStream.range(fromY, toY).boxed().filter(i -> i % 100 == 0)
				.map(i -> (i - fromY) * 5).collect(Collectors.toList());
		List<Integer> xOffsets = IntStream.range(fromX, toX).boxed().filter(i -> i % 100 == 0)
				.map(i -> (i - fromX) * 5).collect(Collectors.toList());
		g.setColor(MiniMapEntity.CONTILINE.getColor());
		for (Integer yOffset : yOffsets) {
			g.fillRect(0, yOffset, excerptWidthPx, 2);
		}
		for (Integer xOffset : xOffsets) {
			g.fillRect(xOffset, 0, 2, excerptHeightPx);
		}
	}

	private void drawBorder(Graphics g, int excerptWidthPx, int excerptHeightPx) {
		g.setColor(MiniMapEntity.BORDER.getColor());

		for (int i = 0; i < excerptWidthPx; i += 25) {
			g.fillRect(i, 0, 1, excerptHeightPx);
		}
		for (int y = 0; y < excerptHeightPx; y += 25) {
			for (int x = 1; x < excerptWidthPx; x += 5) {
				g.fillRect(x, y, 4, 1);
			}
		}
	}

	private Map<Point, Village> drawGrassAndObstacles(Graphics g, int fromX, int toX, int fromY, int toY) {
		Map<Point, Village> villageMap = new LinkedHashMap<>();
		for (int x = fromX; x < toX; x++) {
			int diffX = x - fromX;
			for (int y = fromY; y < toY; y++) {
				int diffY = y - fromY;
				Object object = mapDataCache.get(x).get(y);
				if (object instanceof MapAsset) {
					// DRAW GRASS + OBSTACLE
					MapAsset m = (MapAsset) object;
					boolean isObstacke = m.isObstacle();
					MiniMapEntity e = isObstacke ? MiniMapEntity.OBSTACLE : MiniMapEntity.GRASS;
					g.setColor(e.getColor());
					int xPos = diffX *5+1;
					int yPos = diffY *5+1;
					g.fillRect(xPos, yPos, 4, 4);
				} else {
					// Collect villages
					Village v = (Village) object;
					villageMap.put(new Point(x,y), v);
				}
			}
		}
		return villageMap;
	}

}