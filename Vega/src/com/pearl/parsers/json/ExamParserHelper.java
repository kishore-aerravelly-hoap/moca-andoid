/**
 * 
 */
package com.pearl.parsers.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pearl.examination.QuestionType;
import com.pearl.examination.exceptions.StudentNotAllowedToExamException;
import com.pearl.logger.Logger;
import com.pearl.ui.models.CommonUtility;
import com.pearl.user.teacher.examination.Answer;
import com.pearl.user.teacher.examination.AnswerChoice;
import com.pearl.user.teacher.examination.Exam;
import com.pearl.user.teacher.examination.Question;
import com.pearl.user.teacher.examination.ServerExamDetails;
import com.pearl.user.teacher.examination.answertype.OrderingAnswerChoice;
import com.pearl.user.teacher.examination.questiontype.FillInTheBlankQuestion;
import com.pearl.user.teacher.examination.questiontype.LongAnswerQuestion;
import com.pearl.user.teacher.examination.questiontype.MatchTheFollowingQuestion;
import com.pearl.user.teacher.examination.questiontype.MultipleChoiceQuestion;
import com.pearl.user.teacher.examination.questiontype.OrderingQuestion;
import com.pearl.user.teacher.examination.questiontype.ShortAnswerQuestion;
import com.pearl.user.teacher.examination.questiontype.TrueOrFalseQuestion;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamParserHelper.
 *
 * @author Eng5
 */
public class ExamParserHelper {
    
    /** The Constant tag. */
    private static final String tag="ExamParserHelper";

    /**
     * Convert to exam object.
     *
     * @param dataObject the data object
     * @return the exam
     * @throws JSONException the jSON exception
     */
    public static Exam convertToExamObject(JSONObject dataObject) throws JSONException{
	final Exam exam = new Exam();
	final String examDetails = dataObject.getString("examDetails");
	final JSONObject examDetailsObject = new JSONObject(examDetails);
	final ServerExamDetails serverExamDetails =  convertToServerExamDetails(examDetailsObject);
	try {
	    exam.setStudentId(examDetailsObject.getString("studentId"));
	} catch (final StudentNotAllowedToExamException e) {
	}
	exam.setExamCreatedTeacherId(dataObject.getString("examCreatedTeacherId"));
	if(dataObject.has ("examCreatedTeacherName"))
	    exam.setExamCreatedTeacherName(dataObject.getString("examCreatedTeacherName"));
	exam.setExamDetails(serverExamDetails);
	exam.setId(dataObject.getString("id"));
	exam.setOpenBookExam(dataObject.getBoolean("openBookExam"));
	exam.setQuestionPaperSet(dataObject.getString("questionPaperSet"));
	if(! dataObject.isNull("openBooks"))
	    exam.setOpenBooks(ExamParserHelper.convertToHintAnswers(dataObject.getJSONArray("openBooks")));
	//Do the coding for questions list
	if(dataObject.has("questions")){
	    final String questions = dataObject.getString("questions");
	    Logger.info(tag,"questions ===> " + questions);
	    if(!questions.equals("null") && questions.length() > 0){
		final JSONArray suggestions = dataObject.getJSONArray("questions");
		//Logger.info(tag,"List size ====> " + suggestions.size());
		final List<Question> questionsList = new ArrayList<Question>();
		for (int i=0; i<suggestions.length(); i++) {
		    final JSONObject questionObject = (JSONObject)suggestions.get(i);
		    Logger.info(tag, "Question Object ===> " + questionObject);
		    final String questionType = questionObject.getString("type");
		    Logger.info(tag,"Question Type ===> " + questionType);
		    if(questionType.equalsIgnoreCase(QuestionType.TRUE_OR_FALSE_QUESTION.name())){
			final TrueOrFalseQuestion trueFalseQuestion = convertToTrueOrFalseQuestion(questionObject);
			questionsList.add(trueFalseQuestion);
			Logger.info(tag,"TRUE_OR_FALSE_QUESTION ===> " + trueFalseQuestion);
		    }else if(questionType.equalsIgnoreCase(QuestionType.MULTIPLECHOICE_QUESTION.name())){
			final MultipleChoiceQuestion multipleChoiceQuestion = convertToMultipleChoiceQuestion(questionObject);
			questionsList.add(multipleChoiceQuestion);
			Logger.info(tag,"MULTIPLECHOICE_QUESTION ===> " + multipleChoiceQuestion);
		    }else if(questionType.equalsIgnoreCase(QuestionType.FILL_IN_THE_BLANK_QUESTION.name())){
			final FillInTheBlankQuestion fillInTheBlankQuestion = convertToFillInTheBlankQuestion(questionObject);
			questionsList.add(fillInTheBlankQuestion);
			Logger.info(tag,"FILL_IN_THE_BLANK_QUESTION ===> " + fillInTheBlankQuestion);
		    }else if(questionType.equalsIgnoreCase(QuestionType.LONG_ANSWER_QUESTION.name())){
			final LongAnswerQuestion longAnswerQuestion = convertToLongAnswerQuestion(questionObject);
			questionsList.add(longAnswerQuestion);
			Logger.info(tag,"LONG_ANSWER_QUESTION ===> " + longAnswerQuestion);
		    }else if(questionType.equalsIgnoreCase(QuestionType.SHORT_ANSWER_QUESTION.name())){
			final ShortAnswerQuestion shortAnswerQuestion = convertToShortAnswerQuestion(questionObject);
			questionsList.add(shortAnswerQuestion);
			Logger.info(tag,"SHORT_ANSWER_QUESTION ===> " + shortAnswerQuestion);
		    }else if(questionType.equalsIgnoreCase(QuestionType.ORDERING_QUESTION.name())){
			final OrderingQuestion orderingQuestion = convertToOrderingQuestion(questionObject);
			questionsList.add(orderingQuestion);
			Logger.info(tag,"ORDERING_QUESTION ===> " + orderingQuestion);
		    }else if(questionType.equalsIgnoreCase(QuestionType.MATCH_THE_FOLLOWING_QUESTION.name())){
			final MatchTheFollowingQuestion matchTheFollowingQuestion = convertToOMatchTheFollowingQuestion(questionObject);
			questionsList.add(matchTheFollowingQuestion);
			Logger.info(tag,"ORDERING_QUESTION ===> " + matchTheFollowingQuestion);
		    }
		}
		Logger.info(tag,"QuestionsList ===> " + questionsList);
		exam.setQuestions(questionsList);
	    }
	}
	return exam;
    }
    
