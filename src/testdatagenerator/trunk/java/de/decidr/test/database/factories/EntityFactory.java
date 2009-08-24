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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.hibernate.Session;

import de.decidr.model.DecidrGlobals;
import de.decidr.test.database.main.ProgressListener;

/**
 * Abstract base class for all entity factories.
 * 
 * @author Daniel Huss
 * @version 0.1
 */
public class EntityFactory {

    /**
     * The approximate number of seconds in a year for use with getRandomDate.
     */
    public static final int SPAN_YEAR = 365 * 24 * 60 * 60;

    /**
     * The number of seconds in a week for use with getRandomDate.
     */
    public static final int SPAN_WEEK = 7 * 24 * 60 * 60;

    /**
     * Random number generator
     */
    protected static Random rnd = new Random();

    /**
     * Current Hibernate session
     */
    protected Session session = null;

    /**
     * Progress event receiver
     */
    private ProgressListener progressListener;

    /**
     * Constructor
     * 
     * @param session
     *            current Hibernate Session
     */
    public EntityFactory(Session session) {
        this(session, null);
    }

    /**
     * Constructor
     * 
     * @param session
     *            current Hibernate session
     * @param progressListener
     *            event receiver for progress events
     */
    public EntityFactory(Session session, ProgressListener progressListener) {
        this.session = session;
        this.progressListener = progressListener;
    }

    /**
     * @return the current Hibernate session
     */
    public Session getSession() {
        return session;
    }

    /**
     * @return a random date +-1 year from now
     */
    public Date getRandomDate(Boolean allowFuture, Boolean allowPast,
            int spanSeconds) {
        Calendar result = DecidrGlobals.getTime();
        ArrayList<Integer> multiplicators = new ArrayList<Integer>();

        spanSeconds = Math.abs(spanSeconds);

        if (!allowFuture && !allowPast) {
            multiplicators.add(0);
        } else {
            if (allowFuture) {
                multiplicators.add(1);
            }
            if (allowPast) {
                multiplicators.add(-1);
            }
        }

        // multiplicators is now never empty
        int multiplicator = multiplicators.get(rnd.nextInt(multiplicators
                .size()));

        result.add(Calendar.SECOND, multiplicator * rnd.nextInt(spanSeconds));
        return result.getTime();
    }

    /**
     * @return a (not so large) blob that contains the binary UTF-8
     *         representation of the string "empty".
     */
    public byte[] getBlobStub() {
        return "empty".getBytes(Charset.forName("UTF-8"));
    }

    /**
     * Fires a progress event.
     * 
     * @param totalItems
     * @param doneItems
     */
    protected void fireProgressEvent(int totalItems, int doneItems) {
        if (progressListener != null) {
            progressListener.reportProgress(totalItems, doneItems);
        }
    }
}
