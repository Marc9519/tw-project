package org.tw.data.service.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Model for a Bonus
 */
@Entity
@Table(name = "BONUS", schema = "GENERAL", catalog = "GENERAL")
public class Bonus implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "BONUS_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "BONUS_KEY")
	private String bonusKey;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "ASSET_NAME")
	private String assetName;

}