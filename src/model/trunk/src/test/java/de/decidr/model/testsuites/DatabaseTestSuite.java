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

package de.decidr.model.testsuites;

import static org.junit.Assert.fail;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.decidr.model.DecidrGlobalsTest;
import de.decidr.model.GlobalPreconditionsSuite;
import de.decidr.model.LifetimeValidatorTest;
import de.decidr.model.commands.SystemCommandsTest;
import de.decidr.model.commands.TenantCommandsTest;
import de.decidr.model.commands.WorkItemCommandsTest;
import de.decidr.model.commands.WorkflowInstanceCommandsTest;
import de.decidr.model.facades.SystemFacadeTest;
import de.decidr.model.facades.TenantFacadeTest;
import de.decidr.model.facades.UserFacadeTest;
import de.decidr.model.facades.WorkItemFacadeTest;
import de.decidr.model.facades.WorkflowInstanceFacadeTest;
import de.decidr.model.facades.WorkflowModelFacadeTest;
import de.decidr.model.storage.HibernateEntityStorageProviderTest;
import de.decidr.model.transactions.HibernateTransactionCoordinatorTest;

/**
 * This is the test suite for all test classes testing database interaction.<br>
 * It insures a working hibernate configuration and a consistent database for
 * each test.
 * 
 * @author Reinhold
 */
@RunWith(Suite.class)
// The order of these tests should not be changed without good reason
@SuiteClasses( { HibernateTransactionCoordinatorTest.class,
        DecidrGlobalsTest.class, LifetimeValidatorTest.class,
        SystemCommandsTest.class, TenantCommandsTest.class,
        WorkflowInstanceCommandsTest.class, WorkItemCommandsTest.class,
        SystemFacadeTest.class, TenantFacadeTest.class, UserFacadeTest.class,
        WorkflowModelFacadeTest.class, WorkflowInstanceFacadeTest.class,
        WorkItemFacadeTest.class, HibernateEntityStorageProviderTest.class,
        DatabaseTestSuite.class })
public class DatabaseTestSuite extends GlobalPreconditionsSuite {

    static Session session;
    private static String testString;
    private static final String RUNNING_CONSTANT = "is running";

    /**
     * Fails if hibernate is not working properly and no working condition can
     * be produced. Creates a testing database.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        try {
            session = new Configuration().configure("hibernate.cfg.xml")
                    .buildSessionFactory().openSession();
            session.createQuery("FROM User").list();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Couldn't connect to database; Error message: "
                    + e.getMessage());
        }

        testString = RUNNING_CONSTANT;
    }

    /**
     * Removes the testing database.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        testString = null;

        if (session != null) {
            // Query q = session.createSQLQuery("drop database decidrdb");
            // q.executeUpdate();
            session.close();
        }
    }

    /**
     * Checks whether the test suite is currently running. Can be used to fail
     * classes that need to run inside the suite when it isn't running.
     * 
     * @return - <code>true</code>, when this test suite is running,<br>
     *         - <code>false</code> when it isn't.
     */
    public static boolean running() {
        return RUNNING_CONSTANT.equals(testString);
    }
}
