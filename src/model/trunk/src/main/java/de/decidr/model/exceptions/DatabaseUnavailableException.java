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
 * TODO: add comment
 * 
 * @author TODO
 */
public class DatabaseUnavailableException extends TransactionException {
    private static final long serialVersionUID = 1L;

    public DatabaseUnavailableException() {
        super();
    }

    public DatabaseUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseUnavailableException(String message) {
        super(message);
    }

    public DatabaseUnavailableException(Throwable cause) {
        super(cause);
    }
}
