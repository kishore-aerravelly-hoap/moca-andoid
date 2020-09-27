package com.pearl.parsers.json;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import com.pearl.logger.Logger;
import com.pearl.student.StudentsList;
import com.pearl.ui.models.Student;

// TODO: Auto-generated Javadoc
/**
 * The Class StudentDetailsParser.
 */
public class StudentDetailsParser {

    /** The Constant tag. */
    public static final String tag="StudentDetialsParser";

    /**
     * Gets the student details.
     *
     * @param content the content
     * @return the student details
     */
    public static Student getStudentDetails(String content){

	Student student = null;
	final ObjectMapper mapper = new ObjectMapper();
	try{
	    student = mapper.readValue(content, Student.class);
	}catch(final JsonParseException e){
	    Logger.error(tag, e);
	}catch (final IOException e) { 
	    Logger.error(tag, e);
	}
	return student;
    }

    /**
     * Gets the students list.
     *
     * @param content the content
     * @return the students list
     * @throws JsonProcessingException the json processing exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static List<Student> getStudentsList(String content)
	    throws JsonProcessingException, IOException {
	final ObjectMapper mapper = new ObjectMapper();
	content = "{\"studentDetailsList\":" + content + "}";
	Logger.info(tag,"studentsList content after concatening is:-------------"
		+ content);
	final ObjectReader reader = mapper.reader(StudentsList.class);

	final StudentsList studentsList = reader.readValue(content);
	return studentsList.getStudentDetailsList();

    }

}
