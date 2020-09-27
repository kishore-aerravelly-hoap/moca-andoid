/**
 * 
 */
package com.pearl.teacher.examination;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.pearl.ui.models.CommonUtility;
import com.pearl.user.teacher.examination.ExamDetails;


// TODO: Auto-generated Javadoc
/**
 * The Class ServerExamDetails.
 *
 * @author ENG2
 */
public class ServerExamDetails extends ExamDetails implements Serializable {

    /**
     * Instantiates a new server exam details.
     *
     * @param gradeList the grade list
     * @param sectionList the section list
     * @param subjectList the subject list
     * @param grade the grade
     * @param section the section
     * @param startDate the start date
     * @param endDate the end date
     */
    public ServerExamDetails(List<String> gradeList, List<String> sectionList,
	    List<String> subjectList, String grade, String section,
	    Date startDate, Date endDate) {
	super();
	this.gradeList = gradeList;
	this.sectionList = sectionList;
	this.subjectList = subjectList;
	this.grade = grade;
	this.section = section;
	this.startDate = startDate;
	this.endDate = endDate;
    }
    
    /**
     * Instantiates a new server exam details.
     */
    public ServerExamDetails() {
	super();
	// TODO Auto-generated constructor stub
    }
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The grade list. */
    private List<String> gradeList;
    
    /** The section list. */
    private List<String> sectionList;
    
    /** The subject list. */
    private List<String> subjectList;
    
    /** The grade. */
    private String grade;
    
    /** The section. */
    private String section;
    
    /** The start date. */
    private Date startDate;
    
    /** The end date. */
    private Date endDate;
    
    /** The begin date. */
    private String beginDate="";
    
    /** The complete date. */
    private String completeDate="";
    
    /** The student id. */
    private String studentId;
    
    /**
     * Gets the grade list.
     *
     * @return the gradeList
     */
    public List<String> getGradeList() {
	return gradeList;
    }
    
    /**
     * Sets the grade list.
     *
     * @param gradeList the gradeList to set
     */
    public void setGradeList(List<String> gradeList) {
	this.gradeList = gradeList;
    }
    
    /**
     * Gets the section list.
     *
     * @return the sectionList
     */
    public List<String> getSectionList() {
	return sectionList;
    }
    
    /**
     * Sets the section list.
     *
     * @param sectionList the sectionList to set
     */
    public void setSectionList(List<String> sectionList) {
	this.sectionList = sectionList;
    }
    
    /**
     * Gets the subject list.
     *
     * @return the subjectList
     */
    public List<String> getSubjectList() {
	return subjectList;
    }
    
    /**
     * Sets the subject list.
     *
     * @param subjectList the subjectList to set
     */
    public void setSubjectList(List<String> subjectList) {
	this.subjectList = subjectList;
    }
    
    /**
     * Gets the grade.
     *
     * @return the grade
     */
    public String getGrade() {
	return grade;
    }
    
    /**
     * Sets the grade.
     *
     * @param grade the grade to set
     */
    public void setGrade(String grade) {
	this.grade = grade;
    }
    
    /**
     * Gets the section.
     *
     * @return the section
     */
    public String getSection() {
	return section;
    }
    
    /**
     * Sets the section.
     *
     * @param section the section to set
     */
    public void setSection(String section) {
	this.section = section;
    }


    /**
     * Gets the update current object.
     *
     * @param object the object
     * @return the update current object
     */
    public void getUpdateCurrentObject(ServerExamDetails object)
    {
	try{
	    this.setTitle(object.getTitle());
	    //SimpleDateFormat format=new SimpleDateFormat("EEE MMM DD HH:mm:ss zzz yyyy");
	    this.setEndDate(object.getEndDate());
	    this.setEndTime(object.getEndTime());
	    this.setGrade(object.getGrade());
	    this.setSection(object.getSection());
	    this.setStartDate(object.getStartDate());
	    this.setStartTime(object.getStartTime());
	    this.setSubject(object.getSubject());
	    this.setDuration(object.getDuration());
	}catch(final Exception e)
	{
	    e.printStackTrace();
	}
	System.out.println(toString());
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ServerExamDetails [title="+getTitle()+", gradeList=" + gradeList + ", sectionList="
		+ sectionList + ", subjectList=" + subjectList + ", grade="
		+ grade + ", section=" + section + ", startDate=" + startDate
		+ ", endDate=" + endDate + "]";
    }
    
    /**
     * Gets the student id.
     *
     * @return the studentId
     */
    public String getStudentId() {
	return studentId;
    }
    
    /**
     * Sets the student id.
     *
     * @param studentId the studentId to set
     */
    public void setStudentId(String studentId) {
	this.studentId = studentId;
    }
    
    /**
     * Gets the start date.
     *
     * @return the startDate
     */
    public Date getStartDate() {
	if(getBeginDate() == null || getBeginDate().equals("")||getBeginDate().equals("0")) return startDate;
	return CommonUtility.convertStringToDate(getBeginDate(),"MM/dd/yyyy HH:mm:ss");
    }
    
    /**
     * Sets the start date.
     *
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }
    
    /**
     * Gets the end date.
     *
     * @return the endDate
     */
    public Date getEndDate() {
	if(getCompleteDate() == null || getCompleteDate().equals("") ||  getCompleteDate().equals("0") ) return endDate;
	return CommonUtility.convertStringToDate(getCompleteDate(),"MM/dd/yyyy HH:mm:ss");
    }
    
    /**
     * Sets the end date.
     *
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }
    
    /**
     * Gets the begin date.
     *
     * @return the beginDate
     */
    public String getBeginDate() {
	return beginDate;
    }
    
    /**
     * Sets the begin date.
     *
     * @param beginDate the beginDate to set
     */
    public void setBeginDate(String beginDate) {
	this.beginDate = beginDate;
    }
    
    /**
     * Gets the complete date.
     *
     * @return the completeDate
     */
    public String getCompleteDate() {
	return completeDate;
    }
    
    /**
     * Sets the complete date.
     *
     * @param completeDate the completeDate to set
     */
    public void setCompleteDate(String completeDate) {
	this.completeDate = completeDate;
    }

}
