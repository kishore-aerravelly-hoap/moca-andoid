package com.pearl.parsers.json;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.pearl.examination.Answer;
import com.pearl.examination.ExamResult;
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
 * The Class ExamResultParser.
 */
public class ExamResultParser {

    /** The Constant tag. */
    private static final String tag = "ExamResultParser";

    /**
     * Gets the exam result.
     *
     * @param content the content
     * @return the exam result
     */
    public static ExamResult getExamResult(String content) {
	final Gson gson = new Gson();
	ExamResult examResult = null;

	Logger.info("ExamResultParser", "result PARSER - json content is:"
		+ content);
	try {
	    final ArrayList<Question> questionsList = new ArrayList<Question>();
	    examResult = gson.fromJson(content, ExamResult.class);
	    if (examResult.getExam() != null
		    && examResult.getExam().getQuestions() != null) {
		for (int i = 0; i < examResult.getExam().getQuestions().size(); i++) {
		    if (examResult != null
			    && examResult.getExam().getQuestions() != null
			    && examResult.getExam().getQuestions().get(i)
			    .getType()
			    .equals(Question.MULTIPLECHOICE_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ examResult.getExam().getQuestions().get(i)
				.getType());
			final MultipleChoiceQuestion tfq = new MultipleChoiceQuestion();

			tfq.setId(examResult.getExam().getQuestions().get(i)
				.getId());
			tfq.setType(examResult.getExam().getQuestions().get(i)
				.getType());
			tfq.setMarksAlloted(examResult.getExam().getQuestions()
				.get(i).getMarksAlloted());
			tfq.setMarksAwarded(examResult.getExam().getQuestions()
				.get(i).getMarksAwarded());
			tfq.setQuestionOrderNo(examResult.getExam()
				.getQuestions().get(i).getQuestionOrderNo());
			tfq.setDurationInSecs(examResult.getExam()
				.getQuestions().get(i).getDurationInSecs());
			tfq.setDescription(examResult.getExam().getQuestions()
				.get(i).getDescription());
			tfq.setQuestion(examResult.getExam().getQuestions()
				.get(i).getQuestion());
			tfq.setHint(examResult.getExam().getQuestions().get(i)
				.getHint());
			tfq.setCorrectAnswer(examResult.getExam()
				.getQuestions().get(i).getCorrectAnswer());
			for (int j = 0; j < examResult.getExam().getQuestions()
				.get(i).getChoices().size(); j++) {
			    Logger.info(tag,
				    "RESULT - MTF - choices before setting is:"
					    + examResult.getExam()
					    .getQuestions().get(i)
					    .getChoices().get(j)
					    .isSelected());
			}
			Answer studentAnswer = new Answer();
			studentAnswer.setAnswerString("");
			studentAnswer.setChoices(examResult.getExam().getQuestions().get(i).getChoices());
			//com.pearl.examination.Answer
			/*Answer studentAnswer = new Answer();
			studentAnswer.setAnswerString("");
			studentAnswer.setChoices(examResult.getExam().getQuestions().get(i).getChoices());*/
//			tfq.setStudentAnswer(studentAnswer);
			tfq.setStudentAnswer(studentAnswer);
			tfq.setChoices(examResult.getExam().getQuestions()
				.get(i).getChoices());

			questionsList.add(tfq);
		    } else if (examResult != null
			    && examResult.getExam().getQuestions() != null
			    && examResult.getExam().getQuestions().get(i)
			    .getType()
			    .equals(Question.TRUE_OR_FALSE_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ examResult.getExam().getQuestions().get(i)
				.getType());
			final TrueOrFalseQuestion tfq = new TrueOrFalseQuestion();

			tfq.setId(examResult.getExam().getQuestions().get(i)
				.getId());
			tfq.setType(examResult.getExam().getQuestions().get(i)
				.getType());
			tfq.setMarksAlloted(examResult.getExam().getQuestions()
				.get(i).getMarksAlloted());
			tfq.setMarksAwarded(examResult.getExam().getQuestions()
				.get(i).getMarksAwarded());
			tfq.setQuestionOrderNo(examResult.getExam()
				.getQuestions().get(i).getQuestionOrderNo());
			tfq.setDurationInSecs(examResult.getExam()
				.getQuestions().get(i).getDurationInSecs());
			tfq.setDescription(examResult.getExam().getQuestions()
				.get(i).getDescription());
			tfq.setQuestion(examResult.getExam().getQuestions()
				.get(i).getQuestion());
			tfq.setHint(examResult.getExam().getQuestions().get(i)
				.getHint());
			tfq.setCorrectAnswer(examResult.getExam()
				.getQuestions().get(i).getCorrectAnswer());

			for (int j = 0; j < examResult.getExam().getQuestions()
				.get(i).getChoices().size(); j++) {
			    Logger.info(tag, "choices before setting is:"
				    + examResult.getExam().getQuestions()
				    .get(i).getChoices().get(j)
				    .isSelected());
			}
			tfq.setChoices(examResult.getExam().getQuestions()
				.get(i).getChoices());

			questionsList.add(tfq);
		    } else if (examResult != null
			    && examResult.getExam().getQuestions() != null
			    && examResult.getExam().getQuestions().get(i)
			    .getType()
			    .equals(Question.ORDERING_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ examResult.getExam().getQuestions().get(i)
				.getType());
			final OrderingQuestion oq = new OrderingQuestion();

			oq.setId(examResult.getExam().getQuestions().get(i)
				.getId());
			oq.setType(examResult.getExam().getQuestions().get(i)
				.getType());
			oq.setMarksAlloted(examResult.getExam().getQuestions()
				.get(i).getMarksAlloted());
			oq.setMarksAwarded(examResult.getExam().getQuestions()
				.get(i).getMarksAwarded());
			oq.setQuestionOrderNo(examResult.getExam()
				.getQuestions().get(i).getQuestionOrderNo());
			oq.setDurationInSecs(examResult.getExam()
				.getQuestions().get(i).getDurationInSecs());
			oq.setDescription(examResult.getExam().getQuestions()
				.get(i).getDescription());
			oq.setQuestion(examResult.getExam().getQuestions()
				.get(i).getQuestion());
			oq.setHint(examResult.getExam().getQuestions().get(i)
				.getHint());
			oq.setCorrectAnswer(examResult.getExam().getQuestions()
				.get(i).getCorrectAnswer());
			oq.setChoices(examResult.getExam().getQuestions()
				.get(i).getChoices());
			oq.setStudentAnswer(examResult.getExam().getQuestions()
				.get(i).getStudentAnswer());
			Logger.warn("exam result parser - ",
				"Answered flag getIsAnswered- "
					+ examResult.getExam().getQuestions()
					.get(i).getIsAnswered());
			;
			Logger.warn("exam result parser - ",
				"Answered flag isAnswered- "
					+ examResult.getExam().getQuestions()
					.get(i).isAnswered());
			;
			oq.setIsAnswered(examResult.getExam().getQuestions()
				.get(i).getIsAnswered());

			questionsList.add(oq);
		    } else if (examResult != null
			    && examResult.getExam().getQuestions() != null
			    && examResult.getExam().getQuestions().get(i)
			    .getType()
			    .equals(Question.SHORT_ANSWER_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ examResult.getExam().getQuestions().get(i)
				.getType());
			final ShortAnswerQuestion saq = new ShortAnswerQuestion();

			saq.setId(examResult.getExam().getQuestions().get(i)
				.getId());
			saq.setType(examResult.getExam().getQuestions().get(i)
				.getType());
			saq.setMarksAlloted(examResult.getExam().getQuestions()
				.get(i).getMarksAlloted());
			saq.setMarksAwarded(examResult.getExam().getQuestions()
				.get(i).getMarksAwarded());
			saq.setQuestionOrderNo(examResult.getExam()
				.getQuestions().get(i).getQuestionOrderNo());
			saq.setDurationInSecs(examResult.getExam()
				.getQuestions().get(i).getDurationInSecs());
			saq.setDescription(examResult.getExam().getQuestions()
				.get(i).getDescription());
			saq.setQuestion(examResult.getExam().getQuestions()
				.get(i).getQuestion());
			saq.setHint(examResult.getExam().getQuestions().get(i)
				.getHint());
			saq.setAnswer(examResult.getExam().getQuestions()
				.get(i).getAnswer());
			saq.setCorrectAnswer(examResult.getExam()
				.getQuestions().get(i).getCorrectAnswer());
			saq.setStudentAnswer(examResult.getExam()
				.getQuestions().get(i).getStudentAnswer());

			questionsList.add(saq);
		    } else if (examResult != null
			    && examResult.getExam().getQuestions() != null
			    && examResult.getExam().getQuestions().get(i)
			    .getType()
			    .equals(Question.LONG_ANSWER_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ examResult.getExam().getQuestions().get(i)
				.getType());
			final LongAnswerQuestion saq = new LongAnswerQuestion();

			saq.setId(examResult.getExam().getQuestions().get(i)
				.getId());
			saq.setType(examResult.getExam().getQuestions().get(i)
				.getType());
			saq.setMarksAlloted(examResult.getExam().getQuestions()
				.get(i).getMarksAlloted());
			saq.setMarksAwarded(examResult.getExam().getQuestions()
				.get(i).getMarksAwarded());
			saq.setQuestionOrderNo(examResult.getExam()
				.getQuestions().get(i).getQuestionOrderNo());
			saq.setDurationInSecs(examResult.getExam()
				.getQuestions().get(i).getDurationInSecs());
			saq.setDescription(examResult.getExam().getQuestions()
				.get(i).getDescription());
			saq.setQuestion(examResult.getExam().getQuestions()
				.get(i).getQuestion());
			saq.setHint(examResult.getExam().getQuestions().get(i)
				.getHint());
			saq.setAnswer(examResult.getExam().getQuestions()
				.get(i).getAnswer());
			saq.setCorrectAnswer(examResult.getExam()
				.getQuestions().get(i).getCorrectAnswer());
			saq.setStudentAnswer(examResult.getExam()
				.getQuestions().get(i).getStudentAnswer());

			questionsList.add(saq);
		    } else if (examResult != null
			    && examResult.getExam().getQuestions() != null
			    && examResult
			    .getExam()
			    .getQuestions()
			    .get(i)
			    .getType()
			    .equals(Question.FILL_IN_THE_BLANK_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ examResult.getExam().getQuestions().get(i)
				.getType());
			Logger.warn(tag,
				"Double checking, we are in the FBQ condition");
			final FillInTheBlankQuestion fbq = new FillInTheBlankQuestion();

			Logger.warn(tag,
				"created fill in the blank question object");
			Logger.warn(tag, "QUESTION ID"
				+ examResult.getExam().getQuestions().get(i)
				.getId());
			Logger.warn(tag, "Type "
				+ examResult.getExam().getQuestions().get(i)
				.getType());
			Logger.warn(tag, "Marks allocated"
				+ examResult.getExam().getQuestions().get(i)
				.getMarksAlloted());
			Logger.warn(tag, "Question order no "
				+ examResult.getExam().getQuestions().get(i)
				.getQuestionOrderNo());
			Logger.warn(tag, "Duration "
				+ examResult.getExam().getQuestions().get(i)
				.getDurationInSecs());
			Logger.warn(tag, "Descrition "
				+ examResult.getExam().getQuestions().get(i)
				.getDescription());
			Logger.warn(tag, "Question "
				+ examResult.getExam().getQuestions().get(i)
				.getQuestion());
			Logger.warn(tag, "Hint "
				+ examResult.getExam().getQuestions().get(i)
				.getHint());
			Logger.warn(tag, "Answer "
				+ examResult.getExam().getQuestions().get(i)
				.getAnswer());

			fbq.setId(examResult.getExam().getQuestions().get(i)
				.getId());
			fbq.setType(examResult.getExam().getQuestions().get(i)
				.getType());
			fbq.setMarksAlloted(examResult.getExam().getQuestions()
				.get(i).getMarksAlloted());
			fbq.setMarksAwarded(examResult.getExam().getQuestions()
				.get(i).getMarksAwarded());
			fbq.setQuestionOrderNo(examResult.getExam()
				.getQuestions().get(i).getQuestionOrderNo());
			fbq.setDurationInSecs(examResult.getExam()
				.getQuestions().get(i).getDurationInSecs());
			fbq.setDescription(examResult.getExam().getQuestions()
				.get(i).getDescription());
			fbq.setQuestion(examResult.getExam().getQuestions()
				.get(i).getQuestion());
			fbq.setHint(examResult.getExam().getQuestions().get(i)
				.getHint());
			fbq.setAnswer(examResult.getExam().getQuestions()
				.get(i).getAnswer());
			Logger.info("Result", "===================="
				+ examResult.getExam().getQuestions().get(i)
				.getClass());
			fbq.setCorrectAnswer(examResult.getExam()
				.getQuestions().get(i).getCorrectAnswer());
			fbq.setStudentAnswer(examResult.getExam()
				.getQuestions().get(i).getStudentAnswer());
			Logger.warn(tag,
				"populating FBQ object with question data");
			questionsList.add(fbq);
			Logger.warn(tag,
				"Fill in the blank question added to the list");
		    } else if (examResult != null
			    && examResult.getExam().getQuestions() != null
			    && examResult.getExam().getQuestions().get(i)
			    .getType()
			    .equals(Question.MATCH_THE_FOLLOWING)) {
			Logger.warn(tag, "question type is:"
				+ examResult.getExam().getQuestions().get(i)
				.getType());
			final MultipleChoiceQuestion mfq = new MultipleChoiceQuestion();
			mfq.setId(examResult.getExam().getQuestions().get(i)
				.getId());
			mfq.setType(examResult.getExam().getQuestions().get(i)
				.getType());
			mfq.setMarksAlloted(examResult.getExam().getQuestions()
				.get(i).getMarksAlloted());
			mfq.setQuestionOrderNo(examResult.getExam()
				.getQuestions().get(i).getQuestionOrderNo());
			mfq.setDurationInSecs(examResult.getExam()
				.getQuestions().get(i).getDurationInSecs());
			mfq.setDescription(examResult.getExam().getQuestions()
				.get(i).getDescription());
			mfq.setQuestion(examResult.getExam().getQuestions()
				.get(i).getQuestion());
			mfq.setHint(examResult.getExam().getQuestions().get(i)
				.getHint());
			mfq.setAnswer(examResult.getExam().getQuestions()
				.get(i).getAnswer());
			mfq.setMarksAwarded(examResult.getExam().getQuestions()
				.get(i).getMarksAwarded());
			Logger.warn(tag,
				"populating MFQ object with question data & choices are");
			mfq.setChoices(examResult.getExam().getQuestions()
				.get(i).getChoices());
			mfq.setCorrectAnswer(examResult.getExam()
				.getQuestions().get(i).getCorrectAnswer());
			mfq.setIsAnswered(examResult.getExam().getQuestions()
				.get(i).getIsAnswered());

			Logger.warn(tag,
				"populating MFQ object with question data");
			questionsList.add(mfq);
			Logger.warn(tag, "mfq question added to the list");
		    }

		    else {
			Logger.warn(tag,
				"PARSER - else case adding question to list");
		    }
		    /*
		     * }else{ Logger.info(tag,"Question/Question type is null");
		     * }
		     */
		}
		examResult.getExam().setQuestions(questionsList);
	    }
	    // String id=output.getGrade();
	} catch (final Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return examResult;

    }
    
    /**
     * Gets the teacher exam result.
     *
     * @param content the content
     * @return the teacher exam result
     */
    public static com.pearl.user.teacher.examination.ExamResult getTeacherExamResult(String content) {
	com.pearl.user.teacher.examination.ExamResult examResult = null;

	Logger.info("ExamResultParser", "result PARSER - json content is:"
		+ content);
	try {
	    final ArrayList<com.pearl.user.teacher.examination.Question> questionsList = new ArrayList<com.pearl.user.teacher.examination.Question>();
	    examResult = ExamParserHelper.parseJsonToExamResultObject(content);
	    if (examResult.getExam() != null
		    && examResult.getExam().getQuestions() != null) {
		for (int i = 0; i < examResult.getExam().getQuestions().size(); i++) {
		    if (examResult != null
			    && examResult.getExam().getQuestions() != null
			    && examResult.getExam().getQuestions().get(i)
			    .getType()
			    .equals(Question.MULTIPLECHOICE_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ examResult.getExam().getQuestions().get(i)
				.getType());
			final com.pearl.user.teacher.examination.questiontype.MultipleChoiceQuestion tfq = new com.pearl.user.teacher.examination.questiontype.MultipleChoiceQuestion();

			tfq.setId(examResult.getExam().getQuestions().get(i)
				.getId());
			tfq.setType(examResult.getExam().getQuestions().get(i)
				.getType());
			tfq.setMarksAlloted(examResult.getExam().getQuestions()
				.get(i).getMarksAlloted());
			tfq.setMarksAwarded(examResult.getExam().getQuestions()
				.get(i).getMarksAwarded());
			tfq.setQuestionOrderNo(examResult.getExam()
				.getQuestions().get(i).getQuestionOrderNo());
			tfq.setDurationInSecs(examResult.getExam()
				.getQuestions().get(i).getDurationInSecs());
			tfq.setDescription(examResult.getExam().getQuestions()
				.get(i).getDescription());
			tfq.setQuestion(examResult.getExam().getQuestions()
				.get(i).getQuestion());
			tfq.setHint(examResult.getExam().getQuestions().get(i)
				.getHint());
			tfq.setCorrectAnswer(examResult.getExam()
				.getQuestions().get(i).getCorrectAnswer());
			/*for (int j = 0; j < examResult.getExam().getQuestions()
								.get(i).getChoices().size(); j++) {
							Logger.info(tag,
									"RESULT - MTF - choices before setting is:"
											+ examResult.getExam()
													.getQuestions().get(i)
													.getChoices().get(j)
													.isSelected());
						}*/
			/*Answer studentAnswer = new Answer();
			studentAnswer.setAnswerString("");
			studentAnswer.setChoices(examResult.getExam().getQuestions().get(i).getc);*/
			tfq.setStudentAnswer(examResult.getExam().getQuestions().get(i).getStudentAnswer());
			questionsList.add(tfq);
		    } else if (examResult != null
			    && examResult.getExam().getQuestions() != null
			    && examResult.getExam().getQuestions().get(i)
			    .getType()
			    .equals(Question.TRUE_OR_FALSE_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ examResult.getExam().getQuestions().get(i)
				.getType());
			final com.pearl.user.teacher.examination.questiontype.TrueOrFalseQuestion tfq = new com.pearl.user.teacher.examination.questiontype.TrueOrFalseQuestion();

			tfq.setId(examResult.getExam().getQuestions().get(i)
				.getId());
			tfq.setType(examResult.getExam().getQuestions().get(i)
				.getType());
			tfq.setMarksAlloted(examResult.getExam().getQuestions()
				.get(i).getMarksAlloted());
			tfq.setMarksAwarded(examResult.getExam().getQuestions()
				.get(i).getMarksAwarded());
			tfq.setQuestionOrderNo(examResult.getExam()
				.getQuestions().get(i).getQuestionOrderNo());
			tfq.setDurationInSecs(examResult.getExam()
				.getQuestions().get(i).getDurationInSecs());
			tfq.setDescription(examResult.getExam().getQuestions()
				.get(i).getDescription());
			tfq.setQuestion(examResult.getExam().getQuestions()
				.get(i).getQuestion());
			tfq.setHint(examResult.getExam().getQuestions().get(i)
				.getHint());
			tfq.setCorrectAnswer(examResult.getExam()
				.getQuestions().get(i).getCorrectAnswer());

			/*	for (int j = 0; j < examResult.getExam().getQuestions()
								.get(i).getChoices().size(); j++) {
							Logger.info(tag, "choices before setting is:"
									+ examResult.getExam().getQuestions()
											.get(i).getChoices().get(j)
											.isSelected());
						}*/
			tfq.setChoices(((com.pearl.user.teacher.examination.questiontype.TrueOrFalseQuestion)examResult.getExam().getQuestions()).getChoices());

			questionsList.add(tfq);
		    } else if (examResult != null
			    && examResult.getExam().getQuestions() != null
			    && examResult.getExam().getQuestions().get(i)
			    .getType()
			    .equals(Question.ORDERING_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ examResult.getExam().getQuestions().get(i)
				.getType());
			final com.pearl.user.teacher.examination.questiontype.OrderingQuestion oq = new com.pearl.user.teacher.examination.questiontype.OrderingQuestion();

			oq.setId(examResult.getExam().getQuestions().get(i)
				.getId());
			oq.setType(examResult.getExam().getQuestions().get(i)
				.getType());
			oq.setMarksAlloted(examResult.getExam().getQuestions()
				.get(i).getMarksAlloted());
			oq.setMarksAwarded(examResult.getExam().getQuestions()
				.get(i).getMarksAwarded());
			oq.setQuestionOrderNo(examResult.getExam()
				.getQuestions().get(i).getQuestionOrderNo());
			oq.setDurationInSecs(examResult.getExam()
				.getQuestions().get(i).getDurationInSecs());
			oq.setDescription(examResult.getExam().getQuestions()
				.get(i).getDescription());
			oq.setQuestion(examResult.getExam().getQuestions()
				.get(i).getQuestion());
			oq.setHint(examResult.getExam().getQuestions().get(i)
				.getHint());
			oq.setCorrectAnswer(examResult.getExam().getQuestions()
				.get(i).getCorrectAnswer());
			oq.setChoices(examResult.getExam().getQuestions()
				.get(i).getStudentAnswer().getChoices());
			oq.setStudentAnswer(examResult.getExam().getQuestions()
				.get(i).getStudentAnswer());
			/*Logger.warn("exam result parser - ",
								"Answered flag getIsAnswered- "
										+ examResult.getExam().getQuestions()
												.get(i).getIsAnswered());
						;*/
			/*Logger.warn("exam result parser - ",
								"Answered flag isAnswered- "
										+ examResult.getExam().getQuestions()
												.get(i).isAnswered());
						;*/
			/*oq.setIsAnswered(examResult.getExam().getQuestions()
								.get(i).getIsAnswered());*/

			questionsList.add(oq);
		    } else if (examResult != null
			    && examResult.getExam().getQuestions() != null
			    && examResult.getExam().getQuestions().get(i)
			    .getType()
			    .equals(Question.SHORT_ANSWER_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ examResult.getExam().getQuestions().get(i)
				.getType());
			final com.pearl.user.teacher.examination.questiontype.ShortAnswerQuestion saq = new com.pearl.user.teacher.examination.questiontype.ShortAnswerQuestion();

			saq.setId(examResult.getExam().getQuestions().get(i)
				.getId());
			saq.setType(examResult.getExam().getQuestions().get(i)
				.getType());
			saq.setMarksAlloted(examResult.getExam().getQuestions()
				.get(i).getMarksAlloted());
			saq.setMarksAwarded(examResult.getExam().getQuestions()
				.get(i).getMarksAwarded());
			saq.setQuestionOrderNo(examResult.getExam()
				.getQuestions().get(i).getQuestionOrderNo());
			saq.setDurationInSecs(examResult.getExam()
				.getQuestions().get(i).getDurationInSecs());
			saq.setDescription(examResult.getExam().getQuestions()
				.get(i).getDescription());
			saq.setQuestion(examResult.getExam().getQuestions()
				.get(i).getQuestion());
			saq.setHint(examResult.getExam().getQuestions().get(i)
				.getHint());
			saq.setAnswer(examResult.getExam().getQuestions()
				.get(i).getAnswer());
			saq.setCorrectAnswer(examResult.getExam()
				.getQuestions().get(i).getCorrectAnswer());
			saq.setStudentAnswer(examResult.getExam()
				.getQuestions().get(i).getStudentAnswer());

			questionsList.add(saq);
		    } else if (examResult != null
			    && examResult.getExam().getQuestions() != null
			    && examResult.getExam().getQuestions().get(i)
			    .getType()
			    .equals(Question.LONG_ANSWER_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ examResult.getExam().getQuestions().get(i)
				.getType());
			final com.pearl.user.teacher.examination.questiontype.LongAnswerQuestion saq = new com.pearl.user.teacher.examination.questiontype.LongAnswerQuestion();

			saq.setId(examResult.getExam().getQuestions().get(i)
				.getId());
			saq.setType(examResult.getExam().getQuestions().get(i)
				.getType());
			saq.setMarksAlloted(examResult.getExam().getQuestions()
				.get(i).getMarksAlloted());
			saq.setMarksAwarded(examResult.getExam().getQuestions()
				.get(i).getMarksAwarded());
			saq.setQuestionOrderNo(examResult.getExam()
				.getQuestions().get(i).getQuestionOrderNo());
			saq.setDurationInSecs(examResult.getExam()
				.getQuestions().get(i).getDurationInSecs());
			saq.setDescription(examResult.getExam().getQuestions()
				.get(i).getDescription());
			saq.setQuestion(examResult.getExam().getQuestions()
				.get(i).getQuestion());
			saq.setHint(examResult.getExam().getQuestions().get(i)
				.getHint());
			saq.setAnswer(examResult.getExam().getQuestions()
				.get(i).getAnswer());
			saq.setCorrectAnswer(examResult.getExam()
				.getQuestions().get(i).getCorrectAnswer());
			saq.setStudentAnswer(examResult.getExam()
				.getQuestions().get(i).getStudentAnswer());

			questionsList.add(saq);
		    } else if (examResult != null
			    && examResult.getExam().getQuestions() != null
			    && examResult
			    .getExam()
			    .getQuestions()
			    .get(i)
			    .getType()
			    .equals(Question.FILL_IN_THE_BLANK_QUESTION)) {
			Logger.warn(tag, "question type is:"
				+ examResult.getExam().getQuestions().get(i)
				.getType());
			Logger.warn(tag,
				"Double checking, we are in the FBQ condition");
			final com.pearl.user.teacher.examination.questiontype.FillInTheBlankQuestion fbq = new com.pearl.user.teacher.examination.questiontype.FillInTheBlankQuestion();

			fbq.setId(examResult.getExam().getQuestions().get(i)
				.getId());
			fbq.setType(examResult.getExam().getQuestions().get(i)
				.getType());
			fbq.setMarksAlloted(examResult.getExam().getQuestions()
				.get(i).getMarksAlloted());
			fbq.setMarksAwarded(examResult.getExam().getQuestions()
				.get(i).getMarksAwarded());
			fbq.setQuestionOrderNo(examResult.getExam()
				.getQuestions().get(i).getQuestionOrderNo());
			fbq.setDurationInSecs(examResult.getExam()
				.getQuestions().get(i).getDurationInSecs());
			fbq.setDescription(examResult.getExam().getQuestions()
				.get(i).getDescription());
			fbq.setQuestion(examResult.getExam().getQuestions()
				.get(i).getQuestion());
			fbq.setHint(examResult.getExam().getQuestions().get(i)
				.getHint());
			fbq.setAnswer(examResult.getExam().getQuestions()
				.get(i).getAnswer());

			fbq.setAnswers(((com.pearl.user.teacher.examination.questiontype.FillInTheBlankQuestion)examResult.getExam().getQuestions().get(i)).getAnswers());
			fbq.setCorrectAnswer(examResult.getExam()
				.getQuestions().get(i).getCorrectAnswer());
			fbq.setStudentAnswer(examResult.getExam()
				.getQuestions().get(i).getStudentAnswer());
			Logger.warn(tag,
				"populating FBQ object with question data");
			questionsList.add(fbq);
			Logger.warn(tag,
				"Fill in the blank question added to the list");
		    } else if (examResult != null
			    && examResult.getExam().getQuestions() != null
			    && examResult.getExam().getQuestions().get(i)
			    .getType()
			    .equals(Question.MATCH_THE_FOLLOWING)) {
			Logger.warn(tag, "question type is:"
				+ examResult.getExam().getQuestions().get(i)
				.getType());
			final com.pearl.user.teacher.examination.questiontype.MultipleChoiceQuestion mfq = new com.pearl.user.teacher.examination.questiontype.MultipleChoiceQuestion();
			mfq.setId(examResult.getExam().getQuestions().get(i)
				.getId());
			mfq.setType(examResult.getExam().getQuestions().get(i)
				.getType());
			mfq.setMarksAlloted(examResult.getExam().getQuestions()
				.get(i).getMarksAlloted());
			mfq.setQuestionOrderNo(examResult.getExam()
				.getQuestions().get(i).getQuestionOrderNo());
			mfq.setDurationInSecs(examResult.getExam()
				.getQuestions().get(i).getDurationInSecs());
			mfq.setDescription(examResult.getExam().getQuestions()
				.get(i).getDescription());
			mfq.setQuestion(examResult.getExam().getQuestions()
				.get(i).getQuestion());
			mfq.setHint(examResult.getExam().getQuestions().get(i)
				.getHint());
			mfq.setAnswer(examResult.getExam().getQuestions()
				.get(i).getAnswer());
			mfq.setMarksAwarded(examResult.getExam().getQuestions()
				.get(i).getMarksAwarded());

			mfq.setChoices(examResult.getExam().getQuestions()
				.get(i).getStudentAnswer().getChoices());
			mfq.setCorrectAnswer(examResult.getExam()
				.getQuestions().get(i).getCorrectAnswer());
			/*mfq.setIsAnswered(examResult.getExam().getQuestions()
								.get(i).getIsAnswered());*/

			/*Logger.warn(tag,
								"populating MFQ object with question data");*/
			questionsList.add(mfq);
			Logger.warn(tag, "mfq question added to the list");
		    }

		    else {
			Logger.warn(tag,
				"PARSER - else case adding question to list");
		    }
		    /*
		     * }else{ Logger.info(tag,"Question/Question type is null");
		     * }
		     */
		}
		examResult.getExam().setQuestions(questionsList);
	    }
	    // String id=output.getGrade();
	} catch (final Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return examResult;

    }
}