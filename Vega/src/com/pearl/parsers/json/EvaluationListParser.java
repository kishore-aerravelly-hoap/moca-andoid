package com.pearl.parsers.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

import com.pearl.chat.server.response.BaseResponse;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class EvaluationListParser.
 */
public class EvaluationListParser {
    
    /** The Constant tag. */
    public static final String tag = "EvaluationListParser";

    /**
     * Parses the evaluation list.
     *
     * @param content the content
     * @return the object
     */
    public static Object parseEvaluationList(String content){
	Object evaluationList = null;
	BaseResponse response = new BaseResponse();
	final ObjectMapper mapper = new ObjectMapper();
	try{
	    response = mapper.readValue(content, BaseResponse.class);
	    evaluationList = response.getData();
	}catch(final JsonParseException e){
	    Logger.error(tag, e);
	}catch (final IOException e) {
	    Logger.error(tag, e);
	}
	return evaluationList;
    }
}
