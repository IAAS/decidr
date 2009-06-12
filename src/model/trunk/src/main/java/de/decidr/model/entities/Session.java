package de.decidr.model.entities;

// Generated 12.06.2009 08:13:27 by Hibernate Tools 3.2.4.GA

/**
 * Session generated by hbm2java
 */
public class Session implements java.io.Serializable {

    private String sessionId;
    private String app;
    private byte[] data;
    private long lastAccessed;
    private int maxInactive;
    private boolean valid;

    public Session() {
    }

    public Session(String sessionId, long lastAccessed, int maxInactive,
            boolean valid) {
        this.sessionId = sessionId;
        this.lastAccessed = lastAccessed;
        this.maxInactive = maxInactive;
        this.valid = valid;
    }

    public Session(String sessionId, String app, byte[] data,
            long lastAccessed, int maxInactive, boolean valid) {
        this.sessionId = sessionId;
        this.app = app;
        this.data = data;
        this.lastAccessed = lastAccessed;
        this.maxInactive = maxInactive;
        this.valid = valid;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getApp() {
        return this.app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getLastAccessed() {
        return this.lastAccessed;
    }

    public void setLastAccessed(long lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public int getMaxInactive() {
        return this.maxInactive;
    }

    public void setMaxInactive(int maxInactive) {
        this.maxInactive = maxInactive;
    }

    public boolean isValid() {
        return this.valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    // The following is extra code specified in the hbm.xml files
    private static final long serialVersionUID = 1L;
    // end of extra code specified in the hbm.xml files

}
