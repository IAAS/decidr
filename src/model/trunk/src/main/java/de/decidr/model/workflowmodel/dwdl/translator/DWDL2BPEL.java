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

package de.decidr.model.workflowmodel.dwdl.translator;

import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.apache.log4j.Logger;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.bpel.*;
import de.decidr.model.workflowmodel.bpel.Literal;
import de.decidr.model.workflowmodel.bpel.ObjectFactory;
import de.decidr.model.workflowmodel.bpel.Process;
import de.decidr.model.workflowmodel.dwdl.*;
import de.decidr.model.workflowmodel.dwdl.Workflow;

/**
 * This class converts a given DWDL object and returns the resulting BPEL.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DWDL2BPEL {

    private static Logger log = DefaultLogger.getLogger(DWDL2BPEL.class);

    // global process variables names
    private final String EMAIL_INPUT_VARIABLE = "standardMessageRequest";
    private final String EMAIL_OUTPUT_VARIABLE = "standardMessageResponse";
    private final String HUMANTASK_INPUT_VARIABLE = "createTaskMessageResquest";
    private final String HUMANTASK_OUTPUT_VARIABLE = "createTaskMessageResponse";
    private final String SUCCESS_MESSAGE_REQUEST = "successMessageRequest";
    private final String SUCCESS_MESSAGE_RESPONSE = "successMessageResponse";
    private final String FAULT_MESSAGE_REQUEST = "faultMessageRequest";
    private final String FAULT_MESSAGE_RESPONSE = "faultMessageResponse";
    private final String PROCESS_HUMANTASK_INPUT_VARIABLE = "taskDataMessageRequest";
    private final String PROCESS_HUMANTASK_OUTPUT_VARIABLE = "taskDataMessageRequest";

    // namespace prefixes
    private static final String HUMANTASK_PREFIX = "hWS";
    private static final String EMAIL_PREFIX = "eWS";
    private static final String DECIDRTYPES_PREFIX = "decidr";
    private final String PROCESS_PREFIX = "tns";

    // process message types
    private final String PROCESS_INPUT_VARIABLE = "startConfigurations";
    private final String PROCESS_OPERATION = "startProcess";
    private final String PROCESS_REQUEST_MESSAGE = "startMessage";
    private final String PROCESS_HUMANTASK_REQUEST_MESSAGE = "taskCompletedRequest";
    private final String PROCESS_HUMANTASK_RESPONSE_MESSAGE = "taskCompletedResponse";

    // process partnerlink names and types
    private final String PROCESS_PARTNERLINK = "ProcessPL";
    private final String PROCESS_PARTNERLINKTYPE = "ProcessPLT";

    private Process process = null;
    private Workflow dwdl = null;
    private ObjectFactory factory = null;
    private String tenantName = null;
    private HumanTaskWebservice humanTask = new HumanTaskWebservice();
    private EmailWebservice email = new EmailWebservice();

    private void addCopyStatement(Assign assign, SetProperty property,
            String toVariable) {
        Copy copy = factory.createCopy();
        From from = factory.createFrom();
        To to = factory.createTo();
        if (property.getVariable() == null
                && property.getPropertyValue() != null) {
            Literal literal = factory.createLiteral();
            literal.getContent().addAll(
                    property.getPropertyValue().getContent());
            from.getContent().add(literal);
        } else if (property.getVariable() != null
                && property.getPropertyValue() == null) {
            from.setVariable(property.getVariable());
        }
        to.setVariable(toVariable);
        to.setProperty(new QName(process.getTargetNamespace(), property
                .getName(), PROCESS_PREFIX));
        copy.setFrom(from);
        copy.setTo(to);
        assign.getCopyOrExtensionAssignOperation().add(copy);
    }

    public Process getBPEL(Workflow dwdl, String tenant) {

        this.dwdl = dwdl;
        factory = new ObjectFactory();
        process = factory.createProcess();
        tenantName = tenant;

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

    private TInvoke getEmailActivity(TInvokeNode node) {
        TInvoke emailInvoke = factory.createTInvoke();
        setNameAndDocumentation(node, emailInvoke);
        setSourceAndTargets(node, emailInvoke);
        emailInvoke.setPartnerLink(email.PARTNERLINK);
        emailInvoke.setOperation(email.OPERATION);
        emailInvoke.setInputVariable(EMAIL_INPUT_VARIABLE);
        emailInvoke.setOutputVariable(EMAIL_OUTPUT_VARIABLE);
        return emailInvoke;
    }

    private TSequence getEndActivity(TEndNode node) {
        TSequence sequence = factory.createTSequence();
        TAssign assign = factory.createTAssign();
        TInvoke emailInvoke = factory.createTInvoke();
        setNameAndDocumentation(node, sequence);
        setSourceAndTargets(node, sequence);
        for (TSetProperty property : node.getNotificationOfSuccess()
                .getSetProperty()) {
            addCopyStatement(assign, property, SUCCESS_MESSAGE_REQUEST);
        }
        for (TRecipient recipient : node.getNotificationOfSuccess()
                .getRecipient()) {
            for (TSetProperty property : recipient.getSetProperty()) {
                addCopyStatement(assign, property, SUCCESS_MESSAGE_REQUEST);
            }
        }
        emailInvoke.setPartnerLink(email.PARTNERLINK);
        emailInvoke.setOperation(email.OPERATION);
        emailInvoke.setInputVariable(SUCCESS_MESSAGE_REQUEST);
        emailInvoke.setOutputVariable(SUCCESS_MESSAGE_RESPONSE);
        sequence.getActivity().add(assign);
        sequence.getActivity().add(emailInvoke);
        return sequence;
    }

    private TFlow getFlowActivity(TFlowNode node) {
        TFlow flow = factory.createTFlow();
        TLinks links = factory.createTLinks();
        flow.setLinks(links);
        setNameAndDocumentation(node, flow);
        setSourceAndTargets(node, flow);
        setArcs(node.getArcs().getArc(), flow.getLinks().getLink());
        setActivityNode(flow.getActivity(), node.getNodes().getAllNodes());
        return flow;
    }

    private TForEach getForEachActivity(TForEachNode node) {
        TForEach foreach = factory.createTForEach();
        setNameAndDocumentation(node, foreach);
        setSourceAndTargets(node, foreach);
        if (node.getParallel().equals(TBoolean.YES)) {
            foreach.setParallel(TBoolean.YES);
        } else if (node.getParallel().equals(TBoolean.NO)) {
            foreach.setParallel(TBoolean.NO);
        }
        foreach.setCounterName(node.getCounterName());
        TExpression startExpression = factory.createTExpression();
        startExpression.getContent().add(node.getStartCounterValue());
        foreach.setStartCounterValue(startExpression);
        TExpression endExpression = factory.createTExpression();
        endExpression.getContent().add(node.getFinalCounterValue());
        foreach.setFinalCounterValue(endExpression);
        TCompletionCondition completeConditon = factory
                .createTCompletionCondition();
        TBranches branches = factory.createTBranches();
        branches.setSuccessfulBranchesOnly(TBoolean.YES);
        branches.getContent().add(node.getCompletionCondition());
        completeConditon.setBranches(branches);
        foreach.setCompletionCondition(completeConditon);
        TScope scope = factory.createTScope();
        TFlow flow = factory.createTFlow();
        setActivityNode(flow.getActivity(), node.getNodes().getAllNodes());
        scope.setFlow(flow);
        foreach.setScope(scope);
        return foreach;
    }

    private TInvoke getHumanTaskActivity(TInvokeNode node) {

        return factory.createTInvoke();
    }

    private TIf getIfActivity(TIfNode node) {
        TIf ifActivity = factory.createTIf();
        setNameAndDocumentation(node, ifActivity);
        setDocumentation(node, ifActivity);
        if (!node.getCondition().isEmpty()) {
            for (TCondition dwdlCondition : node.getCondition()) {
                TBooleanExpr bpelCondition = factory.createTBooleanExpr();
                bpelCondition.getContent().add(dwdlCondition.getLeftOperand());
                bpelCondition.getContent().add(dwdlCondition.getOperator());
                bpelCondition.getContent().add(dwdlCondition.getRightOperand());
                TFlow flow = factory.createTFlow();
                TLinks links = factory.createTLinks();
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
                    TActivityContainer activityContainer = factory
                            .createTActivityContainer();
                    activityContainer.setFlow(flow);
                    ifActivity.setElse(activityContainer);
                } else {
                    TElseif elseif = factory.createTElseif();
                    elseif.setCondition(bpelCondition);
                    elseif.setFlow(flow);
                    ifActivity.getElseif().add(elseif);
                }

            }
        }
        return ifActivity;
    }

    private TSequence getInvokeSequence(TInvokeNode node) {
        TInvoke invoke = null;
        TSequence sequence = factory.createTSequence();
        setSourceAndTargets(node, sequence);
        TAssign assign = factory.createTAssign();
        if (node.getActivity().equals("Decidr-" + humanTask.NAME)) {
            for (TSetProperty property : node.getSetProperty()) {
                addCopyStatement(assign, property, EMAIL_INPUT_VARIABLE);
            }
            sequence.getActivity().add(assign);
            invoke = getHumanTaskActivity(node);
        } else if (node.getActivity().equals("Decidr-" + email.NAME)) {
            for (TSetProperty property : node.getSetProperty()) {
                addCopyStatement(assign, property, HUMANTASK_INPUT_VARIABLE);
            }
            invoke = getEmailActivity(node);
        }
        setNameAndDocumentation(node, invoke);
        sequence.getActivity().add(invoke);
        return sequence;
    }

    private TReceive getReceiveActivity(TStartNode node) {
        TReceive receive = factory.createTReceive();
        setNameAndDocumentation(node, receive);
        receive.setPartnerLink(tenantName);
        receive.setOperation(PROCESS_OPERATION);
        receive.setVariable(PROCESS_INPUT_VARIABLE);
        receive.setCreateInstance(TBoolean.YES);
        setSourceAndTargets(node, receive);

        return receive;
    }

    private TAssign initRoles() {
        TAssign assign = null;
        if (dwdl.getRoles() != null && !dwdl.getRoles().getRole().isEmpty()) {
            assign = factory.createTAssign();
            assign.setName("initRoles");
            TCopy copyRole = null;
            for (TRole role : dwdl.getRoles().getRole()) {
                if (!role.getActor().isEmpty()
                        && (role.getConfigurationVariable() == null || role
                                .getConfigurationVariable().value()
                                .equals("no"))) {
                    copyRole = factory.createTCopy();
                    TFrom from = factory.createTFrom();
                    TTo to = factory.createTTo();
                    TLiteral literal = factory.createTLiteral();
                    StringBuffer content = new StringBuffer();
                    content.append("<decidr:role name=" + role.getName() + ">");
                    for (TActor actor : role.getActor()) {
                        StringBuffer actorXML = new StringBuffer();
                        actorXML.append("<decidr:actor");
                        actorXML.append(actor.getName() != null ? " name="
                                + actor.getName() : "");
                        actorXML.append(actor.getUserId() != null ? " userId="
                                + actor.getUserId() : "");
                        actorXML.append(actor.getEmail() != null ? " email="
                                + actor.getEmail() : "");
                        actorXML.append("/>");
                        content.append(actorXML);
                    }
                    content.append("</decidr:role>");
                    literal.getContent().add((content.toString()));
                    from.getContent().add(literal);
                    to.setVariable(role.getName());
                    copyRole.setFrom(from);
                    copyRole.setTo(to);
                    assign.getCopyOrExtensionAssignOperation().add(copyRole);
                }

            }
        }
        if (!dwdl.getRoles().getActor().isEmpty()) {
            TCopy copyActor = null;
            for (TActor actor : dwdl.getRoles().getActor()) {
                if (actor.getConfigurationVariable() == null
                        || actor.getConfigurationVariable().value().equals("no")) {
                    copyActor = factory.createTCopy();
                    TFrom from = factory.createTFrom();
                    TTo to = factory.createTTo();
                    TLiteral literal = factory.createTLiteral();
                    StringBuffer actorXML = new StringBuffer();
                    actorXML.append("<decidr:actor");
                    actorXML.append(actor.getName() != null ? " name="
                            + actor.getName() : "");
                    actorXML.append(actor.getUserId() != null ? " userId="
                            + actor.getUserId() : "");
                    actorXML.append(actor.getEmail() != null ? " email="
                            + actor.getEmail() : "");
                    actorXML.append("/>");
                    literal.getContent().add(actorXML.toString());
                    from.getContent().add(literal);
                    to.setVariable(actor.getName());
                    copyActor.setFrom(from);
                    copyActor.setTo(to);
                    // MA: "assign" might be null if first if's condition was
                    // false
                    assign.getCopyOrExtensionAssignOperation().add(copyActor);
                }
            }
        }
        return assign;
    }

    private TAssign initVariables() {
        TAssign assign = null;
        assign = factory.createTAssign();
        assign.setName("initVariables");
        if (dwdl.getVariables() != null
                && !dwdl.getVariables().getVariable().isEmpty()) {
            TCopy copy = factory.createTCopy();
            for (de.decidr.model.workflowmodel.dwdl.TVariable var : dwdl
                    .getVariables().getVariable()) {
                if (var.getInitialValue() != null) {
                    TFrom from = factory.createTFrom();
                    TTo to = factory.createTTo();
                    TLiteral literal = factory.createTLiteral();
                    log.debug(var.getInitialValue().getContent().get(0));
                    literal.getContent().addAll(
                            var.getInitialValue().getContent());
                    from.getContent().add(literal);
                    to.setVariable(var.getName());
                    copy.setFrom(from);
                    copy.setTo(to);
                    assign.getCopyOrExtensionAssignOperation().add(copy);
                } else if (var.getInitialValues() != null) {
                    TLiteral literal = factory.createTLiteral();
                    for (de.decidr.model.workflowmodel.dwdl.TLiteral initialValue : var
                            .getInitialValues().getInitialValue()) {
                        literal.getContent().addAll(initialValue.getContent());
                    }
                    TFrom from = factory.createTFrom();
                    TTo to = factory.createTTo();
                    from.getContent().add(literal);
                    to.setVariable(var.getName());
                    assign.getCopyOrExtensionAssignOperation().add(copy);
                }
            }
        }
        return assign;
    }

    private void setActivity() {
        TSequence mainSequence = factory.createTSequence();
        mainSequence.setName("mainSequence");
        TAssign initVariables = initVariables();
        TAssign initRoles = initRoles();
        mainSequence.getActivity().add(initVariables);
        mainSequence.getActivity().add(initRoles);
        TFlow mainFlow = factory.createTFlow();
        TLinks links = factory.createTLinks();
        mainFlow.setName("mainFlow");
        mainFlow.setLinks(links);
        setArcs(dwdl.getArcs().getArc(), mainFlow.getLinks().getLink());
        setActivityNode(mainFlow.getActivity(), dwdl.getNodes().getAllNodes());

        mainSequence.getActivity().add(mainFlow);
        process.setSequence(mainSequence);
    }

    private void setActivityNode(List<Object> activityList,
            List<TBasicNode> nodes) {
        for (TBasicNode node : nodes) {
            if (node instanceof TStartNode) {
                TReceive receive = getReceiveActivity((TStartNode) node);
                activityList.add(receive);
            } else if (node instanceof TEndNode) {
                TSequence replySequence = getEndActivity((TEndNode) node);
                activityList.add(replySequence);
            } else if (node instanceof TFlowNode) {
                TFlow flow = getFlowActivity((TFlowNode) node);
                activityList.add(flow);
            } else if (node instanceof TForEachNode) {
                TForEach foreach = getForEachActivity((TForEachNode) node);
                activityList.add(foreach);
            } else if (node instanceof TIfNode) {
                TIf ifActivity = getIfActivity((TIfNode) node);
                activityList.add(ifActivity);
            } else if (node instanceof TInvokeNode) {
                TSequence invokeSequence = getInvokeSequence((TInvokeNode) node);
                activityList.add(invokeSequence);
            }
        }

    }

    private void setArcs(List<TArc> arcs, List<TLink> links) {
        if (arcs != null && !arcs.isEmpty()) {
            for (TArc arc : arcs) {
                TLink link = factory.createTLink();
                link.setName("a" + String.valueOf(arc.getId()));
                links.add(link);
            }
        }
    }

    private void setCorrelationSets() {
        TCorrelationSets correlationSets = factory.createTCorrelationSets();
        process.setCorrelationSets(correlationSets);
        TCorrelationSet correlation = factory.createTCorrelationSet();
        correlation.setName("standard-correlation");
        for (String propertyName : BPELConstants.DWDL_STANDARD_CORRELATION_PROPERTIES) {
            correlation.getProperties().add(
                    new QName(process.getTargetNamespace(), propertyName,
                            PROCESS_PREFIX));
        }
        process.getCorrelationSets().getCorrelationSet().add(correlation);
    }

    private void setDocumentation(TBasicNode fromNode,
            TExtensibleElements toActivity) {
        TDocumentation documentation = factory.createTDocumentation();
        documentation.getContent().addAll(
                fromNode.getDescription().getContent());
        toActivity.getDocumentation().add(documentation);
    }

    private void setFaultHandler() {
        TFaultHandlers faultHandlers = factory.createTFaultHandlers();
        TActivityContainer activityContainer = factory
                .createTActivityContainer();
        TSequence sequence = factory.createTSequence();
        TAssign assign = factory.createTAssign();
        TInvoke emailInvoke = factory.createTInvoke();
        if (dwdl.getFaultHandler() != null
                && !dwdl.getFaultHandler().getSetProperty().isEmpty()) {
            for (TSetProperty property : dwdl.getFaultHandler()
                    .getSetProperty()) {
                addCopyStatement(assign, property, FAULT_MESSAGE_REQUEST);
            }
            if (!dwdl.getFaultHandler().getRecipient().isEmpty()) {
                for (TRecipient recipient : dwdl.getFaultHandler()
                        .getRecipient()) {
                    for (TSetProperty property : recipient.getSetProperty()) {
                        addCopyStatement(assign, property,
                                FAULT_MESSAGE_REQUEST);
                    }
                }
            }
            emailInvoke.setPartnerLink(email.PARTNERLINK);
            emailInvoke.setOperation(email.OPERATION);
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
        TImport htwsImport = factory.createTImport();
        TImport ewsImport = factory.createTImport();
        TImport dtImport = factory.createTImport();
        TImport odeImport = factory.createTImport();
        TImport pwsdlImport = factory.createTImport();

        // setting import for HumanTaskWS
        htwsImport.setNamespace(humanTask.NAMESPACE);
        htwsImport.setImportType(BPELConstants.WSDL_IMPORTTYPE);
        htwsImport.setLocation(humanTask.LOCATION);

        // setting import for EmailWS
        ewsImport.setNamespace(email.NAMESPACE);
        ewsImport.setImportType(BPELConstants.WSDL_IMPORTTYPE);
        ewsImport.setLocation(email.LOCATION);

        // setting import for DecidrTypes
        dtImport.setNamespace(BPELConstants.DECIDRTYPES_NAMESPACE);
        dtImport.setImportType(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        dtImport.setLocation(BPELConstants.DECIDRTYPES_LOCATION);

        // setting import for Ode extension
        odeImport.setNamespace(BPELConstants.ODE_NAMESPACE);
        odeImport.setImportType(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // setting import for process wsdl
        pwsdlImport.setNamespace(process.getTargetNamespace());
        pwsdlImport.setImportType(BPELConstants.WSDL_IMPORTTYPE);
        // replace all space with underscore
        pwsdlImport.setLocation(process.getName().replace(" ", "_") + ".wsdl");

        // add imports to process
        process.getImport().add(odeImport);
        process.getImport().add(htwsImport);
        process.getImport().add(ewsImport);
        process.getImport().add(dtImport);
        process.getImport().add(pwsdlImport);
    }

    private void setNameAndDocumentation(TBasicNode fromNode, TActivity toNode) {
        if (fromNode.getName() != null) {
            toNode.setName(fromNode.getName());
        }
        if (fromNode.getDescription() != null) {
            setDocumentation(fromNode, toNode);
        }
    }

    private void setPartnerLinks() {
        TPartnerLinks pls = factory.createTPartnerLinks();
        process.setPartnerLinks(pls);

        // create HumanTaskWS partnerlink
        TPartnerLink htwsPL = factory.createTPartnerLink();
        htwsPL.setName(humanTask.PARTNERLINK);
        htwsPL.setPartnerLinkType(new QName(humanTask.NAMESPACE,
                humanTask.PARTNERLINKTYPE, HUMANTASK_PREFIX));
        htwsPL.setPartnerRole(BPELConstants.HTWS_PARTNERROLE);
        htwsPL.setMyRole(BPELConstants.HTWS_MYROLE);

        // create EmailWS partnerlink
        TPartnerLink ewsPL = factory.createTPartnerLink();
        ewsPL.setName(email.PARTNERLINK);
        ewsPL.setPartnerLinkType(new QName(email.PARTNERLINKTYPE,
                email.PARTNERLINKTYPE, EMAIL_PREFIX));
        ewsPL.setPartnerRole(BPELConstants.EWS_PARTNERROLE);

        // create process client partnerlink
        TPartnerLink processPL = factory.createTPartnerLink();
        processPL.setName(PROCESS_PARTNERLINK);
        processPL.setPartnerLinkType(new QName(process.getTargetNamespace(),
                PROCESS_PARTNERLINKTYPE, PROCESS_PREFIX));
        processPL.setMyRole("ProcessProvider");

        // add partnerlinks to process
        process.getPartnerLinks().getPartnerLink().add(htwsPL);
        process.getPartnerLinks().getPartnerLink().add(ewsPL);
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
        TDocumentation documentation = null;
        if (dwdl.getDescription() != null) {
            documentation = factory.createTDocumentation();
            documentation.getContent().addAll(
                    dwdl.getDescription().getContent());
            process.getDocumentation().add(documentation);
        }
    }

    private void setProcessVariables() {
        TVariables variables = factory.createTVariables();

        // create global process variables
        TVariable startConfigurations = factory.createTVariable();
        TVariable wfmid = factory.createTVariable();
        TVariable faultMessage = factory.createTVariable();
        TVariable faultMessageResponse = factory.createTVariable();
        TVariable successMessage = factory.createTVariable();
        TVariable successMessageResponse = factory.createTVariable();
        TVariable standardEmailMessage = factory.createTVariable();
        TVariable standardEmailMessageResponse = factory.createTVariable();
        TVariable taskMessage = factory.createTVariable();
        TVariable taskMessageResponse = factory.createTVariable();
        TVariable taskDataMessage = factory.createTVariable();

        // setting process start variable
        startConfigurations.setName(PROCESS_INPUT_VARIABLE);
        startConfigurations
                .setMessageType(new QName(process.getTargetNamespace(),
                        PROCESS_REQUEST_MESSAGE, PROCESS_PREFIX));
        // setting process id variable
        wfmid.setName("wfmid");
        wfmid
                .setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "ID",
                        "xsd"));
        // setting fault handler variables
        faultMessage.setName(FAULT_MESSAGE_REQUEST);
        faultMessage.setMessageType(new QName(email.NAMESPACE,
                email.REQUEST_MESSAGE, HUMANTASK_PREFIX));
        faultMessageResponse.setName(FAULT_MESSAGE_RESPONSE);
        faultMessageResponse.setMessageType(new QName(email.NAMESPACE,
                email.RESPONSE_MESSAGE, EMAIL_PREFIX));
        // setting success notification variables
        successMessage.setName(SUCCESS_MESSAGE_REQUEST);
        successMessage.setMessageType(new QName(email.NAMESPACE,
                email.REQUEST_MESSAGE, EMAIL_PREFIX));
        successMessageResponse.setName(SUCCESS_MESSAGE_RESPONSE);
        successMessageResponse.setMessageType(new QName(email.NAMESPACE,
                email.RESPONSE_MESSAGE, EMAIL_PREFIX));
        // setting standard email web service invocation variables
        standardEmailMessage.setName(EMAIL_INPUT_VARIABLE);
        standardEmailMessage.setMessageType(new QName(email.NAMESPACE,
                email.REQUEST_MESSAGE, EMAIL_PREFIX));
        standardEmailMessageResponse.setName(EMAIL_OUTPUT_VARIABLE);
        standardEmailMessageResponse.setMessageType(new QName(email.NAMESPACE,
                email.RESPONSE_MESSAGE, EMAIL_PREFIX));
        // setting standard humantask web service invocation variables
        taskMessage.setName(HUMANTASK_INPUT_VARIABLE);
        taskMessage.setMessageType(new QName(humanTask.NAMESPACE,
                humanTask.REQUEST_MESSAGE, HUMANTASK_PREFIX));
        taskMessageResponse.setName(HUMANTASK_OUTPUT_VARIABLE);
        taskMessageResponse.setMessageType(new QName(humanTask.NAMESPACE,
                humanTask.RESPONSE_MESSAGE, HUMANTASK_PREFIX));
        // setting process human task data receiving variables
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
        process.getVariables().getVariable().add(taskMessage);
        process.getVariables().getVariable().add(taskMessageResponse);
        process.getVariables().getVariable().add(taskDataMessage);
    }

    private void setSourceAndTargets(TBasicNode fromNode, TActivity toActivity) {
        if (fromNode.getSources() != null
                && !fromNode.getSources().getSource().isEmpty()) {
            TSources sources = factory.createTSources();
            for (TSource src : fromNode.getSources().getSource()) {
                de.decidr.model.workflowmodel.bpel.TSource source = factory
                        .createTSource();
                source.setLinkName("a" + String.valueOf(src.getArcID()));
                sources.getSource().add(source);
            }
            toActivity.setSources(sources);
        }
        if (fromNode.getTargets() != null
                && !fromNode.getTargets().getTarget().isEmpty()) {
            TTargets targets = factory.createTTargets();
            for (TTarget trg : fromNode.getTargets().getTarget()) {
                de.decidr.model.workflowmodel.bpel.TTarget target = factory
                        .createTTarget();
                target.setLinkName("a" + String.valueOf(trg.getArcID()));
                targets.getTarget().add(target);
            }
            toActivity.setTargets(targets);
        }
    }

    /**
     * This function creates and sets variables defined by the dwdl workflow
     */
    private void setVariables() {
        if (dwdl.getVariables() != null
                && !dwdl.getVariables().getVariable().isEmpty()) {
            for (de.decidr.model.workflowmodel.dwdl.TVariable v : dwdl
                    .getVariables().getVariable()) {
                de.decidr.model.workflowmodel.bpel.TVariable var = factory
                        .createTVariable();
                var.setName(v.getName());
                if (BPELConstants.DWDL_BASIC_TYPES.contains(v.getType())) {
                    var.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, v
                            .getType(), "xsd"));
                } else if (BPELConstants.DWDL_COMPLEX_TYPES.contains(v
                        .getType())) {
                    if (v.getType().equals("form")) {
                        var.setType(new QName(
                                BPELConstants.DECIDRTYPES_NAMESPACE,
                                "tItemList", DECIDRTYPES_PREFIX));
                    } else {
                        var.setType(new QName(
                                BPELConstants.DECIDRTYPES_NAMESPACE, v
                                        .getType(), DECIDRTYPES_PREFIX));
                    }
                } else {
                    var.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI,
                            "anyType", "xsd"));
                }
                process.getVariables().getVariable().add(var);
            }
        }
    }

    private void setVariablesFromRoles() {
        if (dwdl.getRoles() != null) {
            if (!dwdl.getRoles().getRole().isEmpty()) {
                for (TRole r : dwdl.getRoles().getRole()) {
                    de.decidr.model.workflowmodel.bpel.TVariable role = factory
                            .createTVariable();
                    role.setName(r.getName());
                    role.setElement(new QName(
                            BPELConstants.DECIDRTYPES_NAMESPACE, "role",
                            DECIDRTYPES_PREFIX));
                    process.getVariables().getVariable().add(role);
                }
            }
            if (!dwdl.getRoles().getActor().isEmpty()) {
                for (TActor a : dwdl.getRoles().getActor()) {
                    de.decidr.model.workflowmodel.bpel.TVariable actor = factory
                            .createTVariable();
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
