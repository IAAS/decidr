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
 * Thrown to indicate that two authentication keys do not match.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
public class AuthKeyException extends TransactionException {

    private static final long serialVersionUID = 1L;

    public AuthKeyException() {
        super("Authentication key does not match.");
    }

    public AuthKeyException(String message) {
        super(message);
    }

    public AuthKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthKeyException(Throwable cause) {
        super(cause);
    }
}