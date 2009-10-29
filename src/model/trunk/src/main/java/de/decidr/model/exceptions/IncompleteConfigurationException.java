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

import javax.xml.ws.WebFault;

/**
 * When this exception is thrown, either a configuration is incomplete or not
 * applicable.
 * 
 * @author Reinhold
 */
@WebFault(targetNamespace = "http://decidr.de/model/exceptions", name = "incompleteConfigurationException")
public class IncompleteConfigurationException extends Exception {
    private String serviceDetail = "";

    /**
     * Implementation of {@link #TransactionException(String, Throwable)} needed
     * for {@link WebFault} annotation.
     */
    public IncompleteConfigurationException(String message, String detail, Throwable cause) {
        this(message, cause);
        serviceDetail = detail;
    }

    /**
     * Implementation of {@link #TransactionException(String)} needed for
     * {@link WebFault} annotation.
     */
    public IncompleteConfigurationException(String message, String detail) {
        this(message);
        serviceDetail = detail;
    }

    /**
     * Method returning {@link TransactionException#serviceDetail} needed for
     * {@link WebFault} annotation.
     */
    String getServiceDetail() {
        return serviceDetail;
    }

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