    /**
     * Convert to exam details object.
     *
     * @param dataObject the data object
     * @return the server exam details
     * @throws JSONException the jSON exception
     */
    public static ServerExamDetails convertToExamDetailsObject(JSONObject dataObject) throws JSONException{
	final String examDetails = dataObject.getString("examDetails");
	final JSONObject examDetailsObject = new JSONObject(examDetails);
	final ServerExamDetails serverExamDetails =  convertToServerExamDetails(examDetailsObject);
	return serverExamDetails;
    }
    
    /**
     * Convert to ordering question.
     *
     * @param questionObject the question object
     * @return the ordering question
     * @throws JSONException the jSON exception
     */
    public static OrderingQuestion convertToOrderingQuestion(JSONObject questionObject) throws JSONException{
	final OrderingQuestion orderingQuestion = new OrderingQuestion();
	//Convert to answer Object
	final String answer = questionObject.getString("answer");
	if(answer.equalsIgnoreCase("null") || answer.equals("")){
	    final List<AnswerChoice> answers = new ArrayList<AnswerChoice>();
	    orderingQuestion.setAnswer(answers);
	}else{
	    final JSONObject answerObject = new JSONObject(answer);
	    final Answer answerObj = convertToAnswer(answerObject);
	    orderingQuestion.setAnswer(answerObj);
	}
	if(questionObject.has("existed"))
		orderingQuestion.setExisted(questionObject.getBoolean("existed"));
	orderingQuestion.setAnswered(questionObject.getBoolean("answered"));
	orderingQuestion.setBookName(questionObject.getString("bookName"));
	orderingQuestion.setChapterName(questionObject.getString("chapterName"));
	//Convert to choices List object
	final JSONArray choicesList = questionObject.getJSONArray("choices");
	final List<AnswerChoice> answerChoiceList = convertToListOfAnswerChoice(choicesList);
	orderingQuestion.setChoices(answerChoiceList);

	orderingQuestion.setCorrect(questionObject.getBoolean("correct"));
	//Convert to correct answer object
	final String correctAnswer = questionObject.getString("correctAnswer");
	if(correctAnswer.equalsIgnoreCase("null") || correctAnswer.equals("")){
	    orderingQuestion.setCorrectAnswer(null);
	}else{
	    final JSONObject correctAnswerObject = new JSONObject(correctAnswer);
	    final Answer correctAnswerObj = convertToAnswer(correctAnswerObject);
	    orderingQuestion.setCorrectAnswer(correctAnswerObj);
	}
	orderingQuestion.setCorrected(questionObject.getBoolean("corrected"));
	orderingQuestion.setDescription(questionObject.getString("description"));
	orderingQuestion.setDifficultyLevel(questionObject.getInt("difficultyLevel"));
	orderingQuestion.setDurationInSecs(questionObject.getLong("durationInSecs"));
	orderingQuestion.setHasMultipleAnswers(false);
	orderingQuestion.setHint(questionObject.getString("hint"));
	orderingQuestion.setId(questionObject.getString("id"));
	orderingQuestion.setMarked(questionObject.getBoolean("marked"));
	orderingQuestion.setMarksAlloted(questionObject.getInt("marksAlloted"));
	orderingQuestion.setMarksAwarded(questionObject.getInt("marksAwarded"));
	orderingQuestion.setQuestion(questionObject.getString("question"));
	orderingQuestion.setQuestionOrderNo(questionObject.getInt("questionOrderNo"));
	//Convert to student answer object
	final String studentAnswer = questionObject.getString("studentAnswer");
	final JSONObject studentAnswerObject = new JSONObject(studentAnswer);
	final Answer studentAnswerObj = convertToAnswer(studentAnswerObject);
	orderingQuestion.setStudentAnswer(studentAnswerObj);

	orderingQuestion.setType(questionObject.getString("type"));
	orderingQuestion.setViewed(questionObject.getBoolean("viewed"));
	return orderingQuestion;
    }
    
    /**
     * Convert to short answer question.
     *
     * @param questionObject the question object
     * @return the short answer question
     * @throws JSONException the jSON exception
     */
    public static ShortAnswerQuestion convertToShortAnswerQuestion(JSONObject questionObject) throws JSONException{
	final ShortAnswerQuestion shortAnswerQuestion = new ShortAnswerQuestion();
	//Convert to answer Object
	final String answer = questionObject.getString("answer");
	if(answer.equalsIgnoreCase("null") || answer.equals("")){
	    shortAnswerQuestion.setAnswer(null);
	}else{
	    final JSONObject answerObject = new JSONObject(answer);
	    final Answer answerObj = convertToAnswer(answerObject);
	    shortAnswerQuestion.setAnswer(answerObj);
	}
	if(questionObject.has("existed"))
		shortAnswerQuestion.setExisted(questionObject.getBoolean("existed"));

	shortAnswerQuestion.setAnswered(questionObject.getBoolean("answered"));
	shortAnswerQuestion.setBookName(questionObject.getString("bookName"));
	shortAnswerQuestion.setChapterName(questionObject.getString("chapterName"));
	shortAnswerQuestion.setCorrect(questionObject.getBoolean("correct"));
	//Convert to correct answer object
	final String correctAnswer = questionObject.getString("correctAnswer");
	if(correctAnswer.equalsIgnoreCase("null") || correctAnswer.equals("")){
	    shortAnswerQuestion.setCorrectAnswer(null);
	}else{
	    final JSONObject correctAnswerObject = new JSONObject(correctAnswer);
	    final Answer correctAnswerObj = convertToAnswer(correctAnswerObject);
	    shortAnswerQuestion.setCorrectAnswer(correctAnswerObj);
	}
	shortAnswerQuestion.setCorrected(questionObject.getBoolean("corrected"));
	shortAnswerQuestion.setDescription(questionObject.getString("description"));
	shortAnswerQuestion.setDifficultyLevel(questionObject.getInt("difficultyLevel"));
	shortAnswerQuestion.setDurationInSecs(questionObject.getLong("durationInSecs"));
	shortAnswerQuestion.setHint(questionObject.getString("hint"));
	shortAnswerQuestion.setId(questionObject.getString("id"));
	shortAnswerQuestion.setMarked(questionObject.getBoolean("marked"));
	shortAnswerQuestion.setMarksAlloted(questionObject.getInt("marksAlloted"));
	shortAnswerQuestion.setMarksAwarded(questionObject.getInt("marksAwarded"));
	shortAnswerQuestion.setQuestion(questionObject.getString("question"));
	shortAnswerQuestion.setQuestionOrderNo(questionObject.getInt("questionOrderNo"));

	final String studentAnswer = questionObject.getString("studentAnswer");
	final JSONObject studentAnswerObject = new JSONObject(studentAnswer);
	final Answer studentAnswerObj = convertToAnswer(studentAnswerObject);
	shortAnswerQuestion.setStudentAnswer(studentAnswerObj);
	shortAnswerQuestion.setType(questionObject.getString("type"));
	shortAnswerQuestion.setViewed(questionObject.getBoolean("viewed"));

	return shortAnswerQuestion;
    }
    
