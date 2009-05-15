package de.decidr.model.permissions;

import de.decidr.model.entities.File;

public class FilePermission extends de.decidr.model.permissions.Permission {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Long fileId;

	public FilePermission(Long fileId) {
		super(File.class.getCanonicalName() + "." + fileId.toString());
		this.fileId = fileId;
	}

	public Long getFileId() {
		return this.fileId;
	}
}