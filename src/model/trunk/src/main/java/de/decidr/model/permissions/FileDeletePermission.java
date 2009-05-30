package de.decidr.model.permissions;

/**
 * Represents the permission to delete a file.
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class FileDeletePermission extends FilePermission {
    
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param fileId
     */
    public FileDeletePermission(Long fileId) {
        super(fileId);
    }

}
