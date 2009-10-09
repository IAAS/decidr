package de.decidr.model.entities;

// Generated 09.10.2009 11:43:53 by Hibernate Tools 3.2.4.GA

/**
 * Activity generated by hbm2java
 */
public class Activity implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private KnownWebService knownWebService;
    private byte[] mapping;
    private String name;

    public Activity() {
    }

    public Activity(KnownWebService knownWebService, byte[] mapping, String name) {
        this.knownWebService = knownWebService;
        this.mapping = mapping;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public KnownWebService getKnownWebService() {
        return this.knownWebService;
    }

    public void setKnownWebService(KnownWebService knownWebService) {
        this.knownWebService = knownWebService;
    }

    public byte[] getMapping() {
        return this.mapping;
    }

    public void setMapping(byte[] mapping) {
        this.mapping = mapping;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
