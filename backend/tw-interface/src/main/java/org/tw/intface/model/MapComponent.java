package org.tw.intface.model;

/**
 * Models a map component
 */
public class MapComponent {

	/*
	 * The coordinates
	 */
	private Integer xPos;
	private Integer yPos;

	/*
	 * The referenced map asset
	 */
	private Long mapAssetId;

	/*
	 * Getters and setters
	 */

	public Integer getxPos() {
		return xPos;
	}

	public void setxPos(Integer xPos) {
		this.xPos = xPos;
	}

	public Integer getyPos() {
		return yPos;
	}

	public void setyPos(Integer yPos) {
		this.yPos = yPos;
	}

	public Long getMapAssetId() {
		return mapAssetId;
	}

	public void setMapAssetId(Long mapAssetId) {
		this.mapAssetId = mapAssetId;
	}

}