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
import de.decidr.modelingtool.client.model.ifcondition.IfContainerModel;
import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.model.variable.VariableType;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class ModelingToolTest extends GWTTestCase {

    /**
     * Must refer to a valid module that sources this class.
     */
    public String getModuleName() {
        return "de.decidr.modelingtool.ModelingToolWidget";
    }

    /**
     * Holds the xmls which are used to test the parser
     */
    private String dwdl_1 = "<workflow name=\"Simple Workflow\" id=\"1255087539250\" targetNamespace=\"namespace\" xmlns=\"schema\"><description>This is a simple workflow.</description><variables><variable name=\"L1255088324562\" label=\"Form Container\" type=\"form\" configurationVariable=\"no\"><initialValue>New Value</initialValue></variable><variable name=\"L1255088196312\" label=\"Name\" type=\"string\" configurationVariable=\"no\"><initialValue>New Value</initialValue></variable><variable name=\"L1255088188640\" label=\"Description\" type=\"string\" configurationVariable=\"no\"><initialValue>New Value</initialValue></variable><variable name=\"L1255088176468\" label=\"Output\" type=\"string\" configurationVariable=\"no\"><initialValue>New Value</initialValue></variable><variable name=\"L123\" label=\"Fault Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow failed</initialValue></variable><variable name=\"L12\" label=\"Success Message\" type=\"string\" configurationVariable=\"yes\"><initialValue>Workflow succeded</initialValue></variable><variable name=\"L768768\" label=\"Text\" type=\"string\" configurationVariable=\"yes\"><initialValue>Loram Ipsum</initialValue></variable></variables><roles><role configurationVariable=\"no\" label=\"User\" name=\"L1255088207843\"><actor userId=\"23\" /><actor userId=\"113\" /></role><role configurationVariable=\"no\" label=\"Recipient\" name=\"L345\"><actor userId=\"decidradmin\" /></role></roles><faultHandler><setProperty name=\"message\" variable=\"L123\" /><recipient><setProperty name=\"name\" variable=\"L345\" /></recipient></faultHandler><nodes><invokeNode name=\"EmailInvokeNode\" id=\"1255088341203\" activity=\"Decidr-Email\"><description></description><graphics x=\"32\" y=\"96\" width=\"100\" height=\"60\" /><sources><source arcId=\"1255088341234\" /></sources><targets><target arcId=\"1255088341235\" /></targets><setProperty name=\"to\" variable=\"L345\" /><setProperty name=\"cc\" variable=\"L345\" /><setProperty name=\"bcc\" variable=\"L1255088207843\" /><setProperty name=\"subject\" variable=\"L768768\" /><setProperty name=\"message\" variable=\"L768768\" /><setProperty name=\"attachement\" /></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1255088341250\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"184\" y=\"27\" width=\"100\" height=\"60\" /><targets><target arcId=\"1255088341234\" /></targets><sources><source arcId=\"1255088341281\" /></sources><setProperty name=\"wfmID\" variable=\"L1255087539250\" /><setProperty name=\"user\" variable=\"L1255088207843\" /><setProperty name=\"name\" variable=\"L1255088196312\" /><setProperty name=\"description\" variable=\"L1255088188640\" /><setProperty name=\"userNotification\"><propertyValue>yes</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"L1255088324562\"><parameters><humanTaskData><taskItem name=\"taskItem\" variable=\"L1255088176468\" type=\"string\"><label>Label 1</label><hint>First</hint><value>New Value</value></taskItem><taskItem name=\"taskItem\" variable=\"L1255088176468\" type=\"string\"><label>Label 2</label><hint>Second</hint><value>New Value</value></taskItem></humanTaskData></parameters></getProperty></invokeNode><flowNode name=\"FlowContainer\" id=\"1255088341296\"><description></description><graphics x=\"176\" y=\"128\" width=\"311\" height=\"154\" /><targets><target arcId=\"1255088341281\" /></targets><sources><source arcId=\"1255088341500\" /></sources><nodes><invokeNode name=\"EmailInvokeNode\" id=\"1255088341329\" activity=\"Decidr-Email\"><description></description><graphics x=\"23\" y=\"54\" width=\"100\" height=\"60\" /><sources><source arcId=\"1255088341312\" /></sources><targets><target arcId=\"1255088341359\" /></targets><setProperty name=\"to\" /><setProperty name=\"cc\" /><setProperty name=\"bcc\" /><setProperty name=\"subject\" /><setProperty name=\"message\" /><setProperty name=\"attachement\" /></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1255088341360\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"172\" y=\"54\" width=\"100\" height=\"60\" /><targets><target arcId=\"1255088341328\" /></targets><sources><source arcId=\"1255088341406\" /></sources><setProperty name=\"wfmID\" variable=\"L1255087539250\" /><setProperty name=\"user\" /><setProperty name=\"name\" /><setProperty name=\"description\" /><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"Lnull\"><parameters><humanTaskData /></parameters></getProperty></invokeNode></nodes><arcs><arc name=\"\" id=\"1255088341359\" source=\"1255088341296\" target=\"1255088341329\" /><arc name=\"\" id=\"1255088341312\" source=\"1255088341329\" target=\"1255088341296\" /><arc name=\"\" id=\"1255088341406\" source=\"1255088341360\" target=\"1255088341296\" /><arc name=\"\" id=\"1255088341328\" source=\"1255088341296\" target=\"1255088341360\" /></arcs></flowNode><forEachNode name=\"de.decidr.modelingtool.client.model.foreach.ForEachContainerModel\" id=\"1255341672812\" countername=\"L345\" parallel=\"yes\"><description></description><graphics x=\"316\" y=\"67\" width=\"127\" height=\"163\" /><startCounterValue>1</startCounterValue><finalCounterValue>L345</finalCounterValue><completionCondition>XOR</completionCondition><nodes /><arcs /></forEachNode><endNode name=\"EndNode\" id=\"1255087539312\"><description></description><graphics x=\"495\" y=\"325\" width=\"50\" height=\"30\" /><targets><target arcId=\"1255088341500\" /></targets><notificationOfSuccess><setProperty name=\"successMessage\" variable=\"L12\" /><setProperty name=\"recipient\" variable=\"L345\" /></notificationOfSuccess></endNode><startNode name=\"StartNode\" id=\"1255087539343\"><description></description><graphics x=\"25\" y=\"25\" width=\"50\" height=\"30\" /><sources><source arcId=\"1255088341235\" /></sources></startNode></nodes><arcs><arc name=\"\" id=\"1255088341281\" source=\"1255088341250\" target=\"1255088341296\" /><arc name=\"\" id=\"1255088341235\" source=\"1255087539343\" target=\"1255088341203\" /><arc name=\"\" id=\"1255088341234\" source=\"1255088341203\" target=\"1255088341250\" /><arc name=\"\" id=\"1255088341500\" source=\"1255088341296\" target=\"1255087539312\" /></arcs></workflow>";
    private String dwdl_2 = "<workflow name=\"Simple Workflow\" id=\"1255353235687\" targetNamespace=\"namespace\" xmlns=\"schema\"><description></description><variables><variable name=\"L123\" label=\"Fault Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow failed</initialValue></variable><variable name=\"L12\" label=\"Success Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow succeded</initialValue></variable><variable name=\"L768768\" label=\"Text\" type=\"string\" configurationVariable=\"yes\"><initialValue>Loram Ipsum</initialValue></variable></variables><roles><role configurationVariable=\"no\" label=\"Recipient\" name=\"L345\"><actor userId=\"decidradmin\" /></role></roles><faultHandler><setProperty name=\"message\" variable=\"L123\" /><recipient><setProperty name=\"name\" variable=\"L345\" /></recipient></faultHandler><nodes><startNode name=\"de.decidr.modelingtool.client.model.StartNodeModel\" id=\"1255353235765\"><description></description><graphics x=\"25\" y=\"25\" width=\"50\" height=\"30\" /><sources><source arcId=\"1255353235812\" /></sources></startNode><flowNode name=\"de.decidr.modelingtool.client.model.FlowContainerModel\" id=\"1255353235813\"><description></description><graphics x=\"105\" y=\"102\" width=\"505\" height=\"326\" /><sources><source arcId=\"1255353235843\" /></sources><targets><target arcId=\"1255353235812\" /></targets><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1255353235844\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"57\" y=\"142\" width=\"100\" height=\"60\" /><sources><source arcId=\"1255353235875\" /></sources><targets><target arcId=\"1255353235876\" /></targets><setProperty name=\"wfmID\" variable=\"L1255353235687\" /><setProperty name=\"user\" /><setProperty name=\"name\" /><setProperty name=\"description\" /><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"Lnull\"><parameters><humanTaskData /></parameters></getProperty></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1255353235890\" activity=\"Decidr-Email\"><description></description><graphics x=\"310\" y=\"145\" width=\"100\" height=\"60\" /><sources><source arcId=\"1255353235921\" /></sources><targets><target arcId=\"1255353235922\" /></targets><setProperty name=\"to\" /><setProperty name=\"cc\" /><setProperty name=\"bcc\" /><setProperty name=\"subject\" /><setProperty name=\"message\" /><setProperty name=\"attachement\" /></invokeNode></nodes><arcs><arc name=\"de.decidr.modelingtool.client.model.ContainerStartConnectionModel\" id=\"1255353235876\" source=\"1255353235813\" target=\"1255353235844\" /><arc name=\"de.decidr.modelingtool.client.model.ContainerExitConnectionModel\" id=\"1255353235921\" source=\"1255353235890\" target=\"1255353235813\" /><arc name=\"de.decidr.modelingtool.client.model.ContainerExitConnectionModel\" id=\"1255353235875\" source=\"1255353235844\" target=\"1255353235813\" /><arc name=\"de.decidr.modelingtool.client.model.ContainerStartConnectionModel\" id=\"1255353235922\" source=\"1255353235813\" target=\"1255353235890\" /></arcs></flowNode><endNode name=\"de.decidr.modelingtool.client.model.EndNodeModel\" id=\"1255353235937\"><description></description><graphics x=\"619\" y=\"480\" width=\"50\" height=\"30\" /><targets><target arcId=\"1255353235843\" /></targets><notificationOfSuccess><setProperty name=\"successMessage\" variable=\"L12\" /><setProperty name=\"recipient\" variable=\"L345\" /></notificationOfSuccess></endNode></nodes><arcs><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1255353235812\" source=\"1255353235765\" target=\"1255353235813\" /><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1255353235843\" source=\"1255353235813\" target=\"1255353235937\" /></arcs></workflow> ";
    private String dwdl_3 = "<workflow name=\"Simple Workflow\" id=\"1255956748515\" targetNamespace=\"namespace\" xmlns=\"schema\"><description></description><variables><variable name=\"L123\" label=\"Fault Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow failed</initialValue></variable><variable name=\"L12\" label=\"Success Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow succeded</initialValue></variable><variable name=\"L768768\" label=\"Text\" type=\"string\" configurationVariable=\"yes\"><initialValue>Loram Ipsum</initialValue></variable></variables><roles><role configurationVariable=\"no\" label=\"Recipient\" name=\"L345\"><actor userId=\"decidradmin\"/></role></roles><faultHandler><setProperty name=\"message\" variable=\"L123\"/><recipient><setProperty name=\"name\" variable=\"L345\"/></recipient></faultHandler><nodes><startNode name=\"de.decidr.modelingtool.client.model.StartNodeModel\" id=\"1255956748593\"><description></description><graphics x=\"25\" y=\"25\" width=\"50\" height=\"30\"/><sources><source arcId=\"1255956748640\"/></sources></startNode><endNode name=\"de.decidr.modelingtool.client.model.EndNodeModel\" id=\"1255956748656\"><description></description><graphics x=\"690\" y=\"499\" width=\"50\" height=\"30\"/><targets><target arcId=\"1255956748671\"/></targets><notificationOfSuccess><setProperty name=\"successMessage\" variable=\"L12\"/><setProperty name=\"recipient\" variable=\"L345\"/></notificationOfSuccess></endNode><forEachNode name=\"de.decidr.modelingtool.client.model.foreach.ForEachContainerModel\" id=\"1255956748687\" parallel=\"no\"><description></description><graphics x=\"57\" y=\"154\" width=\"200\" height=\"150\"/></forEachNode><forEachNode name=\"de.decidr.modelingtool.client.model.foreach.ForEachContainerModel\" id=\"1255956748703\" countername=\"L768768\" parallel=\"yes\"><description></description><graphics x=\"367\" y=\"126\" width=\"312\" height=\"279\"/><sources><source arcId=\"1255956748671\"/></sources><targets><target arcId=\"1255956748640\"/></targets><startCounterValue>1</startCounterValue><finalCounterValue>L768768</finalCounterValue><completionCondition>XOR</completionCondition><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1255956748750\" activity=\"Decidr-Email\"><description></description><graphics x=\"119\" y=\"123\" width=\"100\" height=\"60\"/><sources><source arcId=\"1255956748765\"/></sources><targets><target arcId=\"1255956748781\"/></targets><setProperty name=\"to\"/><setProperty name=\"cc\"/><setProperty name=\"bcc\"/><setProperty name=\"subject\"/><setProperty name=\"message\"/><setProperty name=\"attachement\"/></invokeNode></nodes><arcs><arc name=\"de.decidr.modelingtool.client.model.ContainerExitConnectionModel\" id=\"1255956748765\" source=\"1255956748750\" target=\"1255956748703\"/><arc name=\"de.decidr.modelingtool.client.model.ContainerStartConnectionModel\" id=\"1255956748781\" source=\"1255956748703\" target=\"1255956748750\"/></arcs></forEachNode></nodes><arcs><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1255956748671\" source=\"1255956748703\" target=\"1255956748656\"/><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1255956748640\" source=\"1255956748593\" target=\"1255956748703\"/></arcs></workflow>";
    private String dwdl_4 = "<workflow name=\"Simple Workflow\" id=\"1256211693359\" targetNamespace=\"namespace\" xmlns=\"schema\"><description></description><variables><variable name=\"L123\" label=\"Fault Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow failed</initialValue></variable><variable name=\"L12\" label=\"Success Message\" type=\"string\" configurationVariable=\"no\"><initialValue>Workflow succeded</initialValue></variable><variable name=\"L768768\" label=\"Text\" type=\"string\" configurationVariable=\"yes\"><initialValue>Loram Ipsum</initialValue></variable></variables><roles><role configurationVariable=\"no\" label=\"Recipient\" name=\"L345\"><actor userId=\"decidradmin\"/></role></roles><faultHandler><setProperty name=\"message\" variable=\"L123\"/><recipient><setProperty name=\"name\" variable=\"L345\"/></recipient></faultHandler><nodes><startNode name=\"de.decidr.modelingtool.client.model.StartNodeModel\" id=\"1256211693437\"><description></description><graphics x=\"25\" y=\"25\" width=\"50\" height=\"30\"/></startNode><ifNode name=\"de.decidr.modelingtool.client.model.ifcondition.IfContainerModel\" id=\"1256211850125\"><description></description><graphics x=\"380\" y=\"94\" width=\"381\" height=\"295\"/><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1256211850156\" activity=\"Decidr-Email\"><description></description><graphics x=\"40\" y=\"76\" width=\"100\" height=\"60\"/><setProperty name=\"to\"/><setProperty name=\"cc\"/><setProperty name=\"bcc\"/><setProperty name=\"subject\"/><setProperty name=\"message\"/><setProperty name=\"attachement\"/></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1256211850187\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"47\" y=\"169\" width=\"100\" height=\"60\"/><sources><source arcId=\"1256211850218\"/></sources><targets><target arcId=\"1256211850219\"/></targets><setProperty name=\"wfmID\" variable=\"L1256211693359\"/><setProperty name=\"user\"/><setProperty name=\"name\"/><setProperty name=\"description\"/><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"Lnull\"><parameters><humanTaskData/></parameters></getProperty></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1256211850234\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"230\" y=\"79\" width=\"100\" height=\"60\"/><sources><source arcId=\"1256211850219\"/></sources><targets><target arcId=\"1256211850218\"/></targets><setProperty name=\"wfmID\" variable=\"L1256211693359\"/><setProperty name=\"user\"/><setProperty name=\"name\"/><setProperty name=\"description\"/><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"Lnull\"><parameters><humanTaskData/></parameters></getProperty></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1256211850281\" activity=\"Decidr-Email\"><description></description><graphics x=\"229\" y=\"160\" width=\"100\" height=\"60\"/><sources><source arcId=\"1256211850296\"/></sources><setProperty name=\"to\"/><setProperty name=\"cc\"/><setProperty name=\"bcc\"/><setProperty name=\"subject\"/><setProperty name=\"message\"/><setProperty name=\"attachement\"/></invokeNode></nodes><arcs><arc name=\"de.decidr.modelingtool.client.model.ContainerExitConnectionModel\" id=\"1256211850296\" source=\"1256211850281\" target=\"1256211850125\"/><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1256211850219\" source=\"1256211850234\" target=\"1256211850187\"/><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1256211850218\" source=\"1256211850187\" target=\"1256211850234\"/></arcs></ifNode><ifNode name=\"de.decidr.modelingtool.client.model.ifcondition.IfContainerModel\" id=\"1256211693453\"><description></description><graphics x=\"29\" y=\"148\" width=\"282\" height=\"326\"/><condition order=\"0\" defaultCondition=\"yes\"><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1256211693484\" activity=\"Decidr-Email\"><description></description><graphics x=\"18\" y=\"86\" width=\"100\" height=\"60\"/><sources><source arcId=\"1256211850375\"/></sources><targets><target arcId=\"1256211684046\"/></targets><setProperty name=\"to\"/><setProperty name=\"cc\"/><setProperty name=\"bcc\"/><setProperty name=\"subject\"/><setProperty name=\"message\"/><setProperty name=\"attachement\"/></invokeNode><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1256211850343\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"19\" y=\"193\" width=\"100\" height=\"60\"/><sources><source arcId=\"1256211850421\"/></sources><targets><target arcId=\"1256211850375\"/></targets><setProperty name=\"wfmID\" variable=\"L1256211693359\"/><setProperty name=\"user\"/><setProperty name=\"name\"/><setProperty name=\"description\"/><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"Lnull\"><parameters><humanTaskData/></parameters></getProperty></invokeNode></nodes><arcs><arc name=\"Condition 1\" id=\"1256211684046\" source=\"1256211693453\" target=\"1256211693484\"/><arc name=\"de.decidr.modelingtool.client.model.ConnectionModel\" id=\"1256211850375\" source=\"1256211693484\" target=\"1256211850343\"/><arc name=\"de.decidr.modelingtool.client.model.ContainerExitConnectionModel\" id=\"1256211850421\" source=\"1256211850343\" target=\"1256211693453\"/></arcs></condition><condition order=\"1\" defaultCondition=\"no\"><left-operand>12</left-operand><operator>?=</operator><right-operand>123</right-operand><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel\" id=\"1256211693546\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"173\" y=\"85\" width=\"100\" height=\"60\"/><targets><target arcId=\"1256211684048\"/></targets><setProperty name=\"wfmID\" variable=\"L1256211693359\"/><setProperty name=\"user\"/><setProperty name=\"name\"/><setProperty name=\"description\"/><setProperty name=\"userNotification\"><propertyValue>no</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"Lnull\"><parameters><humanTaskData/></parameters></getProperty></invokeNode></nodes><arcs><arc name=\"Condition 2\" id=\"1256211684048\" source=\"1256211693453\" target=\"1256211693546\"/></arcs></condition><nodes><invokeNode name=\"de.decidr.modelingtool.client.model.EmailInvokeNodeModel\" id=\"1256211850500\" activity=\"Decidr-Email\"><description></description><graphics x=\"172\" y=\"192\" width=\"100\" height=\"60\"/><setProperty name=\"to\"/><setProperty name=\"cc\"/><setProperty name=\"bcc\"/><setProperty name=\"subject\"/><setProperty name=\"message\"/><setProperty name=\"attachement\"/></invokeNode></nodes></ifNode><endNode name=\"de.decidr.modelingtool.client.model.EndNodeModel\" id=\"1256211693578\"><description></description><graphics x=\"642\" y=\"475\" width=\"50\" height=\"30\"/><notificationOfSuccess><setProperty name=\"successMessage\" variable=\"L12\"/><setProperty name=\"recipient\" variable=\"L345\"/></notificationOfSuccess></endNode></nodes><arcs/></workflow>";

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
        WorkflowModel model = parser.parse(dwdl_1);

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
        WorkflowModel model = parser.parse(dwdl_1);

        assertEquals(9, model.getVariables().size());

        assertNotNull(model.getVariable(textVarId));
        Variable text = model.getVariable(textVarId);

        assertEquals("Text", text.getLabel());
        assertEquals(VariableType.STRING, text.getType());
        assertEquals(new Boolean(true), text.isConfig());

        assertEquals(false, text.isArray());
        assertEquals(1, text.getValues().size());
        assertEquals("Loram Ipsum", text.getValues().get(0));
    }

    public void testRoles() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(dwdl_1);

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
        WorkflowModel model = parser.parse(dwdl_1);

        StartNodeModel startNodeModel = null;
        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof StartNodeModel) {
                startNodeModel = (StartNodeModel) nodeModel;
            }
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
        WorkflowModel model = parser.parse(dwdl_1);

        EndNodeModel endNodeModel = null;
        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof EndNodeModel) {
                endNodeModel = (EndNodeModel) nodeModel;
            }
        }
        assertEquals(endNodeId, endNodeModel.getId());
        assertEquals(495, endNodeModel.getChangeListenerLeft());
        assertEquals(325, endNodeModel.getChangeListenerTop());
        assertEquals(flowNodeId, endNodeModel.getInput().getSource().getId());
        assertNull(endNodeModel.getOutput());
    }

    public void testEmailInvokeNode() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(dwdl_1);

        EmailInvokeNodeModel emailNodeModel = null;
        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof EmailInvokeNodeModel
                    && nodeModel.getId().equals(emailNodeId)) {
                emailNodeModel = (EmailInvokeNodeModel) nodeModel;
            }
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
        WorkflowModel model = parser.parse(dwdl_1);

        HumanTaskInvokeNodeModel humanTaskNodeModel = null;
        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof HumanTaskInvokeNodeModel
                    && nodeModel.getId().equals(humanTaskNodeId)) {
                humanTaskNodeModel = (HumanTaskInvokeNodeModel) nodeModel;
            }
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
        WorkflowModel model = parser.parse(dwdl_1);

        FlowContainerModel flowModel = null;
        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof FlowContainerModel
                    && nodeModel.getId().equals(flowNodeId)) {
                flowModel = (FlowContainerModel) nodeModel;
            }
        }

        assertEquals(flowNodeId, flowModel.getId());
        assertEquals(176, flowModel.getChangeListenerLeft());
        assertEquals(128, flowModel.getChangeListenerTop());
        assertEquals(humanTaskNodeId, flowModel.getInput().getSource().getId());
        assertEquals(endNodeId, flowModel.getOutput().getTarget().getId());

        assertEquals(2, flowModel.getChildNodeModels().size());
        assertEquals(4, flowModel.getChildConnectionModels().size());

        HashMap<Long, ConnectionModel> childConnectionModels = new HashMap<Long, ConnectionModel>();
        for (ConnectionModel connectionModel : flowModel
                .getChildConnectionModels()) {
            childConnectionModels.put(connectionModel.getId(), connectionModel);
        }
        assertEquals(true, childConnectionModels.containsKey(1255088341359L));
        assertEquals(flowNodeId, childConnectionModels.get(1255088341359L)
                .getSource().getId());
        assertEquals(true, childConnectionModels.get(1255088341359L)
                .getTarget() instanceof EmailInvokeNodeModel);

        assertEquals(true, childConnectionModels.containsKey(1255088341312L));
        assertEquals(true, childConnectionModels.get(1255088341312L)
                .getSource() instanceof EmailInvokeNodeModel);
        assertEquals(flowNodeId, childConnectionModels.get(1255088341312L)
                .getTarget().getId());

        assertEquals(true, childConnectionModels.containsKey(1255088341406L));
        assertEquals(flowNodeId, childConnectionModels.get(1255088341406L)
                .getTarget().getId());
        assertEquals(true, childConnectionModels.get(1255088341406L)
                .getSource() instanceof HumanTaskInvokeNodeModel);

        assertEquals(true, childConnectionModels.containsKey(1255088341328L));
        assertEquals(flowNodeId, childConnectionModels.get(1255088341328L)
                .getSource().getId());
        assertEquals(true, childConnectionModels.get(1255088341328L)
                .getTarget() instanceof HumanTaskInvokeNodeModel);

    }

    public void testForEachNode() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(dwdl_3);

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

        assertEquals(new Long("1255956748593"), forEachModel_2.getInput()
                .getSource().getId());
        assertEquals(new Long("1255956748656"), forEachModel_2.getOutput()
                .getTarget().getId());

        assertEquals(ExitCondition.XOR, forEachModel_2.getExitCondition());
        assertEquals(textVarId, forEachModel_2.getIterationVariableId());
        assertEquals(new Boolean(true), forEachModel_2.isParallel());
    }

    public void testIfNode() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(dwdl_4);

        HashMap<Long, IfContainerModel> ifModels = new HashMap<Long, IfContainerModel>();
        for (NodeModel node : model.getChildNodeModels()) {
            if (node instanceof IfContainerModel) {
                ifModels.put(node.getId(), (IfContainerModel) node);
            }
        }

        assertEquals(2, ifModels.size());

        /* If node without conditions */
        IfContainerModel ifModel = ifModels.get(1256211850125L);
        assertEquals(new Long("1256211693359"), ((AbstractModel) ifModel
                .getParentModel()).getId());
        assertEquals(4, ifModel.getChildConnectionModels().size());
        assertEquals(4, ifModel.getBranchlessChildNodes());
        assertEquals(3, ifModel.getChildConnectionModels());
        assertEquals(0, ifModel.getConditions().size());

        HashMap<Long, NodeModel> childModels = new HashMap<Long, NodeModel>();
        for (NodeModel node : ifModel.getChildNodeModels()) {
            childModels.put(node.getId(), node);
        }

        assertEquals(ifModel.getId(), childModels.get(1256211850343L)
                .getOutput().getTarget());

    }

    public void testConnections() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(dwdl_2);

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
