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

package de.decidr.modelingtool.client.io.resources;

import de.decidr.modelingtool.client.io.DWDLParserImpl;
import de.decidr.modelingtool.client.io.WorkflowParserImpl;

/**
 * This class holds all tag and attribute names of a dwdl file as static field.
 * This is done because both {@link WorkflowParserImpl} and
 * {@link DWDLParserImpl} access this class and rely on the same tag and
 * attribute names.
 * 
 * @author Jonas Schlaak
 */
public class DWDLNames {

    /* general */
    public static String root = "workflow";
    public static String namespace = "targetNamespace";
    public static String schema = "xmlns";

    public static String id = "id";
    public static String name = "name";
    public static String description = "description";
    public static String label = "label";
    public static String variable = "variable";
    public static String faultHandler = "faultHandler";
    public static String setProperty = "setProperty";
    public static String yes = "yes";
    public static String no = "no";
    public static String type = "type";

    public static String nodes = "nodes";

    /* variables */
    public static String initValue = "initialValue";
    public static String initValues = "initialValues";
    public static String vNCnamePrefix = "ID";
    public static String variables = "variables";
    public static String listprefix = "list-";
    public static String configVar = "configurationVariable";
    public static VariableTypes variableTypes = new VariableTypes();

    /* roles */
    public static String role = "role";
    public static String roles = "roles";
    public static String actor = "actor";
    public static String userId = "userId";

    /* arcs */
    public static String arc = "arc";
    public static String arcId = "arcId";
    public static String arcs = "arcs";

    /* graphics */
    public static String graphics = "graphics";
    public static String height = "height";
    public static String width = "width";
    public static String x = "x";
    public static String y = "y";
    public static String source = "source";
    public static String sources = "sources";
    public static String target = "target";
    public static String targets = "targets";

    /* invoke nodes */
    public static String invokeNode = "invokeNode";
    public static String activity = "activity";

    /* email invoke node */
    public static String decidrEmail = "Decidr-Email";
    public static String to = "to";
    public static String cc = "cc";
    public static String bcc = "bcc";
    public static String subject = "subject";
    public static String message = "message";
    public static String attachment = "attachment";

    /* human task invoke node */
    public static String decidrHumanTask = "Decidr-HumanTask";
    public static String wfmId = "wfmID";
    public static String user = "user";
    public static String humanTaskData = "humanTaskData";
    public static String taskItem = "taskItem";
    public static String taskResult = "taskResult";
    public static String getProperty = "getProperty";
    public static String propertyValue = "propertyValue";
    public static String parameter = "parameter";
    public static String hint = "hint";
    public static String value = "value";

    /* flow node */
    public static String flowNode = "flowNode";

    /* for each node */
    public static String forEachNode = "forEachNode";
    public static String countername = "countername";
    public static String startCounterValue = "startCounterValue";
    public static String finalCounterValue = "finalCounterValue";
    public static String completionCon = "completionCondition";
    public static String parallel = "parallel";

    /* if node */
    public static String ifNode = "ifNode";
    public static String defaultCondition = "defaultCondition";
    public static String condition = "condition";
    public static String order = "order";
    public static String leftOp = "left-operand";
    public static String operator = "operator";
    public static String rightOp = "right-operand";

    /* start and end node */
    public static String endNode = "endNode";
    public static String notificationOfSuccess = "notificationOfSuccess";
    public static String recipient = "recipient";
    public static String startNode = "startNode";
    public static String successMsg = "successMessage";
    public static String userNotification = "userNotification";
}