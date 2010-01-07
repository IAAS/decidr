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

package de.decidr.modelingtool.client;

import java.util.HashMap;

import com.google.gwt.junit.client.GWTTestCase;

import de.decidr.modelingtool.client.io.DWDLParser;
import de.decidr.modelingtool.client.io.DWDLParserImpl;
import de.decidr.modelingtool.client.model.AbstractModel;
import de.decidr.modelingtool.client.model.ConnectionModel;
import de.decidr.modelingtool.client.model.ContainerExitConnectionModel;
import de.decidr.modelingtool.client.model.ContainerStartConnectionModel;
import de.decidr.modelingtool.client.model.EmailInvokeNodeModel;
import de.decidr.modelingtool.client.model.EndNodeModel;
import de.decidr.modelingtool.client.model.FlowContainerModel;
import de.decidr.modelingtool.client.model.NodeModel;
import de.decidr.modelingtool.client.model.StartNodeModel;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.model.foreach.ExitCondition;
import de.decidr.modelingtool.client.model.foreach.ForEachContainerModel;
import de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel;
import de.decidr.modelingtool.client.model.ifcondition.Condition;
import de.decidr.modelingtool.client.model.ifcondition.IfContainerModel;
import de.decidr.modelingtool.client.model.ifcondition.Operator;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class ModelingToolTest extends GWTTestCase {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.decidr.modelingtool.ModelingToolWidget";
    }

    /**
     * Holds the xmls which are used to test the parser.
     */
    private String general_dwdl = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><workflow name=\"Simple Workflow\" id=\"1255087539250\" targetNamespace=\"namespace\" xmlns=\"schema\"><description>This is a simple workflow.</description><variables><variable name=\"ID1255088324562\" label=\"Form Container\" type=\"form\" configurationVariable=\"no\"><initialValue>New Value</initialValue></variable><variable name=\"ID1255088196312\" label=\"Name\" type=\"string\" configurationVariable=\"no\"><initialValue>New Value</initialValue></variable><variable name=\"ID1255088188640\" label=\"Description\" type=\"string\" configurationVariable=\"no\"><initialValue>New Value</initialValue></variable><variable name=\"ID1255088176468\" label=\"Output\" type=\"string\" configurationVariable=\"no\"><initialValue>New Value</initialValue></variable><variable name=\"ID123\" label=\"Fault Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow failed</initialValue></variable><variable name=\"ID12\" label=\"Success Message\" type=\"string\" configurationVariable=\"yes\"><initialValue>Workflow succeded</initialValue></variable><variable name=\"ID768768\" label=\"Text\" type=\"string\" configurationVariable=\"yes\"><initialValue>Loram Ipsum</initialValue></variable><variable name=\"ID1256904131562\" label=\"Dates\" type=\"list-date\" configurationVariable=\"yes\"><initialValues><initialValue>2009-10-30</initialValue><initialValue>2013-09-11</initialValue><initialValue>2014-03-11</initialValue></initialValues></variable></variables><roles><role configurationVariable=\"no\" label=\"User\" name=\"ID1255088207843\"><actor userId=\"23\" /><actor userId=\"113\" /></role><role configurationVariable=\"no\" label=\"Recipient\" name=\"ID345\"><actor userId=\"decidradmin\" /></role></roles><faultHandler><setProperty name=\"message\" variable=\"ID123\" /><recipient><setProperty name=\"name\" variable=\"ID345\" /></recipient></faultHandler><nodes><invokeNode name=\"EmailInvokeNode\" id=\"1255088341203\" activity=\"Decidr-Email\"><description></description><graphics x=\"32\" y=\"96\" width=\"100\" height=\"60\" /><sources><source arcId=\"1255088341234\" /></sources><targets><target arcId=\"1255088341235\" /></targets><setProperty name=\"to\" variable=\"ID345\" /><setProperty name=\"cc\" variable=\"ID345\" /><setProperty name=\"bcc\" variable=\"ID1255088207843\" /><setProperty name=\"subject\" variable=\"ID768768\" /><setProperty name=\"message\" variable=\"ID768768\" /><setProperty name=\"attachment\" /></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1255088341250\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"184\" y=\"27\" width=\"100\" height=\"60\" /><targets><target arcId=\"1255088341234\" /></targets><sources><source arcId=\"1255088341281\" /></sources><setProperty name=\"wfmID\"><propertyValue>1255087539250</propertyValue></setProperty><setProperty name=\"user\" variable=\"ID1255088207843\" /><setProperty name=\"name\" variable=\"ID1255088196312\" /><setProperty name=\"description\" variable=\"ID1255088188640\" /><setProperty name=\"userNotification\"><propertyValue>yes</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"ID1255088324562\" />  <parameter><humanTaskData><taskItem name=\"NC74643\" variable=\"ID1255088176468\" type=\"string\"><label>Label 1</label><hint>First</hint><value>New Value</value></taskItem><taskItem name=\"NC74647\" variable=\"ID1255088176468\" type=\"string\"><label>Label 2</label><hint>Second</hint><value>New Value</value></taskItem></humanTaskData></parameter></invokeNode><flowNode name=\"FlowContainer\" id=\"1255088341296\"><description></description><graphics x=\"176\" y=\"128\" width=\"311\" height=\"154\" /><targets><target arcId=\"1255088341281\" /></targets><sources><source arcId=\"1255088341500\" /></sources><nodes><invokeNode name=\"EmailInvokeNode\" id=\"1255088341329\" activity=\"Decidr-Email\"><description></description><graphics x=\"23\" y=\"54\" width=\"100\" height=\"60\" /><sources><source arcId=\"1255088341312\" /></sources><targets><target arcId=\"1255088341359\" /></targets><setProperty name=\"to\" /><setProperty name=\"cc\" /><setProperty name=\"bcc\" /><setProperty name=\"subject\" /><setProperty name=\"message\" /><setProperty name=\"attachment\" /></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1255088341360\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"172\" y=\"54\" width=\"100\" height=\"60\" /><targets><target arcId=\"1255088341328\" /></targets><sources><source arcId=\"1255088341406\" /></sources><setProperty name=\"wfmID\"><propertyValue>1255087539250</propertyValue></setProperty><setProperty name=\"user\" /><setProperty name=\"name\" /><setProperty name=\"description\" /><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"IDnull\" /><parameter><humanTaskData /></parameter></invokeNode></nodes><arcs><arc name=\"\" id=\"1255088341359\" source=\"1255088341296\" target=\"1255088341329\" /><arc name=\"\" id=\"1255088341312\" source=\"1255088341329\" target=\"1255088341296\" /><arc name=\"\" id=\"1255088341406\" source=\"1255088341360\" target=\"1255088341296\" /><arc name=\"\" id=\"1255088341328\" source=\"1255088341296\" target=\"1255088341360\" /></arcs></flowNode><forEachNode name=\"de.decidr.modelingtool.client.model.foreach.ForEachContainerModel\" id=\"1255341672812\" countername=\"ID345\" parallel=\"yes\"><description></description><graphics x=\"316\" y=\"67\" width=\"127\" height=\"163\" /><startCounterValue>1</startCounterValue><finalCounterValue>L345</finalCounterValue><completionCondition>XOR</completionCondition><nodes /><arcs /></forEachNode><endNode name=\"EndNode\" id=\"1255087539312\"><description></description><graphics x=\"495\" y=\"325\" width=\"50\" height=\"30\" /><targets><target arcId=\"1255088341500\" /></targets><notificationOfSuccess><setProperty name=\"successMessage\" variable=\"ID12\" /><setProperty name=\"recipient\" variable=\"ID345\" /></notificationOfSuccess></endNode><startNode name=\"StartNode\" id=\"1255087539343\"><description></description><graphics x=\"25\" y=\"25\" width=\"50\" height=\"30\" /><sources><source arcId=\"1255088341235\" /></sources></startNode></nodes><arcs><arc name=\"\" id=\"1255088341281\" source=\"1255088341250\" target=\"1255088341296\" /><arc name=\"\" id=\"1255088341235\" source=\"1255087539343\" target=\"1255088341203\" /><arc name=\"\" id=\"1255088341234\" source=\"1255088341203\" target=\"1255088341250\" /><arc name=\"\" id=\"1255088341500\" source=\"1255088341296\" target=\"1255087539312\" /></arcs></workflow>";
    private String flowNode_dwdl = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><workflow name=\"Simple Workflow\" id=\"1256287630390\" targetNamespace=\"namespace\" xmlns=\"schema\"><description></description><variables><variable name=\"ID123\" label=\"Fault Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow failed</initialValue></variable><variable name=\"ID12\" label=\"Success Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow succeded</initialValue></variable><variable name=\"ID768768\" label=\"Text\" type=\"string\" configurationVariable=\"yes\"><initialValue>Loram Ipsum</initialValue></variable></variables><roles><role configurationVariable=\"no\" label=\"Recipient\" name=\"ID345\"><actor userId=\"decidradmin\" /></role></roles><faultHandler><setProperty name=\"message\" variable=\"ID123\" /><recipient><setProperty name=\"name\" variable=\"ID345\" /></recipient></faultHandler><nodes><flowNode name=\"de.decidr.modelingtool.client.model.FlowContainerModel\" id=\"1256287630468\"><description></description><graphics x=\"378\" y=\"213\" width=\"357\" height=\"194\" /><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1256287630515\" activity=\"Decidr-Email\"><description></description><graphics x=\"228\" y=\"65\" width=\"100\" height=\"60\" /><sources><source arcId=\"1256287630531\" /></sources><targets><target arcId=\"1256287630546\" /></targets><setProperty name=\"to\" /><setProperty name=\"cc\" /><setProperty name=\"bcc\" /><setProperty name=\"subject\" /><setProperty name=\"message\" /><setProperty name=\"attachment\" /></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1256287630547\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"31\" y=\"53\" width=\"100\" height=\"60\" /><sources><source arcId=\"1256287630546\" /></sources><targets><target arcId=\"1256287630531\" /></targets><setProperty name=\"wfmID\"><propertyValue>1256287630390</propertyValue></setProperty><setProperty name=\"user\" /><setProperty name=\"name\" /><setProperty name=\"description\" /><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"IDnull\" /><parameter><humanTaskData /></parameter></invokeNode></nodes><arcs><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1256287630546\" source=\"1256287630547\" target=\"1256287630515\" /><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1256287630531\" source=\"1256287630515\" target=\"1256287630547\" /></arcs></flowNode><startNode name=\"de.decidr.modelingtool.client.model.StartNodeModel\" id=\"1256287630593\"><description></description><graphics x=\"25\" y=\"25\" width=\"50\" height=\"30\" /><sources><source arcId=\"1256287630625\" /></sources></startNode><flowNode name=\"de.decidr.modelingtool.client.model.FlowContainerModel\" id=\"1256287630626\"><description></description><graphics x=\"35\" y=\"120\" width=\"318\" height=\"315\" /><sources><source arcId=\"1256287630656\" /></sources><targets><target arcId=\"1256287630625\" /></targets><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1256287630657\" activity=\"Decidr-Email\"><description></description><graphics x=\"28\" y=\"191\" width=\"100\" height=\"60\" /><sources><source arcId=\"1256287630687\" /></sources><targets><target arcId=\"1256287630688\" /></targets><setProperty name=\"to\" /><setProperty name=\"cc\" /><setProperty name=\"bcc\" /><setProperty name=\"subject\" /><setProperty name=\"message\" /><setProperty name=\"attachment\" /></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1256287630689\" activity=\"Decidr-Email\"><description></description><graphics x=\"180\" y=\"71\" width=\"100\" height=\"60\" /><sources><source arcId=\"1256287630718\" /></sources><targets><target arcId=\"1256287630719\" /></targets><setProperty name=\"to\" /><setProperty name=\"cc\" /><setProperty name=\"bcc\" /><setProperty name=\"subject\" /><setProperty name=\"message\" /><setProperty name=\"attachment\" /></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1256287630734\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"175\" y=\"186\" width=\"100\" height=\"60\" /><sources><source arcId=\"1256287630765\" /></sources><targets><target arcId=\"1256287630718\" /></targets><setProperty name=\"wfmID\"><propertyValue>1256287630390</propertyValue></setProperty><setProperty name=\"user\" /><setProperty name=\"name\" /><setProperty name=\"description\" /><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"IDnull\" /><parameter><humanTaskData /></parameter></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1256287630781\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"30\" y=\"69\" width=\"100\" height=\"60\" /><sources><source arcId=\"1256287630688\" /></sources><targets><target arcId=\"1256287630812\" /></targets><setProperty name=\"wfmID\"><propertyValue>1256287630390</propertyValue></setProperty><setProperty name=\"user\" /><setProperty name=\"name\" /><setProperty name=\"description\" /><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"IDnull\" /><parameter><humanTaskData /></parameter></invokeNode></nodes><arcs><arc name=\"de.decidr.modelingtool.client.model.ContainerStartConnectionModel\" id=\"1256287630719\" source=\"1256287630626\" target=\"1256287630689\" /><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1256287630718\" source=\"1256287630689\" target=\"1256287630734\" /><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1256287630688\" source=\"1256287630781\" target=\"1256287630657\" /><arc name=\"de.decidr.modelingtool.client.model.ContainerExitConnectionModel\" id=\"1256287630765\" source=\"1256287630734\" target=\"1256287630626\" /><arc name=\"de.decidr.modelingtool.client.model.ContainerStartConnectionModel\" id=\"1256287630812\" source=\"1256287630626\" target=\"1256287630781\" /><arc name=\"de.decidr.modelingtool.client.model.ContainerExitConnectionModel\" id=\"1256287630687\" source=\"1256287630657\" target=\"1256287630626\" /></arcs></flowNode><flowNode name=\"de.decidr.modelingtool.client.model.FlowContainerModel\" id=\"1256287630828\"><description></description><graphics x=\"374\" y=\"45\" width=\"362\" height=\"150\" /><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1256287630859\" activity=\"Decidr-Email\"><description></description><graphics x=\"256\" y=\"57\" width=\"100\" height=\"60\" /><sources><source arcId=\"1256287630890\" /></sources><setProperty name=\"to\" /><setProperty name=\"cc\" /><setProperty name=\"bcc\" /><setProperty name=\"subject\" /><setProperty name=\"message\" /><setProperty name=\"attachment\" /></invokeNode><forEachNode name=\"de.decidr.modelingtool.client.model.foreach.ForEachContainerModel\" id=\"1256287630906\" parallel=\"no\"><description></description><graphics x=\"151\" y=\"62\" width=\"58\" height=\"45\" /></forEachNode><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1256287630937\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"12\" y=\"56\" width=\"100\" height=\"60\" /><targets><target arcId=\"1256287630953\" /></targets><setProperty name=\"wfmID\"><propertyValue>1256287630390</propertyValue></setProperty><setProperty name=\"user\" /><setProperty name=\"name\" /><setProperty name=\"description\" /><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"IDnull\" /><parameter><humanTaskData /></parameter></invokeNode></nodes><arcs><arc name=\"de.decidr.modelingtool.client.model.ContainerStartConnectionModel\" id=\"1256287630953\" source=\"1256287630828\" target=\"1256287630937\" /><arc name=\"de.decidr.modelingtool.client.model.ContainerExitConnectionModel\" id=\"1256287630890\" source=\"1256287630859\" target=\"1256287630828\" /></arcs></flowNode><endNode name=\"de.decidr.modelingtool.client.model.EndNodeModel\" id=\"1256287630968\"><description></description><graphics x=\"614\" y=\"469\" width=\"50\" height=\"30\" /><targets><target arcId=\"1256287630656\" /></targets><notificationOfSuccess><setProperty name=\"successMessage\" variable=\"ID12\" /><setProperty name=\"recipient\" variable=\"ID345\" /></notificationOfSuccess></endNode></nodes><arcs><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1256287630625\" source=\"1256287630593\" target=\"1256287630626\" /><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1256287630656\" source=\"1256287630626\" target=\"1256287630968\" /></arcs></workflow>";
    private String forEach_dwdl = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><workflow name=\"Simple Workflow\" id=\"1255956748515\" targetNamespace=\"namespace\" xmlns=\"schema\"><description></description><variables><variable name=\"ID123\" label=\"Fault Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow failed</initialValue></variable><variable name=\"ID12\" label=\"Success Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow succeded</initialValue></variable><variable name=\"ID768768\" label=\"Text\" type=\"string\" configurationVariable=\"yes\"><initialValue>Loram Ipsum</initialValue></variable></variables><roles><role configurationVariable=\"no\" label=\"Recipient\" name=\"ID345\"><actor userId=\"decidradmin\" /></role></roles><faultHandler><setProperty name=\"message\" variable=\"ID123\" /><recipient><setProperty name=\"name\" variable=\"ID345\" /></recipient></faultHandler><nodes><startNode name=\"de.decidr.modelingtool.client.model.StartNodeModel\" id=\"1255956748593\"><description></description><graphics x=\"25\" y=\"25\" width=\"50\" height=\"30\" /><sources><source arcId=\"1255956748640\" /></sources></startNode><endNode name=\"de.decidr.modelingtool.client.model.EndNodeModel\" id=\"1255956748656\"><description></description><graphics x=\"690\" y=\"499\" width=\"50\" height=\"30\" /><targets><target arcId=\"1255956748671\" /></targets><notificationOfSuccess><setProperty name=\"successMessage\" variable=\"ID12\" /><setProperty name=\"recipient\" variable=\"ID345\" /></notificationOfSuccess></endNode><forEachNode name=\"de.decidr.modelingtool.client.model.foreach.ForEachContainerModel\" id=\"1255956748687\" parallel=\"no\"><description></description><graphics x=\"57\" y=\"154\" width=\"200\" height=\"150\" /></forEachNode><forEachNode name=\"de.decidr.modelingtool.client.model.foreach.ForEachContainerModel\" id=\"1255956748703\" countername=\"ID768768\" parallel=\"yes\"><description></description><graphics x=\"367\" y=\"126\" width=\"312\" height=\"279\" /><sources><source arcId=\"1255956748671\" /></sources><targets><target arcId=\"1255956748640\" /></targets><startCounterValue>1</startCounterValue><finalCounterValue>ID768768</finalCounterValue><completionCondition>XOR</completionCondition><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1255956748750\" activity=\"Decidr-Email\"><description></description><graphics x=\"119\" y=\"123\" width=\"100\" height=\"60\" /><sources><source arcId=\"1255956748765\" /></sources><targets><target arcId=\"1255956748781\" /></targets><setProperty name=\"to\" /><setProperty name=\"cc\" /><setProperty name=\"bcc\" /><setProperty name=\"subject\" /><setProperty name=\"message\" /><setProperty name=\"attachment\" /></invokeNode></nodes><arcs><arc name=\"de.decidr.modelingtool.client.model.ContainerExitConnectionModel\" id=\"1255956748765\" source=\"1255956748750\" target=\"1255956748703\" /><arc name=\"de.decidr.modelingtool.client.model.ContainerStartConnectionModel\" id=\"1255956748781\" source=\"1255956748703\" target=\"1255956748750\" /></arcs></forEachNode></nodes><arcs><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1255956748671\" source=\"1255956748703\" target=\"1255956748656\" /><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1255956748640\" source=\"1255956748593\" target=\"1255956748703\" /></arcs></workflow>";
    private String ifNode_dwdl = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><workflow name=\"Simple Workflow\" id=\"1256211693359\" targetNamespace=\"namespace\" xmlns=\"schema\"><description></description><variables><variable name=\"ID123\" label=\"Fault Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow failed</initialValue></variable><variable name=\"ID12\" label=\"Success Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow succeded</initialValue></variable><variable name=\"ID768768\" label=\"Text\" type=\"string\" configurationVariable=\"yes\"><initialValue>Loram Ipsum</initialValue></variable></variables><roles><role configurationVariable=\"no\" label=\"Recipient\" name=\"ID345\"><actor userId=\"decidradmin\" /></role></roles><faultHandler><setProperty name=\"message\" variable=\"ID123\" /><recipient><setProperty name=\"name\" variable=\"ID345\" /></recipient></faultHandler><nodes><startNode name=\"de.decidr.modelingtool.client.model.StartNodeModel\" id=\"1256211693437\"><description></description><graphics x=\"25\" y=\"25\" width=\"50\" height=\"30\" /></startNode><ifNode name=\"de.decidr.modelingtool.client.model.ifcondition.IfContainerModel\" id=\"1256211850125\"><description></description><graphics x=\"380\" y=\"94\" width=\"381\" height=\"295\" /><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1256211850156\" activity=\"Decidr-Email\"><description></description><graphics x=\"40\" y=\"76\" width=\"100\" height=\"60\" /><setProperty name=\"to\" /><setProperty name=\"cc\" /><setProperty name=\"bcc\" /><setProperty name=\"subject\" /><setProperty name=\"message\" /><setProperty name=\"attachment\" /></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1256211850187\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"47\" y=\"169\" width=\"100\" height=\"60\" /><sources><source arcId=\"1256211850218\" /></sources><targets><target arcId=\"1256211850219\" /></targets><setProperty name=\"wfmID\"><propertyValue>1256211693359</propertyValue></setProperty><setProperty name=\"user\" /><setProperty name=\"name\" /><setProperty name=\"description\" /><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"IDnull\" /><parameter><humanTaskData /></parameter></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1256211850234\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"230\" y=\"79\" width=\"100\" height=\"60\" /><sources><source arcId=\"1256211850219\" /></sources><targets><target arcId=\"1256211850218\" /></targets><setProperty name=\"wfmID\"><propertyValue>1256211693359</propertyValue></setProperty><setProperty name=\"user\" /><setProperty name=\"name\" /><setProperty name=\"description\" /><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"IDnull\" /><parameter><humanTaskData /></parameter></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1256211850281\" activity=\"Decidr-Email\"><description></description><graphics x=\"229\" y=\"160\" width=\"100\" height=\"60\" /><sources><source arcId=\"1256211850296\" /></sources><setProperty name=\"to\" /><setProperty name=\"cc\" /><setProperty name=\"bcc\" /><setProperty name=\"subject\" /><setProperty name=\"message\" /><setProperty name=\"attachment\" /></invokeNode></nodes><arcs><arc name=\"de.decidr.modelingtool.client.model.ContainerExitConnectionModel\" id=\"1256211850296\" source=\"1256211850281\" target=\"1256211850125\" /><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1256211850219\" source=\"1256211850234\" target=\"1256211850187\" /><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1256211850218\" source=\"1256211850187\" target=\"1256211850234\" /></arcs></ifNode><ifNode name=\"de.decidr.modelingtool.client.model.ifcondition.IfContainerModel\" id=\"1257415091656\"><description></description><graphics x=\"140\" y=\"126\" width=\"389\" height=\"286\" /><condition id=\"1257415091671\"><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1257415091672\" activity=\"Decidr-Email\"><description></description><graphics x=\"221\" y=\"100\" width=\"100\" height=\"60\" /><targets><target arcId=\"1257415091671\" /></targets><setProperty name=\"to\" /><setProperty name=\"cc\" /><setProperty name=\"bcc\" /><setProperty name=\"subject\" /><setProperty name=\"message\" /><setProperty name=\"attachment\" /></invokeNode></nodes><arcs><arc name=\"Condition 4\" id=\"1257415091671\" source=\"1257415091656\" target=\"1257415091672\" /></arcs></condition><condition id=\"1257415091718\"><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1257415091719\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"43\" y=\"97\" width=\"100\" height=\"60\" /><targets><target arcId=\"1257415091718\" /></targets><setProperty name=\"wfmID\"><propertyValue>1256211693359</propertyValue></setProperty><setProperty name=\"user\" /><setProperty name=\"name\" /><setProperty name=\"description\" /><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"IDnull\">  </getProperty><parameter><humanTaskData /></parameter></invokeNode></nodes><arcs><arc name=\"Condition 3\" id=\"1257415091718\" source=\"1257415091656\" target=\"1257415091719\" /></arcs></condition></ifNode><ifNode name=\"de.decidr.modelingtool.client.model.ifcondition.IfContainerModel\" id=\"1256211693453\"><description></description><graphics x=\"29\" y=\"148\" width=\"282\" height=\"326\" /><condition order=\"0\" defaultCondition=\"yes\" id=\"1256211684046\"><left-operand>ID12</left-operand><operator>!=</operator><right-operand>ID768768</right-operand><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1256211693484\" activity=\"Decidr-Email\"><description></description><graphics x=\"18\" y=\"86\" width=\"100\" height=\"60\" /><sources><source arcId=\"1256211850375\" /></sources><targets><target arcId=\"1256211684046\" /></targets><setProperty name=\"to\" /><setProperty name=\"cc\" /><setProperty name=\"bcc\" /><setProperty name=\"subject\" /><setProperty name=\"message\" /><setProperty name=\"attachment\" /></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1256211850343\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"19\" y=\"193\" width=\"100\" height=\"60\" /><sources><source arcId=\"1256211850421\" /></sources><targets><target arcId=\"1256211850375\" /></targets><setProperty name=\"wfmID\"><propertyValue>1256211693359</propertyValue></setProperty>  <setProperty name=\"user\" /><setProperty name=\"name\" /><setProperty name=\"description\" /><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"IDnull\" /><parameter><humanTaskData /></parameter></invokeNode></nodes><arcs><arc name=\"Condition 1\" id=\"1256211684046\" source=\"1256211693453\" target=\"1256211693484\" /><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1256211850375\" source=\"1256211693484\" target=\"1256211850343\" /><arc name=\"de.decidr.modelingtool.client.model.ContainerExitConnectionModel\" id=\"1256211850421\" source=\"1256211850343\" target=\"1256211693453\" /></arcs></condition><condition order=\"1\" defaultCondition=\"no\" id=\"1256211684048\"><left-operand>ID12</left-operand><operator>?=</operator><right-operand>ID123</right-operand><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1256211693546\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"173\" y=\"85\" width=\"100\" height=\"60\" /><targets><target arcId=\"1256211684048\" /></targets><setProperty name=\"wfmID\"><propertyValue>1256211693359</propertyValue></setProperty><setProperty name=\"user\" /><setProperty name=\"name\" /><setProperty name=\"description\" /><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"IDnull\" /><parameter><humanTaskData /></parameter></invokeNode></nodes><arcs><arc name=\"Condition 2\" id=\"1256211684048\" source=\"1256211693453\" target=\"1256211693546\" /></arcs></condition><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1256211850500\" activity=\"Decidr-Email\"><description></description><graphics x=\"172\" y=\"192\" width=\"100\" height=\"60\" /><setProperty name=\"to\" /><setProperty name=\"cc\" /><setProperty name=\"bcc\" /><setProperty name=\"subject\" /><setProperty name=\"message\" /><setProperty name=\"attachment\" /></invokeNode></nodes></ifNode><endNode name=\"de.decidr.modelingtool.client.model.EndNodeModel\" id=\"1256211693578\"><description></description><graphics x=\"642\" y=\"475\" width=\"50\" height=\"30\" /><notificationOfSuccess><setProperty name=\"successMessage\" variable=\"ID12\" /><setProperty name=\"recipient\" variable=\"ID345\" /></notificationOfSuccess></endNode></nodes><arcs /></workflow>";
    private String connections_dwdl = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><workflow name=\"Simple Workflow\" id=\"1255353235687\" targetNamespace=\"namespace\" xmlns=\"schema\"><description></description><variables><variable name=\"ID123\" label=\"Fault Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow failed</initialValue></variable><variable name=\"ID12\" label=\"Success Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow succeded</initialValue></variable><variable name=\"ID768768\" label=\"Text\" type=\"string\" configurationVariable=\"yes\"><initialValue>Loram Ipsum</initialValue></variable></variables><roles><role configurationVariable=\"no\" label=\"Recipient\" name=\"ID345\"><actor userId=\"decidradmin\" /></role></roles><faultHandler><setProperty name=\"message\" variable=\"ID123\" /><recipient><setProperty name=\"name\" variable=\"ID345\" /></recipient></faultHandler><nodes><startNode name=\"de.decidr.modelingtool.client.model.StartNodeModel\" id=\"1255353235765\"><description></description><graphics x=\"25\" y=\"25\" width=\"50\" height=\"30\" /><sources><source arcId=\"1255353235812\" /></sources></startNode><flowNode name=\"de.decidr.modelingtool.client.model.FlowContainerModel\" id=\"1255353235813\"><description></description><graphics x=\"105\" y=\"102\" width=\"505\" height=\"326\" /><sources><source arcId=\"1255353235843\" /></sources><targets><target arcId=\"1255353235812\" /></targets><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1255353235844\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"57\" y=\"142\" width=\"100\" height=\"60\" /><sources><source arcId=\"1255353235875\" /></sources><targets><target arcId=\"1255353235876\" /></targets><setProperty name=\"wfmID\"><propertyValue>1255353235687</propertyValue></setProperty><setProperty name=\"user\" /><setProperty name=\"name\" /><setProperty name=\"description\" /><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"IDnull\" />  <parameter><humanTaskData /></parameter></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1255353235890\" activity=\"Decidr-Email\"><description></description><graphics x=\"310\" y=\"145\" width=\"100\" height=\"60\" /><sources><source arcId=\"1255353235921\" /></sources><targets><target arcId=\"1255353235922\" /></targets><setProperty name=\"to\" /><setProperty name=\"cc\" /><setProperty name=\"bcc\" /><setProperty name=\"subject\" /><setProperty name=\"message\" /><setProperty name=\"attachment\" /></invokeNode></nodes><arcs><arc name=\"de.decidr.modelingtool.client.model.ContainerStartConnectionModel\" id=\"1255353235876\" source=\"1255353235813\" target=\"1255353235844\" /><arc name=\"de.decidr.modelingtool.client.model.ContainerExitConnectionModel\" id=\"1255353235921\" source=\"1255353235890\" target=\"1255353235813\" /><arc name=\"de.decidr.modelingtool.client.model.ContainerExitConnectionModel\" id=\"1255353235875\" source=\"1255353235844\" target=\"1255353235813\" /><arc name=\"de.decidr.modelingtool.client.model.ContainerStartConnectionModel\" id=\"1255353235922\" source=\"1255353235813\" target=\"1255353235890\" /></arcs></flowNode><endNode name=\"de.decidr.modelingtool.client.model.EndNodeModel\" id=\"1255353235937\"><description></description><graphics x=\"619\" y=\"480\" width=\"50\" height=\"30\" /><targets><target arcId=\"1255353235843\" /></targets><notificationOfSuccess><setProperty name=\"successMessage\" variable=\"ID12\" /><setProperty name=\"recipient\" variable=\"ID345\" /></notificationOfSuccess></endNode></nodes><arcs><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1255353235812\" source=\"1255353235765\" target=\"1255353235813\" /><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1255353235843\" source=\"1255353235813\" target=\"1255353235937\" /></arcs></workflow>";
    private Long workflowId = new Long("1255087539250");

    private Long formVarId = new Long("1255088324562");
    private Long successVarId = new Long("12");
    private Long faultVarId = new Long("123");
    private Long textVarId = new Long("768768");
    private Long nameVarId = new Long("1255088196312");
    private Long descVarId = new Long("1255088188640");
    private Long outputVarId = new Long("1255088176468");

    private Long recipientVarId = new Long("345");
    private Long userVarId = new Long("1255088207843");

    private Long startNodeId = new Long("1255087539343");
    private Long endNodeId = new Long("1255087539312");
    private Long emailNodeId = new Long("1255088341203");
    private Long humanTaskNodeId = new Long("1255088341250");
    private Long flowNodeId = new Long("1255088341296");

    public void testWorkflowProperties() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(general_dwdl);

        assertEquals("Simple Workflow", model.getName());
        assertEquals(workflowId, model.getId());
        assertEquals("This is a simple workflow.", model.getDescription());

        assertEquals("namespace", model.getProperties().getNamespace());
        assertEquals("schema", model.getProperties().getSchema());

        assertEquals(faultVarId, model.getProperties()
                .getFaultMessageVariableId());

        assertEquals(new Boolean(true), model.getProperties()
                .getNotifyOnSuccess());
        assertEquals(recipientVarId, model.getProperties()
                .getRecipientVariableId());
        assertEquals(successVarId, model.getProperties()
                .getSuccessMessageVariableId());
    }

    public void testVariables() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(general_dwdl);

        assertEquals(10, model.getVariables().size());

        assertNotNull(model.getVariable(textVarId));
        Variable text = model.getVariable(textVarId);

        assertEquals("Text", text.getLabel());
        assertEquals(VariableType.STRING, text.getType());
        assertEquals(new Boolean(true), text.isConfig());

        assertEquals(false, text.isArray());
        assertEquals(1, text.getValues().size());
        assertEquals("Loram Ipsum", text.getValues().get(0));

        Variable user = model.getVariable(userVarId);
        assertEquals(VariableType.ROLE, user.getType());
        assertEquals(2, user.getValues().size());
        assertEquals(false, user.getValues().contains("12"));
        assertEquals(true, user.getValues().contains("23"));

        Variable dates = model.getVariable(new Long(1256904131562L));
        assertEquals(VariableType.DATE, dates.getType());
        assertEquals(3, dates.getValues().size());
        assertEquals("Dates", dates.getLabel());
        assertEquals(true, dates.getValues().contains("2009-10-30"));
    }

    public void testRoles() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(general_dwdl);

        assertNotNull(model.getVariable(userVarId));
        Variable user = model.getVariable(userVarId);

        assertEquals("User", user.getLabel());
        assertEquals(VariableType.ROLE, user.getType());
        assertEquals(new Boolean(false), user.isConfig());

        assertEquals(true, user.isArray());
        assertEquals(2, user.getValues().size());
        assertEquals("23", user.getValues().get(0));
        assertEquals("113", user.getValues().get(1));
    }

    public void testStartNode() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(general_dwdl);

        StartNodeModel startNodeModel = null;
        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof StartNodeModel) {
                startNodeModel = (StartNodeModel) nodeModel;
            }
        }
        if (startNodeModel == null) {
            fail("Couldn't find a StartNodeModel - please check sample DWDL");
            // returning despite fail since the java compiler doesn't know about
            // the unchecked Exception thrown
            return;
        }

        assertEquals(startNodeId, startNodeModel.getId());
        assertEquals(25, startNodeModel.getChangeListenerLeft());
        assertEquals(25, startNodeModel.getChangeListenerTop());
        assertEquals(emailNodeId, startNodeModel.getOutput().getTarget()
                .getId());
        assertNull(startNodeModel.getInput());
    }

    public void testEndNode() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(general_dwdl);

        EndNodeModel endNodeModel = null;
        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof EndNodeModel) {
                endNodeModel = (EndNodeModel) nodeModel;
            }
        }
        if (endNodeModel == null) {
            fail("Couldn't find a EndNodeModel - please check sample DWDL");
            // returning despite fail since the java compiler doesn't know about
            // the unchecked Exception thrown
            return;
        }

        assertEquals(endNodeId, endNodeModel.getId());
        assertEquals(495, endNodeModel.getChangeListenerLeft());
        assertEquals(325, endNodeModel.getChangeListenerTop());
        assertEquals(flowNodeId, endNodeModel.getInput().getSource().getId());
        assertNull(endNodeModel.getOutput());
    }

    public void testEmailInvokeNode() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(general_dwdl);

        EmailInvokeNodeModel emailNodeModel = null;
        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof EmailInvokeNodeModel
                    && nodeModel.getId().equals(emailNodeId)) {
                emailNodeModel = (EmailInvokeNodeModel) nodeModel;
            }
        }
        if (emailNodeModel == null) {
            fail("Couldn't find a EmailInvokeNodeModel - please check sample DWDL");
            // returning despite fail since the java compiler doesn't know about
            // the unchecked Exception thrown
            return;
        }

        assertEquals(emailNodeId, emailNodeModel.getId());
        assertEquals(32, emailNodeModel.getChangeListenerLeft());
        assertEquals(96, emailNodeModel.getChangeListenerTop());
        assertEquals(startNodeId, emailNodeModel.getInput().getSource().getId());
        assertEquals(humanTaskNodeId, emailNodeModel.getOutput().getTarget()
                .getId());

        assertEquals(recipientVarId, emailNodeModel.getToVariableId());
        assertEquals(recipientVarId, emailNodeModel.getCcVariableId());
        assertEquals(userVarId, emailNodeModel.getBccVariableId());
        assertEquals(textVarId, emailNodeModel.getSubjectVariableId());
        assertEquals(textVarId, emailNodeModel.getMessageVariableId());
        assertNull(emailNodeModel.getAttachmentVariableId());
    }

    public void testHumanTaskInvokeNode() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(general_dwdl);

        HumanTaskInvokeNodeModel humanTaskNodeModel = null;
        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof HumanTaskInvokeNodeModel
                    && nodeModel.getId().equals(humanTaskNodeId)) {
                humanTaskNodeModel = (HumanTaskInvokeNodeModel) nodeModel;
            }
        }
        if (humanTaskNodeModel == null) {
            fail("Couldn't find a HumanTaskInvokeNodeModel - please check sample DWDL");
            // returning despite fail since the java compiler doesn't know about
            // the unchecked Exception thrown
            return;
        }

        assertEquals(humanTaskNodeId, humanTaskNodeModel.getId());
        assertEquals(184, humanTaskNodeModel.getChangeListenerLeft());
        assertEquals(27, humanTaskNodeModel.getChangeListenerTop());
        assertEquals(emailNodeId, humanTaskNodeModel.getInput().getSource()
                .getId());
        assertEquals(flowNodeId, humanTaskNodeModel.getOutput().getTarget()
                .getId());

        assertEquals(userVarId, humanTaskNodeModel.getUserVariableId());
        assertEquals(nameVarId, humanTaskNodeModel.getWorkItemNameVariableId());
        assertEquals(descVarId, humanTaskNodeModel
                .getWorkItemDescriptionVariableId());
        assertEquals(formVarId, humanTaskNodeModel.getFormVariableId());
        assertEquals(true, humanTaskNodeModel.getNotifyActor().booleanValue());

        assertEquals(2, humanTaskNodeModel.getTaskItems().size());
        assertEquals(outputVarId, humanTaskNodeModel.getTaskItems().get(0)
                .getVariableId());
        assertEquals("Label 1", humanTaskNodeModel.getTaskItems().get(0)
                .getLabel());
        assertEquals("First", humanTaskNodeModel.getTaskItems().get(0)
                .getHint());
        assertEquals(outputVarId, humanTaskNodeModel.getTaskItems().get(1)
                .getVariableId());
        assertEquals("Label 2", humanTaskNodeModel.getTaskItems().get(1)
                .getLabel());
        assertEquals("Second", humanTaskNodeModel.getTaskItems().get(1)
                .getHint());
    }

    public void testFlowNode() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(flowNode_dwdl);

        Long workflowId = 1256287630390L;
        Long flowNode1Id = 1256287630468L;
        Long flowNode2Id = 1256287630626L;
        Long flowNode3Id = 1256287630828L;

        FlowContainerModel flowNode1 = null;
        FlowContainerModel flowNode2 = null;
        FlowContainerModel flowNode3 = null;

        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof FlowContainerModel
                    && nodeModel.getId().equals(flowNode1Id)) {
                flowNode1 = (FlowContainerModel) nodeModel;
            }
            if (nodeModel instanceof FlowContainerModel
                    && nodeModel.getId().equals(flowNode2Id)) {
                flowNode2 = (FlowContainerModel) nodeModel;
            }
            if (nodeModel instanceof FlowContainerModel
                    && nodeModel.getId().equals(flowNode3Id)) {
                flowNode3 = (FlowContainerModel) nodeModel;
            }
        }
        if (flowNode1 == null || flowNode2 == null || flowNode3 == null) {
            fail("Couldn't find a FlowContainerModel - please check sample DWDL");
            // returning despite fail since the java compiler doesn't know about
            // the unchecked Exception thrown
            return;
        }

        /* Test first flow node */
        assertEquals(workflowId, ((AbstractModel) flowNode1.getParentModel())
                .getId());
        assertEquals(2, flowNode1.getChildNodeModels().size());
        assertEquals(2, flowNode1.getChildConnectionModels().size());

        for (NodeModel childNode : flowNode1.getChildNodeModels()) {
            assertEquals(childNode.getId(), childNode.getOutput().getTarget()
                    .getOutput().getTarget().getId());
            assertEquals(flowNode1Id, ((AbstractModel) childNode
                    .getParentModel()).getId());
        }

        /* Test second flow node */
        assertEquals(workflowId, ((AbstractModel) flowNode2.getParentModel())
                .getId());
        assertEquals(4, flowNode2.getChildNodeModels().size());
        assertEquals(6, flowNode2.getChildConnectionModels().size());

        for (NodeModel childNode : flowNode2.getChildNodeModels()) {
            assertEquals(flowNode2Id, ((AbstractModel) childNode
                    .getParentModel()).getId());
        }
        for (ConnectionModel childConnection : flowNode2
                .getChildConnectionModels()) {
            assertEquals(flowNode2Id, ((AbstractModel) childConnection
                    .getParentModel()).getId());
        }

        /* Test third flow node */
        assertEquals(workflowId, ((AbstractModel) flowNode3.getParentModel())
                .getId());
        assertEquals(3, flowNode3.getChildNodeModels().size());
        assertEquals(2, flowNode3.getChildConnectionModels().size());

        boolean forEachNodeFound = false;
        boolean emailNodeFound = false;
        boolean humantaskNodeFound = false;
        for (NodeModel childNode : flowNode3.getChildNodeModels()) {
            if (childNode instanceof ForEachContainerModel) {
                forEachNodeFound = true;
                assertNull(childNode.getInput());
                assertNull(childNode.getOutput());
                assertEquals(flowNode3Id, ((AbstractModel) childNode
                        .getParentModel()).getId());
            }
            if (childNode instanceof EmailInvokeNodeModel) {
                emailNodeFound = true;
                assertNull(childNode.getInput());
                assertEquals(flowNode3Id, childNode.getOutput().getTarget()
                        .getId());
                assertEquals(flowNode3Id, ((AbstractModel) childNode
                        .getParentModel()).getId());
            }
            if (childNode instanceof HumanTaskInvokeNodeModel) {
                humantaskNodeFound = true;
                assertEquals(flowNode3Id, childNode.getInput().getSource()
                        .getId());
                assertNull(childNode.getOutput());
                assertEquals(flowNode3Id, ((AbstractModel) childNode
                        .getParentModel()).getId());
            }
        }
        assertTrue(forEachNodeFound);
        assertTrue(emailNodeFound);
        assertTrue(humantaskNodeFound);
    }

    public void testForEachNode() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(forEach_dwdl);

        ForEachContainerModel forEachModel_1 = null;
        ForEachContainerModel forEachModel_2 = null;
        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof ForEachContainerModel
                    && nodeModel.getId() == 1255956748687L) {
                forEachModel_1 = (ForEachContainerModel) nodeModel;
            }
            if (nodeModel instanceof ForEachContainerModel
                    && nodeModel.getId() == 1255956748703L) {
                forEachModel_2 = (ForEachContainerModel) nodeModel;
            }
        }
        if (forEachModel_1 == null) {
            fail("Couldn't find a ForEachContainerModel - please check sample DWDL");
            // returning despite fail since the java compiler doesn't know about
            // the unchecked Exception thrown
            return;
        }
        if (forEachModel_2 == null) {
            fail("Couldn't find a ForEachContainerModel - please check sample DWDL");
            // returning despite fail since the java compiler doesn't know about
            // the unchecked Exception thrown
            return;
        }

        assertEquals(0, forEachModel_1.getChildConnectionModels().size());
        assertEquals(0, forEachModel_1.getChildNodeModels().size());

        assertEquals(57, forEachModel_1.getChangeListenerLeft());
        assertEquals(154, forEachModel_1.getChangeListenerTop());
        assertEquals(150, forEachModel_1.getChangeListenerHeight());
        assertEquals(200, forEachModel_1.getChangeListenerWidth());

        assertEquals(null, forEachModel_1.getExitCondition());
        assertEquals(null, forEachModel_1.getIterationVariableId());
        assertFalse(forEachModel_1.isParallel());

        assertEquals(2, forEachModel_2.getChildConnectionModels().size());
        assertEquals(1, forEachModel_2.getChildNodeModels().size());

        assertEquals(367, forEachModel_2.getChangeListenerLeft());
        assertEquals(126, forEachModel_2.getChangeListenerTop());
        assertEquals(279, forEachModel_2.getChangeListenerHeight());
        assertEquals(312, forEachModel_2.getChangeListenerWidth());

        assertEquals(new Long(1255956748593L), forEachModel_2.getInput()
                .getSource().getId());
        assertEquals(new Long(1255956748656L), forEachModel_2.getOutput()
                .getTarget().getId());

        assertEquals(ExitCondition.XOR, forEachModel_2.getExitCondition());
        assertEquals(textVarId, forEachModel_2.getIterationVariableId());
        assertEquals(new Boolean(true), forEachModel_2.isParallel());
    }

    public void testIfNode() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(ifNode_dwdl);

        HashMap<Long, IfContainerModel> ifModels = new HashMap<Long, IfContainerModel>();
        for (NodeModel node : model.getChildNodeModels()) {
            if (node instanceof IfContainerModel) {
                ifModels.put(node.getId(), (IfContainerModel) node);
            }
        }

        assertEquals(3, ifModels.size());

        /* If node without conditions */
        IfContainerModel ifModel = ifModels.get(1256211850125L);
        assertEquals(new Long(1256211693359L), ((AbstractModel) ifModel
                .getParentModel()).getId());
        assertEquals(4, ifModel.getChildNodeModels().size());
        assertEquals(4, ifModel.getBranchlessChildNodes().size());
        assertEquals(3, ifModel.getChildConnectionModels().size());
        assertEquals(0, ifModel.getConditions().size());

        HashMap<Long, NodeModel> childModels = new HashMap<Long, NodeModel>();
        for (NodeModel node : ifModel.getChildNodeModels()) {
            childModels.put(node.getId(), node);
        }

        assertNull(childModels.get(1256211850281L).getInput());
        assertEquals(ifModel.getId(), childModels.get(1256211850281L)
                .getOutput().getTarget().getId());
        assertEquals(new Long(1256211850187L), childModels.get(1256211850234L)
                .getOutput().getTarget().getId());
        assertEquals(new Long(1256211850234L), childModels.get(1256211850187L)
                .getOutput().getTarget().getId());

        /*
         * If node with two condition that are NOT fully modeled, that means no
         * operands and no operator
         */
        ifModel = ifModels.get(1257415091656L);
        assertEquals(2, ifModel.getConditions().size());

        Condition condition1 = ifModel.getConditionById(1257415091671L);
        Condition condition2 = ifModel.getConditionById(1257415091718L);

        assertNull(condition1.getOrder());
        assertNull(condition1.getLeftOperandId());
        assertNull(condition1.getOperator());
        assertNull(condition1.getRightOperandId());
        assertTrue(condition1.getTarget() instanceof EmailInvokeNodeModel);

        assertNull(condition2.getOrder());
        assertNull(condition2.getLeftOperandId());
        assertNull(condition2.getOperator());
        assertNull(condition2.getRightOperandId());
        assertTrue(condition2.getTarget() instanceof HumanTaskInvokeNodeModel);

        /* If node with two conditions that are fully modeled */
        ifModel = ifModels.get(1256211693453L);
        assertEquals(2, ifModel.getConditions().size());

        condition1 = ifModel.getConditionById(1256211684046L);
        condition2 = ifModel.getConditionById(1256211684048L);

        assertEquals(new Long(1256211693359L), ((AbstractModel) ifModel
                .getParentModel()).getId());
        assertEquals(4, ifModel.getChildNodeModels().size());
        assertEquals(1, ifModel.getBranchlessChildNodes().size());
        assertEquals(2, ifModel.getChildNodesOfCondition(condition1).size());
        assertEquals(1, ifModel.getChildNodesOfCondition(condition2).size());

        assertEquals(4, ifModel.getChildConnectionModels().size());
        assertEquals(0, ifModel.getBranchlessChildConnections().size());
        assertEquals(3, ifModel.getChildConnectionsOfCondition(condition1)
                .size());
        assertEquals(1, ifModel.getChildConnectionsOfCondition(condition2)
                .size());

        assertEquals(new Integer(0), condition1.getOrder());
        assertEquals(new Long(12L), condition1.getLeftOperandId());
        assertEquals(Operator.NOTEQUAL, condition1.getOperator());
        assertEquals(new Long(768768L), condition1.getRightOperandId());

        assertEquals(new Integer(1), condition2.getOrder());
        assertEquals(new Long(12L), condition2.getLeftOperandId());
        assertEquals(Operator.CONTAINS, condition2.getOperator());
        assertEquals(new Long(123L), condition2.getRightOperandId());

    }

    public void testConnections() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(connections_dwdl);

        HashMap<Long, ConnectionModel> connections = new HashMap<Long, ConnectionModel>();
        ConnectionModel connection;
        for (ConnectionModel con : model.getChildConnectionModels()) {
            connections.put(con.getId(), con);
        }
        assertEquals(2, connections.size());

        connection = connections.get(1255353235812L);
        assertEquals(true, connection.getSource() instanceof StartNodeModel);
        assertEquals(true, connection.getTarget() instanceof FlowContainerModel);

        connection = connections.get(1255353235843L);
        assertEquals(true, connection.getSource() instanceof FlowContainerModel);
        assertEquals(true, connection.getTarget() instanceof EndNodeModel);

        FlowContainerModel flowModel = null;
        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof FlowContainerModel) {
                flowModel = (FlowContainerModel) nodeModel;
            }
        }
        if (flowModel == null) {
            fail("Couldn't find a FlowContainerModel - please check sample DWDL");
            // returning despite fail since the java compiler doesn't know about
            // the unchecked Exception thrown
            return;
        }

        connections.clear();
        for (ConnectionModel con : flowModel.getChildConnectionModels()) {
            connections.put(con.getId(), con);
        }
        assertEquals(4, connections.size());

        connection = connections.get(1255353235876L);
        assertNotNull(connection);
        assertEquals(true, connection instanceof ContainerStartConnectionModel);
        assertEquals(true, connection.getSource() instanceof FlowContainerModel);
        assertEquals(true,
                connection.getTarget() instanceof HumanTaskInvokeNodeModel);

        connection = connections.get(1255353235875L);
        assertNotNull(connection);
        assertEquals(true, connection instanceof ContainerExitConnectionModel);
        assertEquals(true,
                connection.getSource() instanceof HumanTaskInvokeNodeModel);
        assertEquals(true, connection.getTarget() instanceof FlowContainerModel);

        connection = connections.get(1255353235922L);
        assertNotNull(connection);
        assertEquals(true, connection instanceof ContainerStartConnectionModel);
        assertEquals(true, connection.getSource() instanceof FlowContainerModel);
        assertEquals(true,
                connection.getTarget() instanceof EmailInvokeNodeModel);

        connection = connections.get(1255353235921L);
        assertNotNull(connection);
        assertEquals(true, connection instanceof ContainerExitConnectionModel);
        assertEquals(true,
                connection.getSource() instanceof EmailInvokeNodeModel);
        assertEquals(true, connection.getTarget() instanceof FlowContainerModel);

    }
}
