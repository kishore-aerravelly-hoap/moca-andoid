/**
 * 
 */
package com.pearl.ui.models;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Enum RoleType.
 *
 * @author kpamulapati
 */
public enum RoleType {
    
    /** The teacher. */
    TEACHER(1), 
 /** The student. */
 STUDENT(2), 
 /** The parent. */
 PARENT(3), 
 /** The principle. */
 PRINCIPLE(4), 
 /** The subjecthead. */
 SUBJECTHEAD(5), 
 /** The homeroomteacher. */
 HOMEROOMTEACHER(
	    6), 
 /** The admin. */
 ADMIN(7), 
 /** The employer. */
 EMPLOYER(8), 
 /** The asstprincipal. */
 ASSTPRINCIPAL(9);

    /**
     * Instantiates a new role type.
     *
     * @param value the value
     */
    private RoleType(long value) {
	role = value;
    }

    /** The role. */
    private final long role;

    /**
     * Gets the role.
     *
     * @return the role
     */
    public long getRole() {
	return role;
    }

    /**
     * Gets the role types.
     *
     * @return the role types
     */
    public static List<String> getRoleTypes() {
	final List<String> list = new ArrayList<String>();
	final RoleType roleTypes[] = RoleType.values();
	for (final RoleType roleType : roleTypes) {
	    list.add(roleType.name());
	}

	return list;
    }
}
