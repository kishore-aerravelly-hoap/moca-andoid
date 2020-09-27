package com.pearl.parsers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import com.pearl.application.ApplicationData;
import com.pearl.examination.Exam;
import com.pearl.examination.ExamDetails;
import com.pearl.examination.ExamDetailsList;
import com.pearl.examination.ExistingQuestionList;
import com.pearl.logger.Logger;
import com.pearl.parsers.json.ExamParser;
import com.pearl.user.teacher.examination.ExistingQuestion;

// TODO: Auto-generated Javadoc
/**
 * The Class JSONParser.
 */
public class JSONParser {
    
    /**
     * Gets the exam.
     *
     * @param jsonFile the json file
     * @return the exam
     * @throws Exception the exception
     */
    public static Exam getExam(File jsonFile) throws Exception {
	return ExamParser.getExam(jsonFile);
    }

    /**
     * Gets the exam.
     *
     * @param jsonFilePath the json file path
     * @return the exam
     * @throws Exception the exception
     */
    public static Exam getExam(String jsonFilePath) throws Exception {
	// XXX
	String content = "";

	/*
	 * content = "" + "{" + "\"examDetails\":{" + " \"examId\" : 2," +
	 * " \"title\" : \"HalfYearly\"," +
	 * " \"description\" : \"Half Yearly Examinations\","+
	 * " \"subject\" : \"Christian Living\"," +
	 * " \"startTime\" : 1328860704610," + " \"endTime\" : 1329860704610," +
	 * " \"duration\" : 123456,"+ " \"state\" : \"submitted\"," +
	 * " \"resultId\" : 21,"+ " \"retakeable\" : true," +
	 * " \"maxPoints\" : 18,"+ " \"minPoints\" : 6," +
	 * " \"negativePoints\" : false,"+ " \"allowedBookIds\" : [" + "		1,3,5"
	 * + "	]" + "}," + " \"studentId\" : 21,"+
	 * " \"questionPaperSet\" : \"four\","+ " \"questions\" : [" + "{" +
	 * "\"type\" : \"short_answer_question\"," + //true_or_false_question
	 * ordering_question "\"id\" : 1," + "\"marksAlloted\" : 4," +
	 * "\"questionOrderNo\" : 1," + "\"durationInSecs\" : 34567," +
	 * "\"question\" : \"saq question here\"," +
	 * "\"description\" : \"saq description here\"," +
	 * "\"hint\" : \"saq hint here\"," + "\"answer\" : {" +
	 * "\"answerString\" : \"student answer here if already answered\"" +
	 * "}" + "}," + "{" + "\"type\" : \"true_or_false_question\"," + //
	 * ordering_question "\"id\" : 2," + "\"marksAlloted\" : 2," +
	 * "\"questionOrderNo\" : 2," + "\"durationInSecs\" : 0," +
	 * "\"question\" : \"tf question here\"," +
	 * "\"description\" : \"tf description here\"," +
	 * "\"hint\" : \"tf hint here\"," + "\"choices\" : [" + "{" +
	 * "\"id\" : 1," + "\"title\" : \"true\"," +
	 * "\"description\" : \"choice 1 description\"," + "\"position\" : 1," +
	 * "\"isSelected\":false" + "}," + "{" + "\"id\" : 2," +
	 * "\"title\" : \"false\"," +
	 * "\"description\" : \"choice 2 description\"," + "\"position\" : 2," +
	 * "\"isSelected\":true" + "}" + "]" + "}," + "{" +
	 * "\"type\" : \"fill_in_the_blank_question\"," + // ordering_question
	 * "\"id\" : 2," + "\"marksAlloted\" : 2," + "\"questionOrderNo\" : 2,"
	 * + "\"durationInSecs\" : 0," +
	 * "\"question\" : \"fb question answer1 ___ here, anser2 ___ here , anser3 ___ here , anser4 ___ here and answer5 ___ here\","
	 * + "\"description\" : \"fb description here\"," +
	 * "\"hint\" : \"fb hint here\"" + "}," + "{" +
	 * "\"type\" : \"ordering_question\"," + "\"id\" : 3," +
	 * "\"marksAlloted\" : 5," + "\"questionOrderNo\" : 3," +
	 * "\"durationInSecs\" : 0," + "\"question\" : \"oq question here\"," +
	 * "\"description\" : \"oq description here\"," +
	 * "\"hint\" : \"oq hint here\"," + "\"choices\" : [" + "{" +
	 * "\"id\" : 1," + "\"title\" : \"choice 1 title\"," +
	 * "\"description\" : \"choice 1 description\"," + "\"position\" : 1," +
	 * "\"isSelected\":false" + "}," + "{" + "\"id\" : 2," +
	 * "\"title\" : \"choice 2 title\"," +
	 * "\"description\" : \"choice 2 description\"," + "\"position\" : 2," +
	 * "\"isSelected\":false" + "}," + "{" + "\"id\" : 3," +
	 * "\"title\" : \"choice 3 title\"," +
	 * "\"description\" : \"choice 3 description\"," + "\"position\" : 3," +
	 * "\"isSelected\":false" + "}" + "]" + "}" + "]" + "}";
	 */

	// return ExamParser.getExam(content);

	// TODO uncomment above line
	content = ApplicationData.readFile(jsonFilePath);

	// return ExamParserHelper.parseJsonToExamObject(content);
	return ExamParser.getExam(content);
    }

