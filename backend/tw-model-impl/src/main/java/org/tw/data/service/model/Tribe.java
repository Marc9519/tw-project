package org.tw.data.service.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Model for a tribe
 */
@Entity
@Table(name = "TRIBE", schema = "TW", catalog = "TW")
public class Tribe implements Serializable {

	private static final long serialVersionUID = -6176307985963390195L;

	@Id
	@Column(name = "TRIBE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "TRIBE_NR")
	private Integer tribeNr;

	@Column(name = "NAME")
	private String name;

	@Column(name = "TAG")
	private String tag;

	@Column(name = "DESCRIPTION")
	private String description;

}