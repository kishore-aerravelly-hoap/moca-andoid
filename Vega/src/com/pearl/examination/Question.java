package com.pearl.examination;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class Question.
 */
public class Question {
    
    /** The id. */
    protected String id;
    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Question [id=" + id + ", choices=" + choices
				+ ", correctAnswer=" + correctAnswer + ", studentAnswer="
				+ studentAnswer + ", getAnswer()=" + getAnswer()
				+ ", getCorrectAnswer()=" + getCorrectAnswer()
				+ ", getStudentAnswer()=" + getStudentAnswer() + ", getId()="
				+ getId() + ", getChoices()=" + getChoices() + "]";
	}

	/** The question order no. */
	protected int questionOrderNo = 0;
    
    /** The question. */
    protected String question;
    
    /** The description. */
    protected String description;
    
    /** The duration in secs. */
    protected long durationInSecs;
    
    /** The hint. */
    protected String hint;
    
    /** The type. */
    protected String type;
    
    /** The difficulty level. */
    protected int difficultyLevel;
    
    /** The chapter name. */
    protected String chapterName; // Added chapterName for feature reference to
    // fetch existing questions while preparing
    // questionPaper.
    // TODO protected CountDownTimer timer;
    // protected boolean isManualCorrection = false;
    /** The choices. */
    List<AnswerChoice> choices;

    /** The correct answer. */
    protected Answer correctAnswer;
    
    /** The marks alloted. */
    protected int marksAlloted = 0;
    
    /** The is corrected. */
    protected boolean isCorrected = false;

    /** The student answer. */
    protected Answer studentAnswer = new Answer();
    
    /** The marks awarded. */
    protected double marksAwarded = 0;
    
    /** The marks alloted by teacher. */
    @JsonIgnore
    private boolean marksAllotedByTeacher;
    
    /** The is answered. */
    protected boolean isAnswered = false;
    
    /** The viewed. */
    protected boolean viewed = false;
    
    /** The is marked. */
    protected boolean isMarked = false;

    /** The is correct. */
    protected boolean isCorrect = false;

    /** The Constant SHORT_ANSWER_QUESTION. */
    public static final String SHORT_ANSWER_QUESTION = "SHORT_ANSWER_QUESTION";
    
    /** The Constant LONG_ANSWER_QUESTION. */
    public static final String LONG_ANSWER_QUESTION = "LONG_ANSWER_QUESTION";
    
    /** The Constant TRUE_OR_FALSE_QUESTION. */
    public static final String TRUE_OR_FALSE_QUESTION = "TRUE_OR_FALSE_QUESTION";
    
    /** The Constant MULTIPLECHOICE_QUESTION. */
    public static final String MULTIPLECHOICE_QUESTION = "MULTIPLECHOICE_QUESTION";
    
    /** The Constant FILL_IN_THE_BLANK_QUESTION. */
    public static final String FILL_IN_THE_BLANK_QUESTION = "FILL_IN_THE_BLANK_QUESTION";
    
    /** The Constant ORDERING_QUESTION. */
    public static final String ORDERING_QUESTION = "ORDERING_QUESTION";
    
    /** The Constant MATCH_THE_FOLLOWING. */
    public static final String MATCH_THE_FOLLOWING = "MATCH_THE_FOLLOWING_QUESTION";

    /**
     * HELPER FUNCTIONS.
     *
     * @param answer the new answer
     */
    /*
     * public boolean isCorrectAnswer() throws
     * ManualCorrectionTypeQuestionException { if (!isManualCorrection) { if
     * (studentAnswer.equals(correctAnswer)) { this.setCorrect(true); } else {
     * this.setCorrect(false); }
     * 
     * this.setCorrected(true);
     * 
     * return this.isCorrect(); } else { throw new
     * ManualCorrectionTypeQuestionException(); } }
     */

    public void setAnswer(Answer answer) {
	studentAnswer = answer;
    }

