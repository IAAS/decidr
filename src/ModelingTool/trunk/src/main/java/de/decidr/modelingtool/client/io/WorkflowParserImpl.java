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

import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.ui.EmailInvokeNode;

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

        /* Create description child node */
        Element description = doc.createElement("description");
        Text descTest = doc.createTextNode("this is a simple workflow");
        description.appendChild(descTest);
        workflow.appendChild(description);

        /* Create variable child node */
        workflow.appendChild(createVariables(doc, model));

        /* Create role child node */
        // JS implement (also server get method in value editor)
        workflow.appendChild(createRoles(doc, model));

        /* Create fault handler node */
        workflow.appendChild(createFaultHandlerNode(doc, model));

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
        // JS implement
        return faultHandler;
    }

    private Node createEmailNode(Document doc, EmailInvokeNode node) {
        Element emailNode = doc.createElement("invokeNode");
        
        return emailNode;
    }
}
