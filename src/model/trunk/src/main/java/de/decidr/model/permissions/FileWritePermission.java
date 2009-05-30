package de.decidr.model.permissions;

/**
 * Represents the permission to write a file.
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class FileWritePermission extends FilePermission {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param fileId
     */
    public FileWritePermission(Long fileId) {
        super(fileId);
    }    
    
}