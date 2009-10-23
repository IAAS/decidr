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

import java.util.Collection;

import com.google.gwt.core.client.GWT;
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
import de.decidr.modelingtool.client.model.foreach.ExitCondition;
import de.decidr.modelingtool.client.model.foreach.ForEachContainerModel;
import de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel;
import de.decidr.modelingtool.client.model.humantask.TaskItem;
import de.decidr.modelingtool.client.model.ifcondition.Condition;
import de.decidr.modelingtool.client.model.ifcondition.IfContainerModel;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;
import de.decidr.modelingtool.client.ui.Workflow;

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
    public String parse(WorkflowModel workflow) {
        Document doc = XMLParser.createDocument();

        /* Create workflow root element and set attributes */
        Element workflowElement = doc.createElement(DWDLNames.root);
        workflowElement.setAttribute(DWDLNames.name, workflow.getName());
        workflowElement.setAttribute(DWDLNames.id, workflow.getId().toString());
        workflowElement.setAttribute(DWDLNames.namespace, workflow
                .getProperties().getNamespace());
        workflowElement.setAttribute(DWDLNames.schema, workflow.getProperties()
                .getSchema());

        /* Create description node */
        workflowElement.appendChild(createTextElement(doc,
                DWDLNames.description, workflow.getDescription()));

        /* Create variable and role nodes */
        createVariablesAndRoles(doc, workflowElement, workflow);

        /* Create fault handler node */
        workflowElement.appendChild(createFaultHandlerElement(doc, workflow));

        /* Create container and invoke nodes */
        workflowElement.appendChild(createChildNodeElements(doc, workflow,
                workflow, workflow.getChildNodeModels()));

        /* Create arcs */
        workflowElement.appendChild(createArcElements(doc, workflow, workflow
                .getChildConnectionModels()));

        /* append tree to root element */
        doc.appendChild(workflowElement);
        return doc.toString();
    }

    private void createVariablesAndRoles(Document doc, Element parent,
            WorkflowModel workflow) {
        /*
         * Because a role is just a variable of the type role, it is easier to
         * create both variables and roles node here and append it to the root
         * element.
         */
        Element variables = doc.createElement(DWDLNames.variables);
        Element roles = doc.createElement(DWDLNames.roles);
        for (Variable var : workflow.getVariables()) {
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
        variableElement.setAttribute(DWDLNames.name, VariableNameFactory
                .createNCNameFromId(variable.getId()));

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
        /* For variableNCNamePrefix, see comment in createVariables() */
        roleElement.setAttribute(DWDLNames.name, VariableNameFactory
                .createNCNameFromId(role.getId()));

        /* Append the values of the role variables as actors */
        for (String value : role.getValues()) {
            Element actor = doc.createElement(DWDLNames.actor);
            actor.setAttribute(DWDLNames.userId, value);
            roleElement.appendChild(actor);
        }
        return roleElement;
    }

    private Element createFaultHandlerElement(Document doc,
            WorkflowModel workflow) {
        Element faultHandler = doc.createElement(DWDLNames.faultHandler);

        /* Create the property node for the fault message */
        faultHandler.appendChild(createPropertyElement(doc, DWDLNames.message,
                workflow.getProperties().getFaultMessageVariableId()));

        /* Create the property node for the recipient */
        Element recipient = doc.createElement(DWDLNames.recipient);
        recipient.appendChild(createPropertyElement(doc, DWDLNames.name,
                workflow.getProperties().getRecipientVariableId()));
        faultHandler.appendChild(recipient);

        return faultHandler;
    }

    private Element createChildNodeElements(Document doc,
            WorkflowModel workflow, HasChildModels parent,
            Collection<NodeModel> childModels) {
        Element nodes = doc.createElement(DWDLNames.nodes);
        GWT.log("Creating " + childModels.size() + " child nodes for "
                + parent.toString(), null);

        for (NodeModel nodeModel : childModels) {
            /* Find out type and call the according node */
            if (nodeModel instanceof StartNodeModel) {
                nodes.appendChild(createStartElement(doc,
                        (StartNodeModel) nodeModel));
            } else if (nodeModel instanceof EndNodeModel) {
                nodes.appendChild(createEndElement(doc, workflow,
                        (EndNodeModel) nodeModel));
            } else if (nodeModel instanceof EmailInvokeNodeModel) {
                nodes.appendChild(createEmailElement(doc,
                        (EmailInvokeNodeModel) nodeModel));
            } else if (nodeModel instanceof HumanTaskInvokeNodeModel) {
                nodes.appendChild(createHumanTaskElement(doc, workflow,
                        (HumanTaskInvokeNodeModel) nodeModel));
            } else if (nodeModel instanceof FlowContainerModel) {
                nodes.appendChild(createFlowElement(doc, workflow,
                        (FlowContainerModel) nodeModel));
            } else if (nodeModel instanceof IfContainerModel) {
                nodes.appendChild(createIfElement(doc, workflow,
                        (IfContainerModel) nodeModel));
            } else if (nodeModel instanceof ForEachContainerModel) {
                nodes.appendChild(createForEachElement(doc, workflow,
                        (ForEachContainerModel) nodeModel));
            }
        }

        GWT.log("Finished creating child nodes for " + parent.toString(), null);
        return nodes;
    }

    private Element createStartElement(Document doc, StartNodeModel model) {
        GWT.log("Creating StartNode", null);

        Element startElement = doc.createElement(DWDLNames.startNode);
        startElement.setAttribute(DWDLNames.name, model.getName());
        startElement.setAttribute(DWDLNames.id, model.getId().toString());
        startElement.appendChild(createTextElement(doc, DWDLNames.description,
                model.getDescription()));
        startElement.appendChild(createGraphicsElement(doc, model));

        /*
         * start node is only source to connections, create source element only
         * if connection exists
         */
        if (model.getOutput() != null) {
            startElement.appendChild(createSourceElement(doc, model));
        }

        GWT.log("Finished creating StartNode", null);
        return startElement;
    }

    private Element createEndElement(Document doc, WorkflowModel workflow,
            EndNodeModel model) {
        GWT.log("Creating EndNode", null);

        Element endElement = doc.createElement(DWDLNames.endNode);
        endElement.setAttribute(DWDLNames.name, model.getName());
        endElement.setAttribute(DWDLNames.id, model.getId().toString());
        endElement.appendChild(createTextElement(doc, DWDLNames.description,
                model.getDescription()));
        endElement.appendChild(createGraphicsElement(doc, model));

        /*
         * end node is only target to connections, create target element only if
         * connection exists
         */
        if (model.getInput() != null) {
            endElement.appendChild(createTargetElement(doc, model));
        }

        /* notification of success and recipient, create only if set to true */
        if (workflow.getProperties().getNotifyOnSuccess()) {
            GWT.log("Creating notification of success node", null);
            Element notification = doc
                    .createElement(DWDLNames.notificationOfSuccess);
            notification.appendChild(createPropertyElement(doc,
                    DWDLNames.successMsg, workflow.getProperties()
                            .getSuccessMessageVariableId()));
            notification.appendChild(createPropertyElement(doc,
                    DWDLNames.recipient, workflow.getProperties()
                            .getRecipientVariableId()));
            endElement.appendChild(notification);
        }

        GWT.log("Finished creating EndNode", null);
        return endElement;
    }

    private Element createEmailElement(Document doc, EmailInvokeNodeModel model) {
        Element emailElement = doc.createElement(DWDLNames.invokeNode);
        emailElement.setAttribute(DWDLNames.name, model.getName());
        emailElement.setAttribute(DWDLNames.id, model.getId().toString());
        emailElement.setAttribute(DWDLNames.activity, DWDLNames.decidrEmail);

        emailElement.appendChild(createTextElement(doc, DWDLNames.description,
                model.getDescription()));

        emailElement.appendChild(createGraphicsElement(doc, model));

        /* Create targets and source elements only if connections exist */
        if (model.getOutput() != null) {
            emailElement.appendChild(createSourceElement(doc, model));
        }
        if (model.getInput() != null) {
            emailElement.appendChild(createTargetElement(doc, model));
        }

        emailElement.appendChild(createPropertyElement(doc, DWDLNames.to, model
                .getToVariableId()));
        emailElement.appendChild(createPropertyElement(doc, DWDLNames.cc, model
                .getCcVariableId()));
        emailElement.appendChild(createPropertyElement(doc, DWDLNames.bcc,
                model.getBccVariableId()));
        emailElement.appendChild(createPropertyElement(doc, DWDLNames.subject,
                model.getSubjectVariableId()));
        emailElement.appendChild(createPropertyElement(doc, DWDLNames.message,
                model.getMessageVariableId()));
        emailElement.appendChild(createPropertyElement(doc,
                DWDLNames.attachment, model.getAttachmentVariableId()));
        return emailElement;
    }

    private Element createHumanTaskElement(Document doc,
            WorkflowModel workflow, HumanTaskInvokeNodeModel model) {
        Element humanTaskElement = doc.createElement(DWDLNames.invokeNode);
        humanTaskElement.setAttribute(DWDLNames.name, model.getName());
        humanTaskElement.setAttribute(DWDLNames.id, model.getId().toString());
        humanTaskElement.setAttribute(DWDLNames.activity,
                DWDLNames.decidrHumanTask);

        humanTaskElement.appendChild(createTextElement(doc,
                DWDLNames.description, model.getDescription()));

        humanTaskElement.appendChild(createGraphicsElement(doc, model));

        /* Create targets and source elements only if connections exist */
        if (model.getOutput() != null) {
            humanTaskElement.appendChild(createSourceElement(doc, model));
        }
        if (model.getInput() != null) {
            humanTaskElement.appendChild(createTargetElement(doc, model));
        }

        /* Create property elements for user */
        humanTaskElement.appendChild(createPropertyElement(doc, DWDLNames.wfId,
                workflow.getId()));
        humanTaskElement.appendChild(createPropertyElement(doc, DWDLNames.user,
                model.getUserVariableId()));
        humanTaskElement.appendChild(createPropertyElement(doc, DWDLNames.name,
                model.getWorkItemNameVariableId()));
        humanTaskElement
                .appendChild(createPropertyElement(doc, DWDLNames.description,
                        model.getWorkItemDescriptionVariableId()));

        /*
         * Create set property for user notification variable. Cannot use
         * createPropertyElement method here because it does not take into
         * account that boolean values have to be converted to "yes"/"no".
         */
        Element userNotification = doc.createElement(DWDLNames.setProperty);
        userNotification.setAttribute(DWDLNames.name,
                DWDLNames.userNotification);
        String valueText;
        if (model.getNotifyActor() != null && model.getNotifyActor()) {
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
        getProperty.setAttribute(DWDLNames.variable, VariableNameFactory
                .createNCNameFromId(model.getFormVariableId()));
        Element humanTaskData = doc.createElement(DWDLNames.humanTaskData);

        /* Create task items, if there are any */
        if (model.getTaskItems() != null) {
            for (TaskItem ti : model.getTaskItems()) {
                Element taskItem = doc.createElement(DWDLNames.taskItem);
                Variable variable = Workflow.getInstance().getModel()
                        .getVariable(ti.getVariableId());
                taskItem.setAttribute(DWDLNames.name, "taskItem");
                taskItem.setAttribute(DWDLNames.variable, VariableNameFactory
                        .createNCNameFromId(variable.getId()));
                taskItem.setAttribute(DWDLNames.type, variable.getType()
                        .getDwdlName());
                taskItem.appendChild(createTextElement(doc, DWDLNames.label, ti
                        .getLabel()));
                taskItem.appendChild(createTextElement(doc, DWDLNames.hint, ti
                        .getHint()));
                taskItem.appendChild(createTextElement(doc, DWDLNames.value,
                        variable.getValues().get(0)));
                humanTaskData.appendChild(taskItem);
            }
        }

        /* Append human task data to parameter element */
        Element parameters = doc.createElement(DWDLNames.parameters);
        parameters.appendChild(humanTaskData);
        getProperty.appendChild(parameters);

        humanTaskElement.appendChild(getProperty);
        return humanTaskElement;
    }

    private Element createFlowElement(Document doc, WorkflowModel workflow,
            FlowContainerModel model) {
        GWT.log("Creating FlowNode, children: "
                + model.getChildNodeModels().size() + ", parent: "
                + model.getParentModel().toString(), null);

        Element flowElement = doc.createElement(DWDLNames.flowNode);
        flowElement.setAttribute(DWDLNames.name, model.getName());
        flowElement.setAttribute(DWDLNames.id, model.getId().toString());

        flowElement.appendChild(createTextElement(doc, DWDLNames.description,
                model.getDescription()));
        flowElement.appendChild(createGraphicsElement(doc, model));

        /* Create targets and source elements only if connections exist */
        if (model.getOutput() != null) {
            flowElement.appendChild(createSourceElement(doc, model));
        }
        if (model.getInput() != null) {
            flowElement.appendChild(createTargetElement(doc, model));
        }

        /* Create child nodes and child connections if they exist */
        if (model.getChildNodeModels().size() > 0) {
            flowElement.appendChild(createChildNodeElements(doc, workflow,
                    model, model.getChildNodeModels()));
        }
        if (model.getChildConnectionModels().size() > 0) {
            flowElement.appendChild(createArcElements(doc, model, model
                    .getChildConnectionModels()));
        }

        GWT.log("Finished creating FlowNode", null);
        return flowElement;
    }

    private Element createIfElement(Document doc, WorkflowModel workflow,
            IfContainerModel model) {
        GWT.log("Creating IfNode, children: "
                + model.getChildNodeModels().size() + ", parent: "
                + model.getParentModel().toString(), null);
        GWT.log("IfNode has " + model.getConditions().size() + " conditions.",
                null);

        Element ifElement = doc.createElement(DWDLNames.ifNode);
        ifElement.setAttribute(DWDLNames.name, model.getName());
        ifElement.setAttribute(DWDLNames.id, model.getId().toString());

        ifElement.appendChild(createTextElement(doc, DWDLNames.description,
                model.getDescription()));
        ifElement.appendChild(createGraphicsElement(doc, model));

        /* Create targets and source elements only if connections exist */
        if (model.getOutput() != null) {
            ifElement.appendChild(createSourceElement(doc, model));
        }
        if (model.getInput() != null) {
            ifElement.appendChild(createTargetElement(doc, model));
        }

        /* Create every condition element */
        for (Condition condition : model.getConditions()) {
            Element conditionElement = doc.createElement(DWDLNames.condition);

            /*
             * Create the condition element itself, including order and
             * condition statement
             */
            if (condition.getOrder() != null) {
                conditionElement.setAttribute(DWDLNames.order, condition
                        .getOrder().toString());
                conditionElement.setAttribute(DWDLNames.id, condition.getId()
                        .toString());

                /*
                 * If order is zero, that means the condition is the default
                 * condition
                 */
                if (condition.getOrder() == 0) {
                    conditionElement.setAttribute(DWDLNames.defaultCondition,
                            DWDLNames.yes);
                } else {
                    conditionElement.setAttribute(DWDLNames.defaultCondition,
                            DWDLNames.no);
                }

                /* Create the condition statement */
                conditionElement.appendChild(createTextElement(doc,
                        DWDLNames.leftOp, condition.getLeftOperandId()
                                .toString()));
                conditionElement.appendChild(createTextElement(doc,
                        DWDLNames.operator, condition.getOperator()
                                .getDisplayString()));
                conditionElement.appendChild(createTextElement(doc,
                        DWDLNames.rightOp, condition.getRightOperandId()
                                .toString()));
            }

            /*
             * Create child nodes and child connections of the condition branch.
             * A condition branch consists of all nodes and connection which are
             * executed if the condition is true.
             */
            Collection<NodeModel> branchNodes = model
                    .getChildNodesOfCondition(condition);
            if (branchNodes.size() > 0) {
                conditionElement.appendChild(createChildNodeElements(doc,
                        workflow, model, branchNodes));
            }
            Collection<ConnectionModel> branchConnections = model
                    .getChildConnectionsOfCondition(condition);
            if (branchConnections.size() > 0) {
                conditionElement.appendChild(createArcElements(doc, model,
                        branchConnections));
            }

            ifElement.appendChild(conditionElement);
        }

        /*
         * Create child nodes and child connections which are not part of any
         * condition branch
         */
        Collection<NodeModel> branchlessNodes = model.getBranchlessChildNodes();
        if (branchlessNodes.size() > 0) {
            ifElement.appendChild(createChildNodeElements(doc, workflow, model,
                    branchlessNodes));
        }
        Collection<ConnectionModel> branchlessConnections = model
                .getBranchlessChildConnections();
        if (branchlessConnections.size() > 0) {
            ifElement.appendChild(createArcElements(doc, model,
                    branchlessConnections));
        }

        GWT.log("Finished creating IfNode", null);
        return ifElement;
    }

    private Element createForEachElement(Document doc, WorkflowModel workflow,
            ForEachContainerModel model) {
        GWT.log("Creating ForEachNode, children: "
                + model.getChildNodeModels().size() + ", parent: "
                + model.getParentModel().toString(), null);

        Element forEachElement = doc.createElement(DWDLNames.forEachNode);
        forEachElement.setAttribute(DWDLNames.name, model.getName());
        forEachElement.setAttribute(DWDLNames.id, model.getId().toString());

        forEachElement.appendChild(createTextElement(doc,
                DWDLNames.description, model.getDescription()));

        forEachElement.appendChild(createGraphicsElement(doc, model));

        /* Create targets and source elements only if connections exist */
        if (model.getOutput() != null) {
            forEachElement.appendChild(createSourceElement(doc, model));
        }
        if (model.getInput() != null) {
            forEachElement.appendChild(createTargetElement(doc, model));
        }

        if (model.getIterationVariableId() != null) {
            /*
             * Creating counter values and completion condition elements. The
             * start counter value is always set to 1.
             */
            forEachElement.setAttribute(DWDLNames.countername,
                    VariableNameFactory.createNCNameFromId(model
                            .getIterationVariableId()));
            forEachElement.appendChild(createTextElement(doc,
                    DWDLNames.startCounterValue, "1"));
            forEachElement
                    .appendChild(createTextElement(doc,
                            DWDLNames.finalCounterValue, VariableNameFactory
                                    .createNCNameFromId(model
                                            .getIterationVariableId())));
        }

        ExitCondition exitCondition = model.getExitCondition();
        if (exitCondition != null) {
            forEachElement.appendChild(createTextElement(doc,
                    DWDLNames.completionCon, exitCondition.toString()));

        }

        if (model.isParallel()) {
            forEachElement.setAttribute(DWDLNames.parallel, DWDLNames.yes);
        } else {
            forEachElement.setAttribute(DWDLNames.parallel, DWDLNames.no);
        }

        /* Create child nodes and child connections if they exist */
        if (model.getChildNodeModels().size() > 0) {
            forEachElement.appendChild(createChildNodeElements(doc, workflow,
                    model, model.getChildNodeModels()));
        }
        if (model.getChildConnectionModels().size() > 0) {
            forEachElement.appendChild(createArcElements(doc, model, model
                    .getChildConnectionModels()));
        }

        GWT.log("Finished creating ForEachNode", null);
        return forEachElement;
    }

    private Element createGraphicsElement(Document doc, NodeModel nodeModel) {
        Element graphics = doc.createElement(DWDLNames.graphics);
        /*
         * the position of a node is defined by the x and y coordinates of the
         * top left pixel
         */
        graphics.setAttribute(DWDLNames.x, ((Integer) nodeModel.getNode()
                .getGraphicLeft()).toString());
        graphics.setAttribute(DWDLNames.y, ((Integer) nodeModel.getNode()
                .getGraphicTop()).toString());
        /* width and height of the node */
        graphics.setAttribute(DWDLNames.width, ((Integer) nodeModel.getNode()
                .getGraphicWidth()).toString());
        graphics.setAttribute(DWDLNames.height, ((Integer) nodeModel.getNode()
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
     * variable="<NCnamePrefix + variableID>"> <propertyValue> variableID.value
     * </propertyValue> </setProperty>
     */
    private Element createPropertyElement(Document doc, String name,
            Long variableId) {
        Element property = doc.createElement(DWDLNames.setProperty);
        property.setAttribute(DWDLNames.name, name);
        if (variableId != null) {
            property.setAttribute(DWDLNames.variable, VariableNameFactory
                    .createNCNameFromId(variableId));
        }
        return property;
    }

    private Element createSourceElement(Document doc, NodeModel nodeModel) {
        GWT.log("Creating source element for " + nodeModel.getName(), null);

        Element sources = doc.createElement(DWDLNames.sources);
        Element source = doc.createElement(DWDLNames.source);
        /*
         * Source means: the node is source of a connection. So get the id of
         * the connection which is attached to the output port of the node.
         */
        source.setAttribute(DWDLNames.arcId, nodeModel.getOutput().getId()
                .toString());
        GWT.log("ArcID: " + nodeModel.getOutput().getId() + " Source: "
                + nodeModel.getOutput().getSource().getName() + " Target: "
                + nodeModel.getOutput().getTarget(), null);
        sources.appendChild(source);

        GWT.log("Finished creating source element", null);
        return sources;
    }

    private Element createTargetElement(Document doc, NodeModel nodeModel) {
        GWT.log("Creating target element for " + nodeModel.getName(), null);

        Element targets = doc.createElement(DWDLNames.targets);
        Element target = doc.createElement(DWDLNames.target);
        /*
         * Target means: the node is target of a connection. So get the id of
         * the connection which is attached to the input port of the node.
         */
        target.setAttribute(DWDLNames.arcId, nodeModel.getInput().getId()
                .toString());
        GWT.log("ArcID: " + nodeModel.getInput().getId() + " Source: "
                + nodeModel.getInput().getSource().getName() + " Target: "
                + nodeModel.getInput().getTarget(), null);
        targets.appendChild(target);

        GWT.log("Finished creating target element", null);
        return targets;
    }

    private Element createArcElements(Document doc, HasChildModels parent,
            Collection<ConnectionModel> connections) {
        Element arcs = doc.createElement(DWDLNames.arcs);
        GWT.log("Creating " + connections.size() + " arc elements for "
                + parent.toString(), null);

        for (ConnectionModel con : connections) {
            Element arc = doc.createElement(DWDLNames.arc);
            arc.setAttribute(DWDLNames.name, con.getName());
            arc.setAttribute(DWDLNames.id, con.getId().toString());
            arc.setAttribute(DWDLNames.source, con.getSource().getId()
                    .toString());
            arc.setAttribute(DWDLNames.target, con.getTarget().getId()
                    .toString());
            arcs.appendChild(arc);
        }

        GWT.log("Finished creating arc elements", null);
        return arcs;
    }

}
