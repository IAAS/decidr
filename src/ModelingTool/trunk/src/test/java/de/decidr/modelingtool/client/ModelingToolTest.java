package de.decidr.modelingtool.client;

import java.util.HashMap;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

import de.decidr.modelingtool.client.io.DWDLParser;
import de.decidr.modelingtool.client.io.DWDLParserImpl;
import de.decidr.modelingtool.client.model.ConnectionModel;
import de.decidr.modelingtool.client.model.EmailInvokeNodeModel;
import de.decidr.modelingtool.client.model.EndNodeModel;
import de.decidr.modelingtool.client.model.FlowContainerModel;
import de.decidr.modelingtool.client.model.NodeModel;
import de.decidr.modelingtool.client.model.StartNodeModel;
import de.decidr.modelingtool.client.model.WorkflowModel;
import de.decidr.modelingtool.client.model.humantask.HumanTaskInvokeNodeModel;
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
private String dwdl_2 ="";
    
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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
        assertEquals(true, childConnectionModels.containsKey(new Long(
                "1255088341359")));
//        assertEquals(flowNodeId, childConnectionModels.get(
//                new Long("1255088341359")).getSource().getId());
        assertEquals(true, childConnectionModels.get(new Long("1255088341359"))
                .getTarget() instanceof EmailInvokeNodeModel);

        assertEquals(true, childConnectionModels.containsKey(new Long(
                "1255088341312")));
        assertEquals(true, childConnectionModels.get(new Long("1255088341312"))
                .getSource() instanceof EmailInvokeNodeModel);
        assertEquals(flowNodeId, childConnectionModels.get(
                new Long("1255088341312")).getTarget());

        assertEquals(true, childConnectionModels.containsKey(new Long(
                "1255088341406")));
        assertEquals(flowNodeId, childConnectionModels.get(
                new Long("1255088341406")).getTarget());
        assertEquals(true, childConnectionModels.get(new Long("1255088341406"))
                .getSource() instanceof HumanTaskInvokeNodeModel);

        assertEquals(true, childConnectionModels.containsKey(new Long(
                "1255088341328")));
        assertEquals(flowNodeId, childConnectionModels.get(
                new Long("1255088341328")).getSource());
        assertEquals(true, childConnectionModels.get(new Long("1255088341328"))
                .getTarget() instanceof HumanTaskInvokeNodeModel);

    }

    @Test
    public void testForEachNode() {
        fail();
    }

    @Test
    public void testIfNode() {
        fail();
    }
}
