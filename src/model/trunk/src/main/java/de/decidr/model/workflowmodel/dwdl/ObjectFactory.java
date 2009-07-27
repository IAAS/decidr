//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.07.27 at 02:02:16 PM GMT+01:00 
//


package de.decidr.model.workflowmodel.dwdl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.decidr.model.workflowmodel.dwdl package. 
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

    private final static QName _ForEachNode_QNAME = new QName("http://decidr.de/schema/dwdl", "forEachNode");
    private final static QName _StartNode_QNAME = new QName("http://decidr.de/schema/dwdl", "startNode");
    private final static QName _Actor_QNAME = new QName("http://decidr.de/schema/dwdl", "actor");
    private final static QName _CompletionCondition_QNAME = new QName("http://decidr.de/schema/dwdl", "completionCondition");
    private final static QName _StartCounterValue_QNAME = new QName("http://decidr.de/schema/dwdl", "startCounterValue");
    private final static QName _Sources_QNAME = new QName("http://decidr.de/schema/dwdl", "sources");
    private final static QName _Recipient_QNAME = new QName("http://decidr.de/schema/dwdl", "recipient");
    private final static QName _InvokeNode_QNAME = new QName("http://decidr.de/schema/dwdl", "invokeNode");
    private final static QName _Description_QNAME = new QName("http://decidr.de/schema/dwdl", "description");
    private final static QName _Role_QNAME = new QName("http://decidr.de/schema/dwdl", "role");
    private final static QName _BasicNode_QNAME = new QName("http://decidr.de/schema/dwdl", "basicNode");
    private final static QName _Graphics_QNAME = new QName("http://decidr.de/schema/dwdl", "graphics");
    private final static QName _Targets_QNAME = new QName("http://decidr.de/schema/dwdl", "targets");
    private final static QName _FinalCounterValue_QNAME = new QName("http://decidr.de/schema/dwdl", "finalCounterValue");
    private final static QName _IfNode_QNAME = new QName("http://decidr.de/schema/dwdl", "ifNode");
    private final static QName _FaultHandler_QNAME = new QName("http://decidr.de/schema/dwdl", "faultHandler");
    private final static QName _Condition_QNAME = new QName("http://decidr.de/schema/dwdl", "condition");
    private final static QName _HumanTaskData_QNAME = new QName("http://decidr.de/schema/dwdl", "humanTaskData");
    private final static QName _SetProperty_QNAME = new QName("http://decidr.de/schema/dwdl", "setProperty");
    private final static QName _Workflow_QNAME = new QName("http://decidr.de/schema/dwdl", "workflow");
    private final static QName _EndNode_QNAME = new QName("http://decidr.de/schema/dwdl", "endNode");
    private final static QName _Variables_QNAME = new QName("http://decidr.de/schema/dwdl", "variables");
    private final static QName _Arcs_QNAME = new QName("http://decidr.de/schema/dwdl", "arcs");
    private final static QName _Content_QNAME = new QName("http://decidr.de/schema/dwdl", "content");
    private final static QName _FlowNode_QNAME = new QName("http://decidr.de/schema/dwdl", "flowNode");
    private final static QName _Source_QNAME = new QName("http://decidr.de/schema/dwdl", "source");
    private final static QName _Roles_QNAME = new QName("http://decidr.de/schema/dwdl", "roles");
    private final static QName _GetProperty_QNAME = new QName("http://decidr.de/schema/dwdl", "getProperty");
    private final static QName _Nodes_QNAME = new QName("http://decidr.de/schema/dwdl", "nodes");
    private final static QName _Target_QNAME = new QName("http://decidr.de/schema/dwdl", "target");
    private final static QName _Variable_QNAME = new QName("http://decidr.de/schema/dwdl", "variable");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.decidr.model.workflowmodel.dwdl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TSource }
     * 
     */
    public TSource createTSource() {
        return new TSource();
    }

    /**
     * Create an instance of {@link TActor }
     * 
     */
    public TActor createTActor() {
        return new TActor();
    }

    /**
     * Create an instance of {@link TArcs }
     * 
     */
    public TArcs createTArcs() {
        return new TArcs();
    }

    /**
     * Create an instance of {@link TParameter }
     * 
     */
    public TParameter createTParameter() {
        return new TParameter();
    }

    /**
     * Create an instance of {@link TIfNode }
     * 
     */
    public TIfNode createTIfNode() {
        return new TIfNode();
    }

    /**
     * Create an instance of {@link TBasicNode }
     * 
     */
    public TBasicNode createTBasicNode() {
        return new TBasicNode();
    }

    /**
     * Create an instance of {@link TContent }
     * 
     */
    public TContent createTContent() {
        return new TContent();
    }

    /**
     * Create an instance of {@link TCondition }
     * 
     */
    public TCondition createTCondition() {
        return new TCondition();
    }

    /**
     * Create an instance of {@link TArc }
     * 
     */
    public TArc createTArc() {
        return new TArc();
    }

    /**
     * Create an instance of {@link TRole }
     * 
     */
    public TRole createTRole() {
        return new TRole();
    }

    /**
     * Create an instance of {@link TParameters }
     * 
     */
    public TParameters createTParameters() {
        return new TParameters();
    }

    /**
     * Create an instance of {@link TDescription }
     * 
     */
    public TDescription createTDescription() {
        return new TDescription();
    }

    /**
     * Create an instance of {@link TGetProperty }
     * 
     */
    public TGetProperty createTGetProperty() {
        return new TGetProperty();
    }

    /**
     * Create an instance of {@link TRecipient }
     * 
     */
    public TRecipient createTRecipient() {
        return new TRecipient();
    }

    /**
     * Create an instance of {@link TInitialValues }
     * 
     */
    public TInitialValues createTInitialValues() {
        return new TInitialValues();
    }

    /**
     * Create an instance of {@link TTarget }
     * 
     */
    public TTarget createTTarget() {
        return new TTarget();
    }

    /**
     * Create an instance of {@link TRoles }
     * 
     */
    public TRoles createTRoles() {
        return new TRoles();
    }

    /**
     * Create an instance of {@link TNodes }
     * 
     */
    public TNodes createTNodes() {
        return new TNodes();
    }

    /**
     * Create an instance of {@link TTaskItem }
     * 
     */
    public TTaskItem createTTaskItem() {
        return new TTaskItem();
    }

    /**
     * Create an instance of {@link TSetProperty }
     * 
     */
    public TSetProperty createTSetProperty() {
        return new TSetProperty();
    }

    /**
     * Create an instance of {@link TInformation }
     * 
     */
    public TInformation createTInformation() {
        return new TInformation();
    }

    /**
     * Create an instance of {@link TTargets }
     * 
     */
    public TTargets createTTargets() {
        return new TTargets();
    }

    /**
     * Create an instance of {@link TVariables }
     * 
     */
    public TVariables createTVariables() {
        return new TVariables();
    }

    /**
     * Create an instance of {@link TStartNode }
     * 
     */
    public TStartNode createTStartNode() {
        return new TStartNode();
    }

    /**
     * Create an instance of {@link TLiteral }
     * 
     */
    public TLiteral createTLiteral() {
        return new TLiteral();
    }

    /**
     * Create an instance of {@link TVariable }
     * 
     */
    public TVariable createTVariable() {
        return new TVariable();
    }

    /**
     * Create an instance of {@link TSources }
     * 
     */
    public TSources createTSources() {
        return new TSources();
    }

    /**
     * Create an instance of {@link TEndNode }
     * 
     */
    public TEndNode createTEndNode() {
        return new TEndNode();
    }

    /**
     * Create an instance of {@link TFlowNode }
     * 
     */
    public TFlowNode createTFlowNode() {
        return new TFlowNode();
    }

    /**
     * Create an instance of {@link TFaultHandler }
     * 
     */
    public TFaultHandler createTFaultHandler() {
        return new TFaultHandler();
    }

    /**
     * Create an instance of {@link TGraphics }
     * 
     */
    public TGraphics createTGraphics() {
        return new TGraphics();
    }

    /**
     * Create an instance of {@link THumanTaskData }
     * 
     */
    public THumanTaskData createTHumanTaskData() {
        return new THumanTaskData();
    }

    /**
     * Create an instance of {@link TInvokeNode }
     * 
     */
    public TInvokeNode createTInvokeNode() {
        return new TInvokeNode();
    }

    /**
     * Create an instance of {@link TWorkflow }
     * 
     */
    public TWorkflow createTWorkflow() {
        return new TWorkflow();
    }

    /**
     * Create an instance of {@link TNotification }
     * 
     */
    public TNotification createTNotification() {
        return new TNotification();
    }

    /**
     * Create an instance of {@link TForEachNode }
     * 
     */
    public TForEachNode createTForEachNode() {
        return new TForEachNode();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TForEachNode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "forEachNode")
    public JAXBElement<TForEachNode> createForEachNode(TForEachNode value) {
        return new JAXBElement<TForEachNode>(_ForEachNode_QNAME, TForEachNode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TStartNode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "startNode")
    public JAXBElement<TStartNode> createStartNode(TStartNode value) {
        return new JAXBElement<TStartNode>(_StartNode_QNAME, TStartNode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TActor }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "actor")
    public JAXBElement<TActor> createActor(TActor value) {
        return new JAXBElement<TActor>(_Actor_QNAME, TActor.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "completionCondition")
    public JAXBElement<String> createCompletionCondition(String value) {
        return new JAXBElement<String>(_CompletionCondition_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "startCounterValue")
    public JAXBElement<String> createStartCounterValue(String value) {
        return new JAXBElement<String>(_StartCounterValue_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSources }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "sources")
    public JAXBElement<TSources> createSources(TSources value) {
        return new JAXBElement<TSources>(_Sources_QNAME, TSources.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TRecipient }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "recipient")
    public JAXBElement<TRecipient> createRecipient(TRecipient value) {
        return new JAXBElement<TRecipient>(_Recipient_QNAME, TRecipient.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TInvokeNode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "invokeNode")
    public JAXBElement<TInvokeNode> createInvokeNode(TInvokeNode value) {
        return new JAXBElement<TInvokeNode>(_InvokeNode_QNAME, TInvokeNode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TDescription }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "description")
    public JAXBElement<TDescription> createDescription(TDescription value) {
        return new JAXBElement<TDescription>(_Description_QNAME, TDescription.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TRole }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "role")
    public JAXBElement<TRole> createRole(TRole value) {
        return new JAXBElement<TRole>(_Role_QNAME, TRole.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TBasicNode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "basicNode")
    public JAXBElement<TBasicNode> createBasicNode(TBasicNode value) {
        return new JAXBElement<TBasicNode>(_BasicNode_QNAME, TBasicNode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TGraphics }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "graphics")
    public JAXBElement<TGraphics> createGraphics(TGraphics value) {
        return new JAXBElement<TGraphics>(_Graphics_QNAME, TGraphics.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TTargets }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "targets")
    public JAXBElement<TTargets> createTargets(TTargets value) {
        return new JAXBElement<TTargets>(_Targets_QNAME, TTargets.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "finalCounterValue")
    public JAXBElement<String> createFinalCounterValue(String value) {
        return new JAXBElement<String>(_FinalCounterValue_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TIfNode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "ifNode")
    public JAXBElement<TIfNode> createIfNode(TIfNode value) {
        return new JAXBElement<TIfNode>(_IfNode_QNAME, TIfNode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TFaultHandler }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "faultHandler")
    public JAXBElement<TFaultHandler> createFaultHandler(TFaultHandler value) {
        return new JAXBElement<TFaultHandler>(_FaultHandler_QNAME, TFaultHandler.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TCondition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "condition")
    public JAXBElement<TCondition> createCondition(TCondition value) {
        return new JAXBElement<TCondition>(_Condition_QNAME, TCondition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link THumanTaskData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "humanTaskData")
    public JAXBElement<THumanTaskData> createHumanTaskData(THumanTaskData value) {
        return new JAXBElement<THumanTaskData>(_HumanTaskData_QNAME, THumanTaskData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSetProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "setProperty")
    public JAXBElement<TSetProperty> createSetProperty(TSetProperty value) {
        return new JAXBElement<TSetProperty>(_SetProperty_QNAME, TSetProperty.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TWorkflow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "workflow")
    public JAXBElement<TWorkflow> createWorkflow(TWorkflow value) {
        return new JAXBElement<TWorkflow>(_Workflow_QNAME, TWorkflow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEndNode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "endNode")
    public JAXBElement<TEndNode> createEndNode(TEndNode value) {
        return new JAXBElement<TEndNode>(_EndNode_QNAME, TEndNode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TVariables }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "variables")
    public JAXBElement<TVariables> createVariables(TVariables value) {
        return new JAXBElement<TVariables>(_Variables_QNAME, TVariables.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TArcs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "arcs")
    public JAXBElement<TArcs> createArcs(TArcs value) {
        return new JAXBElement<TArcs>(_Arcs_QNAME, TArcs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TContent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "content")
    public JAXBElement<TContent> createContent(TContent value) {
        return new JAXBElement<TContent>(_Content_QNAME, TContent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TFlowNode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "flowNode")
    public JAXBElement<TFlowNode> createFlowNode(TFlowNode value) {
        return new JAXBElement<TFlowNode>(_FlowNode_QNAME, TFlowNode.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TSource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "source")
    public JAXBElement<TSource> createSource(TSource value) {
        return new JAXBElement<TSource>(_Source_QNAME, TSource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TRoles }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "roles")
    public JAXBElement<TRoles> createRoles(TRoles value) {
        return new JAXBElement<TRoles>(_Roles_QNAME, TRoles.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TGetProperty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "getProperty")
    public JAXBElement<TGetProperty> createGetProperty(TGetProperty value) {
        return new JAXBElement<TGetProperty>(_GetProperty_QNAME, TGetProperty.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TNodes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "nodes")
    public JAXBElement<TNodes> createNodes(TNodes value) {
        return new JAXBElement<TNodes>(_Nodes_QNAME, TNodes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TTarget }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "target")
    public JAXBElement<TTarget> createTarget(TTarget value) {
        return new JAXBElement<TTarget>(_Target_QNAME, TTarget.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TVariable }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://decidr.de/schema/dwdl", name = "variable")
    public JAXBElement<TVariable> createVariable(TVariable value) {
        return new JAXBElement<TVariable>(_Variable_QNAME, TVariable.class, null, value);
    }

}
