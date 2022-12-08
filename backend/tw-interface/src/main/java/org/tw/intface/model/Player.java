package org.tw.intface.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Model for TW.PLAYER
 */
public class Player {

	private Long id;
	private String playerNr;
	private String name;
	private String email;
	private String pass;
	private String profileText;
	private Long tribeId;

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,37).append(playerNr).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Player)) {
			return false;
		}
		Player other = (Player) obj;
		return new EqualsBuilder().append(this.getPlayerNr(), other.getPlayerNr()).isEquals();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlayerNr() {
		return playerNr;
	}

	public void setPlayerNr(String playerNr) {
		this.playerNr = playerNr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getProfileText() {
		return profileText;
	}

	public void setProfileText(String profileText) {
		this.profileText = profileText;
	}

	public Long getTribeId() {
		return tribeId;
	}

	public void setTribeId(Long tribeId) {
		this.tribeId = tribeId;
	}

}