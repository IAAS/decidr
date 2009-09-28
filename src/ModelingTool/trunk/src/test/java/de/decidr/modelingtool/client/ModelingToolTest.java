package de.decidr.modelingtool.client;

import org.junit.Test;

import com.google.gwt.junit.client.GWTTestCase;

import de.decidr.modelingtool.client.io.DWDLParser;
import de.decidr.modelingtool.client.io.DWDLParserImpl;
import de.decidr.modelingtool.client.model.WorkflowModel;

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
    private String dwdl = "<workflow name=\"Simple Workflow\" id=\"1253883165781\" targetNamespace=\"namespace\" xmlns=\"schema\"><description>This is a simple workflow.</description><variables><variable name=\"L123\" label=\"Fault Message\" type=\"list-string\" configurationVariable=\"no\"><initialValues><initialValue>value</initialValue><initialValue>Workflow failed</initialValue></initialValues></variable><variable name=\"L12\" label=\"Success Message\" type=\"list-string\" configurationVariable=\"no\"><initialValues><initialValue>value</initialValue><initialValue>Workflow succeded</initialValue></initialValues></variable><variable name=\"L768768\" label=\"Text\" type=\"list-string\" configurationVariable=\"yes\"><initialValues><initialValue>value</initialValue><initialValue>Loram Ipsum</initialValue></initialValues></variable></variables><roles><role configurationVariable=\"no\" label=\"Recipient\" name=\"L345\"><actor userId=\"\"/><actor userId=\"decidradmin\"/></role></roles><faultHandler><setProperty name=\"message\" variable=\"L123\"/><recipient><setProperty name=\"name\" variable=\"L345\"/></recipient></faultHandler><nodes><startNode name=\"java.lang.Class1253883165843\" id=\"1253883165843\"><description></description><graphics x=\"25\" y=\"25\" width=\"50\" height=\"30\"/><sources><source arcId=\"1253883165859\"/></sources></startNode><endNode name=\"java.lang.Class1253883165860\" id=\"1253883165860\"><description></description><graphics x=\"495\" y=\"325\" width=\"50\" height=\"30\"/><targets><target arcId=\"1253883165890\"/></targets></endNode><invokeNode name=\"java.lang.Class1253883165906\" id=\"1253883165906\" activity=\"Decidr-Email\"><description></description><graphics x=\"237\" y=\"162\" width=\"100\" height=\"60\"/><sources><source arcId=\"1253883165890\"/></sources><targets><target arcId=\"1253883165859\"/></targets><setProperty name=\"to\"/><setProperty name=\"cc\"/><setProperty name=\"bcc\"/><setProperty name=\"subject\"/><setProperty name=\"message\"/><setProperty name=\"attachement\"/></invokeNode></nodes><arcs><arc name=\"java.lang.Class1253883165890\" id=\"1253883165890\" source=\"1253883165906\" target=\"1253883165860\"/><arc name=\"java.lang.Class1253883165859\" id=\"1253883165859\" source=\"1253883165843\" target=\"1253883165906\"/></arcs></workflow>";

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
        fail();
    }

    @Test
    public void testRoles() {
        fail();
    }
}
