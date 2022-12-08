package org.tw.intface.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Model for TW.VILLAGE
 */
public class Village {

	private Long id;
	private String villageNr;
	private String name;
	private Integer xPos;
	private Integer yPos;
	private Integer points;
	private Integer continent;
	private Long playerId;
	private Long bonusId;

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,37).append(villageNr).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Village)) {
			return false;
		}
		Village other = (Village) obj;
		return new EqualsBuilder().append(this.getVillageNr(), other.getVillageNr()).isEquals();
	}

	@Override
	public String toString() {
		return name + " (" + xPos + "|" + yPos + ")";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVillageNr() {
		return villageNr;
	}

	public void setVillageNr(String villageNr) {
		this.villageNr = villageNr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public Long getBonusId() {
		return bonusId;
	}

	public void setBonusId(Long bonusId) {
		this.bonusId = bonusId;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getContinent() {
		return continent;
	}

	public void setContinent(Integer continent) {
		this.continent = continent;
	}

}