    /**
     * Convert to long answer question.
     *
     * @param questionObject the question object
     * @return the long answer question
     * @throws JSONException the jSON exception
     */
    public static LongAnswerQuestion convertToLongAnswerQuestion(JSONObject questionObject) throws JSONException{
	final LongAnswerQuestion longAnswerQuestion = new LongAnswerQuestion();
	//Convert to answer Object
	final String answer = questionObject.getString("answer");
	if(answer.equalsIgnoreCase("null") || answer.equals("")){
	    longAnswerQuestion.setAnswer(null);
	}else{
	    final JSONObject answerObject = new JSONObject(answer);
	    final Answer answerObj = convertToAnswer(answerObject);
	    longAnswerQuestion.setAnswer(answerObj);
	}
	
	if(questionObject.has("existed"))
		longAnswerQuestion.setExisted(questionObject.getBoolean("existed"));
	longAnswerQuestion.setAnswered(questionObject.getBoolean("answered"));
	longAnswerQuestion.setBookName(questionObject.getString("bookName"));
	longAnswerQuestion.setChapterName(questionObject.getString("chapterName"));
	longAnswerQuestion.setCorrect(questionObject.getBoolean("correct"));
	//Convert to correct answer object
	final String correctAnswer = questionObject.getString("correctAnswer");
	if(correctAnswer.equalsIgnoreCase("null") || correctAnswer.equals("")){
	    longAnswerQuestion.setCorrectAnswer(null);
	}else{
	    final JSONObject correctAnswerObject = new JSONObject(correctAnswer);
	    final Answer correctAnswerObj = convertToAnswer(correctAnswerObject);
	    longAnswerQuestion.setCorrectAnswer(correctAnswerObj);
	}
	longAnswerQuestion.setCorrected(questionObject.getBoolean("corrected"));
	longAnswerQuestion.setDescription(questionObject.getString("description"));
	longAnswerQuestion.setDifficultyLevel(questionObject.getInt("difficultyLevel"));
	longAnswerQuestion.setDurationInSecs(questionObject.getLong("durationInSecs"));
	longAnswerQuestion.setHint(questionObject.getString("hint"));
	longAnswerQuestion.setId(questionObject.getString("id"));
	longAnswerQuestion.setMarked(questionObject.getBoolean("marked"));
	longAnswerQuestion.setMarksAlloted(questionObject.getInt("marksAlloted"));
	longAnswerQuestion.setMarksAwarded(questionObject.getInt("marksAwarded"));
	longAnswerQuestion.setQuestion(questionObject.getString("question"));
	longAnswerQuestion.setQuestionOrderNo(questionObject.getInt("questionOrderNo"));

	final String studentAnswer = questionObject.getString("studentAnswer");
	final JSONObject studentAnswerObject = new JSONObject(studentAnswer);
	final Answer studentAnswerObj = convertToAnswer(studentAnswerObject);
	longAnswerQuestion.setStudentAnswer(studentAnswerObj);
	longAnswerQuestion.setType(questionObject.getString("type"));
	longAnswerQuestion.setViewed(questionObject.getBoolean("viewed"));

	return longAnswerQuestion;
    }
    
