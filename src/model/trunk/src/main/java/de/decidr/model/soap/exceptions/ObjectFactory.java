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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the de.decidr.model.soap.exceptions package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private static final String EXCEPTION_NAMESPACE = "http://decidr.de/model/soap/exceptions";
    private final static QName _IoException_QNAME = new QName(
            EXCEPTION_NAMESPACE, "ioException");
    private final static QName _MalformedURLException_QNAME = new QName(
            EXCEPTION_NAMESPACE, "malformedURLException");
    private final static QName _MessagingException_QNAME = new QName(
            EXCEPTION_NAMESPACE, "messagingException");
    private final static QName _ReportingException_QNAME = new QName(
            EXCEPTION_NAMESPACE, "reportingException");
    private final static QName _IllegalArgumentException_QNAME = new QName(
            EXCEPTION_NAMESPACE, "illegalArgumentException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: de.decidr.model.soap.exceptions
     */
    public ObjectFactory() {
        // needed by JAXB
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = EXCEPTION_NAMESPACE, name = "ioException")
    public JAXBElement<String> createIoExceptionWrapper(String value) {
        return new JAXBElement<String>(_IoException_QNAME, String.class, null,
                value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = EXCEPTION_NAMESPACE, name = "malformedURLException")
    public JAXBElement<String> createMalformedURLExceptionWrapper(String value) {
        return new JAXBElement<String>(_MalformedURLException_QNAME,
                String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = EXCEPTION_NAMESPACE, name = "messagingException")
    public JAXBElement<String> createMessagingExceptionWrapper(String value) {
        return new JAXBElement<String>(_MessagingException_QNAME, String.class,
                null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = EXCEPTION_NAMESPACE, name = "reportingException")
    public JAXBElement<String> createReportingExceptionWrapper(String value) {
        return new JAXBElement<String>(_ReportingException_QNAME, String.class,
                null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = EXCEPTION_NAMESPACE, name = "illegalArgumentException")
    public JAXBElement<String> createIllegalArgumentExceptionWrapper(
            String value) {
        return new JAXBElement<String>(_IllegalArgumentException_QNAME,
                String.class, null, value);
    }
}
