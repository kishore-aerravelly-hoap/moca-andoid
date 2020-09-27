package com.pearl.examination;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamDetails.
 */
public class ExamDetails {
    
    /** The exam id. */
    private String examId;
    
    /** The title. */
    private String title;
    
    /** The description. */
    private String description;

    /** The subject. */
    private String subject;
    
    /** The subject list. */
    private List<String> subjectList;

    /** The grade. */
    private String grade;
    
    /** The section. */
    private String section;
    
    /** The section list. */
    private List<String> sectionList;
    
    /** The grade list. */
    private List<String> gradeList;

    /** The student id. */
    private String studentId;

    /** The start time. */
    private long startTime;
    
    /** The end time. */
    private long endTime;
    
    /** The duration. */
    private long duration;

    /** The start date. */
    private long startDate;
    
    /** The end date. */
    private long endDate;

    /** The begin date. */
    private long beginDate;
    
    /** The complete date. */
    private long completeDate;
    
    /** The uploaded date. */
    private long uploadedDate;

    /** The state. */
    private String state;
    
    /** The result id. */
    private long resultId;
    
    /** The retakeable. */
    private boolean retakeable;
    
    /** The max points. */
    private int maxPoints;
    
    /** The min points. */
    private int minPoints;
    
    /** The negative points. */
    private boolean negativePoints;
    
    /** The allowed book ids. */
    private ArrayList<Integer> allowedBookIds;
    
    /** The evaluated. */
    private boolean evaluated;
    
    /** The exam category. */
    private String examCategory;
    
    /** The academic year. */
    private String academicYear;
    
    @JsonIgnore
    private boolean isExpired;

    /**
     * Instantiates a new exam details.
     */
    public ExamDetails() {

    }
    
    /**
     * Gets the exam category.
     *
     * @return the examCategory
     */
	public String getExamCategory() {
		return examCategory;
	}

	/**
	 * Sets the exam category.
	 *
	 * @param examCategory the examCategory to set
	 */
	public void setExamCategory(String examCategory) {
		this.examCategory = examCategory;
	}

    /**
     * Gets the title.
     *
     * @return the name
     */
    public String getTitle() {
	return title;
    }

