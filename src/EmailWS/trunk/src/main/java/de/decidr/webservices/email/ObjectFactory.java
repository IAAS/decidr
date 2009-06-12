package de.decidr.webservices.email;

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
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _IoException_QNAME = new QName(
            "http://decidr.de/webservices/Email", "ioException");
    private final static QName _MalformedURLException_QNAME = new QName(
            "http://decidr.de/webservices/Email", "malformedURLException");
    private final static QName _MessagingException_QNAME = new QName(
            "http://decidr.de/webservices/Email", "messagingException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: de.decidr.webservices.email
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/webservices/Email", name = "ioException")
    public JAXBElement<String> createIoExceptionWrapper(String value) {
        return new JAXBElement<String>(_IoException_QNAME, String.class, null,
                value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/webservices/Email", name = "malformedURLException")
    public JAXBElement<String> createMalformedURLExceptionWrapper(String value) {
        return new JAXBElement<String>(_MalformedURLException_QNAME,
                String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/webservices/Email", name = "messagingException")
    public JAXBElement<String> createMessagingExceptionWrapper(String value) {
        return new JAXBElement<String>(_MessagingException_QNAME, String.class,
                null, value);
    }
}
