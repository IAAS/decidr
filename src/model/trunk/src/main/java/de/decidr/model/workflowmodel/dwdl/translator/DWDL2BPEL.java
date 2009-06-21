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

import de.decidr.model.workflowmodel.bpel.ObjectFactory;
import de.decidr.model.workflowmodel.bpel.TCorrelationSets;
import de.decidr.model.workflowmodel.bpel.TDocumentation;
import de.decidr.model.workflowmodel.bpel.TFaultHandlers;
import de.decidr.model.workflowmodel.bpel.TImport;

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
    private final String SOURCE = "http://www.decidr.de";

    private de.decidr.model.workflowmodel.bpel.TProcess process = null;
    private de.decidr.model.workflowmodel.dwdl.TWorkflow dwdl = null;
    private ObjectFactory factory;

    public de.decidr.model.workflowmodel.bpel.TProcess getBPEL(
            de.decidr.model.workflowmodel.dwdl.TWorkflow dwdl) {

        this.dwdl = dwdl;

        factory = new ObjectFactory();
        process = factory.createTProcess();
        
        process.setTargetNamespace(dwdl.getTargetNamespace());
        process.setExpressionLanguage(EXPRESSIONLANGUAGE);
        process.setQueryLanguage(QUERYLANGUAGE);
        process
                .setSuppressJoinFailure(de.decidr.model.workflowmodel.bpel.TBoolean
                        .fromValue(SUPPRESSJOINFAILURE));
        process
                .setExitOnStandardFault(de.decidr.model.workflowmodel.bpel.TBoolean
                        .fromValue(EXITONSTANDARDFAULT));
        process.getImport().add(createImports());

        process.setPartnerLinks(createPartnerLinks());
        process.getDocumentation().add(getDocumentation(dwdl.getDescription()));
        process.setVariables(getVariables(dwdl.getVariables()));
        process.getVariables().getVariable().addAll(
                createVariablesFromRoles(dwdl.getRoles()));
        process.setCorrelationSets(createCorrelationSets());
        process.setFaultHandlers(createFaultHandlers()); 
        return null;
    }

    private de.decidr.model.workflowmodel.bpel.TImport createImports() {

        return null;
    }
    

    private de.decidr.model.workflowmodel.bpel.TDocumentation getDocumentation(
            de.decidr.model.workflowmodel.dwdl.TDescription description) {
        de.decidr.model.workflowmodel.bpel.TDocumentation documentation = factory.createTDocumentation();
        documentation.getContent().addAll(description.getContent());
        return null;
    }

    private de.decidr.model.workflowmodel.bpel.TVariables getVariables(
            de.decidr.model.workflowmodel.dwdl.TVariables variables) {
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
    
    private TFaultHandlers createFaultHandlers() {
        
        return null;
    }
    
}
