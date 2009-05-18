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
 * element interface generated in the de.decidr.schema.decidrtypes package.
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

	private final static QName _Role_QNAME = new QName(
			"http://decidr.de/schema/DecidrTypes", "role");
	private final static QName _Actor_QNAME = new QName(
			"http://decidr.de/schema/DecidrTypes", "actor");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: de.decidr.schema.decidrtypes
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link TaskIdentifier }
	 * 
	 */
	public TaskIdentifier createCreateTaskResponse() {
		return new TaskIdentifier();
	}

	/**
	 * Create an instance of {@link URIItem }
	 * 
	 */
	public URIItem createTURIItem() {
		return new URIItem();
	}

	/**
	 * Create an instance of {@link StringItem }
	 * 
	 */
	public StringItem createTStringItem() {
		return new StringItem();
	}

	/**
	 * Create an instance of {@link IDList }
	 * 
	 */
	public IDList createTIDList() {
		return new IDList();
	}

	/**
	 * Create an instance of {@link IntegerItem }
	 * 
	 */
	public IntegerItem createTIntegerItem() {
		return new IntegerItem();
	}

	/**
	 * Create an instance of {@link Role }
	 * 
	 */
	public Role createTRole() {
		return new Role();
	}

	/**
	 * Create an instance of {@link Actor }
	 * 
	 */
	public Actor createTActor() {
		return new Actor();
	}

	/**
	 * Create an instance of {@link Item }
	 * 
	 */
	public Item createTItem() {
		return new Item();
	}

	/**
	 * Create an instance of {@link DateItem }
	 * 
	 */
	public DateItem createTDateItem() {
		return new DateItem();
	}

	/**
	 * Create an instance of {@link ItemList }
	 * 
	 */
	public ItemList createTItemList() {
		return new ItemList();
	}

	/**
	 * Create an instance of {@link BooleanItem }
	 * 
	 */
	public BooleanItem createTBooleanItem() {
		return new BooleanItem();
	}

	/**
	 * Create an instance of {@link FloatItem }
	 * 
	 */
	public FloatItem createTFloatItem() {
		return new FloatItem();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Role }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "role")
	public JAXBElement<Role> createRole(Role value) {
		return new JAXBElement<Role>(_Role_QNAME, Role.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Actor }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://decidr.de/schema/DecidrTypes", name = "actor")
	public JAXBElement<Actor> createActor(Actor value) {
		return new JAXBElement<Actor>(_Actor_QNAME, Actor.class, null, value);
	}
}
