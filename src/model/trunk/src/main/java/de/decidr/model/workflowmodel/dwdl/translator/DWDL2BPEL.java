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

import javax.xml.namespace.QName;

import de.decidr.model.workflowmodel.bpel.ObjectFactory;
import de.decidr.model.workflowmodel.bpel.TDocumentation;
import de.decidr.model.workflowmodel.bpel.TImport;
import de.decidr.model.workflowmodel.bpel.TProcess;
import de.decidr.model.workflowmodel.dwdl.TDescription;
import de.decidr.model.workflowmodel.dwdl.TFaultHandler;
import de.decidr.model.workflowmodel.dwdl.TNodes;
import de.decidr.model.workflowmodel.dwdl.TRoles;
import de.decidr.model.workflowmodel.dwdl.TWorkflow;

/**
 * This class converts a given DWDL object and returns the resulting BPEL.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DWDL2BPEL {

    private final String EXPRESSIONLANGUAGE = "urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0";
    private final String QUERYLANGUAGE = "urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0";
    private final String SUPPRESSJOINFAILURE = "no";
    private final String EXITONSTANDARDFAULT = "no";
    private final String HTWSNAMESPACE = "http://decidr.de/webservices/HumanTask";
    private final String EWSNAMESPACE = "http://decidr.de/webservices/Email";
    private final String WSDLIMPORTTYPE = "http://schemas.xmlsoap.org/wsdl/";
    private final String XSDIMPORTTYPE = "http://www.w3.org/2001/XMLSchema";
    private final String DECIDRTYPESNS = "http://decidr.de/schema/DecidrTypes";

    private TProcess process = null;
    private TWorkflow dwdl = null;
    private ObjectFactory factory;

    public TProcess getBPEL(TWorkflow dwdl) {

        this.dwdl = dwdl;

        factory = new ObjectFactory();
        process = factory.createTProcess();
        setDocumentation(dwdl.getDescription());
        setProcessAttributes();
        setImports();
        setPartnerLinks();
        setVariables(dwdl.getVariables().getVariable());
        setVariablesFromRoles(dwdl.getRoles());
        setCorrelationSets();
        setFaultHandler(dwdl.getFaultHandler());
        setActivity(dwdl.getNodes());
        return process;
    }

    private void setActivity(TNodes nodes) {

    }

    private void setFaultHandler(TFaultHandler faultHandler) {

    }

    private void setCorrelationSets() {

    }

    private void setVariablesFromRoles(TRoles roles) {

    }

    private void setPartnerLinks() {

    }

    private void setImports() {
        TImport htwsImport = factory.createTImport();
        TImport ewsImport = factory.createTImport();
        TImport dtImport = factory.createTImport();
        htwsImport.setNamespace(HTWSNAMESPACE);
        htwsImport.setImportType(WSDLIMPORTTYPE);
        htwsImport.setLocation("HumanTask.wsdl");
        ewsImport.setNamespace(EWSNAMESPACE);
        ewsImport.setImportType(WSDLIMPORTTYPE);
        ewsImport.setLocation("Email.wsdl");
        dtImport.setNamespace(DECIDRTYPESNS);
        dtImport.setImportType(XSDIMPORTTYPE);
        dtImport.setLocation("DecidrTypes.xsd");
    }

    private void setProcessAttributes() {
        process.setName(dwdl.getName() + "_" + dwdl.getId());
        process.setTargetNamespace(dwdl.getTargetNamespace());
        process.setExpressionLanguage(EXPRESSIONLANGUAGE);
        process.setQueryLanguage(QUERYLANGUAGE);
        process
                .setSuppressJoinFailure(de.decidr.model.workflowmodel.bpel.TBoolean
                        .fromValue(SUPPRESSJOINFAILURE));
        process
                .setExitOnStandardFault(de.decidr.model.workflowmodel.bpel.TBoolean
                        .fromValue(EXITONSTANDARDFAULT));
    }

    private void setDocumentation(TDescription description) {
        TDocumentation documentation = factory.createTDocumentation();
        documentation.getContent().addAll(description.getContent());

    }

    private void setVariables(
            List<de.decidr.model.workflowmodel.dwdl.TVariable> variables) {
        for (de.decidr.model.workflowmodel.dwdl.TVariable v : variables) {
            de.decidr.model.workflowmodel.bpel.TVariable var = factory
                    .createTVariable();
            var.setName(v.getName());
            var.setType(new QName("xsd:" + v.getType()));
            process.getVariables().getVariable().add(var);
        }

        return null;
    }

    private List<de.decidr.model.workflowmodel.bpel.TVariable> createVariablesFromRoles(
            de.decidr.model.workflowmodel.dwdl.TRoles roles) {
        return null;
    }

    private de.decidr.model.workflowmodel.bpel.TFaultHandlers getFaultHandler(
            de.decidr.model.workflowmodel.dwdl.TNotification faultNotification) {
        return null;
    }

    private de.decidr.model.workflowmodel.bpel.TReceive getStartActivity(
            de.decidr.model.workflowmodel.dwdl.TBasicNode basicNode) {
        return null;
    }

    private de.decidr.model.workflowmodel.bpel.TReply getEndActivity(
            de.decidr.model.workflowmodel.dwdl.TEndNode endNode) {
        return null;
    }

    private de.decidr.model.workflowmodel.bpel.TFlow getFlowActivity(
            de.decidr.model.workflowmodel.dwdl.TFlowNode flowNode) {
        return null;
    }

    private de.decidr.model.workflowmodel.bpel.TIf getIfActivity(
            de.decidr.model.workflowmodel.dwdl.TIfNode ifNode) {
        return null;
    }

    private de.decidr.model.workflowmodel.bpel.TForEach getForeachActivity(
            de.decidr.model.workflowmodel.dwdl.TForEachNode foreachNode) {
        return null;
    }

    private de.decidr.model.workflowmodel.bpel.TInvoke getInvokeActivity(
            de.decidr.model.workflowmodel.dwdl.TInvokeNode invokeNode) {
        return null;
    }

    private de.decidr.model.workflowmodel.bpel.TLinks getLinks(
            de.decidr.model.workflowmodel.dwdl.TArcs arcs) {
        return null;
    }

    private de.decidr.model.workflowmodel.bpel.TPartnerLinks createPartnerLinks() {
        return null;
    }

    private de.decidr.model.workflowmodel.bpel.TCorrelationSets createCorrelationSets() {

        return null;
    }

    private de.decidr.model.workflowmodel.bpel.TFaultHandlers createFaultHandlers(
            de.decidr.model.workflowmodel.dwdl.TFaultHandler faultHandler) {

        return null;
    }

    private de.decidr.model.workflowmodel.bpel.TFlow getNodes(
            de.decidr.model.workflowmodel.dwdl.TNodes nodes) {

        return null;
    }

}
