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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

import de.decidr.modelingtool.client.io.resources.DWDLNames;
import de.decidr.modelingtool.client.model.ConnectionModel;
import de.decidr.modelingtool.client.model.ContainerExitConnectionModel;
import de.decidr.modelingtool.client.model.ContainerModel;
import de.decidr.modelingtool.client.model.ContainerStartConnectionModel;
import de.decidr.modelingtool.client.model.EmailInvokeNodeModel;
import de.decidr.modelingtool.client.model.EndNodeModel;
import de.decidr.modelingtool.client.model.FlowContainerModel;
import de.decidr.modelingtool.client.model.HasChildModels;
import de.decidr.modelingtool.client.model.NodeModel;
import de.decidr.modelingtool.client.model.StartNodeModel;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.model.WorkflowProperties;
import de.decidr.modelingtool.client.model.foreach.ExitCondition;
import de.decidr.modelingtool.client.model.foreach.ForEachContainerModel;
import de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel;
import de.decidr.modelingtool.client.model.humantask.TaskItem;
import de.decidr.modelingtool.client.model.ifcondition.Condition;
import de.decidr.modelingtool.client.model.ifcondition.IfContainerModel;
import de.decidr.modelingtool.client.model.ifcondition.Operator;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;

/**
 * The actual implementation of a dwdl parser.
 * 
 * @author Modood Alvi, Jonas Schlaak
 * @version 0.1
 */
public class DWDLParserImpl implements DWDLParser {

    private HashMap<Long, ConnectionModel> connectionModels;

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.modelingtool.client.io.DWDLParser#parse(java.lang.String)
     */
    @Override
    public WorkflowModel parse(String dwdl) {
        Document doc;
        doc = XMLParser.createDocument();
        doc = XMLParser.parse(dwdl);

        WorkflowModel workflow = new WorkflowModel();

        createWorkflowProperties(doc, workflow);

        /* Create variables and roles */
        List<Variable> variables = new ArrayList<Variable>();
        createVariables(doc, variables);
        createRoles(doc, variables);
        workflow.setVariables(variables);

        /* Create nodes */
        createChildNodeModels((Element) doc.getFirstChild(), workflow, workflow);

        return workflow;
    }

    private void createWorkflowProperties(Document doc, WorkflowModel workflow) {
        Element root = (Element) doc.getElementsByTagName(DWDLNames.root).item(
                0);

        /* Set workflow name, id and description */
        workflow.setName(root.getAttribute(DWDLNames.name));
        workflow.setId(new Long(root.getAttribute(DWDLNames.id)));
        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            /*
             * Go through all child nodes until node with name "description" is
             * found
             */
            Node childNode = root.getChildNodes().item(i);
            if (childNode.getNodeName().equals(DWDLNames.description)
                    && childNode.getFirstChild() != null) {
                workflow.setDescription(childNode.getFirstChild()
                        .getNodeValue());
            }
        }
        WorkflowProperties properties = new WorkflowProperties();

        /* Get namespace and schema */
        properties.setNamespace(root.getAttribute(DWDLNames.namespace));
        properties.setSchema(root.getAttribute(DWDLNames.schema));

        /* parse faultHandlerElement and set fault message id and recipient */
        Element faultHandler = getChildNodesByTagName(root,
                DWDLNames.faultHandler).get(0);
        properties.setFaultMessageVariableId(getVariableIdFromPropertyElement(
                faultHandler, DWDLNames.message));
        Element recipient = getChildNodesByTagName(faultHandler,
                DWDLNames.recipient).get(0);
        properties.setRecipientVariableId(getVariableIdFromPropertyElement(
                recipient, DWDLNames.name));

