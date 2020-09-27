package com.pearl.examination;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Enum QuestionType.
 *
 * @author ENG2
 */
public enum QuestionType {
    
    /** The true or false question. */
    TRUE_OR_FALSE_QUESTION("true_or_false_question"), 
 /** The fill in the blank question. */
 FILL_IN_THE_BLANK_QUESTION(
	    "fill_in_the_blank_question"), 
 /** The multiplechoice question. */
 MULTIPLECHOICE_QUESTION(
		    "multiplechoice_question"), 
 /** The short answer question. */
 SHORT_ANSWER_QUESTION(
			    "short_answer_question"), 
 /** The long answer question. */
 LONG_ANSWER_QUESTION(
				    "long_answer_question"), 
 /** The ordering question. */
 ORDERING_QUESTION("ordering_question"), 
 /** The match the following question. */
 MATCH_THE_FOLLOWING_QUESTION(
					    "match_the_following_question");

    /** The question type. */
    private final String questionType;

    /**
     * Instantiates a new question type.
     *
     * @param questionType the question type
     */
    private QuestionType(String questionType) {
	this.questionType = questionType;
    }

    /**
     * Gets the question type.
     *
     * @return the question type
     */
    public static List<String> getQuestionType() {
	final List<String> list = new ArrayList<String>();
	final QuestionType questionTypes[] = QuestionType.values();
	for (final QuestionType questionType : questionTypes) {
	    list.add(questionType.name());
	}

	return list;
    }

}
