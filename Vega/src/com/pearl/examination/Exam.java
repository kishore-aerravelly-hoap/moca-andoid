package com.pearl.examination;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import android.util.Log;

import com.pearl.examination.exceptions.QuestionDoesNotExistsException;
import com.pearl.examination.exceptions.StudentNotAllowedToExamException;

// TODO: Auto-generated Javadoc
/**
 * The Class Exam.
 */
public class Exam implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id. */
    private String id;
    
    /** The exam details. */
    protected ExamDetails examDetails;

    /** The student id. */
    protected String studentId;
    
    /** The question paper set. */
    protected String questionPaperSet;
    
    /** The questions. */
    protected List<Question> questions;
    
    /** The open books. */
    protected List<String> openBooks;
    
    /** The open book exam. */
    protected boolean openBookExam;
    
    /** The exam created teacher id. */
    private String examCreatedTeacherId;

    // EXAM STATE - CONSTANTS
    /** The Constant SCHEDULED. */
    public static final String SCHEDULED = "scheduled";
    
    /** The Constant TIME_TO_START. */
    public static final String TIME_TO_START = "time_to_start";
    
    /** The Constant INPROGRESS. */
    public static final String INPROGRESS = "in_progress";
    
    /** The Constant YET_TO_UPLOAD. */
    public static final String YET_TO_UPLOAD = "yet_to_upload";
    
    /** The Constant UPLOADING. */
    public static final String UPLOADING = "uploading";
    
    /** The Constant UPLOAD_FAILED. */
    public static final String UPLOAD_FAILED = "upload_failed";
    
    /** The Constant MISSED_EXAM. */
    public static final String MISSED_EXAM = "missed_exam";
    
    /** The Constant STUDENT_SUBMITTED. */
    public static final String STUDENT_SUBMITTED = "student_submitted";
    
    /** The Constant TEACHER_EVALUATED. */
    public static final String TEACHER_EVALUATED = "teacher_evaluated";
    
    /** The Constant RESULTS_RELEASED. */
    public static final String RESULTS_RELEASED = "results_released";
    
    /** The Constant RETAKE_EXAM. */
    public static final String RETAKE_EXAM = "retake_exam";

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Exam [id=" + id + ", examDetails=" + examDetails
		+ ", studentId=" + studentId + ", questionPaperSet="
		+ questionPaperSet + ", questions=" + questions
		+ ", openBooks=" + openBooks + ", openBookExam=" + openBookExam
		+ ", examCreatedTeacherId=" + examCreatedTeacherId + "]";
    }

    /**
     * To xml.
     *
     * @return the string
     */
    @JsonIgnore
    public String toXML() {
	String xml = "<?xml version='1.0' encoding='utf-8' ?>";
	xml += "<testpaper version='1.0'>";
	xml += "	<header lang='en' date='2011-12-02 09:56:14'>";
	xml += "		<name>" + this.getExamDetails().getTitle() + "</name>";
	xml += "		<id>" + this.getExamDetails().getExamId() + "</id>";
	xml += "		<description>" + this.getExamDetails().getDescription()
		+ "</description>";
	xml += "		<starttime>" + this.getExamDetails().getStartDate()
		+ "</starttime>";
	xml += "		<endtime>" + this.getExamDetails().getEndDate()
		+ "</endtime>";
	xml += "		<duration>" + this.getExamDetails().getDuration()
		+ "</duration>";
	xml += "		<maxpoints>" + this.getExamDetails().getMaxPoints()
		+ "</maxpoints>";
	xml += "		<negativepoints>" + this.getExamDetails().hasNegativePoints()
		+ "</negativepoints>";
	xml += "		<passpoints>" + this.getExamDetails().getMinPoints()
		+ "</passpoints>";
	xml += "		<books>";
	if (null != this.getExamDetails().getAllowedBookIds()) {
	    for (final Integer bookId : this.getExamDetails().getAllowedBookIds()) {
		xml += "		<book>" + bookId + "</book>";
	    }
	}
	xml += "		</books>";
	xml += "	</header>";
	xml += "	<questions>";
	for (final Question q : questions) {
	    xml += q.toXML();
	}
	xml += "	</questions>";
	xml += "</testpaper>";

	return xml;
    }

    /*
     * public String toJSON(){ String json = "";
     * 
     * json += "";
     * 
     * return json; }
     */

    /**
     * GETTERS And SETTERS.
     *
     * @return the number of questions
     */
    @JsonIgnore
    public int getNumberOfQuestions() {
	return questions.size();
    }

    /**
     * Gets the question with number.
     *
     * @param questionNo the question no
     * @return the question with number
     * @throws QuestionDoesNotExistsException the question does not exists exception
     */
    @JsonIgnore
    public Question getQuestionWithNumber(int questionNo)
	    throws QuestionDoesNotExistsException {
	if (questions != null) {
	    try {
		if (questionNo <= getNumberOfQuestions()) {
		    return questions.get(questionNo);
		}
	    } catch (final ArrayIndexOutOfBoundsException e) {
		Log.e("EXAM ", "-----------ArrayIndexOutOfBoundsException" + e);
	    }
	}

	throw new QuestionDoesNotExistsException();
    }

    /**
     * Sets the correct answer.
     *
     * @param questionNo the question no
     * @param answer the answer
     */
    public void setCorrectAnswer(int questionNo, Answer answer) {
	questions.get(questionNo - 1).setCorrectAnswer(answer);
    }

    /**
     * Sets the student answer.
     *
     * @param questionNo the question no
     * @param answer the answer
     */
    public void setStudentAnswer(int questionNo, Answer answer) {
	questions.get(questionNo - 1).setStudentAnswer(answer);
    }

    /**
     * Gets the question paper set.
     *
     * @return the questionPaperSet
     */
    public String getQuestionPaperSet() {
	return questionPaperSet;
    }

    /**
     * Gets the questions.
     *
     * @return the questions
     */
    public List<Question> getQuestions() {
	return questions;
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
     * Sets the question paper set.
     *
     * @param questionPaperSet the questionPaperSet to set
     */
    public void setQuestionPaperSet(String questionPaperSet) {
	this.questionPaperSet = questionPaperSet;
    }

    /**
     * Sets the questions.
     *
     * @param questions the questions to set
     */
    public void setQuestions(List<Question> questions) {
	this.questions = questions;
    }

    /**
     * Sets the student id.
     *
     * @param studentId the studentId to set
     * @throws StudentNotAllowedToExamException the student not allowed to exam exception
     */
    public void setStudentId(String studentId)
	    throws StudentNotAllowedToExamException {
	this.studentId = studentId;
    }

    /**
     * Gets the exam details.
     *
     * @return the examDetails
     */
    public ExamDetails getExamDetails() {
	return examDetails;
    }

    /**
     * Sets the exam details.
     *
     * @param examDetails the examDetails to set
     */
    public void setExamDetails(ExamDetails examDetails) {
	this.examDetails = examDetails;
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
     * Gets the open books.
     *
     * @return the openBooks
     */
    public List<String> getOpenBooks() {
	return openBooks;
    }

    /**
     * Sets the open books.
     *
     * @param openBooks the openBooks to set
     */
    public void setOpenBooks(List<String> openBooks) {
	this.openBooks = openBooks;
    }

    /**
     * Checks if is open book exam.
     *
     * @return the openBookExam
     */
    public boolean isOpenBookExam() {
	return openBookExam;
    }

    /**
     * Sets the open book exam.
     *
     * @param openBookExam the openBookExam to set
     */
    public void setOpenBookExam(boolean openBookExam) {
	this.openBookExam = openBookExam;
    }
    
    /**
     * Gets the exam created teacher id.
     *
     * @return the exam created teacher id
     */
    public String getExamCreatedTeacherId() {
	return examCreatedTeacherId;
    }

    /**
     * Sets the exam created teacher id.
     *
     * @param examCreatedTeacherId the examCreatedTeacherId to set
     */
    public void setExamCreatedTeacherId(String examCreatedTeacherId) {
	this.examCreatedTeacherId = examCreatedTeacherId;
    }
}