package org.tw.client.model;

import java.awt.Point;
import java.io.Serializable;

/**
 * Model for a ViewPort
 */
public class ViewPort implements Serializable {

	private static final long serialVersionUID = 1L;
	private final int tileWidthPx;
	private final int tileHeightPx;

	/*
	 * OuterFrameSize (tiles)
	 */
	private final int outerFrameWidth;
	private final int outerFrameHeight;

	/*
	 * InnerFrameSize (tiles)
	 */
	private final int innerFrameSize;

	/*
	 * OuterFrameLocation (tiles)
	 */
	private int outerFrameLocationX;
	private int outerFrameLocationY;
	/*
	 * InnerFrameLocation (Px)
	 */
	private int innerFrameLocationXPx;
	private int innerFrameLocationYPx;

	/*
	 * GlobalMapCenter (Tiles)
	 */
	private Point currentMapCenterPoint;
	/*
	 * The inner frames center point in PX (relative to center Tile)
	 */
	private Point innerFrameCenterPos;

	public ViewPort(int tileWidthPx, int tileHeightPx, int outerFrameWidth, int outerFrameHeight, Point centerPoint) {
		this.tileWidthPx = tileWidthPx;
		this.tileHeightPx = tileHeightPx;
		this.outerFrameWidth = outerFrameWidth;
		this.outerFrameHeight = outerFrameHeight;
		this.innerFrameSize = 9;
		this.currentMapCenterPoint = centerPoint;
		moveToCenter();
		calculateInnerFramePos();
	}

	private void calculateInnerFramePos() {
		int dist = (int) Math.ceil(((double) outerFrameWidth - innerFrameSize) / 2);
		this.innerFrameLocationXPx = dist * tileWidthPx;
		this.innerFrameLocationYPx = dist * tileHeightPx;
		int xPos =(int) Math.round((double)tileWidthPx /2);
		int yPos =(int) Math.round((double)tileHeightPx /2);
		this.innerFrameCenterPos = new Point(xPos, yPos);
	}

	private void moveToCenter() {
		int centerX = currentMapCenterPoint.x;
		int centerY = currentMapCenterPoint.y;
		int halfWidth  = outerFrameWidth/2;
		int halfHeight = outerFrameHeight/2;
		this.outerFrameLocationX = centerX - halfWidth;
		this.outerFrameLocationY = centerY - halfHeight;
	}

	public int executeShiftHorizontally(int dX) {
		int prevCenterX = innerFrameCenterPos.x;
		int sumVal = prevCenterX + dX;
		int targetCenterX = sumVal;
		int diff = 0;
		if (sumVal < 0) {
			// Left shift
			targetCenterX = Math.floorMod(sumVal, tileWidthPx);
			double ratio = (double) sumVal / tileWidthPx;
			diff = (int) Math.floor(ratio);
		} else if (sumVal >= tileWidthPx) {
			// Right shift
			targetCenterX = sumVal % tileWidthPx;
			diff = sumVal / tileWidthPx;
		}
		this.outerFrameLocationX += diff;
		this.currentMapCenterPoint.x += diff;
		this.innerFrameCenterPos.x = targetCenterX;
		int shiftVal = targetCenterX - prevCenterX;
		this.innerFrameLocationXPx += shiftVal;
		return diff;
	}

	public int executeShiftVertically(int dY) {
		int prevCenterY = innerFrameCenterPos.y;
		int sumVal = prevCenterY + dY;
		int targetCenterY = sumVal;
		int diff = 0;
		if (sumVal < 0) {
			// Up shift
			targetCenterY = Math.floorMod(sumVal, tileHeightPx);
			double ratio = (double) sumVal / tileHeightPx;
			diff = (int) Math.floor(ratio);
		} else if (sumVal >= tileHeightPx) {
			// Down shift
			targetCenterY = sumVal % tileHeightPx;
			diff = sumVal / tileHeightPx;
		}
		this.outerFrameLocationY += diff;
		this.currentMapCenterPoint.y += diff;
		this.innerFrameCenterPos.y = targetCenterY;
		int shiftVal = targetCenterY - prevCenterY;
		this.innerFrameLocationYPx += shiftVal;
		return diff;
	}

	public Point getMapLocByInnerFrameOffsets(int locXPx, int locYPx) {
		int offsetX = (innerFrameLocationXPx + locXPx) / tileWidthPx;
		int offsetY = (innerFrameLocationYPx + locYPx) / tileHeightPx;
		int targetLocX = outerFrameLocationX + offsetX;
		int targetLocY = outerFrameLocationY + offsetY;
		return new Point(targetLocX, targetLocY);
	}

	/*
	 * Getters and Setters
	 */

	public int getOuterFrameLocationX() {
		return outerFrameLocationX;
	}

	public void setOuterFrameLocationX(int outerFrameLocationX) {
		this.outerFrameLocationX = outerFrameLocationX;
	}

	public int getOuterFrameLocationY() {
		return outerFrameLocationY;
	}

	public void setOuterFrameLocationY(int outerFrameLocationY) {
		this.outerFrameLocationY = outerFrameLocationY;
	}

	public int getInnerFrameLocationXPx() {
		return innerFrameLocationXPx;
	}

	public void setInnerFrameLocationXPx(int innerFrameLocationXPx) {
		this.innerFrameLocationXPx = innerFrameLocationXPx;
	}

	public int getInnerFrameLocationYPx() {
		return innerFrameLocationYPx;
	}

	public void setInnerFrameLocationYPx(int innerFrameLocationYPx) {
		this.innerFrameLocationYPx = innerFrameLocationYPx;
	}

	public int getOuterFrameWidth() {
		return outerFrameWidth;
	}

	public int getOuterFrameHeight() {
		return outerFrameHeight;
	}

	public int getTileWidthPx() {
		return tileWidthPx;
	}

	public int getTileHeightPx() {
		return tileHeightPx;
	}

	public Point getInnerFrameCenterPos() {
		return innerFrameCenterPos;
	}

	public Point getCurrentMapCenterPoint() {
		return currentMapCenterPoint;
	}

	public int getInnerFrameSize() {
		return innerFrameSize;
	}

}