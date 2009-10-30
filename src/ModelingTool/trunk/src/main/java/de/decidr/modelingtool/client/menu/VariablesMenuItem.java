package de.decidr.modelingtool.client.menu;

import java.util.List;

import com.google.gwt.user.client.Command;

import de.decidr.modelingtool.client.model.variable.Variable;
import de.decidr.modelingtool.client.ui.Workflow;
import de.decidr.modelingtool.client.ui.dialogs.variableeditor.VariableEditorWindow;
import de.decidr.modelingtool.client.ui.dialogs.variableeditor.VariableEditorWindowInvoker;

/**
 * Displays the {@link VariableEditorWindow}.
 * 
 * @author Jonas Schlaak
 */
public class VariablesMenuItem implements Command {
    @Override
    public void execute() {
        List<Variable> variables = Workflow.getInstance().getModel()
                .getVariables();
        VariableEditorWindowInvoker.invoke(variables);
    }
}
