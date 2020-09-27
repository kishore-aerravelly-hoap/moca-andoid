package com.pearl.examination.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.pearl.logger.Logger;
import com.pearl.user.teacher.examination.Answer;
import com.pearl.user.teacher.examination.AnswerChoice;
import com.pearl.user.teacher.examination.MultiChoiceQuestionAnswer;
import com.pearl.user.teacher.examination.ServerQuestion;
import com.pearl.user.teacher.examination.answertype.LongAnswer;
import com.pearl.user.teacher.examination.answertype.MultipleChoiceAnswer;
import com.pearl.user.teacher.examination.answertype.ShortAnswer;
import com.pearl.user.teacher.examination.answertype.TrueOrFalseAnswer;
import com.pearl.user.teacher.examination.questiontype.FillInTheBlankQuestion;
import com.pearl.user.teacher.examination.questiontype.LongAnswerQuestion;
import com.pearl.user.teacher.examination.questiontype.MatchTheFollowingQuestion;
import com.pearl.user.teacher.examination.questiontype.MultipleChoiceQuestion;
import com.pearl.user.teacher.examination.questiontype.OrderingQuestion;
import com.pearl.user.teacher.examination.questiontype.ShortAnswerQuestion;
import com.pearl.user.teacher.examination.questiontype.TrueOrFalseQuestion;
import com.pearl.util.SeedGenerator;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamHelper.
 */
public class ExamHelper {

    /** The Constant tag. */
    private static final String tag = "ExamHelper";

    /**
     * Gets the true or flse question.
     *
     * @param serverQuestion the server question
     * @return the true or flse question
     */
    public TrueOrFalseQuestion getTrueOrFlseQuestion(ServerQuestion serverQuestion){

	final TrueOrFalseQuestion tfqQuestion = new TrueOrFalseQuestion();
	tfqQuestion.setQuestion(serverQuestion.getQuestion());
	tfqQuestion.setId(SeedGenerator.getNextSeed());
	tfqQuestion.setBookName(serverQuestion.getBookName());
	tfqQuestion.setChapterName(serverQuestion.getChapterName());
	tfqQuestion.setDescription(serverQuestion.getDescription());
	tfqQuestion.setMarksAlloted(serverQuestion.getMarksAlloted());
	tfqQuestion.setQuestionOrderNo(serverQuestion.getQuestionOrderNo());
	tfqQuestion.setType(serverQuestion.getType());
	final TrueOrFalseAnswer tfqAnswer = new TrueOrFalseAnswer();
	boolean flag = false;
	if(serverQuestion.getTruefalseAnswer()){
	    flag = true;
	}
	final List<AnswerChoice> answerChoiceList = new ArrayList<AnswerChoice>();
	final AnswerChoice answerChoice1 = new AnswerChoice();
	answerChoice1.setTitle("TRUE");
	answerChoice1.setId(1);
	answerChoice1.setPosition(1);
	if(flag == true)
	    answerChoice1.setTeacherSelected(flag);
	answerChoiceList.add(answerChoice1);

	final AnswerChoice answerChoice2 = new AnswerChoice();
	answerChoice2.setId(2);
	answerChoice2.setTitle("FALSE");
	answerChoice2.setPosition(2);
	if(flag == false)
	    answerChoice2.setTeacherSelected(!flag);
	answerChoiceList.add(answerChoice2);

	tfqQuestion.setChoices(answerChoiceList);
	tfqAnswer.setChoices(answerChoiceList);
	tfqQuestion.setCorrectAnswer(tfqAnswer);
	tfqQuestion.setChoices(answerChoiceList);
	return tfqQuestion;
    }

    /**
     * Gets the short answer question.
     *
     * @param serverQuestion the server question
     * @return the short answer question
     */
    public ShortAnswerQuestion getShortAnswerQuestion(ServerQuestion serverQuestion){
	final ShortAnswerQuestion saq = new ShortAnswerQuestion();
	saq.setBookName(serverQuestion.getBookName());
	saq.setChapterName(serverQuestion.getChapterName());
	saq.setId(SeedGenerator.getNextSeed());
	saq.setMarksAlloted(serverQuestion.getMarksAlloted());
	saq.setQuestion(serverQuestion.getQuestion());
	saq.setQuestionOrderNo(serverQuestion.getQuestionOrderNo());
	saq.setType(serverQuestion.getType());

	final ShortAnswer answer = new ShortAnswer();
	if(serverQuestion.getShortAnswer() == null 
		|| serverQuestion.getShortAnswer().equals("")){
	    answer.setAnswerString("");
	}else
	    answer.setAnswerString(serverQuestion.getShortAnswer().toString());
	saq.setCorrectAnswer(answer);

	return saq;
    }

