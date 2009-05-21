package de.decidr.model.permissions;

import java.security.BasicPermission;

/**
 * Represents a resource to which access is granted or denied.
 * 
 * The DecidR Permission follows the same naming convention as BasicPermission:
 * <P>
 * The name for a Permission is the name of the given permission (for example,
 * "exit", "setFactory", "print.queueJob", etc). The naming convention follows
 * the hierarchical property naming convention. An asterisk may appear by
 * itself, or if immediately preceded by a "." may appear at the end of the
 * name, to signify a wildcard match. For example, "*" and "java.*" are valid,
 * while "*java", "a*b", and "java*" are not valid.
 * 
 * @author Markus Fischer
 * @author Daniel Huss
 * 
 * @version 0.1
 */
public class Permission extends BasicPermission {

    private static final long serialVersionUID = 1L;

    public Permission(String name) {
        super(name);
    }
}