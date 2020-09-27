package com.pearl.parsers.json;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import com.pearl.teachers.TeachersList;
import com.pearl.ui.models.Teacher;

// TODO: Auto-generated Javadoc
/**
 * The Class TeacherDetailsParser.
 */
public class TeacherDetailsParser {


    /**
     * Gets the teacher details.
     *
     * @param content the content
     * @return the teacher details
     */
    public static Teacher getTeacherDetails(String content) {
	Teacher teacher=null;
	final ObjectMapper objMapper=new ObjectMapper();
	try {
	    teacher=objMapper.readValue(content,Teacher.class);
	} catch (final JsonParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (final JsonMappingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (final IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return teacher;
    }

    /**
     * Gets the teachers list.
     *
     * @param content the content
     * @return the teachers list
     */
    public static List<Teacher> getTeachersList(String content)
    {
	TeachersList teachersList=null;
	final ObjectMapper objMapper=new ObjectMapper();
	content = "{\"_teachersList\":" + content + "}";

	final ObjectReader objreader=objMapper.reader(TeachersList.class);
	try {
	    teachersList=objreader.readValue(content);
	} catch (final JsonProcessingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (final IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return teachersList.get_teachersList();
    }

    
}