    /**
     * Convert to fill in the blank question.
     *
     * @param questionObject the question object
     * @return the fill in the blank question
     * @throws JSONException the jSON exception
     */
    public static FillInTheBlankQuestion convertToFillInTheBlankQuestion(JSONObject questionObject) throws JSONException{
	final FillInTheBlankQuestion fillInTheBlankQuestion = new FillInTheBlankQuestion();
	//Convert to answer Object
	final String answer = questionObject.getString("answer");
	if(answer.equalsIgnoreCase("null") || answer.equals("")){
	    fillInTheBlankQuestion.setAnswer(null);
	}else{
	    final JSONObject answerObject = new JSONObject(answer);
	    final Answer answerObj = convertToAnswer(answerObject);
	    fillInTheBlankQuestion.setAnswer(answerObj);
	}
	fillInTheBlankQuestion.setAnswered(questionObject.getBoolean("answered"));
	fillInTheBlankQuestion.setBookName(questionObject.getString("bookName"));
	fillInTheBlankQuestion.setChapterName(questionObject.getString("chapterName"));

	if(questionObject.has("existed"))
		fillInTheBlankQuestion.setExisted(questionObject.getBoolean("existed"));
	fillInTheBlankQuestion.setCorrect(questionObject.getBoolean("correct"));
	//Convert to correct answer object
	final String correctAnswer = questionObject.getString("correctAnswer");
	if(correctAnswer.equalsIgnoreCase("null") || correctAnswer.equals("")){
	    fillInTheBlankQuestion.setCorrectAnswer(null);
	}else{
	    final JSONObject correctAnswerObject = new JSONObject(correctAnswer);
	    final Answer correctAnswerObj = convertToAnswer(correctAnswerObject);
	    fillInTheBlankQuestion.setCorrectAnswer(correctAnswerObj);
	}

	fillInTheBlankQuestion.setCorrected(questionObject.getBoolean("corrected"));
	fillInTheBlankQuestion.setDescription(questionObject.getString("description"));
	fillInTheBlankQuestion.setDifficultyLevel(questionObject.getInt("difficultyLevel"));
	fillInTheBlankQuestion.setDurationInSecs(questionObject.getLong("durationInSecs"));
	fillInTheBlankQuestion.setHint(questionObject.getString("hint"));
	//Convert to hint answers list object
	final String hintAnswers = questionObject.getString("hintAnswers");
	if(hintAnswers.equals("null") || hintAnswers.equals("")){
	    fillInTheBlankQuestion.setHintAnswers(null);
	}else{
	    final JSONArray hintAnswersArray = questionObject.getJSONArray("hintAnswers");
	    final List<String> hintAnswersList = convertToHintAnswers(hintAnswersArray);
	    fillInTheBlankQuestion.setHintAnswers(hintAnswersList);
	}
	// convert to answers
	final String answers=questionObject.getString("answers");
	if(answers.equals("null") || answers.equals("")){
	    fillInTheBlankQuestion.setHintAnswers(null);
	}else{
	    final JSONArray answersArray = questionObject.getJSONArray("answers");
	    final List<String> answersList = convertToHintAnswers(answersArray);
	    fillInTheBlankQuestion.setAnswers(answersList);
	}

	final String totalBlanks = questionObject.getString("totalBlanks");
	fillInTheBlankQuestion.setTotalBlanks(Integer.parseInt(totalBlanks));

	fillInTheBlankQuestion.setId(questionObject.getString("id"));
	fillInTheBlankQuestion.setMarked(questionObject.getBoolean("marked"));
	fillInTheBlankQuestion.setMarksAlloted(questionObject.getInt("marksAlloted"));
	fillInTheBlankQuestion.setMarksAwarded(questionObject.getInt("marksAwarded"));
	fillInTheBlankQuestion.setQuestion(questionObject.getString("question"));
	fillInTheBlankQuestion.setQuestionOrderNo(questionObject.getInt("questionOrderNo"));
	//Convert to student answer object
	final String studentAnswer = questionObject.getString("studentAnswer");
	final JSONObject studentAnswerObject = new JSONObject(studentAnswer);
	final Answer studentAnswerObj = convertToAnswer(studentAnswerObject);
	fillInTheBlankQuestion.setStudentAnswer(studentAnswerObj);
	fillInTheBlankQuestion.setType(questionObject.getString("type"));
	fillInTheBlankQuestion.setViewed(questionObject.getBoolean("viewed"));

	return fillInTheBlankQuestion;
    }
    
    /**
     * Convert to multiple choice question.
     *
     * @param questionObject the question object
     * @return the multiple choice question
     * @throws JSONException the jSON exception
     */
    public static MultipleChoiceQuestion convertToMultipleChoiceQuestion(JSONObject questionObject) throws JSONException{
	final MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion();
	//Convert to answer Object
	final String answer = questionObject.getString("answer");
	if(answer.equalsIgnoreCase("null") || answer.equals("")){
	    multipleChoiceQuestion.setAnswer(null);
	}else{
	    final JSONObject answerObject = new JSONObject(answer);
	    final Answer answerObj = convertToAnswer(answerObject);
	    multipleChoiceQuestion.setAnswer(answerObj);
	}
	if(questionObject.has("existed"))
		multipleChoiceQuestion.setExisted(questionObject.getBoolean("existed"));
	multipleChoiceQuestion.setAnswered(questionObject.getBoolean("answered"));
	multipleChoiceQuestion.setBookName(questionObject.getString("bookName"));
	multipleChoiceQuestion.setChapterName(questionObject.getString("chapterName"));
	//Convert to choices List object
	final JSONArray choicesList = questionObject.getJSONArray("choices");
	final List<AnswerChoice> answerChoiceList = convertToListOfAnswerChoice(choicesList);
	multipleChoiceQuestion.setChoices(answerChoiceList);

	multipleChoiceQuestion.setCorrect(questionObject.getBoolean("correct"));
	//Convert to correct answer object
	final String correctAnswer = questionObject.getString("correctAnswer");
	if(correctAnswer.equalsIgnoreCase("null") || correctAnswer.equals("")){
	    multipleChoiceQuestion.setCorrectAnswer(null);
	}else{
	    final JSONObject correctAnswerObject = new JSONObject(correctAnswer);
	    final Answer correctAnswerObj = convertToAnswer(correctAnswerObject);
	    multipleChoiceQuestion.setCorrectAnswer(correctAnswerObj);
	}
	multipleChoiceQuestion.setCorrected(questionObject.getBoolean("corrected"));
	multipleChoiceQuestion.setDescription(questionObject.getString("description"));
	multipleChoiceQuestion.setDifficultyLevel(questionObject.getInt("difficultyLevel"));
	multipleChoiceQuestion.setDurationInSecs(questionObject.getLong("durationInSecs"));
	multipleChoiceQuestion.setHasMultipleAnswers(false);
	multipleChoiceQuestion.setHint(questionObject.getString("hint"));
	multipleChoiceQuestion.setId(questionObject.getString("id"));
	multipleChoiceQuestion.setMarked(questionObject.getBoolean("marked"));
	multipleChoiceQuestion.setMarksAlloted(questionObject.getInt("marksAlloted"));
	multipleChoiceQuestion.setMarksAwarded(questionObject.getInt("marksAwarded"));
	multipleChoiceQuestion.setQuestion(questionObject.getString("question"));
	multipleChoiceQuestion.setQuestionOrderNo(questionObject.getInt("questionOrderNo"));
	//Convert to student answer object
	final String studentAnswer = questionObject.getString("studentAnswer");
	final JSONObject studentAnswerObject = new JSONObject(studentAnswer);
	final Answer studentAnswerObj = convertToAnswer(studentAnswerObject);
	multipleChoiceQuestion.setStudentAnswer(studentAnswerObj);

	multipleChoiceQuestion.setType(questionObject.getString("type"));
	multipleChoiceQuestion.setViewed(questionObject.getBoolean("viewed"));

	return multipleChoiceQuestion;
    }

