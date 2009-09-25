package de.decidr.modelingtool.client;

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
        return "de.decidr.modelingtool.ModelingTool";
    }

    private String dwdl = "<workflow name=\"Simple Workflow\" id=\"1253883165781\" targetNamespace=\"namespace\" xmlns=\"schema\"><description></description><variables><variable name=\"L123\" label=\"Fault Message\" type=\"list-string\" configurationVariable=\"no\"><initialValues><initialValue></initialValue><initialValue>Workflow failed</initialValue></initialValues></variable><variable name=\"L12\" label=\"Success Message\" type=\"list-string\" configurationVariable=\"no\"><initialValues><initialValue></initialValue><initialValue>Workflow succeded</initialValue></initialValues></variable><variable name=\"L768768\" label=\"Text\" type=\"list-string\" configurationVariable=\"yes\"><initialValues><initialValue></initialValue><initialValue>Loram Ipsum</initialValue></initialValues></variable></variables><roles><role configurationVariable=\"no\" label=\"Recipient\" name=\"L345\"><actor userId=\"\"/><actor userId=\"decidradmin\"/></role></roles><faultHandler><setProperty name=\"message\" variable=\"L123\"/><recipient><setProperty name=\"name\" variable=\"L345\"/></recipient></faultHandler><nodes><startNode name=\"java.lang.Class1253883165843\" id=\"1253883165843\"><description></description><graphics x=\"25\" y=\"25\" width=\"50\" height=\"30\"/><sources><source arcId=\"1253883165859\"/></sources></startNode><endNode name=\"java.lang.Class1253883165860\" id=\"1253883165860\"><description></description><graphics x=\"495\" y=\"325\" width=\"50\" height=\"30\"/><targets><target arcId=\"1253883165890\"/></targets></endNode><invokeNode name=\"java.lang.Class1253883165906\" id=\"1253883165906\" activity=\"Decidr-Email\"><description></description><graphics x=\"237\" y=\"162\" width=\"100\" height=\"60\"/><sources><source arcId=\"1253883165890\"/></sources><targets><target arcId=\"1253883165859\"/></targets><setProperty name=\"to\"/><setProperty name=\"cc\"/><setProperty name=\"bcc\"/><setProperty name=\"subject\"/><setProperty name=\"message\"/><setProperty name=\"attachement\"/></invokeNode></nodes><arcs><arc name=\"java.lang.Class1253883165890\" id=\"1253883165890\" source=\"1253883165906\" target=\"1253883165860\"/><arc name=\"java.lang.Class1253883165859\" id=\"1253883165859\" source=\"1253883165843\" target=\"1253883165906\"/></arcs></workflow>";

    /**
     * Add as many tests as you like.
     */
    public void testWorkflowProperties() {

        assertTrue(true);

    }

}