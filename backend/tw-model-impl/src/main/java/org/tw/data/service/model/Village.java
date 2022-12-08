package org.tw.data.service.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Model for a village
 */
@Entity
@Table(name = "village", schema = "tw", catalog = "tw")
public class Village implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "VILLAGE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "VILLAGE_NR")
	private Integer villageNr;

	@Column(name = "XPOS")
	private Integer xPos;

	@Column(name = "YPOS")
	private Integer yPos;

	@Column(name = "NAME")
	private String name;

	@Column(name = "POINTS")
	private Integer points;

	@Column(name = "CURRENT_WOOD")
	private Integer currentWood;

	@Column(name = "CURRENT_CLAY")
	private Integer currentClay;

	@Column(name = "CURRENT_IRON")
	private Integer currentIron;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PLAYER_ID")
	private Player player;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BONUS_ID")
	private Bonus bonus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVillageNr() {
		return villageNr;
	}

	public void setVillageNr(Integer villageNr) {
		this.villageNr = villageNr;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getCurrentWood() {
		return currentWood;
	}

	public void setCurrentWood(Integer currentWood) {
		this.currentWood = currentWood;
	}

	public Integer getCurrentClay() {
		return currentClay;
	}

	public void setCurrentClay(Integer currentClay) {
		this.currentClay = currentClay;
	}

	public Integer getCurrentIron() {
		return currentIron;
	}

	public void setCurrentIron(Integer currentIron) {
		this.currentIron = currentIron;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Bonus getBonus() {
		return bonus;
	}

	public void setBonus(Bonus bonus) {
		this.bonus = bonus;
	}

}