    /**
     * Gets the answer.
     *
     * @return the answer
     */
    public Answer getAnswer() {
	return studentAnswer;
    }

    /**
     * Gets the answer xml.
     *
     * @return the answer xml
     */
    @JsonIgnore
    public String getAnswerXML() {
	if (null != studentAnswer) {
	    return studentAnswer.toXML();
	}

	return "<answer></answer>";
    }

    /**
     * To xml.
     *
     * @return the string
     */
    @JsonIgnore
    public String toXML() {
	String xml = "";

	xml += "<question>";
	xml += "<id>" + id + "</id>";
	xml += "<type>" + type + "</type>";
	xml += "<points>" + marksAlloted + "</points>";
	xml += "<position>" + questionOrderNo + "</position>";
	xml += "<timer>" + durationInSecs + "</timer>";
	xml += "<description>" + question + "</description>";
	xml += "<explanation>" + hint + "</explanation>";

	xml += getAnswerXML();

	xml += "</question>";

	return xml;
    }

    /**
     * GETTERS AND SETTERS.
     *
     * @return the question
     */

    /**
     * @return the question
     */
    public String getQuestion() {
	return question;
    }

    /**
     * Gets the difficulty level.
     *
     * @return the difficultyLevel
     */
    public int getDifficultyLevel() {
	return difficultyLevel;
    }

    /**
     * Gets the correct answer.
     *
     * @return the correctAnswer
     */
    public Answer getCorrectAnswer() {
	return correctAnswer;
    }

    /**
     * Gets the marks alloted.
     *
     * @return the marksAlloted
     */
    public int getMarksAlloted() {
	return marksAlloted;
    }

    /**
     * Checks if is corrected.
     *
     * @return the isCorrected
     */
    public boolean isCorrected() {
	return isCorrected;
    }

    /**
     * Gets the student answer.
     *
     * @return the studentAnswer
     */
    public Answer getStudentAnswer() {
	return studentAnswer;
    }

    /**
     * Gets the marks awarded.
     *
     * @return the marksAwarded
     */
    public double getMarksAwarded() {
	return marksAwarded;
    }

    /**
     * Checks if is answered.
     *
     * @return the isAnswered
     */
    public boolean isAnswered() {
	return isAnswered;
    }

    /**
     * Gets the checks if is answered.
     *
     * @return the checks if is answered
     */
    public boolean getIsAnswered() {
	return isAnswered;
    }

    /**
     * Checks if is marked.
     *
     * @return the isMarked
     */
    public boolean isMarked() {
	return isMarked;
    }

    /**
     * Checks if is correct.
     *
     * @return the isCorrect
     */
    public boolean isCorrect() {
	return isCorrect;
    }

    /**
     * Sets the question.
     *
     * @param question the question to set
     */
    public void setQuestion(String question) {
	this.question = question;
    }

    /**
     * Sets the difficulty level.
     *
     * @param difficultyLevel the difficultyLevel to set
     */
    public void setDifficultyLevel(int difficultyLevel) {
	this.difficultyLevel = difficultyLevel;
    }

    /**
     * Sets the correct answer.
     *
     * @param correctAnswer the correctAnswer to set
     */
    public void setCorrectAnswer(Answer correctAnswer) {
	this.correctAnswer = correctAnswer;
    }

    /**
     * Sets the marks alloted.
     *
     * @param marksAlloted the marksAlloted to set
     */
    public void setMarksAlloted(int marksAlloted) {
	this.marksAlloted = marksAlloted;
    }

    /**
     * Sets the corrected.
     *
     * @param isCorrected the isCorrected to set
     */
    public void setCorrected(boolean isCorrected) {
	this.isCorrected = isCorrected;
    }

    /**
     * Sets the student answer.
     *
     * @param studentAnswer the studentAnswer to set
     */
    public void setStudentAnswer(Answer studentAnswer) {
	this.studentAnswer = studentAnswer;
    }