    /**
     * Gets the exam id.
     *
     * @return the id
     */
    public String getExamId() {
	return examId;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * Gets the start time.
     *
     * @return the startTime
     */
    public long getStartTime() {
	return startTime;
    }

    /**
     * Gets the end time.
     *
     * @return the endTime
     */
    public long getEndTime() {
	return endTime;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public String getState() {
	return state;
    }

    /**
     * Gets the result id.
     *
     * @return the resultId
     */
    public long getResultId() {
	return resultId;
    }

    /**
     * Checks if is retakeable.
     *
     * @return the isRetakeable
     */
    public boolean isRetakeable() {
	return retakeable;
    }

    /**
     * Gets the max points.
     *
     * @return the maxPoints
     */
    public int getMaxPoints() {
	return maxPoints;
    }

    /**
     * Gets the min points.
     *
     * @return the minPoints
     */
    public int getMinPoints() {
	return minPoints;
    }

    /**
     * Checks for negative points.
     *
     * @return the negativePoints
     */
    public boolean hasNegativePoints() {
	return negativePoints;
    }

    /**
     * Gets the allowed book ids.
     *
     * @return the allowedBookIds
     */
    public ArrayList<Integer> getAllowedBookIds() {
	return allowedBookIds;
    }

    /**
     * Sets the title.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * Sets the exam id.
     *
     * @param examId the new exam id
     */
    public void setExamId(String examId) {
	this.examId = examId;
    }

    /**
     * Sets the description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Sets the start time.
     *
     * @param startTime the startTime to set
     */
    public void setStartTime(long startTime) {
	this.startTime = startTime;
    }

    /**
     * Sets the end time.
     *
     * @param endTime the endTime to set
     */
    public void setEndTime(long endTime) {
	this.endTime = endTime;
    }

    /**
     * Sets the state.
     *
     * @param state the state to set
     */
    public void setState(String state) {
	this.state = state;
    }

    /**
     * Sets the result id.
     *
     * @param resultId the resultId to set
     */
    public void setResultId(long resultId) {
	this.resultId = resultId;
    }

    /**
     * Sets the retakeable.
     *
     * @param retakeable the new retakeable
     */
    public void setRetakeable(boolean retakeable) {
	this.retakeable = retakeable;
    }

    /**
     * Sets the max points.
     *
     * @param maxPoints the maxPoints to set
     */
    public void setMaxPoints(int maxPoints) {
	this.maxPoints = maxPoints;
    }

    /**
     * Sets the min points.
     *
     * @param minPoints the minPoints to set
     */
    public void setMinPoints(int minPoints) {
	this.minPoints = minPoints;
    }

    /**
     * Sets the negative points.
     *
     * @param hasNegativePoints the new negative points
     */
    public void setNegativePoints(boolean hasNegativePoints) {
	negativePoints = hasNegativePoints;
    }

    /**
     * Sets the allowed book ids.
     *
     * @param allowedBookIds the allowedBookIds to set
     */
    public void setAllowedBookIds(ArrayList<Integer> allowedBookIds) {
	this.allowedBookIds = allowedBookIds;
    }

    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public String getSubject() {
	return subject;
    }

    /**
     * Sets the subject.
     *
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
	this.subject = subject;
    }

    /**
     * Gets the duration.
     *
     * @return the duration
     */
    public long getDuration() {
	return duration;
    }

    /**
     * Sets the duration.
     *
     * @param duration the duration to set
     */
    public void setDuration(long duration) {
	this.duration = duration;
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
     * Gets the subject list.
     *
     * @return the subjectList
     */
    public List<String> getSubjectList() {
	return subjectList;
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
     * Gets the grade list.
     *
     * @return the gradeList
     */
    public List<String> getGradeList() {
	return gradeList;
    }

    /**
     * Gets the start date.
     *
     * @return the startDate
     */
    public long getStartDate() {
	return startDate;
    }

    /**
     * Gets the end date.
     *
     * @return the endDate
     */
    public long getEndDate() {
	return endDate;
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
     * Sets the section list.
     *
     * @param sectionList the sectionList to set
     */
    public void setSectionList(List<String> sectionList) {
	this.sectionList = sectionList;
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
     * Sets the start date.
     *
     * @param startDate the startDate to set
     */
    public void setStartDate(long startDate) {
	this.startDate = startDate;
    }

    /**
     * Sets the end date.
     *
     * @param endDate the endDate to set
     */
    public void setEndDate(long endDate) {
	this.endDate = endDate;
    }

    /**
     * Gets the begin date.
     *
     * @return the beginDate
     */
    public long getBeginDate() {
	return beginDate;
    }

    /**
     * Sets the begin date.
     *
     * @param beginDate the beginDate to set
     */
    public void setBeginDate(long beginDate) {
	this.beginDate = beginDate;
    }

    /**
     * Gets the complete date.
     *
     * @return the completeDate
     */
    public long getCompleteDate() {
	return completeDate;
    }

    /**
     * Sets the complete date.
     *
     * @param completeDate the completeDate to set
     */
    public void setCompleteDate(long completeDate) {
	this.completeDate = completeDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ExamDetails [examId=" + examId + ", title=" + title
		+ ", description=" + description + ", subject=" + subject
		+ ", subjectList=" + subjectList + ", grade=" + grade
		+ ", section=" + section + ", sectionList=" + sectionList
		+ ", gradeList=" + gradeList + ", studentId=" + studentId
		+ ", startTime=" + startTime + ", endTime=" + endTime
		+ ", duration=" + duration + ", startDate=" + startDate
		+ ", endDate=" + endDate + ", beginDate=" + beginDate
		+ ", completeDate=" + completeDate + ", state=" + state
		+ ", resultId=" + resultId + ", isRetakeable=" + retakeable
		+ ", maxPoints=" + maxPoints + ", minPoints=" + minPoints
		+ ", negativePoints=" + negativePoints + ", allowedBookIds="
		+ allowedBookIds + "]";
    }

    /**
     * Checks if is evaluated.
     *
     * @return the evaluated
     */
    public boolean isEvaluated() {
	return evaluated;
    }

    /**
     * Sets the evaluated.
     *
     * @param evaluated the evaluated to set
     */
    public void setEvaluated(boolean evaluated) {
	this.evaluated = evaluated;
    }

    /**
     * Gets the uploaded date.
     *
     * @return the uploadedDate
     */
    public long getUploadedDate() {
	return uploadedDate;
    }

    /**
     * Sets the uploaded date.
     *
     * @param uploadedDate the uploadedDate to set
     */
    public void setUploadedDate(long uploadedDate) {
	this.uploadedDate = uploadedDate;
    }

	/**
	 * Gets the academic year.
	 *
	 * @return the academicDetails
	 */
	public String getAcademicYear() {
		return academicYear;
	}

	/**
	 * Sets the academic year.
	 *
	 * @param academicYear the new academic year
	 */
	public void setAcademicYear(String academicYear) {
		this.academicYear = academicYear;
	}

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}


}