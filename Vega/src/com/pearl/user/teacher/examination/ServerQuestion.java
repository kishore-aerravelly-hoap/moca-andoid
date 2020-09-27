/**
 * 
 */
package com.pearl.user.teacher.examination;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import com.pearl.examination.QuestionType;


// TODO: Auto-generated Javadoc
/**
 * The Class ServerQuestion.
 *
 * @author ENG2
 */
public class ServerQuestion extends Question {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The chapter list. */
    private List<String> chapterList;
    
    /** The chapter name. */
    private String chapterName;
    
    /** The has multiple answers. */
    private boolean hasMultipleAnswers;
    
    /** The multi choice qn a. */
    @SuppressWarnings("unchecked")
    private List<MultiChoiceQuestionAnswer> multiChoiceQnA=LazyList.decorate(new ArrayList(),FactoryUtils.instantiateFactory(MultiChoiceQuestionAnswer.class));
    
    /** The check values. */
    private String checkValues;
    
    /** The short answer. */
    private StringBuffer shortAnswer;
    
    /** The long answer. */
    private StringBuffer longAnswer;
    
    /** The question type list. */
    private List<String> questionTypeList;
    
    /** The marksrange. */
    private int[] marksrange=new int[2]; 
    
    /** The truefalse answer. */
    private boolean truefalseAnswer;


    /**
     * Gets the chapter list.
     *
     * @return the chapter list
     */
    public List<String> getChapterList() {

	return chapterList;
    }

    /**
     * Sets the chapter list.
     *
     * @param chapterList the chapterList to set
     */
    public void setChapterList(List<String> chapterList) {
	this.chapterList = chapterList;
    }

    /**
     * Gets the chapter name.
     *
     * @return the chapterName
     */
    @Override
    public String getChapterName() {
	return chapterName;
    }

    /**
     * Sets the chapter name.
     *
     * @param chapterName the chapterName to set
     */
    @Override
    public void setChapterName(String chapterName) {
	this.chapterName = chapterName;
    }

    /**
     * Gets the question type list.
     *
     * @return the questionTypeList
     */
    public List<String> getQuestionTypeList() {
	questionTypeList = QuestionType.getQuestionType();
	return questionTypeList;
    }

    /**
     * Gets the multi choice qn a.
     *
     * @return the multiChoiceQnA
     */
    public List<MultiChoiceQuestionAnswer> getMultiChoiceQnA() {
	return multiChoiceQnA;
    }

    /**
     * Sets the multi choice qn a.
     *
     * @param multiChoiceQnA the multiChoiceQnA to set
     */
    public void setMultiChoiceQnA(List<MultiChoiceQuestionAnswer> multiChoiceQnA) {
	this.multiChoiceQnA = multiChoiceQnA;
    }

    /**
     * Gets the short answer.
     *
     * @return the shortAnswer
     */
    public StringBuffer getShortAnswer() {
	return shortAnswer;
    }

    /**
     * Sets the short answer.
     *
     * @param shortAnswer the shortAnswer to set
     */
    public void setShortAnswer(StringBuffer shortAnswer) {
	this.shortAnswer = shortAnswer;
    }

    /**
     * Gets the long answer.
     *
     * @return the longAnswer
     */
    public StringBuffer getLongAnswer() {
	return longAnswer;
    }

    /**
     * Sets the long answer.
     *
     * @param longAnswer the longAnswer to set
     */
    public void setLongAnswer(StringBuffer longAnswer) {
	this.longAnswer = longAnswer;
    }

    /**
     * Sets the question type list.
     *
     * @param questionTypeList the questionTypeList to set
     */
    public void setQuestionTypeList(List<String> questionTypeList) {
	this.questionTypeList = questionTypeList;
    }

    /**
     * Gets the checks for multiple answers.
     *
     * @return the hasMultipleAnswers
     */
    public boolean getHasMultipleAnswers() {
	return hasMultipleAnswers;
    }

    /**
     * Sets the checks for multiple answers.
     *
     * @param hasMultipleAnswers the hasMultipleAnswers to set
     */
    public void setHasMultipleAnswers(boolean hasMultipleAnswers) {
	this.hasMultipleAnswers = hasMultipleAnswers;
    }

    /**
     * Gets the truefalse answer.
     *
     * @return the truefalseAnswer
     */
    public boolean getTruefalseAnswer() {
	return truefalseAnswer;
    }

    /**
     * Sets the truefalse answer.
     *
     * @param truefalseAnswer the truefalseAnswer to set
     */
    public void setTruefalseAnswer(boolean truefalseAnswer) {
	this.truefalseAnswer = truefalseAnswer;
    }


    /**
     * Gets the check values.
     *
     * @return the checkValues
     */
    public String getCheckValues() {
	return checkValues;
    }

    /**
     * Sets the check values.
     *
     * @param checkValues the checkValues to set
     */
    public void setCheckValues(String checkValues) {
	this.checkValues = checkValues;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ServerQuestion [type="+type+",chapterList=" + chapterList + ", chapterName="
		+ chapterName + ", hasMultipleAnswers=" + hasMultipleAnswers
		+ ", multiChoiceQnA="  + ", checkValues="
		+ checkValues + ", shortAnswer=" + shortAnswer
		+ ", longAnswer=" + longAnswer + ", questionTypeList="
		+ questionTypeList + ", truefalseAnswer=" + truefalseAnswer
		+ "]";
    }

    /**
     * Gets the marksrange.
     *
     * @return the marksrange
     */
    public int[] getMarksrange() {
	return marksrange;
    }

    /**
     * Sets the marksrange.
     *
     * @param marksrange the marksrange to set
     */
    public void setMarksrange(int[] marksrange) {
	this.marksrange = marksrange;
    }




}
