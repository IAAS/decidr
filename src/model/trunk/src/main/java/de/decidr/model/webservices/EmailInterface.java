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
package de.decidr.model.webservices;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.mail.MessagingException;
import javax.xml.bind.TypeConstraintException;
import javax.xml.namespace.QName;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.soap.exceptions.IoExceptionWrapper;
import de.decidr.model.soap.exceptions.MalformedURLExceptionWrapper;
import de.decidr.model.soap.exceptions.MessagingExceptionWrapper;
import de.decidr.model.soap.types.AbstractUser;
import de.decidr.model.soap.types.AbstractUserList;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.StringMap;

/**
 * The DecidR e-mail interface. It allows sending an e-mail message to a list of
 * recipients with specified attachments.
 * 
 * @author Reinhold
 */
@WebService(targetNamespace = EmailInterface.TARGET_NAMESPACE, name = EmailInterface.PORT_TYPE_NAME)
public interface EmailInterface {

    public static final String PORT_TYPE_NAME = "EmailPT";
    public static final String SERVICE_NAME = "Email";
    public static final String TARGET_NAMESPACE = "http://decidr.de/webservices/Email";
    public final static QName SERVICE = new QName(TARGET_NAMESPACE,
            SERVICE_NAME);
    public final static QName ENDPOINT = new QName(TARGET_NAMESPACE,
            "EmailSOAP");

    /**
     * The web service operation used to send an e-mail.
     * 
     * @param to
     *            A list of main recipients.
     * @param cc
     *            A list of recipients of copies. May be <code>null</code>.
     * @param bcc
     *            A list of hidden recipients. May be <code>null</code>.
     * @param fromName
     *            The name of the sender. May be <code>null</code>.
     * @param fromAddress
     *            The e-mail address of the sender. May be <code>null</code>.
     * @param subject
     *            The subject of the e-mail.
     *            <em>Must not be <code>null</code></em>.
     * @param headers
     *            A list of headers and their values.
     * @param bodyTXT
     *            The body of the e-mail (text/plain). May be <code>null</code>
     *            if <code>bodyHTML</code> is set.
     * @param bodyHTML
     *            The body of the e-mail (text/html). May be <code>null</code>
     *            if <code>bodyTXT</code> is set.
     * @param attachments
     *            A list of file references to files that should be attached to
     *            the e-mail.
     * @throws MessagingExceptionWrapper
     *             May be treated like <code>{@link MessagingException}</code>.
     * @throws MalformedURLExceptionWrapper
     *             May be treated like
     *             <code>{@link MalformedURLException}</code>.
     * @throws TransactionException
     *             Thrown by the <code>{@link de.decidr.model}</code>.
     * @throws IoExceptionWrapper
     *             May be treated like <code>{@link IOException}</code>.
     * @throws IllegalArgumentException
     *             Thrown whenever a parameter contains illegal values or wasn't
     *             set even though it should be.
     * @throws TypeConstraintException
     *             If an unknown subclass of <code>{@link AbstractUser}</code>
     *             is contained in a <code>{@link AbstractUserList}</code>.
     */
    @WebMethod(action = TARGET_NAMESPACE + "/sendEmail", operationName = "sendEmail")
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