    /**
     * Gets the long answer question.
     *
     * @param serverQuestion the server question
     * @return the long answer question
     */
    public LongAnswerQuestion getLongAnswerQuestion(ServerQuestion serverQuestion){
	final LongAnswerQuestion laq = new LongAnswerQuestion();
	laq.setBookName(serverQuestion.getBookName());
	laq.setChapterName(serverQuestion.getChapterName());
	laq.setId(SeedGenerator.getNextSeed());
	laq.setMarksAlloted(serverQuestion.getMarksAlloted());
	laq.setQuestion(serverQuestion.getQuestion());
	laq.setQuestionOrderNo(serverQuestion.getQuestionOrderNo());
	laq.setType(serverQuestion.getType());

	final LongAnswer answer = new LongAnswer();
	if(serverQuestion.getLongAnswer()== null 
		|| serverQuestion.getLongAnswer().equals("")){
	    answer.setAnswerString("");
	}
	else
	    answer.setAnswerString(serverQuestion.getLongAnswer().toString());
	laq.setCorrectAnswer(answer);

	return laq;
    }

    /**
     * Gets the fill in the blank question.
     *
     * @param serverQuestion the server question
     * @return the fill in the blank question
     */
    public FillInTheBlankQuestion getFillInTheBlankQuestion(ServerQuestion serverQuestion){
	final FillInTheBlankQuestion fib = new FillInTheBlankQuestion();
	final StringBuffer question = new StringBuffer();
	final List<AnswerChoice> answerChoiceList = new ArrayList<AnswerChoice>();
	final List<String> answersList = new ArrayList<String>();
	final Answer answer = new Answer();

	fib.setBookName(serverQuestion.getBookName());
	fib.setChapterName(serverQuestion.getChapterName());
	fib.setId(SeedGenerator.getNextSeed());
	fib.setType(serverQuestion.getType().toUpperCase());
	fib.setQuestionOrderNo(serverQuestion.getQuestionOrderNo());
	fib.setMarksAlloted(serverQuestion.getMarksAlloted());
	final String tempQuestion = serverQuestion.getQuestion();
	final String[] splittedString = tempQuestion.split("@@");
	int count = 1, blankCount = 1;
	AnswerChoice answerChoice = null;
	for(final String string : splittedString){
	    answerChoice = new AnswerChoice();
	    Logger.info(tag, "FIB HELPER - string after splitting is:"+string);
	    if(count%2 == 0){
		question.append("___");
		answerChoice.setTitle(string.trim());
		answerChoice.setId(blankCount);
		answersList.add(string.trim());
		answerChoice.setTeacherPosition(blankCount);
		answerChoiceList.add(answerChoice);
		blankCount++;
	    }else{
		question.append(string);
	    }
	    count++;
	}
	answer.setChoices(answerChoiceList);
	fib.setQuestion(question.toString().trim());
	fib.setCorrectAnswer(answer);
	fib.setAnswer(answer);
	fib.setAnswers(answersList);
	fib.setTotalBlanks(blankCount);
	fib.setHintAnswers(answersList);
	return fib;
    }

    /**
     * Convert to match the following question.
     *
     * @param serverQuestion the server question
     * @return the match the following question
     */
    public MatchTheFollowingQuestion convertToMatchTheFollowingQuestion(ServerQuestion serverQuestion)
    {
	final MatchTheFollowingQuestion matchQuestion=new MatchTheFollowingQuestion();
	final List<AnswerChoice> answerChoicesList=new ArrayList<AnswerChoice>();
	try{
	    matchQuestion.setId(SeedGenerator.getNextSeed());
	    matchQuestion.setType(serverQuestion.getType());
	    matchQuestion.setBookName(serverQuestion.getBookName());
	    matchQuestion.setChapterName(serverQuestion.getChapterName());
	    matchQuestion.setMarksAlloted(serverQuestion.getMarksAlloted());
	    matchQuestion.setQuestionOrderNo(serverQuestion.getQuestionOrderNo());
	    //matchQuestion.setQuestion(ExamHelper.handleSpecialCharacters(questionObj.getQuestion().trim()));
	    matchQuestion.setQuestion(serverQuestion.getQuestion().trim());
	    final List<MultiChoiceQuestionAnswer> multiChoiceQnA=serverQuestion.getMultiChoiceQnA();
	    final Answer answer=new Answer();
	    AnswerChoice orderAnsChoice=null;
	    if(multiChoiceQnA!=null){
		final Iterator<MultiChoiceQuestionAnswer> iterator=multiChoiceQnA.iterator();
		while(iterator.hasNext())
		{
		    final MultiChoiceQuestionAnswer multiChoice=iterator.next();

		    if(multiChoice.getCheckedValue()!=null ||
			    multiChoice.getAnswerDes()!=null ||
			    multiChoice.getCheckedString()!=null ||
			    multiChoice.getCheckedText()!=null){
			orderAnsChoice=null;
			orderAnsChoice=new AnswerChoice();

			String checkedvalue=multiChoice.getCheckedValue();
			if(checkedvalue.contains(","))
			{
			    final String b[]=checkedvalue.split(",");
			    for (final String string : b) {
				if(string!=null && !string.equals("") && string.length() > 0){
				    checkedvalue=string.trim();
				    break;
				}
			    }
			}
			String ansDes=multiChoice.getAnswerDes();
			if(ansDes.contains(","))
			{
			    final String a[]=ansDes.split(",");
			    for (final String string : a) {
				if(string!=null && !string.equals("") && string.length() > 0){
				    ansDes=string.trim();
				    break;
				}
			    }
			}
			final int position=multiChoice.getPosition();
			orderAnsChoice.setMatch_FieldOne(checkedvalue);
			orderAnsChoice.setTitle(ansDes);
			orderAnsChoice.setId(position);
			orderAnsChoice.setMatch_FieldThree(multiChoice.getCheckedString());
			orderAnsChoice.setMatch_FieldFour(multiChoice.getCheckedText());
			answerChoicesList.add(orderAnsChoice);
		    }
		}
	    }
	    matchQuestion.setChoices(answerChoicesList);
	    answer.setChoices(answerChoicesList);
	    matchQuestion.setCorrectAnswer(answer);
	    orderAnsChoice=null;

	}catch(final Exception e)
	{
	    e.printStackTrace();
	}

	return matchQuestion;
    }


