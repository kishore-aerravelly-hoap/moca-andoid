package com.pearl.parsers.json;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import com.pearl.application.ApplicationData;
import com.pearl.examination.ExamDetails;
import com.pearl.logger.Logger;
import com.pearl.ui.models.ExamListResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamListParser.
 */
public class ExamListParser {

    /**
     * Gets the exam file.
     *
     * @param examResponseJsonData the exam response json data
     * @param file the file
     * @return the exam file
     * @throws JsonParseException the json parse exception
     * @throws JsonMappingException the json mapping exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static ArrayList<ExamDetails> getExamFile(
	    String examResponseJsonData, String file)
		    throws JsonParseException, JsonMappingException, IOException {
	Logger.warn("ExamListParser", "exam response data"
		+ examResponseJsonData);
	final ObjectMapper mapper = new ObjectMapper();
	final ObjectReader reader = mapper.reader(ExamListResponse.class);
	ExamListResponse examListResponse = null;
	try {
	    examListResponse = reader.readValue(examResponseJsonData);
	} catch (final JsonProcessingException e) {
	    Logger.error("ExamListParser", e);
	} catch (final IOException e) {
	    Logger.error("ExamListParser", e);
	}
	ArrayList<ExamDetails> tempExamDetailsList = null;
	try {
	    tempExamDetailsList = examListResponse.getExamList();
	} catch (final Exception e) {
	    Logger.error("ExamListParser", e);
	}

	final ObjectMapper omp = new ObjectMapper();
	String examListJson = "";
	Logger.warn("", "examl ist after parsing is:"+tempExamDetailsList);
	try {
	    examListJson = omp.writeValueAsString(tempExamDetailsList);
	} catch (final JsonGenerationException e) {
	    Logger.error("ExamListParser", e);
	} catch (final JsonMappingException e) {
	    Logger.error("ExamListParser", e);
	} catch (final IOException e) {
	    Logger.error("ExamListParser", e);
	}

	ApplicationData.writeToFile(examListJson, file);
	return tempExamDetailsList;
    }
}
