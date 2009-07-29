package de.decidr.modelingtool.client.menu;

import com.google.gwt.user.client.Command;

import de.decidr.modelingtool.client.ui.dialogs.DialogRegistry;
import de.decidr.modelingtool.client.ui.dialogs.variableeditor.VariableEditor;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public class VariablesMenuItem implements Command {
    @Override
    public void execute() {
        DialogRegistry.getInstance().showDialog(VariableEditor.class.getName());
    }
}
