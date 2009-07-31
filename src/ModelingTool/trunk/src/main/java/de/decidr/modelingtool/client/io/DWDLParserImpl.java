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

import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.model.WorkflowProperties;
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

        setWorkflowProperties(doc, model);
        setVariables(doc, model);
        setRoles(doc, model);

        return model;
    }

    private void setWorkflowProperties(Document doc, WorkflowModel model) {
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

        /* parse endNode and set success message and notification */
        Element endNode = getChildNodesByTagName(root, DWDLTagNames.endNode)
                .get(0);
        Element notification = getChildNodesByTagName(endNode,
                DWDLTagNames.notificationOfSuccess).get(0);
        properties
                .setSuccessMessageVariableId(getPropertyVariableIdFromElement(
                        notification, DWDLTagNames.successMsg));
        // JS check if notify yes/no

        model.setProperties(properties);
    }

    private void setVariables(Document doc, WorkflowModel model) {

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

    private void setRoles(Document doc, WorkflowModel model) {

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

}
