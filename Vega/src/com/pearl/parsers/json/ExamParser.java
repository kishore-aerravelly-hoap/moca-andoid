package com.pearl.parsers.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.pearl.examination.Exam;
import com.pearl.examination.Question;
import com.pearl.examination.questiontype.FillInTheBlankQuestion;
import com.pearl.examination.questiontype.LongAnswerQuestion;
import com.pearl.examination.questiontype.MultipleChoiceQuestion;
import com.pearl.examination.questiontype.OrderingQuestion;
import com.pearl.examination.questiontype.ShortAnswerQuestion;
import com.pearl.examination.questiontype.TrueOrFalseQuestion;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamParser.
 */
public class ExamParser {
    
    /** The Constant FAIL_LIMIT. */
    public static final int FAIL_LIMIT = 5000;
    
    /** The tag. */
    private static String tag = "ExamParser";

    /** The retakeable. */
    boolean retakeable;
    
    /**
     * Gets the exam.
     *
     * @param content the content
     * @return the exam
     * @throws Exception the exception
     */
    public static Exam getExam(String content) throws Exception {
	return getExamFromString(content);
    }

    /**
     * Gets the exam.
     *
     * @param file the file
     * @return the exam
     * @throws Exception the exception
     */
    public static Exam getExam(File file) throws Exception {
	final StringBuilder contents = new StringBuilder();

	final BufferedReader input = new BufferedReader(new FileReader(file));
	String line = null; // not declared within while loop
	while ((line = input.readLine()) != null) {
	    contents.append(line);
	    contents.append(System.getProperty("line.separator"));
	}
	input.close();

	return getExamFromString(contents.toString());
    }

