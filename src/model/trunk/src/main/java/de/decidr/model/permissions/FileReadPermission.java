package de.decidr.model.permissions;

/**
 * Represents the permission to read a file.
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class FileReadPermission extends FilePermission {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * 
     * @param fileId
     */
    public FileReadPermission(Long fileId) {
        super(fileId);
    }    
    
}