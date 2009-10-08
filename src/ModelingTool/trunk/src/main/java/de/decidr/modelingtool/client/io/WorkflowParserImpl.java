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
    public String parse(WorkflowModel model) {
        Document doc = XMLParser.createDocument();

        /* Create workflow root element and set attributes */
        Element workflowElement = doc.createElement(DWDLNames.root);
        workflowElement.setAttribute(DWDLNames.name, model.getName());
        workflowElement.setAttribute(DWDLNames.id, model.getId().toString());
        workflowElement.setAttribute(DWDLNames.namespace, model.getProperties()
                .getNamespace());
        workflowElement.setAttribute(DWDLNames.schema, model.getProperties()
                .getSchema());

        /* Create description node */
        workflowElement.appendChild(createTextElement(doc,
                DWDLNames.description, model.getDescription()));

        /* Create variable and role nodes */
        createVariablesAndRoles(doc, workflowElement, model);

        /* Create fault handler node */
        workflowElement.appendChild(createFaultHandlerElement(doc, model));

        /* Create container and invoke nodes */
        workflowElement.appendChild(createChildNodeElements(doc, model, model));

        /* Create arcs */
        workflowElement.appendChild(createArcElements(doc, model));

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
        /* For variableNCNamePrefix, see comment in createVariables() */
        roleElement.setAttribute(DWDLNames.name, DWDLNames.variableNCnamePrefix
                + role.getId());

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
            WorkflowModel workflow, HasChildModels parent) {
        /*
         * Create nodes element which contains all child nodes of a workflow or
         * a container
         */
        Element nodes = doc.createElement(DWDLNames.nodes);
        for (NodeModel nodeModel : parent.getChildNodeModels()) {
            /* Find out type and call the according node */
            if (nodeModel instanceof StartNodeModel) {
                nodes.appendChild(createStartElement(doc,
                        (StartNodeModel) nodeModel));
            } else if (nodeModel instanceof EndNodeModel) {
                nodes.appendChild(createEndElement(doc, (WorkflowModel) parent,
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
        return nodes;
    }

    private Element createStartElement(Document doc, StartNodeModel model) {
        Element startElement = doc.createElement(DWDLNames.startNode);
        startElement.setAttribute(DWDLNames.name, model.getName());
        startElement.setAttribute(DWDLNames.id, model.getId().toString());
        startElement.appendChild(createTextElement(doc, DWDLNames.description,
                model.getDescription()));
        startElement.appendChild(createGraphicsElement(doc, model));
        /* start node is only source to connections */
        startElement.appendChild(createSourceElement(doc, model));

        return startElement;
    }

    private Element createEndElement(Document doc, WorkflowModel workflow,
            EndNodeModel model) {
        Element endElement = doc.createElement(DWDLNames.endNode);
        endElement.setAttribute(DWDLNames.name, model.getName());
        endElement.setAttribute(DWDLNames.id, model.getId().toString());
        endElement.appendChild(createTextElement(doc, DWDLNames.description,
                model.getDescription()));
        endElement.appendChild(createGraphicsElement(doc, model));
        /* end node is only target to connections */
        endElement.appendChild(createTargetElement(doc, model));

        /* notification of success and recipient, create only if set to true */
        if (workflow.getProperties().getNotifyOnSuccess()) {
            Element notification = doc
                    .createElement(DWDLNames.notificationOfSuccess);
            notification.appendChild(createPropertyElement(doc,
                    DWDLNames.successMsg, workflow.getProperties()
                            .getSuccessMessageVariableId()));
            notification.appendChild(createPropertyElement(doc,
                    DWDLNames.recipient, workflow.getProperties()
                            .getRecipientVariableId()));
        }

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
        emailElement.appendChild(createSourceElement(doc, model));
        emailElement.appendChild(createTargetElement(doc, model));
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
        humanTaskElement.appendChild(createTargetElement(doc, model));
        humanTaskElement.appendChild(createSourceElement(doc, model));

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
        if (model.getNotifyActor()) {
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
        getProperty.setAttribute(DWDLNames.variable,
                DWDLNames.variableNCnamePrefix + model.getFormVariableId());
        Element humanTaskData = doc.createElement(DWDLNames.humanTaskData);

        /* Create task items */
        for (TaskItem ti : model.getTaskItems()) {
            Element taskItem = doc.createElement(DWDLNames.taskItem);
            Variable variable = Workflow.getInstance().getModel().getVariable(
                    ti.getVariableId());
            taskItem.setAttribute(DWDLNames.name, "taskItem");
            taskItem.setAttribute(DWDLNames.variable,
                    DWDLNames.variableNCnamePrefix + variable.getId());
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

        /* Append human task data to parameter element */
        Element parameters = doc.createElement(DWDLNames.parameters);
        parameters.appendChild(humanTaskData);
        getProperty.appendChild(parameters);

        humanTaskElement.appendChild(getProperty);
        return humanTaskElement;
    }

    private Element createFlowElement(Document doc, WorkflowModel workflow,
            FlowContainerModel model) {
        Element flowElement = doc.createElement(DWDLNames.flowNode);
        flowElement.appendChild(createTextElement(doc, DWDLNames.description,
                model.getDescription()));
        flowElement.appendChild(createGraphicsElement(doc, model));
        flowElement.appendChild(createTargetElement(doc, model));
        flowElement.appendChild(createSourceElement(doc, model));
        flowElement.appendChild(createChildNodeElements(doc, workflow, model));
        flowElement.appendChild(createArcElements(doc, model));
        return flowElement;
    }

    private Element createIfElement(Document doc, WorkflowModel workflow,
            IfContainerModel model) {
        Element ifElement = doc.createElement(DWDLNames.ifNode);
        ifElement.appendChild(createTextElement(doc, DWDLNames.description,
                model.getDescription()));
        ifElement.appendChild(createGraphicsElement(doc, model));
        ifElement.appendChild(createTargetElement(doc, model));
        ifElement.appendChild(createSourceElement(doc, model));

        /*
         * Create condition elements. The dwdl schema requires that the
         * condition elements have to be created in the specific order in which
         * the are later executed. To do that, first gather all connections
         * which are conditions and store them in a hashmap with their order as
         * key. Second, "Iterate" over hashmap to create the condition elements.
         */
        HashMap<Integer, Condition> conditions = new HashMap<Integer, Condition>();
        Integer highestKey = 0;
        for (ConnectionModel connectionModel : model.getChildConnectionModels()) {
            if (connectionModel instanceof Condition) {
                Condition condition = (Condition) connectionModel;
                conditions.put(condition.getOrder(), condition);
                if (condition.getOrder() > highestKey) {
                    highestKey = condition.getOrder();
                }
            }
        }
        for (int i = 0; i < highestKey; i++) {
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
                conditionElement.appendChild(createTextElement(doc,
                        DWDLNames.leftOp, conditionModel.getLeftOperandId()
                                .toString()));
                conditionElement.appendChild(createTextElement(doc,
                        DWDLNames.operator, conditionModel.getOperator()
                                .getDisplayString()));
                conditionElement.appendChild(createTextElement(doc,
                        DWDLNames.rightOp, conditionModel.getRightOperandId()
                                .toString()));
                conditionElement.appendChild(createChildNodeElements(doc,
                        workflow, model));
                conditionElement.appendChild(createArcElements(doc, model));
            }
            ifElement.appendChild(conditionElement);
        }
        return ifElement;
    }

    private Element createForEachElement(Document doc, WorkflowModel workflow,
            ForEachContainerModel model) {
        Element forEachElement = doc.createElement(DWDLNames.forEachNode);
        forEachElement.setAttribute(DWDLNames.countername, model
                .getIterationVariableId().toString());
        if (model.isParallel()) {
            forEachElement.setAttribute(DWDLNames.parallel, DWDLNames.yes);
        } else {
            forEachElement.setAttribute(DWDLNames.parallel, DWDLNames.no);
        }
        forEachElement.appendChild(createTextElement(doc,
                DWDLNames.description, model.getDescription()));
        forEachElement.appendChild(createGraphicsElement(doc, model));
        forEachElement.appendChild(createTargetElement(doc, model));
        forEachElement.appendChild(createSourceElement(doc, model));

        /*
         * Creating counter values and completion condition elements. The start
         * counter value is always set to 1.
         */
        forEachElement.appendChild(createTextElement(doc,
                DWDLNames.startCounterValue, "1"));
        forEachElement.appendChild(createTextElement(doc,
                DWDLNames.finalCounterValue, model.getIterationVariableId()
                        .toString()));
        forEachElement.appendChild(createTextElement(doc,
                DWDLNames.completionCon, model.getExitCondition().toString()));

        forEachElement
                .appendChild(createChildNodeElements(doc, workflow, model));
        forEachElement.appendChild(createArcElements(doc, model));

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
            property.setAttribute(DWDLNames.variable,
                    DWDLNames.variableNCnamePrefix + variableId.toString());
        }
        return property;
    }

    private Element createSourceElement(Document doc, NodeModel nodeModel) {
        Element sources = doc.createElement(DWDLNames.sources);
        Element source = doc.createElement(DWDLNames.source);
        /*
         * Source means: the node is source of a connection. So get the id of
         * the connection which is attached to the output port of the node.
         */
        source.setAttribute(DWDLNames.arcId, nodeModel.getOutput().getId()
                .toString());
        sources.appendChild(source);
        return sources;
    }

    private Element createTargetElement(Document doc, NodeModel nodeModel) {
        Element targets = doc.createElement(DWDLNames.targets);
        Element target = doc.createElement(DWDLNames.target);
        /*
         * Target means: the node is target of a connection. So get the id of
         * the connection which is attached to the input port of the node.
         */
        target.setAttribute(DWDLNames.arcId, nodeModel.getInput().getId()
                .toString());
        targets.appendChild(target);
        return targets;
    }

    private Element createArcElements(Document doc, HasChildModels parent) {
        Element arcs = doc.createElement(DWDLNames.arcs);
        for (ConnectionModel con : parent.getChildConnectionModels()) {
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
