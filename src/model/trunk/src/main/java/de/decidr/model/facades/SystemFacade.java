package de.decidr.model.facades;

import java.util.List;

import com.itmill.toolkit.data.Item;

import de.decidr.model.filters.Filter;
import de.decidr.model.filters.Paginator;
/**
 * FIXME: add itmill dependeny to maven 
 */
public class SystemFacade extends AbstractFacade {

	public SystemFacade(Long actorId) {
		super(actorId);
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