package org.tw.client.model;

import java.awt.Color;

public enum MiniMapEntity {

	PLAYER(new int[] { 130, 60, 10 }, new Color(130, 60, 10)),
	BORDER(new int[] { 48, 73, 14 }, new Color(48, 73, 14)),
	OBSTACLE(new int[] { 73, 103, 21 }, new Color(73, 103, 21)),
	GRASS(new int[] { 88, 118, 27 }, new Color(88, 118, 27)),
	LEFT(new int[] { 150, 150, 150 }, new Color(150, 150, 150)),
	BACKGROUND(new int[] { 67, 68, 19 }, new Color(67, 98, 19)),
	CONTILINE(new int[] { 0, 0, 0 }, new Color(0, 0, 0));

	private int[] rgb;
	private Color color;

	private MiniMapEntity(int[] rgb, Color color) {
		this.rgb = rgb;
		this.color = color;
	}

	public int[] getRgb() {
		return rgb;
	}

	public Color getColor() {
		return color;
	}

}