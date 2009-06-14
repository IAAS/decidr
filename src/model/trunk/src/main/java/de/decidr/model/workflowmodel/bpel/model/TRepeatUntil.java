//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.06.13 at 08:32:08 PM GMT+01:00 
//


package de.decidr.model.workflowmodel.bpel.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tRepeatUntil complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tRepeatUntil">
 *   &lt;complexContent>
 *     &lt;extension base="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tActivity">
 *       &lt;sequence>
 *         &lt;group ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}activity"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}condition"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tRepeatUntil", propOrder = {
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
    "_while",
    "condition"
})
public class TRepeatUntil
    extends TActivity
{

    protected TAssign assign;
    protected TCompensate compensate;
    protected TCompensateScope compensateScope;
    protected TEmpty empty;
    protected TExit exit;
    protected TExtensionActivity extensionActivity;
    protected TFlow flow;
    protected TForEach forEach;
    @XmlElement(name = "if")
    protected TIf _if;
    protected TInvoke invoke;
    protected TPick pick;
    protected TReceive receive;
    protected TRepeatUntil repeatUntil;
    protected TReply reply;
    protected TRethrow rethrow;
    protected TScope scope;
    protected TSequence sequence;
    @XmlElement(name = "throw")
    protected TThrow _throw;
    protected TValidate validate;
    protected TWait wait;
    @XmlElement(name = "while")
    protected TWhile _while;
    @XmlElement(required = true)
    protected TBooleanExpr condition;

    /**
     * Gets the value of the assign property.
     * 
     * @return
     *     possible object is
     *     {@link TAssign }
     *     
     */
    public TAssign getAssign() {
        return assign;
    }

    /**
     * Sets the value of the assign property.
     * 
     * @param value
     *     allowed object is
     *     {@link TAssign }
     *     
     */
    public void setAssign(TAssign value) {
        this.assign = value;
    }

    /**
     * Gets the value of the compensate property.
     * 
     * @return
     *     possible object is
     *     {@link TCompensate }
     *     
     */
    public TCompensate getCompensate() {
        return compensate;
    }

    /**
     * Sets the value of the compensate property.
     * 
     * @param value
     *     allowed object is
     *     {@link TCompensate }
     *     
     */
    public void setCompensate(TCompensate value) {
        this.compensate = value;
    }

    /**
     * Gets the value of the compensateScope property.
     * 
     * @return
     *     possible object is
     *     {@link TCompensateScope }
     *     
     */
    public TCompensateScope getCompensateScope() {
        return compensateScope;
    }

    /**
     * Sets the value of the compensateScope property.
     * 
     * @param value
     *     allowed object is
     *     {@link TCompensateScope }
     *     
     */
    public void setCompensateScope(TCompensateScope value) {
        this.compensateScope = value;
    }

    /**
     * Gets the value of the empty property.
     * 
     * @return
     *     possible object is
     *     {@link TEmpty }
     *     
     */
    public TEmpty getEmpty() {
        return empty;
    }

    /**
     * Sets the value of the empty property.
     * 
     * @param value
     *     allowed object is
     *     {@link TEmpty }
     *     
     */
    public void setEmpty(TEmpty value) {
        this.empty = value;
    }

    /**
     * Gets the value of the exit property.
     * 
     * @return
     *     possible object is
     *     {@link TExit }
     *     
     */
    public TExit getExit() {
        return exit;
    }

    /**
     * Sets the value of the exit property.
     * 
     * @param value
     *     allowed object is
     *     {@link TExit }
     *     
     */
    public void setExit(TExit value) {
        this.exit = value;
    }

    /**
     * Gets the value of the extensionActivity property.
     * 
     * @return
     *     possible object is
     *     {@link TExtensionActivity }
     *     
     */
    public TExtensionActivity getExtensionActivity() {
        return extensionActivity;
    }

    /**
     * Sets the value of the extensionActivity property.
     * 
     * @param value
     *     allowed object is
     *     {@link TExtensionActivity }
     *     
     */
    public void setExtensionActivity(TExtensionActivity value) {
        this.extensionActivity = value;
    }

    /**
     * Gets the value of the flow property.
     * 
     * @return
     *     possible object is
     *     {@link TFlow }
     *     
     */
    public TFlow getFlow() {
        return flow;
    }

    /**
     * Sets the value of the flow property.
     * 
     * @param value
     *     allowed object is
     *     {@link TFlow }
     *     
     */
    public void setFlow(TFlow value) {
        this.flow = value;
    }

    /**
     * Gets the value of the forEach property.
     * 
     * @return
     *     possible object is
     *     {@link TForEach }
     *     
     */
    public TForEach getForEach() {
        return forEach;
    }

    /**
     * Sets the value of the forEach property.
     * 
     * @param value
     *     allowed object is
     *     {@link TForEach }
     *     
     */
    public void setForEach(TForEach value) {
        this.forEach = value;
    }

    /**
     * Gets the value of the if property.
     * 
     * @return
     *     possible object is
     *     {@link TIf }
     *     
     */
    public TIf getIf() {
        return _if;
    }

    /**
     * Sets the value of the if property.
     * 
     * @param value
     *     allowed object is
     *     {@link TIf }
     *     
     */
    public void setIf(TIf value) {
        this._if = value;
    }

    /**
     * Gets the value of the invoke property.
     * 
     * @return
     *     possible object is
     *     {@link TInvoke }
     *     
     */
    public TInvoke getInvoke() {
        return invoke;
    }

    /**
     * Sets the value of the invoke property.
     * 
     * @param value
     *     allowed object is
     *     {@link TInvoke }
     *     
     */
    public void setInvoke(TInvoke value) {
        this.invoke = value;
    }

    /**
     * Gets the value of the pick property.
     * 
     * @return
     *     possible object is
     *     {@link TPick }
     *     
     */
    public TPick getPick() {
        return pick;
    }

    /**
     * Sets the value of the pick property.
     * 
     * @param value
     *     allowed object is
     *     {@link TPick }
     *     
     */
    public void setPick(TPick value) {
        this.pick = value;
    }

    /**
     * Gets the value of the receive property.
     * 
     * @return
     *     possible object is
     *     {@link TReceive }
     *     
     */
    public TReceive getReceive() {
        return receive;
    }

    /**
     * Sets the value of the receive property.
     * 
     * @param value
     *     allowed object is
     *     {@link TReceive }
     *     
     */
    public void setReceive(TReceive value) {
        this.receive = value;
    }

    /**
     * Gets the value of the repeatUntil property.
     * 
     * @return
     *     possible object is
     *     {@link TRepeatUntil }
     *     
     */
    public TRepeatUntil getRepeatUntil() {
        return repeatUntil;
    }

    /**
     * Sets the value of the repeatUntil property.
     * 
     * @param value
     *     allowed object is
     *     {@link TRepeatUntil }
     *     
     */
    public void setRepeatUntil(TRepeatUntil value) {
        this.repeatUntil = value;
    }

    /**
     * Gets the value of the reply property.
     * 
     * @return
     *     possible object is
     *     {@link TReply }
     *     
     */
    public TReply getReply() {
        return reply;
    }

    /**
     * Sets the value of the reply property.
     * 
     * @param value
     *     allowed object is
     *     {@link TReply }
     *     
     */
    public void setReply(TReply value) {
        this.reply = value;
    }

    /**
     * Gets the value of the rethrow property.
     * 
     * @return
     *     possible object is
     *     {@link TRethrow }
     *     
     */
    public TRethrow getRethrow() {
        return rethrow;
    }

    /**
     * Sets the value of the rethrow property.
     * 
     * @param value
     *     allowed object is
     *     {@link TRethrow }
     *     
     */
    public void setRethrow(TRethrow value) {
        this.rethrow = value;
    }

    /**
     * Gets the value of the scope property.
     * 
     * @return
     *     possible object is
     *     {@link TScope }
     *     
     */
    public TScope getScope() {
        return scope;
    }

    /**
     * Sets the value of the scope property.
     * 
     * @param value
     *     allowed object is
     *     {@link TScope }
     *     
     */
    public void setScope(TScope value) {
        this.scope = value;
    }

    /**
     * Gets the value of the sequence property.
     * 
     * @return
     *     possible object is
     *     {@link TSequence }
     *     
     */
    public TSequence getSequence() {
        return sequence;
    }

    /**
     * Sets the value of the sequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link TSequence }
     *     
     */
    public void setSequence(TSequence value) {
        this.sequence = value;
    }

    /**
     * Gets the value of the throw property.
     * 
     * @return
     *     possible object is
     *     {@link TThrow }
     *     
     */
    public TThrow getThrow() {
        return _throw;
    }

    /**
     * Sets the value of the throw property.
     * 
     * @param value
     *     allowed object is
     *     {@link TThrow }
     *     
     */
    public void setThrow(TThrow value) {
        this._throw = value;
    }

    /**
     * Gets the value of the validate property.
     * 
     * @return
     *     possible object is
     *     {@link TValidate }
     *     
     */
    public TValidate getValidate() {
        return validate;
    }

    /**
     * Sets the value of the validate property.
     * 
     * @param value
     *     allowed object is
     *     {@link TValidate }
     *     
     */
    public void setValidate(TValidate value) {
        this.validate = value;
    }

    /**
     * Gets the value of the wait property.
     * 
     * @return
     *     possible object is
     *     {@link TWait }
     *     
     */
    public TWait getWait() {
        return wait;
    }

    /**
     * Sets the value of the wait property.
     * 
     * @param value
     *     allowed object is
     *     {@link TWait }
     *     
     */
    public void setWait(TWait value) {
        this.wait = value;
    }

    /**
     * Gets the value of the while property.
     * 
     * @return
     *     possible object is
     *     {@link TWhile }
     *     
     */
    public TWhile getWhile() {
        return _while;
    }

    /**
     * Sets the value of the while property.
     * 
     * @param value
     *     allowed object is
     *     {@link TWhile }
     *     
     */
    public void setWhile(TWhile value) {
        this._while = value;
    }

    /**
     * Gets the value of the condition property.
     * 
     * @return
     *     possible object is
     *     {@link TBooleanExpr }
     *     
     */
    public TBooleanExpr getCondition() {
        return condition;
    }

    /**
     * Sets the value of the condition property.
     * 
     * @param value
     *     allowed object is
     *     {@link TBooleanExpr }
     *     
     */
    public void setCondition(TBooleanExpr value) {
        this.condition = value;
    }

}
