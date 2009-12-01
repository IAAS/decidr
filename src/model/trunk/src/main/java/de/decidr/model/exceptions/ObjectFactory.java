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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

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

    private static final String EXCEPTION_NAMESPACE = "http://decidr.de/model/exceptions";

    private final static QName _TransactionException_QNAME = new QName(
            EXCEPTION_NAMESPACE, "transactionException");

    private final static QName _StorageException_QNAME = new QName(
            EXCEPTION_NAMESPACE, "storageException");

    private final static QName _IncompleteConfigurationException_QNAME = new QName(
            EXCEPTION_NAMESPACE, "incompleteConfiguratioException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: de.decidr.webservices.email
     */
    public ObjectFactory() {
        // needed by JAXB
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = EXCEPTION_NAMESPACE, name = "transactionException")
    public JAXBElement<String> createTransactionException(String value) {
        return new JAXBElement<String>(_TransactionException_QNAME,
                String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = EXCEPTION_NAMESPACE, name = "storageException")
    public JAXBElement<String> createStorageException(String value) {
        return new JAXBElement<String>(_StorageException_QNAME, String.class,
                null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     */
    @XmlElementDecl(namespace = EXCEPTION_NAMESPACE, name = "incompleteConfiguratioException")
    public JAXBElement<String> createIncompleteConfigurationException(
            String value) {
        return new JAXBElement<String>(_IncompleteConfigurationException_QNAME,
                String.class, null, value);
    }
}
