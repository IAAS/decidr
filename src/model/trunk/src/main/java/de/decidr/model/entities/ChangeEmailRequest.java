package de.decidr.model.entities;

// Generated 15.05.2009 17:31:29 by Hibernate Tools 3.2.2.GA

import java.util.Date;

/**
 * ChangeEmailRequest generated by hbm2java
 */
public class ChangeEmailRequest implements java.io.Serializable {

	private long userId;
	private User user;
	private String newEmail;
	private Date creationDate;
	private String authKey;

	public ChangeEmailRequest() {
	}

	public ChangeEmailRequest(long userId, User user, String newEmail,
			Date creationDate, String authKey) {
		this.userId = userId;
		this.user = user;
		this.newEmail = newEmail;
		this.creationDate = creationDate;
		this.authKey = authKey;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getNewEmail() {
		return this.newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getAuthKey() {
		return this.authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

}