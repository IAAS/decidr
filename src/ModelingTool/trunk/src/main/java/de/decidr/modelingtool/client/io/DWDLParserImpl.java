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
 * TODO: add comment
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

        /* Create variables */
        List<Variable> variables = new ArrayList<Variable>();
        createVariables(doc, variables);
        createRoles(doc, variables);
        workflow.setVariables(variables);

        /* Create nodes */
        createChildNodeModels((Element) doc, workflow, workflow);

        return workflow;
    }

    private void createWorkflowProperties(Document doc, WorkflowModel workflow) {
        Element root = (Element) doc.getElementsByTagName(DWDLTagNames.root)
                .item(0);

        workflow.setName(root.getAttribute(DWDLTagNames.name));
        workflow.setId(new Long(root.getAttribute(DWDLTagNames.id)));
        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            if (root.getChildNodes().item(i).getNodeName() == DWDLTagNames.description) {
                workflow.setDescription(root.getChildNodes().item(i)
                        .getNodeValue());
            }
        }
        WorkflowProperties properties = new WorkflowProperties();

        /* parse faultHandlerElement and set fault message id and recipient */
        Element faultHandler = getChildNodesByTagName(root,
                DWDLTagNames.faultHandler).get(0);
        properties.setFaultMessageVariableId(getVariableIdFromPropertyElement(
                faultHandler, DWDLTagNames.message));
        Element recipient = getChildNodesByTagName(faultHandler,
                DWDLTagNames.recipient).get(0);
        properties.setRecipientVariableId(getVariableIdFromPropertyElement(
                recipient, DWDLTagNames.name));

        /*
         * parse endNode and set success message and notification. endNode is a
         * child of the nodes element.
         */
        if (getChildNodesByTagName(getChildNodesByTagName(root,
                DWDLTagNames.nodes).get(0), DWDLTagNames.endNode) != null) {
            Element endNode = getChildNodesByTagName(
                    getChildNodesByTagName(root, DWDLTagNames.nodes).get(0),
                    DWDLTagNames.endNode).get(0);
            Element notification = getChildNodesByTagName(endNode,
                    DWDLTagNames.notificationOfSuccess).get(0);
            properties
                    .setSuccessMessageVariableId(getVariableIdFromPropertyElement(
                            notification, DWDLTagNames.successMsg));
        }
        workflow.setProperties(properties);
    }

    private void createVariables(Document doc, List<Variable> variables) {

        /* Get the variables element */
        Element variablesElement = (Element) doc.getElementsByTagName(
                DWDLTagNames.variables).item(0);

        /* Go through all child elements with tag name variable */
        for (Element variableElement : getChildNodesByTagName(variablesElement,
                DWDLTagNames.variable)) {
            Variable variable = new Variable();

            /* Set id and label, get rid of the ncname prefix */
            variable.setId(new Long(variableElement.getAttribute(
                    DWDLTagNames.name).substring(
                    DWDLTagNames.variableNCnamePrefix.length())));
            variable.setLabel(variableElement.getAttribute(DWDLTagNames.label));

            /* Set configuration */
            if (variableElement.getAttribute(DWDLTagNames.configVar) == DWDLTagNames.yes) {
                variable.setConfig(true);
            } else {
                variable.setConfig(false);
            }

            /* Set type, get rid of the list prefix */
            String typeString = new String(variableElement
                    .getAttribute(DWDLTagNames.type));
            /*
             * If we have to get rid of the list prefix, that also means the
             * variables has multiple values
             */
            boolean isArray = false;
            if (typeString.startsWith(DWDLTagNames.listprefix)) {
                typeString = typeString.substring(DWDLTagNames.listprefix
                        .length());
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
                        DWDLTagNames.initValue);
            } else {
                valueElements = getChildNodesByTagName(variableElement,
                        DWDLTagNames.initValue);
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
                DWDLTagNames.roles).item(0);

        /* Go through all child elements */
        for (Element roleElement : getChildNodesByTagName(rolesElement,
                DWDLTagNames.role)) {
            Variable role = new Variable();

            /* Set id, name and type */
            role.setId(new Long(roleElement.getAttribute(DWDLTagNames.name)
                    .substring(DWDLTagNames.variableNCnamePrefix.length())));
            role.setLabel(roleElement.getAttribute(DWDLTagNames.label));
            role.setType(VariableType.ROLE);

            /* Set configuration */
            if (roleElement.getAttribute(DWDLTagNames.configVar) == DWDLTagNames.yes) {
                role.setConfig(true);
            } else {
                role.setConfig(false);
            }

            /* Get actors */
            for (Element actor : getChildNodesByTagName(roleElement,
                    DWDLTagNames.actor)) {
                role.getValues().add(
                        new String(actor.getAttribute(DWDLTagNames.userId)));
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
                DWDLTagNames.setProperty)) {
            if (property.getAttribute(DWDLTagNames.name) == propertyName) {
                variableId = new Long(property
                        .getAttribute(DWDLTagNames.variable));
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
            if (childElement.getNodeName() == DWDLTagNames.startNode) {
                nodeModel = createStartModel(childElement, workflow);
            } else if (childElement.getNodeName() == DWDLTagNames.endNode) {
                nodeModel = createEndModel(childElement, workflow);
            } else if (childElement.getNodeName() == DWDLTagNames.flowNode) {
                nodeModel = createFlowModel(childElement, workflow, parentModel);
            } else if (childElement.getNodeName() == DWDLTagNames.ifNode) {
                nodeModel = createIfModel(childElement, workflow, parentModel);
            } else if (childElement.getNodeName() == DWDLTagNames.forEachNode) {
                nodeModel = createForEachModel(childElement, workflow,
                        parentModel);
            } else if (childElement.getNodeName() == DWDLTagNames.invokeNode) {
                if (childElement.getAttribute(DWDLTagNames.activity) == DWDLTagNames.decidrEmail) {
                    nodeModel = createEmailInvokeNode(childElement, parentModel);
                } else if (childElement.getAttribute(DWDLTagNames.activity) == DWDLTagNames.decidrHumanTask) {
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
        startModel.setName(startElement.getAttribute(DWDLTagNames.name));
        startModel.setId(new Long(startElement.getAttribute(DWDLTagNames.id)));
        setGraphics(startElement, startModel);

        /* Set outgoing connection */
        Element sourceElement = getChildNodesByTagName(startElement,
                DWDLTagNames.source).get(0);
        startModel.setOutput(getConnectionForSourceElement(sourceElement,
                startModel, workflow));
        return startModel;
    }

    private EndNodeModel createEndModel(Element endElement,
            WorkflowModel workflow) {
        EndNodeModel endModel = new EndNodeModel(workflow);

        /* Set name, id and graphics */
        endModel.setName(endElement.getAttribute(DWDLTagNames.name));
        endModel.setId(new Long(endElement.getAttribute(DWDLTagNames.id)));
        setGraphics(endElement, endModel);

        /* Set incoming connection */
        Element targetElement = getChildNodesByTagName(endElement,
                DWDLTagNames.target).get(0);
        endModel.setInput(getConnectionForTargetElement(targetElement,
                endModel, workflow));

        return endModel;
    }

    private EmailInvokeNodeModel createEmailInvokeNode(Element emailElement,
            HasChildModels parentModel) {
        EmailInvokeNodeModel emailModel = new EmailInvokeNodeModel(parentModel);

        /* Set name id and graphics */
        emailModel.setName(emailElement.getAttribute(DWDLTagNames.name));
        emailModel.setId(new Long(emailElement.getAttribute(DWDLTagNames.id)));
        setGraphics(emailElement, emailModel);

        /* Set properties of the email */
        emailModel.setToVariableId(getVariableIdFromPropertyElement(
                emailElement, DWDLTagNames.to));
        emailModel.setCcVariableId(getVariableIdFromPropertyElement(
                emailElement, DWDLTagNames.cc));
        emailModel.setBccVariableId(getVariableIdFromPropertyElement(
                emailElement, DWDLTagNames.bcc));
        emailModel.setSubjectVariableId(getVariableIdFromPropertyElement(
                emailElement, DWDLTagNames.subject));
        emailModel.setMessageVariableId(getVariableIdFromPropertyElement(
                emailElement, DWDLTagNames.message));
        emailModel.setAttachmentVariableId(getVariableIdFromPropertyElement(
                emailElement, DWDLTagNames.attachment));

        /* Set incoming and outgoing connections */
        Element targetElement = getChildNodesByTagName(emailElement,
                DWDLTagNames.target).get(0);
        emailModel.setInput(getConnectionForTargetElement(targetElement,
                emailModel, parentModel));
        Element sourceElement = getChildNodesByTagName(emailElement,
                DWDLTagNames.target).get(0);
        emailModel.setInput(getConnectionForSourceElement(sourceElement,
                emailModel, parentModel));

        return emailModel;
    }

    private HumanTaskInvokeNodeModel createHumanTaskInvokeNode(
            Element humanTaskElement, HasChildModels parentModel) {
        HumanTaskInvokeNodeModel humanTaskModel = new HumanTaskInvokeNodeModel(
                parentModel);

        /* Set name id and graphics */
        humanTaskModel
                .setName(humanTaskElement.getAttribute(DWDLTagNames.name));
        humanTaskModel.setId(new Long(humanTaskElement
                .getAttribute(DWDLTagNames.id)));
        setGraphics(humanTaskElement, humanTaskModel);

        /* Set properties of the human task */
        humanTaskModel.setUserVariableId(getVariableIdFromPropertyElement(
                humanTaskElement, DWDLTagNames.user));
        humanTaskModel
                .setWorkItemNameVariableId(getVariableIdFromPropertyElement(
                        humanTaskElement, DWDLTagNames.name));
        humanTaskModel
                .setWorkItemDescriptionVariableId(getVariableIdFromPropertyElement(
                        humanTaskElement, DWDLTagNames.description));

        /* Get user notification property */
        for (Element propertyElement : getChildElementsAsList(humanTaskElement)) {
            if (propertyElement.getAttribute(DWDLTagNames.name) == DWDLTagNames.userNotification) {
                Element valueElement = getChildNodesByTagName(propertyElement,
                        DWDLTagNames.propertyValue).get(0);
                Text valueText = (Text) valueElement.getChildNodes().item(0);
                if (valueText.getNodeValue() == DWDLTagNames.yes) {
                    humanTaskModel.setNotifyVariableId(true);
                } else {
                    humanTaskModel.setNotifyVariableId(false);
                }
            }
        }

        /* Get the task items */
        for (Element getProperty : getChildNodesByTagName(
                getChildNodesByTagName(humanTaskElement,
                        DWDLTagNames.parameters).get(0),
                DWDLTagNames.getProperty)) {
            if (getProperty.getAttribute(DWDLTagNames.name) == DWDLTagNames.taskResult) {
                // JS change so that parameter element is considered
                Element humanTaskData = getChildNodesByTagName(getProperty,
                        DWDLTagNames.humanTaskData).get(0);
                List<Element> taskItemElements = getChildNodesByTagName(
                        humanTaskData, DWDLTagNames.taskItem);
                List<TaskItem> taskItems = new ArrayList<TaskItem>();
                for (Element taskItemElement : taskItemElements) {
                    /*
                     * Every task item node has to have a variable id as
                     * attribute and a text child node with the label of the
                     * task item
                     */
                    Long variableId = new Long(taskItemElement
                            .getAttribute(DWDLTagNames.variable));
                    String label = new String(getChildNodesByTagName(
                            taskItemElement, DWDLTagNames.label).get(0)
                            .getNodeValue());
                    TaskItem taskitem = new TaskItem(label, variableId);
                    taskItems.add(taskitem);
                }
                humanTaskModel.setTaskItems(taskItems);
            }
        }

        /* Set incoming and outgoing connections */
        Element targetElement = getChildNodesByTagName(humanTaskElement,
                DWDLTagNames.target).get(0);
        humanTaskModel.setInput(getConnectionForTargetElement(targetElement,
                humanTaskModel, parentModel));
        Element sourceElement = getChildNodesByTagName(humanTaskElement,
                DWDLTagNames.target).get(0);
        humanTaskModel.setInput(getConnectionForSourceElement(sourceElement,
                humanTaskModel, parentModel));

        return humanTaskModel;
    }

    private FlowContainerModel createFlowModel(Element flowElement,
            WorkflowModel workflow, HasChildModels parentModel) {
        FlowContainerModel flowModel = new FlowContainerModel(parentModel);

        /* Set name id and graphics */
        flowModel.setName(flowElement.getAttribute(DWDLTagNames.name));
        flowModel.setId(new Long(flowElement.getAttribute(DWDLTagNames.id)));
        setGraphics(flowElement, flowModel);

        createChildNodeModels(flowElement, workflow, flowModel);

        /* Set incoming and outgoing connections */
        Element targetElement = getChildNodesByTagName(flowElement,
                DWDLTagNames.target).get(0);
        flowModel.setInput(getConnectionForTargetElement(targetElement,
                flowModel, parentModel));
        Element sourceElement = getChildNodesByTagName(flowElement,
                DWDLTagNames.target).get(0);
        flowModel.setInput(getConnectionForSourceElement(sourceElement,
                flowModel, parentModel));

        return flowModel;
    }

    private ContainerModel createIfModel(Element ifElement,
            WorkflowModel workflow, HasChildModels parentModel) {
        IfContainerModel ifModel = new IfContainerModel(parentModel);

        /* Set name id and graphics */
        ifModel.setName(ifElement.getAttribute(DWDLTagNames.name));
        ifModel.setId(new Long(ifElement.getAttribute(DWDLTagNames.id)));
        setGraphics(ifElement, ifModel);

        /*
         * The child nodes of the if container are children of the condition
         * element
         */
        for (Element conditionElement : getChildNodesByTagName(ifElement,
                DWDLTagNames.condition)) {
            Collection<NodeModel> childNodeModels = createChildNodeModels(
                    conditionElement, workflow, ifModel);

            /* Set conditions */
            for (NodeModel nodeModel : childNodeModels) {
                if (nodeModel.getInput() instanceof Condition) {
                    Condition condition = (Condition) nodeModel.getInput();
                    Element leftOperand = getChildNodesByTagName(
                            conditionElement, DWDLTagNames.leftOp).get(0);
                    condition
                            .setOperand1Id(new Long(leftOperand.getNodeValue()));
                    Element operator = getChildNodesByTagName(conditionElement,
                            DWDLTagNames.operator).get(0);
                    condition.setOperator(Operator
                            .getOperatorFromDisplayString(operator
                                    .getNodeValue()));
                    Element rightOperand = getChildNodesByTagName(
                            conditionElement, DWDLTagNames.rightOp).get(0);
                    condition.setOperand2Id(new Long(rightOperand
                            .getNodeValue()));
                }
            }
        }

        /* Set incoming and outgoing connections */
        Element targetElement = getChildNodesByTagName(ifElement,
                DWDLTagNames.target).get(0);
        ifModel.setInput(getConnectionForTargetElement(targetElement, ifModel,
                parentModel));
        Element sourceElement = getChildNodesByTagName(ifElement,
                DWDLTagNames.target).get(0);
        ifModel.setInput(getConnectionForSourceElement(sourceElement, ifModel,
                parentModel));

        return ifModel;
    }

    private ContainerModel createForEachModel(Element forEachElement,
            WorkflowModel workflow, HasChildModels parentModel) {
        ForEachContainerModel forEachModel = new ForEachContainerModel();

        /* Set name id and graphics */
        forEachModel.setName(forEachElement.getAttribute(DWDLTagNames.name));
        forEachModel.setId(new Long(forEachElement
                .getAttribute(DWDLTagNames.id)));
        setGraphics(forEachElement, forEachModel);

        createChildNodeModels(forEachElement, workflow, forEachModel);

        /* Set for each properties */
        Element finalCounterValueElement = getChildNodesByTagName(
                forEachElement, DWDLTagNames.finalCounterValue).get(0);
        forEachModel.setIterationVariableId(new Long(finalCounterValueElement
                .getNodeValue()));
        Element completionConditionElement = getChildNodesByTagName(
                forEachElement, DWDLTagNames.completionCon).get(0);
        forEachModel.setExitCondition(ExitCondition
                .valueOf(completionConditionElement.getNodeValue()));

        /* Set incoming and outgoing connections */
        Element targetElement = getChildNodesByTagName(forEachElement,
                DWDLTagNames.target).get(0);
        forEachModel.setInput(getConnectionForTargetElement(targetElement,
                forEachModel, parentModel));
        Element sourceElement = getChildNodesByTagName(forEachElement,
                DWDLTagNames.target).get(0);
        forEachModel.setInput(getConnectionForSourceElement(sourceElement,
                forEachModel, parentModel));

        return forEachModel;
    }

    private void setGraphics(Element node, NodeModel model) {
        Element graphics = getChildNodesByTagName(node, DWDLTagNames.graphics)
                .get(0);

        /* Set position */
        Integer left = new Integer(graphics.getAttribute(DWDLTagNames.x));
        Integer top = new Integer(graphics.getAttribute(DWDLTagNames.y));
        model.setChangeListenerPosition(left, top);

        /* If model is a container, it has also a width and height */
        if (model instanceof ContainerModel) {
            Integer height = new Integer(graphics
                    .getAttribute(DWDLTagNames.height));
            Integer width = new Integer(graphics
                    .getAttribute(DWDLTagNames.width));
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
                .getAttribute(DWDLTagNames.arcId));

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
                .getAttribute(DWDLTagNames.arcId));

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
