package de.decidr.model.permissions;

import de.decidr.model.entities.File;
/**
 * Represents the permission to read from or write to a file.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 *
 * @version 0.1
 */
public class FilePermission extends EntityPermission {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param fileId
     */
    public FilePermission(Long fileId) {
        super(File.class.getCanonicalName(), fileId);
    }

}
