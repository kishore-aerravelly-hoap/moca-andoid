package com.pearl.ui.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.pearl.examination.ExamDetails;
import com.pearl.examination.ExamDetailsList;
import com.pearl.parsers.JSONParser;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamListResponse.
 */
public class ExamListResponse extends BaseResponse implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The user id. */
    private String userId;
    
    /** The exam details list. */
    private ExamDetailsList examDetailsList;
    
    /** The exams list. */
    private ArrayList<ExamDetails> examsList;

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
     * Gets the exam list.
     *
     * @return the notices
     * @throws Exception the exception
     */
    public ArrayList<ExamDetails> getExamList() throws Exception {
	if (null == examsList) {
	    return examsList = JSONParser.getExamsList(data);
	} else {
	    return examsList;
	}
    }
}