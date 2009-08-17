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

import java.util.HashMap;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;

import de.decidr.modelingtool.client.io.resources.DWDLNames;
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
        Element workflow = doc.createElement(DWDLNames.root);
        // JS set namespace properly
        workflow.setAttribute(DWDLNames.name, model.getName());
        workflow.setAttribute(DWDLNames.id, model.getId().toString());
        workflow.setAttribute(DWDLNames.namespace, "http://decidr.de/"
                + "insert tenant name here" + "processes/" + model.getName());
        workflow.setAttribute("xmlns", "http://decidr.de/schema/dwdl");

        /* Create description node */
        workflow.appendChild(createTextElement(doc, DWDLNames.description,
                model.getDescription()));

        /* Create variable and role nodes */
        createVariablesAndRoles(doc, workflow, model);

        /* Create fault handler node */
        workflow.appendChild(createFaultHandlerElement(doc, model));

        /* Create container and invoke nodes */
        workflow.appendChild(createChildNodeElements(doc, model, model));

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
        Element variables = doc.createElement(DWDLNames.variables);
        Element roles = doc.createElement(DWDLNames.roles);
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
        Element variableElement = doc.createElement(DWDLNames.variable);
        /*
         * According to the DWDL schema, the "name" attribute is used to
         * uniquely identify the variables. In the Modeling tool, variables of
         * type long are used. Because the attribute in the dwdl is a NCName
         * (NCnames must not begin with a digit), we cannot simply use the long
         * id for this attribute, we have to set a prefix.
         */
        variableElement.setAttribute(DWDLNames.name,
                DWDLNames.variableNCnamePrefix + variable.getId());

        /* Label */
        variableElement.setAttribute(DWDLNames.label, variable.getLabel());

        /* Array */
        if (variable.isArray()) {
            variableElement.setAttribute(DWDLNames.type, DWDLNames.listprefix
                    + variable.getType().getDwdlName());
        } else {
            variableElement.setAttribute(DWDLNames.type, variable.getType()
                    .getDwdlName());
        }

        /* Configuration variable */
        if (variable.isConfig()) {
            variableElement.setAttribute(DWDLNames.configVar, DWDLNames.yes);
        } else {
            variableElement.setAttribute(DWDLNames.configVar, DWDLNames.no);
        }
        /*
         * Values of the variable, if the variable has multiple values an
         * additional node is created which has the values as children
         */
        if (variable.isArray()) {
            Element values = doc.createElement(DWDLNames.initValues);
            for (String value : variable.getValues()) {
                values.appendChild(createTextElement(doc, DWDLNames.initValue,
                        value));
            }
            variableElement.appendChild(values);
        } else {
            variableElement.appendChild(createTextElement(doc,
                    DWDLNames.initValue, variable.getValues().get(0)));
        }
        return variableElement;
    }

    private Element createRoleElement(Document doc, Variable role) {

        /* Create parent element for one role */
        Element roleElement = doc.createElement(DWDLNames.role);
        if (role.isConfig()) {
            roleElement.setAttribute(DWDLNames.configVar, DWDLNames.yes);
        } else {
            roleElement.setAttribute(DWDLNames.configVar, DWDLNames.no);
        }

        /* Set name and label */
        roleElement.setAttribute(DWDLNames.label, role.getLabel());
        roleElement.setAttribute(DWDLNames.name, DWDLNames.variableNCnamePrefix
                + role.getId());

        /* Append the values of the role variables as actors */
        for (String value : role.getValues()) {
            Element actor = doc.createElement(DWDLNames.actor);
            // JS fetch user ids properly
            actor.setAttribute(DWDLNames.userId, value);
            roleElement.appendChild(actor);
        }
        return roleElement;
    }

    private Element createFaultHandlerElement(Document doc, WorkflowModel model) {
        Element faultHandler = doc.createElement(DWDLNames.faultHandler);

        /* Create the property node for the fault message */
        faultHandler.appendChild(createPropertyElement(doc, DWDLNames.message,
                model.getProperties().getFaultMessageVariableId()));

        /* Create the property node for the recipient */
        Element recipient = doc.createElement(DWDLNames.recipient);
        recipient.appendChild(createPropertyElement(doc, DWDLNames.name, model
                .getProperties().getRecipientVariableId()));
        faultHandler.appendChild(recipient);

        return faultHandler;
    }

    private Element createChildNodeElements(Document doc,
            WorkflowModel workflow, HasChildModels parent) {
        /*
         * Create nodes element which contains all child nodes of a workflow or
         * a container
         */
        Element nodes = doc.createElement(DWDLNames.nodes);
        for (NodeModel node : parent.getChildNodeModels()) {
            /* Find out type and call the according node */
            if (node instanceof StartNodeModel) {
                nodes
                        .appendChild(createStartElement(doc,
                                (StartNodeModel) node));
            } else if (node instanceof EndNodeModel) {
                nodes.appendChild(createEndElement(doc, (WorkflowModel) parent,
                        (EndNodeModel) node));
            } else if (node instanceof EmailInvokeNodeModel) {
                nodes.appendChild(createEmailElement(doc,
                        (EmailInvokeNodeModel) node));
            } else if (node instanceof HumanTaskInvokeNodeModel) {
                nodes.appendChild(createHumanTaskElement(doc, workflow,
                        (HumanTaskInvokeNodeModel) node));
            } else if (node instanceof FlowContainerModel) {
                nodes.appendChild(createFlowElement(doc, workflow,
                        (FlowContainerModel) node));
            } else if (node instanceof IfContainerModel) {
                nodes.appendChild(createIfElement(doc, workflow,
                        (IfContainerModel) node));
            } else if (node instanceof ForEachContainerModel) {
                nodes.appendChild(createForEachElement(doc, workflow,
                        (ForEachContainerModel) node));
            }
        }
        return nodes;
    }

    private Element createStartElement(Document doc, StartNodeModel node) {
        Element startElement = doc.createElement(DWDLNames.startNode);
        startElement.setAttribute(DWDLNames.name, node.getName());
        startElement.setAttribute(DWDLNames.id, node.getId().toString());
        startElement.appendChild(createTextElement(doc, DWDLNames.description,
                node.getDescription()));
        startElement.appendChild(createGraphicsElement(doc, node));
        /* start node is only source to connections */
        startElement.appendChild(createSourceElement(doc, node));

        return startElement;
    }

    private Element createEndElement(Document doc, WorkflowModel model,
            EndNodeModel node) {
        Element endElement = doc.createElement(DWDLNames.endNode);
        endElement.setAttribute(DWDLNames.name, node.getName());
        endElement.setAttribute(DWDLNames.id, node.getId().toString());
        endElement.appendChild(createTextElement(doc, DWDLNames.description,
                node.getDescription()));
        endElement.appendChild(createGraphicsElement(doc, node));
        /* end node is only target to connections */
        endElement.appendChild(createTargetElement(doc, node));

        /* notification of success and recipient, create only if set to true */
        if (model.getProperties().getNotifyOnSuccess()) {
            Element notification = doc
                    .createElement(DWDLNames.notificationOfSuccess);
            notification.appendChild(createPropertyElement(doc,
                    DWDLNames.successMsg, model.getProperties()
                            .getSuccessMessageVariableId()));
            notification.appendChild(createPropertyElement(doc,
                    DWDLNames.recipient, model.getProperties()
                            .getRecipientVariableId()));
        }

        return endElement;
    }

    private Element createEmailElement(Document doc, EmailInvokeNodeModel node) {
        Element emailElement = doc.createElement(DWDLNames.invokeNode);
        emailElement.setAttribute(DWDLNames.name, node.getName());
        emailElement.setAttribute(DWDLNames.id, node.getId().toString());
        emailElement.setAttribute(DWDLNames.activity, DWDLNames.decidrEmail);

        emailElement.appendChild(createTextElement(doc, DWDLNames.description,
                node.getDescription()));

        emailElement.appendChild(createGraphicsElement(doc, node));
        emailElement.appendChild(createSourceElement(doc, node));
        emailElement.appendChild(createTargetElement(doc, node));
        emailElement.appendChild(createPropertyElement(doc, DWDLNames.to, node
                .getToVariableId()));
        emailElement.appendChild(createPropertyElement(doc, DWDLNames.cc, node
                .getCcVariableId()));
        emailElement.appendChild(createPropertyElement(doc, DWDLNames.bcc, node
                .getBccVariableId()));
        emailElement.appendChild(createPropertyElement(doc, DWDLNames.subject,
                node.getSubjectVariableId()));
        emailElement.appendChild(createPropertyElement(doc, DWDLNames.message,
                node.getMessageVariableId()));
        emailElement.appendChild(createPropertyElement(doc,
                DWDLNames.attachment, node.getAttachmentVariableId()));
        return emailElement;
    }

    private Element createHumanTaskElement(Document doc,
            WorkflowModel workflow, HumanTaskInvokeNodeModel node) {
        Element humanTaskElement = doc.createElement(DWDLNames.invokeNode);
        humanTaskElement.setAttribute(DWDLNames.name, node.getName());
        humanTaskElement.setAttribute(DWDLNames.id, node.getId().toString());
        humanTaskElement.setAttribute(DWDLNames.activity,
                DWDLNames.decidrHumanTask);

        humanTaskElement.appendChild(createTextElement(doc,
                DWDLNames.description, node.getDescription()));

        humanTaskElement.appendChild(createGraphicsElement(doc, node));
        humanTaskElement.appendChild(createTargetElement(doc, node));
        humanTaskElement.appendChild(createSourceElement(doc, node));

        /* Create property elements for user */
        humanTaskElement.appendChild(createPropertyElement(doc, DWDLNames.wfId,
                workflow.getId()));
        humanTaskElement.appendChild(createPropertyElement(doc, DWDLNames.user,
                node.getUserVariableId()));

        /*
         * Create set property for user notification variable. Cannot use
         * createPropertyElement method here because it does not take into
         * account that boolean values have to be converted to "yes"/"no".
         */
        Element userNotification = doc.createElement(DWDLNames.setProperty);
        userNotification.setAttribute(DWDLNames.name,
                DWDLNames.userNotification);
        String valueText;
        if (node.getNotify()) {
            valueText = DWDLNames.yes;
        } else {
            valueText = DWDLNames.no;
        }
        userNotification.appendChild(createTextElement(doc,
                DWDLNames.propertyValue, valueText));
        humanTaskElement.appendChild(userNotification);

        /* Create get property of human task */
        Element getProperty = doc.createElement(DWDLNames.getProperty);
        getProperty.setAttribute(DWDLNames.name, DWDLNames.taskResult);
        getProperty.setAttribute(DWDLNames.variable, node.getFormVariableId()
                .toString());
        Element humanTaskData = doc.createElement(DWDLNames.humanTaskData);

        /* Create task items */
        for (TaskItem ti : node.getTaskItems()) {
            Element taskItem = doc.createElement(DWDLNames.taskItem);
            Variable variable = VariablesFilter.getVariableById(ti
                    .getVariableId());
            taskItem.setAttribute(DWDLNames.name, "name");
            taskItem.setAttribute(DWDLNames.variable, variable.getId()
                    .toString());
            taskItem.setAttribute(DWDLNames.type, variable.getType()
                    .getDwdlName());
            taskItem.appendChild(createTextElement(doc, DWDLNames.label, ti
                    .getLabel()));
            taskItem.appendChild(createTextElement(doc, DWDLNames.value,
                    variable.getValues().get(0)));
            humanTaskData.appendChild(taskItem);
        }

        /* Append human task data to parameter element */
        Element parameters = doc.createElement(DWDLNames.parameters);
        parameters.appendChild(humanTaskData);
        getProperty.appendChild(parameters);

        humanTaskElement.appendChild(getProperty);
        return humanTaskElement;
    }

    private Element createFlowElement(Document doc, WorkflowModel workflow,
            FlowContainerModel node) {
        Element flowElement = doc.createElement(DWDLNames.flowNode);
        flowElement.appendChild(createTextElement(doc, DWDLNames.description,
                node.getDescription()));
        flowElement.appendChild(createGraphicsElement(doc, node));
        flowElement.appendChild(createTargetElement(doc, node));
        flowElement.appendChild(createSourceElement(doc, node));
        flowElement.appendChild(createChildNodeElements(doc, workflow, node));
        flowElement.appendChild(createArcElements(doc, node));
        return flowElement;
    }

    private Element createIfElement(Document doc, WorkflowModel workflow,
            IfContainerModel node) {
        Element ifElement = doc.createElement(DWDLNames.ifNode);
        ifElement.appendChild(createTextElement(doc, DWDLNames.description,
                node.getDescription()));
        ifElement.appendChild(createGraphicsElement(doc, node));
        ifElement.appendChild(createTargetElement(doc, node));
        ifElement.appendChild(createSourceElement(doc, node));

        /*
         * Create condition elements. The dwdl schema requires that the
         * condition elements have to be created in the specific order in which
         * the are later executed. To do that, first gather all connections
         * which are conditions and store them in a hashmap with their order as
         * key. Second, "Iterate" over hashmap to create the condition elements.
         */
        HashMap<Integer, Condition> conditions = new HashMap<Integer, Condition>();
        Integer highestKey = 0;
        for (ConnectionModel connectionModel : node.getChildConnectionModels()) {
            if (connectionModel instanceof Condition) {
                Condition condition = (Condition) connectionModel;
                conditions.put(condition.getOrder(), condition);
                if (condition.getOrder() > highestKey) {
                    highestKey = condition.getOrder();
                }
            }
        }
        for (int i = 0; i < highestKey; i++) {

            // JS default condition has no operators, check with if window
            Condition conditionModel = (Condition) conditions.get(i);
            Element conditionElement = doc.createElement(DWDLNames.condition);
            /*
             * If order is zero, that means the condition is the default
             * condition
             */
            if (i == 0) {
                conditionElement.setAttribute(DWDLNames.defaultCondition,
                        DWDLNames.yes);
            } else {
                conditionElement.setAttribute(DWDLNames.defaultCondition,
                        DWDLNames.no);
            }

            conditionElement.appendChild(createTextElement(doc,
                    DWDLNames.leftOp, conditionModel.getLeftOperandId()
                            .toString()));
            conditionElement.appendChild(createTextElement(doc,
                    DWDLNames.operator, conditionModel.getOperator()
                            .getDisplayString()));
            conditionElement.appendChild(createTextElement(doc,
                    DWDLNames.rightOp, conditionModel.getRightOperandId()
                            .toString()));
            conditionElement.appendChild(createChildNodeElements(doc, workflow,
                    node));
            conditionElement.appendChild(createArcElements(doc, node));
            ifElement.appendChild(conditionElement);

        }
        return ifElement;
    }

    private Element createForEachElement(Document doc, WorkflowModel workflow,
            ForEachContainerModel node) {
        Element forEachElement = doc.createElement(DWDLNames.forEachNode);
        forEachElement.setAttribute(DWDLNames.countername, node
                .getIterationVariableId().toString());
        if (node.isParallel()) {
            forEachElement.setAttribute(DWDLNames.parallel, DWDLNames.yes);
        } else {
            forEachElement.setAttribute(DWDLNames.parallel, DWDLNames.no);
        }
        forEachElement.appendChild(createTextElement(doc,
                DWDLNames.description, node.getDescription()));
        forEachElement.appendChild(createGraphicsElement(doc, node));
        forEachElement.appendChild(createTargetElement(doc, node));
        forEachElement.appendChild(createSourceElement(doc, node));

        /*
         * Creating counter values and completion condition elements. The start
         * counter value is always set to 1.
         */
        forEachElement.appendChild(createTextElement(doc,
                DWDLNames.startCounterValue, "1"));
        forEachElement.appendChild(createTextElement(doc,
                DWDLNames.finalCounterValue, node.getIterationVariableId()
                        .toString()));
        forEachElement.appendChild(createTextElement(doc,
                DWDLNames.completionCon, node.getExitCondition().toString()));

        forEachElement
                .appendChild(createChildNodeElements(doc, workflow, node));
        forEachElement.appendChild(createArcElements(doc, node));

        return forEachElement;

    }

    private Element createGraphicsElement(Document doc, NodeModel node) {
        Element graphics = doc.createElement(DWDLNames.graphics);
        /*
         * the position of a node is defined by the x and y coordinates of the
         * top left pixel
         */
        graphics.setAttribute(DWDLNames.x, ((Integer) node.getNode()
                .getGraphicLeft()).toString());
        graphics.setAttribute(DWDLNames.y, ((Integer) node.getNode()
                .getGraphicTop()).toString());
        /* width and height of the node */
        graphics.setAttribute(DWDLNames.width, ((Integer) node.getNode()
                .getGraphicWidth()).toString());
        graphics.setAttribute(DWDLNames.height, ((Integer) node.getNode()
                .getGraphicHeight()).toString());
        return graphics;
    }

    /*
     * The created element looks like this: <tagName>data</tagName>
     */
    private Element createTextElement(Document doc, String tagName, String data) {

        Element element = doc.createElement(tagName);
        Text text = doc.createTextNode(data);
        element.appendChild(text);
        return element;
    }

    /*
     * The created element looks like this: <setProperty name="<name>"
     * variable="<variableID>"> <propertyValue> variableID.value
     * </propertyValue> </setProperty>
     */
    private Element createPropertyElement(Document doc, String name,
            Long variableId) {
        Element property = doc.createElement(DWDLNames.setProperty);
        property.setAttribute(DWDLNames.name, name);
        property.setAttribute(DWDLNames.variable, variableId.toString());
        return property;
    }

    private Element createSourceElement(Document doc, NodeModel node) {
        Element sources = doc.createElement(DWDLNames.sources);
        Element source = doc.createElement(DWDLNames.source);
        /*
         * Source means: the node is source of a connection. So get the id of
         * the connection which is attached to the output port of the node.
         */
        source.setAttribute(DWDLNames.arcId, node.getOutput().getId()
                .toString());
        sources.appendChild(source);
        return sources;
    }

    private Element createTargetElement(Document doc, NodeModel node) {
        Element targets = doc.createElement(DWDLNames.targets);
        Element target = doc.createElement(DWDLNames.target);
        /*
         * Target means: the node is target of a connection. So get the id of
         * the connection which is attached to the input port of the node.
         */
        target
                .setAttribute(DWDLNames.arcId, node.getInput().getId()
                        .toString());
        targets.appendChild(target);
        return targets;
    }

    private Element createArcElements(Document doc, HasChildModels node) {
        Element arcs = doc.createElement(DWDLNames.arcs);
        for (ConnectionModel con : node.getChildConnectionModels()) {
            Element arc = doc.createElement(DWDLNames.arc);
            arc.setAttribute(DWDLNames.name, con.getName());
            arc.setAttribute(DWDLNames.id, con.getId().toString());
            arc.setAttribute(DWDLNames.source, con.getSource().getId()
                    .toString());
            arc.setAttribute(DWDLNames.target, con.getTarget().getId()
                    .toString());
            arcs.appendChild(arc);
        }
        return arcs;
    }

}
