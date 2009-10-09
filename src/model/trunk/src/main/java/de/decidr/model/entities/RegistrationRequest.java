package de.decidr.model.entities;

// Generated 09.10.2009 15:53:47 by Hibernate Tools 3.2.4.GA

import java.util.Date;

/**
 * RegistrationRequest generated by hbm2java
 */
public class RegistrationRequest implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long userId;
    private User user;
    private Date creationDate;
    private String authKey;

    public RegistrationRequest() {
    }

    public RegistrationRequest(User user, Date creationDate, String authKey) {
        this.user = user;
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
