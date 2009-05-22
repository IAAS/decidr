package de.decidr.model.facades;

import java.util.List;

import com.itmill.toolkit.data.Item;

import de.decidr.model.permissions.Role;

public class WorkflowInstanceFacade extends AbstractFacade {

	public WorkflowInstanceFacade(Role actor) {
	        super(actor);
	}

	public void stopWorkflowInstance(Long workflowInstanceId) {
		throw new UnsupportedOperationException();
	}

	public String getOdeUrl(Long workflowInstanceId) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getParticipatingUsers(Long workflowInstanceId) {
		throw new UnsupportedOperationException();
	}

	public void deleteWorkflowInstance(Long workflowInstanceId) {
		throw new UnsupportedOperationException();
	}
	
	public void getAllWorkItems(Long workflowInstanceId) {
	    throw new UnsupportedOperationException();
	}
}