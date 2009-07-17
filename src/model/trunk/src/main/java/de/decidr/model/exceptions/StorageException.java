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
 * Thrown upon file storage backend failures.
 * 
 * @author Daniel Huss
 * @author Markus Fischer
 * @version 0.1
 */
@WebFault(targetNamespace = "http://decidr.de/exceptions", name = "storageException")
public class StorageException extends Exception {

    private String serviceDetail = "";
    private static final long serialVersionUID = 1L;

    /**
     * Implementation of {@link #TransactionException(String, Throwable)} needed
     * for {@link WebFault} annotation.
     */
    public StorageException(String message, String detail, Throwable cause) {
        this(message, cause);
        serviceDetail = detail;
    }

    /**
     * Implementation of {@link #TransactionException(String)} needed for
     * {@link WebFault} annotation.
     */
    public StorageException(String message, String detail) {
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

    public StorageException() {
        super();
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageException(String message) {
        super(message);
    }

    public StorageException(Throwable cause) {
        super(cause);
    }
}
