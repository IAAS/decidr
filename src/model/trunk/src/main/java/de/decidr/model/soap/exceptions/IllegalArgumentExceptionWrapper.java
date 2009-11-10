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

package de.decidr.model.soap.exceptions;

import javax.xml.ws.WebFault;

/**
 * Wrapper around <code>{@link IllegalArgumentException}</code> to be used by
 * web services.
 * 
 * @author Reinhold
 */
@WebFault(name = "illegalArgumentException", targetNamespace = "http://decidr.de/model/soap/exceptions")
public class IllegalArgumentExceptionWrapper extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;
    
    private String illegalArgumentException;

    public IllegalArgumentExceptionWrapper() {
        super();
    }

    public IllegalArgumentExceptionWrapper(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalArgumentExceptionWrapper(String s) {
        super(s);
    }

    public IllegalArgumentExceptionWrapper(String message,
            String illegalArgumentException, Throwable cause) {
        super(message, cause);
        this.illegalArgumentException = illegalArgumentException;
    }

    public IllegalArgumentExceptionWrapper(String s,
            String illegalArgumentException) {
        super(s);
        this.illegalArgumentException = illegalArgumentException;
    }

    public IllegalArgumentExceptionWrapper(Throwable cause) {
        super(cause);
    }

    public String getFaultInfo() {
        return this.illegalArgumentException;
    }
}
