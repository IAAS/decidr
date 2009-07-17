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

package de.decidr.model.exceptions;

/**
 * When this exception is thrown, either a configuration is incomplete or not
 * applicable.
 * 
 * @author Reinhold
 */
public class IncompleteConfigurationException extends Exception {

    private static final long serialVersionUID = 1L;

    public IncompleteConfigurationException() {
        super();
    }

    public IncompleteConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncompleteConfigurationException(String message) {
        super(message);
    }

    public IncompleteConfigurationException(Throwable cause) {
        super(cause);
    }
}
