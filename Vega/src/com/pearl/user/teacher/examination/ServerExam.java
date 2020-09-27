/**
 * 
 */
package com.pearl.user.teacher.examination;

import java.io.Serializable;
import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * The Class ServerExam.
 */
public class ServerExam implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    private String id;
    
    /** The exam. */
    private Exam exam;
    //private QuestionPaperStatusType statusType;
    /** The students list. */
    private List<String> studentsList;
    
    /** The student id list. */
    private List<String> studentIdList;
    
    /** The actions. */
    private List<String> actions;
    
    /** The test action. */
    private String testAction;
    
    /** The open books list. */
    private List<String> openBooksList;
    
    /** The open books1. */
    private String openBooks1;

    /**
     * Gets the open books1.
     *
     * @return the open books1
     */
    public String getOpenBooks1() {
	return openBooks1;
    }
    
    /**
     * Sets the open books1.
     *
     * @param openBooks1 the new open books1
     */
    public void setOpenBooks1(String openBooks1) {
	this.openBooks1 = openBooks1;
    }
    
    /**
     * Gets the open books list.
     *
     * @return the open books list
     */
    public List<String> getOpenBooksList() {
	return openBooksList;
    }
    
    /**
     * Sets the open books list.
     *
     * @param openBooksList the new open books list
     */
    public void setOpenBooksList(List<String> openBooksList) {
	this.openBooksList = openBooksList;
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
     * @param id the id to set
     */
    public void setId(String id) {
	this.id = id;
    }
    
    /**
     * Gets the exam.
     *
     * @return the exam
     */
    public Exam getExam() {
	return exam;
    }
    
    /**
     * Sets the exam.
     *
     * @param exam the exam to set
     */
    public void setExam(Exam exam) {
	this.exam = exam;
    }
    
    /**
     * Gets the students list.
     *
     * @return the statusType
     */
    /*public QuestionPaperStatusType getStatusType() {
		return statusType;
	}
     *//**
     * @param statusType the statusType to set
     *//*
	public void setStatusType(QuestionPaperStatusType statusType) {
		this.statusType = statusType;
	}*/
    /**
     * @return the studentsList
     */
    public List<String> getStudentsList() {
	return studentsList;
    }
    
    /**
     * Sets the students list.
     *
     * @param studentsList the studentsList to set
     */
    public void setStudentsList(List<String> studentsList) {
	this.studentsList = studentsList;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ServerExam [id=" + id + ", exam=" + exam + ", statusType="
		+ ", studentsList=" + studentsList
		+ ", studentIdList=" + studentIdList + ", actions=" + actions
		+ ", testAction=" + testAction + ", openBooksList="
		+ openBooksList + ", openBooks=" + openBooks1 + "]";
    }

    /**
     * Gets the student id list.
     *
     * @return the studentIdList
     */
    public List<String> getStudentIdList() {
	return studentIdList;
    }
    
    /**
     * Sets the student id list.
     *
     * @param studentIdList the studentIdList to set
     */
    public void setStudentIdList(List<String> studentIdList) {
	this.studentIdList = studentIdList;
    }
    
    /**
     * Gets the actions.
     *
     * @return the actions
     */
    public List<String> getActions() {
	return actions;
    }
    
    /**
     * Sets the actions.
     *
     * @param actions the actions to set
     */
    public void setActions(List<String> actions) {
	this.actions = actions;
    }
    
    /**
     * Gets the test action.
     *
     * @return the testAction
     */
    public String getTestAction() {
	return testAction;
    }
    
    /**
     * Sets the test action.
     *
     * @param testAction the testAction to set
     */
    public void setTestAction(String testAction) {
	this.testAction = testAction;
    }

}
