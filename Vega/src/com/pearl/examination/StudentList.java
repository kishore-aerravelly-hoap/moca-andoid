package com.pearl.examination;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class StudentList.
 */
public class StudentList {

    /** The studentlist. */
    private List<String> studentlist;

    /**
     * Instantiates a new student list.
     */
    public StudentList() {
	super();
    }

    /**
     * Gets the studentlist.
     *
     * @return the studentlist
     */
    public List<String> getStudentlist() {
	return studentlist;
    }

    /**
     * Sets the studentlist.
     *
     * @param studentlist the new studentlist
     */
    public void setStudentlist(List<String> studentlist) {
	this.studentlist = studentlist;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "StudentList [studentlist=" + studentlist + "]";
    }
}
