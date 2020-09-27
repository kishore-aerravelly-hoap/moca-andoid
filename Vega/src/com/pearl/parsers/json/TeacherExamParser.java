package com.pearl.parsers.json;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.pearl.logger.Logger;
import com.pearl.user.teacher.examination.ServerExam;
import com.pearl.user.teacher.examination.ServerExamDetails;
import com.pearl.user.teacher.examination.ServerExamDetailsList;

// TODO: Auto-generated Javadoc
/**
 * The Class TeacherExamParser.
 */
public class TeacherExamParser {

    /** The Constant tag. */
    public static final String tag = "TeacherExamParser";

    /**
     * Gets the exam.
     *
     * @param content the content
     * @return the exam
     */
    public static void getExam(String content){

    }

    /**
     * Gets the exam from string.
     *
     * @param content the content
     * @return the exam from string
     */
    public static ServerExam getExamFromString(String content){
	Logger.warn("tag", "TeacherExamParser - json content is:" + content);
	final Gson gson = new Gson();
	ServerExam serverExam = new ServerExam();
	final JsonReader reader = new JsonReader(new StringReader(content));
	reader.setLenient(true);
	serverExam = gson.fromJson(reader, ServerExam.class);
	Logger.warn(tag, "TeacherExamParser - server exam object after parsing is:"+serverExam);
	return serverExam;
    }
    
    /**
     * Gets the exams list.
     *
     * @param content the content
     * @return the exams list
     * @throws JsonProcessingException the json processing exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static List<ServerExamDetails> getExamsList(String content) throws JsonProcessingException, IOException{
	Logger.warn(tag, "Content for parsing is:"+content);
	final ObjectMapper mapper = new ObjectMapper();

	//content = "{\"serverExamDetailsList\":" + content + "}";

	final ObjectReader reader = mapper.reader(ServerExamDetailsList.class);

	final ServerExamDetailsList examDetailsList = reader.readValue(content);

	return examDetailsList.getServerExamDetailsList();
    }

    /**
     * Parses the json to sever exam object.
     *
     * @param jsonString the json string
     * @return the server exam
     */
    public static ServerExam parseJsonToSeverExamObject(String jsonString) {
	final ServerExam serverExam= new ServerExam();
	try{
	    final JSONObject dataObject = new JSONObject(jsonString);
	    serverExam.setTestAction(dataObject.getString("testAction"));
	    if(!dataObject.getString("exam").equals(null) && !dataObject.getString("exam").equals("null")){
		final JSONObject examObject = new JSONObject(dataObject.getString("exam"));
		serverExam.setExam(ExamParserHelper.convertToExamObject(examObject));
	    }
	    serverExam.setOpenBooks1(dataObject.getString("openBooks1"));
	    if(dataObject.has("openBooksList")){
		List<String> openBooksList = new LinkedList<String>();
		if(dataObject.getString("openBooksList") != "" && dataObject.getString("openBooksList") != "null"){
		    final JSONArray openBooksArray = dataObject.getJSONArray("openBooksList");
		    openBooksList = ExamParserHelper.convertToHintAnswers(openBooksArray);
		}
		serverExam.setOpenBooksList(openBooksList);		
	    }
	    if(dataObject.has("actions")){
		List<String> actionsList = new LinkedList<String>();
		if(dataObject.getString("actions") != "" && dataObject.getString("actions") != "null"){
		    final JSONArray actionsArray = dataObject.getJSONArray("actions");
		    actionsList = ExamParserHelper.convertToHintAnswers(actionsArray);					
		}
		serverExam.setActions(actionsList);
	    }
	    if(dataObject.has("studentsList")){
		List<String> studentsList = new LinkedList<String>();
		if(dataObject.getString("studentsList") != "" && dataObject.getString("studentsList") != "null"){
		    final JSONArray studentsArray = dataObject.getJSONArray("studentsList");
		    studentsList = ExamParserHelper.convertToHintAnswers(studentsArray);
		}
		serverExam.setStudentsList(studentsList);
	    }
	    if(dataObject.has("studentIdList")){
		List<String> StudentsIdList = new LinkedList<String>();
		if(dataObject.getString("studentIdList") != "" && dataObject.getString("studentIdList") != "null"){
		    final JSONArray StudentsidArray = dataObject.getJSONArray("studentIdList");
		    StudentsIdList = ExamParserHelper.convertToHintAnswers(StudentsidArray);
		}
		serverExam.setStudentIdList(StudentsIdList);
	    }

	    serverExam.getExam().setOpenBooks(serverExam.getOpenBooksList());
	}catch(final Exception e){
	    e.printStackTrace();
	}

	return serverExam;
    }
    
    /**
     * Parses the json to sever exam details object.
     *
     * @param jsonString the json string
     * @return the server exam details
     */
    public static ServerExamDetails parseJsonToSeverExamDetailsObject(String jsonString) {
	ServerExamDetails serverExamDetails= new ServerExamDetails();
	JSONObject dataObject;
	try {
	    dataObject = new JSONObject(jsonString);
	    final JSONObject examObject = new JSONObject(dataObject.getString("exam"));
	    serverExamDetails=ExamParserHelper.convertToExamDetailsObject(examObject);

	} catch (final JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return serverExamDetails;
    }
    /*public static ServerExamDetails parseJsonToSeverExamDetailsObject(String content){
		JSONObject dataObject;
		ServerExamDetails se = new ServerExamDetails();

		List<ServerExamDetails> serverExamDetailsList = new LinkedList<ServerExamDetails>();
		if(dataObject.getString("examDetails") != "" && dataObject.getString("examDetails") != "null"){
			JSONArray actionsArray = dataObject.getJSONArray("examDetails");
			serverExamDetailsList = ExamParserHelper.convertToHintAnswers(actionsArray);					
		}
		serverExam.setActions(actionsList);

		try {
			dataObject = new JSONObject(content);
			JSONObject examDetailsObject = new JSONObject(dataObject.getString("serverExamDetails"));
			se =  ExamParserHelper.convertToServerExamDetails(examDetailsObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return se;
	}*/

}

