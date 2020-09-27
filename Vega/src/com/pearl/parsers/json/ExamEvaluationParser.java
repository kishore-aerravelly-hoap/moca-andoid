package com.pearl.parsers.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

import com.pearl.chat.server.response.BaseResponse;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamEvaluationParser.
 */
public class ExamEvaluationParser {
    
    /** The Constant tag. */
    private static final String tag = "ExamEvaluationParser";

    /**
     * Parses the evaluation.
     *
     * @param content the content
     * @return the object
     */
    public static Object parseEvaluation(String content){
	BaseResponse response = new BaseResponse();
	final ObjectMapper mapper = new ObjectMapper();
	try{
	    response = mapper.readValue(content, BaseResponse.class);
	}catch(final JsonParseException e){
	    Logger.error(tag, e);
	}catch (final IOException e) {
	    Logger.error(tag, e);
	}
	return response.getData();
    }
}
