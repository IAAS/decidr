package de.decidr.modelingtool.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import de.decidr.modelingtool.client.ui.Node;
import de.decidr.modelingtool.client.ui.StartNode;
import de.decidr.modelingtool.client.ui.Workflow;

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
        
        StartNode node = new StartNode();
        
        workflow.add(node);
        
        //workflow.getDragController().makeDraggable(node);
        
    }
}
