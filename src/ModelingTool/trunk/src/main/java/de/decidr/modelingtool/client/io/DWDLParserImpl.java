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
import java.util.List;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

import de.decidr.modelingtool.client.model.ConnectionModel;
import de.decidr.modelingtool.client.model.ContainerModel;
import de.decidr.modelingtool.client.model.EmailInvokeNodeModel;
import de.decidr.modelingtool.client.model.FlowContainerModel;
import de.decidr.modelingtool.client.model.HasChildModels;
import de.decidr.modelingtool.client.model.NodeModel;
import de.decidr.modelingtool.client.model.StartNodeModel;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.model.WorkflowProperties;
import de.decidr.modelingtool.client.model.foreach.ForEachContainerModel;
import de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel;
import de.decidr.modelingtool.client.model.ifcondition.IfContainerModel;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;

/**
 * TODO: add comment
 * 
 * @author Modood Alvi, Jonas Schlaak
 * @version 0.1
 */
public class DWDLParserImpl implements DWDLParser {

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

        WorkflowModel model = new WorkflowModel();

        createWorkflowProperties(doc, model);
        createVariables(doc, model);
        createRoles(doc, model);

        /*
         * Create nodes, start with start node. Only the creation method of the
         * start node is used here. All other node creating methods are used
         * when following the outgoing connections of the nodes.
         */
        Element startNode = (Element) ((Element) doc.getElementsByTagName(
                DWDLTagNames.nodes).item(0)).getElementsByTagName(
                DWDLTagNames.startNode).item(0);
        createStartModel(startNode, model);

