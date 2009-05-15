
package de.decidr.schema.decidrtypes;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.decidr.schema.decidrtypes package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Role_QNAME = new QName("http://decidr.de/schema/DecidrTypes", "role");
    private final static QName _Actor_QNAME = new QName("http://decidr.de/schema/DecidrTypes", "actor");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.decidr.schema.decidrtypes
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TURIItem }
     * 
     */
    public TURIItem createTURIItem() {
        return new TURIItem();
    }

    /**
     * Create an instance of {@link TStringItem }
     * 
     */
    public TStringItem createTStringItem() {
        return new TStringItem();
    }

    /**
     * Create an instance of {@link TIDList }
     * 
     */
    public TIDList createTIDList() {
        return new TIDList();
    }

    /**
     * Create an instance of {@link TIntegerItem }
     * 
     */
    public TIntegerItem createTIntegerItem() {
        return new TIntegerItem();
    }

    /**
     * Create an instance of {@link TRole }
     * 
     */
    public TRole createTRole() {
        return new TRole();
    }

    /**
     * Create an instance of {@link TActor }
     * 
     */
    public TActor createTActor() {
        return new TActor();
    }

    /**
     * Create an instance of {@link TItem }
     * 
     */
    public TItem createTItem() {
        return new TItem();
    }

    /**
     * Create an instance of {@link TDateItem }
     * 
     */
    public TDateItem createTDateItem() {
        return new TDateItem();
    }

    /**
     * Create an instance of {@link TItemList }
     * 
     */
    public TItemList createTItemList() {
        return new TItemList();
    }

    /**
     * Create an instance of {@link TBooleanItem }
     * 
     */
    public TBooleanItem createTBooleanItem() {
        return new TBooleanItem();
    }

    /**
     * Create an instance of {@link TFloatItem }
     * 
     */
    public TFloatItem createTFloatItem() {
        return new TFloatItem();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "role")
    public JAXBElement<TRole> createRole(TRole value) {
        return new JAXBElement<TRole>(_Role_QNAME, TRole.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TActor }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "actor")
    public JAXBElement<TActor> createActor(TActor value) {
        return new JAXBElement<TActor>(_Actor_QNAME, TActor.class, null, value);
    }

}
