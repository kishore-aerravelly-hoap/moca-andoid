package com.pearl.parsers.json;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import com.pearl.application.ApplicationData;
import com.pearl.attendance.Attendance;
import com.pearl.logger.Logger;
import com.pearl.ui.models.BaseResponse;
import com.pearl.ui.models.BaseResponse1;
import com.pearl.ui.models.PeriodConfig;

// TODO: Auto-generated Javadoc
/**
 * The Class AttendanceParser.
 */
public class AttendanceParser {

    /** The Constant tag. */
    private static final String tag = "AttendanceParser";

    /**
     * Gets the attendance file.
     *
     * @param file the file
     * @return the attendance file
     * @throws JsonParseException the json parse exception
     * @throws JsonMappingException the json mapping exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static Attendance getAttendanceFile(File file)
	    throws JsonParseException, JsonMappingException, IOException {
	String content = null;
	try {
	    content = ApplicationData.readFile(file);
	} catch (final IOException e) {
	    e.printStackTrace();
	}

	return getAttendanceFile(content);

    }

    /**
     * Gets the attendance file.
     *
     * @param filePath the file path
     * @return the attendance file
     * @throws JsonParseException the json parse exception
     * @throws JsonMappingException the json mapping exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static Attendance getAttendanceFile(String filePath)
	    throws JsonParseException, JsonMappingException, IOException {
	Attendance attendance = null;
	final ObjectMapper mapper = new ObjectMapper();
	String jsonData = "";
	BaseResponse baseResponse = null;
	baseResponse = mapper.readValue(new File(filePath), BaseResponse.class);

	if (baseResponse.getData() == null) {
	    Logger.info(tag, "Attendance parser DATA is NULL");
	} else {
	    jsonData = baseResponse.getData().toString();

	    Logger.warn(tag, "json data from server is:" + jsonData);
	    final ObjectReader reader = mapper.reader(Attendance.class);
	    try {
		attendance = reader.readValue(jsonData);
	    } catch (final JsonProcessingException e) {
		Logger.error(tag, e);
	    } catch (final IOException e) {
		Logger.error(tag, e);
	    }
	}
	return attendance;
    }

    /**
     * Gets the last attendance taken details.
     *
     * @param downloadedString the downloaded string
     * @return the last attendance taken details
     */
    public static String getLastAttendanceTakenDetails(String downloadedString){
	String date = "";
	final ObjectMapper mapper = new ObjectMapper();
	BaseResponse1 response = null;
	try {
	    response = mapper.readValue(downloadedString,
		    BaseResponse1.class);
	} catch (final JsonParseException e) {
	    Logger.error(tag, e);
	} catch (final JsonMappingException e) {
	    Logger.error(tag, e);
	} catch (final IOException e) {
	    Logger.error(tag, e);
	}
	if (response.getData() != null && response.getData() != "") {
	    date = response.getData().toString();
	    Logger.warn(tag, "data from response string is:" + date);

	} else
	    Logger.error(tag,
		    "data from last attendance url is null");
	return date;
    }

    /**
     * Gets the last attendance taken details.
     *
     * @param file the file
     * @return the last attendance taken details
     */
    public static String getLastAttendanceTakenDetails(File file){
	try {
	    return getLastAttendanceTakenDetails(ApplicationData.readFile(file));
	} catch (final IOException e) {
	    Logger.error(tag, e);
	    return "";
	}
    }

    /**
     * Gets the period configuration.
     *
     * @param downloadedString the downloaded string
     * @return the period configuration
     */
    public static PeriodConfig getPeriodConfiguration(String downloadedString){

	Logger.warn(tag, "Downloaded string in onsuccess is:"
		+ downloadedString);
	final ObjectMapper mapper = new ObjectMapper();
	final ObjectReader objectReader = mapper
		.reader(PeriodConfig.class);
	PeriodConfig periodConfig = new PeriodConfig();
	try {
	    periodConfig = objectReader
		    .readValue(downloadedString);
	} catch (final JsonProcessingException e) {
	    Logger.error(tag, e);
	} catch (final IOException e) {
	    Logger.error(tag, e);
	}
	return periodConfig;
    }
}
