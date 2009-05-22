package de.decidr.model.facades;

import java.io.InputStream;
import java.util.List;

import com.itmill.toolkit.data.Item;

import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;

public class TenantFacade extends AbstractFacade {

	public TenantFacade(Role actor) {
	        super(actor);
	}

	public Long createTenant(String name, String description, Long adminId) {
		throw new UnsupportedOperationException();
	}

	public void setDescription(Long tenantId, String description) {
		throw new UnsupportedOperationException();
	}

	public InputStream getLogo(Long tenantId) {
		throw new UnsupportedOperationException();
	}

	public void setLogo(Long tenantId, InputStream logo) {
		throw new UnsupportedOperationException();
	}

	public void setSimpleColorScheme(InputStream simpleColorScheme,
			Long tenantId) {
		throw new UnsupportedOperationException();
	}

	public void setAdvancedColorScheme(InputStream advancedColorScheme,
			Long tenantId) {
		throw new UnsupportedOperationException();
	}

	public void getCurrentColorScheme() {
		throw new UnsupportedOperationException();
	}

	public void setCurrentColorScheme(InputStream currentColorScheme,
			Long tenantId) {
		throw new UnsupportedOperationException();
	}

	public void addTenantMember(Long tenantId, Long memberId) {
		throw new UnsupportedOperationException();
	}

	public Long createWorkflowModel(Long tenantId, String name) {
		throw new UnsupportedOperationException();
	}

	public void removeTenantMember(Long tenantId, Long userId) {
		throw new UnsupportedOperationException();
	}

	public void removeWorkflowModel(Long tenantId, Long workflowModelId) {
		throw new UnsupportedOperationException();
	}

	public void approveTenants(List<Long> tenantIds) {
		throw new UnsupportedOperationException();
	}

	public void disapproveTenants(List<Long> tenantIds) {
		throw new UnsupportedOperationException();
	}

	public void deleteTenants(List<Long> tenantIds) {
		throw new UnsupportedOperationException();
	}

	public void inviteUsersAsMembers(Long tenantId,
			List<String> emailOrUsernames) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getTenantsToApprove(List<Filter> filters,
			Paginator paginator) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getAllTenants(List<Filter> filters, Paginator paginator) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getUsersOfTenant(Long tenantId) {
		throw new UnsupportedOperationException();
	}

	public Long getTenantId(String tenantName) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getWorkflowModels(Long tenantId, List<Filter> filters,
			Paginator paginator) {
		throw new UnsupportedOperationException();
	}

	public void importPublishedWorkflowModels(Long tenantId,
			List<Long> workflowModelIds) {
		throw new UnsupportedOperationException();
	}

	public List<com.itmill.toolkit.data.Item> getWorkflowInstances(Long tenantId) {
		throw new UnsupportedOperationException();
	}
}