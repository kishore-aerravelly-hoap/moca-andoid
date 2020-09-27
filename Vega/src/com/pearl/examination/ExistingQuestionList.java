package com.pearl.examination;

import java.util.List;

import com.pearl.user.teacher.examination.ExistingQuestion;

// TODO: Auto-generated Javadoc
/**
 * The Class ExistingQuestionList.
 */
public class ExistingQuestionList {
    
    /** The existing question list. */
    List<ExistingQuestion> existingQuestionList;

/**
 * Gets the existing question list.
 *
 * @return the existing question list
 */
public synchronized List<ExistingQuestion> getExistingQuestionList() {
    return this.existingQuestionList;
}

/**
 * Sets the existing question list.
 *
 * @param existingQuestionList the new existing question list
 */
public synchronized void setExistingQuestionList(
	List<ExistingQuestion> existingQuestionList) {
    this.existingQuestionList = existingQuestionList;
}
}
