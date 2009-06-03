package main.java.de.decidr.modelingtool.client;

import main.java.de.decidr.modelingtool.client.ui.Container;
import main.java.de.decidr.modelingtool.client.ui.EmailInvokeNode;
import main.java.de.decidr.modelingtool.client.ui.EndNode;
import main.java.de.decidr.modelingtool.client.ui.Node;
import main.java.de.decidr.modelingtool.client.ui.StartNode;
import main.java.de.decidr.modelingtool.client.ui.Workflow;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ModelingTool implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        // create workflow and add to the root panel.
        final Workflow workflow = new Workflow();
        RootPanel.get("workflow").add(workflow);

        // create test elements
        Node startNode = new StartNode();
        Node endNode = new EndNode();
        Node emailInvokeNode = new EmailInvokeNode();

        Container con = new Container();

        workflow.add(startNode, 50, 50);
        workflow.add(emailInvokeNode, 50, 150);
        workflow.add(endNode, 50, 250);

        workflow.add(con, 200, 50);

    }
}