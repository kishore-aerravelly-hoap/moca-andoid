package com.pearl.parsers.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.pearl.examination.Answer;
import com.pearl.examination.AnswerChoice;
import com.pearl.examination.Exam;
import com.pearl.examination.ExamDetails;
import com.pearl.examination.Question;
import com.pearl.examination.questiontype.OrderingQuestion;
import com.pearl.examination.questiontype.ShortAnswerQuestion;
import com.pearl.examination.questiontype.TrueOrFalseQuestion;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamParser.
 */
public class ExamParser extends DefaultHandler {
    
    /** The current element. */
    private Boolean currentElement = false;
    
    /** The current value. */
    private String currentValue = "";

    /** The exam. */
    private Exam exam;
    
    /** The allowed books. */
    private ArrayList<Integer> allowedBooks;

    /** The temp question. */
    private Question tempQuestion;
    
    /** The questions. */
    private List<Question> questions;
    
    /** The temp answer choice. */
    private AnswerChoice tempAnswerChoice;
    
    /** The answer choices. */
    private List<AnswerChoice> answerChoices;

    /** The main parent. */
    private String mainParent = null;

    // TODO this is used as flag to skip processing other/related meanings.. if
    // we need to handle that we need to update the logic accordingly
    /** The first meaning extraction completed. */
    private final boolean firstMeaningExtractionCompleted = false;

    /**
     * Gets the exam.
     *
     * @return the exam
     */
    public Exam getExam() {
	return exam;
    }

    /**
     * Called when tag starts ( ex:- <name>Book Name</name> -- <name> ).
     *
     * @param uri the uri
     * @param localName the local name
     * @param qName the q name
     * @param attributes the attributes
     * @throws SAXException the sAX exception
     */
    @Override
    public void startElement(String uri, String localName, String qName,
	    Attributes attributes) throws SAXException {
	currentElement = true;
	currentValue = "";

	// TODO initialization based on tag names for rest of the tags and
	// properties also
	if (localName.equalsIgnoreCase("testpaper")) {
	    if (exam == null) {
		exam = new Exam();
		exam.setExamDetails(new ExamDetails());
	    }

	    mainParent = "exam";
	} else if (localName.equalsIgnoreCase("questions")) {
	    questions = new ArrayList<Question>();
	} else if (localName.equalsIgnoreCase("question")) {
	    Logger.error("ExamParser", "entered QUESTION open tag");

	    // TODO verify
	    tempQuestion = new Question();
	    answerChoices = new ArrayList<AnswerChoice>();

	    mainParent = "question";
	} else if (localName.equalsIgnoreCase("answer")) {
	    tempAnswerChoice = new AnswerChoice();

	    mainParent = "answer";
	} else if (localName.equalsIgnoreCase("books")) {
	    allowedBooks = new ArrayList<Integer>();
	}
    }