    /**
     * Convert to multiple choice question.
     *
     * @param serverQuestion the server question
     * @return the multiple choice question
     */
    public MultipleChoiceQuestion convertToMultipleChoiceQuestion(ServerQuestion serverQuestion){
	final MultipleChoiceQuestion question = new MultipleChoiceQuestion();
	try{
	    question.setId(SeedGenerator.getNextSeed());
	    question.setType(serverQuestion.getType());
	    question.setBookName(serverQuestion.getBookName());
	    question.setChapterName(serverQuestion.getChapterName());
	    question.setQuestionOrderNo(serverQuestion.getQuestionOrderNo());
	    //	question.setQuestion(ExamHelper.handleSpecialCharacters(serverQuestion.getQuestion().trim()));
	    question.setQuestion(serverQuestion.getQuestion().trim());
	    question.setMarksAlloted(serverQuestion.getMarksAlloted());
	    question.setCorrect(serverQuestion.getHasMultipleAnswers());
	    question.setHasMultipleAnswers(serverQuestion.getHasMultipleAnswers());
	    final List<AnswerChoice> answerChoiceList = new ArrayList<AnswerChoice>();
	    final List<AnswerChoice> answerChoiceList1 = new ArrayList<AnswerChoice>();
	    final List<MultiChoiceQuestionAnswer> multiChoiceQnA=serverQuestion.getMultiChoiceQnA();
	    AnswerChoice answerChoice=null;
	    AnswerChoice answerChoice1=null;
	    final MultipleChoiceAnswer answer = new MultipleChoiceAnswer(); 
	    //	question.getChoices().get(Integer.parseInt(serverQuestion.getCheckValues())).setTeacherSelected(true);
	    if(multiChoiceQnA!=null)
	    {
		final Iterator<MultiChoiceQuestionAnswer> iterator=multiChoiceQnA.listIterator();
		while(iterator.hasNext())
		{
		    boolean flag=false;
		    final MultiChoiceQuestionAnswer multiChoiceQuesndAns=iterator.next();

		    if(multiChoiceQuesndAns.getCheckedValue()!=null || multiChoiceQuesndAns.getAnswerDes()!=null) {

			answerChoice=new AnswerChoice();
			answerChoice1=new AnswerChoice();
			final int position=multiChoiceQuesndAns.getPosition();
			String ckValue;

			if(question.isCorrect())
			{
			    ckValue=multiChoiceQuesndAns.getCheckedValue();
			    if(ckValue != null && ckValue.contains(",")){
				final String[] str =StringUtils.split(ckValue, ",");
				for (final String string : str) {
				    if(string.trim().length() > 0){
					ckValue = string.trim();
				    }
				}
			    }
			    if(ckValue != null && ckValue.trim().equalsIgnoreCase("on"))
				flag=true;

			}else{
			    ckValue=serverQuestion.getCheckValues();
			    if(Integer.parseInt(ckValue.trim()) == position){
				flag=true;
			    }
			}
			String ans = multiChoiceQuesndAns.getAnswerDes();
			if(ans.contains(",")){
			    final String a[]=ans.split(",");
			    for (final String string : a) {
				if(string!=null && !string.equals("") && string.length() > 0){
				    ans=string.trim();
				    break;
				}
			    }
			}
			answerChoice.setTitle(ans);
			answerChoice.setDescription(multiChoiceQuesndAns.getAnswerDes());
			answerChoice.setId(position);
			answerChoice.setTeacherPosition(position);
			answerChoice.setTeacherSelected(flag);
			answerChoiceList.add(answerChoice);

			answerChoice1.setTitle(ans);
			answerChoice1.setDescription(multiChoiceQuesndAns.getAnswerDes());
			answerChoice1.setId(position);
			answerChoice1.setTeacherPosition(position);
			answerChoice1.setTeacherSelected(false);
			answerChoiceList1.add(answerChoice1);
			answerChoice=null;
			answerChoice1=null;
		    }
		}
		Logger.info(tag, "answerChoiceList1 ====> " + answerChoiceList1);
		Logger.info(tag, "answerChoiceList ====> " + answerChoiceList);
		question.setChoices(answerChoiceList1);
		int position1 = (Integer.parseInt(serverQuestion.getCheckValues().toString()));
		question.getChoices().get(position1 - 1 ).setTeacherSelected(true);
		answer.setChoices(answerChoiceList);

		question.setCorrectAnswer(answer);
	    }		
	}catch(final Exception e)
	{
	    e.printStackTrace();
	}
	return question;

    }

