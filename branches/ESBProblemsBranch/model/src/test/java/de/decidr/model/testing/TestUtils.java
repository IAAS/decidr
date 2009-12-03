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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import junit.framework.TestSuite;

/**
 * Class containing utility methods that can be used in all tests.
 * 
 * @author Reinhold
 */
public class TestUtils {

    public static void executeStaticMethodsWithAnnotation(
            Class<? extends TestSuite> suite, Class<? extends Annotation> ann) {
        for (Method m : suite.getMethods()) {
            if (!Modifier.isStatic(m.getModifiers())
                    && m.getParameterTypes().length == 0) {
                continue;
            }

            if (m.isAnnotationPresent(ann)) {
                try {
                    m.invoke(null);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void executeMethodsWithAnnotation(TestSuite suite,
            Class<? extends Annotation> ann) {
        for (Method m : suite.getClass().getMethods()) {
            if (Modifier.isStatic(m.getModifiers())
                    && m.getParameterTypes().length == 0) {
                continue;
            }

            if (m.isAnnotationPresent(ann)) {
                try {
                    m.invoke(suite);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
