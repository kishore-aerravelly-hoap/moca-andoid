package  com.pearl.user.teacher.examination.answertype;

import java.io.Serializable;

import com.pearl.user.teacher.examination.AnswerChoice;


// TODO: Auto-generated Javadoc
/**
 * The Class OrderingAnswerChoice.
 */
public class OrderingAnswerChoice extends AnswerChoice implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The teacher position. */
    protected int teacherPosition;
    
    /** The match_ field one. */
    protected String match_FieldOne;
    
    /** The match_ field three. */
    protected String match_FieldThree;
    
    /** The match_ field four. */
    protected String match_FieldFour;

    /**
     * Gets the teacher position.
     *
     * @return the teacherPosition
     */
    @Override
    public int getTeacherPosition() {
	return teacherPosition;
    }

    /**
     * Sets the teacher position.
     *
     * @param teacherPosition the teacherPosition to set
     */
    @Override
    public void setTeacherPosition(int teacherPosition) {
	this.teacherPosition = teacherPosition;
    }

    /**
     * Gets the match_ field one.
     *
     * @return the match_FieldOne
     */
    @Override
    public String getMatch_FieldOne() {
	return match_FieldOne;
    }

    /**
     * Sets the match_ field one.
     *
     * @param match_FieldOne the match_FieldOne to set
     */
    @Override
    public void setMatch_FieldOne(String match_FieldOne) {
	this.match_FieldOne = match_FieldOne;
    }

    /**
     * Gets the match_ field three.
     *
     * @return the match_FieldThree
     */
    @Override
    public String getMatch_FieldThree() {
	return match_FieldThree;
    }

    /**
     * Sets the match_ field three.
     *
     * @param match_FieldThree the match_FieldThree to set
     */
    @Override
    public void setMatch_FieldThree(String match_FieldThree) {
	this.match_FieldThree = match_FieldThree;
    }

    /**
     * Gets the match_ field four.
     *
     * @return the match_FieldFour
     */
    @Override
    public String getMatch_FieldFour() {
	return match_FieldFour;
    }

    /**
     * Sets the match_ field four.
     *
     * @param match_FieldFour the match_FieldFour to set
     */
    @Override
    public void setMatch_FieldFour(String match_FieldFour) {
	this.match_FieldFour = match_FieldFour;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "OrderingAnswerChoice [teacherPosition=" + teacherPosition
		+ ", match_FieldOne=" + match_FieldOne + ", match_FieldThree="
		+ match_FieldThree + ", match_FieldFour=" + match_FieldFour
		+ "]";
    }
}