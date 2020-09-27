package com.pearl.parsers.json;

import java.io.IOException;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import com.pearl.analytics.models.GradePassFailPercentage;
import com.pearl.analytics.models.GradePassFailPercentageList;
import com.pearl.analytics.models.PercentageList;
import com.pearl.analytics.models.StudentPercentage;
import com.pearl.analytics.models.StudentPercentageList;

// TODO: Auto-generated Javadoc
/**
 * The Class AnalyticsParser.
 */
public class AnalyticsParser {

	/** The Constant tag. */
	private static final String tag = "AnalyticsParser";
	
	/**
	 * Parses the grade pass fail percentage list details.
	 *
	 * @param data the data
	 * @return the grade pass fail percentage list
	 * @throws JsonProcessingException the json processing exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static GradePassFailPercentageList parseGradePassFailPercentageListDetails(String data) throws JsonProcessingException, IOException{
		GradePassFailPercentageList list = new GradePassFailPercentageList();
		ObjectMapper mapper = new ObjectMapper();
		ObjectReader reader = mapper.reader(GradePassFailPercentageList.class);
		list = reader.readValue(data);
		return list;
	}
	
	/**
	 * Parses the grade pass fail percentage details.
	 *
	 * @param data the data
	 * @return the grade pass fail percentage
	 * @throws JsonProcessingException the json processing exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static GradePassFailPercentage parseGradePassFailPercentageDetails(String data) throws JsonProcessingException, IOException{
		GradePassFailPercentage object = new GradePassFailPercentage();
		ObjectMapper mapper = new ObjectMapper();
		ObjectReader reader = mapper.reader(GradePassFailPercentage.class);
		object = reader.readValue(data);
		return object;
	}
	
	/**
	 * Parses the percentage range details.
	 *
	 * @param data the data
	 * @return the percentage list
	 * @throws JsonProcessingException the json processing exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static PercentageList parsePercentageRangeDetails(String data) throws JsonProcessingException, IOException{
		data = "{\"percentageList\":"+data+"}";
		PercentageList percentList = new PercentageList();
		ObjectMapper mapper = new ObjectMapper();
		ObjectReader reader = mapper.reader(PercentageList.class);
		percentList = reader.readValue(data);
		return percentList;
	}
	
	/**
	 * Parses the student percentage details.
	 *
	 * @param data the data
	 * @return the student percentage list
	 * @throws JsonProcessingException the json processing exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static StudentPercentageList parseStudentPercentageDetails(String data) throws JsonProcessingException, IOException{
		StudentPercentageList list = new StudentPercentageList();
		ObjectMapper mapper = new ObjectMapper();
		ObjectReader reader = mapper.reader(StudentPercentageList.class);
		list = reader.readValue(data);
		return list;
	}
	
	/**
	 * Parses the student percentage data.
	 *
	 * @param data the data
	 * @return the student percentage
	 * @throws JsonProcessingException the json processing exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static StudentPercentage parseStudentPercentageData(String data) throws JsonProcessingException, IOException{
		StudentPercentage percentage = new StudentPercentage();
		ObjectMapper mapper = new ObjectMapper();
		ObjectReader reader = mapper.reader(StudentPercentage.class);
		percentage = reader.readValue(data);
		return percentage;
	}
}
