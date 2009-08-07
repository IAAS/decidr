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
import de.decidr.model.workflowmodel.bpel.Sources;
import de.decidr.model.workflowmodel.bpel.Targets;
import de.decidr.model.workflowmodel.bpel.Variable;
import de.decidr.model.workflowmodel.bpel.Variables;
import de.decidr.model.workflowmodel.dwdl.*;
import de.decidr.model.workflowmodel.dwdl.Boolean;
import de.decidr.model.workflowmodel.dwdl.Condition;
import de.decidr.model.workflowmodel.dwdl.Role;
import de.decidr.model.workflowmodel.dwdl.Source;
import de.decidr.model.workflowmodel.dwdl.Target;

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

    private Invoke getEmailActivity(InvokeNode node) {
        Invoke emailInvoke = factory.createInvoke();
        setNameAndDocumentation(node, emailInvoke);
        setSourceAndTargets(node, emailInvoke);
        emailInvoke.setPartnerLink(email.PARTNERLINK);
        emailInvoke.setOperation(email.OPERATION);
        emailInvoke.setInputVariable(EMAIL_INPUT_VARIABLE);
        emailInvoke.setOutputVariable(EMAIL_OUTPUT_VARIABLE);
        return emailInvoke;
    }

    private Sequence getEndActivity(EndNode node) {
        Sequence sequence = factory.createSequence();
        Assign assign = factory.createAssign();
        Invoke emailInvoke = factory.createInvoke();
        setNameAndDocumentation(node, sequence);
        setSourceAndTargets(node, sequence);
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
        emailInvoke.setPartnerLink(email.PARTNERLINK);
        emailInvoke.setOperation(email.OPERATION);
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
        branches.setSuccessfulBranchesOnly(de.decidr.model.workflowmodel.bpel.Boolean.YES);
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

    private Invoke getHumanTaskActivity(InvokeNode node) {

        return factory.createInvoke();
    }

    private If getIfActivity(IfNode node) {
        If ifActivity = factory.createIf();
        setNameAndDocumentation(node, ifActivity);
        setDocumentation(node, ifActivity);
        if (!node.getCondition().isEmpty()) {
            for (Condition dwdlCondition : node.getCondition()) {
                BooleanExpression bpelCondition = factory.createBooleanExpression();
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
        Invoke invoke = null;
        Sequence sequence = factory.createSequence();
        setSourceAndTargets(node, sequence);
        Assign assign = factory.createAssign();
        if (node.getActivity().equals("Decidr-" + humanTask.NAME)) {
            for (SetProperty property : node.getSetProperty()) {
                addCopyStatement(assign, property, EMAIL_INPUT_VARIABLE);
            }
            sequence.getActivity().add(assign);
            invoke = getHumanTaskActivity(node);
        } else if (node.getActivity().equals("Decidr-" + email.NAME)) {
            for (SetProperty property : node.getSetProperty()) {
                addCopyStatement(assign, property, HUMANTASK_INPUT_VARIABLE);
            }
            invoke = getEmailActivity(node);
        }
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
        receive.setCreateInstance(de.decidr.model.workflowmodel.bpel.Boolean.YES);
        setSourceAndTargets(node, receive);

        return receive;
    }

    private Assign initRoles() {
        Assign assign = null;
        if (dwdl.getRoles() != null && !dwdl.getRoles().getRole().isEmpty()) {
            assign = factory.createAssign();
            assign.setName("initRoles");
            Copy copyRole = null;
            for (Role role : dwdl.getRoles().getRole()) {
                if (!role.getActor().isEmpty()
                        && (role.getConfigurationVariable() == null || role
                                .getConfigurationVariable().value()
                                .equals("no"))) {
                    copyRole = factory.createCopy();
                    From from = factory.createFrom();
                    To to = factory.createTo();
                    Literal literal = factory.createLiteral();
                    StringBuffer content = new StringBuffer();
                    content.append("<decidr:role name=" + role.getName() + ">");
                    for (Actor actor : role.getActor()) {
                        StringBuffer actorXML = new StringBuffer();
                        actorXML.append("<decidr:actor");
                        actorXML.append(actor.getName() != null ? " name="
                                + actor.getName() : "");
                        actorXML.append((Long)actor.getUserId() != null ? " userId="
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
            Copy copyActor = null;
            for (Actor actor : dwdl.getRoles().getActor()) {
                if (actor.getConfigurationVariable() == null
                        || actor.getConfigurationVariable().value().equals("no")) {
                    copyActor = factory.createCopy();
                    From from = factory.createFrom();
                    To to = factory.createTo();
                    Literal literal = factory.createLiteral();
                    StringBuffer actorXML = new StringBuffer();
                    actorXML.append("<decidr:actor");
                    actorXML.append(actor.getName() != null ? " name="
                            + actor.getName() : "");
                    actorXML.append((Long)actor.getUserId() != null ? " userId="
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

    private Assign initVariables() {
        Assign assign = null;
        assign = factory.createAssign();
        assign.setName("initVariables");
        if (dwdl.getVariables() != null
                && !dwdl.getVariables().getVariable().isEmpty()) {
            Copy copy = factory.createCopy();
            for (de.decidr.model.workflowmodel.dwdl.Variable var : dwdl
                    .getVariables().getVariable()) {
                if (var.getInitialValue() != null) {
                    From from = factory.createFrom();
                    To to = factory.createTo();
                    Literal literal = factory.createLiteral();
                    log.debug(var.getInitialValue().getContent().get(0));
                    literal.getContent().addAll(
                            var.getInitialValue().getContent());
                    from.getContent().add(literal);
                    to.setVariable(var.getName());
                    copy.setFrom(from);
                    copy.setTo(to);
                    assign.getCopyOrExtensionAssignOperation().add(copy);
                } else if (var.getInitialValues() != null) {
                    Literal literal = factory.createLiteral();
                    for (de.decidr.model.workflowmodel.dwdl.Literal initialValue : var
                            .getInitialValues().getInitialValue()) {
                        literal.getContent().addAll(initialValue.getContent());
                    }
                    From from = factory.createFrom();
                    To to = factory.createTo();
                    from.getContent().add(literal);
                    to.setVariable(var.getName());
                    assign.getCopyOrExtensionAssignOperation().add(copy);
                }
            }
        }
        return assign;
    }

    private void setActivity() {
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
        for (String propertyName : BPELConstants.DWDL_STANDARD_CORRELATION_PROPERTIES) {
            correlation.getProperties().add(
                    new QName(process.getTargetNamespace(), propertyName,
                            PROCESS_PREFIX));
        }
        process.getCorrelationSets().getCorrelationSet().add(correlation);
    }

    private void setDocumentation(BasicNode fromNode,
            ExtensibleElements toActivity) {
        Documentation documentation = factory.createDocumentation();
        documentation.getContent().add(
                fromNode.getDescription());
        toActivity.getDocumentation().add(documentation);
    }

    private void setFaultHandler() {
        FaultHandlers faultHandlers = factory.createFaultHandlers();
        ActivityContainer activityContainer = factory
                .createActivityContainer();
        Sequence sequence = factory.createSequence();
        Assign assign = factory.createAssign();
        Invoke emailInvoke = factory.createInvoke();
        if (dwdl.getFaultHandler() != null
                && !dwdl.getFaultHandler().getSetProperty().isEmpty()) {
            for (SetProperty property : dwdl.getFaultHandler()
                    .getSetProperty()) {
                addCopyStatement(assign, property, FAULT_MESSAGE_REQUEST);
            }
            if (!dwdl.getFaultHandler().getRecipient().isEmpty()) {
                for (Recipient recipient : dwdl.getFaultHandler()
                        .getRecipient()) {
                    for (SetProperty property : recipient.getSetProperty()) {
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
        Import htwsImport = factory.createImport();
        Import ewsImport = factory.createImport();
        Import dtImport = factory.createImport();
        Import odeImport = factory.createImport();
        Import pwsdlImport = factory.createImport();

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

        // create HumanTaskWS partnerlink
        PartnerLink htwsPL = factory.createPartnerLink();
        htwsPL.setName(humanTask.PARTNERLINK);
        htwsPL.setPartnerLinkType(new QName(humanTask.NAMESPACE,
                humanTask.PARTNERLINKTYPE, HUMANTASK_PREFIX));
        htwsPL.setPartnerRole(BPELConstants.HTWS_PARTNERROLE);
        htwsPL.setMyRole(BPELConstants.HTWS_MYROLE);

        // create EmailWS partnerlink
        PartnerLink ewsPL = factory.createPartnerLink();
        ewsPL.setName(email.PARTNERLINK);
        ewsPL.setPartnerLinkType(new QName(email.PARTNERLINKTYPE,
                email.PARTNERLINKTYPE, EMAIL_PREFIX));
        ewsPL.setPartnerRole(BPELConstants.EWS_PARTNERROLE);

        // create process client partnerlink
        PartnerLink processPL = factory.createPartnerLink();
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
        Documentation documentation = null;
        if (dwdl.getDescription() != null) {
            documentation = factory.createDocumentation();
            documentation.getContent().add(
                    dwdl.getDescription());
            process.getDocumentation().add(documentation);
        }
    }

    private void setProcessVariables() {
        Variables variables = factory.createVariables();

        // create global process variables
        Variable startConfigurations = factory.createVariable();
        Variable wfmid = factory.createVariable();
        Variable faultMessage = factory.createVariable();
        Variable faultMessageResponse = factory.createVariable();
        Variable successMessage = factory.createVariable();
        Variable successMessageResponse = factory.createVariable();
        Variable standardEmailMessage = factory.createVariable();
        Variable standardEmailMessageResponse = factory.createVariable();
        Variable taskMessage = factory.createVariable();
        Variable taskMessageResponse = factory.createVariable();
        Variable taskDataMessage = factory.createVariable();

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

    /**
     * This function creates and sets variables defined by the dwdl workflow
     */
    private void setVariables() {
        if (dwdl.getVariables() != null
                && !dwdl.getVariables().getVariable().isEmpty()) {
            for (de.decidr.model.workflowmodel.dwdl.Variable v : dwdl
                    .getVariables().getVariable()) {
                de.decidr.model.workflowmodel.bpel.Variable var = factory
                        .createVariable();
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
            if (!dwdl.getRoles().getActor().isEmpty()) {
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
