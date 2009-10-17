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

package de.decidr.model.soap.wrappers;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.decidr.model.soap.types.AbstractUserList;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.StringMap;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;to&quot; type=&quot;{http://decidr.de/schema/DecidrTypes}AbstractUserList&quot;/&gt;
 *         &lt;element name=&quot;cc&quot; type=&quot;{http://decidr.de/schema/DecidrTypes}AbstractUserList&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;bcc&quot; type=&quot;{http://decidr.de/schema/DecidrTypes}AbstractUserList&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;fromName&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;fromAddress&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;subject&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot;/&gt;
 *         &lt;element name=&quot;headers&quot; type=&quot;{http://decidr.de/schema/DecidrWSTypes}tStringMap&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;bodyTXT&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;bodyHTML&quot; type=&quot;{http://www.w3.org/2001/XMLSchema}string&quot; minOccurs=&quot;0&quot;/&gt;
 *         &lt;element name=&quot;attachments&quot; type=&quot;{http://decidr.de/schema/DecidrWSTypes}tIDList&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "to", "cc", "bcc", "fromName", "fromAddress",
        "subject", "headers", "bodyTXT", "bodyHTML", "attachments" })
@XmlRootElement(name = "sendEmail", namespace = "http://decidr.de/webservices/Email")
public class SendEmail {

    @XmlElement(namespace = "http://decidr.de/webservices/Email", required = true)
    protected AbstractUserList to;
    @XmlElementRef(name = "cc", namespace = "http://decidr.de/webservices/Email", type = JAXBElement.class)
    protected JAXBElement<AbstractUserList> cc;
    @XmlElementRef(name = "bcc", namespace = "http://decidr.de/webservices/Email", type = JAXBElement.class)
    protected JAXBElement<AbstractUserList> bcc;
    @XmlElementRef(name = "fromName", namespace = "http://decidr.de/webservices/Email", type = JAXBElement.class)
    protected JAXBElement<String> fromName;
    @XmlElement(namespace = "http://decidr.de/webservices/Email", required = true)
    protected String fromAddress;
    @XmlElement(namespace = "http://decidr.de/webservices/Email", required = true)
    protected String subject;
    @XmlElementRef(name = "headers", namespace = "http://decidr.de/webservices/Email", type = JAXBElement.class)
    protected JAXBElement<StringMap> headers;
    @XmlElementRef(name = "bodyTXT", namespace = "http://decidr.de/webservices/Email", type = JAXBElement.class)
    protected JAXBElement<String> bodyTXT;
    @XmlElementRef(name = "bodyHTML", namespace = "http://decidr.de/webservices/Email", type = JAXBElement.class)
    protected JAXBElement<String> bodyHTML;
    @XmlElementRef(name = "attachments", namespace = "http://decidr.de/webservices/Email", type = JAXBElement.class)
    protected JAXBElement<IDList> attachments;

    /**
     * Gets the value of the to property.
     * 
     * @return possible object is {@link AbstractUserList }
     */
    public AbstractUserList getTo() {
        return to;
    }

    /**
     * Sets the value of the to property.
     * 
     * @param value
     *            allowed object is {@link AbstractUserList }
     */
    public void setTo(AbstractUserList value) {
        this.to = value;
    }

    /**
     * Gets the value of the cc property.
     * 
     * @return possible object is {@link JAXBElement }{@code <}
     *         {@link AbstractUserList }{@code >}
     */
    public JAXBElement<AbstractUserList> getCc() {
        return cc;
    }

    /**
     * Sets the value of the cc property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}
     *            {@link AbstractUserList }{@code >}
     */
    public void setCc(JAXBElement<AbstractUserList> value) {
        this.cc = value;
    }

    /**
     * Gets the value of the bcc property.
     * 
     * @return possible object is {@link JAXBElement }{@code <}
     *         {@link AbstractUserList }{@code >}
     */
    public JAXBElement<AbstractUserList> getBcc() {
        return bcc;
    }

    /**
     * Sets the value of the bcc property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}
     *            {@link AbstractUserList }{@code >}
     */
    public void setBcc(JAXBElement<AbstractUserList> value) {
        this.bcc = value;
    }

    /**
     * Gets the value of the fromName property.
     * 
     * @return possible object is {@link JAXBElement }{@code <}{@link String }
     *         {@code >}
     */
    public JAXBElement<String> getFromName() {
        return fromName;
    }

    /**
     * Sets the value of the fromName property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }
     *            {@code >}
     */
    public void setFromName(JAXBElement<String> value) {
        this.fromName = value;
    }

    /**
     * Gets the value of the fromAddress property.
     * 
     * @return possible object is {@link String }
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * Sets the value of the fromAddress property.
     * 
     * @param value
     *            allowed object is {@link String }
     */
    public void setFromAddress(String value) {
        this.fromAddress = value;
    }

    /**
     * Gets the value of the subject property.
     * 
     * @return possible object is {@link String }
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *            allowed object is {@link String }
     */
    public void setSubject(String value) {
        this.subject = value;
    }

    /**
     * Gets the value of the headers property.
     * 
     * @return possible object is {@link JAXBElement }{@code <}{@link TStringMap }
     *         {@code >}
     */
    public JAXBElement<StringMap> getHeaders() {
        return headers;
    }

    /**
     * Sets the value of the headers property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}
     *            {@link StringMap }{@code >}
     */
    public void setHeaders(JAXBElement<StringMap> value) {
        this.headers = value;
    }

    /**
     * Gets the value of the bodyTXT property.
     * 
     * @return possible object is {@link JAXBElement }{@code <}{@link String }
     *         {@code >}
     */
    public JAXBElement<String> getBodyTXT() {
        return bodyTXT;
    }

    /**
     * Sets the value of the bodyTXT property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }
     *            {@code >}
     */
    public void setBodyTXT(JAXBElement<String> value) {
        this.bodyTXT = value;
    }

    /**
     * Gets the value of the bodyHTML property.
     * 
     * @return possible object is {@link JAXBElement }{@code <}{@link String }
     *         {@code >}
     */
    public JAXBElement<String> getBodyHTML() {
        return bodyHTML;
    }

    /**
     * Sets the value of the bodyHTML property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link String }
     *            {@code >}
     */
    public void setBodyHTML(JAXBElement<String> value) {
        this.bodyHTML = value;
    }

    /**
     * Gets the value of the attachments property.
     * 
     * @return possible object is {@link JAXBElement }{@code <}{@link IDList }
     *         {@code >}
     */
    public JAXBElement<IDList> getAttachments() {
        return attachments;
    }

    /**
     * Sets the value of the attachments property.
     * 
     * @param value
     *            allowed object is {@link JAXBElement }{@code <}{@link IDList }
     *            {@code >}
     */
    public void setAttachments(JAXBElement<IDList> value) {
        this.attachments = value;
    }
}
