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
import de.decidr.model.LifetimeValidatorTest;
import de.decidr.model.commands.SystemCommandsTest;
import de.decidr.model.facades.FileFacadeTest;
import de.decidr.model.facades.SystemFacadeTest;
import de.decidr.model.facades.TenantFacadeTest;
import de.decidr.model.facades.UserFacadeTest;
import de.decidr.model.facades.WorkItemFacadeTest;
import de.decidr.model.facades.WorkflowInstanceFacadeTest;
import de.decidr.model.facades.WorkflowModelFacadeTest;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.storage.HibernateEntityStorageProviderTest;
import de.decidr.model.testing.GlobalPreconditionsSuite;
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
@SuiteClasses( { HibernateEntityStorageProviderTest.class,
        HibernateTransactionCoordinatorTest.class, DecidrGlobalsTest.class,
        LifetimeValidatorTest.class, SystemFacadeTest.class,
        FileFacadeTest.class, TenantFacadeTest.class, UserFacadeTest.class,
        WorkflowModelFacadeTest.class, WorkflowInstanceFacadeTest.class,
        WorkItemFacadeTest.class, SystemCommandsTest.class })
public class DatabaseTestSuite extends GlobalPreconditionsSuite {

    static Session session;

    /**
     * Fails if hibernate is not working properly and no working condition can
     * be produced. Creates a testing database.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        DefaultLogger.getLogger(DatabaseTestSuite.class);

        try {
            session = new Configuration().configure("hibernate.cfg.xml")
                    .buildSessionFactory().openSession();
            session.createQuery("FROM User").setMaxResults(1).list();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Couldn't connect to database; Error message: "
                    + e.getMessage());
        }
    }

    /**
     * Removes the testing database.
     */
    @AfterClass
    public static void tearDownAfterClass() {
        if (session != null) {
            session.close();
        }
    }
}
