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

package de.decidr.model.testing;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import de.decidr.model.testsuites.OtherTestsSuite;

/**
 * RR: add comment
 * 
 * @author Reinhold
 */
public abstract class DecidrOthersTest {

    @BeforeClass
    public static final void beforeClass() {
        TestUtils.executeMethodsWithAnnotation(OtherTestsSuite.class,
                BeforeClass.class);
    }

    @AfterClass
    public static final void afterClass() {
        TestUtils.executeMethodsWithAnnotation(OtherTestsSuite.class,
                AfterClass.class);
    }

    @Before
    public final void beforeTest() {
        TestUtils.executeMethodsWithAnnotation(OtherTestsSuite.class,
                Before.class);
    }

    @After
    public final void afterTest() {
        TestUtils.executeMethodsWithAnnotation(OtherTestsSuite.class,
                After.class);
    }
}
