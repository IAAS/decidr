package de.decidr.modelingtool.client;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

import de.decidr.modelingtool.client.io.DWDLParser;
import de.decidr.modelingtool.client.io.DWDLParserImpl;
import de.decidr.modelingtool.client.model.EmailInvokeNodeModel;
import de.decidr.modelingtool.client.model.EndNodeModel;
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
     * Holds the xml which is used to test the parser
     */
    private String dwdl = "<workflow name=\"Simple Workflow\" id=\"1253883165781\" targetNamespace=\"namespace\" xmlns=\"schema\"><description>This is a simple workflow.</description><variables><variable name=\"L1254909164796\" label=\"Form Container\" type=\"form\" configurationVariable=\"no\"></variable><variable name=\"L123\" label=\"Fault Message\" type=\"list-string\" configurationVariable=\"no\"><initialValues><initialValue>value</initialValue><initialValue>Workflow failed</initialValue></initialValues></variable><variable name=\"L12\" label=\"Success Message\" type=\"list-string\" configurationVariable=\"no\"><initialValues><initialValue>value</initialValue><initialValue>Workflow succeded</initialValue></initialValues></variable><variable name=\"L768768\" label=\"Text\" type=\"list-string\" configurationVariable=\"yes\"><initialValues><initialValue>value</initialValue><initialValue>Loram Ipsum</initialValue></initialValues></variable></variables><roles><role configurationVariable=\"no\" label=\"Recipient\" name=\"L345\"><actor userId=\"444555\"/><actor userId=\"666777\"/></role></roles><faultHandler><setProperty name=\"message\" variable=\"L123\"/><recipient><setProperty name=\"name\" variable=\"L345\"/></recipient></faultHandler><nodes><startNode name=\"java.lang.Class1253883165843\" id=\"1253883165843\"><description></description><graphics x=\"25\" y=\"25\" width=\"50\" height=\"30\"/><sources><source arcId=\"1253883165859\"/></sources></startNode><endNode name=\"java.lang.Class1253883165860\" id=\"1253883165860\"><description></description><graphics x=\"495\" y=\"325\" width=\"50\" height=\"30\"/><targets><target arcId=\"1253883165890\"/></targets></endNode><invokeNode name=\"java.lang.Class1253883165906\" id=\"1253883165906\" activity=\"Decidr-Email\"><description></description><graphics x=\"237\" y=\"162\" width=\"100\" height=\"60\"/><sources><source arcId=\"1254909325312\"/></sources><targets><target arcId=\"1253883165859\"/></targets><setProperty name=\"to\" variable=\"L345\"/><setProperty name=\"cc\" variable=\"L345\"/><setProperty name=\"bcc\" variable=\"L345\"/><setProperty name=\"subject\" variable=\"L12\"/><setProperty name=\"message\" variable=\"L123\"/><setProperty name=\"attachement\"/></invokeNode><invokeNode name=\"java.lang.Class1254909325281\" id=\"1254909325281\" activity=\"Decidr-HumanTask\"><description></description><graphics x=\"243\" y=\"143\" width=\"100\" height=\"60\"/><targets><target arcId=\"1253883165890\"/></targets><sources><source arcId=\"1254909325312\"/></sources><setProperty name=\"wfmID\" variable=\"L1254909325187\"/><setProperty name=\"user\" variable=\"L345\"/><setProperty name=\"name\" variable=\"L12\"/><setProperty name=\"description\" variable=\"L12\"/><setProperty name=\"userNotification\"><propertyValue>yes</propertyValue></setProperty><getProperty name=\"taskResult\" variable=\"1254909164796\"><parameters><humanTaskData><taskItem name=\"taskItem\" variable=\"123\" type=\"string\"><label>Label</label><hint>Hint</hint><value>Workflow failed</value></taskItem><taskItem name=\"taskItem\" variable=\"123\" type=\"string\"><label>Label</label><hint>Hint</hint><value>Workflow failed</value></taskItem></humanTaskData></parameters></getProperty></invokeNode></nodes><arcs><arc name=\"java.lang.Class1253883165890\" id=\"1253883165890\" source=\"1254909325281\" target=\"1253883165860\"/><arc name=\"java.lang.Class1253883165859\" id=\"1253883165859\" source=\"1253883165843\" target=\"1253883165906\"/><arc name=\"java.lang.Class1254909325312\" id=\"1254909325312\" source=\"1253883165906\" target=\"1254909325281\"/></arcs></workflow>";

    private Long formVarId = new Long("1254909164796");
    private Long successVarId = new Long("12");
    private Long faultVarId = new Long("123");
    private Long textVarId = new Long("768768");
    private Long userVarId = new Long("345");

    private Long startNodeId = new Long("1253883165843");
    private Long endNodeId = new Long("1253883165860");
    private Long emailNodeId = new Long("1253883165906");
    private Long humanTaskNodeId = new Long("1254909325281");

    @Test
    public void testWorkflowProperties() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(dwdl);

        assertEquals("Simple Workflow", model.getName());
        assertEquals(new Long("1253883165781"), model.getId());
        assertEquals("This is a simple workflow.", model.getDescription());

        assertEquals("namespace", model.getProperties().getNamespace());
        assertEquals("schema", model.getProperties().getSchema());

        assertEquals(new Long("123"), model.getProperties()
                .getFaultMessageVariableId());
        assertEquals(new Boolean(false), model.getProperties()
                .getNotifyOnSuccess());
        assertEquals(new Long("345"), model.getProperties()
                .getRecipientVariableId());
        assertEquals(null, model.getProperties().getSuccessMessageVariableId());
    }

    @Test
    public void testVariables() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(dwdl);

        assertEquals(5, model.getVariables().size());

        assertNotNull(model.getVariable(textVarId));
        Variable text = model.getVariable(textVarId);

        assertEquals("Text", text.getLabel());
        assertEquals(VariableType.STRING, text.getType());
        assertEquals(new Boolean(true), text.isConfig());

        assertEquals(true, text.isArray());
        assertEquals(2, text.getValues().size());
        assertEquals("value", text.getValues().get(0));
        assertEquals("Loram Ipsum", text.getValues().get(1));
    }

    @Test
    public void testRoles() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(dwdl);

        assertNotNull(model.getVariable(userVarId));
        Variable user = model.getVariable(userVarId);
        assertEquals(new Long(345), user.getId());
        assertEquals("Recipient", user.getLabel());
        assertEquals(VariableType.ROLE, user.getType());
        assertEquals(new Boolean(false), user.isConfig());

        assertEquals(true, user.isArray());
        assertEquals(2, user.getValues().size());
        assertEquals("444555", user.getValues().get(0));
        assertEquals("666777", user.getValues().get(1));
    }

    @Test
    public void testStartNode() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(dwdl);

        StartNodeModel startNodeModel = null;
        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof StartNodeModel) {
                startNodeModel = (StartNodeModel) nodeModel;
            }
        }

        assertEquals(startNodeId, startNodeModel.getId());
        assertEquals(25, startNodeModel.getChangeListenerLeft());
        assertEquals(25, startNodeModel.getChangeListenerTop());
        assertEquals(new Long("1253883165859"), startNodeModel.getOutput()
                .getId());
        assertNull(startNodeModel.getInput());

    }

    @Test
    public void testEndNode() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(dwdl);

        EndNodeModel endNodeModel = null;
        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof EndNodeModel) {
                endNodeModel = (EndNodeModel) nodeModel;
            }
        }
        assertEquals(endNodeId, endNodeModel.getId());
        assertEquals(495, endNodeModel.getChangeListenerLeft());
        assertEquals(325, endNodeModel.getChangeListenerTop());
        assertEquals(new Long("1253883165890"), endNodeModel.getInput().getId());
        assertNull(endNodeModel.getOutput());
    }

    @Test
    public void testEmailInvokeNode() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(dwdl);

        EmailInvokeNodeModel emailNodeModel = null;
        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof EmailInvokeNodeModel) {
                emailNodeModel = (EmailInvokeNodeModel) nodeModel;
            }
        }

        assertEquals(emailNodeId, emailNodeModel.getId());
        assertEquals(237, emailNodeModel.getChangeListenerLeft());
        assertEquals(162, emailNodeModel.getChangeListenerTop());
        assertEquals(new Long("1253883165859"), emailNodeModel.getInput()
                .getId());
        assertEquals(new Long("1253883165890"), emailNodeModel.getOutput()
                .getId());

        assertEquals(new Long("345"), emailNodeModel.getToVariableId());
        assertEquals(new Long("345"), emailNodeModel.getCcVariableId());
        assertEquals(new Long("345"), emailNodeModel.getBccVariableId());
        assertEquals(new Long("12"), emailNodeModel.getSubjectVariableId());
        assertEquals(new Long("123"), emailNodeModel.getMessageVariableId());
        assertNull(emailNodeModel.getAttachmentVariableId());
    }

    @Test
    public void testHumanTaskInvokeNode() {
        DWDLParser parser = new DWDLParserImpl();
        WorkflowModel model = parser.parse(dwdl);

        HumanTaskInvokeNodeModel humanTaskNodeModel = null;
        for (NodeModel nodeModel : model.getChildNodeModels()) {
            if (nodeModel instanceof HumanTaskInvokeNodeModel) {
                humanTaskNodeModel = (HumanTaskInvokeNodeModel) nodeModel;
            }
        }

        assertEquals(humanTaskNodeId, humanTaskNodeModel.getId());
    }

    @Test
    public void testFlowNode() {
        fail();
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
