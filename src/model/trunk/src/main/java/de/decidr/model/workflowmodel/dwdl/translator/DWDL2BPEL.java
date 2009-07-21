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

import javax.wsdl.Definition;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.apache.log4j.Logger;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.workflowmodel.bpel.*;
import de.decidr.model.workflowmodel.dwdl.TActor;
import de.decidr.model.workflowmodel.dwdl.TArc;
import de.decidr.model.workflowmodel.dwdl.TBasicNode;
import de.decidr.model.workflowmodel.dwdl.TEndNode;
import de.decidr.model.workflowmodel.dwdl.TFlowNode;
import de.decidr.model.workflowmodel.dwdl.TForEachNode;
import de.decidr.model.workflowmodel.dwdl.TIfNode;
import de.decidr.model.workflowmodel.dwdl.TInvokeNode;
import de.decidr.model.workflowmodel.dwdl.TRecipient;
import de.decidr.model.workflowmodel.dwdl.TRole;
import de.decidr.model.workflowmodel.dwdl.TSetProperty;
import de.decidr.model.workflowmodel.dwdl.TStartNode;
import de.decidr.model.workflowmodel.dwdl.TWorkflow;

/**
 * This class converts a given DWDL object and returns the resulting BPEL.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DWDL2BPEL {

    private static Logger log = DefaultLogger.getLogger(DWDL2BPEL.class);

    private TProcess process = null;
    private TWorkflow dwdl = null;
    private Definition emailDefinition = null;
    private Definition humanTaskDefinition = null;
    private ObjectFactory factory = null;

    private void addCopyStatement(TAssign assign, TSetProperty property,
            String toVariable) {
        TCopy copy = factory.createTCopy();
        TFrom from = factory.createTFrom();
        TTo to = factory.createTTo();
        from.setVariable(property.getVariable());
        to.setVariable(toVariable);
        to.setProperty(new QName(process.getTargetNamespace(), property
                .getName(), "tns"));
        copy.setFrom(from);
        copy.setTo(to);
        assign.getCopyOrExtensionAssignOperation().add(copy);
    }

    private void addCopyValueStatement(TAssign assign, TSetProperty property,
            String toVariable) {
        TCopy copy = factory.createTCopy();
        TFrom from = factory.createTFrom();
        TTo to = factory.createTTo();
        TLiteral literal = factory.createTLiteral();
        literal.getContent().addAll(property.getPropertyValue().getContent());
        from.getContent().add(literal);
        to.setVariable(toVariable);
        to.setProperty(new QName(process.getTargetNamespace(), property
                .getName(), "tns"));
        copy.setFrom(from);
        copy.setTo(to);
        assign.getCopyOrExtensionAssignOperation().add(copy);
    }

    public TProcess getBPEL(TWorkflow dwdl, List<Definition> webservices) {

        this.dwdl = dwdl;
        factory = new ObjectFactory();
        process = factory.createTProcess();
        
        //sets the email and human task definition
        //loadDefinitions(webservices);
        
        log.trace("setting process attributes");
        setProcessAttributes();
        setDocumentation();
        setImports();
        setPartnerLinks();
        setProcessVariables();
        setVariables();
        setVariablesFromRoles();
        setCorrelationSets();
        setFaultHandler();
        setActivity();
        return process;
    }

    private void loadDefinitions(List<Definition> webservices) {
        assert(webservices!=null);
          for (Definition def : webservices){
              if (def.getQName().getLocalPart().equalsIgnoreCase(BPELConstants.EWS_NAME)){
                  emailDefinition = def;
              } else if (def.getQName().getLocalPart().equalsIgnoreCase(BPELConstants.HTWS_NAME)){
                  humanTaskDefinition = def;
              }
          }
    }

    private TFlow getFlowActivity(TFlowNode node) {

        return null;
    }

    private TForEach getForEachActivity(TForEachNode node) {

        return null;
    }

    private TIf getIfActivity(TIfNode node) {
        return null;
    }

    private TInvoke getInvokeActivity(TInvokeNode node) {

        return null;
    }

    private TReceive getReceiveActivity(TStartNode node) {

        return null;
    }

    private TReply getReplyActivity(TEndNode node) {

        return null;
    }

    private TAssign initRoles() {
        TAssign assign = null;
        if (dwdl.getRoles() != null) {
            assign = factory.createTAssign();
            assign.setName("initRoles");
            TCopy copyRole = null;
            if (!dwdl.getRoles().getRole().isEmpty()) {
                for (TRole role : dwdl.getRoles().getRole()) {
                    if (!role.getActor().isEmpty()
                            && (role.getConfigurationVariable() == null || role
                                    .getConfigurationVariable().value()
                                    .equalsIgnoreCase("no"))) {
                        copyRole = factory.createTCopy();
                        TFrom from = factory.createTFrom();
                        TTo to = factory.createTTo();
                        TLiteral literal = factory.createTLiteral();
                        StringBuffer content = new StringBuffer();
                        content.append("<decidr:role name=" + role.getName()
                                + ">");
                        for (TActor actor : role.getActor()) {
                            StringBuffer actorXML = new StringBuffer();
                            actorXML.append("<decidr:actor");
                            actorXML.append(actor.getName() != null ? " name="
                                    + actor.getName() : "");
                            actorXML
                                    .append(actor.getUserId() != null ? " userId="
                                            + actor.getUserId()
                                            : "");
                            actorXML
                                    .append(actor.getEmail() != null ? " email="
                                            + actor.getEmail()
                                            : "");
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
                            || actor.getConfigurationVariable().value()
                                    .equalsIgnoreCase("no")) {
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
                        assign.getCopyOrExtensionAssignOperation().add(
                                copyActor);
                    }
                }
            }
        }
        return assign;
    }

    private TAssign initVariables() {
        TAssign assign = null;
        if (dwdl.getVariables() != null) {
            assign = factory.createTAssign();
            assign.setName("initVariables");
            if (!dwdl.getVariables().getVariable().isEmpty()) {
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
                            literal.getContent().addAll(
                                    initialValue.getContent());
                        }
                        TFrom from = factory.createTFrom();
                        TTo to = factory.createTTo();
                        from.getContent().add(literal);
                        to.setVariable(var.getName());
                        assign.getCopyOrExtensionAssignOperation().add(copy);
                    }
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
        mainFlow.setLinks(links);
        for (TArc arc : dwdl.getArcs().getArc()) {
            TLink link = factory.createTLink();
            link.setName(arc.getSourceNode() + "-to-" + arc.getTargetNode());
            mainFlow.getLinks().getLink().add(link);
        }
        for (TBasicNode node : dwdl.getNodes().getAllNodes()) {
            if (node instanceof TStartNode) {
                TReceive receive = getReceiveActivity((TStartNode) node);
                mainFlow.getActivity().add(receive);
            } else if (node instanceof TEndNode) {
                TReply reply = getReplyActivity((TEndNode) node);
                mainFlow.getActivity().add(reply);
            } else if (node instanceof TFlowNode) {
                TFlow flow = getFlowActivity((TFlowNode) node);
                mainFlow.getActivity().add(flow);
            } else if (node instanceof TForEachNode) {
                TForEach foreach = getForEachActivity((TForEachNode) node);
                mainFlow.getActivity().add(foreach);
            } else if (node instanceof TIfNode) {
                TIf ifActivity = getIfActivity((TIfNode) node);
                mainFlow.getActivity().add(ifActivity);
            } else if (node instanceof TInvokeNode) {
                TInvoke invoke = getInvokeActivity((TInvokeNode) node);
                mainFlow.getActivity().add(invoke);
            }
        }
        mainSequence.getActivity().add(mainFlow);
        process.setSequence(mainSequence);
    }

    private void setCorrelationSets() {
        TCorrelationSets correlationSets = factory.createTCorrelationSets();
        process.setCorrelationSets(correlationSets);
        TCorrelationSet correlation = factory.createTCorrelationSet();
        correlation.setName("standard-correlation");
        correlation.getProperties().add(
                new QName(process.getTargetNamespace(), "processID", "tns"));
        correlation.getProperties().add(
                new QName(process.getTargetNamespace(), "userID", "tns"));
        correlation.getProperties().add(
                new QName(process.getTargetNamespace(), "taskID", "tns"));
        process.getCorrelationSets().getCorrelationSet().add(correlation);
    }

    private void setDocumentation() {
        TDocumentation documentation = null;
        if (dwdl.getDescription() != null) {
            documentation = factory.createTDocumentation();
            documentation.getContent().addAll(
                    dwdl.getDescription().getContent());
            process.getDocumentation().add(documentation);
        }
    }

    private void setFaultHandler() {
        TFaultHandlers faultHandlers = factory.createTFaultHandlers();
        TActivityContainer activityContainer = factory
                .createTActivityContainer();
        TSequence sequence = factory.createTSequence();
        TAssign assign = factory.createTAssign();
        TInvoke emailInvoke = factory.createTInvoke();
        if (dwdl.getFaultHandler() != null) {
            if (!dwdl.getFaultHandler().getSetProperty().isEmpty()) {
                for (TSetProperty property : dwdl.getFaultHandler()
                        .getSetProperty()) {
                    if (property.getVariable() == null
                            && property.getPropertyValue() != null) {
                        addCopyValueStatement(assign, property, "faultMessageRequest");
                    } else if (property.getVariable() != null
                            && property.getPropertyValue() == null) {
                        addCopyStatement(assign, property, "faultMessageRequest");
                    }
                }
            }
            if (!dwdl.getFaultHandler().getRecipient().isEmpty()) {

                for (TRecipient recipient : dwdl.getFaultHandler()
                        .getRecipient()) {
                    for (TSetProperty property : recipient.getSetProperty()) {
                        if (property.getVariable() == null
                                && property.getPropertyValue() != null) {
                            addCopyValueStatement(assign, property,
                                    "faultMessageRequest");
                        } else if (property.getVariable() != null
                                && property.getPropertyValue() == null) {
                            addCopyStatement(assign, property, "faultMessageRequest");
                        }
                    }
                }
            }
            emailInvoke.setPartnerLink(BPELConstants.EWS_PARTNERLINK);
            emailInvoke.setOperation("sendEmail");
            emailInvoke.setInputVariable("faultMessageRequest");
            emailInvoke.setOutputVariable("faultMessageResponse");
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
        htwsImport.setNamespace(BPELConstants.HTWS_NAMESPACE);
        htwsImport.setImportType(BPELConstants.WSDL_IMPORTTYPE);
        htwsImport.setLocation(BPELConstants.HTWS_LOCATION);

        // setting import for EmailWS
        ewsImport.setNamespace(BPELConstants.EWS_NAMESPACE);
        ewsImport.setImportType(BPELConstants.WSDL_IMPORTTYPE);
        ewsImport.setLocation(BPELConstants.EWS_LOCATION);

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

    private void setPartnerLinks() {
        TPartnerLinks pls = factory.createTPartnerLinks();
        process.setPartnerLinks(pls);

        // create HumanTaskWS partnerlink
        TPartnerLink htwsPL = factory.createTPartnerLink();
        htwsPL.setName(BPELConstants.HTWS_PARTNERLINK);
        htwsPL.setPartnerLinkType(new QName(BPELConstants.HTWS_NAMESPACE,
                BPELConstants.HTWS_PARTNERLINKTYPE, "hWS"));
        htwsPL.setPartnerRole(BPELConstants.HTWS_PARTNERROLE);
        htwsPL.setMyRole(BPELConstants.HTWS_MYROLE);

        // create EmailWS partnerlink
        TPartnerLink ewsPL = factory.createTPartnerLink();
        ewsPL.setName(BPELConstants.EWS_PARTNERLINK);
        ewsPL.setPartnerLinkType(new QName(BPELConstants.EWS_PARTNERLINKTYPE,
                BPELConstants.EWS_PARTNERLINKTYPE, "eWS"));
        ewsPL.setPartnerRole(BPELConstants.EWS_PARTNERROLE);

        // create process client partnerlink
        TPartnerLink processPL = factory.createTPartnerLink();
        processPL.setName("Client");
        processPL.setPartnerLinkType(new QName(process.getTargetNamespace(),
                "ProcessPLT", "tns"));
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

        // add id attribute
        process.getOtherAttributes().put(
                new QName(BPELConstants.DECIDRTYPES_NAMESPACE, "id", "decidr"),
                dwdl.getId());
    }

    private void setProcessVariables() {
        // instantiation of variables object
        TVariables variables = factory.createTVariables();

        TVariable startConfigurations = factory.createTVariable();
        TVariable wfmid = factory.createTVariable();
        TVariable faultMessage = factory.createTVariable();
        TVariable faultMessageResponse = factory.createTVariable();
        TVariable successMessage = factory.createTVariable();
        TVariable successMessageResponse = factory.createTVariable();
        TVariable taskMessage = factory.createTVariable();
        TVariable taskMessageResponse = factory.createTVariable();
        TVariable taskDataMessage = factory.createTVariable();

        // creates global process variables
        startConfigurations.setName("startConfigurations");
        startConfigurations.setMessageType(new QName(process
                .getTargetNamespace(), "startMessage", "tns"));
        wfmid.setName("wfmid");
        wfmid
                .setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "ID",
                        "xsd"));
        faultMessage.setName("faultMessageRequest");
        faultMessage.setMessageType(new QName(BPELConstants.EWS_NAMESPACE,
                "sendEmailRequest", "eWS"));
        faultMessageResponse.setName("faultMessageResponse");
        faultMessageResponse.setMessageType(new QName(
                BPELConstants.EWS_NAMESPACE, "sendEmailResponse", "eWS"));
        successMessage.setName("successMessageRequest");
        successMessage.setMessageType(new QName(BPELConstants.EWS_NAMESPACE,
                "sendEmailRequest", "eWS"));
        successMessageResponse.setName("successMessageResponse");
        successMessageResponse.setMessageType(new QName(
                BPELConstants.EWS_NAMESPACE, "sendEmailResponse", "eWS"));
        taskMessage.setName("createTaskMessageRequest");
        taskMessage.setMessageType(new QName(BPELConstants.HTWS_NAMESPACE,
                "createTaskRequest", "hWS"));
        taskMessageResponse.setName("createTaskMessageResponse");
        taskMessageResponse.setMessageType(new QName(
                BPELConstants.HTWS_NAMESPACE, "createTaskResponse", "hWS"));
        taskDataMessage.setName("taskDataMessageRequest");
        taskDataMessage.setMessageType(new QName(BPELConstants.HTWS_NAMESPACE,
                "taskDataRequest", "hWS"));

        // set variables
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

    private void setVariables() {
        if (dwdl.getVariables() != null) {
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
                    var.setType(new QName(BPELConstants.DECIDRTYPES_NAMESPACE,
                            v.getType(), "decidr"));
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
                            "decidr"));
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
                            "decidr"));
                    process.getVariables().getVariable().add(actor);
                }
            }
        }
    }

}
