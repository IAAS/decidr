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

package de.decidr.modelingtool.client.io;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;

import de.decidr.modelingtool.client.model.ConnectionModel;
import de.decidr.modelingtool.client.model.EmailInvokeNodeModel;
import de.decidr.modelingtool.client.model.EndNodeModel;
import de.decidr.modelingtool.client.model.FlowContainerModel;
import de.decidr.modelingtool.client.model.HasChildModels;
import de.decidr.modelingtool.client.model.NodeModel;
import de.decidr.modelingtool.client.model.StartNodeModel;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariablesFilter;

/**
 * TODO: add comment
 * 
 * @author Modood Alvi, Jonas Schlaak
 * @version 0.1
 */
public class WorkflowParserImpl implements WorkflowParser {
    // JS externalize dwdl tag names?

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.modelingtool.client.io.WorkflowParser#parse(de.decidr.modelingtool
     * .client.model.WorkflowModel)
     */
    @Override
    public String parse(WorkflowModel model) {
        Document doc = XMLParser.createDocument();

        /* Create work flow root element */
        Element workflow = doc.createElement("workflow");
        // JS work flow id and name space, make window appear when saving
        workflow.setAttribute("name", "decision process");
        workflow.setAttribute("ID", "123");
        workflow.setAttribute("targetNamespace", "insert namespace here");

        /* Create description node */
        Element description = doc.createElement("description");
        Text descTest = doc.createTextNode("this is a simple workflow");
        description.appendChild(descTest);
        workflow.appendChild(description);

        /* Create variable node */
        workflow.appendChild(createVariables(doc, model));

        /* Create role node */
        // JS implement (also server get method in value editor)
        // workflow.appendChild(createRoles(doc, model));
        /* Create fault handler node */
        workflow.appendChild(createFaultHandlerElement(doc, model));

        /* Create container and invoke nodes */
        workflow.appendChild(createChildNodeElements(doc, model));

        /* Create Arcs */
        workflow.appendChild(createArcElements(doc, model));

        /* append tree to root element */
        doc.appendChild(workflow);
        return doc.toString();
    }

    private Element createVariables(Document doc, WorkflowModel model) {
        Element variables = doc.createElement("variables");
        for (Variable var : model.getVariables()) {
            variables.appendChild(createVariableElement(doc, var));
        }
        return variables;
    }

    private Element createVariableElement(Document doc, Variable var) {
        Element variable = doc.createElement("variable");
        /* Name */
        variable.setAttribute("name", var.getName());
        /* Array */
        if (var.isArray()) {
            variable
                    .setAttribute("type", "list-" + var.getType().getDwdlName());
        } else {
            variable.setAttribute("type", var.getType().getDwdlName());
        }
        /* Configuration variable */
        if (var.isConfig()) {
            variable.setAttribute("configurationVariable", "yes");
        } else {
            variable.setAttribute("configurationVariable", "no");
        }
        /*
         * Values, if the variable has multiple values an additional node is
         * created which has the values as children
         */
        if (var.isArray()) {
            Element values = doc.createElement("initialValues");
            appendValueElements(doc, var, values);
            variable.appendChild(values);
        } else {
            appendValueElements(doc, var, variable);
        }
        return variable;
    }

    private void appendValueElements(Document doc, Variable var, Element parent) {
        for (String val : var.getValues()) {
            Element element = doc.createElement("initialValue");
            Text value = doc.createTextNode(val);
            element.appendChild(value);
            parent.appendChild(element);
        }
    }

    private Element createRoles(Document doc, WorkflowModel model) {
        // TODO Auto-generated method stub
        return null;
    }

    private Element createFaultHandlerElement(Document doc, WorkflowModel model) {
        Element faultHandler = doc.createElement("faultHandler");

        /* Create the property node for the fault message */
        faultHandler.appendChild(createPropertyElement(doc, "message", model
                .getProperties().getFaultMessageVariableId()));

        /* Create the property node for the recipient */
        faultHandler.appendChild(createPropertyElement(doc, "recipient", model
                .getProperties().getRecipientVariableId()));

        return faultHandler;
    }

    private Element createChildNodeElements(Document doc, HasChildModels model) {
        Element nodes = doc.createElement("nodes");
        for (NodeModel node : model.getChildNodeModels()) {
            if (node instanceof StartNodeModel) {
                nodes
                        .appendChild(createStartElement(doc,
                                (StartNodeModel) node));
            } else if (node instanceof EndNodeModel) {
                nodes.appendChild(createEndElement(doc, (WorkflowModel) model,
                        (EndNodeModel) node));
            } else if (node instanceof EmailInvokeNodeModel) {
                nodes.appendChild(createEmailElement(doc,
                        (EmailInvokeNodeModel) node));
            } else if (node instanceof HumanTaskInvokeNodeModel) {
                nodes.appendChild(createHumanTaskElement(doc,
                        (HumanTaskInvokeNodeModel) node));
            } else if (node instanceof FlowContainerModel) {
                nodes.appendChild(createFlowElement(doc,
                        (FlowContainerModel) node));
            }
        }
        return nodes;
    }

    private Element createStartElement(Document doc, StartNodeModel node) {
        // JS: ASK what is the id about
        Element startNode = doc.createElement("startNode");
        startNode.setAttribute("name", "insert name here");
        startNode.setAttribute("id", "insert is here");

        startNode.appendChild(createGraphicsElement(doc, node));
        startNode.appendChild(createSourceElement(doc, node));

        return startNode;
    }

    private Element createEndElement(Document doc, WorkflowModel model,
            EndNodeModel node) {
        Element endElement = doc.createElement("endNode");
        endElement.setAttribute("name", "insert name here");
        endElement.setAttribute("id", "insert is here");

        endElement.appendChild(createGraphicsElement(doc, node));
        endElement.appendChild(createTargetElement(doc, node));

        Element notification = doc.createElement("notificationOfSuccess");
        notification.appendChild(createPropertyElement(doc, "successMessage",
                model.getProperties().getSuccessMessageVariableId()));
        notification.appendChild(createPropertyElement(doc, "recipient", model
                .getProperties().getRecipientVariableId()));
        return endElement;
    }

    private Element createEmailElement(Document doc, EmailInvokeNodeModel node) {
        Element emailElement = doc.createElement("invokeNode");
        emailElement.setAttribute("name", node.getName());
        emailElement.setAttribute("id", node.getId().toString());
        emailElement.setAttribute("activity", "Decidr-Email");
        emailElement.appendChild(createGraphicsElement(doc, node));
        emailElement.appendChild(createSourceElement(doc, node));
        emailElement.appendChild(createTargetElement(doc, node));
        emailElement.appendChild(createPropertyElement(doc, "to", node
                .getToVariableId()));
        emailElement.appendChild(createPropertyElement(doc, "cc", node
                .getCcVariableId()));
        emailElement.appendChild(createPropertyElement(doc, "bcc", node
                .getBccVariableId()));
        emailElement.appendChild(createPropertyElement(doc, "subject", node
                .getSubjectVariableId()));
        emailElement.appendChild(createPropertyElement(doc, "message", node
                .getMessageVariableId()));
        emailElement.appendChild(createPropertyElement(doc, "attachement", node
                .getAttachmentVariableId()));
        return emailElement;
    }

    private Element createHumanTaskElement(Document doc,
            HumanTaskInvokeNodeModel node) {
        Element humanTaskElement = doc.createElement("invokeNode");
        // TODO: implement
        return humanTaskElement;
    }

    private Element createFlowElement(Document doc, FlowContainerModel node) {
        Element flowElement = doc.createElement("flowNode");
        // JS implement attributes name, id, description
        flowElement.appendChild(createGraphicsElement(doc, node));
        flowElement.appendChild(createTargetElement(doc, node));
        flowElement.appendChild(createSourceElement(doc, node));
        // JS implement arcs
        flowElement.appendChild(createChildNodeElements(doc, node));
        flowElement.appendChild(createArcElements(doc, node));
        return flowElement;
    }

    private Element createGraphicsElement(Document doc, NodeModel node) {
        Element graphics = doc.createElement("graphics");
        graphics.setAttribute("x", ((Integer) node.getNode().getGraphicLeft())
                .toString());
        graphics.setAttribute("y", ((Integer) node.getNode().getGraphicTop())
                .toString());
        graphics.setAttribute("width", ((Integer) node.getNode()
                .getGraphicWidth()).toString());
        graphics.setAttribute("height", ((Integer) node.getNode()
                .getGraphicHeight()).toString());
        return graphics;
    }

    private Element createPropertyElement(Document doc, String name,
            Long variableId) {
        Element property = doc.createElement("setProperty");
        // JS ASK what name is about (name of recipient property)
        property.setAttribute("name", name);
        property.setAttribute("variable", variableId.toString());
        Element propertyValue = doc.createElement("propertyValue");
        // JS think this over (get(0)), what about multiple values?
        Text valueText = doc.createTextNode(VariablesFilter.getVariableById(
                variableId).getValues().get(0));
        propertyValue.appendChild(valueText);
        property.appendChild(propertyValue);
        return property;
    }

    private Element createSourceElement(Document doc, NodeModel node) {
        // JS ASK what about multiple sources?
        Element sources = doc.createElement("sources");
        Element source = doc.createElement("source");
        // JS set arcId properly
        source.setAttribute("arcId", "insert id here");
        sources.appendChild(source);
        return sources;
    }

    private Element createTargetElement(Document doc, NodeModel node) {
        // JS ASK what about multiple sources?
        Element targets = doc.createElement("targets");
        Element target = doc.createElement("target");
        // JS set arcId properly
        target.setAttribute("arcId", "insert id here");
        targets.appendChild(target);
        return targets;
    }

    private Element createArcElements(Document doc, HasChildModels node) {
        Element arcs = doc.createElement("arcs");
        for (ConnectionModel con : node.getChildConnectionModels()) {
            Element arc = doc.createElement("arc");
            arc.setAttribute("name", con.getName());
            arc.setAttribute("id", con.getId().toString());
            // JS implements Ids for Nodes
            arc.setAttribute("source", con.getSource().getId().toString());
            arc.setAttribute("target", con.getTarget().getId().toString());
            arcs.appendChild(arc);
        }
        return arcs;
    }

}
