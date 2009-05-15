package de.decidr.model.entities;

// Generated 15.05.2009 17:31:29 by Hibernate Tools 3.2.2.GA

/**
 * UserHasFileAccess generated by hbm2java
 */
public class UserHasFileAccess implements java.io.Serializable {

	private UserHasFileAccessId id;
	private User user;
	private File file;
	private boolean mayRead;
	private boolean mayDelete;
	private boolean mayReplace;

	public UserHasFileAccess() {
	}

	public UserHasFileAccess(UserHasFileAccessId id, User user, File file,
			boolean mayRead, boolean mayDelete, boolean mayReplace) {
		this.id = id;
		this.user = user;
		this.file = file;
		this.mayRead = mayRead;
		this.mayDelete = mayDelete;
		this.mayReplace = mayReplace;
	}

	public UserHasFileAccessId getId() {
		return this.id;
	}

	public void setId(UserHasFileAccessId id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public File getFile() {
		return this.file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isMayRead() {
		return this.mayRead;
	}

	public void setMayRead(boolean mayRead) {
		this.mayRead = mayRead;
	}

	public boolean isMayDelete() {
		return this.mayDelete;
	}

	public void setMayDelete(boolean mayDelete) {
		this.mayDelete = mayDelete;
	}

	public boolean isMayReplace() {
		return this.mayReplace;
	}

	public void setMayReplace(boolean mayReplace) {
		this.mayReplace = mayReplace;
	}

}