    /**
     * Convert to ordering question.
     *
     * @param serverQuestion the server question
     * @return the ordering question
     */
    public OrderingQuestion convertToOrderingQuestion(ServerQuestion serverQuestion)
    {
	final OrderingQuestion orderingQuestion=new OrderingQuestion();
	final List<AnswerChoice> answerChoiceList = new ArrayList<AnswerChoice>();
	final List<AnswerChoice> answerChoiceList1 = new ArrayList<AnswerChoice>();
	try{
	    orderingQuestion.setId(SeedGenerator.getNextSeed());
	    orderingQuestion.setType(serverQuestion.getType());
	    orderingQuestion.setBookName(serverQuestion.getBookName());
	    orderingQuestion.setChapterName(serverQuestion.getChapterName());
	    //orderingQuestion.setQuestion(ExamHelper.handleSpecialCharacters(serverQuestion.getQuestion().trim()));
	    orderingQuestion.setQuestion(serverQuestion.getQuestion().trim());
	    orderingQuestion.setQuestionOrderNo(serverQuestion.getQuestionOrderNo());
	    orderingQuestion.setMarksAlloted(serverQuestion.getMarksAlloted());
	    final List<MultiChoiceQuestionAnswer> multiChoiceQnA = serverQuestion.getMultiChoiceQnA();
	    final Answer answer=new Answer();
	    AnswerChoice orderAnsChoice=null;
	    if(multiChoiceQnA!=null){
		final Iterator<MultiChoiceQuestionAnswer> iterator=multiChoiceQnA.iterator();
		while(iterator.hasNext())
		{
		    final MultiChoiceQuestionAnswer multiChoice=iterator.next();
		    if(multiChoice.getCheckedValue()!=null && multiChoice.getAnswerDes()!=null) {
			orderAnsChoice=new AnswerChoice();
			String value=multiChoice.getCheckedValue();
			if(value.contains(",")){
			    final String a[]=value.split(",");
			    for (final String string : a) {
				if(string!=null && !string.equals("") &&!string.trim().equals("")  && string.trim().length() > 0){
				    value=string.trim();
				    break;
				}
			    }
			}
			// orderAnsChoice.setMatch_FieldOne(value);
			orderAnsChoice.setTeacherPosition(Integer.parseInt(value));
			String ans = multiChoice.getAnswerDes();
			if(ans.contains(",")){
			    final String a[]=ans.split(",");
			    for (final String string : a) {
				if(string!=null && !string.equals("") && string.length() > 0){
				    ans=string.trim();
				    break;
				}
			    }
			}
			orderAnsChoice.setTitle(ans);
			orderAnsChoice.setDescription("");
			final int position=multiChoice.getPosition();

			if(multiChoice.getCheckedValue()!=null)
			    orderAnsChoice.setTeacherSelected(true);
			orderAnsChoice.setId(position);
			answerChoiceList.add(orderAnsChoice);
			orderAnsChoice.setTeacherSelected(false);
			answerChoiceList1.add(orderAnsChoice);
			orderAnsChoice=null;
		    }
		}
	    }
	    answer.setChoices(answerChoiceList);
	    orderingQuestion.setCorrectAnswer(answer);
	    orderingQuestion.setChoices(answerChoiceList1);
	}catch(final Exception e)
	{
	    e.printStackTrace();
	}

	return orderingQuestion;
    }
}
