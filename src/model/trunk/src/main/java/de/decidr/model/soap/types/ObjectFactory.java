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

package de.decidr.model.soap.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the de.decidr.model.soap.types package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Content_QNAME = new QName(
            "http://decidr.de/schema/DecidrProcessTypes", "content");
    private final static QName _User_QNAME = new QName(
            "http://decidr.de/schema/DecidrTypes", "user");
    private final static QName _Role_QNAME = new QName(
            "http://decidr.de/schema/DecidrTypes", "role");
    private final static QName _IntItem_QNAME = new QName(
            "http://decidr.de/schema/DecidrTypes", "intItem");
    private final static QName _Item_QNAME = new QName(
            "http://decidr.de/schema/DecidrTypes", "item");
    private final static QName _DateItem_QNAME = new QName(
            "http://decidr.de/schema/DecidrTypes", "dateItem");
    private final static QName _StringItem_QNAME = new QName(
            "http://decidr.de/schema/DecidrTypes", "stringItem");
    private final static QName _FloatItem_QNAME = new QName(
            "http://decidr.de/schema/DecidrTypes", "floatItem");
    private final static QName _Actor_QNAME = new QName(
            "http://decidr.de/schema/DecidrTypes", "actor");
    private final static QName _EmailUser_QNAME = new QName(
            "http://decidr.de/schema/DecidrTypes", "emailUser");
    private final static QName _HumanTaskData_QNAME = new QName(
            "http://decidr.de/schema/DecidrProcessTypes", "humanTaskData");
    private final static QName _ActorUser_QNAME = new QName(
            "http://decidr.de/schema/DecidrTypes", "actorUser");
    private final static QName _BooleanItem_QNAME = new QName(
            "http://decidr.de/schema/DecidrTypes", "booleanItem");
    private final static QName _RoleUser_QNAME = new QName(
            "http://decidr.de/schema/DecidrTypes", "roleUser");
    private final static QName _UriItem_QNAME = new QName(
            "http://decidr.de/schema/DecidrTypes", "uriItem");

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: de.decidr.model.soap.types
     */
    public ObjectFactory() {
        // needed by JAXB
    }

    /**
     * Create an instance of {@link IntItem }
     */
    public IntItem createTIntItem() {
        return new IntItem();
    }

    /**
     * Create an instance of {@link IDList }
     */
    public IDList createTIDList() {
        return new IDList();
    }

    /**
     * Create an instance of {@link ActorUser }
     */
    public ActorUser createTActorUser() {
        return new ActorUser();
    }

    /**
     * Create an instance of {@link Actor }
     */
    public Actor createTActor() {
        return new Actor();
    }

    /**
     * Create an instance of {@link RoleUser }
     */
    public RoleUser createTRoleUser() {
        return new RoleUser();
    }

    /**
     * Create an instance of {@link Information }
     */
    public Information createTInformation() {
        return new Information();
    }

    /**
     * Create an instance of {@link BooleanItem }
     */
    public BooleanItem createTBooleanItem() {
        return new BooleanItem();
    }

    /**
     * Create an instance of {@link HumanTaskData }
     */
    public HumanTaskData createTHumanTaskData() {
        return new HumanTaskData();
    }

    /**
     * Create an instance of {@link TaskItem }
     */
    public TaskItem createTaskItem() {
        return new TaskItem();
    }

    /**
     * Create an instance of {@link URIItem }
     */
    public URIItem createURIItem() {
        return new URIItem();
    }

    /**
     * Create an instance of {@link StringMapping }
     */
    public StringMapping createStringMapping() {
        return new StringMapping();
    }

    /**
     * Create an instance of {@link Role }
     */
    public Role createTRole() {
        return new Role();
    }

    /**
     * Create an instance of {@link Content }
     */
    public Content createTContent() {
        return new Content();
    }

    /**
     * Create an instance of {@link DateItem }
     */
    public DateItem createTDateItem() {
        return new DateItem();
    }

    /**
     * Create an instance of {@link StringMap }
     */
    public StringMap createTStringMap() {
        return new StringMap();
    }

    /**
     * Create an instance of {@link Parameters }
     */
    public Parameters createTParameters() {
        return new Parameters();
    }

    /**
     * Create an instance of {@link EmailUser }
     */
    public EmailUser createTEmailUser() {
        return new EmailUser();
    }

    /**
     * Create an instance of {@link AbstractUserList }
     */
    public AbstractUserList createTAbstractUserList() {
        return new AbstractUserList();
    }

    /**
     * Create an instance of {@link FloatItem }
     */
    public FloatItem createTFloatItem() {
        return new FloatItem();
    }

    /**
     * Create an instance of {@link StringItem }
     */
    public StringItem createTStringItem() {
        return new StringItem();
    }

    /**
     * Create an instance of {@link ItemList }
     */
    public ItemList createTItemList() {
        return new ItemList();
    }

    /**
     * Create an instance of {@link Parameter }
     */
    public Parameter createTParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link TaskIdentifier }
     */
    public TaskIdentifier createTaskIdentifier() {
        return new TaskIdentifier();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Content }{@code
     * >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrProcessTypes", name = "content")
    public JAXBElement<Content> createContent(Content value) {
        return new JAXBElement<Content>(_Content_QNAME, Content.class, null,
                value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AbstractUser }
     * {@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "user")
    public JAXBElement<AbstractUser> createUser(AbstractUser value) {
        return new JAXBElement<AbstractUser>(_User_QNAME, AbstractUser.class,
                null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Role }{@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "role")
    public JAXBElement<Role> createRole(Role value) {
        return new JAXBElement<Role>(_Role_QNAME, Role.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IntItem }{@code
     * >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "intItem", substitutionHeadNamespace = "http://decidr.de/schema/DecidrTypes", substitutionHeadName = "item")
    public JAXBElement<IntItem> createIntItem(IntItem value) {
        return new JAXBElement<IntItem>(_IntItem_QNAME, IntItem.class, null,
                value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Item }{@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "item")
    public JAXBElement<Item> createItem(Item value) {
        return new JAXBElement<Item>(_Item_QNAME, Item.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DateItem }
     * {@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "dateItem", substitutionHeadNamespace = "http://decidr.de/schema/DecidrTypes", substitutionHeadName = "item")
    public JAXBElement<DateItem> createDateItem(DateItem value) {
        return new JAXBElement<DateItem>(_DateItem_QNAME, DateItem.class, null,
                value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StringItem }
     * {@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "stringItem", substitutionHeadNamespace = "http://decidr.de/schema/DecidrTypes", substitutionHeadName = "item")
    public JAXBElement<StringItem> createStringItem(StringItem value) {
        return new JAXBElement<StringItem>(_StringItem_QNAME, StringItem.class,
                null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FloatItem }
     * {@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "floatItem", substitutionHeadNamespace = "http://decidr.de/schema/DecidrTypes", substitutionHeadName = "item")
    public JAXBElement<FloatItem> createFloatItem(FloatItem value) {
        return new JAXBElement<FloatItem>(_FloatItem_QNAME, FloatItem.class,
                null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Actor }{@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "actor")
    public JAXBElement<Actor> createActor(Actor value) {
        return new JAXBElement<Actor>(_Actor_QNAME, Actor.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EmailUser }
     * {@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "emailUser", substitutionHeadNamespace = "http://decidr.de/schema/DecidrTypes", substitutionHeadName = "user")
    public JAXBElement<EmailUser> createEmailUser(EmailUser value) {
        return new JAXBElement<EmailUser>(_EmailUser_QNAME, EmailUser.class,
                null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HumanTaskData }
     * {@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrProcessTypes", name = "humanTaskData")
    public JAXBElement<HumanTaskData> createHumanTaskData(HumanTaskData value) {
        return new JAXBElement<HumanTaskData>(_HumanTaskData_QNAME,
                HumanTaskData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActorUser }
     * {@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "actorUser", substitutionHeadNamespace = "http://decidr.de/schema/DecidrTypes", substitutionHeadName = "user")
    public JAXBElement<ActorUser> createActorUser(ActorUser value) {
        return new JAXBElement<ActorUser>(_ActorUser_QNAME, ActorUser.class,
                null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BooleanItem }
     * {@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "booleanItem", substitutionHeadNamespace = "http://decidr.de/schema/DecidrTypes", substitutionHeadName = "item")
    public JAXBElement<BooleanItem> createBooleanItem(BooleanItem value) {
        return new JAXBElement<BooleanItem>(_BooleanItem_QNAME,
                BooleanItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RoleUser }
     * {@code >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "roleUser", substitutionHeadNamespace = "http://decidr.de/schema/DecidrTypes", substitutionHeadName = "user")
    public JAXBElement<RoleUser> createRoleUser(RoleUser value) {
        return new JAXBElement<RoleUser>(_RoleUser_QNAME, RoleUser.class, null,
                value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link URIItem }{@code
     * >}
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "uriItem", substitutionHeadNamespace = "http://decidr.de/schema/DecidrTypes", substitutionHeadName = "item")
    public JAXBElement<URIItem> createUriItem(URIItem value) {
        return new JAXBElement<URIItem>(_UriItem_QNAME, URIItem.class, null,
                value);
    }
}
