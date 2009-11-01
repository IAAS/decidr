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

package de.decidr.model.workflowmodel.dwdl.transformation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.transform.TransformerException;

import org.apache.axis2.namespace.Constants;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.bpel.Activity;
import de.decidr.model.workflowmodel.bpel.ActivityContainer;
import de.decidr.model.workflowmodel.bpel.Assign;
import de.decidr.model.workflowmodel.bpel.BooleanExpression;
import de.decidr.model.workflowmodel.bpel.Branches;
import de.decidr.model.workflowmodel.bpel.CompletionCondition;
import de.decidr.model.workflowmodel.bpel.Copy;
import de.decidr.model.workflowmodel.bpel.CorrelationSet;
import de.decidr.model.workflowmodel.bpel.CorrelationSets;
import de.decidr.model.workflowmodel.bpel.Documentation;
import de.decidr.model.workflowmodel.bpel.Elseif;
import de.decidr.model.workflowmodel.bpel.Expression;
import de.decidr.model.workflowmodel.bpel.ExtensibleElements;
import de.decidr.model.workflowmodel.bpel.FaultHandlers;
import de.decidr.model.workflowmodel.bpel.Flow;
import de.decidr.model.workflowmodel.bpel.ForEach;
import de.decidr.model.workflowmodel.bpel.From;
import de.decidr.model.workflowmodel.bpel.If;
import de.decidr.model.workflowmodel.bpel.Import;
import de.decidr.model.workflowmodel.bpel.Invoke;
import de.decidr.model.workflowmodel.bpel.Link;
import de.decidr.model.workflowmodel.bpel.Links;
import de.decidr.model.workflowmodel.bpel.Literal;
import de.decidr.model.workflowmodel.bpel.ObjectFactory;
import de.decidr.model.workflowmodel.bpel.PartnerLink;
import de.decidr.model.workflowmodel.bpel.PartnerLinks;
import de.decidr.model.workflowmodel.bpel.Process;
import de.decidr.model.workflowmodel.bpel.Receive;
import de.decidr.model.workflowmodel.bpel.Scope;
import de.decidr.model.workflowmodel.bpel.Sequence;
import de.decidr.model.workflowmodel.bpel.Sources;
import de.decidr.model.workflowmodel.bpel.Targets;
import de.decidr.model.workflowmodel.bpel.To;
import de.decidr.model.workflowmodel.bpel.Variable;
import de.decidr.model.workflowmodel.bpel.Variables;
import de.decidr.model.workflowmodel.dwdl.Actor;
import de.decidr.model.workflowmodel.dwdl.Arc;
import de.decidr.model.workflowmodel.dwdl.BasicNode;
import de.decidr.model.workflowmodel.dwdl.Boolean;
import de.decidr.model.workflowmodel.dwdl.ComplexType;
import de.decidr.model.workflowmodel.dwdl.Condition;
import de.decidr.model.workflowmodel.dwdl.EndNode;
import de.decidr.model.workflowmodel.dwdl.FlowNode;
import de.decidr.model.workflowmodel.dwdl.ForEachNode;
import de.decidr.model.workflowmodel.dwdl.IfNode;
import de.decidr.model.workflowmodel.dwdl.InvokeNode;
import de.decidr.model.workflowmodel.dwdl.Recipient;
import de.decidr.model.workflowmodel.dwdl.Role;
import de.decidr.model.workflowmodel.dwdl.SetProperty;
import de.decidr.model.workflowmodel.dwdl.SimpleType;
import de.decidr.model.workflowmodel.dwdl.Source;
import de.decidr.model.workflowmodel.dwdl.StartNode;
import de.decidr.model.workflowmodel.dwdl.Target;
import de.decidr.model.workflowmodel.dwdl.Workflow;
import de.decidr.model.workflowmodel.webservices.DecidrWebserviceAdapter;