        /*
         * parse endNode and set success message and notification. endNode is a
         * child of the nodes element.
         */
        if (getChildNodesByTagName(
                getChildNodesByTagName(root, DWDLNames.nodes).get(0),
                DWDLNames.endNode) != null) {
            Element endNode = getChildNodesByTagName(
                    getChildNodesByTagName(root, DWDLNames.nodes).get(0),
                    DWDLNames.endNode).get(0);
            // notification of success is optional
            if (getChildNodesByTagName(endNode, DWDLNames.notificationOfSuccess)
                    .isEmpty() == false) {
                // if notification is existent, this attribute is always true
                properties.setNotifyOnSuccess(true);

                Element notification = getChildNodesByTagName(endNode,
                        DWDLNames.notificationOfSuccess).get(0);
                properties
                        .setSuccessMessageVariableId(getVariableIdFromPropertyElement(
                                notification, DWDLNames.successMsg));
            } else {
                properties.setNotifyOnSuccess(false);
            }
        }
        workflow.setProperties(properties);
    }

    private void createVariables(Document doc, List<Variable> variables) {

        /* Get the variables element */
        Element variablesElement = (Element) doc.getElementsByTagName(
                DWDLNames.variables).item(0);

        /* Go through all child elements with tag name variable */
        for (Element variableElement : getChildNodesByTagName(variablesElement,
                DWDLNames.variable)) {
            Variable variable = new Variable();

            /* Set id and label, get rid of the ncname prefix */
            variable.setId(VariableNameFactory
                    .createIdFromNCName(variableElement
                            .getAttribute(DWDLNames.name)));
            variable.setLabel(variableElement.getAttribute(DWDLNames.label));

            /* Set configuration */
            if (variableElement.getAttribute(DWDLNames.configVar).equals(
                    DWDLNames.yes)) {
                variable.setConfig(true);
            } else {
                variable.setConfig(false);
            }

            /* Set type, get rid of the list prefix */
            String typeString = new String(variableElement
                    .getAttribute(DWDLNames.type));
            /*
             * If we have to get rid of the list prefix, that also means the
             * variables has multiple values
             */
            boolean isArray = false;
            if (typeString.startsWith(DWDLNames.listprefix)) {
                typeString = typeString
                        .substring(DWDLNames.listprefix.length());
                isArray = true;
            }

            /*
             * Set the values, if the variable has multiple values (determined
             * in the previous step), then get rid of the wrapping parent node
             * "initialValues"
             */
            List<Element> valueElements = new ArrayList<Element>();
            if (isArray) {
                valueElements = getChildNodesByTagName(
                        (Element) variableElement.getFirstChild(),
                        DWDLNames.initValue);
            } else {
                valueElements = getChildNodesByTagName(variableElement,
                        DWDLNames.initValue);
            }
            for (Element valueElement : valueElements) {
                if (valueElement.getFirstChild().getNodeValue() != null) {
                    variable.getValues().add(
                            new String(valueElement.getFirstChild()
                                    .getNodeValue()));
                }
            }

            /* Add variable to model */
            variables.add(variable);
        }
    }

    private void createRoles(Document doc, List<Variable> variables) {

        /* Get the roles element */
        Element rolesElement = (Element) doc.getElementsByTagName(
                DWDLNames.roles).item(0);

        /* Go through all child elements */
        for (Element roleElement : getChildNodesByTagName(rolesElement,
                DWDLNames.role)) {
            Variable role = new Variable();

            /* Set id, name and type, get rid of variable ncname prefix */
            role.setId(VariableNameFactory.createIdFromNCName(roleElement
                    .getAttribute(DWDLNames.name)));
            role.setLabel(roleElement.getAttribute(DWDLNames.label));
            role.setType(VariableType.ROLE);

            /* Set configuration */
            if (roleElement.getAttribute(DWDLNames.configVar) == DWDLNames.yes) {
                role.setConfig(true);
            } else {
                role.setConfig(false);
            }

            /* Get actors */
            for (Element actor : getChildNodesByTagName(roleElement,
                    DWDLNames.actor)) {
                role.getValues().add(
                        new String(actor.getAttribute(DWDLNames.userId)));
            }

            /* Add role to model */
            variables.add(role);
        }
    }

    private Long getVariableIdFromPropertyElement(Element parent,
            String propertyName) {
        Long variableId = null;
        /*
         * Go through all child nodes which are properties. If the name
         * attribute of a property matches propertyName, that's the element we
         * want to get the variableId from.
         */
        for (Element property : getChildNodesByTagName(parent,
                DWDLNames.setProperty)) {
            if (property.getAttribute(DWDLNames.name).equals(propertyName)
                    && property.getAttribute(DWDLNames.variable) != null) {
                variableId = VariableNameFactory.createIdFromNCName(property
                        .getAttribute(DWDLNames.variable));
            }
        }
        return variableId;
    }

    private Collection<NodeModel> createChildNodeModels(Element parentElement,
            WorkflowModel workflow, HasChildModels parentModel) {
        /* This colletion will be returned */
        Collection<NodeModel> childNodeModels = new HashSet<NodeModel>();

        List<Element> children = getChildNodesByTagName(parentElement,
                DWDLNames.nodes);
        if (children.size() > 0) {
            Element nodesElement = children.get(0);
            List<Element> nodeElements = getChildElementsAsList(nodesElement);

            for (Element childElement : nodeElements) {

                NodeModel nodeModel = null;
                /*
                 * Check the name of the child nodes against the names of all
                 * know node type and call their creation method. In case the
                 * name is startnode or endnode it is safe to assume the parent
                 * element is a workflow element
                 */
                if (childElement.getNodeName().equals(DWDLNames.startNode)) {
                    nodeModel = createStartModel(childElement, workflow);
                } else if (childElement.getNodeName().equals(DWDLNames.endNode)) {
                    nodeModel = createEndModel(childElement, workflow);
                } else if (childElement.getNodeName()
                        .equals(DWDLNames.flowNode)) {
                    nodeModel = createFlowModel(childElement, workflow,
                            parentModel);
                } else if (childElement.getNodeName().equals(DWDLNames.ifNode)) {
                    nodeModel = createIfModel(childElement, workflow,
                            parentModel);
                } else if (childElement.getNodeName().equals(
                        DWDLNames.forEachNode)) {
                    nodeModel = createForEachModel(childElement, workflow,
                            parentModel);
                } else if (childElement.getNodeName().equals(
                        DWDLNames.invokeNode)) {
                    if (childElement.getAttribute(DWDLNames.activity).equals(
                            DWDLNames.decidrEmail)) {
                        nodeModel = createEmailInvokeNode(childElement,
                                parentModel);
                    } else if (childElement.getAttribute(DWDLNames.activity)
                            .equals(DWDLNames.decidrHumanTask)) {
                        nodeModel = createHumanTaskInvokeNode(childElement,
                                parentModel);
                    }
                }

                if (nodeModel != null) {
                    parentModel.addNodeModel(nodeModel);
                    childNodeModels.add(nodeModel);
                }
            }
        }

        return childNodeModels;
    }

    private StartNodeModel createStartModel(Element startElement,
            WorkflowModel workflow) {
        StartNodeModel startModel = new StartNodeModel(workflow);

        /* Set name, id and graphics */
        startModel.setName(startElement.getAttribute(DWDLNames.name));
        startModel.setId(new Long(startElement.getAttribute(DWDLNames.id)));
        setGraphics(startElement, startModel);

        /* Set outgoing connection */
        List<Element> sources = getChildNodesByTagName(startElement,
                DWDLNames.sources);
        if (sources.size() > 0) {
            Element sourcesElement = sources.get(0);
            Element sourceElement = getChildNodesByTagName(sourcesElement,
                    DWDLNames.source).get(0);
            startModel.setOutput(getConnectionForSourceElement(new Long(
                    sourceElement.getAttribute(DWDLNames.arcId)), startModel,
                    workflow));
        }

        return startModel;
    }

    private EndNodeModel createEndModel(Element endElement,
            WorkflowModel workflow) {
        EndNodeModel endModel = new EndNodeModel(workflow);

        /* Set name, id and graphics */
        endModel.setName(endElement.getAttribute(DWDLNames.name));
        endModel.setId(new Long(endElement.getAttribute(DWDLNames.id)));
        setGraphics(endElement, endModel);

        /* Set incoming connection */
        List<Element> targets = getChildNodesByTagName(endElement,
                DWDLNames.targets);
        if (targets.size() > 0) {
            Element targetsElement = targets.get(0);
            Element targetElement = getChildNodesByTagName(targetsElement,
                    DWDLNames.target).get(0);
            endModel.setInput(getConnectionForTargetElement(new Long(
                    targetElement.getAttribute(DWDLNames.arcId)), endModel,
                    workflow));
        }

        return endModel;
    }

    private EmailInvokeNodeModel createEmailInvokeNode(Element emailElement,
            HasChildModels parentModel) {
        EmailInvokeNodeModel emailModel = new EmailInvokeNodeModel(parentModel);

        /* Set name id and graphics */
        emailModel.setName(emailElement.getAttribute(DWDLNames.name));
        emailModel.setId(new Long(emailElement.getAttribute(DWDLNames.id)));
        setGraphics(emailElement, emailModel);

        /* Set properties of the email */
        emailModel.setToVariableId(getVariableIdFromPropertyElement(
                emailElement, DWDLNames.to));
        emailModel.setCcVariableId(getVariableIdFromPropertyElement(
                emailElement, DWDLNames.cc));
        emailModel.setBccVariableId(getVariableIdFromPropertyElement(
                emailElement, DWDLNames.bcc));
        emailModel.setSubjectVariableId(getVariableIdFromPropertyElement(
                emailElement, DWDLNames.subject));
        emailModel.setMessageVariableId(getVariableIdFromPropertyElement(
                emailElement, DWDLNames.message));
        emailModel.setAttachmentVariableId(getVariableIdFromPropertyElement(
                emailElement, DWDLNames.attachment));

        /* Set incoming and outgoing connections */
        List<Element> targets = getChildNodesByTagName(emailElement,
                DWDLNames.targets);
        if (targets.size() > 0) {
            Element targetsElement = targets.get(0);
            Element targetElement = getChildNodesByTagName(targetsElement,
                    DWDLNames.target).get(0);
            emailModel.setInput(getConnectionForTargetElement(new Long(
                    targetElement.getAttribute(DWDLNames.arcId)), emailModel,
                    parentModel));
        }
        List<Element> sources = getChildNodesByTagName(emailElement,
                DWDLNames.sources);
        if (sources.size() > 0) {
            Element sourcesElement = sources.get(0);
            Element sourceElement = getChildNodesByTagName(sourcesElement,
                    DWDLNames.source).get(0);
            emailModel.setOutput(getConnectionForSourceElement(new Long(
                    sourceElement.getAttribute(DWDLNames.arcId)), emailModel,
                    parentModel));
        }

        return emailModel;
    }

    private HumanTaskInvokeNodeModel createHumanTaskInvokeNode(
            Element humanTaskElement, HasChildModels parentModel) {
        HumanTaskInvokeNodeModel humanTaskModel = new HumanTaskInvokeNodeModel(
                parentModel);

        /* Set name id and graphics */
        humanTaskModel.setName(humanTaskElement.getAttribute(DWDLNames.name));
        humanTaskModel.setId(new Long(humanTaskElement
                .getAttribute(DWDLNames.id)));
        setGraphics(humanTaskElement, humanTaskModel);

        /* Set properties of the human task */
        humanTaskModel.setUserVariableId(getVariableIdFromPropertyElement(
                humanTaskElement, DWDLNames.user));
        humanTaskModel
                .setWorkItemNameVariableId(getVariableIdFromPropertyElement(
                        humanTaskElement, DWDLNames.name));
        humanTaskModel
                .setWorkItemDescriptionVariableId(getVariableIdFromPropertyElement(
                        humanTaskElement, DWDLNames.description));

        /* Get user notification property */
        for (Element propertyElement : getChildNodesByTagName(humanTaskElement,
                DWDLNames.setProperty)) {
            if (propertyElement.getAttribute(DWDLNames.name).equals(
                    DWDLNames.userNotification)) {
                String valueText = propertyElement.getFirstChild()
                        .getFirstChild().getNodeValue();
                if (valueText.equals(DWDLNames.yes)) {
                    humanTaskModel.setNotifyActor(true);
                } else {
                    humanTaskModel.setNotifyActor(false);
                }
            }
        }

        /*
         * Get the task items; first get the property element which name is task
         * result. Set the form variable id
         */
        Element taskResultElement = null;
        for (Element getPropertyElement : getChildNodesByTagName(
                humanTaskElement, DWDLNames.getProperty)) {
            if (getPropertyElement.getAttribute(DWDLNames.name).equals(
                    DWDLNames.taskResult)) {
                taskResultElement = getPropertyElement;
                humanTaskModel.setFormVariableId(VariableNameFactory
                        .createIdFromNCName(taskResultElement
                                .getAttribute(DWDLNames.variable)));
            }
        }

        /*
         * From task result element, get the child node parameters, and from
         * there the child node human task data
         */
        Element parametersElement = getChildNodesByTagName(taskResultElement,
                DWDLNames.parameters).get(0);
        Element humanTaskDataElement = getChildNodesByTagName(
                parametersElement, DWDLNames.humanTaskData).get(0);

        /* Get the task items, which are child nodes of human task data */
        List<Element> taskItemElements = getChildNodesByTagName(
                humanTaskDataElement, DWDLNames.taskItem);
        List<TaskItem> taskItems = new ArrayList<TaskItem>();
        for (Element taskItemElement : taskItemElements) {
            /*
             * Every task item node has to have a variable id as attribute and a
             * text child node with the label of the task item
             */
            Long variableId = VariableNameFactory
                    .createIdFromNCName(taskItemElement
                            .getAttribute(DWDLNames.variable));
            String label = new String(getChildNodesByTagName(taskItemElement,
                    DWDLNames.label).get(0).getFirstChild().getNodeValue());
            String hint = new String(getChildNodesByTagName(taskItemElement,
                    DWDLNames.hint).get(0).getFirstChild().getNodeValue());
            TaskItem taskitem = new TaskItem(label, hint, variableId);
            taskItems.add(taskitem);
        }
        humanTaskModel.setTaskItems(taskItems);

        /* Set incoming and outgoing connections */
        List<Element> targets = getChildNodesByTagName(humanTaskElement,
                DWDLNames.targets);
        if (targets.size() > 0) {
            Element targetsElement = targets.get(0);
            Element targetElement = getChildNodesByTagName(targetsElement,
                    DWDLNames.target).get(0);
            humanTaskModel.setInput(getConnectionForTargetElement(new Long(
                    targetElement.getAttribute(DWDLNames.arcId)),
                    humanTaskModel, parentModel));
        }
        List<Element> sources = getChildNodesByTagName(humanTaskElement,
                DWDLNames.sources);
        if (sources.size() > 0) {
            Element sourcesElement = sources.get(0);
            Element sourceElement = getChildNodesByTagName(sourcesElement,
                    DWDLNames.source).get(0);
            humanTaskModel.setOutput(getConnectionForSourceElement(new Long(
                    sourceElement.getAttribute(DWDLNames.arcId)),
                    humanTaskModel, parentModel));
        }

        return humanTaskModel;
    }

    private FlowContainerModel createFlowModel(Element flowElement,
            WorkflowModel workflow, HasChildModels parentModel) {
        FlowContainerModel flowModel = new FlowContainerModel(parentModel);

        /* Set name id and graphics */
        flowModel.setName(flowElement.getAttribute(DWDLNames.name));
        flowModel.setId(new Long(flowElement.getAttribute(DWDLNames.id)));
        setGraphics(flowElement, flowModel);

        createInnerContainerConnections(flowElement, flowModel);

        createChildNodeModels(flowElement, workflow, flowModel);

        /* Set incoming and outgoing connections */
        List<Element> targets = getChildNodesByTagName(flowElement,
                DWDLNames.targets);
        if (targets.size() > 0) {
            Element targetsElement = targets.get(0);
            Element targetElement = getChildNodesByTagName(targetsElement,
                    DWDLNames.target).get(0);
            flowModel.setInput(getConnectionForTargetElement(new Long(
                    targetElement.getAttribute(DWDLNames.arcId)), flowModel,
                    parentModel));
        }
        List<Element> sources = getChildNodesByTagName(flowElement,
                DWDLNames.sources);
        if (sources.size() > 0) {
            Element sourcesElement = sources.get(0);
            Element sourceElement = getChildNodesByTagName(sourcesElement,
                    DWDLNames.source).get(0);
            flowModel.setOutput(getConnectionForSourceElement(new Long(
                    sourceElement.getAttribute(DWDLNames.arcId)), flowModel,
                    parentModel));
        }

        return flowModel;
    }

    private ContainerModel createIfModel(Element ifElement,
            WorkflowModel workflow, HasChildModels parentModel) {
        IfContainerModel ifModel = new IfContainerModel(parentModel);

        /* Set name id and graphics */
        ifModel.setName(ifElement.getAttribute(DWDLNames.name));
        ifModel.setId(new Long(ifElement.getAttribute(DWDLNames.id)));
        setGraphics(ifElement, ifModel);

        /* Get the child nodes which are part of a condition branch */
        List<Element> conditionElements = getChildNodesByTagName(ifElement,
                DWDLNames.condition);
        if (conditionElements.size() > 0) {
            for (Element conditionElement : conditionElements) {
                createInnerContainerConnections(conditionElement, ifModel);
                createChildNodeModels(conditionElement, workflow, ifModel);

                /* Set conditions */
                Condition condition = ifModel.getConditionById(new Long(
                        conditionElement.getAttribute(DWDLNames.id)));

                condition.setOrder(new Integer(conditionElement
                        .getAttribute(DWDLNames.order)));

                // JS remove if branch, when workflow parser has changed
                if (condition.getOrder() != 0) {
                    Element leftOperand = getChildNodesByTagName(
                            conditionElement, DWDLNames.leftOp).get(0);
                    condition.setLeftOperandId(new Long(leftOperand
                            .getFirstChild().getNodeValue()));

                    Element operator = getChildNodesByTagName(conditionElement,
                            DWDLNames.operator).get(0);
                    condition.setOperator(Operator
                            .getOperatorFromDisplayString(operator
                                    .getFirstChild().getNodeValue()));

                    Element rightOperand = getChildNodesByTagName(
                            conditionElement, DWDLNames.rightOp).get(0);
                    condition.setRightOperandId(new Long(rightOperand
                            .getFirstChild().getNodeValue()));
                }
            }
        }

        /* Create branchless connections and nodes, i.e. without a condition */
        createInnerContainerConnections(ifElement, ifModel);
        createChildNodeModels(ifElement, workflow, ifModel);

        /* Set incoming and outgoing connections */
        List<Element> targets = getChildNodesByTagName(ifElement,
                DWDLNames.targets);
        if (targets.size() > 0) {
            Element targetsElement = targets.get(0);
            Element targetElement = getChildNodesByTagName(targetsElement,
                    DWDLNames.target).get(0);
            ifModel.setInput(getConnectionForTargetElement(new Long(
                    targetElement.getAttribute(DWDLNames.arcId)), ifModel,
                    parentModel));
        }
        List<Element> sources = getChildNodesByTagName(ifElement,
                DWDLNames.sources);
        if (sources.size() > 0) {
            Element sourcesElement = sources.get(0);
            Element sourceElement = getChildNodesByTagName(sourcesElement,
                    DWDLNames.source).get(0);
            ifModel.setOutput(getConnectionForSourceElement(new Long(
                    sourceElement.getAttribute(DWDLNames.arcId)), ifModel,
                    parentModel));
        }

        return ifModel;
    }

    private ContainerModel createForEachModel(Element forEachElement,
            WorkflowModel workflow, HasChildModels parentModel) {
        ForEachContainerModel forEachModel = new ForEachContainerModel(
                parentModel);

        /* Set name id and graphics */
        forEachModel.setName(forEachElement.getAttribute(DWDLNames.name));
        forEachModel.setId(new Long(forEachElement.getAttribute(DWDLNames.id)));
        setGraphics(forEachElement, forEachModel);

        createInnerContainerConnections(forEachElement, forEachModel);

        createChildNodeModels(forEachElement, workflow, forEachModel);

        /* Set for each properties, if there are any */
        List<Element> finalCounterValueElement = getChildNodesByTagName(
                forEachElement, DWDLNames.finalCounterValue);
        if (finalCounterValueElement.size() > 0) {
            forEachModel.setIterationVariableId(VariableNameFactory
                    .createIdFromNCName(finalCounterValueElement.get(0)
                            .getFirstChild().getNodeValue()));
        }
        List<Element> completionConditionElement = getChildNodesByTagName(
                forEachElement, DWDLNames.completionCon);
        if (completionConditionElement.size() > 0) {
            forEachModel.setExitCondition(ExitCondition
                    .valueOf(completionConditionElement.get(0).getFirstChild()
                            .getNodeValue()));
        }
        if (forEachElement.getAttribute(DWDLNames.parallel).equals(
                DWDLNames.yes)) {
            forEachModel.setParallel(true);
        } else {
            forEachModel.setParallel(false);
        }

        /* Set incoming and outgoing connections */
        List<Element> targets = getChildNodesByTagName(forEachElement,
                DWDLNames.targets);
        if (targets.size() > 0) {
            Element targetsElement = targets.get(0);
            Element targetElement = getChildNodesByTagName(targetsElement,
                    DWDLNames.target).get(0);
            forEachModel.setInput(getConnectionForTargetElement(new Long(
                    targetElement.getAttribute(DWDLNames.arcId)), forEachModel,
                    parentModel));
        }
        List<Element> sources = getChildNodesByTagName(forEachElement,
                DWDLNames.sources);
        if (sources.size() > 0) {
            Element sourcesElement = sources.get(0);
            Element sourceElement = getChildNodesByTagName(sourcesElement,
                    DWDLNames.source).get(0);
            forEachModel.setOutput(getConnectionForSourceElement(new Long(
                    sourceElement.getAttribute(DWDLNames.arcId)), forEachModel,
                    parentModel));
        }

        return forEachModel;
    }

    private void setGraphics(Element node, NodeModel model) {
        Element graphics = getChildNodesByTagName(node, DWDLNames.graphics)
                .get(0);

        /* Set position */
        Integer left = new Integer(graphics.getAttribute(DWDLNames.x));
        Integer top = new Integer(graphics.getAttribute(DWDLNames.y));
        model.setChangeListenerPosition(left, top);

        /* If model is a container, it has also a width and height */
        if (model instanceof ContainerModel) {
            Integer height = new Integer(graphics
                    .getAttribute(DWDLNames.height));
            Integer width = new Integer(graphics.getAttribute(DWDLNames.width));
            ((ContainerModel) model).setChangeListenerSize(width, height);
        }

    }

    private void createInnerContainerConnections(Element containerElement,
            ContainerModel containerModel) {
        /*
         * Parse inner arcs element of the container. Arcs, which reference the
         * container as source element, are container start connections. Arcs,
         * which reference the container as target element, are container exit
         * connections.
         */
        List<Element> arcs = getChildNodesByTagName(containerElement,
                DWDLNames.arcs);

        /* If there are any arcs, that means the container has child connections */
        if (arcs.size() > 0) {
            Element arcsElement = arcs.get(0);
            List<Element> arcElements = getChildElementsAsList(arcsElement);
            /*
             * check for every arc element (connection): Is the container the
             * source or the target of the connection?
             */
            for (Element arcElement : arcElements) {
                if (arcElement.getAttribute(DWDLNames.source).equals(
                        containerModel.getId().toString())) {
                    getConnectionForSourceElement(new Long(arcElement
                            .getAttribute(DWDLNames.id)), containerModel,
                            containerModel);
                } else if (arcElement.getAttribute(DWDLNames.target).equals(
                        containerModel.getId().toString())) {
                    getConnectionForTargetElement(new Long(arcElement
                            .getAttribute(DWDLNames.id)), containerModel,
                            containerModel);
                }
            }
        }
    }

    /**
     * Returns the {@link ConnectionModel} for a given connection id. Can also
     * be a child type of {@link ConnectionModel}.
     * 
     * @param connectionId
     *            the id of the connection
     * @param sourceModel
     *            the source of the connection
     * @param parentModel
     *            the parent (container) of the connection
     * @return the ConnectionModel
     */
    private ConnectionModel getConnectionForSourceElement(Long connectionId,
            NodeModel sourceModel, HasChildModels parentModel) {
        /*
         * If this is the first connection to be got, then the hash map has to
         * be created first
         */
        if (connectionModels == null) {
            connectionModels = new HashMap<Long, ConnectionModel>();
        }

        ConnectionModel resultConnection;

        /* If the connection is not in the hash map, it has to be created */
        if (connectionModels.containsKey(connectionId) == false) {
            /*
             * check the instance of the source model and create the appropriate
             * connection type.
             */
            if (sourceModel instanceof ContainerModel) {
                if (sourceModel instanceof IfContainerModel) {
                    resultConnection = new Condition();
                } else {
                    resultConnection = new ContainerStartConnectionModel();
                }
            } else {
                resultConnection = new ConnectionModel();
            }
            resultConnection.setId(connectionId);

            /* make sure that parent model and child connection know each other */
            resultConnection.setParentModel(parentModel);
            parentModel.addConnectionModel(resultConnection);

            connectionModels.put(connectionId, resultConnection);
        } else {
            resultConnection = connectionModels.get(connectionId);
        }
        resultConnection.setSource(sourceModel);
        return resultConnection;
    }

    /**
     * Returns the {@link ConnectionModel} for a given connection id. Can also
     * be a child type of {@link ConnectionModel}.
     * 
     * @param connectionId
     *            the id of the connection
     * @param targetModel
     *            the target of the connection
     * @param parentModel
     *            the parent (container) of the connection
     * @return the ConnectionModel
     */
    private ConnectionModel getConnectionForTargetElement(Long connectionId,
            NodeModel targetModel, HasChildModels parentModel) {
        /*
         * If this is the first connection to be got, then the hash map has to
         * be created first
         */
        if (connectionModels == null) {
            connectionModels = new HashMap<Long, ConnectionModel>();
        }

        ConnectionModel resultConnection;

        /* If the connection is not in the hash map, it has to be created */
        if (connectionModels.containsKey(connectionId) == false) {
            /*
             * check the instance of the target model and create the appropriate
             * connection type.
             */
            if (targetModel instanceof ContainerModel) {
                resultConnection = new ContainerExitConnectionModel();
            } else {
                resultConnection = new ConnectionModel();
            }
            resultConnection.setId(connectionId);

            /* make sure that parent model and child connection know each other */
            resultConnection.setParentModel(parentModel);
            parentModel.addConnectionModel(resultConnection);

            connectionModels.put(connectionId, resultConnection);
        } else {
            resultConnection = connectionModels.get(connectionId);
        }
        resultConnection.setTarget(targetModel);
        return resultConnection;
    }

    private List<Element> getChildNodesByTagName(Element parent, String tagName) {
        List<Element> result = new ArrayList<Element>();

        for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
            Node child = parent.getChildNodes().item(i);
            if (child.getNodeName().equals(tagName)) {
                result.add((Element) child);
            }
        }

        return result;
    }

    private List<Element> getChildElementsAsList(Node parentElement) {
        List<Element> list = new ArrayList<Element>();
        for (int i = 0; i < parentElement.getChildNodes().getLength(); i++) {
            list.add((Element) parentElement.getChildNodes().item(i));
        }
        return list;
    }

}
