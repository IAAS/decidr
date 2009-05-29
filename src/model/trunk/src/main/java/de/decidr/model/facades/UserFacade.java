package de.decidr.model.facades;

// import Item;
import java.util.Date;
import java.util.List;

import com.vaadin.data.Item;

import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;
import de.decidr.model.permissions.UserRole;
// import ListItem;

public class UserFacade extends AbstractFacade {

	public UserFacade(Role actor) {
	        super(actor);
	}

	public Long registerUser(String email, String password, Item userProfile) {
		throw new UnsupportedOperationException();
	}

	public Long getUserIdByLogin(String emailOrUsername, String password) {
		throw new UnsupportedOperationException();
	}

	public Long getUserId(String authCode) {
		throw new UnsupportedOperationException();
	}

	public void setEmailAddress(Long userId, String newEmail) {
		throw new UnsupportedOperationException();
	}

	public void setDisableSince(Long userId, Date date) {
		throw new UnsupportedOperationException();
	}

	public void setUnavailableSince(Long userId, Date date) {
		throw new UnsupportedOperationException();
	}

	public void setPassword(Long userId, String oldPassword, String newPassword) {
		throw new UnsupportedOperationException();
	}

	public void requestPasswordReset(String emailOrUsername) {
		throw new UnsupportedOperationException();
	}

	public void setProfile(Long userId, Item newProfile) {
		throw new UnsupportedOperationException();
	}

	public void leaveTenant(Long userId, Long tenantId) {
		throw new UnsupportedOperationException();
	}

	public void removeFromTenant(Long userId, Long tenantId) {
		throw new UnsupportedOperationException();
	}

	public void confirmRegistration(String authKey) {
		throw new UnsupportedOperationException();
	}

	public void confirmChangeEmailRequest(String requestAuthKey) {
		throw new UnsupportedOperationException();
	}

	public void confirmInvitation(Long invitationId) {
		throw new UnsupportedOperationException();
	}

	public void refuseInviation(Long invitationId) {
		throw new UnsupportedOperationException();
	}

	public Item getUserProfile(Long userId) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getAllUsers(List<Filter> filters, Paginator paginator) {
		throw new UnsupportedOperationException();
	}

	public Class<? extends UserRole> getHighestUserRole(Long userId) {
		throw new UnsupportedOperationException();
	}

	public Class<? extends UserRole> getUserRoleForTenant(Long userId, Long tenantId) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getAdministratedTenants(Long userId) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getAdminstratedWorkflowInstances(Long userId) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getJoinedTenants(Long userId) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getAdministratedWorkflowModels(Long userId) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getWorkItems(Long userId, List<Filter> filters, Paginator paginator) {
		throw new UnsupportedOperationException();
	}
}