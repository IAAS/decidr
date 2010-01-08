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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

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
import de.decidr.model.workflowmodel.bpel.Reply;
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
 * This class transforms a given {@link Workflow} object into {@link Process}
 * object. Following naming convention are used: <br>
 * <li>WSDL import location = WSDL local name + ".wsdl"</li> <li>Process WSDL
 * location = Process name + ".wsdl"</li> <li>Web service input/ output variable
 * name = Web service operation name + "In"/"Out"</li>
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DWDL2BPEL {

    private static Logger log = DefaultLogger.getLogger(DWDL2BPEL.class);

    // will be initialized in getBPEL()
    private Process process = null;
    private Workflow dwdl = null;
    private ObjectFactory factory = null;
    private Map<String, DecidrWebserviceAdapter> adapters = null;
    // will be initialized in setProcessVariables()
    private Map<DecidrWebserviceAdapter, Variable> webserviceInputVariables = null;
    private Map<DecidrWebserviceAdapter, Variable> webserviceOutputVariables = null;

    private void addCopyStatement(Assign assign, Element element,
            SetProperty property, String toVariable) {
        Copy copy = factory.createCopy();
        From from = factory.createFrom();
        To to = factory.createTo();
        Literal literal = factory.createLiteral();
        literal.getContent().add(element);
        from.getContent().add(literal);
        to.setVariable(toVariable);
        to.setProperty(new QName(process.getTargetNamespace(), property
                .getName()));
        copy.setFrom(from);
        copy.setTo(to);
        assign.getCopyOrExtensionAssignOperation().add(copy);
    }

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
                .getName()));
        copy.setFrom(from);
        copy.setTo(to);
        assign.getCopyOrExtensionAssignOperation().add(copy);
    }

    private Element createActorElement(Actor actor) {
        de.decidr.model.soap.types.Actor decidrActor = new de.decidr.model.soap.types.Actor();
        if (actor.isSetEmail()) {
            decidrActor.setEmail(actor.getEmail());
        }
        if (actor.isSetName()) {
            decidrActor.setName(actor.getName());
        }
        if (actor.isSetUserId()) {
            decidrActor.setUserid(actor.getUserId());
        }
        return BPELHelper.getActorElement(decidrActor);
    }

    private Element createRecipientElement(Recipient recipient,
            SetProperty property) {
        de.decidr.model.soap.types.Actor decidrActor = new de.decidr.model.soap.types.Actor();
        decidrActor.setEmail(property.getPropertyValue().toString());
        return BPELHelper.getActorElement(decidrActor);
    }

    private Element createRoleElement(Role role) {
        de.decidr.model.soap.types.Role decidrRole = new de.decidr.model.soap.types.Role();
        if (role.isSetName()) {
            decidrRole.setName(role.getName());
        }
        for (Actor actor : role.getActor()) {
            de.decidr.model.soap.types.Actor decidrActor = new de.decidr.model.soap.types.Actor();
            if (actor.isSetEmail()) {
                decidrActor.setEmail(actor.getEmail());
            }
            if (actor.isSetName()) {
                decidrActor.setName(actor.getName());
            }
            if (actor.isSetUserId()) {
                decidrActor.setUserid(actor.getUserId());
            }
            decidrRole.getActor().add(decidrActor);
        }
        return BPELHelper.getRoleElement(decidrRole);
    }

    public Process getBPEL(Workflow dwdl,
            Map<String, DecidrWebserviceAdapter> adapters) {

        this.dwdl = dwdl;
        factory = new ObjectFactory();
        process = factory.createProcess();
        this.adapters = adapters;

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
                if (property.getName().equals(
                        PropertyConstants.Notification.MESSAGE)) {
                    addCopyStatement(assign, property,
                            BPELConstants.Variables.SUCCESS_IN);
                }
            }
            for (Recipient recipient : node.getNotificationOfSuccess()
                    .getRecipient()) {
                for (SetProperty property : recipient.getSetProperty()) {
                    if (property.getName().equals(
                            PropertyConstants.Recipient.TO)) {
                        if (property.isSetPropertyValue()) {
                            addCopyStatement(assign, createRecipientElement(
                                    recipient, property), property,
                                    BPELConstants.Variables.FAULT_IN);
                        } else if (property.isSetVariable()) {
                            addCopyStatement(assign, property,
                                    BPELConstants.Variables.FAULT_IN);
                        }
                    }
                }
            }
        }
        emailInvoke.setPartnerLink(adapters.get(BPELConstants.Email.NAME)
                .getPartnerLink().getName());
        emailInvoke.setOperation(adapters.get(BPELConstants.Email.NAME)
                .getOpertation().getName());
        emailInvoke.setInputVariable(BPELConstants.Variables.SUCCESS_IN);
        emailInvoke.setOutputVariable(BPELConstants.Variables.SUCCESS_OUT);
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
        if (node.getActivity().equals(BPELConstants.Humantask.NAME)) {
            for (SetProperty property : node.getSetProperty()) {
                addCopyStatement(assign, property, webserviceInputVariables
                        .get(adapters.get(node.getActivity())).getName());
            }
            Copy copy = factory.createCopy();
            From from = factory.createFrom();
            To to = factory.createTo();
            Literal literal = factory.createLiteral();
            literal.getContent().addAll(node.getParameter().getContent());
            from.getContent().add(literal);
            to.setVariable(webserviceInputVariables.get(
                    adapters.get(node.getActivity())).getName());
            to.setProperty(new QName(process.getTargetNamespace(),
                    PropertyConstants.Humantask.TASKDESCRIPTION));
            copy.setFrom(from);
            copy.setTo(to);
            assign.getCopyOrExtensionAssignOperation().add(copy);
        }

        invoke.setPartnerLink(adapters.get(node.getActivity()).getPartnerLink()
                .getName());
        invoke.setOperation(adapters.get(node.getActivity()).getOpertation()
                .getName());
        invoke.setInputVariable(webserviceInputVariables.get(
                adapters.get(node.getActivity())).getName());
        invoke.setOutputVariable(webserviceOutputVariables.get(
                adapters.get(node.getActivity())).getName());
        setNameAndDocumentation(node, invoke);
        sequence.getActivity().add(assign);
        sequence.getActivity().add(invoke);
        return sequence;
    }

    private Sequence getReceiveSequence(StartNode startNode) {
        Sequence sequence = factory.createSequence();
        Receive receive = factory.createReceive();
        Reply reply = factory.createReply();
        setNameAndDocumentation(startNode, receive);
        receive.setPartnerLink(BPELConstants.Process.PARTNERLINK);
        receive.setOperation(WSDLConstants.PROCESS_OPERATION);
        receive.setVariable(BPELConstants.Variables.PROCESS_IN);
        reply.setPartnerLink(BPELConstants.Process.PARTNERLINK);
        reply.setOperation(WSDLConstants.PROCESS_OPERATION);
        reply.setVariable(BPELConstants.Variables.PROCESS_OUT);
        sequence.getActivity().add(receive);
        sequence.getActivity().add(reply);
        setSourceAndTargets(startNode, sequence);

        return sequence;
    }

    private Assign initRoles() {
        Assign assign = null;
        if (dwdl.isSetRoles()) {
            assign = factory.createAssign();
            assign.setName("initRoles");
            for (Role role : dwdl.getRoles().getRole()) {
                if (!role.isSetConfigurationVariable()
                        || role.getConfigurationVariable().equals(Boolean.NO)) {
                    addCopyStatement(assign, createRoleElement(role), role
                            .getName());
                } else if (role.isSetConfigurationVariable()
                        && role.getConfigurationVariable().equals(Boolean.YES)) {
                    Copy copy = factory.createCopy();
                    From from = factory.createFrom();
                    To to = factory.createTo();
                    from.setVariable(BPELConstants.Variables.PROCESS_IN);
                    from.setProperty(new QName(process.getTargetNamespace(),
                            role.getName()));
                    to.setVariable(role.getName());
                    copy.setFrom(from);
                    copy.setTo(to);
                    assign.getCopyOrExtensionAssignOperation().add(copy);
                }
            }
            for (Actor actor : dwdl.getRoles().getActor()) {
                if (!actor.isSetConfigurationVariable()
                        || actor.getConfigurationVariable().equals(Boolean.NO)) {
                    addCopyStatement(assign, createActorElement(actor), actor
                            .getName());
                } else if (actor.isSetConfigurationVariable()
                        && actor.getConfigurationVariable().equals(Boolean.YES)) {
                    Copy copy = factory.createCopy();
                    From from = factory.createFrom();
                    To to = factory.createTo();
                    from.setVariable(BPELConstants.Variables.PROCESS_IN);
                    from.setProperty(new QName(process.getTargetNamespace(),
                            actor.getName()));
                    to.setVariable(actor.getName());
                    copy.setFrom(from);
                    copy.setTo(to);
                    assign.getCopyOrExtensionAssignOperation().add(copy);
                }
            }
        }
        return assign;
    }

    private void addCopyStatement(Assign assign, Element element,
            String toVariable) {
        Copy copy = factory.createCopy();
        From from = factory.createFrom();
        To to = factory.createTo();
        Literal literal = factory.createLiteral();
        literal.getContent().add(element);
        from.getContent().add(literal);
        to.setVariable(toVariable);
        copy.setFrom(from);
        copy.setTo(to);
        assign.getCopyOrExtensionAssignOperation().add(copy);
    }

    private Assign initVariables() {
        Assign assign = factory.createAssign();
        assign.setName("initVariables");

        if (dwdl.isSetVariables()) {
            for (de.decidr.model.workflowmodel.dwdl.Variable dwdlVariable : dwdl
                    .getVariables().getVariable()) {
                if (!dwdlVariable.isSetConfigurationVariable()
                        || dwdlVariable.getConfigurationVariable().equals(
                                Boolean.NO)) {
                    if (dwdlVariable.isSetInitialValue()
                            && !dwdlVariable.isSetInitialValues()) {
                        Copy copy = factory.createCopy();
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
                        Copy copy = factory.createCopy();
                        Literal literal = factory.createLiteral();
                        for (de.decidr.model.workflowmodel.dwdl.Literal initialValue : dwdlVariable
                                .getInitialValues().getInitialValue()) {
                            literal.getContent().addAll(
                                    initialValue.getContent());
                        }
                        From from = factory.createFrom();
                        To to = factory.createTo();
                        from.getContent().add(literal);
                        to.setVariable(dwdlVariable.getName());
                        copy.setFrom(from);
                        copy.setTo(to);
                        assign.getCopyOrExtensionAssignOperation().add(copy);
                    }
                } else if (dwdlVariable.isSetConfigurationVariable()
                        && dwdlVariable.getConfigurationVariable().equals(
                                Boolean.YES)) {
                    Copy copy = factory.createCopy();
                    From from = factory.createFrom();
                    To to = factory.createTo();
                    from.setVariable(BPELConstants.Variables.PROCESS_IN);
                    from.setProperty(new QName(process.getTargetNamespace(),
                            dwdlVariable.getName()));
                    to.setVariable(dwdlVariable.getName());
                    copy.setFrom(from);
                    copy.setTo(to);
                    assign.getCopyOrExtensionAssignOperation().add(copy);
                }

            }
        }
        // save pid in PROCESS_OUTPUT_VARIABLE
        From from = factory.createFrom();
        from.getContent().add("$ode:pid");
        To to = factory.createTo();
        to.setVariable(BPELConstants.Variables.PROCESS_OUT);
        Copy copy = factory.createCopy();
        copy.setFrom(from);
        copy.setTo(to);
        assign.getCopyOrExtensionAssignOperation().add(copy);
        return assign;
    }

    private boolean isComplexType(String type) {
        boolean result = false;
        for (ComplexType c : ComplexType.values()) {
            if (c.value().equals(type)) {
                return true;
            }
        }
        return result;
    }

    private boolean isSimpleType(String type) {
        boolean result = false;
        for (SimpleType s : SimpleType.values()) {
            if (s.value().equals(type)) {
                return true;
            }
        }
        return result;
    }

    private void setActivity() {
        Sequence mainSequence = factory.createSequence();
        mainSequence.setName("mainSequence");
        Assign initVariables = initVariables();
        Assign initRoles = initRoles();
        mainSequence.getActivity().add(initVariables);
        mainSequence.getActivity().add(initRoles);
        Flow mainFlow = factory.createFlow();
        mainFlow.setName("mainFlow");

        if (dwdl.isSetArcs()) {
            Links links = factory.createLinks();
            setArcs(dwdl.getArcs().getArc(), links.getLink());
            mainFlow.setLinks(links);
        }

        if (dwdl.isSetNodes()) {
            setActivityNode(mainFlow.getActivity(), dwdl.getNodes()
                    .getAllNodes());
        }
        mainSequence.getActivity().add(mainFlow);
        process.setSequence(mainSequence);
    }

    private void setActivityNode(List<Object> activityList,
            List<BasicNode> nodes) {
        for (BasicNode node : nodes) {
            if (node instanceof StartNode) {
                Sequence receiveSequence = getReceiveSequence((StartNode) node);
                activityList.add(receiveSequence);
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
        for (Arc arc : arcs) {
            Link link = factory.createLink();
            link.setName("link" + String.valueOf(arc.getId()));
            links.add(link);
        }
    }

    private void setCorrelationSets() {
        CorrelationSets correlationSets = factory.createCorrelationSets();
        CorrelationSet correlation = factory.createCorrelationSet();
        correlation.setName(BPELConstants.Process.CORRELATION);
        for (String propertyName : BPELConstants.Process.CORRELATION_PROPERTIES) {
            correlation.getProperties().add(
                    new QName(adapters.get(BPELConstants.Humantask.NAME)
                            .getTargetNamespace(), propertyName));
        }
        process.setCorrelationSets(correlationSets);
        process.getCorrelationSets().getCorrelationSet().add(correlation);
    }

    private void setDocumentation(BasicNode fromNode,
            ExtensibleElements toActivity) {
        if (fromNode.isSetDescription()) {
            Documentation documentation = factory.createDocumentation();
            documentation.getContent().add(fromNode.getDescription());
            toActivity.getDocumentation().add(documentation);
        }
    }

    private void setFaultHandler() {
        FaultHandlers faultHandlers = factory.createFaultHandlers();
        ActivityContainer activityContainer = factory.createActivityContainer();
        Sequence sequence = factory.createSequence();
        Assign assign = factory.createAssign();
        Invoke emailInvoke = factory.createInvoke();
        if (dwdl.isSetFaultHandler()) {
            for (SetProperty property : dwdl.getFaultHandler().getSetProperty()) {
                if (property.getName().equals(
                        PropertyConstants.FaultHandler.MESSAGE)) {
                    addCopyStatement(assign, property,
                            BPELConstants.Variables.FAULT_IN);
                }
            }
            for (Recipient recipient : dwdl.getFaultHandler().getRecipient()) {
                for (SetProperty property : recipient.getSetProperty()) {
                    if (property.getName().equals(
                            PropertyConstants.Recipient.TO)) {
                        if (property.isSetPropertyValue()) {
                            addCopyStatement(assign, createRecipientElement(
                                    recipient, property), property,
                                    BPELConstants.Variables.FAULT_IN);
                        } else if (property.isSetVariable()) {
                            addCopyStatement(assign, property,
                                    BPELConstants.Variables.FAULT_IN);
                        }
                    }
                }
            }
        }
        emailInvoke.setPartnerLink(adapters.get(BPELConstants.Email.NAME)
                .getPartnerLink().getName());
        emailInvoke.setOperation(adapters.get(BPELConstants.Email.NAME)
                .getOpertation().getName());
        emailInvoke.setInputVariable(BPELConstants.Variables.FAULT_IN);
        emailInvoke.setOutputVariable(BPELConstants.Variables.FAULT_OUT);
        sequence.getActivity().add(assign);
        sequence.getActivity().add(emailInvoke);
        activityContainer.setSequence(sequence);
        faultHandlers.setCatchAll(activityContainer);

        process.setFaultHandlers(faultHandlers);
    }

    private void setImports() {
        Import dtImport = factory.createImport();
        Import dtpImport = factory.createImport();
        Import odeImport = factory.createImport();
        Import processImport = factory.createImport();
        // import all known web services
        for (DecidrWebserviceAdapter adapter : adapters.values()) {
            Import wsImport = factory.createImport();
            wsImport.setNamespace(adapter.getTargetNamespace());
            wsImport.setLocation(adapter.getName().getLocalPart() + ".wsdl");
            wsImport.setImportType(Constants.WSDL_NAMESPACE);
            // initialize web service prefixes
            process.getImport().add(wsImport);
        }

        // setting import for DecidrTypes
        dtImport.setNamespace(Constants.DECIDRTYPES_NAMESPACE);
        dtImport.setImportType(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        dtImport.setLocation(Constants.DECIDRTYPES_LOCATION);

        // setting import for DecidrTypes
        dtpImport.setNamespace(Constants.DECIDRPROCESSTYPES_NAMESPACE);
        dtpImport.setImportType(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        dtpImport.setLocation(Constants.DECIDRPROCESSTYPES_LOCATION);

        // setting import for Ode extension
        odeImport.setNamespace(Constants.ODE_NAMESPACE);
        odeImport.setImportType(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // setting import for process WSDL
        processImport.setNamespace(process.getTargetNamespace());
        processImport.setImportType(Constants.WSDL_NAMESPACE);
        processImport.setLocation(process.getName() + ".wsdl");

        // adding imports to process
        process.getImport().add(odeImport);
        process.getImport().add(dtImport);
        process.getImport().add(dtpImport);
        process.getImport().add(processImport);
    }

    private void setNameAndDocumentation(BasicNode fromNode, Activity toNode) {
        if (fromNode.isSetName()) {
            toNode.setName(fromNode.getName());
        }
        if (fromNode.isSetDescription()) {
            setDocumentation(fromNode, toNode);
        }
    }

    private void setPartnerLinks() {
        PartnerLinks partnerLinks = factory.createPartnerLinks();

        // create partner links for all known web services
        for (DecidrWebserviceAdapter adapter : adapters.values()) {
            PartnerLink wsPL = factory.createPartnerLink();
            wsPL.setName(adapter.getPartnerLink().getName());
            if (adapter.getPartnerLink().isSetMyRole()) {
                wsPL.setMyRole(adapter.getPartnerLink().getMyRole());
            }
            if (adapter.getPartnerLink().isSetPartnerRole()) {
                wsPL.setPartnerRole(adapter.getPartnerLink().getPartnerRole());
            }
            wsPL.setPartnerLinkType(new QName(adapter.getTargetNamespace(),
                    adapter.getPartnerLinkType().getName()));

            partnerLinks.getPartnerLink().add(wsPL);
        }

        // create process client partner link
        PartnerLink processPL = factory.createPartnerLink();
        processPL.setName(BPELConstants.Process.PARTNERLINK);
        processPL.setPartnerLinkType(new QName(process.getTargetNamespace(),
                WSDLConstants.PROCESS_PARTNERLINKTYPE));
        processPL.setMyRole(BPELConstants.Process.MYROLE);
        partnerLinks.getPartnerLink().add(processPL);

        process.setPartnerLinks(partnerLinks);
    }

    private void setProcessAttributes() {
        // sets the targetNamespace and name of the process
        if (dwdl.isSetName()) {
            process.setName(dwdl.getName());
            process.setTargetNamespace(dwdl.getTargetNamespace());
        }
    }

    private void setProcessDocumentation() {
        // sets the documentation part
        if (dwdl.isSetDescription()) {
            Documentation documentation = factory.createDocumentation();
            documentation.getContent().add(dwdl.getDescription());
            process.getDocumentation().add(documentation);
        }
    }

    private void setProcessVariables() {
        Variables variables = factory.createVariables();
        webserviceInputVariables = new HashMap<DecidrWebserviceAdapter, Variable>();
        webserviceOutputVariables = new HashMap<DecidrWebserviceAdapter, Variable>();

        // create global process variables
        Variable processIn = factory.createVariable();
        Variable processOut = factory.createVariable();
        Variable wfmid = factory.createVariable();
        Variable faultMessage = factory.createVariable();
        Variable faultMessageResponse = factory.createVariable();
        Variable successMessage = factory.createVariable();
        Variable successMessageResponse = factory.createVariable();

        // create web service invocation standard variables
        for (DecidrWebserviceAdapter adapter : adapters.values()) {
            Variable inputVariable = factory.createVariable();
            Variable outputVariable = factory.createVariable();
            inputVariable.setName(adapter.getOpertation().getName() + "In");
            inputVariable.setMessageType(adapter.getInputMessageType());
            outputVariable.setName(adapter.getOpertation().getName() + "Out");
            outputVariable.setMessageType(adapter.getOutputMessageType());

            webserviceInputVariables.put(adapter, inputVariable);
            webserviceOutputVariables.put(adapter, outputVariable);
        }

        // setting process start variable
        processIn.setName(BPELConstants.Variables.PROCESS_IN);
        processIn.setMessageType(new QName(process.getTargetNamespace(),
                WSDLConstants.PROCESS_MESSAGE_IN));

        processOut.setName(BPELConstants.Variables.PROCESS_OUT);
        processOut.setMessageType(new QName(process.getTargetNamespace(),
                WSDLConstants.PROCESS_MESSAGE_OUT));

        // create process id variable
        wfmid.setName(BPELConstants.Variables.WFMID);
        wfmid.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "long"));

        // setting fault handler variables
        faultMessage.setName(BPELConstants.Variables.FAULT_IN);
        faultMessage.setMessageType(adapters.get(BPELConstants.Email.NAME)
                .getInputMessageType());
        faultMessageResponse.setName(BPELConstants.Variables.FAULT_OUT);
        faultMessageResponse.setMessageType(adapters.get(
                BPELConstants.Email.NAME).getOutputMessageType());

        // setting success notification variables
        successMessage.setName(BPELConstants.Variables.SUCCESS_IN);
        successMessage.setMessageType(adapters.get(BPELConstants.Email.NAME)
                .getInputMessageType());
        successMessageResponse.setName(BPELConstants.Variables.SUCCESS_IN);
        successMessageResponse.setMessageType(adapters.get(
                BPELConstants.Email.NAME).getOutputMessageType());

        // add variables to process
        variables.getVariable().add(processIn);
        variables.getVariable().add(processOut);
        variables.getVariable().add(faultMessage);
        variables.getVariable().add(faultMessageResponse);
        variables.getVariable().add(successMessage);
        variables.getVariable().add(successMessageResponse);
        process.setVariables(variables);
    }

    private void setSourceAndTargets(BasicNode fromNode, Activity toActivity) {
        if (fromNode.isSetSources()
                && !fromNode.getSources().getSource().isEmpty()) {
            Sources sources = factory.createSources();
            for (Source src : fromNode.getSources().getSource()) {
                de.decidr.model.workflowmodel.bpel.Source source = factory
                        .createSource();
                source.setLinkName("link" + String.valueOf(src.getArcId()));
                sources.getSource().add(source);
            }
            toActivity.setSources(sources);
        }
        if (fromNode.isSetTargets()
                && !fromNode.getTargets().getTarget().isEmpty()) {
            Targets targets = factory.createTargets();
            for (Target trg : fromNode.getTargets().getTarget()) {
                de.decidr.model.workflowmodel.bpel.Target target = factory
                        .createTarget();
                target.setLinkName("link" + String.valueOf(trg.getArcId()));
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
                if (isSimpleType(dwdlVariable.getType())) {
                    bpelVariable.setType(new QName(
                            XMLConstants.W3C_XML_SCHEMA_NS_URI, dwdlVariable
                                    .getType()));
                } else if (isComplexType(dwdlVariable.getType())) {
                    bpelVariable.setType(new QName(
                            Constants.DECIDRPROCESSTYPES_NAMESPACE,
                            dwdlVariable.getType()));
                } else {
                    bpelVariable.setType(new QName(
                            XMLConstants.W3C_XML_SCHEMA_NS_URI, "anyType"));
                }
                process.getVariables().getVariable().add(bpelVariable);
            }
        }
    }

    private void setVariablesFromRoles() {
        if (dwdl.isSetRoles()) {
            if (dwdl.getRoles().isSetRole()) {
                for (Role r : dwdl.getRoles().getRole()) {
                    de.decidr.model.workflowmodel.bpel.Variable role = factory
                            .createVariable();
                    role.setName(r.getName());
                    role.setElement(new QName(Constants.DECIDRTYPES_NAMESPACE,
                            "role"));
                    process.getVariables().getVariable().add(role);
                }
            }
            if (dwdl.getRoles().isSetActor()) {
                for (Actor a : dwdl.getRoles().getActor()) {
                    de.decidr.model.workflowmodel.bpel.Variable actor = factory
                            .createVariable();
                    actor.setName(a.getName());
                    actor.setElement(new QName(Constants.DECIDRTYPES_NAMESPACE,
                            "actor"));
                    process.getVariables().getVariable().add(actor);
                }
            }
        }
    }

}
