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

package de.decidr.model;

import static org.junit.Assert.fail;
import junit.framework.TestSuite;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.decidr.model.commands.SystemCommandsTest;
import de.decidr.model.transactions.HibernateTransactionCoordinatorTest;

/**
 * This is the abstract base class for all test classes testing database
 * interaction.<br>
 * It insures a working hibernate configuration and a consistent database for
 * each test.
 * 
 * @author Reinhold
 */
@RunWith(Suite.class)
@SuiteClasses( { DecidrGlobalsTest.class, LifetimeValidatorTest.class,
        HibernateTransactionCoordinatorTest.class, SystemCommandsTest.class })
public class DatabaseTestsuite extends TestSuite {

    static Session session;

    /**
     * Fails if hibernate is not working properly and no working condition can
     * be produced. Creates a testing database.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        try {
            session = new Configuration().configure("hibernate.cfg.xml")
                    .buildSessionFactory().openSession();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Couldn't connect to database");
        }
    }

    /**
     * Removes the testing database.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        if (session != null) {
            // Query q = session.createSQLQuery("drop database decidrdb");
            // q.executeUpdate();
            session.close();
        }
    }
}