    /**
     * Convert to true or false question.
     *
     * @param questionObject the question object
     * @return the true or false question
     * @throws JSONException the jSON exception
     */
    public static TrueOrFalseQuestion convertToTrueOrFalseQuestion(JSONObject questionObject) throws JSONException{
	final TrueOrFalseQuestion trueFalseQuestion = new TrueOrFalseQuestion();
	trueFalseQuestion.setType(questionObject.getString("type"));
	trueFalseQuestion.setQuestion(questionObject.getString("question"));
	Logger.info(tag,"questionObject.getBoolean('answered')"+ questionObject.getBoolean("answered"));
	trueFalseQuestion.setAnswered(questionObject.getBoolean("answered"));
	if(questionObject.has("existed"))
	trueFalseQuestion.setExisted(questionObject.getBoolean("existed"));

	//Convert to answer Object
	final String answer = questionObject.getString("answer");
	if(answer.equalsIgnoreCase("null") || answer.equals("")){
	    final OrderingAnswerChoice answerChoice = new OrderingAnswerChoice();
	    trueFalseQuestion.setAnswer(answerChoice);
	}else{
	    final JSONObject answerObject = new JSONObject(answer);
	    final Answer answerObj = convertToAnswer(answerObject);
	    trueFalseQuestion.setAnswer(answerObj);
	}
	trueFalseQuestion.setBookName(questionObject.getString("bookName"));
	trueFalseQuestion.setChapterName(questionObject.getString("chapterName"));
	trueFalseQuestion.setCorrect(questionObject.getBoolean("correct"));

	//Convert to correct answer object
	final String correctAnswer = questionObject.getString("correctAnswer");
	if(correctAnswer.equalsIgnoreCase("null") || correctAnswer.equals("")){
	    trueFalseQuestion.setCorrectAnswer(null);
	}else{
	    final JSONObject correctAnswerObject = new JSONObject(correctAnswer);
	    final Answer correctAnswerObj = convertToAnswer(correctAnswerObject);
	    trueFalseQuestion.setCorrectAnswer(correctAnswerObj);
	}

	trueFalseQuestion.setCorrected(questionObject.getBoolean("corrected"));
	trueFalseQuestion.setDescription(questionObject.getString("description"));
	trueFalseQuestion.setDifficultyLevel(questionObject.getInt("difficultyLevel"));
	trueFalseQuestion.setDurationInSecs(questionObject.getLong("durationInSecs"));
	trueFalseQuestion.setHasMultipleAnswers(false);
	trueFalseQuestion.setHint(questionObject.getString("hint"));
	trueFalseQuestion.setId(questionObject.getString("id"));
	trueFalseQuestion.setMarked(questionObject.getBoolean("marked"));
	trueFalseQuestion.setMarksAlloted(questionObject.getInt("marksAlloted"));
	trueFalseQuestion.setMarksAwarded(questionObject.getInt("marksAwarded"));
	trueFalseQuestion.setQuestion(questionObject.getString("question"));
	trueFalseQuestion.setQuestionOrderNo(questionObject.getInt("questionOrderNo"));

	//Convert to student answer object
	final String studentAnswer = questionObject.getString("studentAnswer");
	final JSONObject studentAnswerObject = new JSONObject(studentAnswer);
	final Answer studentAnswerObj = convertToAnswer(studentAnswerObject);
	trueFalseQuestion.setStudentAnswer(studentAnswerObj);

	trueFalseQuestion.setViewed(questionObject.getBoolean("viewed"));

	//Convert to choices List object
	final JSONArray choicesList = questionObject.getJSONArray("choices");
	final List<AnswerChoice> answerChoiceList = convertToListOfAnswerChoice(choicesList);
	trueFalseQuestion.setChoices(answerChoiceList);

	//Convert to studentSelectedChoice object
	if(questionObject.has("studentSelectedChoice")){
	final String studentSelectedChoice = questionObject.getString("studentSelectedChoice");
	if(studentSelectedChoice.equalsIgnoreCase("null") || studentSelectedChoice.equals("")){
	    trueFalseQuestion.setStudentSelectedChoice(null);
	}else{
	    final JSONObject studentSelectedChoiceObject = new JSONObject(studentSelectedChoice);
	    final AnswerChoice studentSelectedChoiceAnswer = convertToAnswerChoice(studentSelectedChoiceObject);
	    trueFalseQuestion.setStudentSelectedChoice(studentSelectedChoiceAnswer);
	}
	}

	Logger.info(tag,"TRUE FALSE QUESTION ===> " + trueFalseQuestion);
	return trueFalseQuestion;
    }
    
