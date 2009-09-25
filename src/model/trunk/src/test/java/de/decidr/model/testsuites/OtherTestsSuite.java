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

import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.decidr.model.GlobalPreconditionsSuite;
import de.decidr.model.acl.PasswordTest;
import de.decidr.model.email.MailBackendTest;
import de.decidr.model.logging.DefaultLoggerTest;
import de.decidr.model.reveng.DecidrReverseEngineeringStrategyTest;

/**
 * JUnit <code>{@link TestSuite}</code> for tests that don't fit into any other
 * <code>{@link TestSuite TestSuites}</code>.
 * 
 * @author Reinhold
 */
@RunWith(Suite.class)
@SuiteClasses( { MailBackendTest.class, DefaultLoggerTest.class,
        PasswordTest.class, DecidrReverseEngineeringStrategyTest.class })
public class OtherTestsSuite extends GlobalPreconditionsSuite {
    // no pre-conditions or other things, so nothing to see here
}
