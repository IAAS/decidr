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
import de.decidr.modelingtool.client.model.foreach.ForEachContainerModel;
import de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel;
import de.decidr.modelingtool.client.model.humantask.TaskItem;
import de.decidr.modelingtool.client.model.ifcondition.Condition;
import de.decidr.modelingtool.client.model.ifcondition.IfContainerModel;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.model.variable.VariablesFilter;

/**
 * The actual implementation of a workflow parser.
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
        // JS replace by actual description
        workflow.appendChild(createTextElement(doc, "description",
                "this is a simple workflow"));

        /* Create variable and role nodes */
        createVariablesAndRoles(doc, workflow, model);

        /* Create role node */
        // JS implement (also server get method in value editor)
        // workflow.appendChild(createRoles(doc, model));
        /* Create fault handler node */
        workflow.appendChild(createFaultHandlerElement(doc, model));

        /* Create container and invoke nodes */
        workflow.appendChild(createChildNodeElements(doc, model));

        /* Create arcs */
        workflow.appendChild(createArcElements(doc, model));

        /* append tree to root element */
        doc.appendChild(workflow);
        return doc.toString();
    }

    private void createVariablesAndRoles(Document doc, Element parent,
            WorkflowModel model) {
        /*
         * Because a role is just a variable of the type role, it is easier to
         * create both variables and roles node here and append it to the root
         * element.
         */
        Element variables = doc.createElement("variables");
        Element roles = doc.createElement("roles");
        for (Variable var : model.getVariables()) {
            /* Distinguish between "normal" variables and roles */
            if (var.getType() == VariableType.ROLE) {
                roles.appendChild(createRoleElement(doc, var));
            } else {
                variables.appendChild(createVariableElement(doc, var));
            }

        }
        /* Append variables and roles node to parent element */
        parent.appendChild(variables);
        parent.appendChild(roles);
    }

    private Element createVariableElement(Document doc, Variable variable) {
        Element variableElement = doc.createElement("variable");
        /* Name */
        variableElement.setAttribute("name", variable.getName());
        /* Array */
        if (variable.isArray()) {
            variableElement.setAttribute("type", "list-"
                    + variable.getType().getDwdlName());
        } else {
            variableElement.setAttribute("type", variable.getType()
                    .getDwdlName());
        }
        /* Configuration variable */
        if (variable.isConfig()) {
            variableElement.setAttribute("configurationVariable", "yes");
        } else {
            variableElement.setAttribute("configurationVariable", "no");
        }
        /*
         * Values of the variable, if the variable has multiple values an
         * additional node is created which has the values as children
         */
        if (variable.isArray()) {
            Element values = doc.createElement("initialValues");
            for (String value : variable.getValues()) {
                values
                        .appendChild(createTextElement(doc, "initialValue",
                                value));
            }
            variableElement.appendChild(values);
        } else {
            variableElement.appendChild(createTextElement(doc, "initialValue",
                    variable.getValues().get(0)));
        }
        return variableElement;
    }

    private Element createRoleElement(Document doc, Variable role) {
        /* Create parent element for one role */
        Element roleElement = doc.createElement("role");
        if (role.isConfig()) {
            roleElement.setAttribute("configurationVariable", "yes");
        } else {
            roleElement.setAttribute("configurationVariable", "no");
        }
        /* Append the values of the role variables as actors */
        for (String value : role.getValues()) {
            Element actor = doc.createElement("actor");
            // JS fetch user ids properly
            actor.setAttribute("userId", value);
            roleElement.appendChild(actor);
        }
        return roleElement;
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
        /*
         * Create nodes element which contains all child nodes of a workflow or
         * a container
         */
        Element nodes = doc.createElement("nodes");
        for (NodeModel node : model.getChildNodeModels()) {
            /* Find out type and call the according node */
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
            } else if (node instanceof IfContainerModel) {
                nodes
                        .appendChild(createIfElement(doc,
                                (IfContainerModel) node));
            } else if (node instanceof ForEachContainerModel) {
                nodes.appendChild(createForEachElement(doc,
                        (ForEachContainerModel) node));
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
        /* start node is only source to connections */
        startNode.appendChild(createSourceElement(doc, node));

        return startNode;
    }

    private Element createEndElement(Document doc, WorkflowModel model,
            EndNodeModel node) {
        Element endElement = doc.createElement("endNode");
        endElement.setAttribute("name", "insert name here");
        endElement.setAttribute("id", "insert is here");

        endElement.appendChild(createGraphicsElement(doc, node));
        /* end node is only target to connections */
        endElement.appendChild(createTargetElement(doc, node));

        /* notification of success and recipient */
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

        humanTaskElement.setAttribute("name", node.getName());
        humanTaskElement.setAttribute("id", node.getId().toString());
        humanTaskElement.setAttribute("activity", "Decidr-HumanTask");
        humanTaskElement.appendChild(createGraphicsElement(doc, node));
        humanTaskElement.appendChild(createTargetElement(doc, node));
        humanTaskElement.appendChild(createSourceElement(doc, node));
        // JS ASK

        /* Create property elements for user */
        humanTaskElement.appendChild(createPropertyElement(doc, "wfmID", 0L));
        humanTaskElement.appendChild(createPropertyElement(doc, "user", node
                .getUserVariableId()));

        /*
         * Create set property for user notification variable. Cannot use
         * createPropertyElement method here because it does not take into
         * account that boolean values have to be converted to "yes"/"no".
         */
        Element userNotification = doc.createElement("setProperty");
        userNotification.setAttribute("name", "userNotification");
        String valueText;
        if (node.getNotify()) {
            valueText = "yes";
        } else {
            valueText = "no";
        }
        userNotification.appendChild(createTextElement(doc, "propertyValue",
                valueText));
        humanTaskElement.appendChild(userNotification);

        /* Create get property of human task */
        Element getProperty = doc.createElement("getProperty");
        getProperty.setAttribute("name", "taskResult");
        getProperty.setAttribute("variable", node.getFormVariableId()
                .toString());
        // JS ASK parameters and information item
        Element humanTaskData = doc.createElement("humanTaskData");
        /* Create task items */
        for (TaskItem ti : node.getTaskItems()) {
            Element taskItem = doc.createElement("taskItem");
            Variable variable = VariablesFilter.getVariableById(ti
                    .getVariableId());
            taskItem.setAttribute("name", "name");
            taskItem.setAttribute("variable", variable.getId().toString());
            taskItem.setAttribute("type", variable.getType().getDwdlName());
            taskItem
                    .appendChild(createTextElement(doc, "label", ti.getLabel()));
            taskItem.appendChild(createTextElement(doc, "value", variable
                    .getValues().get(0)));
            humanTaskData.appendChild(taskItem);
        }
        getProperty.appendChild(humanTaskData);

        humanTaskElement.appendChild(getProperty);
        return humanTaskElement;
    }

    private Element createFlowElement(Document doc, FlowContainerModel node) {
        Element flowElement = doc.createElement("flowNode");
        flowElement.appendChild(createGraphicsElement(doc, node));
        flowElement.appendChild(createTargetElement(doc, node));
        flowElement.appendChild(createSourceElement(doc, node));
        flowElement.appendChild(createChildNodeElements(doc, node));
        flowElement.appendChild(createArcElements(doc, node));
        return flowElement;
    }

    private Element createIfElement(Document doc, IfContainerModel node) {
        Element ifElement = doc.createElement("ifNode");
        ifElement.appendChild(createGraphicsElement(doc, node));
        ifElement.appendChild(createTargetElement(doc, node));
        ifElement.appendChild(createSourceElement(doc, node));
        /* Create condition elements */
        for (ConnectionModel connectionModel : node.getChildConnectionModels()) {
            if (connectionModel instanceof Condition) {
                Condition conModel = (Condition) connectionModel;
                Element conElement = doc.createElement("condition");
                conElement.setAttribute("oder", conModel.getOrder().toString());
                if (conModel.getOrder() == 1) {
                    conElement.setAttribute("defaultCondition", "yes");
                } else {
                    conElement.setAttribute("defaultCondition", "no");
                }
                conElement.appendChild(createTextElement(doc, "left-operand",
                        conModel.getOperand1Id().toString()));
                conElement.appendChild(createTextElement(doc, "operator",
                        conModel.getOperator().getDisplayString()));
                conElement.appendChild(createTextElement(doc, "right-operand",
                        conModel.getOperand2Id().toString()));
                conElement.appendChild(createChildNodeElements(doc, node));
                conElement.appendChild(createArcElements(doc, node));
                ifElement.appendChild(conElement);
            }
        }
        return ifElement;
    }

    private Element createForEachElement(Document doc,
            ForEachContainerModel node) {
        Element forEachElement = doc.createElement("forEachNode");
        forEachElement.setAttribute("countername", node
                .getIterationVariableId().toString());
        if (node.isParallel()) {
            forEachElement.setAttribute("parallel", "yes");
        } else {
            forEachElement.setAttribute("parallel", "no");
        }
        forEachElement.appendChild(createGraphicsElement(doc, node));
        forEachElement.appendChild(createTargetElement(doc, node));
        forEachElement.appendChild(createSourceElement(doc, node));

        /*
         * Creating counter values and completion condition elements. The start
         * counter value is always set to 1.
         */
        forEachElement.appendChild(createTextElement(doc, "startCounterValue",
                "1"));
        forEachElement.appendChild(createTextElement(doc, "finalCounterValue",
                node.getIterationVariableId().toString()));
        forEachElement.appendChild(createTextElement(doc,
                "completionCondition", node.getExitCondition().toString()));

        forEachElement.appendChild(createChildNodeElements(doc, node));
        forEachElement.appendChild(createArcElements(doc, node));

        return forEachElement;

    }

    private Element createGraphicsElement(Document doc, NodeModel node) {
        Element graphics = doc.createElement("graphics");
        /*
         * the position of a node is defined by the x and y coordinates of the
         * top left pixel
         */
        graphics.setAttribute("x", ((Integer) node.getNode().getGraphicLeft())
                .toString());
        graphics.setAttribute("y", ((Integer) node.getNode().getGraphicTop())
                .toString());
        /* width and height of the node */
        graphics.setAttribute("width", ((Integer) node.getNode()
                .getGraphicWidth()).toString());
        graphics.setAttribute("height", ((Integer) node.getNode()
                .getGraphicHeight()).toString());
        return graphics;
    }

    private Element createTextElement(Document doc, String tagName, String data) {
        /*
         * The created element looks like this: <tagName>data</tagName>
         */
        Element element = doc.createElement(tagName);
        Text text = doc.createTextNode(data);
        element.appendChild(text);
        return element;
    }

    private Element createPropertyElement(Document doc, String name,
            Long variableId) {
        Element property = doc.createElement("setProperty");
        // JS ASK what name is about (name of recipient property)
        property.setAttribute("name", name);
        property.setAttribute("variable", variableId.toString());
        // JS think this over (get(0)), what about multiple values?
        String data = VariablesFilter.getVariableById(variableId).getValues()
                .get(0);
        Element propertyValue = createTextElement(doc, "propertyValue", data);
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
            arc.setAttribute("source", con.getSource().getId().toString());
            arc.setAttribute("target", con.getTarget().getId().toString());
            arcs.appendChild(arc);
        }
        return arcs;
    }

}
