package com.pearl.parsers.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.pearl.application.ApplicationData;
import com.pearl.logger.Logger;
import com.pearl.ui.models.BaseResponse1;

// TODO: Auto-generated Javadoc
/**
 * The Class AnswerSheetParser.
 */
public class AnswerSheetParser {

    /** The Constant tag. */
    private static final String tag = "AnswerSheetParser";

    /**
     * Gets the answer sheet.
     *
     * @param filePath the file path
     * @return the answer sheet
     */
    public static List<String> getAnswerSheet(String filePath) {
	String content = null;
	BaseResponse1 baseResponse = null;
	List<String> data = new ArrayList<String>();
	try {
	    content = ApplicationData.readFile(filePath);
	} catch (final IOException e) {
	    e.printStackTrace();
	}
	final ObjectMapper mapper = new ObjectMapper();
	if (content != null && content != "") {
	    try {
		baseResponse = mapper.readValue(content, BaseResponse1.class);
		data = (List<String>) baseResponse.getData();
	    } catch (final JsonParseException e) {
		Logger.error(tag, e);
	    } catch (final JsonMappingException e) {
		Logger.error(tag, e);
	    } catch (final IOException e) {
		Logger.error(tag, e);
	    } finally {
		Logger.warn(tag, "in finally base response object is:"
			+ baseResponse.getData());
	    }
	} else {
	    Logger.info("", "content to parse is null");
	}
	return data;
    }
}
