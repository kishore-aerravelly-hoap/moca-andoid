package com.pearl.student;

import java.util.Comparator;

// TODO: Auto-generated Javadoc
/**
 * The Class Student.
 *
 * @author Samreen
 */
public class Student implements Comparator<Student> {

    /** The name. */
    private String name;
    
    /** The id. */
    private String id;
    
    /** The ispresent. */
    private boolean ispresent;
    
    /** The sort by id. */
    private boolean sortById;
    
    /** The role call id. */
    private String roleCallId;

    /** The student. */
    private static Student student = null;

    /**
     * Instantiates a new student.
     */
    public Student() {
	super();
    }

    /**
     * Gets the single instance of Student.
     *
     * @return single instance of Student
     */
    public static Student getInstance() {
	if (student == null) {
	    student = new Student();
	}

	return student;
    }

    /**
     * Checks if is sort by id.
     *
     * @return true, if is sort by id
     */
    public boolean isSortById() {
	return sortById;
    }

    /**
     * Sets the sort by id.
     *
     * @param sortById the new sort by id
     */
    public void setSortById(boolean sortById) {
	this.sortById = sortById;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(String id) {
	this.id = id;
    }

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Student object1, Student object2) {
	if (isSortById()) {
	    return object1.getId().compareTo(object2.getId());
	} else {
	    return object1.getName().compareTo(object2.getName());
	}
    }

    /**
     * Checks if is ispresent.
     *
     * @return the ispresent
     */
    public boolean isIspresent() {
	return ispresent;
    }

    /**
     * Sets the ispresent.
     *
     * @param ispresent the ispresent to set
     */
    public void setIspresent(boolean ispresent) {
	this.ispresent = ispresent;
    }

    /**
     * Gets the role call id.
     *
     * @return the roleCallId
     */
    public String getRoleCallId() {
	return roleCallId;
    }

    /**
     * Sets the role call id.
     *
     * @param roleCallId the roleCallId to set
     */
    public void setRoleCallId(String roleCallId) {
	this.roleCallId = roleCallId;
    }

}
