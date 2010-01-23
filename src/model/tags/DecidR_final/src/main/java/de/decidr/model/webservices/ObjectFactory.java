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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import de.decidr.model.soap.types.AbstractUserList;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.StringMap;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the de.decidr.webservices.email package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SendEmailBodyTXT_QNAME = new QName(
            "http://decidr.de/webservices/Email", "bodyTXT");
    private final static QName _SendEmailHeaders_QNAME = new QName(
            "http://decidr.de/webservices/Email", "headers");
    private final static QName _SendEmailBodyHTML_QNAME = new QName(
            "http://decidr.de/webservices/Email", "bodyHTML");
    private final static QName _SendEmailAttachments_QNAME = new QName(
            "http://decidr.de/webservices/Email", "attachments");
    private final static QName _SendEmailFromName_QNAME = new QName(
            "http://decidr.de/webservices/Email", "fromName");
    private final static QName _SendEmailBcc_QNAME = new QName(
            "http://decidr.de/webservices/Email", "bcc");
    private final static QName _SendEmailCc_QNAME = new QName(
            "http://decidr.de/webservices/Email", "cc");

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: de.decidr.webservices.email
     */
    public ObjectFactory() {
        // needed by JAXB
    }

    /**
     * Create an instance of {@link CreateTask }
     */
    public CreateTask createCreateTask() {
        return new CreateTask();
    }

    /**
     * Create an instance of {@link CreateTaskResponse }
     */
    public CreateTaskResponse createCreateTaskResponse() {
        return new CreateTaskResponse();
    }

    /**
     * Create an instance of {@link RemoveTask }
     */
    public RemoveTask createRemoveTask() {
        return new RemoveTask();
    }

    /**
     * Create an instance of {@link RemoveTaskResponse }
     */
    public RemoveTaskResponse createRemoveTaskResponse() {
        return new RemoveTaskResponse();
    }

    /**
     * Create an instance of {@link RemoveTasks }
     */
    public RemoveTasks createRemoveTasks() {
        return new RemoveTasks();
    }

    /**
     * Create an instance of {@link RemoveTasksResponse }
     */
    public RemoveTasksResponse createRemoveTasksResponse() {
        return new RemoveTasksResponse();
    }

    /**
     * Create an instance of {@link SendEmail }
     */
    public SendEmail createSendEmail() {
        return new SendEmail();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IDList }{@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/webservices/Email", name = "attachments", scope = SendEmail.class)
    public JAXBElement<IDList> createSendEmailAttachments(IDList value) {
        return new JAXBElement<IDList>(_SendEmailAttachments_QNAME,
                IDList.class, SendEmail.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}
     * {@link AbstractUserList }{@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/webservices/Email", name = "bcc", scope = SendEmail.class)
    public JAXBElement<AbstractUserList> createSendEmailBcc(
            AbstractUserList value) {
        return new JAXBElement<AbstractUserList>(_SendEmailBcc_QNAME,
                AbstractUserList.class, SendEmail.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/webservices/Email", name = "bodyHTML", scope = SendEmail.class)
    public JAXBElement<String> createSendEmailBodyHTML(String value) {
        return new JAXBElement<String>(_SendEmailBodyHTML_QNAME, String.class,
                SendEmail.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/webservices/Email", name = "bodyTXT", scope = SendEmail.class)
    public JAXBElement<String> createSendEmailBodyTXT(String value) {
        return new JAXBElement<String>(_SendEmailBodyTXT_QNAME, String.class,
                SendEmail.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}
     * {@link AbstractUserList }{@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/webservices/Email", name = "cc", scope = SendEmail.class)
    public JAXBElement<AbstractUserList> createSendEmailCc(
            AbstractUserList value) {
        return new JAXBElement<AbstractUserList>(_SendEmailCc_QNAME,
                AbstractUserList.class, SendEmail.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/webservices/Email", name = "fromName", scope = SendEmail.class)
    public JAXBElement<String> createSendEmailFromName(String value) {
        return new JAXBElement<String>(_SendEmailFromName_QNAME, String.class,
                SendEmail.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StringMap }
     * {@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/webservices/Email", name = "headers", scope = SendEmail.class)
    public JAXBElement<StringMap> createSendEmailHeaders(StringMap value) {
        return new JAXBElement<StringMap>(_SendEmailHeaders_QNAME,
                StringMap.class, SendEmail.class, value);
    }

    /**
     * Create an instance of {@link SendEmailResponse }
     */
    public SendEmailResponse createSendEmailResponse() {
        return new SendEmailResponse();
    }

    /**
     * Create an instance of {@link TaskCompleted }
     */
    public TaskCompleted createTaskCompleted() {
        return new TaskCompleted();
    }

    /**
     * Create an instance of {@link TaskCompletedResponse }
     */
    public TaskCompletedResponse createTaskCompletedResponse() {
        return new TaskCompletedResponse();
    }
}
