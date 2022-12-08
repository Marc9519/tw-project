package org.tw.intface.model;

/**
 * Models a map asset
 */
public class MapAsset {

	private Long id;
	private String name;
	private boolean isObstacle;


	@Override
	public String toString() {
		return name;
	}

	/*
	 * Getters and setters
	 */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getObstacle() {
		return isObstacle;
	}

	public void setIsObstacle(boolean isObstacle) {
		this.isObstacle = isObstacle;
	}

}