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
 * Model for a Player
 */
@Entity
@Table(name = "player", schema = "tw", catalog = "tw")
public class Player implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "PLAYER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "PLAYER_NR")
	private Integer playerNr;

	@Column(name = "NAME")
	private String name;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "PASS")
	private String pass;

	@Column(name = "PROFILE_TEXT")
	private String profileText;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TRIBE_ID")
	private Tribe tribe;

}