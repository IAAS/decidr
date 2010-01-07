//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.11.07 at 09:21:14 PM MEZ 
//


package de.decidr.model.workflowmodel.bpel;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for tProcess complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tProcess">
 *   &lt;complexContent>
 *     &lt;extension base="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tExtensibleElements">
 *       &lt;sequence>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}extensions" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}import" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}partnerLinks" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}messageExchanges" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}variables" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}correlationSets" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}faultHandlers" minOccurs="0"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}eventHandlers" minOccurs="0"/>
 *         &lt;group ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}activity"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="targetNamespace" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="queryLanguage" type="{http://www.w3.org/2001/XMLSchema}anyURI" default="urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0" />
 *       &lt;attribute name="expressionLanguage" type="{http://www.w3.org/2001/XMLSchema}anyURI" default="urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0" />
 *       &lt;attribute name="suppressJoinFailure" type="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tBoolean" default="no" />
 *       &lt;attribute name="exitOnStandardFault" type="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tBoolean" default="no" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tProcess", propOrder = {
    "extensions",
    "_import",
    "partnerLinks",
    "messageExchanges",
    "variables",
    "correlationSets",
    "faultHandlers",
    "eventHandlers",
    "assign",
    "compensate",
    "compensateScope",
    "empty",
    "exit",
    "extensionActivity",
    "flow",
    "forEach",
    "_if",
    "invoke",
    "pick",
    "receive",
    "repeatUntil",
    "reply",
    "rethrow",
    "scope",
    "sequence",
    "_throw",
    "validate",
    "wait",
    "_while"
})
public class Process
    extends ExtensibleElements
{

    protected Extensions extensions;
    @XmlElement(name = "import")
    protected List<Import> _import;
    protected PartnerLinks partnerLinks;
    protected MessageExchanges messageExchanges;
    protected Variables variables;
    protected CorrelationSets correlationSets;
    protected FaultHandlers faultHandlers;
    protected EventHandlers eventHandlers;
    protected Assign assign;
    protected Compensate compensate;
    protected CompensateScope compensateScope;
    protected Empty empty;
    protected Exit exit;
    protected ExtensionActivity extensionActivity;
    protected Flow flow;
    protected ForEach forEach;
    @XmlElement(name = "if")
    protected If _if;
    protected Invoke invoke;
    protected Pick pick;
    protected Receive receive;
    protected RepeatUntil repeatUntil;
    protected Reply reply;
    protected Rethrow rethrow;
    protected Scope scope;
    protected Sequence sequence;
    @XmlElement(name = "throw")
    protected Throw _throw;
    protected Validate validate;
    protected Wait wait;
    @XmlElement(name = "while")
    protected While _while;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String name;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String targetNamespace;
    @XmlAttribute
    @XmlSchemaType(name = "anyURI")
    protected String queryLanguage;
    @XmlAttribute
    @XmlSchemaType(name = "anyURI")
    protected String expressionLanguage;
    @XmlAttribute
    protected Boolean suppressJoinFailure;
    @XmlAttribute
    protected Boolean exitOnStandardFault;

    /**
     * Gets the value of the extensions property.
     * 
     * @return
     *     possible object is
     *     {@link Extensions }
     *     
     */
    public Extensions getExtensions() {
        return extensions;
    }

    /**
     * Sets the value of the extensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Extensions }
     *     
     */
    public void setExtensions(Extensions value) {
        this.extensions = value;
    }

    public boolean isSetExtensions() {
        return (this.extensions!= null);
    }

    /**
     * Gets the value of the import property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the import property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImport().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Import }
     * 
     * 
     */
    public List<Import> getImport() {
        if (_import == null) {
            _import = new ArrayList<Import>();
        }
        return this._import;
    }

    public boolean isSetImport() {
        return ((this._import!= null)&&(!this._import.isEmpty()));
    }

    public void unsetImport() {
        this._import = null;
    }

    /**
     * Gets the value of the partnerLinks property.
     * 
     * @return
     *     possible object is
     *     {@link PartnerLinks }
     *     
     */
    public PartnerLinks getPartnerLinks() {
        return partnerLinks;
    }

    /**
     * Sets the value of the partnerLinks property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartnerLinks }
     *     
     */
    public void setPartnerLinks(PartnerLinks value) {
        this.partnerLinks = value;
    }

    public boolean isSetPartnerLinks() {
        return (this.partnerLinks!= null);
    }

    /**
     * Gets the value of the messageExchanges property.
     * 
     * @return
     *     possible object is
     *     {@link MessageExchanges }
     *     
     */
    public MessageExchanges getMessageExchanges() {
        return messageExchanges;
    }

    /**
     * Sets the value of the messageExchanges property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageExchanges }
     *     
     */
    public void setMessageExchanges(MessageExchanges value) {
        this.messageExchanges = value;
    }

    public boolean isSetMessageExchanges() {
        return (this.messageExchanges!= null);
    }

    /**
     * Gets the value of the variables property.
     * 
     * @return
     *     possible object is
     *     {@link Variables }
     *     
     */
    public Variables getVariables() {
        return variables;
    }

    /**
     * Sets the value of the variables property.
     * 
     * @param value
     *     allowed object is
     *     {@link Variables }
     *     
     */
    public void setVariables(Variables value) {
        this.variables = value;
    }

    public boolean isSetVariables() {
        return (this.variables!= null);
    }

    /**
     * Gets the value of the correlationSets property.
     * 
     * @return
     *     possible object is
     *     {@link CorrelationSets }
     *     
     */
    public CorrelationSets getCorrelationSets() {
        return correlationSets;
    }

    /**
     * Sets the value of the correlationSets property.
     * 
     * @param value
     *     allowed object is
     *     {@link CorrelationSets }
     *     
     */
    public void setCorrelationSets(CorrelationSets value) {
        this.correlationSets = value;
    }

    public boolean isSetCorrelationSets() {
        return (this.correlationSets!= null);
    }

    /**
     * Gets the value of the faultHandlers property.
     * 
     * @return
     *     possible object is
     *     {@link FaultHandlers }
     *     
     */
    public FaultHandlers getFaultHandlers() {
        return faultHandlers;
    }

    /**
     * Sets the value of the faultHandlers property.
     * 
     * @param value
     *     allowed object is
     *     {@link FaultHandlers }
     *     
     */
    public void setFaultHandlers(FaultHandlers value) {
        this.faultHandlers = value;
    }

    public boolean isSetFaultHandlers() {
        return (this.faultHandlers!= null);
    }

    /**
     * Gets the value of the eventHandlers property.
     * 
     * @return
     *     possible object is
     *     {@link EventHandlers }
     *     
     */
    public EventHandlers getEventHandlers() {
        return eventHandlers;
    }

    /**
     * Sets the value of the eventHandlers property.
     * 
     * @param value
     *     allowed object is
     *     {@link EventHandlers }
     *     
     */
    public void setEventHandlers(EventHandlers value) {
        this.eventHandlers = value;
    }

    public boolean isSetEventHandlers() {
        return (this.eventHandlers!= null);
    }

    /**
     * Gets the value of the assign property.
     * 
     * @return
     *     possible object is
     *     {@link Assign }
     *     
     */
    public Assign getAssign() {
        return assign;
    }

    /**
     * Sets the value of the assign property.
     * 
     * @param value
     *     allowed object is
     *     {@link Assign }
     *     
     */
    public void setAssign(Assign value) {
        this.assign = value;
    }

    public boolean isSetAssign() {
        return (this.assign!= null);
    }

    /**
     * Gets the value of the compensate property.
     * 
     * @return
     *     possible object is
     *     {@link Compensate }
     *     
     */
    public Compensate getCompensate() {
        return compensate;
    }

    /**
     * Sets the value of the compensate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Compensate }
     *     
     */
    public void setCompensate(Compensate value) {
        this.compensate = value;
    }

    public boolean isSetCompensate() {
        return (this.compensate!= null);
    }

    /**
     * Gets the value of the compensateScope property.
     * 
     * @return
     *     possible object is
     *     {@link CompensateScope }
     *     
     */
    public CompensateScope getCompensateScope() {
        return compensateScope;
    }

    /**
     * Sets the value of the compensateScope property.
     * 
     * @param value
     *     allowed object is
     *     {@link CompensateScope }
     *     
     */
    public void setCompensateScope(CompensateScope value) {
        this.compensateScope = value;
    }

    public boolean isSetCompensateScope() {
        return (this.compensateScope!= null);
    }

    /**
     * Gets the value of the empty property.
     * 
     * @return
     *     possible object is
     *     {@link Empty }
     *     
     */
    public Empty getEmpty() {
        return empty;
    }

    /**
     * Sets the value of the empty property.
     * 
     * @param value
     *     allowed object is
     *     {@link Empty }
     *     
     */
    public void setEmpty(Empty value) {
        this.empty = value;
    }

    public boolean isSetEmpty() {
        return (this.empty!= null);
    }

    /**
     * Gets the value of the exit property.
     * 
     * @return
     *     possible object is
     *     {@link Exit }
     *     
     */
    public Exit getExit() {
        return exit;
    }

    /**
     * Sets the value of the exit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Exit }
     *     
     */
    public void setExit(Exit value) {
        this.exit = value;
    }

    public boolean isSetExit() {
        return (this.exit!= null);
    }

    /**
     * Gets the value of the extensionActivity property.
     * 
     * @return
     *     possible object is
     *     {@link ExtensionActivity }
     *     
     */
    public ExtensionActivity getExtensionActivity() {
        return extensionActivity;
    }

    /**
     * Sets the value of the extensionActivity property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionActivity }
     *     
     */
    public void setExtensionActivity(ExtensionActivity value) {
        this.extensionActivity = value;
    }

    public boolean isSetExtensionActivity() {
        return (this.extensionActivity!= null);
    }

    /**
     * Gets the value of the flow property.
     * 
     * @return
     *     possible object is
     *     {@link Flow }
     *     
     */
    public Flow getFlow() {
        return flow;
    }

    /**
     * Sets the value of the flow property.
     * 
     * @param value
     *     allowed object is
     *     {@link Flow }
     *     
     */
    public void setFlow(Flow value) {
        this.flow = value;
    }

    public boolean isSetFlow() {
        return (this.flow!= null);
    }

    /**
     * Gets the value of the forEach property.
     * 
     * @return
     *     possible object is
     *     {@link ForEach }
     *     
     */
    public ForEach getForEach() {
        return forEach;
    }

    /**
     * Sets the value of the forEach property.
     * 
     * @param value
     *     allowed object is
     *     {@link ForEach }
     *     
     */
    public void setForEach(ForEach value) {
        this.forEach = value;
    }

    public boolean isSetForEach() {
        return (this.forEach!= null);
    }

    /**
     * Gets the value of the if property.
     * 
     * @return
     *     possible object is
     *     {@link If }
     *     
     */
    public If getIf() {
        return _if;
    }

    /**
     * Sets the value of the if property.
     * 
     * @param value
     *     allowed object is
     *     {@link If }
     *     
     */
    public void setIf(If value) {
        this._if = value;
    }

    public boolean isSetIf() {
        return (this._if!= null);
    }

    /**
     * Gets the value of the invoke property.
     * 
     * @return
     *     possible object is
     *     {@link Invoke }
     *     
     */
    public Invoke getInvoke() {
        return invoke;
    }

    /**
     * Sets the value of the invoke property.
     * 
     * @param value
     *     allowed object is
     *     {@link Invoke }
     *     
     */
    public void setInvoke(Invoke value) {
        this.invoke = value;
    }

    public boolean isSetInvoke() {
        return (this.invoke!= null);
    }

    /**
     * Gets the value of the pick property.
     * 
     * @return
     *     possible object is
     *     {@link Pick }
     *     
     */
    public Pick getPick() {
        return pick;
    }

    /**
     * Sets the value of the pick property.
     * 
     * @param value
     *     allowed object is
     *     {@link Pick }
     *     
     */
    public void setPick(Pick value) {
        this.pick = value;
    }

    public boolean isSetPick() {
        return (this.pick!= null);
    }

    /**
     * Gets the value of the receive property.
     * 
     * @return
     *     possible object is
     *     {@link Receive }
     *     
     */
    public Receive getReceive() {
        return receive;
    }

    /**
     * Sets the value of the receive property.
     * 
     * @param value
     *     allowed object is
     *     {@link Receive }
     *     
     */
    public void setReceive(Receive value) {
        this.receive = value;
    }

    public boolean isSetReceive() {
        return (this.receive!= null);
    }

    /**
     * Gets the value of the repeatUntil property.
     * 
     * @return
     *     possible object is
     *     {@link RepeatUntil }
     *     
     */
    public RepeatUntil getRepeatUntil() {
        return repeatUntil;
    }

    /**
     * Sets the value of the repeatUntil property.
     * 
     * @param value
     *     allowed object is
     *     {@link RepeatUntil }
     *     
     */
    public void setRepeatUntil(RepeatUntil value) {
        this.repeatUntil = value;
    }

    public boolean isSetRepeatUntil() {
        return (this.repeatUntil!= null);
    }

    /**
     * Gets the value of the reply property.
     * 
     * @return
     *     possible object is
     *     {@link Reply }
     *     
     */
    public Reply getReply() {
        return reply;
    }

    /**
     * Sets the value of the reply property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reply }
     *     
     */
    public void setReply(Reply value) {
        this.reply = value;
    }

    public boolean isSetReply() {
        return (this.reply!= null);
    }

    /**
     * Gets the value of the rethrow property.
     * 
     * @return
     *     possible object is
     *     {@link Rethrow }
     *     
     */
    public Rethrow getRethrow() {
        return rethrow;
    }

    /**
     * Sets the value of the rethrow property.
     * 
     * @param value
     *     allowed object is
     *     {@link Rethrow }
     *     
     */
    public void setRethrow(Rethrow value) {
        this.rethrow = value;
    }

    public boolean isSetRethrow() {
        return (this.rethrow!= null);
    }

    /**
     * Gets the value of the scope property.
     * 
     * @return
     *     possible object is
     *     {@link Scope }
     *     
     */
    public Scope getScope() {
        return scope;
    }

    /**
     * Sets the value of the scope property.
     * 
     * @param value
     *     allowed object is
     *     {@link Scope }
     *     
     */
    public void setScope(Scope value) {
        this.scope = value;
    }

    public boolean isSetScope() {
        return (this.scope!= null);
    }

    /**
     * Gets the value of the sequence property.
     * 
     * @return
     *     possible object is
     *     {@link Sequence }
     *     
     */
    public Sequence getSequence() {
        return sequence;
    }

    /**
     * Sets the value of the sequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link Sequence }
     *     
     */
    public void setSequence(Sequence value) {
        this.sequence = value;
    }

    public boolean isSetSequence() {
        return (this.sequence!= null);
    }

    /**
     * Gets the value of the throw property.
     * 
     * @return
     *     possible object is
     *     {@link Throw }
     *     
     */
    public Throw getThrow() {
        return _throw;
    }

    /**
     * Sets the value of the throw property.
     * 
     * @param value
     *     allowed object is
     *     {@link Throw }
     *     
     */
    public void setThrow(Throw value) {
        this._throw = value;
    }

    public boolean isSetThrow() {
        return (this._throw!= null);
    }

    /**
     * Gets the value of the validate property.
     * 
     * @return
     *     possible object is
     *     {@link Validate }
     *     
     */
    public Validate getValidate() {
        return validate;
    }

    /**
     * Sets the value of the validate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Validate }
     *     
     */
    public void setValidate(Validate value) {
        this.validate = value;
    }

    public boolean isSetValidate() {
        return (this.validate!= null);
    }

    /**
     * Gets the value of the wait property.
     * 
     * @return
     *     possible object is
     *     {@link Wait }
     *     
     */
    public Wait getWait() {
        return wait;
    }

    /**
     * Sets the value of the wait property.
     * 
     * @param value
     *     allowed object is
     *     {@link Wait }
     *     
     */
    public void setWait(Wait value) {
        this.wait = value;
    }

    public boolean isSetWait() {
        return (this.wait!= null);
    }

    /**
     * Gets the value of the while property.
     * 
     * @return
     *     possible object is
     *     {@link While }
     *     
     */
    public While getWhile() {
        return _while;
    }

    /**
     * Sets the value of the while property.
     * 
     * @param value
     *     allowed object is
     *     {@link While }
     *     
     */
    public void setWhile(While value) {
        this._while = value;
    }

    public boolean isSetWhile() {
        return (this._while!= null);
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    public boolean isSetName() {
        return (this.name!= null);
    }

    /**
     * Gets the value of the targetNamespace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetNamespace() {
        return targetNamespace;
    }

    /**
     * Sets the value of the targetNamespace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetNamespace(String value) {
        this.targetNamespace = value;
    }

    public boolean isSetTargetNamespace() {
        return (this.targetNamespace!= null);
    }

    /**
     * Gets the value of the queryLanguage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueryLanguage() {
        if (queryLanguage == null) {
            return "urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0";
        } else {
            return queryLanguage;
        }
    }

    /**
     * Sets the value of the queryLanguage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueryLanguage(String value) {
        this.queryLanguage = value;
    }

    public boolean isSetQueryLanguage() {
        return (this.queryLanguage!= null);
    }

    /**
     * Gets the value of the expressionLanguage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpressionLanguage() {
        if (expressionLanguage == null) {
            return "urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0";
        } else {
            return expressionLanguage;
        }
    }

    /**
     * Sets the value of the expressionLanguage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpressionLanguage(String value) {
        this.expressionLanguage = value;
    }

    public boolean isSetExpressionLanguage() {
        return (this.expressionLanguage!= null);
    }

    /**
     * Gets the value of the suppressJoinFailure property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean getSuppressJoinFailure() {
        if (suppressJoinFailure == null) {
            return Boolean.NO;
        } else {
            return suppressJoinFailure;
        }
    }

    /**
     * Sets the value of the suppressJoinFailure property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSuppressJoinFailure(Boolean value) {
        this.suppressJoinFailure = value;
    }

    public boolean isSetSuppressJoinFailure() {
        return (this.suppressJoinFailure!= null);
    }

    /**
     * Gets the value of the exitOnStandardFault property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean getExitOnStandardFault() {
        if (exitOnStandardFault == null) {
            return Boolean.NO;
        } else {
            return exitOnStandardFault;
        }
    }

    /**
     * Sets the value of the exitOnStandardFault property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setExitOnStandardFault(Boolean value) {
        this.exitOnStandardFault = value;
    }

    public boolean isSetExitOnStandardFault() {
        return (this.exitOnStandardFault!= null);
    }

}