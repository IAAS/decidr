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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.07 at 09:21:14 PM MEZ 
//

package de.decidr.model.workflowmodel.bpel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tImport complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="tImport">
 *   &lt;complexContent>
 *     &lt;extension base="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tExtensibleElements">
 *       &lt;attribute name="namespace" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="location" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="importType" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tImport")
public class Import extends ExtensibleElements {

    @XmlAttribute
    @XmlSchemaType(name = "anyURI")
    protected String namespace;
    @XmlAttribute
    @XmlSchemaType(name = "anyURI")
    protected String location;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String importType;

    /**
     * Gets the value of the importType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getImportType() {
        return importType;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the value of the namespace property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getNamespace() {
        return namespace;
    }

    public boolean isSetImportType() {
        return (this.importType != null);
    }

    public boolean isSetLocation() {
        return (this.location != null);
    }

    public boolean isSetNamespace() {
        return (this.namespace != null);
    }

    /**
     * Sets the value of the importType property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setImportType(String value) {
        this.importType = value;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Sets the value of the namespace property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setNamespace(String value) {
        this.namespace = value;
    }

}