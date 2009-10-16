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

import java.net.MalformedURLException;

import javax.xml.ws.WebFault;

//This class was generated by Apache CXF 2.1.3
//Fri Jun 12 16:46:57 CEST 2009
//Generated source version: 2.1.3
/**
 * Wrapper around <code>{@link MalformedURLException}</code> to be used by web
 * services.
 * 
 * @author Reinhold
 */
@WebFault(name = "malformedURLException", targetNamespace = "http://decidr.de/exceptions")
public class MalformedURLExceptionWrapper extends MalformedURLException {
    public static final long serialVersionUID = 20090612164657L;

    private java.lang.String malformedURLException;

    public MalformedURLExceptionWrapper() {
        super();
    }

    public MalformedURLExceptionWrapper(String message) {
        super(message);
    }

    public MalformedURLExceptionWrapper(String message,
            java.lang.String malformedURLException) {
        super(message);
        this.malformedURLException = malformedURLException;
    }

    public java.lang.String getFaultInfo() {
        return this.malformedURLException;
    }
}