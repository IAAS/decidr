//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.07 at 10:09:06 PM MEZ 
//

package de.decidr.model.workflowmodel.bpel.varprop;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the de.decidr.model.workflowmodel.bpel.varprop
 * package.
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

    private final static QName _Query_QNAME = new QName(
            "http://docs.oasis-open.org/wsbpel/2.0/varprop", "query");

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package:
     * de.decidr.model.workflowmodel.bpel.varprop
     * 
     */
    public ObjectFactory() {
        // TODO document empty block
    }

    /**
     * Create an instance of {@link Query }
     * 
     */
    public Query createQuery() {
        return new Query();
    }

    /**
     * Create an instance of {@link ExtensibleElements }
     * 
     */
    public ExtensibleElements createExtensibleElements() {
        return new ExtensibleElements();
    }

    /**
     * Create an instance of {@link Documentation }
     * 
     */
    public Documentation createDocumentation() {
        return new Documentation();
    }

    /**
     * Create an instance of {@link PropertyAlias }
     * 
     */
    public PropertyAlias createPropertyAlias() {
        return new PropertyAlias();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Query }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wsbpel/2.0/varprop", name = "query")
    public JAXBElement<Query> createQuery(Query value) {
        return new JAXBElement<Query>(_Query_QNAME, Query.class, null, value);
    }

}
