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
import javax.jws.soap.SOAPBinding;
import javax.mail.MessagingException;
import javax.xml.namespace.QName;
import javax.xml.ws.RequestWrapper;

import de.decidr.model.exceptions.IncompleteConfigurationException;
import de.decidr.model.exceptions.StorageException;
import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.soap.exceptions.IllegalArgumentExceptionWrapper;
import de.decidr.model.soap.exceptions.IoExceptionWrapper;
import de.decidr.model.soap.exceptions.MalformedURLExceptionWrapper;
import de.decidr.model.soap.exceptions.MessagingExceptionWrapper;
import de.decidr.model.soap.types.AbstractUserList;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.StringMap;
import de.decidr.model.storage.StorageProvider;

/**
 * The DecidR e-mail interface. It allows sending an e-mail message to a list of
 * recipients with specified attachments.
 * 
 * @author Reinhold
 */
// @HandlerChain(file = "handler-chain.xml")
@WebService(targetNamespace = EmailInterface.TARGET_NAMESPACE, portName = EmailInterface.PORT_NAME, wsdlLocation = "Email.wsdl", name = EmailInterface.SERVICE_NAME, serviceName = EmailInterface.SERVICE_NAME)
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED, style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public interface EmailInterface {

    public static final String PORT_NAME = "EmailSOAP11";
    public static final String SERVICE_NAME = "Email";
    public static final String PORT_TYPE_NAME = "EmailPT";
    public static final String TARGET_NAMESPACE = "http://decidr.de/webservices/Email";
    public final static QName SERVICE = new QName(TARGET_NAMESPACE,
            SERVICE_NAME);
    public final static QName ENDPOINT = new QName(TARGET_NAMESPACE, PORT_NAME);

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
     * @throws IncompleteConfigurationException
     *             Thrown if the <code>{@link StorageProvider StorageProvider's}</code>
     *             configuration doesn't contain some values it needs.
     * @throws StorageException
     *             Thrown if there was an exception in the storage backend (e.g.
     *             an <code>{@link IOException}</code>)
     */
    @WebMethod(action = "urn:sendEmail", operationName = "sendEmail")
    @RequestWrapper(localName = "sendEmail", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.model.soap.wrappers.SendEmail")
    //@ResponseWrapper(localName = "sendEmailResponse", targetNamespace = TARGET_NAMESPACE, className = "de.decidr.model.soap.wrappers.SendEmailResponse")
    public void sendEmail(
            @WebParam(name = "to", targetNamespace = "http://decidr.de/webservices/Email") AbstractUserList to,
            @WebParam(name = "cc", targetNamespace = "http://decidr.de/webservices/Email") AbstractUserList cc,
            @WebParam(name = "bcc", targetNamespace = "http://decidr.de/webservices/Email") AbstractUserList bcc,
            @WebParam(name = "fromName", targetNamespace = "http://decidr.de/webservices/Email") String fromName,
            @WebParam(name = "fromAddress", targetNamespace = "http://decidr.de/webservices/Email") String fromAddress,
            @WebParam(name = "subject", targetNamespace = "http://decidr.de/webservices/Email") String subject,
            @WebParam(name = "headers", targetNamespace = "http://decidr.de/webservices/Email") StringMap headers,
            @WebParam(name = "bodyTXT", targetNamespace = "http://decidr.de/webservices/Email") String bodyTXT,
            @WebParam(name = "bodyHTML", targetNamespace = "http://decidr.de/webservices/Email") String bodyHTML,
            @WebParam(name = "attachments", targetNamespace = "http://decidr.de/webservices/Email") IDList attachments)
            throws MessagingExceptionWrapper, MalformedURLExceptionWrapper,
            TransactionException, IoExceptionWrapper,
            IllegalArgumentExceptionWrapper, StorageException,
            IncompleteConfigurationException;
}
