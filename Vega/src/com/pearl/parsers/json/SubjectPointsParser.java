package com.pearl.parsers.json;

import java.io.IOException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import com.pearl.analytics.models.AndroidAnalytics;
import com.pearl.analytics.models.AndroidAnalyticsList;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class SubjectPointsParser.
 */
public class SubjectPointsParser {
	
	/** The tag. */
	private static String tag = "SubjectPointsParser";

	/**
	 * Gets the analytics details.
	 *
	 * @param content the content
	 * @return the analytics details
	 * @throws JsonProcessingException the json processing exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static AndroidAnalytics getAnalyticsDetails(String content)
			throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectReader reader = mapper.reader(AndroidAnalytics.class);
		AndroidAnalytics androidDashboard = reader.readValue(content);
		return androidDashboard;

	}
	
	/**
	 * Gets the android analytics from list.
	 *
	 * @param content the content
	 * @return the android analytics from list
	 */
	public static AndroidAnalyticsList getAndroidAnalyticsFromList(String content){
		AndroidAnalyticsList analyticsList = null;
		ObjectMapper mapper = new ObjectMapper();
		ObjectReader reader = mapper.reader(AndroidAnalyticsList.class);
		try {
			content = "{\"androidAnalyitcsList\":" + content + "}";
			Logger.warn(tag, "content after concatenating is:"+content);
			analyticsList = reader.readValue(content);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return analyticsList;
		
	}
}