    /**
     * Gets the exam from string.
     *
     * @param content the content
     * @return the exam from string
     * @throws Exception the exception
     */
    public static Exam getExamFromString(String content) throws Exception {
	Logger.warn("ExamParser", "PARSER - json content is:" + content);
	final Gson gson = new Gson();

	Exam exam = null;
	try {
	    final ArrayList<Question> questionsList = new ArrayList<Question>();
	    final JsonReader reader = new JsonReader(new StringReader(content));
	    reader.setLenient(true);
	    exam = gson.fromJson(reader, Exam.class);

	    Logger.warn(tag, "Exam Parser - parsed successfully");
	    if (exam != null && exam.getQuestions() != null) {
		for (int i = 0; i < exam.getQuestions().size(); i++) {
		    if (exam != null
			    && exam.getQuestions() != null
			    && exam.getQuestions().get(i).getType()
			    .equals(Question.MULTIPLECHOICE_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ exam.getQuestions().get(i).getType());
			final MultipleChoiceQuestion tfq = new MultipleChoiceQuestion();

			tfq.setId(exam.getQuestions().get(i).getId());
			tfq.setType(exam.getQuestions().get(i).getType());
			tfq.setMarksAlloted(exam.getQuestions().get(i)
				.getMarksAlloted());
			tfq.setQuestionOrderNo(exam.getQuestions().get(i)
				.getQuestionOrderNo());
			tfq.setDurationInSecs(exam.getQuestions().get(i)
				.getDurationInSecs());
			tfq.setDescription(exam.getQuestions().get(i)
				.getDescription());
			tfq.setQuestion(exam.getQuestions().get(i)
				.getQuestion());
			tfq.setHint(exam.getQuestions().get(i).getHint());
			tfq.setIsAnswered(exam.getQuestions().get(i)
				.isAnswered());
			tfq.setMarked(exam.getQuestions().get(i).isMarked());
			tfq.setViewed(exam.getQuestions().get(i).isViewed());

			for (int j = 0; j < exam.getQuestions().get(i)
				.getChoices().size(); j++) {
			    Logger.info(tag,
				    "EXAMPARSER - MULTI - choices before setting is:"
					    + exam.getQuestions().get(i)
					    .getChoices().get(j)
					    .isTeacherSelected());
			}
			tfq.setChoices(exam.getQuestions().get(i).getChoices());

			questionsList.add(tfq);
		    } else if (exam != null
			    && exam.getQuestions() != null
			    && exam.getQuestions().get(i).getType()
			    .equals(Question.TRUE_OR_FALSE_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ exam.getQuestions().get(i).getType());
			final TrueOrFalseQuestion tfq = new TrueOrFalseQuestion();

			tfq.setId(exam.getQuestions().get(i).getId());
			tfq.setType(exam.getQuestions().get(i).getType());
			tfq.setMarksAlloted(exam.getQuestions().get(i)
				.getMarksAlloted());
			tfq.setQuestionOrderNo(exam.getQuestions().get(i)
				.getQuestionOrderNo());
			tfq.setDurationInSecs(exam.getQuestions().get(i)
				.getDurationInSecs());
			tfq.setDescription(exam.getQuestions().get(i)
				.getDescription());
			tfq.setQuestion(exam.getQuestions().get(i)
				.getQuestion());
			tfq.setHint(exam.getQuestions().get(i).getHint());

			tfq.setChoices(exam.getQuestions().get(i).getChoices());
			tfq.setIsAnswered(exam.getQuestions().get(i)
				.isAnswered());
			tfq.setMarked(exam.getQuestions().get(i).isMarked());
			tfq.setViewed(exam.getQuestions().get(i).isViewed());

			questionsList.add(tfq);
		    } else if (exam != null
			    && exam.getQuestions() != null
			    && exam.getQuestions().get(i).getType()
			    .equals(Question.ORDERING_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ exam.getQuestions().get(i).getType());
			final OrderingQuestion oq = new OrderingQuestion();

			oq.setId(exam.getQuestions().get(i).getId());
			oq.setType(exam.getQuestions().get(i).getType());
			oq.setMarksAlloted(exam.getQuestions().get(i)
				.getMarksAlloted());
			oq.setQuestionOrderNo(exam.getQuestions().get(i)
				.getQuestionOrderNo());
			oq.setDurationInSecs(exam.getQuestions().get(i)
				.getDurationInSecs());
			oq.setDescription(exam.getQuestions().get(i)
				.getDescription());
			oq.setQuestion(exam.getQuestions().get(i).getQuestion());
			oq.setHint(exam.getQuestions().get(i).getHint());

			oq.setChoices(exam.getQuestions().get(i).getChoices());
			oq.setIsAnswered(exam.getQuestions().get(i)
				.isAnswered());
			oq.setMarked(exam.getQuestions().get(i).isMarked());
			oq.setViewed(exam.getQuestions().get(i).isViewed());

			questionsList.add(oq);
		    } else if (exam != null
			    && exam.getQuestions() != null
			    && exam.getQuestions().get(i).getType()
			    .equals(Question.SHORT_ANSWER_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ exam.getQuestions().get(i).getType());
			final ShortAnswerQuestion saq = new ShortAnswerQuestion();

			saq.setId(exam.getQuestions().get(i).getId());
			saq.setType(exam.getQuestions().get(i).getType());
			saq.setMarksAlloted(exam.getQuestions().get(i)
				.getMarksAlloted());
			saq.setQuestionOrderNo(exam.getQuestions().get(i)
				.getQuestionOrderNo());
			saq.setDurationInSecs(exam.getQuestions().get(i)
				.getDurationInSecs());
			saq.setDescription(exam.getQuestions().get(i)
				.getDescription());
			saq.setQuestion(exam.getQuestions().get(i)
				.getQuestion());
			saq.setHint(exam.getQuestions().get(i).getHint());
			saq.setAnswer(exam.getQuestions().get(i).getAnswer());
			saq.setIsAnswered(exam.getQuestions().get(i)
				.isAnswered());
			saq.setMarked(exam.getQuestions().get(i).isMarked());
			saq.setViewed(exam.getQuestions().get(i).isViewed());
			saq.setCorrectAnswer(exam.getQuestions().get(i).getCorrectAnswer());
			questionsList.add(saq);
		    } else if (exam != null
			    && exam.getQuestions() != null
			    && exam.getQuestions().get(i).getType()
			    .equals(Question.LONG_ANSWER_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ exam.getQuestions().get(i).getType());
			final LongAnswerQuestion saq = new LongAnswerQuestion();

			saq.setId(exam.getQuestions().get(i).getId());
			saq.setType(exam.getQuestions().get(i).getType());
			saq.setMarksAlloted(exam.getQuestions().get(i)
				.getMarksAlloted());
			saq.setQuestionOrderNo(exam.getQuestions().get(i)
				.getQuestionOrderNo());
			saq.setDurationInSecs(exam.getQuestions().get(i)
				.getDurationInSecs());
			saq.setDescription(exam.getQuestions().get(i)
				.getDescription());
			saq.setQuestion(exam.getQuestions().get(i)
				.getQuestion());
			saq.setHint(exam.getQuestions().get(i).getHint());
			saq.setAnswer(exam.getQuestions().get(i).getAnswer());
			saq.setIsAnswered(exam.getQuestions().get(i)
				.isAnswered());
			saq.setMarked(exam.getQuestions().get(i).isMarked());
			saq.setViewed(exam.getQuestions().get(i).isViewed());
			saq.setCorrectAnswer(exam.getQuestions().get(i).getCorrectAnswer());
			questionsList.add(saq);
		    } else if (exam != null
			    && exam.getQuestions() != null
			    && exam.getQuestions()
			    .get(i)
			    .getType()
			    .equals(Question.FILL_IN_THE_BLANK_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ exam.getQuestions().get(i).getType());
			Logger.warn(tag,
				"Double checking, we are in the FBQ condition");
			final FillInTheBlankQuestion fbq = new FillInTheBlankQuestion();

			Logger.warn(tag,
				"created fill in the blank question object");
			Logger.warn(tag, "QUESTION ID"
				+ exam.getQuestions().get(i).getId());
			Logger.warn(tag, "Type "
				+ exam.getQuestions().get(i).getType());
			Logger.warn(tag, "Marks allocated"
				+ exam.getQuestions().get(i).getMarksAlloted());
			Logger.warn(tag, "Question order no "
				+ exam.getQuestions().get(i)
				.getQuestionOrderNo());
			Logger.warn(tag,
				"Duration "
					+ exam.getQuestions().get(i)
					.getDurationInSecs());
			Logger.warn(tag, "Descrition "
				+ exam.getQuestions().get(i).getDescription());
			Logger.warn(tag,
				"Question "
					+ exam.getQuestions().get(i)
					.getQuestion());
			Logger.warn(tag, "Hint "
				+ exam.getQuestions().get(i).getHint());
			Logger.warn(tag, "Answer "
				+ exam.getQuestions().get(i).getAnswer());

			fbq.setId(exam.getQuestions().get(i).getId());
			fbq.setType(exam.getQuestions().get(i).getType());
			fbq.setMarksAlloted(exam.getQuestions().get(i)
				.getMarksAlloted());
			fbq.setQuestionOrderNo(exam.getQuestions().get(i)
				.getQuestionOrderNo());
			fbq.setDurationInSecs(exam.getQuestions().get(i)
				.getDurationInSecs());
			fbq.setDescription(exam.getQuestions().get(i)
				.getDescription());
			fbq.setQuestion(exam.getQuestions().get(i)
				.getQuestion());
			fbq.setHint(exam.getQuestions().get(i).getHint());
			fbq.setAnswer(exam.getQuestions().get(i).getAnswer());
			fbq.setIsAnswered(exam.getQuestions().get(i)
				.isAnswered());
			fbq.setMarked(exam.getQuestions().get(i).isMarked());
			fbq.setViewed(exam.getQuestions().get(i).isViewed());
			fbq.setCorrectAnswer(exam.getQuestions().get(i).getCorrectAnswer());
			Logger.warn(tag,
				"populating FBQ object with question data");
			questionsList.add(fbq);
			Logger.warn(tag,
				"Fill in the blank question added to the list");
		    } else if (exam != null
			    && exam.getQuestions() != null
			    && exam.getQuestions().get(i).getType()
			    .equals(Question.MATCH_THE_FOLLOWING)) {
			Logger.warn(tag, "question type is:"
				+ exam.getQuestions().get(i).getType());
			final MultipleChoiceQuestion mfq = new MultipleChoiceQuestion();
			mfq.setId(exam.getQuestions().get(i).getId());
			mfq.setType(exam.getQuestions().get(i).getType());
			mfq.setMarksAlloted(exam.getQuestions().get(i)
				.getMarksAlloted());
			mfq.setQuestionOrderNo(exam.getQuestions().get(i)
				.getQuestionOrderNo());
			mfq.setDurationInSecs(exam.getQuestions().get(i)
				.getDurationInSecs());
			mfq.setDescription(exam.getQuestions().get(i)
				.getDescription());
			mfq.setQuestion(exam.getQuestions().get(i)
				.getQuestion());
			mfq.setHint(exam.getQuestions().get(i).getHint());
			mfq.setAnswer(exam.getQuestions().get(i).getAnswer());

			Logger.warn(tag,
				"populating MFQ object with question data & choices are");
			mfq.setChoices(exam.getQuestions().get(i).getChoices());
			mfq.setIsAnswered(exam.getQuestions().get(i)
				.isAnswered());
			mfq.setMarked(exam.getQuestions().get(i).isMarked());
			mfq.setViewed(exam.getQuestions().get(i).isViewed());
			Logger.warn(tag,
				"populating MFQ object with question data");
			questionsList.add(mfq);
			Logger.warn(tag, "mfq question added to the list");

		    }

		    else {
			Logger.warn(tag,
				"PARSER - else case adding question to list");
		    }
		}
		exam.setQuestions(questionsList);
	    } else {
		Logger.warn("EXAM PARSER", "EXAM PARSER - exam is null");
	    }

	} catch (final Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return exam;

    }

    /**
     * Checks if is retakeable.
     *
     * @return the retakeable
     */
    public boolean isRetakeable() {
	return retakeable;
    }

    /**
     * Sets the retakeable.
     *
     * @param retakeable the retakeable to set
     */
    public void setRetakeable(boolean retakeable) {
	this.retakeable = retakeable;
    }
}
