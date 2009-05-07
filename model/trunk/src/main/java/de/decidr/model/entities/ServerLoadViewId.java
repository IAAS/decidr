package de.decidr.model.entities;

// Generated 07.05.2009 13:21:40 by Hibernate Tools 3.2.2.GA

/**
 * ServerLoadViewId generated by hbm2java
 */
public class ServerLoadViewId implements java.io.Serializable {

	private long id;
	private String location;
	private byte load;
	private boolean locked;
	private boolean dynamicallyAdded;
	private long numInstances;

	public ServerLoadViewId() {
	}

	public ServerLoadViewId(long id, String location, byte load,
			boolean locked, boolean dynamicallyAdded, long numInstances) {
		this.id = id;
		this.location = location;
		this.load = load;
		this.locked = locked;
		this.dynamicallyAdded = dynamicallyAdded;
		this.numInstances = numInstances;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public byte getLoad() {
		return this.load;
	}

	public void setLoad(byte load) {
		this.load = load;
	}

	public boolean isLocked() {
		return this.locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isDynamicallyAdded() {
		return this.dynamicallyAdded;
	}

	public void setDynamicallyAdded(boolean dynamicallyAdded) {
		this.dynamicallyAdded = dynamicallyAdded;
	}

	public long getNumInstances() {
		return this.numInstances;
	}

	public void setNumInstances(long numInstances) {
		this.numInstances = numInstances;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ServerLoadViewId))
			return false;
		ServerLoadViewId castOther = (ServerLoadViewId) other;

		return (this.getId() == castOther.getId())
				&& ((this.getLocation() == castOther.getLocation()) || (this
						.getLocation() != null
						&& castOther.getLocation() != null && this
						.getLocation().equals(castOther.getLocation())))
				&& (this.getLoad() == castOther.getLoad())
				&& (this.isLocked() == castOther.isLocked())
				&& (this.isDynamicallyAdded() == castOther.isDynamicallyAdded())
				&& (this.getNumInstances() == castOther.getNumInstances());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getId();
		result = 37 * result
				+ (getLocation() == null ? 0 : this.getLocation().hashCode());
		result = 37 * result + this.getLoad();
		result = 37 * result + (this.isLocked() ? 1 : 0);
		result = 37 * result + (this.isDynamicallyAdded() ? 1 : 0);
		result = 37 * result + (int) this.getNumInstances();
		return result;
	}

}
