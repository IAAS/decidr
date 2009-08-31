package de.decidr.modelingtool.client.menu;

import java.util.List;

import com.google.gwt.user.client.Command;

import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.dialogs.variableeditor.VariableEditorInvoker;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class VariablesMenuItem implements Command {
    @Override
    public void execute() {
        List<Variable> variables = Workflow.getInstance().getModel()
                .getVariables();
        VariableEditorInvoker.invoke(variables);
    }
}
