package com.pearl.ui.models;

import java.io.Serializable;

import com.pearl.examination.Exam;
import com.pearl.parsers.json.ExamParser;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamResponse.
 */
public class ExamResponse extends BaseResponse implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The user id. */
    private String userId;
    
    /** The exam. */
    private Exam exam;
    
    /** The id. */
    private String id;

    /**
     * Gets the user id.
     *
     * @return the userId
     */
    public String getUserId() {
	return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
	this.userId = userId;
    }

    /**
     * Gets the exam.
     *
     * @return the notices
     * @throws Exception the exception
     */
    public Exam getExam() throws Exception {
	if (null == exam) {

	    // exam = ExamParserHelper.parseJsonToExamObject(data);

	    exam = ExamParser.getExam(data);
	}

	return exam;
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

}