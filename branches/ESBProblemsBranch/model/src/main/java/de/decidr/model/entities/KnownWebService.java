package de.decidr.model.entities;

// Generated 16.11.2009 18:35:09 by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;

/**
 * KnownWebService generated by hbm2java
 */
public class KnownWebService implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Set<Activity> activities = new HashSet<Activity>(0);

    public KnownWebService() {
        //default empty JavaBean constructor
    }

    public KnownWebService(String name) {
        //generated minimal constructor
        this.name = name;
    }

    public KnownWebService(String name, Set<Activity> activities) {
        //generated full constructor
        this.name = name;
        this.activities = activities;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Activity> getActivities() {
        return this.activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

}