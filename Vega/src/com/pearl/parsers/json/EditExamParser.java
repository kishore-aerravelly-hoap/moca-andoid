package com.pearl.parsers.json;

import com.pearl.user.teacher.examination.ServerQuestion;
import com.pearl.user.teacher.examination.questiontype.FillInTheBlankQuestion;
import com.pearl.user.teacher.examination.questiontype.LongAnswerQuestion;
import com.pearl.user.teacher.examination.questiontype.MatchTheFollowingQuestion;
import com.pearl.user.teacher.examination.questiontype.MultipleChoiceQuestion;
import com.pearl.user.teacher.examination.questiontype.OrderingQuestion;
import com.pearl.user.teacher.examination.questiontype.ShortAnswerQuestion;
import com.pearl.user.teacher.examination.questiontype.TrueOrFalseQuestion;

// TODO: Auto-generated Javadoc
/**
 * The Class EditExamParser.
 */
public class EditExamParser {

	/**
	 * Convert multiple choice question.
	 *
	 * @param serverQuestion the server question
	 * @param mcq the mcq
	 * @return the server question
	 */
	public static ServerQuestion convertMultipleChoiceQuestion(
			ServerQuestion serverQuestion, MultipleChoiceQuestion mcq) {
		serverQuestion.setMarksAlloted(mcq.getMarksAlloted());
		serverQuestion.setQuestion(mcq.getQuestion());
		serverQuestion.setHasMultipleAnswers(mcq.isHasMultipleAnswers());
		serverQuestion.setExisted(true);

		if(mcq.getCorrectAnswer()!=null){
		for (int i = 0; i < mcq.getCorrectAnswer().getChoices().size(); i++) {
			serverQuestion.getMultiChoiceQnA().get(i)
					.setAnswerDes(mcq.getCorrectAnswer().getChoices().get(i).getDescription());
			if(mcq.getCorrectAnswer().getChoices().get(i).isTeacherSelected()){
			serverQuestion.setCheckValues(String.valueOf(i));		
			}
			}
		}else{
			for (int i = 0; i < mcq.getChoices().size(); i++) {
				serverQuestion.getMultiChoiceQnA().get(i)
						.setAnswerDes(mcq.getChoices().get(i).getDescription());
				if(mcq.getChoices().get(i).isTeacherSelected()){
				serverQuestion.setCheckValues(String.valueOf(i));		
				}
				}	
		}
		

		return serverQuestion;
	}

	/**
	 * Convert true or false question.
	 *
	 * @param serverQuestion the server question
	 * @param tfq the tfq
	 * @return the server question
	 */
	public static ServerQuestion convertTrueOrFalseQuestion(
			ServerQuestion serverQuestion, TrueOrFalseQuestion tfq) {
		serverQuestion.setMarksAlloted(tfq.getMarksAlloted());
		serverQuestion.setQuestion(tfq.getQuestion());
		serverQuestion.setExisted(true);
		for (int i = 0; i < tfq.getChoices().size(); i++) {
			if (tfq.getChoices().get(i).isTeacherSelected()) {
				serverQuestion.setTruefalseAnswer(Boolean.parseBoolean(tfq.getChoices().get(i)
						.getTitle()));
			}

		}

		return serverQuestion;
	}

	/**
	 * Convert fill in the blank question.
	 *
	 * @param serverQuestion the server question
	 * @param fib the fib
	 * @return the server question
	 */
	public static ServerQuestion convertFillInTheBlankQuestion(
			ServerQuestion serverQuestion, FillInTheBlankQuestion fib) {
		serverQuestion.setMarksAlloted(fib.getMarksAlloted());
		String questionString = fib.getQuestion();
		serverQuestion.setExisted(true);
		if (questionString.contains("___") && fib.getHintAnswers()!=null)
			for (int i = 0; i < fib.getHintAnswers().size(); i++) {
				questionString = questionString.replaceFirst("___", "@@"
						+ fib.getHintAnswers().get(i) + "@@");
			}
		serverQuestion.setQuestion(questionString);

		return serverQuestion;
	}

	/**
	 * Convert short answer question.
	 *
	 * @param serverQuestion the server question
	 * @param saq the saq
	 * @return the server question
	 */
	public static ServerQuestion convertShortAnswerQuestion(
			ServerQuestion serverQuestion, ShortAnswerQuestion saq) {
		serverQuestion.setMarksAlloted(saq.getMarksAlloted());
		serverQuestion.setQuestion(saq.getQuestion());
		serverQuestion.setExisted(true);
		String ans = saq.getCorrectAnswer().getAnswerString();
		StringBuffer sbuff = new StringBuffer(ans);
		serverQuestion.setShortAnswer(sbuff);

		return serverQuestion;
	}

	/**
	 * Convert long answer question.
	 *
	 * @param serverQuestion the server question
	 * @param laq the laq
	 * @return the server question
	 */
	public static ServerQuestion convertLongAnswerQuestion(
			ServerQuestion serverQuestion, LongAnswerQuestion laq) {
		serverQuestion.setMarksAlloted(laq.getMarksAlloted());
		serverQuestion.setQuestion(laq.getQuestion());
		serverQuestion.setExisted(true);
		String ans = laq.getCorrectAnswer().getAnswerString();
		StringBuffer sbuff = new StringBuffer(ans);
		serverQuestion.setLongAnswer(sbuff);

		return serverQuestion;
	}

	/**
	 * Convert ordering question.
	 *
	 * @param serverQuestion the server question
	 * @param oq the oq
	 * @return the server question
	 */
	public static ServerQuestion convertOrderingQuestion(
			ServerQuestion serverQuestion, OrderingQuestion oq) {
		serverQuestion.setQuestion(oq.getQuestion());
		serverQuestion.setExisted(true);
		serverQuestion.setMarksAlloted(oq.getMarksAlloted());
		for (int i = 0; i < oq.getChoices().size(); i++) {

			serverQuestion.getMultiChoiceQnA().get(i)
					.setPosition(oq.getChoices().get(i).getId());
			serverQuestion.getMultiChoiceQnA().get(i)
					.setAnswerDes(oq.getChoices().get(i).getTitle());
			serverQuestion.getMultiChoiceQnA().get(i).setCheckedValue(oq.getChoices().get(i).getTeacherPosition()+"");
		}

		return serverQuestion;
	}

	/**
	 * Convert match the following question.
	 *
	 * @param serverQuestion the server question
	 * @param mtf the mtf
	 * @return the server question
	 */
	public static ServerQuestion convertMatchTheFollowingQuestion(
			ServerQuestion serverQuestion, MatchTheFollowingQuestion mtf) {
		serverQuestion.setQuestion(mtf.getQuestion());
		serverQuestion.setExisted(true);
		serverQuestion.setMarksAlloted(mtf.getMarksAlloted());
		for (int i = 0; i < mtf.getCorrectAnswer().getChoices().size(); i++) {

			serverQuestion
					.getMultiChoiceQnA()
					.get(i)
					.setAnswerDes(
							mtf.getCorrectAnswer().getChoices().get(i)
									.getTitle());
			serverQuestion
					.getMultiChoiceQnA()
					.get(i)
					.setCheckedString(
							mtf.getCorrectAnswer().getChoices().get(i)
									.getMatch_FieldThree());
			serverQuestion
					.getMultiChoiceQnA()
					.get(i)
					.setCheckedText(
							mtf.getCorrectAnswer().getChoices().get(i)
									.getMatch_FieldFour());

		}

		return serverQuestion;
	}
}