/**
 * This class converts a given DWDL object and returns the resulting BPEL.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DWDL2BPEL {

    private static Logger log = DefaultLogger.getLogger(DWDL2BPEL.class);

    // global process variables names
    private final String SUCCESS_MESSAGE_REQUEST = "successMessageRequest";
    private final String SUCCESS_MESSAGE_RESPONSE = "successMessageResponse";
    private final String FAULT_MESSAGE_REQUEST = "faultMessageRequest";
    private final String FAULT_MESSAGE_RESPONSE = "faultMessageResponse";
    private final String PROCESS_HUMANTASK_INPUT_VARIABLE = "taskDataMessageRequest";
    private final String PROCESS_INPUT_VARIABLE = "startConfigurations";

    // namespace prefixes
    private static final String DECIDRTYPES_PREFIX = "decidr";
    private final String PROCESS_PREFIX = "tns";

    // process message types and operations
    private final String PROCESS_OPERATION = "startProcess";
    private final String PROCESS_REQUEST_MESSAGE = "startMessage";
    private final String PROCESS_HUMANTASK_REQUEST_MESSAGE = "taskCompletedRequest";

    // process partner link names and types
    private final String PROCESS_PARTNERLINK = "ProcessPL";
    private final String PROCESS_PARTNERLINKTYPE = "ProcessPLT";

    // Decidr web services' names
    public static final String EMAIL_ACTIVITY_NAME = "Decidr-Email";
    public static final String HUMANTASK_ACTIVITY_NAME = "Decidr-HumanTask";

    // will be initialized in getBPEL()
    private Process process = null;
    private Workflow dwdl = null;
    private ObjectFactory factory = null;
    private String tenantName = null;
    private Map<String, DecidrWebserviceAdapter> webservices = null;
    // will be initialized in setProcessVariables()
    private Map<DecidrWebserviceAdapter, Variable> webserviceInputVariables = null;
    private Map<DecidrWebserviceAdapter, Variable> webserviceOutputVariables = null;
    // will be initialized in setImports()
    private Map<DecidrWebserviceAdapter, String> webservicePrefixes = null;

    private void addCopyStatement(Assign assign, SetProperty property,
            String toVariable) {
        Copy copy = factory.createCopy();
        From from = factory.createFrom();
        To to = factory.createTo();
        if (!property.isSetVariable() && property.isSetPropertyValue()) {
            Literal literal = factory.createLiteral();
            literal.getContent().addAll(
                    property.getPropertyValue().getContent());
            from.getContent().add(literal);
        } else if (property.isSetVariable() && !property.isSetPropertyValue()) {
            from.setVariable(property.getVariable());
        }
        to.setVariable(toVariable);
        to.setProperty(new QName(process.getTargetNamespace(), property
                .getName(), PROCESS_PREFIX));
        copy.setFrom(from);
        copy.setTo(to);
        assign.getCopyOrExtensionAssignOperation().add(copy);
    }

    private Element createActorElement(Actor actor) {
        org.w3c.dom.Element actorElement = TransformUtil.createDOMElement(
                BPELConstants.DECIDRTYPES_NAMESPACE, DECIDRTYPES_PREFIX
                        + ":actor");
        if (actor.isSetName()) {
            org.w3c.dom.Attr nameAttr = TransformUtil.createAttributeNode(
                    BPELConstants.DECIDRTYPES_NAMESPACE, DECIDRTYPES_PREFIX
                            + ":name");
            nameAttr.setNodeValue(actor.getName());
            actorElement.setAttributeNode(nameAttr);
        } else if (actor.isSetUserId()) {
            org.w3c.dom.Attr userIdAttr = TransformUtil.createAttributeNode(
                    BPELConstants.DECIDRTYPES_NAMESPACE, DECIDRTYPES_PREFIX
                            + ":userId");
            userIdAttr.setNodeValue(String.valueOf(actor.getUserId()));
            actorElement.setAttributeNode(userIdAttr);
        } else if (actor.isSetEmail()) {
            org.w3c.dom.Attr emailAttr = TransformUtil.createAttributeNode(
                    BPELConstants.DECIDRTYPES_NAMESPACE, DECIDRTYPES_PREFIX
                            + ":email");
            emailAttr.setNodeValue(actor.getEmail());
            actorElement.setAttributeNode(emailAttr);
        }

        return actorElement;
    }

    private Element createRoleElement(Role role) {
        org.w3c.dom.Element roleElement = TransformUtil.createDOMElement(
                BPELConstants.DECIDRTYPES_NAMESPACE, DECIDRTYPES_PREFIX
                        + ":role");
        if (role.isSetName()) {
            org.w3c.dom.Attr nameAttr = TransformUtil.createAttributeNode(
                    BPELConstants.DECIDRTYPES_NAMESPACE, DECIDRTYPES_PREFIX
                            + ":name");
            nameAttr.setNodeValue(role.getName());
            roleElement.setAttributeNode(nameAttr);
        } else if (role.isSetActor()) {
            for (Actor actor : role.getActor()) {
                org.w3c.dom.Element actorElement = createActorElement(actor);
                roleElement.appendChild(actorElement);
            }
        }
        return roleElement;
    }

    public Process getBPEL(Workflow dwdl, String tenant,
            Map<String, DecidrWebserviceAdapter> adapters)
            throws TransformerException {

        this.dwdl = dwdl;
        factory = new ObjectFactory();
        process = factory.createProcess();
        this.tenantName = tenant;
        this.webservices = adapters;

        log.trace("setting process attributes");
        setProcessAttributes();

        log.trace("setting process documentation");
        setProcessDocumentation();

        log.trace("setting process imports");
        setImports();

        log.trace("setting process partnerlinks");
        setPartnerLinks();

        log.trace("setting standard global process variables");
        setProcessVariables();

        log.trace("setting process variables");
        setVariables();

        log.trace("setting process variables representing roles");
        setVariablesFromRoles();

        log.trace("setting process correlation sets");
        setCorrelationSets();

        log.trace("setting process fault handler");
        setFaultHandler();

        log.trace("setting process main activity");
        setActivity();

        return process;
    }

    private Sequence getEndActivity(EndNode node) {
        Sequence sequence = factory.createSequence();
        Assign assign = factory.createAssign();
        Invoke emailInvoke = factory.createInvoke();
        setNameAndDocumentation(node, sequence);
        setSourceAndTargets(node, sequence);
        if (node.isSetNotificationOfSuccess()) {
            for (SetProperty property : node.getNotificationOfSuccess()
                    .getSetProperty()) {
                addCopyStatement(assign, property, SUCCESS_MESSAGE_REQUEST);
            }
            for (Recipient recipient : node.getNotificationOfSuccess()
                    .getRecipient()) {
                for (SetProperty property : recipient.getSetProperty()) {
                    addCopyStatement(assign, property, SUCCESS_MESSAGE_REQUEST);
                }
            }
        }
        emailInvoke.setPartnerLink(webservices.get(EMAIL_ACTIVITY_NAME)
                .getPartnerLink().getName());
        emailInvoke.setOperation(webservices.get(EMAIL_ACTIVITY_NAME)
                .getOpertation().getName());
        emailInvoke.setInputVariable(SUCCESS_MESSAGE_REQUEST);
        emailInvoke.setOutputVariable(SUCCESS_MESSAGE_RESPONSE);
        sequence.getActivity().add(assign);
        sequence.getActivity().add(emailInvoke);
        return sequence;
    }

    private Flow getFlowActivity(FlowNode node) {
        Flow flow = factory.createFlow();
        Links links = factory.createLinks();
        flow.setLinks(links);
        setNameAndDocumentation(node, flow);
        setSourceAndTargets(node, flow);
        setArcs(node.getArcs().getArc(), flow.getLinks().getLink());
        setActivityNode(flow.getActivity(), node.getNodes().getAllNodes());
        return flow;
    }

    private ForEach getForEachActivity(ForEachNode node) {
        ForEach foreach = factory.createForEach();
        setNameAndDocumentation(node, foreach);
        setSourceAndTargets(node, foreach);
        if (node.getParallel().equals(Boolean.YES)) {
            foreach.setParallel(de.decidr.model.workflowmodel.bpel.Boolean.YES);
        } else if (node.getParallel().equals(Boolean.NO)) {
            foreach.setParallel(de.decidr.model.workflowmodel.bpel.Boolean.NO);
        }
        foreach.setCounterName(node.getCounterName());
        Expression startExpression = factory.createExpression();
        startExpression.getContent().add(node.getStartCounterValue());
        foreach.setStartCounterValue(startExpression);
        Expression endExpression = factory.createExpression();
        endExpression.getContent().add(node.getFinalCounterValue());
        foreach.setFinalCounterValue(endExpression);
        CompletionCondition completeConditon = factory
                .createCompletionCondition();
        Branches branches = factory.createBranches();
        branches
                .setSuccessfulBranchesOnly(de.decidr.model.workflowmodel.bpel.Boolean.YES);
        branches.getContent().add(node.getCompletionCondition());
        completeConditon.setBranches(branches);
        foreach.setCompletionCondition(completeConditon);
        Scope scope = factory.createScope();
        Flow flow = factory.createFlow();
        setActivityNode(flow.getActivity(), node.getNodes().getAllNodes());
        scope.setFlow(flow);
        foreach.setScope(scope);
        return foreach;
    }

    private If getIfActivity(IfNode node) {
        If ifActivity = factory.createIf();
        setNameAndDocumentation(node, ifActivity);
        setDocumentation(node, ifActivity);
        if (!node.getCondition().isEmpty()) {
            for (Condition dwdlCondition : node.getCondition()) {
                BooleanExpression bpelCondition = factory
                        .createBooleanExpression();
                bpelCondition.getContent().add(dwdlCondition.getLeftOperand());
                bpelCondition.getContent().add(dwdlCondition.getOperator());
                bpelCondition.getContent().add(dwdlCondition.getRightOperand());
                Flow flow = factory.createFlow();
                Links links = factory.createLinks();
                flow.setLinks(links);
                setArcs(dwdlCondition.getArcs().getArc(), flow.getLinks()
                        .getLink());
                setActivityNode(flow.getActivity(), dwdlCondition.getNodes()
                        .getAllNodes());
                if (dwdlCondition.equals(node.getCondition().get(0))) {
                    ifActivity.setCondition(bpelCondition);
                    ifActivity.setFlow(flow);
                } else if (dwdlCondition.equals(node.getCondition().get(
                        node.getCondition().size() - 1))) {
                    ActivityContainer activityContainer = factory
                            .createActivityContainer();
                    activityContainer.setFlow(flow);
                    ifActivity.setElse(activityContainer);
                } else {
                    Elseif elseif = factory.createElseif();
                    elseif.setCondition(bpelCondition);
                    elseif.setFlow(flow);
                    ifActivity.getElseif().add(elseif);
                }
            }
        }
        return ifActivity;
    }

    private Sequence getInvokeSequence(InvokeNode node) {
        Invoke invoke = factory.createInvoke();
        Sequence sequence = factory.createSequence();
        setSourceAndTargets(node, sequence);
        Assign assign = factory.createAssign();
        for (SetProperty property : node.getSetProperty()) {
            addCopyStatement(assign, property, webserviceInputVariables.get(
                    webservices.get(node.getActivity())).getName());
        }
        if (node.getActivity().equals(HUMANTASK_ACTIVITY_NAME)) {
            SetProperty prop = new SetProperty();
            de.decidr.model.workflowmodel.dwdl.Literal lit = new de.decidr.model.workflowmodel.dwdl.Literal();
            lit.getContent().add(node.getParameter().getAny());
            prop.setName(node.getParameter().getSetProperty());
            prop.setPropertyValue(lit);
            addCopyStatement(assign, prop, webserviceInputVariables.get(
                    webservices.get(node.getActivity())).getName());
        }
        sequence.getActivity().add(assign);
        invoke.setPartnerLink(webservices.get(node.getActivity())
                .getPartnerLink().getName());
        invoke.setOperation(webservices.get(node.getActivity()).getOpertation()
                .getName());
        invoke.setInputVariable(webserviceInputVariables.get(
                webservices.get(node.getActivity())).getName());
        invoke.setOutputVariable(webserviceOutputVariables.get(
                webservices.get(node.getActivity())).getName());
        setNameAndDocumentation(node, invoke);
        sequence.getActivity().add(invoke);
        return sequence;
    }

    private Receive getReceiveActivity(StartNode node) {
        Receive receive = factory.createReceive();
        setNameAndDocumentation(node, receive);
        receive.setPartnerLink(tenantName);
        receive.setOperation(PROCESS_OPERATION);
        receive.setVariable(PROCESS_INPUT_VARIABLE);
        receive
                .setCreateInstance(de.decidr.model.workflowmodel.bpel.Boolean.YES);
        setSourceAndTargets(node, receive);

        return receive;
    }

    private Assign initRoles() throws TransformerException {
        Assign assign = null;
        if (dwdl.isSetRoles()) {
            assign = factory.createAssign();
            assign.setName("initRoles");
            Copy copyRole = null;
            for (Role role : dwdl.getRoles().getRole()) {
                if (role.isSetActor()
                        && (!role.isSetConfigurationVariable() || role
                                .getConfigurationVariable().value()
                                .equals("no"))) {
                    copyRole = factory.createCopy();
                    From from = factory.createFrom();
                    To to = factory.createTo();
                    Literal literal = factory.createLiteral();
                    org.w3c.dom.Element roleElement = createRoleElement(role);
                    literal.getContent().add(
                            (TransformUtil.element2XML(roleElement)));
                    from.getContent().add(literal);
                    to.setVariable(role.getName());
                    copyRole.setFrom(from);
                    copyRole.setTo(to);
                    assign.getCopyOrExtensionAssignOperation().add(copyRole);
                }

            }
            if (dwdl.getRoles().isSetActor()) {
                Copy copyActor = null;
                for (Actor actor : dwdl.getRoles().getActor()) {
                    if (!actor.isSetConfigurationVariable()
                            || actor.getConfigurationVariable().value().equals(
                                    "no")) {
                        copyActor = factory.createCopy();
                        From from = factory.createFrom();
                        To to = factory.createTo();
                        Literal literal = factory.createLiteral();
                        org.w3c.dom.Node actorElement = createActorElement(actor);
                        literal.getContent().add(
                                TransformUtil.element2XML(actorElement));
                        from.getContent().add(literal);
                        to.setVariable(actor.getName());
                        copyActor.setFrom(from);
                        copyActor.setTo(to);
                        assign.getCopyOrExtensionAssignOperation().add(
                                copyActor);
                    }
                }
            }
        }
        return assign;
    }

    private Assign initVariables() {
        Assign assign = null;
        assign = factory.createAssign();
        assign.setName("initVariables");
        if (dwdl.isSetVariables() && dwdl.getVariables().isSetVariable()) {
            Copy copy = factory.createCopy();
            for (de.decidr.model.workflowmodel.dwdl.Variable dwdlVariable : dwdl
                    .getVariables().getVariable()) {
                if (dwdlVariable.isSetInitialValue()
                        && !dwdlVariable.isSetInitialValues()) {
                    From from = factory.createFrom();
                    To to = factory.createTo();
                    Literal literal = factory.createLiteral();
                    literal.getContent().addAll(
                            dwdlVariable.getInitialValue().getContent());
                    from.getContent().add(literal);
                    to.setVariable(dwdlVariable.getName());
                    copy.setFrom(from);
                    copy.setTo(to);
                    assign.getCopyOrExtensionAssignOperation().add(copy);
                } else if (dwdlVariable.isSetInitialValues()
                        && !dwdlVariable.isSetInitialValue()) {
                    Literal literal = factory.createLiteral();
                    for (de.decidr.model.workflowmodel.dwdl.Literal initialValue : dwdlVariable
                            .getInitialValues().getInitialValue()) {
                        literal.getContent().addAll(initialValue.getContent());
                    }
                    From from = factory.createFrom();
                    To to = factory.createTo();
                    from.getContent().add(literal);
                    to.setVariable(dwdlVariable.getName());
                    assign.getCopyOrExtensionAssignOperation().add(copy);
                }
            }
        }
        return assign;
    }

    private void setActivity() throws TransformerException {
        Sequence mainSequence = factory.createSequence();
        mainSequence.setName("mainSequence");
        Assign initVariables = initVariables();
        Assign initRoles = initRoles();
        mainSequence.getActivity().add(initVariables);
        mainSequence.getActivity().add(initRoles);
        Flow mainFlow = factory.createFlow();
        Links links = factory.createLinks();
        mainFlow.setName("mainFlow");
        mainFlow.setLinks(links);
        setArcs(dwdl.getArcs().getArc(), mainFlow.getLinks().getLink());
        setActivityNode(mainFlow.getActivity(), dwdl.getNodes().getAllNodes());

        mainSequence.getActivity().add(mainFlow);
        process.setSequence(mainSequence);
    }

    private void setActivityNode(List<Object> activityList,
            List<BasicNode> nodes) {
        for (BasicNode node : nodes) {
            if (node instanceof StartNode) {
                Receive receive = getReceiveActivity((StartNode) node);
                activityList.add(receive);
            } else if (node instanceof EndNode) {
                Sequence replySequence = getEndActivity((EndNode) node);
                activityList.add(replySequence);
            } else if (node instanceof FlowNode) {
                Flow flow = getFlowActivity((FlowNode) node);
                activityList.add(flow);
            } else if (node instanceof ForEachNode) {
                ForEach foreach = getForEachActivity((ForEachNode) node);
                activityList.add(foreach);
            } else if (node instanceof IfNode) {
                If ifActivity = getIfActivity((IfNode) node);
                activityList.add(ifActivity);
            } else if (node instanceof InvokeNode) {
                Sequence invokeSequence = getInvokeSequence((InvokeNode) node);
                activityList.add(invokeSequence);
            }
        }

    }

    private void setArcs(List<Arc> arcs, List<Link> links) {
        if (arcs != null && !arcs.isEmpty()) {
            for (Arc arc : arcs) {
                Link link = factory.createLink();
                link.setName("a" + String.valueOf(arc.getId()));
                links.add(link);
            }
        }
    }

    private void setCorrelationSets() {
        CorrelationSets correlationSets = factory.createCorrelationSets();
        process.setCorrelationSets(correlationSets);
        CorrelationSet correlation = factory.createCorrelationSet();
        correlation.setName("standard-correlation");
        for (String propertyName : BPELConstants.CORRELATION_PROPERTIES) {
            correlation.getProperties().add(
                    new QName(process.getTargetNamespace(), propertyName,
                            PROCESS_PREFIX));
        }
        process.getCorrelationSets().getCorrelationSet().add(correlation);
    }

    private void setDocumentation(BasicNode fromNode,
            ExtensibleElements toActivity) {
        Documentation documentation = factory.createDocumentation();
        documentation.getContent().add(fromNode.getDescription());
        toActivity.getDocumentation().add(documentation);
    }

    private void setFaultHandler() {
        FaultHandlers faultHandlers = factory.createFaultHandlers();
        ActivityContainer activityContainer = factory.createActivityContainer();
        Sequence sequence = factory.createSequence();
        Assign assign = factory.createAssign();
        Invoke emailInvoke = factory.createInvoke();
        if (dwdl.isSetFaultHandler()
                && dwdl.getFaultHandler().isSetSetProperty()) {
            for (SetProperty property : dwdl.getFaultHandler().getSetProperty()) {
                addCopyStatement(assign, property, FAULT_MESSAGE_REQUEST);
            }
            if (dwdl.getFaultHandler().isSetRecipient()) {
                for (Recipient recipient : dwdl.getFaultHandler()
                        .getRecipient()) {
                    for (SetProperty property : recipient.getSetProperty()) {
                        addCopyStatement(assign, property,
                                FAULT_MESSAGE_REQUEST);
                    }
                }
            }
            emailInvoke.setPartnerLink(webservices.get(EMAIL_ACTIVITY_NAME)
                    .getPartnerLink().getName());
            emailInvoke.setOperation(webservices.get(EMAIL_ACTIVITY_NAME)
                    .getOpertation().getName());
            emailInvoke.setInputVariable(FAULT_MESSAGE_REQUEST);
            emailInvoke.setOutputVariable(FAULT_MESSAGE_RESPONSE);
            sequence.getActivity().add(assign);
            sequence.getActivity().add(emailInvoke);
            activityContainer.setSequence(sequence);
            faultHandlers.setCatchAll(activityContainer);
            process.setFaultHandlers(faultHandlers);
        }

    }

    private void setImports() {
        Import dtImport = factory.createImport();
        Import odeImport = factory.createImport();
        Import processImport = factory.createImport();
        webservicePrefixes = new HashMap<DecidrWebserviceAdapter, String>();
        // import all known web services
        byte wsPrefixNumber = 1;
        for (DecidrWebserviceAdapter adapter : webservices.values()) {
            Import wsImport = factory.createImport();
            wsImport.setNamespace(adapter.getTargetNamespace());
            wsImport.setLocation(adapter.getLocation());
            wsImport.setImportType(Constants.NS_URI_WSDL11);
            // initialize web service prefixes
            webservicePrefixes.put(adapter, "ws" + wsPrefixNumber);
            process.getImport().add(wsImport);
            wsPrefixNumber++;
        }

        // setting import for DecidrTypes
        dtImport.setNamespace(BPELConstants.DECIDRTYPES_NAMESPACE);
        dtImport.setImportType(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        dtImport.setLocation(BPELConstants.DECIDRTYPES_LOCATION);

        // setting import for Ode extension
        odeImport.setNamespace(BPELConstants.ODE_NAMESPACE);
        odeImport.setImportType(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // setting import for process WSDL
        processImport.setNamespace(process.getTargetNamespace());
        processImport.setImportType(Constants.NS_URI_WSDL11);
        // setting location with space replaced with underscore
        processImport
                .setLocation(process.getName().replace(" ", "_") + ".wsdl");

        // adding imports to process
        process.getImport().add(odeImport);
        process.getImport().add(dtImport);
        process.getImport().add(processImport);
    }

    private void setNameAndDocumentation(BasicNode fromNode, Activity toNode) {
        if (fromNode.getName() != null) {
            toNode.setName(fromNode.getName());
        }
        if (fromNode.getDescription() != null) {
            setDocumentation(fromNode, toNode);
        }
    }

    private void setPartnerLinks() {
        PartnerLinks pls = factory.createPartnerLinks();
        process.setPartnerLinks(pls);

        // create partner links for all known web services
        for (DecidrWebserviceAdapter adapter : webservices.values()) {
            PartnerLink wsPL = factory.createPartnerLink();
            wsPL.setName(adapter.getPartnerLink().getName());

            wsPL.setPartnerLinkType(new QName(adapter.getTargetNamespace(),
                    adapter.getPartnerLinkType().getName(), webservicePrefixes
                            .get(adapter)));
            wsPL.setPartnerRole(adapter.getName().getLocalPart() + "Provider");
            // MA How to integrate callback-potential webservices?

            process.getPartnerLinks().getPartnerLink().add(wsPL);
        }
        
        // Decidr HumanTask web service callback
        // MA Think about it!
        PartnerLink humanTaskPL = factory.createPartnerLink();
        humanTaskPL.setName(webservices.get(HUMANTASK_ACTIVITY_NAME).getName().getLocalPart()+"Callback");
        humanTaskPL.setPartnerLinkType(new QName(webservices.get(HUMANTASK_ACTIVITY_NAME).getTargetNamespace(),
                webservices.get(HUMANTASK_ACTIVITY_NAME).getPartnerLinkType().getName(), webservicePrefixes
                .get(webservices.get(HUMANTASK_ACTIVITY_NAME))));
        humanTaskPL.setMyRole("ProcessProvider");

        // create process client partner link
        PartnerLink processPL = factory.createPartnerLink();
        processPL.setName(PROCESS_PARTNERLINK);
        processPL.setPartnerLinkType(new QName(process.getTargetNamespace(),
                PROCESS_PARTNERLINKTYPE, PROCESS_PREFIX));
        processPL.setMyRole("ProcessProvider");
        process.getPartnerLinks().getPartnerLink().add(processPL);
    }

    private void setProcessAttributes() {

        // sets the targetNamespace and name of the process
        if (dwdl.getName() != null) {
            process.setName(dwdl.getName());
            process.setTargetNamespace(dwdl.getTargetNamespace());
        }

        // add id attribute to process element
        process.getOtherAttributes().put(
                new QName(BPELConstants.DECIDRTYPES_NAMESPACE, "id",
                        DECIDRTYPES_PREFIX), String.valueOf(dwdl.getId()));
    }

    private void setProcessDocumentation() {
        Documentation documentation = null;
        if (dwdl.getDescription() != null) {
            documentation = factory.createDocumentation();
            documentation.getContent().add(dwdl.getDescription());
            process.getDocumentation().add(documentation);
        }
    }

    private void setProcessVariables() {
        Variables variables = factory.createVariables();
        webserviceInputVariables = new HashMap<DecidrWebserviceAdapter, Variable>();
        webserviceOutputVariables = new HashMap<DecidrWebserviceAdapter, Variable>();

        // create global process variables
        Variable startConfigurations = factory.createVariable();
        Variable wfmid = factory.createVariable();
        Variable faultMessage = factory.createVariable();
        Variable faultMessageResponse = factory.createVariable();
        Variable successMessage = factory.createVariable();
        Variable successMessageResponse = factory.createVariable();
        Variable taskDataMessage = factory.createVariable();

        // create web service invocation standard variables
        for (DecidrWebserviceAdapter adapter : webservices.values()) {
            Variable inputVariable = factory.createVariable();
            Variable outputVariable = factory.createVariable();
            inputVariable.setName(webservicePrefixes.get(adapter)
                    + adapter.getOpertation().getName() + "Input");
            inputVariable.setMessageType(new QName(
                    adapter.getTargetNamespace(),
                    adapter.getInputMessageType().getLocalPart(), webservicePrefixes
                            .get(adapter)));
            outputVariable.setName(webservicePrefixes.get(adapter)
                    + adapter.getOpertation() + "Output");
            outputVariable.setMessageType(new QName(adapter
                    .getTargetNamespace(), adapter.getOutputMessageType().getLocalPart(),
                    webservicePrefixes.get(adapter)));

            webserviceInputVariables.put(adapter, inputVariable);
            webserviceOutputVariables.put(adapter, outputVariable);
        }

        // setting process start variable
        startConfigurations.setName(PROCESS_INPUT_VARIABLE);
        startConfigurations
                .setMessageType(new QName(process.getTargetNamespace(),
                        PROCESS_REQUEST_MESSAGE, PROCESS_PREFIX));

        // setting process id variable
        wfmid.setName("WFMID");
        wfmid.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "long",
                "xsd"));

        // setting fault handler variables
        faultMessage.setName(FAULT_MESSAGE_REQUEST);
        faultMessage.setMessageType(new QName(webservices.get(
                EMAIL_ACTIVITY_NAME).getTargetNamespace(), webservices.get(
                EMAIL_ACTIVITY_NAME).getInputMessageType().getLocalPart(), webservicePrefixes
                .get(webservices.get(EMAIL_ACTIVITY_NAME))));
        faultMessageResponse.setName(FAULT_MESSAGE_RESPONSE);
        faultMessageResponse.setMessageType(new QName(webservices.get(
                EMAIL_ACTIVITY_NAME).getTargetNamespace(), webservices.get(
                EMAIL_ACTIVITY_NAME).getOutputMessageType().getLocalPart(), webservicePrefixes
                .get(webservices.get(EMAIL_ACTIVITY_NAME))));

        // setting success notification variables
        successMessage.setName(SUCCESS_MESSAGE_REQUEST);
        successMessage.setMessageType(new QName(webservices.get(
                EMAIL_ACTIVITY_NAME).getTargetNamespace(), webservices.get(
                EMAIL_ACTIVITY_NAME).getInputMessageType().getLocalPart(), webservicePrefixes
                .get(webservices.get(EMAIL_ACTIVITY_NAME))));
        successMessageResponse.setName(SUCCESS_MESSAGE_RESPONSE);
        successMessageResponse.setMessageType(new QName(webservices.get(
                EMAIL_ACTIVITY_NAME).getTargetNamespace(), webservices.get(
                EMAIL_ACTIVITY_NAME).getOutputMessageType().getLocalPart(), webservicePrefixes
                .get(webservices.get(EMAIL_ACTIVITY_NAME))));

        // setting process human task data receiving variable
        taskDataMessage.setName(PROCESS_HUMANTASK_INPUT_VARIABLE);
        taskDataMessage.setMessageType(new QName(process.getTargetNamespace(),
                PROCESS_HUMANTASK_REQUEST_MESSAGE, PROCESS_PREFIX));

        // add variables to process
        process.setVariables(variables);
        process.getVariables().getVariable().add(startConfigurations);
        process.getVariables().getVariable().add(faultMessage);
        process.getVariables().getVariable().add(faultMessageResponse);
        process.getVariables().getVariable().add(successMessage);
        process.getVariables().getVariable().add(successMessageResponse);
        process.getVariables().getVariable().add(taskDataMessage);
    }

    private void setSourceAndTargets(BasicNode fromNode, Activity toActivity) {
        if (fromNode.getSources() != null
                && !fromNode.getSources().getSource().isEmpty()) {
            Sources sources = factory.createSources();
            for (Source src : fromNode.getSources().getSource()) {
                de.decidr.model.workflowmodel.bpel.Source source = factory
                        .createSource();
                source.setLinkName("a" + String.valueOf(src.getArcId()));
                sources.getSource().add(source);
            }
            toActivity.setSources(sources);
        }
        if (fromNode.getTargets() != null
                && !fromNode.getTargets().getTarget().isEmpty()) {
            Targets targets = factory.createTargets();
            for (Target trg : fromNode.getTargets().getTarget()) {
                de.decidr.model.workflowmodel.bpel.Target target = factory
                        .createTarget();
                target.setLinkName("a" + String.valueOf(trg.getArcId()));
                targets.getTarget().add(target);
            }
            toActivity.setTargets(targets);
        }
    }

    private void setVariables() {
        if (dwdl.isSetVariables()) {
            for (de.decidr.model.workflowmodel.dwdl.Variable dwdlVariable : dwdl
                    .getVariables().getVariable()) {
                de.decidr.model.workflowmodel.bpel.Variable bpelVariable = factory
                        .createVariable();
                bpelVariable.setName(dwdlVariable.getName());
                if (Arrays.asList(SimpleType.values()).contains(
                        SimpleType.fromValue(dwdlVariable.getType()))) {
                    bpelVariable.setType(new QName(
                            XMLConstants.W3C_XML_SCHEMA_NS_URI, dwdlVariable
                                    .getType(), "xsd"));
                } else if (ComplexType.fromValue(dwdlVariable.getType()) == null) {
                    bpelVariable.setType(new QName(
                            BPELConstants.DECIDRTYPES_NAMESPACE, dwdlVariable
                                    .getType(), DECIDRTYPES_PREFIX));

                } else {
                    bpelVariable.setType(new QName(
                            XMLConstants.W3C_XML_SCHEMA_NS_URI, "anyType",
                            "xsd"));
                }
                // MA please - for god sake - change decidr types
                process.getVariables().getVariable().add(bpelVariable);
            }
        }
    }

    private void setVariablesFromRoles() {
        if (dwdl.isSetRoles()) {
            if (!dwdl.getRoles().isSetRole()) {
                for (Role r : dwdl.getRoles().getRole()) {
                    de.decidr.model.workflowmodel.bpel.Variable role = factory
                            .createVariable();
                    role.setName(r.getName());
                    role.setElement(new QName(
                            BPELConstants.DECIDRTYPES_NAMESPACE, "role",
                            DECIDRTYPES_PREFIX));
                    process.getVariables().getVariable().add(role);
                }
            }
            if (dwdl.getRoles().isSetActor()) {
                for (Actor a : dwdl.getRoles().getActor()) {
                    de.decidr.model.workflowmodel.bpel.Variable actor = factory
                            .createVariable();
                    actor.setName(a.getName());
                    actor.setElement(new QName(
                            BPELConstants.DECIDRTYPES_NAMESPACE, "actor",
                            DECIDRTYPES_PREFIX));
                    process.getVariables().getVariable().add(actor);
                }
            }
        }
    }

}
