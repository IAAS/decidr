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

import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.decidr.model.DecidrGlobalsTest;
import de.decidr.model.LifetimeValidatorTest;
import de.decidr.model.acl.DefaultAccessControlListTest;
import de.decidr.model.acl.asserters.FileAccessAsserterTest;
import de.decidr.model.acl.asserters.IsRoleEqualToAccessedUserAsserterTest;
import de.decidr.model.acl.asserters.UserIsAvailableAsserterTest;
import de.decidr.model.acl.asserters.UserIsEnabledAsserterTest;
import de.decidr.model.acl.asserters.UserIsLoggedInAsserterTest;
import de.decidr.model.acl.asserters.UserIsSuperAdminAsserterTest;
import de.decidr.model.commands.SystemCommandsTest;
import de.decidr.model.facades.FileFacadeTest;
import de.decidr.model.facades.SystemFacadeTest;
import de.decidr.model.facades.TenantFacadeTest;
import de.decidr.model.facades.UserFacadeTest;
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
        WorkflowModelFacadeTest.class, SystemCommandsTest.class,
        DefaultAccessControlListTest.class,
        IsRoleEqualToAccessedUserAsserterTest.class,
        // UserAdministratesWorkflowModelAsserterTest.class,
        UserIsAvailableAsserterTest.class, UserIsEnabledAsserterTest.class,
        // UserIsInvitationReceiverAsserterTest.class,
        UserIsLoggedInAsserterTest.class, UserIsSuperAdminAsserterTest.class,
        FileAccessAsserterTest.class })
// UserIsTenantAdminAsserterTest.class,
// UserIsTenantMemberAsserterTest.class,
// UserIsWorkflowParticipantAsserterTest.class,
// UserNotParticipatingInAnyWorkflowAsserterTest.class})
public class DatabaseTestSuite extends GlobalPreconditionsSuite {

    static Calendar nextAttempt;
    private static boolean inSuite;

    /**
     * Fails if hibernate is not working properly and no working environment can
     * be produced.
     * <p>
     * If a working environment is found, attempting a connection within the
     * next five minutes will be skipped to increase testing speed.
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        if (!inSuite
                && (nextAttempt == null || Calendar.getInstance().after(
                        nextAttempt))) {
            Session session = null;
            // force initialisation of logging subsystem
            DefaultLogger.getLogger(DatabaseTestSuite.class);

            // check whether a database connection can be established
            try {
                session = new Configuration().configure("hibernate.cfg.xml")
                        .buildSessionFactory().openSession();
                // get 1 user to force hibernate to actually attempt a database
                // connection
                session.createQuery("FROM User").setMaxResults(1).list();
            } catch (Exception e) {
                e.printStackTrace();
                fail("Couldn't connect to database; Error message: "
                        + e.getMessage());
            } finally {
                if (session != null) {
                    session.close();
                }
            }

            // don't retry for 5 minutes if a connection was successfully
            // established
            nextAttempt = Calendar.getInstance();
            nextAttempt.add(Calendar.MINUTE, 5);

            inSuite = true;
        }
    }

    /**
     * @return - <code>true</code>, if a {@link DatabaseTestSuite} is currently
     *         running<br>
     *         - <code>false</code> if not
     */
    public static final boolean isInSuite() {
        return inSuite;
    }

    @AfterClass
    public static void deactivateSuite() {
        inSuite = false;
    }
}