    /**
     * Convert to server exam details.
     *
     * @param examDetailsObject the exam details object
     * @return the server exam details
     * @throws JSONException the jSON exception
     */
    public static ServerExamDetails convertToServerExamDetails(JSONObject examDetailsObject) throws JSONException{
	final ServerExamDetails serverExamDetails = new ServerExamDetails();
	serverExamDetails.setExamId(examDetailsObject.getString("examId"));
	serverExamDetails.setAllowedBookIds(null);
	serverExamDetails.setBeginDate(examDetailsObject.getString("beginDate"));
	serverExamDetails.setCompleteDate(examDetailsObject.getString("completeDate"));
	if(examDetailsObject.has("description")){
	    serverExamDetails.setDescription(examDetailsObject.getString("description"));
	}else{
	    serverExamDetails.setDescription(null);
	}
	serverExamDetails.setDuration(examDetailsObject.getLong("duration"));
	final Object obj = examDetailsObject.get("endDate");
	if(obj instanceof Integer) {
	    serverExamDetails.setStartDate(examDetailsObject.getInt("endDate")==0?null:new Date(examDetailsObject.getInt("endDate")));
	}else if(obj instanceof Long ){
	    serverExamDetails.setEndDate(new Date(examDetailsObject.getLong("endDate")));
	}else if(obj instanceof String){
	    serverExamDetails.setEndDate(CommonUtility.getDateTime(examDetailsObject.getString("endDate")));
	}else if(obj instanceof Date){
	    serverExamDetails.setEndDate((Date)examDetailsObject.get("endDate"));
	}else{
	    final JSONObject date = (JSONObject)obj;
	    if(date!=null && date.has("$date")){
		final String actualDate = date.getString("$date");
		final Date endDate = CommonUtility.convertStringToDate(actualDate,CommonUtility.DATE_FORMAT);
		serverExamDetails.setEndDate(endDate);
	    }else if(examDetailsObject.getLong("endTime")!=0){
		final Date enddate=new Date(examDetailsObject.getLong("endTime"));
		serverExamDetails.setEndDate(enddate);
	    }
	}

	serverExamDetails.setEndTime(examDetailsObject.getLong("endTime"));
	if(examDetailsObject.has("grade"))
	    serverExamDetails.setGrade(examDetailsObject.getString("grade"));
	serverExamDetails.setGradeList(null);
	serverExamDetails.setMaxPoints(examDetailsObject.getInt("maxPoints"));
	serverExamDetails.setMinPoints(examDetailsObject.getInt("minPoints"));
	serverExamDetails.setNegativePoints(false);
	serverExamDetails.setResultId(examDetailsObject.getLong("resultId"));
	final Object retakable = examDetailsObject.get("retakeable");
	if(retakable instanceof String){
	    if(examDetailsObject.getString("retakeable").equalsIgnoreCase("false")){
		serverExamDetails.setRetakeable(false);
	    }else if(examDetailsObject.getString("retakeable").equalsIgnoreCase("true")){
		serverExamDetails.setRetakeable(true);
	    }
	}else{
	    serverExamDetails.setRetakeable(examDetailsObject.getBoolean("retakeable"));
	}
	if(examDetailsObject.has("section"))
	    serverExamDetails.setSection(examDetailsObject.getString("section"));


	final String section = examDetailsObject.getString("sectionList");
	if(section.equals("null") || section.equals("")){
	    serverExamDetails.setSectionList(null);
	}else {
	    final JSONArray sectionArray = examDetailsObject.getJSONArray("sectionList");
	    final List<String> sectionsList = convertToHintAnswers(sectionArray);
	    serverExamDetails.setSectionList(sectionsList);
	    if(sectionArray!=null) {

	    }
	    
	}

	  if(examDetailsObject.has("academicYear"))
	    	serverExamDetails.setAcademicYear(examDetailsObject.getString("academicYear"));


	final Object obj1 = examDetailsObject.get("startDate");
	if(obj1 instanceof Integer) {
	    serverExamDetails.setStartDate(examDetailsObject.getInt("startDate")==0?null:new Date(examDetailsObject.getInt("startDate")));
	}else	if(obj1 instanceof Long  ){
	    serverExamDetails.setStartDate(new Date(examDetailsObject.getLong("startDate")));
	}else if(obj1 instanceof String){
	    serverExamDetails.setStartDate(CommonUtility.getDateTime(examDetailsObject.getString("startDate")));
	}else if(obj instanceof Date){
	    serverExamDetails.setStartDate((Date)examDetailsObject.get("startDate"));
	}else{
	    final JSONObject date = (JSONObject)obj;
	    if(date!=null && date.has("$date")){
		final String actualDate = date.getString("$date");
		final Date startDate = CommonUtility.convertStringToDate(actualDate,CommonUtility.DATE_FORMAT);
		serverExamDetails.setStartDate(startDate);
	    }else if(examDetailsObject.getLong("startTime")!=0){
		final Date startDate=new Date(examDetailsObject.getLong("startTime"));
		serverExamDetails.setStartDate(startDate);
	    }
	}

	serverExamDetails.setStartTime(examDetailsObject.getLong("startTime"));
	if(examDetailsObject.has("state"))
	    serverExamDetails.setState(examDetailsObject.getString("state"));
	if(examDetailsObject.has("studentId"))
	    serverExamDetails.setStudentId(examDetailsObject.getString("studentId"));
	if(examDetailsObject.has("subject"))
	    serverExamDetails.setSubject(examDetailsObject.getString("subject"));
	serverExamDetails.setSubjectList(null);
	serverExamDetails.setTitle(examDetailsObject.getString("title"));
	serverExamDetails.setUploadedDate(examDetailsObject.getLong("uploadedDate"));
	serverExamDetails.setExamCategory(examDetailsObject.getString("examCategory"));
	Logger.info(tag,"ServerExamDetails ===> " + serverExamDetails);
	return serverExamDetails;
    }

