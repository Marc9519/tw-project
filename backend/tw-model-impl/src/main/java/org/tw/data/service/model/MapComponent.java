package org.tw.data.service.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Model for a map component
 */
@Entity
@Table(name = "MAPCOMPONENT", schema = "TW", catalog = "TW")
public class MapComponent implements Serializable{

	private static final long serialVersionUID = 3016331315734998534L;

	@EmbeddedId
	private MapComponentId id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MAPASSET_ID")
	private MapAsset mapAsset;

	/*
	 * Getters and Setters
	 */

	public MapComponentId getId() {
		return id;
	}

	public void setId(MapComponentId id) {
		this.id = id;
	}

	public MapAsset getMapAsset() {
		return mapAsset;
	}

	public void setMapAsset(MapAsset mapAsset) {
		this.mapAsset = mapAsset;
	}

}