    /**
     * Called when tag closing ( ex:- <name>Book Name</name> -- </name> ).
     *
     * @param uri the uri
     * @param localName the local name
     * @param qName the q name
     * @throws SAXException the sAX exception
     */
    @Override
    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	currentElement = false;
	/** set value */
	// TODO update objects and properties by comparing with localName
	if (localName.equalsIgnoreCase("name")) {
	    exam.getExamDetails().setTitle(currentValue);

	    Logger.info("Exam Parser", "Title is " + currentValue);
	} else if (localName.equalsIgnoreCase("id")) {
	    try {
		currentValue = "" + Integer.parseInt(currentValue);
	    } catch (final Exception e) {
		currentValue = "1";
	    }

	    if ("exam".equalsIgnoreCase(mainParent)) {
		exam.getExamDetails().setExamId(currentValue);
	    } else if ("question".equalsIgnoreCase(mainParent)
		    && tempQuestion != null) {
		tempQuestion.setId(currentValue);

		Logger.info("Exam Parser", "Desc is " + currentValue);
	    } else if ("answer".equalsIgnoreCase(mainParent)
		    && tempAnswerChoice != null) {
		tempAnswerChoice.setId(Integer.parseInt(currentValue));
	    }
	} else if (localName.equalsIgnoreCase("description")) {
	    if ("exam".equalsIgnoreCase(mainParent)) {
		exam.getExamDetails().setDescription(currentValue);
	    } else if ("question".equalsIgnoreCase(mainParent)
		    && tempQuestion != null) {
		tempQuestion.setQuestion(currentValue);
	    } else if ("answer".equalsIgnoreCase(mainParent)) {
		tempAnswerChoice.setTitle(currentValue);
	    }
	} else if (localName.equalsIgnoreCase("subject")) {
	    exam.getExamDetails().setSubject(currentValue);

	    Logger.error("Exam Parser", "Subject is " + currentValue);
	} else if (localName.equalsIgnoreCase("starttime")) {
	    if (!"".equals(currentValue)) {
		exam.getExamDetails()
		.setStartTime(Long.parseLong(currentValue));
	    }
	} else if (localName.equalsIgnoreCase("endtime")) {
	    if (!"".equals(currentValue)) {
		exam.getExamDetails().setEndDate(Long.parseLong(currentValue));
	    }
	} else if (localName.equalsIgnoreCase("duration")) {
	    int value = 1;
	    try {
		value = Integer.parseInt(currentValue);
	    } catch (final Exception e) {
		Logger.error("Exam Parser", e);
	    }
	    exam.getExamDetails().setDuration(value);// TODO verify.. by default
	    // setting to
	    // large no stating unlimited
	} else if (localName.equalsIgnoreCase("maxpoints")) {
	    int val = 1;
	    try {
		val = Integer.parseInt(currentValue);
	    } catch (final Exception e) {
		currentValue = "1";
		Logger.error("Exam Parser", e);
	    }
	    exam.getExamDetails().setMaxPoints(val);
	} else if (localName.equalsIgnoreCase("negativepoints")) {
	    if (!"".equalsIgnoreCase(currentValue)) {
		// TODO need to set the marks to deduct for wrong answered
		// question.. currently only boolean value is getting saved
		exam.getExamDetails().setNegativePoints(true);
	    } else {
		exam.getExamDetails().setNegativePoints(false);
	    }
	} else if (localName.equalsIgnoreCase("passpoints")) {
	    int val = 1;

	    try {
		val = Integer.parseInt(currentValue);
		Logger.warn("Exam parser", "passspoints value is:" + val);
	    } catch (final Exception e) {
		Logger.error("Exam parser", e);
	    }
	    exam.getExamDetails().setMinPoints(val);
	} else if (localName.equalsIgnoreCase("books")) {
	    exam.getExamDetails().setAllowedBookIds(allowedBooks);
	} else if (localName.equalsIgnoreCase("book")) {
	    if (!"".equals(currentValue)) {
		Logger.warn("Exam Parser....", "current value is(books):"
			+ currentValue);
		allowedBooks.add(Integer.parseInt(currentValue));
	    }
	} else if (localName.equalsIgnoreCase("type")) {
	    // TODO better to set Type from Questions Constant
	    tempQuestion.setType(currentValue);
	} else if (localName.equalsIgnoreCase("points")) {
	    if ("".equals(currentValue)) {
		currentValue = "1";
	    }

	    tempQuestion.setMarksAlloted(Integer.parseInt(currentValue));
	} else if (localName.equalsIgnoreCase("position")) {
	    try {
		currentValue = "" + Integer.parseInt(currentValue);
	    } catch (final Exception e) {
		currentValue = "1";
	    }

	    if ("question".equalsIgnoreCase(mainParent)
		    && tempQuestion != null) {
		tempQuestion.setQuestionOrderNo(Integer.parseInt(currentValue));
	    } else if ("answer".equalsIgnoreCase(mainParent)
		    && tempAnswerChoice != null) {
		tempAnswerChoice.setPosition(Integer.parseInt(currentValue));
	    }
	} else if (localName.equalsIgnoreCase("selected")) {
	    if (currentValue.equals("true")) {
		tempAnswerChoice.setSelected(true);
	    } else {
		tempAnswerChoice.setSelected(false);
	    }
	} else if (localName.equalsIgnoreCase("timer")) {
	    tempQuestion.setDurationInSecs(Long.parseLong(currentValue));
	} else if (localName.equalsIgnoreCase("answer")) {
	    Logger.warn("Exam Parser", localName + ":" + currentValue);

	    if (Question.SHORT_ANSWER_QUESTION.equals(tempQuestion.getType())) {
		final Answer ans = new Answer();
		ans.setAnswerString(currentValue);

		tempQuestion.setAnswer(ans);
	    } else {
		answerChoices.add(tempAnswerChoice);
	    }
	} else if (localName.equalsIgnoreCase("explanation")) {
	    tempQuestion.setHint(currentValue);

	    if ("question".equalsIgnoreCase(mainParent)
		    && tempQuestion != null) {
		tempQuestion.setHint(currentValue);
	    } else if ("answer".equalsIgnoreCase(mainParent)
		    && tempAnswerChoice != null) {
		tempAnswerChoice.setDescription(currentValue);
	    }
	} else if (localName.equalsIgnoreCase("question")) {
	    // tempQuestion.setChoices(answerChoices);
	    if (Question.SHORT_ANSWER_QUESTION.equals(tempQuestion.getType())) {
		final ShortAnswerQuestion saq = new ShortAnswerQuestion();

		saq.setId(tempQuestion.getId());
		saq.setType(tempQuestion.getType());
		saq.setMarksAlloted(tempQuestion.getMarksAlloted());
		saq.setQuestionOrderNo(tempQuestion.getQuestionOrderNo());
		saq.setDurationInSecs(tempQuestion.getDurationInSecs());
		saq.setDescription(tempQuestion.getDescription());
		saq.setQuestion(tempQuestion.getQuestion());
		saq.setHint(tempQuestion.getHint());
		saq.setAnswer(tempQuestion.getAnswer());

		questions.add(saq);
	    } else if (Question.TRUE_OR_FALSE_QUESTION.equals(tempQuestion
		    .getType())) {
		final TrueOrFalseQuestion tfq = new TrueOrFalseQuestion();

		tfq.setId(tempQuestion.getId());
		tfq.setType(tempQuestion.getType());
		tfq.setMarksAlloted(tempQuestion.getMarksAlloted());
		tfq.setQuestionOrderNo(tempQuestion.getQuestionOrderNo());
		tfq.setDurationInSecs(tempQuestion.getDurationInSecs());
		tfq.setDescription(tempQuestion.getDescription());
		tfq.setQuestion(tempQuestion.getQuestion());
		tfq.setHint(tempQuestion.getHint());

		tfq.setChoices(answerChoices);

		questions.add(tfq);
	    } else if (Question.ORDERING_QUESTION
		    .equals(tempQuestion.getType())) {
		final OrderingQuestion orderingQuestion = new OrderingQuestion();

		orderingQuestion.setId(tempQuestion.getId());
		orderingQuestion.setType(tempQuestion.getType());
		orderingQuestion
		.setMarksAlloted(tempQuestion.getMarksAlloted());
		orderingQuestion.setQuestionOrderNo(tempQuestion
			.getQuestionOrderNo());
		orderingQuestion.setDurationInSecs(tempQuestion
			.getDurationInSecs());
		orderingQuestion.setDescription(tempQuestion.getDescription());
		orderingQuestion.setQuestion(tempQuestion.getQuestion());
		orderingQuestion.setHint(tempQuestion.getHint());

		orderingQuestion.setChoices(answerChoices);

		questions.add(orderingQuestion);
	    } else {
		questions.add(tempQuestion);
	    }
	} else if (localName.equalsIgnoreCase("questions")) {
	    exam.setQuestions(questions);
	}
    }

    /**
     * Called to get tag characters ( ex:- <name>Book Name</name> -- to get
     * 'Book Name' Character ).
     *
     * @param ch the ch
     * @param start the start
     * @param length the length
     * @throws SAXException the sAX exception
     */
    @Override
    public void characters(char[] ch, int start, int length)
	    throws SAXException {
	if (currentElement) {
	    final String str = new String(ch, start, length);
	    currentValue += str;

	    currentValue = currentValue.trim();
	    // currentElement = false;
	}
    }
}