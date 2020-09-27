package com.pearl.parsers.json;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.pearl.chat.server.response.BaseResponse;
import com.pearl.ui.models.ExamCategory;
import com.pearl.ui.models.ExamCategoryList;

// TODO: Auto-generated Javadoc
/**
 * The Class ExamCategoryParser.
 */
public class ExamCategoryParser {

	/**
	 * Gets the exam category.
	 *
	 * @param content the content
	 * @return the exam category
	 */
	public static List<ExamCategory> getExamCategory(String content){
		ObjectMapper om = new ObjectMapper();
		List<ExamCategory> examCategoryList = null;
		try {
			content = "{\"examCategoryList\":" +content + "}";
			ExamCategoryList list = om.readValue(content, ExamCategoryList.class);
			examCategoryList = list.getExamCategoryList();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return examCategoryList;
	}
	
	/**
	 * Parses the json.
	 *
	 * @param filePath the file path
	 * @return the string
	 * @throws JsonParseException the json parse exception
	 * @throws JsonMappingException the json mapping exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String ParseJson(String filePath) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper om =  new ObjectMapper();
		String content = "";
		BaseResponse response= om.readValue(new File(filePath), BaseResponse.class);
		if(response != null && (response.getData() != null || response.getData() != "") )
			content = response.getData().toString();
		return content;
	}

}
