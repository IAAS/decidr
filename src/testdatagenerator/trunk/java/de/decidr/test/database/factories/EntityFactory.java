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

package de.decidr.test.database.factories;

import java.util.Random;

import org.hibernate.Session;

/**
 * Abstract base class for all entity factories.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class EntityFactory {

    /**
     * Random number generator
     */
    protected static Random rnd = new Random();
    
    /**
     * Current Hibernate session
     */
    protected Session session = null;

    /**
     * Constructor
     * 
     * @param session
     *            current Hibernate Session
     */
    public EntityFactory(Session session) {
        this.session = session;
    }

    /**
     * @return the current Hibernate session
     */
    public Session getSession() {
        return session;
    }

}
