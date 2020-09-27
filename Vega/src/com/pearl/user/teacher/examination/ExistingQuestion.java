/**
 * 
 */
package com.pearl.user.teacher.examination;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class ExistingQuestion.
 *
 * @author Chari
 */
public class ExistingQuestion implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	private String id;
	
	/** The grade. */
	private String grade;
	
	/** The section. */
	private String section;
	
	/** The subject. */
	private String subject;
	
	/** The chapter. */
	private String chapter;
	
	/** The question type. */
	private String questionType;
	
	/** The marks. */
	private int marks;
	
	/** The question. */
	private Question question;
	// bookName Variable is added for finding bookId/BookName with book reference,
	
	/** The book name. */
	private String bookName;
	
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
	 * Gets the chapter.
	 *
	 * @return the chapter
	 */
	public String getChapter() {
		return chapter;
	}
	
	/**
	 * Sets the chapter.
	 *
	 * @param chapter the chapter to set
	 */
	public void setChapter(String chapter) {
		this.chapter = chapter;
	}
	
	/**
	 * Gets the question type.
	 *
	 * @return the questionType
	 */
	public String getQuestionType() {
		return questionType;
	}
	
	/**
	 * Sets the question type.
	 *
	 * @param questionType the questionType to set
	 */
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	
	/**
	 * Gets the marks.
	 *
	 * @return the marks
	 */
	public int getMarks() {
		return marks;
	}
	
	/**
	 * Sets the marks.
	 *
	 * @param marks the marks to set
	 */
	public void setMarks(int marks) {
		this.marks = marks;
	}
	
	/**
	 * Gets the question.
	 *
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}
	
	/**
	 * Sets the question.
	 *
	 * @param question the question to set
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExistingQuestion [id=" + id + ", grade=" + grade + ", section="
				+ section + ", subject=" + subject + ", chapter=" + chapter
				+ ", questionType=" + questionType + ", marks=" + marks
				+ ", question=" + question + ", bookName=" + bookName + "]";
	}
	
	/**
	 * Gets the book name.
	 *
	 * @return the bookName
	 */
	public String getBookName() {
		return bookName;
	}
	
	/**
	 * Sets the book name.
	 *
	 * @param bookName the bookName to set
	 */
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	

}
