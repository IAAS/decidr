package de.decidr.model.facades;

import com.itmill.toolkit.data.Item;

public class WorkItemFacade extends AbstractFacade {

	public WorkItemFacade(Long actorId) {
		super(actorId);
	}

	public Long createWorkItem(Long userId, Long deployedWorkflowModelId,
			String odePid, String name, String description, byte[] data) {
		throw new UnsupportedOperationException();
	}

	public void setData(Long workItemId, byte[] data) {
		throw new UnsupportedOperationException();
	}

	public void setDataAndMarkAsDone(Long workItemId, byte[] data) {
		throw new UnsupportedOperationException();
	}

	public void markWorkItemAsDone(Long workItemId) {
		throw new UnsupportedOperationException();
	}

	public void deleteWorkItem(Long workItemId) {
		throw new UnsupportedOperationException();
	}

	public Item getWorkItemAndMarkAsInProgress(Long workItemId) {
		throw new UnsupportedOperationException();
	}
}