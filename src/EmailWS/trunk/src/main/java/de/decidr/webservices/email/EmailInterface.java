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
package de.decidr.webservices.email;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Action;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.soap.exceptions.IoExceptionWrapper;
import de.decidr.model.soap.exceptions.MalformedURLExceptionWrapper;
import de.decidr.model.soap.exceptions.MessagingExceptionWrapper;
import de.decidr.model.soap.types.AbstractUserList;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.StringMap;

@WebService(targetNamespace = "http://decidr.de/webservices/Email", name = "EmailPT")
public interface EmailInterface {

    @Action(input = "urn:sendEmail", output = "urn:sendEmailResponse", fault = {})
    @WebMethod(action = "http://decidr.de/webservices/Email/sendEmail")
    public void sendEmail(
            @WebParam(name = "to", targetNamespace = "") AbstractUserList to,
            @WebParam(name = "cc", targetNamespace = "") AbstractUserList cc,
            @WebParam(name = "bcc", targetNamespace = "") AbstractUserList bcc,
            @WebParam(name = "fromName", targetNamespace = "") String fromName,
            @WebParam(name = "fromAddress", targetNamespace = "") String fromAddress,
            @WebParam(name = "subject", targetNamespace = "") String subject,
            @WebParam(name = "headers", targetNamespace = "") StringMap headers,
            @WebParam(name = "bodyTXT", targetNamespace = "") String bodyTXT,
            @WebParam(name = "bodyHTML", targetNamespace = "") String bodyHTML,
            @WebParam(name = "attachments", targetNamespace = "") IDList attachments)
            throws MessagingExceptionWrapper, MalformedURLExceptionWrapper,
            TransactionException, IoExceptionWrapper, IllegalArgumentException;
}
