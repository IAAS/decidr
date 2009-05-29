package de.decidr.model.facades;

import java.util.List;

import com.vaadin.data.Item;

import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
import de.decidr.model.permissions.Role;

public class SystemFacade extends AbstractFacade {

	public SystemFacade(Role actor) {
	        super(actor);
	}

	public Item getSettings() {
		throw new UnsupportedOperationException();
	}

	public void setSettings(Item settings) {
		throw new UnsupportedOperationException();
	}

	public void addServer(String location) {
		throw new UnsupportedOperationException();
	}

	public void removeServer(String location) {
		throw new UnsupportedOperationException();
	}

	public void updateServerLoad(String location, Integer load) {
		throw new UnsupportedOperationException();
	}

	public void lockServer(String location) {
		throw new UnsupportedOperationException();
	}

	public void unlockServer(String location) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getLog(List<Filter> filters, Paginator paginator) {
		throw new UnsupportedOperationException();
	}

	public List<Item> getServerStatistics() {
		throw new UnsupportedOperationException();
	}
}