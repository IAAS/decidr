package de.decidr.model.commands.workflowinstance;

import de.decidr.model.permissions.Role;
import de.decidr.model.transactions.TransactionEvent;

public class StopWorkflowInstanceCommand extends WorkflowInstanceCommand {

    public StopWorkflowInstanceCommand(Role role, Long workflowinstanceId) {
        super(role, null);
      //SECIT Komponente von Modood benötigt.
    }

    @Override
    public void transactionAllowed(TransactionEvent evt) {
        //SECIT Komponente von Modood aufrufen oder so.

    }

}
