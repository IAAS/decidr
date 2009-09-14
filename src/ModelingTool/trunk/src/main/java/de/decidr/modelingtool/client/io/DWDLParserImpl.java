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
import com.google.gwt.xml.client.Text;
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
        createChildNodeModels((Element) doc, workflow, workflow);

        return workflow;
    }

    private void createWorkflowProperties(Document doc, WorkflowModel workflow) {
        Element root = (Element) doc.getElementsByTagName(DWDLNames.root).item(
                0);

        workflow.setName(root.getAttribute(DWDLNames.name));
        workflow.setId(new Long(root.getAttribute(DWDLNames.id)));
        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            if (root.getChildNodes().item(i).getNodeName() == DWDLNames.description) {
                workflow.setDescription(root.getChildNodes().item(i)
                        .getNodeValue());
            }
        }
        WorkflowProperties properties = new WorkflowProperties();

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
            Element notification = getChildNodesByTagName(endNode,
                    DWDLNames.notificationOfSuccess).get(0);
            properties
                    .setSuccessMessageVariableId(getVariableIdFromPropertyElement(
                            notification, DWDLNames.successMsg));
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
            variable.setId(new Long(variableElement
                    .getAttribute(DWDLNames.name).substring(
                            DWDLNames.variableNCnamePrefix.length())));
            variable.setLabel(variableElement.getAttribute(DWDLNames.label));

            /* Set configuration */
            if (variableElement.getAttribute(DWDLNames.configVar) == DWDLNames.yes) {
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
            variable.setType(VariableType.valueOf(typeString));

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
                variable.getValues().add(
                        new String(valueElement.getNodeValue()));
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
            role.setId(new Long(roleElement.getAttribute(DWDLNames.name)
                    .substring(DWDLNames.variableNCnamePrefix.length())));
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
            if (property.getAttribute(DWDLNames.name) == propertyName) {
                variableId = new Long(property.getAttribute(DWDLNames.variable)
                        .substring(DWDLNames.variableNCnamePrefix.length()));
            }
        }
        return variableId;
    }

    private Collection<NodeModel> createChildNodeModels(Element parentElement,
            WorkflowModel workflow, HasChildModels parentModel) {
        Collection<NodeModel> childNodeModels = new HashSet<NodeModel>();
        for (Element childElement : getChildElementsAsList(parentElement)) {
            NodeModel nodeModel = null;
            /*
             * Check the name of the child nodes against the names of all know
             * node type and call their creation method. In case the name is
             * startnode or endnode it is safe to assume the parent element is a
             * workflow element
             */
            if (childElement.getNodeName() == DWDLNames.startNode) {
                nodeModel = createStartModel(childElement, workflow);
            } else if (childElement.getNodeName() == DWDLNames.endNode) {
                nodeModel = createEndModel(childElement, workflow);
            } else if (childElement.getNodeName() == DWDLNames.flowNode) {
                nodeModel = createFlowModel(childElement, workflow, parentModel);
            } else if (childElement.getNodeName() == DWDLNames.ifNode) {
                nodeModel = createIfModel(childElement, workflow, parentModel);
            } else if (childElement.getNodeName() == DWDLNames.forEachNode) {
                nodeModel = createForEachModel(childElement, workflow,
                        parentModel);
            } else if (childElement.getNodeName() == DWDLNames.invokeNode) {
                if (childElement.getAttribute(DWDLNames.activity) == DWDLNames.decidrEmail) {
                    nodeModel = createEmailInvokeNode(childElement, parentModel);
                } else if (childElement.getAttribute(DWDLNames.activity) == DWDLNames.decidrHumanTask) {
                    nodeModel = createHumanTaskInvokeNode(childElement,
                            parentModel);
                }
            }
            if (nodeModel != null) {
                parentModel.addNodeModel(nodeModel);
                childNodeModels.add(nodeModel);
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
        Element sourceElement = getChildNodesByTagName(startElement,
                DWDLNames.source).get(0);
        startModel.setOutput(getConnectionForSourceElement(sourceElement,
                startModel, workflow));
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
        Element targetElement = getChildNodesByTagName(endElement,
                DWDLNames.target).get(0);
        endModel.setInput(getConnectionForTargetElement(targetElement,
                endModel, workflow));

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
        Element targetElement = getChildNodesByTagName(emailElement,
                DWDLNames.target).get(0);
        emailModel.setInput(getConnectionForTargetElement(targetElement,
                emailModel, parentModel));
        Element sourceElement = getChildNodesByTagName(emailElement,
                DWDLNames.target).get(0);
        emailModel.setInput(getConnectionForSourceElement(sourceElement,
                emailModel, parentModel));

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
        for (Element propertyElement : getChildElementsAsList(humanTaskElement)) {
            if (propertyElement.getAttribute(DWDLNames.name) == DWDLNames.userNotification) {
                Element valueElement = getChildNodesByTagName(propertyElement,
                        DWDLNames.propertyValue).get(0);
                Text valueText = (Text) valueElement.getChildNodes().item(0);
                if (valueText.getNodeValue() == DWDLNames.yes) {
                    humanTaskModel.setNotifyActor(true);
                } else {
                    humanTaskModel.setNotifyActor(false);
                }
            }
        }

        /* Get the task items */
        for (Element getProperty : getChildNodesByTagName(
                getChildNodesByTagName(humanTaskElement, DWDLNames.parameters)
                        .get(0), DWDLNames.getProperty)) {
            if (getProperty.getAttribute(DWDLNames.name) == DWDLNames.taskResult) {
                Element humanTaskData = getChildNodesByTagName(getProperty,
                        DWDLNames.humanTaskData).get(0);
                List<Element> taskItemElements = getChildNodesByTagName(
                        humanTaskData, DWDLNames.taskItem);
                List<TaskItem> taskItems = new ArrayList<TaskItem>();
                for (Element taskItemElement : taskItemElements) {
                    /*
                     * Every task item node has to have a variable id as
                     * attribute and a text child node with the label of the
                     * task item
                     */
                    Long variableId = new Long(taskItemElement
                            .getAttribute(DWDLNames.variable));
                    String label = new String(getChildNodesByTagName(
                            taskItemElement, DWDLNames.label).get(0)
                            .getNodeValue());
                    String hint = new String(getChildNodesByTagName(
                            taskItemElement, DWDLNames.hint).get(0)
                            .getNodeValue());
                    TaskItem taskitem = new TaskItem(label, hint, variableId);
                    taskItems.add(taskitem);
                }
                humanTaskModel.setTaskItems(taskItems);
            }
        }

        /* Set incoming and outgoing connections */
        Element targetElement = getChildNodesByTagName(humanTaskElement,
                DWDLNames.target).get(0);
        humanTaskModel.setInput(getConnectionForTargetElement(targetElement,
                humanTaskModel, parentModel));
        Element sourceElement = getChildNodesByTagName(humanTaskElement,
                DWDLNames.target).get(0);
        humanTaskModel.setInput(getConnectionForSourceElement(sourceElement,
                humanTaskModel, parentModel));

        return humanTaskModel;
    }

    private FlowContainerModel createFlowModel(Element flowElement,
            WorkflowModel workflow, HasChildModels parentModel) {
        FlowContainerModel flowModel = new FlowContainerModel(parentModel);

        /* Set name id and graphics */
        flowModel.setName(flowElement.getAttribute(DWDLNames.name));
        flowModel.setId(new Long(flowElement.getAttribute(DWDLNames.id)));
        setGraphics(flowElement, flowModel);

        createChildNodeModels(flowElement, workflow, flowModel);

        /* Set incoming and outgoing connections */
        Element targetElement = getChildNodesByTagName(flowElement,
                DWDLNames.target).get(0);
        flowModel.setInput(getConnectionForTargetElement(targetElement,
                flowModel, parentModel));
        Element sourceElement = getChildNodesByTagName(flowElement,
                DWDLNames.target).get(0);
        flowModel.setInput(getConnectionForSourceElement(sourceElement,
                flowModel, parentModel));

        return flowModel;
    }

    private ContainerModel createIfModel(Element ifElement,
            WorkflowModel workflow, HasChildModels parentModel) {
        IfContainerModel ifModel = new IfContainerModel(parentModel);

        /* Set name id and graphics */
        ifModel.setName(ifElement.getAttribute(DWDLNames.name));
        ifModel.setId(new Long(ifElement.getAttribute(DWDLNames.id)));
        setGraphics(ifElement, ifModel);

        /*
         * The child nodes of the if container are children of the condition
         * element
         */
        for (Element conditionElement : getChildNodesByTagName(ifElement,
                DWDLNames.condition)) {
            Collection<NodeModel> childNodeModels = createChildNodeModels(
                    conditionElement, workflow, ifModel);

            /* Set conditions */
            for (NodeModel nodeModel : childNodeModels) {
                if (nodeModel.getInput() instanceof Condition) {
                    Condition condition = (Condition) nodeModel.getInput();
                    Element leftOperand = getChildNodesByTagName(
                            conditionElement, DWDLNames.leftOp).get(0);
                    condition.setLeftOperandId(new Long(leftOperand
                            .getNodeValue()));
                    Element operator = getChildNodesByTagName(conditionElement,
                            DWDLNames.operator).get(0);
                    condition.setOperator(Operator
                            .getOperatorFromDisplayString(operator
                                    .getNodeValue()));
                    Element rightOperand = getChildNodesByTagName(
                            conditionElement, DWDLNames.rightOp).get(0);
                    condition.setRightOperandId(new Long(rightOperand
                            .getNodeValue()));
                }
            }
        }

        /* Set incoming and outgoing connections */
        Element targetElement = getChildNodesByTagName(ifElement,
                DWDLNames.target).get(0);
        ifModel.setInput(getConnectionForTargetElement(targetElement, ifModel,
                parentModel));
        Element sourceElement = getChildNodesByTagName(ifElement,
                DWDLNames.target).get(0);
        ifModel.setInput(getConnectionForSourceElement(sourceElement, ifModel,
                parentModel));

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

        createChildNodeModels(forEachElement, workflow, forEachModel);

        /* Set for each properties */
        Element finalCounterValueElement = getChildNodesByTagName(
                forEachElement, DWDLNames.finalCounterValue).get(0);
        forEachModel.setIterationVariableId(new Long(finalCounterValueElement
                .getNodeValue()));
        Element completionConditionElement = getChildNodesByTagName(
                forEachElement, DWDLNames.completionCon).get(0);
        forEachModel.setExitCondition(ExitCondition
                .valueOf(completionConditionElement.getNodeValue()));

        /* Set incoming and outgoing connections */
        Element targetElement = getChildNodesByTagName(forEachElement,
                DWDLNames.target).get(0);
        forEachModel.setInput(getConnectionForTargetElement(targetElement,
                forEachModel, parentModel));
        Element sourceElement = getChildNodesByTagName(forEachElement,
                DWDLNames.target).get(0);
        forEachModel.setInput(getConnectionForSourceElement(sourceElement,
                forEachModel, parentModel));

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

    private ConnectionModel getConnectionForSourceElement(
            Element sourceElement, NodeModel sourceModel,
            HasChildModels parentModel) {
        /*
         * If this is the first connection to be got, then the hash map has to
         * be created first
         */
        if (connectionModels == null) {
            connectionModels = new HashMap<Long, ConnectionModel>();
        }

        ConnectionModel resultConnection;

        /* Create id */
        Long connectionId = new Long(sourceElement
                .getAttribute(DWDLNames.arcId));

        /* If the connection is not in the hash map, it has to be created */
        if (connectionModels.get(connectionId) == null) {
            ConnectionModel connection;
            /*
             * check the instance of the source model and create the appropriate
             * connection type.
             */
            if (sourceModel instanceof ContainerModel) {
                if (sourceModel instanceof IfContainerModel) {
                    connection = new Condition();
                } else {
                    connection = new ContainerStartConnectionModel();
                }
            } else {
                connection = new ConnectionModel();
            }
            connection.setId(connectionId);
            connection.setSource(sourceModel);
            connection.setParentModel(parentModel);
            parentModel.addConnectionModel(connection);
            connectionModels.put(connectionId, connection);
        }
        resultConnection = connectionModels.get(0L);

        return resultConnection;
    }

    private ConnectionModel getConnectionForTargetElement(
            Element targetElement, NodeModel targetModel,
            HasChildModels parentModel) {
        /*
         * If this is the first connection to be got, then the hash map has to
         * be created first
         */
        if (connectionModels == null) {
            connectionModels = new HashMap<Long, ConnectionModel>();
        }

        ConnectionModel resultConnection;

        /* Create id */
        Long connectionId = new Long(targetElement
                .getAttribute(DWDLNames.arcId));

        /* If the connection is not in the hash map, it has to be created */
        if (connectionModels.get(connectionId) == null) {
            ConnectionModel connection;
            /*
             * check the instance of the target model and create the appropriate
             * connection type.
             */
            if (targetModel instanceof ContainerModel) {
                connection = new ContainerExitConnectionModel();
            } else {
                connection = new ConnectionModel();
            }
            connection.setId(connectionId);
            connection.setTarget(targetModel);
            connection.setParentModel(parentModel);
            connectionModels.put(connectionId, connection);
        }
        resultConnection = connectionModels.get(0L);

        return resultConnection;
    }

    private List<Element> getChildNodesByTagName(Element parent, String tagName) {
        List<Element> result = new ArrayList<Element>();
        for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
            Node child = parent.getChildNodes().item(i);
            if (child.getNodeName() == tagName) {
                result.add((Element) child);
            }
        }
        return result;
    }

    private List<Element> getChildElementsAsList(Element parentElement) {
        List<Element> list = new ArrayList<Element>();
        for (int i = 0; i < parentElement.getChildNodes().getLength(); i++) {
            list.add((Element) parentElement.getChildNodes().item(i));
        }
        return list;
    }

}