    //convert to Match the following Question
    /**
     * Convert to o match the following question.
     *
     * @param questionObject the question object
     * @return the match the following question
     * @throws JSONException the jSON exception
     */
    public static MatchTheFollowingQuestion convertToOMatchTheFollowingQuestion(JSONObject questionObject) throws JSONException{
	final MatchTheFollowingQuestion matchTheFollowingQuestion=new MatchTheFollowingQuestion();
	//Convert to answer Object
	final String answer = questionObject.getString("answer");
	if(answer.equalsIgnoreCase("null") || answer.equals("")){
	    matchTheFollowingQuestion.setAnswer(null);
	}else{
	    final JSONObject answerObject = new JSONObject(answer);
	    final Answer answerObj = convertToAnswer(answerObject);
	    matchTheFollowingQuestion.setAnswer(answerObj);
	}
	matchTheFollowingQuestion.setAnswered(questionObject.getBoolean("answered"));
	matchTheFollowingQuestion.setBookName(questionObject.getString("bookName"));
	matchTheFollowingQuestion.setChapterName(questionObject.getString("chapterName"));
	//Convert to choices List object
	final JSONArray choicesList = questionObject.getJSONArray("choices");
	final List<AnswerChoice> answerChoiceList = convertToListOfAnswerChoice(choicesList);
	matchTheFollowingQuestion.setChoices(answerChoiceList);

	matchTheFollowingQuestion.setCorrect(questionObject.getBoolean("correct"));
	//Convert to correct answer object
	final String correctAnswer = questionObject.getString("correctAnswer");
	if(correctAnswer.equalsIgnoreCase("null") || correctAnswer.equals("")){
	    matchTheFollowingQuestion.setCorrectAnswer(null);
	}else{
	    final JSONObject correctAnswerObject = new JSONObject(correctAnswer);
	    final Answer correctAnswerObj = convertToAnswer(correctAnswerObject);
	    matchTheFollowingQuestion.setCorrectAnswer(correctAnswerObj);
	}
	matchTheFollowingQuestion.setCorrected(questionObject.getBoolean("corrected"));
	matchTheFollowingQuestion.setDescription(questionObject.getString("description"));
	matchTheFollowingQuestion.setDifficultyLevel(questionObject.getInt("difficultyLevel"));
	matchTheFollowingQuestion.setDurationInSecs(questionObject.getLong("durationInSecs"));
	matchTheFollowingQuestion.setHasMultipleAnswers(false);
	matchTheFollowingQuestion.setHint(questionObject.getString("hint"));
	matchTheFollowingQuestion.setId(questionObject.getString("id"));
	matchTheFollowingQuestion.setMarked(questionObject.getBoolean("marked"));
	matchTheFollowingQuestion.setMarksAlloted(questionObject.getInt("marksAlloted"));
	matchTheFollowingQuestion.setMarksAwarded(questionObject.getInt("marksAwarded"));
	matchTheFollowingQuestion.setQuestion(questionObject.getString("question"));
	matchTheFollowingQuestion.setQuestionOrderNo(questionObject.getInt("questionOrderNo"));
	//Convert to student answer object
	final String studentAnswer = questionObject.getString("studentAnswer");
	final JSONObject studentAnswerObject = new JSONObject(studentAnswer);
	final Answer studentAnswerObj = convertToAnswer(studentAnswerObject);
	matchTheFollowingQuestion.setStudentAnswer(studentAnswerObj);

	matchTheFollowingQuestion.setType(questionObject.getString("type"));
	matchTheFollowingQuestion.setViewed(questionObject.getBoolean("viewed"));

	return matchTheFollowingQuestion;
    }


    /**
     * Convert to answer.
     *
     * @param answerObject the answer object
     * @return the answer
     * @throws JSONException the jSON exception
     */
    public static Answer convertToAnswer(JSONObject answerObject) throws JSONException{
	final Answer answerObj = new Answer();
	answerObj.setAnswerString(answerObject.getString("answerString"));
	if(answerObject.has("choices")){
	final String choices = answerObject.getString("choices");
	if(choices.equalsIgnoreCase("null") || choices.equals("")){
	    answerObj.setChoices(null);
	
	}else{
	    final JSONArray choicesList = answerObject.getJSONArray("choices");
	    final List<AnswerChoice> answerChoiceList = convertToListOfAnswerChoice(choicesList);
	    answerObj.setChoices(answerChoiceList);
	}
	}
	
	return answerObj;
    }

    /**
     * Convert to list of answer choice.
     *
     * @param choicesList the choices list
     * @return the list
     * @throws JSONException the jSON exception
     */
    public static List<AnswerChoice> convertToListOfAnswerChoice(JSONArray choicesList) throws JSONException{
	final List<AnswerChoice> answerChoiceList = new ArrayList<AnswerChoice>();
	for(int i=0; i<choicesList.length(); i++){
	    final JSONObject choicesObject = (JSONObject) choicesList.get(i);
	    final AnswerChoice answerChoice = convertToAnswerChoice(choicesObject);
	    answerChoiceList.add(answerChoice);
	}

	return answerChoiceList;
    }
    
    /**
     * Convert to hint answers.
     *
     * @param hintAnswersList the hint answers list
     * @return the list
     * @throws JSONException the jSON exception
     */
    public static List<String> convertToHintAnswers(JSONArray hintAnswersList) throws JSONException{
	final List<String> list = new LinkedList<String>();
	for(int i=0; i<hintAnswersList.length(); i++){
	    list.add(hintAnswersList.get(i).toString());
	}
	return list;
    }

    /**
     * Convert to answer choice.
     *
     * @param choicesObject the choices object
     * @return the answer choice
     * @throws JSONException the jSON exception
     */
    public static AnswerChoice convertToAnswerChoice(JSONObject choicesObject) throws JSONException{
	final AnswerChoice answerChoice = new AnswerChoice();
	answerChoice.setId(choicesObject.getInt("id"));
	answerChoice.setDescription(choicesObject.getString("description"));
	answerChoice.setPosition(choicesObject.getInt("position"));
	answerChoice.setSelected(choicesObject.getBoolean("selected"));
	answerChoice.setTeacherSelected(choicesObject.getBoolean("teacherSelected"));
	answerChoice.setTitle(choicesObject.getString("title"));
	if(choicesObject.has("match_FieldOne")){
	    answerChoice.setMatch_FieldOne(choicesObject.getString("match_FieldOne"));
	}
	if(choicesObject.has("match_FieldThree")){
	    answerChoice.setMatch_FieldThree(choicesObject.getString("match_FieldThree"));
	}
	if(choicesObject.has("match_FieldFour")){
	    answerChoice.setMatch_FieldFour(choicesObject.getString("match_FieldFour"));
	}
	if(choicesObject.has("teacherPosition")){
	    answerChoice.setTeacherPosition(choicesObject.getInt("teacherPosition"));
	}
	return answerChoice;
    }