    /**
     * Sets the marks awarded.
     *
     * @param marksAwarded the marksAwarded to set
     */
    public void setMarksAwarded(double marksAwarded) {
	this.marksAwarded = marksAwarded;
    }

    /**
     * Sets the checks if is answered.
     *
     * @param isAnswered the isAnswered to set
     */
    public void setIsAnswered(boolean isAnswered) {
	this.isAnswered = isAnswered;
    }

    /**
     * Sets the answered.
     *
     * @param isAnswered the new answered
     */
    public void setAnswered(boolean isAnswered) {
	this.isAnswered = isAnswered;
    }

    /**
     * Sets the marked.
     *
     * @param isMarked the isMarked to set
     */
    public void setMarked(boolean isMarked) {
	Logger.warn("Question", "marked value before setting is:" + isMarked);
	this.isMarked = isMarked;
	Logger.warn("Question", "marked value after setting is:"
		+ this.isMarked);
    }

    /**
     * Sets the correct.
     *
     * @param isCorrect the isCorrect to set
     */
    public void setCorrect(boolean isCorrect) {
	this.isCorrect = isCorrect;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
	return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     * @return the isManualCorrection
     */
    /*
     * public boolean isManualCorrection() { return isManualCorrection; }
     */

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
	this.type = type;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    /*
     * public void setManualCorrection(boolean isManualCorrection) {
     * this.isManualCorrection = isManualCorrection; }
     */

    /**
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
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
	return description;
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
     * Gets the hint.
     *
     * @return the hint
     */
    public String getHint() {
	return hint;
    }

    /**
     * Sets the hint.
     *
     * @param hint the hint to set
     */
    public void setHint(String hint) {
	this.hint = hint;
    }

    /**
     * Gets the question order no.
     *
     * @return the questionOrderNo
     */
    public int getQuestionOrderNo() {
	return questionOrderNo;
    }

    /**
     * Sets the question order no.
     *
     * @param questionOrderNo the questionOrderNo to set
     */
    public void setQuestionOrderNo(int questionOrderNo) {
	this.questionOrderNo = questionOrderNo;
    }

    /**
     * Gets the duration in secs.
     *
     * @return the durationInSecs
     */
    public long getDurationInSecs() {
	return durationInSecs;
    }

    /**
     * Sets the duration in secs.
     *
     * @param durationInSecs the durationInSecs to set
     */
    public void setDurationInSecs(long durationInSecs) {
	this.durationInSecs = durationInSecs;
    }

    /**
     * Gets the chapter name.
     *
     * @return the chapterName
     */
    public String getChapterName() {
	return chapterName;
    }

    /**
     * Sets the chapter name.
     *
     * @param chapterName the chapterName to set
     */
    public void setChapterName(String chapterName) {
	this.chapterName = chapterName;
    }

    /**
     * Gets the choices.
     *
     * @return the choices
     */
    public List<AnswerChoice> getChoices() {
	return choices;
    }

    /**
     * Sets the choices.
     *
     * @param choices the new choices
     */
    public void setChoices(List<AnswerChoice> choices) {
	this.choices = choices;
    }

    /**
     * Checks if is viewed.
     *
     * @return the viewed
     */
    public boolean isViewed() {
	return viewed;
    }

    /**
     * Sets the viewed.
     *
     * @param viewed the viewed to set
     */
    public void setViewed(boolean viewed) {
	this.viewed = viewed;
    }

    /**
     * Checks if is marks alloted by teacher.
     *
     * @return the marksAllotedByTeacher
     */
    public boolean isMarksAllotedByTeacher() {
	return marksAllotedByTeacher;
    }

    /**
     * Sets the marks alloted by teacher.
     *
     * @param marksAllotedByTeacher the marksAllotedByTeacher to set
     */
    public void setMarksAllotedByTeacher(boolean marksAllotedByTeacher) {
	this.marksAllotedByTeacher = marksAllotedByTeacher;
    }

}