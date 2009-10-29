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
 * This exception means that an error occurred during a reporting operation.
 * 
 * @author Reinhold
 */
@WebFault(name = "reportingException", targetNamespace = "http://decidr.de/model/soap/exceptions")
public class ReportingException extends Exception {
    public static final long serialVersionUID = 20090710155853L;

    private java.lang.String reportingException;

    public ReportingException() {
        super();
    }

    public ReportingException(String message) {
        super(message);
    }

    public ReportingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReportingException(String message, java.lang.String reportingException) {
        super(message);
        this.reportingException = reportingException;
    }

    public ReportingException(String message, java.lang.String reportingException,
            Throwable cause) {
        super(message, cause);
        this.reportingException = reportingException;
    }

    public java.lang.String getFaultInfo() {
        return this.reportingException;
    }
}