    /**
     * Parses the json to exam result object.
     *
     * @param jsonString the json string
     * @return the com.pearl.user.teacher.examination. exam result
     */
    public static com.pearl.user.teacher.examination.ExamResult parseJsonToExamResultObject(String jsonString){
	final com.pearl.user.teacher.examination.ExamResult examResult = new com.pearl.user.teacher.examination.ExamResult();
	try{
	    final JSONObject dataObject = new JSONObject(jsonString);
	    examResult.setCorrectAnsTotalMarks(dataObject.getInt("correctAnsTotalMarks"));
	    examResult.setCorrectAnsweredCount(dataObject.getInt("correctAnsweredCount"));
	    final JSONObject examObject = new JSONObject(dataObject.getString("exam"));
	    //		examResult.setExam(ExamParserHelper.convertToExamObject(examObject));
	    examResult.setExam(ExamParserHelper.convertToExamObject(examObject));
	    examResult.setId(dataObject.getString("id"));
	    examResult.setMarksAlloted(dataObject.getInt("marksAlloted"));
	    examResult.setRank(dataObject.getInt("rank"));
	    examResult.setStudentId(dataObject.getString("studentId"));
	    examResult.setTeacherReviewNeeded(dataObject.getBoolean("teacherReviewNeeded"));
	    examResult.setTotalMarks(dataObject.getInt("totalMarks"));
	    examResult.setTotalQuestions(dataObject.getInt("totalQuestions"));
	    examResult.setUnAnsweredCount(dataObject.getInt("unAnsweredCount"));
	    examResult.setUnAnswerTotalMarks(dataObject.getInt("unAnswerTotalMarks"));
	    examResult.setUnSeenCount(dataObject.getInt("unSeenCount"));
	    examResult.setWrongAnsTotalMarks(dataObject.getInt("wrongAnsTotalMarks"));
	    examResult.setWrongAnsweredCount(dataObject.getInt("wrongAnsweredCount"));
	    examResult.setAutomatedTotalQuestions(dataObject.getInt("automatedTotalQuestions"));
	}catch(final Exception e){
	    e.printStackTrace();
	}
	return examResult;
    }

    
    /**
     * Convert to question object.
     *
     * @param dataObject the data object
     * @return the list
     * @throws JSONException the jSON exception
     */
    public static   List<Question> convertToQuestionObject(JSONObject dataObject) throws JSONException {
	final List<Question> questionsList_FINAL = new ArrayList<Question>();
	    final String questions = dataObject.getString("questions");
	    Logger.info(tag,"questions ===> " + questions);
	    if(!questions.equals("null") && questions.length() > 0){
		final JSONArray suggestions = dataObject.getJSONArray("questions");
		//Logger.info(tag,"List size ====> " + suggestions.size());
		final List<Question> questionsList = new ArrayList<Question>();
		for (int i=0; i<suggestions.length(); i++) {
		    final JSONObject questionObject = (JSONObject)suggestions.get(i);
		    Logger.info(tag, "Question Object ===> " + questionObject);
		    final String questionType = questionObject.getString("type");
		    Logger.info(tag,"Question Type ===> " + questionType);
		    if(questionType.equalsIgnoreCase(QuestionType.TRUE_OR_FALSE_QUESTION.name())){
			final TrueOrFalseQuestion trueFalseQuestion = convertToTrueOrFalseQuestion(questionObject);
			questionsList.add(trueFalseQuestion);
			Logger.info(tag,"TRUE_OR_FALSE_QUESTION ===> " + trueFalseQuestion);
		    }else if(questionType.equalsIgnoreCase(QuestionType.MULTIPLECHOICE_QUESTION.name())){
			final MultipleChoiceQuestion multipleChoiceQuestion = convertToMultipleChoiceQuestion(questionObject);
			questionsList.add(multipleChoiceQuestion);
			Logger.info(tag,"MULTIPLECHOICE_QUESTION ===> " + multipleChoiceQuestion);
		    }else if(questionType.equalsIgnoreCase(QuestionType.FILL_IN_THE_BLANK_QUESTION.name())){
			final FillInTheBlankQuestion fillInTheBlankQuestion = convertToFillInTheBlankQuestion(questionObject);
			questionsList.add(fillInTheBlankQuestion);
			Logger.info(tag,"FILL_IN_THE_BLANK_QUESTION ===> " + fillInTheBlankQuestion);
		    }else if(questionType.equalsIgnoreCase(QuestionType.LONG_ANSWER_QUESTION.name())){
			final LongAnswerQuestion longAnswerQuestion = convertToLongAnswerQuestion(questionObject);
			questionsList.add(longAnswerQuestion);
			Logger.info(tag,"LONG_ANSWER_QUESTION ===> " + longAnswerQuestion);
		    }else if(questionType.equalsIgnoreCase(QuestionType.SHORT_ANSWER_QUESTION.name())){
			final ShortAnswerQuestion shortAnswerQuestion = convertToShortAnswerQuestion(questionObject);
			questionsList.add(shortAnswerQuestion);
			Logger.info(tag,"SHORT_ANSWER_QUESTION ===> " + shortAnswerQuestion);
		    }else if(questionType.equalsIgnoreCase(QuestionType.ORDERING_QUESTION.name())){
			final OrderingQuestion orderingQuestion = convertToOrderingQuestion(questionObject);
			questionsList.add(orderingQuestion);
			Logger.info(tag,"ORDERING_QUESTION ===> " + orderingQuestion);
		    }else if(questionType.equalsIgnoreCase(QuestionType.MATCH_THE_FOLLOWING_QUESTION.name())){
			final MatchTheFollowingQuestion matchTheFollowingQuestion = convertToOMatchTheFollowingQuestion(questionObject);
			questionsList.add(matchTheFollowingQuestion);
			Logger.info(tag,"ORDERING_QUESTION ===> " + matchTheFollowingQuestion);
		    }
		}
		Logger.info(tag,"QuestionsList ===> " + questionsList);
		questionsList_FINAL.addAll(questionsList);
	    }
	    return questionsList_FINAL ;
    }
}
