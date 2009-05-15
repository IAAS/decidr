package de.decidr.model.facades;

import java.util.List;

import com.itmill.toolkit.data.Item;

public class WorkflowInstanceFacade extends AbstractFacade {

	public WorkflowInstanceFacade(Long actorId) {
		super(actorId);
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
}