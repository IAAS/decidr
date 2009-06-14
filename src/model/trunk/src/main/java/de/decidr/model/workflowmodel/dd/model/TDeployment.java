//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.06.14 at 01:53:05 PM GMT+01:00 
//


package de.decidr.model.workflowmodel.dd.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;


/**
 * <p>Java class for tDeployment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tDeployment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="process" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *                   &lt;element name="retired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *                   &lt;element name="in-memory" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *                   &lt;element name="property" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/sequence>
 *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="process-events" type="{http://www.apache.org/ode/schemas/dd/2007/03}tProcessEvents" minOccurs="0"/>
 *                   &lt;element name="provide" type="{http://www.apache.org/ode/schemas/dd/2007/03}tProvide" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="invoke" type="{http://www.apache.org/ode/schemas/dd/2007/03}tInvoke" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="mex-interceptors" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="mex-interceptor" type="{http://www.apache.org/ode/schemas/dd/2007/03}tMexInterceptor" maxOccurs="unbounded" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}QName" minOccurs="0"/>
 *                   &lt;element name="cleanup" type="{http://www.apache.org/ode/schemas/dd/2007/03}tCleanup" maxOccurs="3" minOccurs="0"/>
 *                   &lt;element name="schedule" type="{http://www.apache.org/ode/schemas/dd/2007/03}tSchedule" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *                 &lt;attribute name="fileName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="bpel11wsdlFileName" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDeployment", propOrder = {
    "process"
})
public class TDeployment {

    protected List<TDeployment.Process> process;

    /**
     * Gets the value of the process property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the process property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcess().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TDeployment.Process }
     * 
     * 
     */
    public List<TDeployment.Process> getProcess() {
        if (process == null) {
            process = new ArrayList<TDeployment.Process>();
        }
        return this.process;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *         &lt;element name="retired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *         &lt;element name="in-memory" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *         &lt;element name="property" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
     *                 &lt;/sequence>
     *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="process-events" type="{http://www.apache.org/ode/schemas/dd/2007/03}tProcessEvents" minOccurs="0"/>
     *         &lt;element name="provide" type="{http://www.apache.org/ode/schemas/dd/2007/03}tProvide" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="invoke" type="{http://www.apache.org/ode/schemas/dd/2007/03}tInvoke" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="mex-interceptors" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="mex-interceptor" type="{http://www.apache.org/ode/schemas/dd/2007/03}tMexInterceptor" maxOccurs="unbounded" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}QName" minOccurs="0"/>
     *         &lt;element name="cleanup" type="{http://www.apache.org/ode/schemas/dd/2007/03}tCleanup" maxOccurs="3" minOccurs="0"/>
     *         &lt;element name="schedule" type="{http://www.apache.org/ode/schemas/dd/2007/03}tSchedule" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" />
     *       &lt;attribute name="fileName" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="bpel11wsdlFileName" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "active",
        "retired",
        "inMemory",
        "property",
        "processEvents",
        "provide",
        "invoke",
        "mexInterceptors",
        "type",
        "cleanup",
        "schedule"
    })
    public static class Process {

        @XmlElement(defaultValue = "true")
        protected Boolean active;
        @XmlElement(defaultValue = "false")
        protected Boolean retired;
        @XmlElement(name = "in-memory", defaultValue = "false")
        protected Boolean inMemory;
        protected List<TDeployment.Process.Property> property;
        @XmlElement(name = "process-events")
        protected TProcessEvents processEvents;
        protected List<TProvide> provide;
        protected List<TInvoke> invoke;
        @XmlElement(name = "mex-interceptors")
        protected TDeployment.Process.MexInterceptors mexInterceptors;
        protected QName type;
        protected List<TCleanup> cleanup;
        protected List<TSchedule> schedule;
        @XmlAttribute(required = true)
        protected QName name;
        @XmlAttribute
        protected String fileName;
        @XmlAttribute(name = "bpel11wsdlFileName")
        protected String bpel11WsdlFileName;

        /**
         * Gets the value of the active property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isActive() {
            return active;
        }

        /**
         * Sets the value of the active property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setActive(Boolean value) {
            this.active = value;
        }

        /**
         * Gets the value of the retired property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isRetired() {
            return retired;
        }

        /**
         * Sets the value of the retired property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setRetired(Boolean value) {
            this.retired = value;
        }

        /**
         * Gets the value of the inMemory property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isInMemory() {
            return inMemory;
        }

        /**
         * Sets the value of the inMemory property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setInMemory(Boolean value) {
            this.inMemory = value;
        }

        /**
         * Gets the value of the property property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the property property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getProperty().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TDeployment.Process.Property }
         * 
         * 
         */
        public List<TDeployment.Process.Property> getProperty() {
            if (property == null) {
                property = new ArrayList<TDeployment.Process.Property>();
            }
            return this.property;
        }

        /**
         * Gets the value of the processEvents property.
         * 
         * @return
         *     possible object is
         *     {@link TProcessEvents }
         *     
         */
        public TProcessEvents getProcessEvents() {
            return processEvents;
        }

        /**
         * Sets the value of the processEvents property.
         * 
         * @param value
         *     allowed object is
         *     {@link TProcessEvents }
         *     
         */
        public void setProcessEvents(TProcessEvents value) {
            this.processEvents = value;
        }

        /**
         * Gets the value of the provide property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the provide property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getProvide().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TProvide }
         * 
         * 
         */
        public List<TProvide> getProvide() {
            if (provide == null) {
                provide = new ArrayList<TProvide>();
            }
            return this.provide;
        }

        /**
         * Gets the value of the invoke property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the invoke property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getInvoke().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TInvoke }
         * 
         * 
         */
        public List<TInvoke> getInvoke() {
            if (invoke == null) {
                invoke = new ArrayList<TInvoke>();
            }
            return this.invoke;
        }

        /**
         * Gets the value of the mexInterceptors property.
         * 
         * @return
         *     possible object is
         *     {@link TDeployment.Process.MexInterceptors }
         *     
         */
        public TDeployment.Process.MexInterceptors getMexInterceptors() {
            return mexInterceptors;
        }

        /**
         * Sets the value of the mexInterceptors property.
         * 
         * @param value
         *     allowed object is
         *     {@link TDeployment.Process.MexInterceptors }
         *     
         */
        public void setMexInterceptors(TDeployment.Process.MexInterceptors value) {
            this.mexInterceptors = value;
        }

        /**
         * Gets the value of the type property.
         * 
         * @return
         *     possible object is
         *     {@link QName }
         *     
         */
        public QName getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         * 
         * @param value
         *     allowed object is
         *     {@link QName }
         *     
         */
        public void setType(QName value) {
            this.type = value;
        }

        /**
         * Gets the value of the cleanup property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the cleanup property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCleanup().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TCleanup }
         * 
         * 
         */
        public List<TCleanup> getCleanup() {
            if (cleanup == null) {
                cleanup = new ArrayList<TCleanup>();
            }
            return this.cleanup;
        }

        /**
         * Gets the value of the schedule property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the schedule property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSchedule().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TSchedule }
         * 
         * 
         */
        public List<TSchedule> getSchedule() {
            if (schedule == null) {
                schedule = new ArrayList<TSchedule>();
            }
            return this.schedule;
        }

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link QName }
         *     
         */
        public QName getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link QName }
         *     
         */
        public void setName(QName value) {
            this.name = value;
        }

        /**
         * Gets the value of the fileName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFileName() {
            return fileName;
        }

        /**
         * Sets the value of the fileName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFileName(String value) {
            this.fileName = value;
        }

        /**
         * Gets the value of the bpel11WsdlFileName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBpel11WsdlFileName() {
            return bpel11WsdlFileName;
        }

        /**
         * Sets the value of the bpel11WsdlFileName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBpel11WsdlFileName(String value) {
            this.bpel11WsdlFileName = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="mex-interceptor" type="{http://www.apache.org/ode/schemas/dd/2007/03}tMexInterceptor" maxOccurs="unbounded" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "mexInterceptor"
        })
        public static class MexInterceptors {

            @XmlElement(name = "mex-interceptor")
            protected List<TMexInterceptor> mexInterceptor;

            /**
             * Gets the value of the mexInterceptor property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the mexInterceptor property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getMexInterceptor().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link TMexInterceptor }
             * 
             * 
             */
            public List<TMexInterceptor> getMexInterceptor() {
                if (mexInterceptor == null) {
                    mexInterceptor = new ArrayList<TMexInterceptor>();
                }
                return this.mexInterceptor;
            }

        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
         *       &lt;/sequence>
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "any"
        })
        public static class Property {

            @XmlAnyElement(lax = true)
            protected List<Object> any;
            @XmlAttribute(required = true)
            protected QName name;

            /**
             * Gets the value of the any property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the any property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getAny().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link Element }
             * {@link Object }
             * 
             * 
             */
            public List<Object> getAny() {
                if (any == null) {
                    any = new ArrayList<Object>();
                }
                return this.any;
            }

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link QName }
             *     
             */
            public QName getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link QName }
             *     
             */
            public void setName(QName value) {
                this.name = value;
            }

        }

    }

}
