package org.tw.data.service.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MapComponentId implements Serializable{

	private static final long serialVersionUID = -5698099291303628330L;

	@Column(name = "XPOS")
	private Integer xPos;

	@Column(name = "YPOS")
	private Integer yPos;

	/*
	 * Getters and Setters
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

}