/**
 * 
 */
package com.pearl.parsers.json;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import com.pearl.logger.Logger;
import com.pearl.ui.models.TeacherTestComment;
import com.pearl.ui.models.TeacherTestCommentList;

// TODO: Auto-generated Javadoc
/**
 * The Class TeacherTestCommentParser.
 *
 * @author spasnoor
 */
public class TeacherTestCommentParser {
    
    /** The Constant tag. */
    public static final String tag="TeacherTestCommentParser";

    /**
     * Gets the teacher test comment.
     *
     * @param content the content
     * @return the teacher test comment
     */
    public static TeacherTestComment getTeacherTestComment(String content){

	TeacherTestComment teacherTestComment = null;
	final ObjectMapper mapper = new ObjectMapper();
	try{
	    teacherTestComment = mapper.readValue(content, TeacherTestComment.class);
	}catch(final JsonParseException e){
	    Logger.error(tag, e);
	}catch (final IOException e) { 
	    Logger.error(tag, e);
	}
	return teacherTestComment;
    }

    /**
     * Gets the teacher test comment list.
     *
     * @param content the content
     * @return the teacher test comment list
     * @throws JsonProcessingException the json processing exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static List<TeacherTestComment> getTeacherTestCommentList(String content)
	    throws JsonProcessingException, IOException {
	final ObjectMapper mapper = new ObjectMapper();
	Logger.info(tag,"studentsLis content after concatening is:-------------"
		+ content);
	final ObjectReader reader = mapper.reader(TeacherTestCommentList.class);

	final TeacherTestCommentList teacherTestCommentList = reader.readValue(content);
	return teacherTestCommentList.getList();

    }

}
