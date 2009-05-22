package de.decidr.model.facades;

import java.util.List;

import com.itmill.toolkit.data.Item;

import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;

public class WorkflowModelFacade extends AbstractFacade{

	public WorkflowModelFacade(Role actor) {
	        super(actor);
	}

	public void saveWorkflowModel(Item workflowModel) {
		throw new UnsupportedOperationException();
	}

	public Item getWorkflowModel(Long workflowModelId) {
		throw new UnsupportedOperationException();
	}

	public void publishWorkflowModels(List<Long> workflowModelIds) {
		throw new UnsupportedOperationException();
	}

	public void unpublishWorkflowModels(List<Long> workflowModelIds) {
		throw new UnsupportedOperationException();
	}

	public void setExecutable(Long workflowModelId, Boolean excecutable) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getWorkflowAdministrators(Long workflowModelId) {
		throw new UnsupportedOperationException();
	}

	public void setWorkflowAdministrators(Long workflowModelId, List<Long> userIds) {
		throw new UnsupportedOperationException();
	}

	public void deleteWorkflowModels(List<Long> workflowModelIds) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getWorkflowInstances(Long workflowModelId) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getAllPublishedWorkflowModels(List<Filter> filters, Paginator paginator) {
		throw new UnsupportedOperationException();
	}

	public Long startWorkflowInstance(Long workflowModelId, byte[] startConfiguration) {
		throw new UnsupportedOperationException();
	}

	public Item getLastStartConfiguration(Long workflowModelId) {
		throw new UnsupportedOperationException();
	}
}