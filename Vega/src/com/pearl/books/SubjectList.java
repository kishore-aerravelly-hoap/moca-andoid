package com.pearl.books;

import java.util.ArrayList;
import java.util.List;

import com.pearl.ui.models.Subject;

// TODO: Auto-generated Javadoc
/**
 * The Class SubjectList.
 */
public class SubjectList {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The subject list. */
    List<Subject> subjectList = new ArrayList<Subject>();

    /**
     * Gets the subject list.
     *
     * @return the subjectList
     */
    public List<Subject> getSubjectList() {
	return subjectList;
    }

    /**
     * Sets the subject list.
     *
     * @param subjectList the subjectList to set
     */
    public void setSubjectList(List<Subject> subjectList) {
	this.subjectList = subjectList;
    }

}
