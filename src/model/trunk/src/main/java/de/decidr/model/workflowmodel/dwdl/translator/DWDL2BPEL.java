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

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import de.decidr.model.logging.DefaultLogger;

import de.decidr.model.workflowmodel.bpel.ObjectFactory;
import de.decidr.model.workflowmodel.bpel.TActivityContainer;
import de.decidr.model.workflowmodel.bpel.TAssign;
import de.decidr.model.workflowmodel.bpel.TCopy;
import de.decidr.model.workflowmodel.bpel.TCorrelationSet;
import de.decidr.model.workflowmodel.bpel.TCorrelationSets;
import de.decidr.model.workflowmodel.bpel.TDocumentation;
import de.decidr.model.workflowmodel.bpel.TFaultHandlers;
import de.decidr.model.workflowmodel.bpel.TFrom;
import de.decidr.model.workflowmodel.bpel.TImport;
import de.decidr.model.workflowmodel.bpel.TInvoke;
import de.decidr.model.workflowmodel.bpel.TLiteral;
import de.decidr.model.workflowmodel.bpel.TPartnerLink;
import de.decidr.model.workflowmodel.bpel.TPartnerLinks;
import de.decidr.model.workflowmodel.bpel.TProcess;
import de.decidr.model.workflowmodel.bpel.TSequence;
import de.decidr.model.workflowmodel.bpel.TTo;
import de.decidr.model.workflowmodel.bpel.TVariable;
import de.decidr.model.workflowmodel.bpel.TVariables;
import de.decidr.model.workflowmodel.dwdl.TActor;
import de.decidr.model.workflowmodel.dwdl.TRecipient;
import de.decidr.model.workflowmodel.dwdl.TRole;
import de.decidr.model.workflowmodel.dwdl.TSetProperty;
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
    private ObjectFactory factory = null;

    public TProcess getBPEL(TWorkflow dwdl) {

        this.dwdl = dwdl;

        factory = new ObjectFactory();
        process = factory.createTProcess();

        process.setName(dwdl.getName());

        setDocumentation();
        setProcessAttributes();
        setImports();
        setPartnerLinks();
        setVariables();
        setProcessVariables();
        setVariablesFromRoles();
        initVariables();
        setCorrelationSets();
        setFaultHandler();
        setActivity();
        return process;
    }

    private void setProcessVariables() {
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

        process.setVariables(variables);
        startConfigurations.setName("startConfigurations");
        startConfigurations.setMessageType(new QName("startMessage"));
        wfmid.setName("wfmid");
        wfmid.setType(new QName("xsd:ID"));
        faultMessage.setName("faultMessage");
        faultMessage.setMessageType(new QName("sendEmailRequest"));
        faultMessageResponse.setName("faultMessageResponse");
        faultMessageResponse.setMessageType(new QName("sendEmailResponse"));
        successMessage.setName("successMessage");
        successMessage.setMessageType(new QName("sendEmailRequest"));
        successMessageResponse.setName("successMessageResponse");
        successMessageResponse.setMessageType(new QName("sendEmailResponse"));
        taskMessage.setName("createTaskMessage");
        taskMessage.setMessageType(new QName("createTaskRequest"));
        taskMessageResponse.setName("createTaskMessageResponse");
        taskMessageResponse.setMessageType(new QName("createTaskResponse"));
        taskDataMessage.setName("taskDataMessage");
        taskDataMessage.setMessageType(new QName("taskDataRequest"));
        process.getVariables().getVariable().add(startConfigurations);
        process.getVariables().getVariable().add(faultMessage);
        process.getVariables().getVariable().add(faultMessageResponse);
        process.getVariables().getVariable().add(successMessage);
        process.getVariables().getVariable().add(successMessageResponse);
        process.getVariables().getVariable().add(taskMessage);
        process.getVariables().getVariable().add(taskMessageResponse);
        process.getVariables().getVariable().add(taskDataMessage);
    }

    private void initRoles(TAssign assign) {
        if (dwdl.getRoles() != null) {
            TCopy copyRole = factory.createTCopy();
            if (!dwdl.getRoles().getRole().isEmpty()) {
                for (TRole role : dwdl.getRoles().getRole()) {
                    if (!role.getActor().isEmpty()) {
                        TFrom from = factory.createTFrom();
                        TTo to = factory.createTTo();
                        TLiteral literal = factory.createTLiteral();
                        String content = "<role name=" + role.getName() + ">";
                        for (TActor actor : role.getActor()) {
                            content.concat(getXMLElement(actor));
                        }
                        content.concat("</role>");
                        from.getContent().add(literal);
                        to.setVariable(role.getName());
                        copyRole.setFrom(from);
                        copyRole.setTo(to);
                    }

                }
                assign.getCopyOrExtensionAssignOperation().add(copyRole);
            }
            if (!dwdl.getRoles().getActor().isEmpty()) {
                TCopy copyActor = factory.createTCopy();
                for (TActor actor : dwdl.getRoles().getActor()) {
                    TFrom from = factory.createTFrom();
                    TTo to = factory.createTTo();
                    TLiteral literal = factory.createTLiteral();
                    literal.getContent().add(getXMLElement(actor));
                    from.getContent().add(literal);
                    to.setVariable(actor.getName());
                    copyActor.setFrom(from);
                    copyActor.setTo(to);
                }
                assign.getCopyOrExtensionAssignOperation().add(copyActor);
            }
        }
    }

    private String getXMLElement(TActor actor) {
        String xml = "<actor name=" + actor.getName() + " userid="
                + actor.getUserId() + " email=" + actor.getEmail() + "/>";

        return xml;
    }

    private void initVariables() {
        TAssign assign = factory.createTAssign();
        assign.setName("init");
        TCopy copy = factory.createTCopy();
        if (dwdl.getVariables() != null) {
            if (!dwdl.getVariables().getVariable().isEmpty()) {
                for (de.decidr.model.workflowmodel.dwdl.TVariable var : dwdl
                        .getVariables().getVariable()) {
                    if (var.getInitialValue() != null) {
                        TFrom from = factory.createTFrom();
                        TTo to = factory.createTTo();
                        TLiteral literal = factory.createTLiteral();
                        literal.getContent().add(var.getInitialValue());
                        from.getContent().add(literal);
                        to.setVariable(var.getName());
                        assign.getCopyOrExtensionAssignOperation().add(copy);
                    } else if (var.getInitialValues() != null) {
                        TFrom from = factory.createTFrom();
                        TTo to = factory.createTTo();
                        TLiteral literal = factory.createTLiteral();
                        literal.getContent().add(var.getInitialValue());
                        from.getContent().add(literal);
                        to.setVariable(var.getName());
                        assign.getCopyOrExtensionAssignOperation().add(copy);
                    }
                }
            }
        }
        initRoles(assign);
    }

    private void setActivity() {

    }

    private void setFaultHandler() {

        TActivityContainer activityContainer = factory
                .createTActivityContainer();
        TSequence sequence = factory.createTSequence();
        TAssign assign = factory.createTAssign();
        TInvoke emailInvoke = factory.createTInvoke();
        if (dwdl.getFaultHandler() != null) {
            TFaultHandlers faultHandlers = factory.createTFaultHandlers();
            if (!dwdl.getFaultHandler().getSetProperty().isEmpty()) {
                for (TSetProperty property : dwdl.getFaultHandler()
                        .getSetProperty()) {
                    if (property.getVariable().isEmpty()) {
                        addCopyValueStatement(assign, property,
                                getBPELVariableByName("faultMessage"));
                    } else {
                        addCopyStatement(assign, property,
                                getBPELVariableByName("faultMessage"));
                    }
                }
            }
            if (!dwdl.getFaultHandler().getRecipient().isEmpty()) {

                for (TRecipient recipient : dwdl.getFaultHandler()
                        .getRecipient()) {
                    for (TSetProperty property : recipient.getSetProperty()) {
                        if (property.getVariable() != null) {
                            addCopyValueStatement(assign, property,
                                    getBPELVariableByName("faultMessage"));
                        } else {
                            addCopyStatement(assign, property,
                                    getBPELVariableByName("faultMessage"));
                        }
                    }
                }
            }
            emailInvoke.setPartnerLink(BPELConstants.EWSPL);
            emailInvoke.setOperation("sendEmail");
            emailInvoke.setInputVariable("faultMessage");
            emailInvoke.setOutputVariable("faultMessageResponse");
            sequence.getActivity().add(assign);
            sequence.getActivity().add(emailInvoke);
            activityContainer.setSequence(sequence);
            faultHandlers.setCatchAll(activityContainer);
        }
    }

    private void addCopyStatement(TAssign assign, TSetProperty property,
            TVariable toVariable) {
        TCopy copy = factory.createTCopy();
        TFrom from = factory.createTFrom();
        TTo to = factory.createTTo();
        from.setVariable(property.getVariable());
        to.setVariable(toVariable.getName());
        to.setProperty(new QName(property.getName()));
        assign.getCopyOrExtensionAssignOperation().add(copy);
    }

    private void addCopyValueStatement(TAssign assign, TSetProperty property,
            TVariable toVariable) {
        TCopy copy = factory.createTCopy();
        TFrom from = factory.createTFrom();
        TTo to = factory.createTTo();
        TLiteral literal = factory.createTLiteral();
        literal.getContent().add(property.getPropertyValue());
        from.getContent().add(literal);
        to.setVariable(toVariable.getName());
        to.setProperty(new QName(property.getName()));
        assign.getCopyOrExtensionAssignOperation().add(copy);
    }

    private de.decidr.model.workflowmodel.bpel.TVariable getBPELVariableByName(
            String variableName) {
        for (TVariable var : process.getVariables().getVariable()) {
            if (var.getName().equals(variableName)) {
                return var;
            }
        }
        return null;
    }

    private void setCorrelationSets() {
        TCorrelationSets correlationSets = factory.createTCorrelationSets();
        process.setCorrelationSets(correlationSets);
        TCorrelationSet correlation = factory.createTCorrelationSet();
        correlation.setName("standard-correlation");
        correlation.getProperties().add(new QName("processID"));
        correlation.getProperties().add(new QName("userID"));
        correlation.getProperties().add(new QName("taskID"));
        process.getCorrelationSets().getCorrelationSet().add(correlation);
    }

    private void setVariablesFromRoles() {
        if (dwdl.getRoles() != null) {
            if (!dwdl.getRoles().getRole().isEmpty()) {
                for (TRole r : dwdl.getRoles().getRole()) {
                    de.decidr.model.workflowmodel.bpel.TVariable role = factory
                            .createTVariable();
                    role.setName(r.getName());
                    role.setElement(new QName("decidr:role"));
                    process.getVariables().getVariable().add(role);
                }
            }
            if (!dwdl.getRoles().getActor().isEmpty()) {
                for (TActor a : dwdl.getRoles().getActor()) {
                    de.decidr.model.workflowmodel.bpel.TVariable actor = factory
                            .createTVariable();
                    actor.setName(a.getName());
                    actor.setElement(new QName("decidr:actor"));
                    process.getVariables().getVariable().add(actor);
                }
            }
        }
    }

    private void setPartnerLinks() {
        TPartnerLinks pls = factory.createTPartnerLinks();
        process.setPartnerLinks(pls);
        TPartnerLink htwsPL = factory.createTPartnerLink();
        TPartnerLink ewsPL = factory.createTPartnerLink();
        htwsPL.setName(BPELConstants.HTWSPL);
        htwsPL.setPartnerLinkType(new QName(BPELConstants.HTWSPLT));
        htwsPL.setPartnerRole("provider");
        htwsPL.setMyRole("client");
        ewsPL.setName(BPELConstants.EWSPL);
        ewsPL.setPartnerLinkType(new QName(BPELConstants.EWSPLT));
        ewsPL.setPartnerRole("provider");
        process.getPartnerLinks().getPartnerLink().add(htwsPL);
        process.getPartnerLinks().getPartnerLink().add(ewsPL);
    }

    private void setImports() {
        TImport htwsImport = factory.createTImport();
        TImport ewsImport = factory.createTImport();
        TImport dtImport = factory.createTImport();
        htwsImport.setNamespace(BPELConstants.HTWSNAMESPACE);
        htwsImport.setImportType(BPELConstants.WSDLIMPORTTYPE);
        htwsImport.setLocation(BPELConstants.HTWSLOCATION);
        ewsImport.setNamespace(BPELConstants.EWSNAMESPACE);
        ewsImport.setImportType(BPELConstants.WSDLIMPORTTYPE);
        ewsImport.setLocation(BPELConstants.EWSLOCATION);
        dtImport.setNamespace(BPELConstants.DECIDRTYPESNAMESPACE);
        dtImport.setImportType(BPELConstants.XSDIMPORTTYPE);
        dtImport.setLocation("DecidrTypes.xsd");
        process.getImport().add(htwsImport);
        process.getImport().add(ewsImport);
        process.getImport().add(dtImport);
    }

    private void setProcessAttributes() {
        process.setName(dwdl.getName() + "_" + dwdl.getId());
        process.setTargetNamespace(dwdl.getTargetNamespace() + "_"
                + dwdl.getId());
    }

    private void setDocumentation() {
        TDocumentation documentation = factory.createTDocumentation();
        if (dwdl.getDescription() != null) {
            documentation.getContent().addAll(
                    dwdl.getDescription().getContent());
            process.getDocumentation().add(documentation);
        }
    }

    private void setVariables() {
        if (dwdl.getVariables() != null) {
            de.decidr.model.workflowmodel.bpel.TVariables vars = factory
                    .createTVariables();
            process.setVariables(vars);
            for (de.decidr.model.workflowmodel.dwdl.TVariable v : dwdl
                    .getVariables().getVariable()) {
                de.decidr.model.workflowmodel.bpel.TVariable var = factory
                        .createTVariable();
                var.setName(v.getName());
                var.setType(new QName("xsd:" + v.getType()));
                process.getVariables().getVariable().add(var);
            }
        }
    }

}
