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
package de.decidr.model.permissions;

/**
 * Base class for roles that are identified by an id.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class BasicRole implements Role {

    /**
     * Id of the person / system / pricipal that is requesting access to a
     * permission.
     */
    private Long actorId;

    /**
     * Constructor.
     * 
     * @param actorId
     *            Id of the person / system / pricipal that is requesting access
     *            to a permission.
     */
    public BasicRole(Long actorId) {
        super();
        this.actorId = actorId;
    }

    @Override
    public Long getActorId() {
        return this.actorId;
    }

    @Override
    public boolean equals(Object obj) {
        return ((obj != null) && (obj.getClass().equals(this.getClass())));
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getName().toString();
    }

}
