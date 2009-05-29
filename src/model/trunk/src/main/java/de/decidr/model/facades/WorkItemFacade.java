package de.decidr.model.facades;

import com.vaadin.data.Item;

import de.decidr.model.permissions.Role;

public class WorkItemFacade extends AbstractFacade {

    public WorkItemFacade(Role actor) {
        super(actor);
    }

    public Item getWorkItem(Long workItemId) {
        throw new UnsupportedOperationException();
    }

    public Long createWorkItem(Long userId, Long deployedWorkflowModelId,
            String odePid, String name, String description, byte[] data,
            Boolean notifyUser) {
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