package com.pearl.user.teacher.examination;


// TODO: Auto-generated Javadoc
/**
 * The Class ExamResult.
 */
public class ExamResult {
    
    /** The id. */
    private String id;

    /** The exam. */
    private Exam exam;
    
    /** The marks alloted. */
    private int marksAlloted;

    /** The student id. */
    private String studentId;
    
    /** The correct ans total marks. */
    private double correctAnsTotalMarks;
    
    /** The rank. */
    private int rank;
    
    /** The correct answered count. */
    private int correctAnsweredCount;
    
    /** The wrong answered count. */
    private int wrongAnsweredCount;
    
    /** The un answered count. */
    private int unAnsweredCount;
    
    /** The un seen count. */
    private int unSeenCount;

    /** The total questions. */
    private int totalQuestions;
    
    /** The total marks. */
    private int totalMarks;
    
    /** The wrong ans total marks. */
    private double wrongAnsTotalMarks;
    
    /** The un answer total marks. */
    private double unAnswerTotalMarks;
    
    /** The automated total questions. */
    private int automatedTotalQuestions;
    // This is for manual corrections
    /** The manual correct ans total marks. */
    private double manualCorrectAnsTotalMarks;
    
    /** The manual correct answered count. */
    private int manualCorrectAnsweredCount;
    
    /** The manual wrong answered count. */
    private int manualWrongAnsweredCount;
    
    /** The manual un answered count. */
    private int manualUnAnsweredCount;
    
    /** The manual un seen count. */
    private int manualUnSeenCount;
    
    /** The manual total questions. */
    private int manualTotalQuestions;
    
    /** The manual total marks. */
    private double manualTotalMarks;
    
    /** The manual wrong ans total marks. */
    private double manualWrongAnsTotalMarks;
    
    /** The manual un answer total marks. */
    private double manualUnAnswerTotalMarks;
    
    /** The grand total marks. */
    private double grandTotalMarks;

    /** The teacher review needed. */
    private boolean teacherReviewNeeded;

    /**
     * GETTERS AND SETTERS.
     *
     * @return the exam
     */

