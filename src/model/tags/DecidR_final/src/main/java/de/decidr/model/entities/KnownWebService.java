/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.decidr.model.entities;

// Generated 22.01.2010 14:51:15 by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;

/**
 * KnownWebService generated by hbm2java.
 */
public class KnownWebService implements java.io.Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    private Long id;

    /** The name. */
    private String name;

    /** The activities. */
    private Set<Activity> activities = new HashSet<Activity>(0);

    /**
     * Instantiates a new known web service.
     */
    public KnownWebService() {
        // default empty JavaBean constructor
    }

    /**
     * Instantiates a new known web service.
     * 
     * @param name
     *            the name
     */
    public KnownWebService(String name) {
        // generated minimal constructor
        this.name = name;
    }

    /**
     * Instantiates a new known web service.
     * 
     * @param name
     *            the name
     * @param activities
     *            the activities
     */
    public KnownWebService(String name, Set<Activity> activities) {
        // generated full constructor
        this.name = name;
        this.activities = activities;
    }

    /**
     * Gets the activities.
     * 
     * @return the activities
     */
    public Set<Activity> getActivities() {
        return this.activities;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the activities.
     * 
     * @param activities
     *            the new activities
     */
    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            the new name
     */
    public void setName(String name) {
        this.name = name;
    }

}