        return model;
    }

    private void createWorkflowProperties(Document doc, WorkflowModel model) {
        Element root = (Element) doc.getElementsByTagName(DWDLTagNames.root)
                .item(0);

        model.setName(root.getAttribute(DWDLTagNames.name));
        model.setId(new Long(root.getAttribute(DWDLTagNames.id)));
        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            if (root.getChildNodes().item(i).getNodeName() == DWDLTagNames.description) {
                model.setDescription(root.getChildNodes().item(i)
                        .getNodeValue());
            }
        }
        WorkflowProperties properties = new WorkflowProperties();

        /* parse faultHandlerElement and set fault message id and recipient */
        Element faultHandler = getChildNodesByTagName(root,
                DWDLTagNames.faultHandler).get(0);
        properties.setFaultMessageVariableId(getPropertyVariableIdFromElement(
                faultHandler, DWDLTagNames.message));
        Element recipient = getChildNodesByTagName(faultHandler,
                DWDLTagNames.recipient).get(0);
        properties.setRecipientVariableId(getPropertyVariableIdFromElement(
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
                    .setSuccessMessageVariableId(getPropertyVariableIdFromElement(
                            notification, DWDLTagNames.successMsg));
        }
        model.setProperties(properties);
    }

    private void createVariables(Document doc, WorkflowModel model) {

        /* Get the variables element */
        Element variablesElement = (Element) doc.getElementsByTagName(
                DWDLTagNames.variables).item(0);

        /* Go through all child elements with tag name variable */
        for (Element variableElement : getChildNodesByTagName(variablesElement,
                DWDLTagNames.variable)) {
            Variable variable = new Variable();

            /* Set id and name */
            variable.setId(new Long(variableElement
                    .getAttribute(DWDLTagNames.id)));
            variable.setName(variableElement.getAttribute(DWDLTagNames.name));

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
            model.getVariables().add(variable);
        }
    }

    private void createRoles(Document doc, WorkflowModel model) {

        /* Get the roles element */
        Element rolesElement = (Element) doc.getElementsByTagName(
                DWDLTagNames.roles).item(0);

        /* Go through all child elements */
        for (Element roleElement : getChildNodesByTagName(rolesElement,
                DWDLTagNames.role)) {
            Variable role = new Variable();

            /* Set id, name and type */
            role.setId(new Long(roleElement.getAttribute(DWDLTagNames.id)));
            role.setName(roleElement.getAttribute(DWDLTagNames.name));
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
            model.getVariables().add(role);
        }
    }

    private Long getPropertyVariableIdFromElement(Element parent,
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

    private void createStartModel(Element startNode, WorkflowModel model) {
        StartNodeModel startModel = new StartNodeModel(model);

        /* Set name, id and graphics */
        startModel.setName(startNode.getAttribute(DWDLTagNames.name));
        startModel.setId(new Long(startNode.getAttribute(DWDLTagNames.id)));
        setGraphics(startNode, startModel);

        /* Set outgoing connection */
        List<Element> arcs = getChildNodesByTagName((Element) startNode
                .getParentNode().getParentNode(), DWDLTagNames.arcs);
        startModel.setOutput(createSingleConnectionModel(startNode, arcs,
                startModel, model));
    }

    private ConnectionModel createSingleConnectionModel(
            Element sourceNodeElement, List<Element> arcs,
            NodeModel sourceModel, HasChildModels model) {
        ConnectionModel connection = new ConnectionModel();
        connection.setSource(sourceModel);

        /*
         * Get the (single) source element from the node. The source element of
         * the node references the id of the outgoing connection.
         */
        Element sourcesElement = getChildNodesByTagName(sourceNodeElement,
                DWDLTagNames.sources).get(0);
        Element sourceElement = getChildNodesByTagName(sourcesElement,
                DWDLTagNames.sources).get(0);
        connection.setId(new Long(sourceElement
                .getAttribute(DWDLTagNames.arcId)));

        /*
         * Create target of the connection. We have to search for the id of the
         * target node in the arcs element of the dwdl.
         */
        String targetNodeId = null;
        for (Element arc : arcs) {
            if (arc.getAttribute(DWDLTagNames.arc) == connection.getId()
                    .toString()) {
                targetNodeId = new String(arc.getAttribute(DWDLTagNames.target));
            }
        }
        connection.setTarget(createTarget(connection, targetNodeId,
                (Element) sourceNodeElement.getParentNode(), model));

        return connection;
    }

    /* nodesContainer has to be the <nodes></nodes> element. */
    private NodeModel createTarget(ConnectionModel input, String targetNodeId,
            Element nodesContainer, HasChildModels parentModel) {
        NodeModel nodeModel = null;
        List<Element> childNodeElements = getChildNodesAsList(nodesContainer);
        /*
         * Go through all child node elements and check if which one has the
         * target id
         */
        for (Element nodeElement : childNodeElements) {
            if (nodeElement.getAttribute(DWDLTagNames.id) == targetNodeId) {
                /* Check if the node is an invoke node or a container node */
                if (nodeElement.getNodeName() == DWDLTagNames.invokeNode) {
                    /* Check which invoke node type the node has */
                    if (nodeElement.getAttribute(DWDLTagNames.activity) == DWDLTagNames.decidrEmail) {
                        nodeModel = createEmailInvokeNode(parentModel);
                    } else if (nodeElement.getAttribute(DWDLTagNames.activity) == DWDLTagNames.decidrHumanTask) {
                        nodeModel = createHumanTaskInvokeNode(parentModel);
                    }
                } else if (nodeElement.getNodeName() == DWDLTagNames.flowNode) {
                    nodeModel = createFlowModel(nodeElement, parentModel);
                } else if (nodeElement.getNodeName() == DWDLTagNames.ifNode) {
                    nodeModel = createIfModel(parentModel);
                } else if (nodeElement.getNodeName() == DWDLTagNames.forEachNode) {
                    nodeModel = crearteForEachModel(parentModel);
                }
                nodeModel.setInput(input);
                List<Element> arcs = getChildNodesByTagName(
                        (Element) nodesContainer.getParentNode(),
                        DWDLTagNames.arcs);
                nodeModel.setOutput(createSingleConnectionModel(nodeElement,
                        arcs, nodeModel, parentModel));
            }
        }
        return nodeModel;
    }

    private EmailInvokeNodeModel createEmailInvokeNode(
            HasChildModels parentModel) {
        EmailInvokeNodeModel email = new EmailInvokeNodeModel(parentModel);
        // TODO Auto-generated method stub
        return email;
    }

    private NodeModel createHumanTaskInvokeNode(HasChildModels parentModel) {
        HumanTaskInvokeNodeModel humanTask = new HumanTaskInvokeNodeModel(
                parentModel);
        // TODO Auto-generated method stub
        return humanTask;
    }

    private ContainerModel createFlowModel(Element flowNode,
            HasChildModels model) {
        FlowContainerModel flowModel = new FlowContainerModel(model);

        /* Set name id and graphics */
        flowModel.setName(flowNode.getAttribute(DWDLTagNames.name));
        flowModel.setId(new Long(flowNode.getAttribute(DWDLTagNames.id)));
        setGraphics(flowNode, flowModel);
        
        // JS inner children

        return flowModel;
    }

    private ContainerModel createIfModel(HasChildModels parentModel) {
        IfContainerModel ifModel = new IfContainerModel(parentModel);
        // TODO Auto-generated method stub
        return ifModel;
    }

    private ContainerModel crearteForEachModel(HasChildModels parentModel) {
        ForEachContainerModel forEachModel = new ForEachContainerModel();
        // TODO Auto-generated method stub
        return forEachModel;
    }

    private void setGraphics(Element node, NodeModel model) {
        Element graphics = getChildNodesByTagName(node, DWDLTagNames.graphics)
                .get(0);

        /* Set position */
        Integer left = new Integer(graphics.getAttribute(DWDLTagNames.x));
        Integer top = new Integer(graphics.getAttribute(DWDLTagNames.y));
        model.setChangeListenerPosition(left, top);

        /* If model is a container, it has also a with and size */
        if (model instanceof ContainerModel) {
            Integer height = new Integer(graphics
                    .getAttribute(DWDLTagNames.height));
            Integer width = new Integer(graphics
                    .getAttribute(DWDLTagNames.width));
            ((ContainerModel) model).setChangeListenerSize(width, height);
        }

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

    private List<Element> getChildNodesAsList(Element nodesContainer) {
        List<Element> list = new ArrayList<Element>();
        for (int i = 0; i < nodesContainer.getChildNodes().getLength(); i++) {
            list.add((Element) nodesContainer.getChildNodes().item(i));
        }
        return list;
    }

}
