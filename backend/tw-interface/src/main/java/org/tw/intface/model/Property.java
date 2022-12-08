package org.tw.intface.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Model for GENERAL.PROPERTY
 */
public class Property {

	private String key;
	private String value;

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(key).append(value).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Property)) {
			return false;
		}
		Property other = (Property) obj;
		return new EqualsBuilder().append(getKey(), other.getKey()).append(getValue(), other.getValue()).isEquals();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}