package com.pearl.users;

// TODO: Auto-generated Javadoc
/**
 * The Class Role.
 */
public class Role {
    
    /** The Constant STUDENT. */
    public static final String STUDENT = "ROLE_STUDENT";
    
    /** The Constant TEACHER. */
    public static final String TEACHER = "teacher";
    
    /** The Constant GUEST. */
    public static final String GUEST = "guest";

    /** The type. */
    private String type = Role.GUEST;

    /** The role name. */
    private String roleName;
    
    /** The role id. */
    private String roleId;

    /**
     * Instantiates a new role.
     */
    public Role() {
	type = Role.GUEST;
    }

    /**
     * Instantiates a new role.
     *
     * @param type the type
     */
    public Role(String type) {
	this.type = type;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
	return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public void setType(String type) {
	this.type = type;
    }

    /**
     * Gets the role name.
     *
     * @return the roleName
     */
    public String getRoleName() {
	return roleName;
    }

    /**
     * Gets the role id.
     *
     * @return the roleId
     */
    public String getRoleId() {
	return roleId;
    }

    /**
     * Sets the role name.
     *
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
	this.roleName = roleName;
    }

    /**
     * Sets the role id.
     *
     * @param roleId the roleId to set
     */
    public void setRoleId(String roleId) {
	this.roleId = roleId;
    }
}