    /**
     * @return the exam
     */
    public Exam getExam() {
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
     * @param id the new id
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * Gets the marks alloted.
     *
     * @return the marks alloted
     */
    public int getMarksAlloted() {
	return marksAlloted;
    }

    /**
     * Sets the marks alloted.
     *
     * @param marksAlloted the new marks alloted
     */
    public void setMarksAlloted(int marksAlloted) {
	this.marksAlloted = marksAlloted;
    }

    /**
     * Gets the student id.
     *
     * @return the student id
     */
    public String getStudentId() {
	return studentId;
    }

    /**
     * Sets the student id.
     *
     * @param studentId the new student id
     */
    public void setStudentId(String studentId) {
	this.studentId = studentId;
    }

    /**
     * Gets the correct ans total marks.
     *
     * @return the correct ans total marks
     */
    public double getCorrectAnsTotalMarks() {
	return correctAnsTotalMarks;
    }

    /**
     * Sets the correct ans total marks.
     *
     * @param correctAnsTotalMarks the new correct ans total marks
     */
    public void setCorrectAnsTotalMarks(double correctAnsTotalMarks) {
	this.correctAnsTotalMarks = correctAnsTotalMarks;
    }

    /**
     * Gets the rank.
     *
     * @return the rank
     */
    public int getRank() {
	return rank;
    }

    /**
     * Sets the rank.
     *
     * @param rank the new rank
     */
    public void setRank(int rank) {
	this.rank = rank;
    }

    /**
     * Gets the correct answered count.
     *
     * @return the correct answered count
     */
    public int getCorrectAnsweredCount() {
	return correctAnsweredCount;
    }

    /**
     * Sets the correct answered count.
     *
     * @param correctAnsweredCount the new correct answered count
     */
    public void setCorrectAnsweredCount(int correctAnsweredCount) {
	this.correctAnsweredCount = correctAnsweredCount;
    }

    /**
     * Gets the wrong answered count.
     *
     * @return the wrong answered count
     */
    public int getWrongAnsweredCount() {
	return wrongAnsweredCount;
    }

    /**
     * Sets the wrong answered count.
     *
     * @param wrongAnsweredCount the new wrong answered count
     */
    public void setWrongAnsweredCount(int wrongAnsweredCount) {
	this.wrongAnsweredCount = wrongAnsweredCount;
    }

    /**
     * Gets the un answered count.
     *
     * @return the un answered count
     */
    public int getUnAnsweredCount() {
	return unAnsweredCount;
    }

    /**
     * Sets the un answered count.
     *
     * @param unAnsweredCount the new un answered count
     */
    public void setUnAnsweredCount(int unAnsweredCount) {
	this.unAnsweredCount = unAnsweredCount;
    }

    /**
     * Gets the un seen count.
     *
     * @return the un seen count
     */
    public int getUnSeenCount() {
	return unSeenCount;
    }

    /**
     * Sets the un seen count.
     *
     * @param unSeenCount the new un seen count
     */
    public void setUnSeenCount(int unSeenCount) {
	this.unSeenCount = unSeenCount;
    }

    /**
     * Gets the total questions.
     *
     * @return the total questions
     */
    public int getTotalQuestions() {
	return totalQuestions;
    }

    /**
     * Sets the total questions.
     *
     * @param totalQuestions the new total questions
     */
    public void setTotalQuestions(int totalQuestions) {
	this.totalQuestions = totalQuestions;
    }

    /**
     * Gets the total marks.
     *
     * @return the total marks
     */
    public int getTotalMarks() {
	return totalMarks;
    }

    /**
     * Sets the total marks.
     *
     * @param totalMarks the new total marks
     */
    public void setTotalMarks(int totalMarks) {
	this.totalMarks = totalMarks;
    }

    /**
     * Gets the wrong ans total marks.
     *
     * @return the wrong ans total marks
     */
    public double getWrongAnsTotalMarks() {
	return wrongAnsTotalMarks;
    }

    /**
     * Sets the wrong ans total marks.
     *
     * @param wrongAnsTotalMarks the new wrong ans total marks
     */
    public void setWrongAnsTotalMarks(double wrongAnsTotalMarks) {
	this.wrongAnsTotalMarks = wrongAnsTotalMarks;
    }

    /**
     * Gets the un answer total marks.
     *
     * @return the un answer total marks
     */
    public double getUnAnswerTotalMarks() {
	return unAnswerTotalMarks;
    }

    /**
     * Sets the un answer total marks.
     *
     * @param unAnswerTotalMarks the new un answer total marks
     */
    public void setUnAnswerTotalMarks(double unAnswerTotalMarks) {
	this.unAnswerTotalMarks = unAnswerTotalMarks;
    }

    /**
     * Gets the automated total questions.
     *
     * @return the automated total questions
     */
    public int getAutomatedTotalQuestions() {
	return automatedTotalQuestions;
    }

    /**
     * Sets the automated total questions.
     *
     * @param automatedTotalQuestions the new automated total questions
     */
    public void setAutomatedTotalQuestions(int automatedTotalQuestions) {
	this.automatedTotalQuestions = automatedTotalQuestions;
    }

    /**
     * Gets the manual correct ans total marks.
     *
     * @return the manual correct ans total marks
     */
    public double getManualCorrectAnsTotalMarks() {
	return manualCorrectAnsTotalMarks;
    }

    /**
     * Sets the manual correct ans total marks.
     *
     * @param manualCorrectAnsTotalMarks the new manual correct ans total marks
     */
    public void setManualCorrectAnsTotalMarks(double manualCorrectAnsTotalMarks) {
	this.manualCorrectAnsTotalMarks = manualCorrectAnsTotalMarks;
    }

    /**
     * Gets the manual correct answered count.
     *
     * @return the manual correct answered count
     */
    public int getManualCorrectAnsweredCount() {
	return manualCorrectAnsweredCount;
    }

    /**
     * Sets the manual correct answered count.
     *
     * @param manualCorrectAnsweredCount the new manual correct answered count
     */
    public void setManualCorrectAnsweredCount(int manualCorrectAnsweredCount) {
	this.manualCorrectAnsweredCount = manualCorrectAnsweredCount;
    }

    /**
     * Gets the manual wrong answered count.
     *
     * @return the manual wrong answered count
     */
    public int getManualWrongAnsweredCount() {
	return manualWrongAnsweredCount;
    }

    /**
     * Sets the manual wrong answered count.
     *
     * @param manualWrongAnsweredCount the new manual wrong answered count
     */
    public void setManualWrongAnsweredCount(int manualWrongAnsweredCount) {
	this.manualWrongAnsweredCount = manualWrongAnsweredCount;
    }

    /**
     * Gets the manual un answered count.
     *
     * @return the manual un answered count
     */
    public int getManualUnAnsweredCount() {
	return manualUnAnsweredCount;
    }

    /**
     * Sets the manual un answered count.
     *
     * @param manualUnAnsweredCount the new manual un answered count
     */
    public void setManualUnAnsweredCount(int manualUnAnsweredCount) {
	this.manualUnAnsweredCount = manualUnAnsweredCount;
    }

    /**
     * Gets the manual un seen count.
     *
     * @return the manual un seen count
     */
    public int getManualUnSeenCount() {
	return manualUnSeenCount;
    }

    /**
     * Sets the manual un seen count.
     *
     * @param manualUnSeenCount the new manual un seen count
     */
    public void setManualUnSeenCount(int manualUnSeenCount) {
	this.manualUnSeenCount = manualUnSeenCount;
    }

    /**
     * Gets the manual total questions.
     *
     * @return the manual total questions
     */
    public int getManualTotalQuestions() {
	return manualTotalQuestions;
    }

    /**
     * Sets the manual total questions.
     *
     * @param manualTotalQuestions the new manual total questions
     */
    public void setManualTotalQuestions(int manualTotalQuestions) {
	this.manualTotalQuestions = manualTotalQuestions;
    }

    /**
     * Gets the manual total marks.
     *
     * @return the manual total marks
     */
    public double getManualTotalMarks() {
	return manualTotalMarks;
    }

    /**
     * Sets the manual total marks.
     *
     * @param manualTotalMarks the new manual total marks
     */
    public void setManualTotalMarks(double manualTotalMarks) {
	this.manualTotalMarks = manualTotalMarks;
    }

    /**
     * Gets the manual wrong ans total marks.
     *
     * @return the manual wrong ans total marks
     */
    public double getManualWrongAnsTotalMarks() {
	return manualWrongAnsTotalMarks;
    }

    /**
     * Sets the manual wrong ans total marks.
     *
     * @param manualWrongAnsTotalMarks the new manual wrong ans total marks
     */
    public void setManualWrongAnsTotalMarks(double manualWrongAnsTotalMarks) {
	this.manualWrongAnsTotalMarks = manualWrongAnsTotalMarks;
    }

    /**
     * Gets the manual un answer total marks.
     *
     * @return the manual un answer total marks
     */
    public double getManualUnAnswerTotalMarks() {
	return manualUnAnswerTotalMarks;
    }

    /**
     * Sets the manual un answer total marks.
     *
     * @param manualUnAnswerTotalMarks the new manual un answer total marks
     */
    public void setManualUnAnswerTotalMarks(double manualUnAnswerTotalMarks) {
	this.manualUnAnswerTotalMarks = manualUnAnswerTotalMarks;
    }

    /**
     * Gets the grand total marks.
     *
     * @return the grand total marks
     */
    public double getGrandTotalMarks() {
	return grandTotalMarks;
    }

    /**
     * Sets the grand total marks.
     *
     * @param grandTotalMarks the new grand total marks
     */
    public void setGrandTotalMarks(double grandTotalMarks) {
	this.grandTotalMarks = grandTotalMarks;
    }

    /**
     * Checks if is teacher review needed.
     *
     * @return true, if is teacher review needed
     */
    public boolean isTeacherReviewNeeded() {
	return teacherReviewNeeded;
    }

    /**
     * Sets the teacher review needed.
     *
     * @param teacherReviewNeeded the new teacher review needed
     */
    public void setTeacherReviewNeeded(boolean teacherReviewNeeded) {
	this.teacherReviewNeeded = teacherReviewNeeded;
    }

    /**
     * Sets the exam.
     *
     * @param exam the new exam
     */
    public void setExam(Exam exam) {
	this.exam = exam;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ExamResult [id=" + id + ", exam=" + exam + ", marksAlloted="
		+ marksAlloted + ", studentId=" + studentId
		+ ", correctAnsTotalMarks=" + correctAnsTotalMarks + ", rank="
		+ rank + ", correctAnsweredCount=" + correctAnsweredCount
		+ ", wrongAnsweredCount=" + wrongAnsweredCount
		+ ", unAnsweredCount=" + unAnsweredCount + ", unSeenCount="
		+ unSeenCount + ", totalQuestions=" + totalQuestions
		+ ", totalMarks=" + totalMarks + ", wrongAnsTotalMarks="
		+ wrongAnsTotalMarks + ", unAnswerTotalMarks="
		+ unAnswerTotalMarks + ", automatedTotalQuestions="
		+ automatedTotalQuestions + ", manualCorrectAnsTotalMarks="
		+ manualCorrectAnsTotalMarks + ", manualCorrectAnsweredCount="
		+ manualCorrectAnsweredCount + ", manualWrongAnsweredCount="
		+ manualWrongAnsweredCount + ", manualUnAnsweredCount="
		+ manualUnAnsweredCount + ", manualUnSeenCount="
		+ manualUnSeenCount + ", manualTotalQuestions="
		+ manualTotalQuestions + ", manualTotalMarks="
		+ manualTotalMarks + ", manualWrongAnsTotalMarks="
		+ manualWrongAnsTotalMarks + ", manualUnAnswerTotalMarks="
		+ manualUnAnswerTotalMarks + ", grandTotalMarks="
		+ grandTotalMarks + ", teacherReviewNeeded="
		+ teacherReviewNeeded + "]";
    }

}