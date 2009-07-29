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
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;

import de.decidr.modelingtool.client.model.EmailInvokeNodeModel;
import de.decidr.modelingtool.client.model.EndNodeModel;
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
        workflow.appendChild(createFaultHandlerNode(doc, model));

        /* Create container and invoke nodes */
        workflow.appendChild(createNodes(doc, model));

        /* append tree to root element */
        doc.appendChild(workflow);
        return doc.toString();
    }

    private Node createVariables(Document doc, WorkflowModel model) {
        Element variables = doc.createElement("variables");

        for (Variable var : model.getVariables()) {
            variables.appendChild(createVariableNode(doc, var));
        }
        return variables;
    }

    private Node createVariableNode(Document doc, Variable var) {
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
            appendValueNodes(doc, var, values);
            variable.appendChild(values);
        } else {
            appendValueNodes(doc, var, variable);
        }
        return variable;
    }

    private void appendValueNodes(Document doc, Variable var, Node parent) {
        for (String val : var.getValues()) {
            Element element = doc.createElement("initialValue");
            Text value = doc.createTextNode(val);
            element.appendChild(value);
            parent.appendChild(element);
        }
    }

    private Node createRoles(Document doc, WorkflowModel model) {
        // TODO Auto-generated method stub
        return null;
    }

    private Node createFaultHandlerNode(Document doc, WorkflowModel model) {
        Element faultHandler = doc.createElement("faultHandler");

        /* Create the property node for the fault message */
        Element messageProperty = doc.createElement("setProperty");
        messageProperty.setAttribute("name", "insert name here");
        messageProperty.setAttribute("variable", model.getProperties()
                .getFaultMessageVariableId().toString());

        /* Create the child node which contains the actual fault message */
        Element messageValue = doc.createElement("propertyValue");
        Text messageText = doc.createTextNode(VariablesFilter.getVariableById(
                model.getProperties().getFaultMessageVariableId()).getValues()
                .get(0));
        messageValue.appendChild(messageText);
        messageProperty.appendChild(messageValue);
        faultHandler.appendChild(messageProperty);

        /* Create the property node for the recipient */
        Element recipient = doc.createElement("recipient");
        Element recipientProperty = doc.createElement("setProperty");
        // JS ASK what name is about
        recipientProperty.setAttribute("name", "insert name here");
        recipientProperty.setAttribute("variable", model.getProperties()
                .getRecipientVariableId().toString());
        Text recipientValue = doc
                .createTextNode(VariablesFilter.getVariableById(
                        model.getProperties().getRecipientVariableId())
                        .getValues().get(0));
        recipientProperty.appendChild(recipientValue);
        recipient.appendChild(recipientProperty);
        faultHandler.appendChild(recipient);

        return faultHandler;
    }

    private Node createNodes(Document doc, WorkflowModel model) {
        Element nodes = doc.createElement("nodes");

        for (NodeModel node : model.getChildNodeModels()) {
            if (node instanceof StartNodeModel) {
                nodes.appendChild(createStartNode(doc, (StartNodeModel) node));
            } else if (node instanceof EndNodeModel) {
                nodes.appendChild(createEndNode(doc, (EndNodeModel) node));
            } else if (node instanceof EmailInvokeNodeModel) {
                nodes.appendChild(createEmailNode(doc,
                        (EmailInvokeNodeModel) node));
            } else if (node instanceof HumanTaskInvokeNodeModel) {
                nodes.appendChild(createHumanTaskNode(doc,
                        (HumanTaskInvokeNodeModel) node));
            }

        }

        return nodes;
    }

    private Node createStartNode(Document doc, StartNodeModel node) {
        // JS: ASK what is the id about
        Element startNode = doc.createElement("startNode");
        startNode.setAttribute("name", "insert name here");
        startNode.setAttribute("id", "insert is here");

        startNode.appendChild(createGraphicsElement(doc, node));
        startNode.appendChild(createSourceElement(doc, node));

        return startNode;
    }

    private Node createEndNode(Document doc, EndNodeModel node) {
        Element endNode = doc.createElement("endNode");
        endNode.setAttribute("name", "insert name here");
        endNode.setAttribute("id", "insert is here");

        endNode.appendChild(createGraphicsElement(doc, node));
        endNode.appendChild(createTargetElement(doc, node));

        return endNode;
    }

    private Node createEmailNode(Document doc, EmailInvokeNodeModel node) {
        Element emailNode = doc.createElement("invokeNode");

        return emailNode;
    }

    private Node createHumanTaskNode(Document doc, HumanTaskInvokeNodeModel node) {
        Element humanTaskNode = doc.createElement("invokeNode");

        return humanTaskNode;
    }

    private Node createGraphicsElement(Document doc, NodeModel node) {
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

    private Node createSourceElement(Document doc, NodeModel node) {
        // JS ASK what about multiple sources?
        Element sources = doc.createElement("sources");
        Element source = doc.createElement("source");
        // JS set arcId properly
        source.setAttribute("arcId", "insert id here");
        sources.appendChild(source);
        return sources;
    }

    private Node createTargetElement(Document doc, NodeModel node) {
        // JS ASK what about multiple sources?
        Element targets = doc.createElement("targets");
        Element target = doc.createElement("target");
        // JS set arcId properly
        target.setAttribute("arcId", "insert id here");
        targets.appendChild(target);
        return targets;
    }

}