    /**
     * Exams List parser with single file path as parameter.
     *
     * @param jsonFile the json file
     * @return the exams list
     * @throws JsonProcessingException the json processing exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static ArrayList<ExamDetails> getExamsList(File jsonFile)
	    throws JsonProcessingException, IOException {
	final ObjectMapper mapper = new ObjectMapper();
	// we'll be reading instances of MyBean
	final ObjectReader reader = mapper.reader(ExamDetailsList.class);

	// ExamDetailsList examDetailsList = reader.readValue(content2);
	final ExamDetailsList examDetailsList = reader.readValue(jsonFile);

	// Logger.info("JSON PARSER", "exam details list " +
	// examDetailsList.getExamDetailsList().get(0).getTitle());

	return examDetailsList.getExamDetailsList();
    }

    /**
     * Exams List parser with a string as parameters.
     *
     * @param content the content
     * @return Array List of Exam Details
     * @throws JsonProcessingException the json processing exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    /*
     * public static ArrayList<ExamDetails> getExamsList(String content) throws
     * JsonProcessingException, IOException { ObjectMapper mapper = new
     * ObjectMapper(); ExamDetailsList examDetailsList = null; // we'll be
     * reading instances of MyBean Logger.info("JSON content",
     * "JSON CONTENT IS======"+content); //BaseResponse response =
     * mapper.readValue(content, BaseResponse.class); //if(response.getData() !=
     * null || response.getData() != ""){ if(content != null || content != "") {
     * //String responseData = response.getData().toString(); ObjectReader
     * reader = mapper.reader(ExamDetailsList.class);
     * 
     * 
     * content = "{\"examDetailsList\":"+content+"}"; Logger.warn("JSONPaerser",
     * "content after appending is:"+content);
     * 
     * examDetailsList = reader.readValue("{\"examDetailsList\":"+content+"}");
     * 
     * }
     */
    public static ArrayList<ExamDetails> getExamsList(String content)
	    throws JsonProcessingException, IOException {
	final ObjectMapper mapper = new ObjectMapper();
	// we'll be reading instances of MyBean

	content = "{\"examDetailsList\":" + content + "}";
	// Logger.info("JSON PARSER","examlist content after concatening is:-------------"
	// + content);
	final ObjectReader reader = mapper.reader(ExamDetailsList.class);

	final ExamDetailsList examDetailsList = reader.readValue(content);
	return examDetailsList.getExamDetailsList();

    }
    
    /**
     * Gets the existing question list.
     *
     * @param content the content
     * @return the existing question list
     * @throws JsonProcessingException the json processing exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static List<ExistingQuestion> getExistingQuestionList(String content) throws JsonProcessingException, IOException{
	final ObjectMapper mapper=new ObjectMapper();
	
	content="{\"existingQuestionList\":"+content+"}";
	Logger.warn("parser", "content after concatenating----:"+content);
	final ObjectReader reader=mapper.reader(ExistingQuestionList.class);
	final ExistingQuestionList existingQuestionList=reader.readValue(content);
	return existingQuestionList.getExistingQuestionList();
    }
    
}