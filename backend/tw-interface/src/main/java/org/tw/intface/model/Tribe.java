package org.tw.intface.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Model for TW.TRIBE
 */
public class Tribe {

	private Long id;
	private String tribeNr;
	private String name;
	private String tag;
	private String description;

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,37).append(tribeNr).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Tribe)) {
			return false;
		}
		Tribe other = (Tribe) obj;
		return new EqualsBuilder().append(this.getTribeNr(), other.getTribeNr()).isEquals();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTribeNr() {
		return tribeNr;
	}

	public void setTribeNr(String tribeNr) {
		this.tribeNr = tribeNr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}