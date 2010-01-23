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
 * Thrown whenever a failed transaction has been rolled back by the transaction
 * manager. There are several subtypes that specify the reason for the rollback.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * @version 0.1
 */
@WebFault(targetNamespace = "http://decidr.de/model/exceptions", name = "transactionException")
public class TransactionException extends Exception {

    private static final long serialVersionUID = 1L;
    private String serviceDetail = "";

    public TransactionException() {
        super();
    }

    public TransactionException(String message) {
        super(message);
    }

    /**
     * Implementation of {@link #TransactionException(String)} needed for
     * {@link WebFault} annotation.
     */
    public TransactionException(String message, String detail) {
        this(message);
        serviceDetail = detail;
    }

    /**
     * Implementation of {@link #TransactionException(String, Throwable)} needed
     * for {@link WebFault} annotation.
     */
    public TransactionException(String message, String detail, Throwable cause) {
        this(message, cause);
        serviceDetail = detail;
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }

    /**
     * Method returning {@link TransactionException#getFaultInfo} needed for
     * {@link WebFault} annotation.
     */
    public String getFaultInfo() {
        return serviceDetail;
    }
}