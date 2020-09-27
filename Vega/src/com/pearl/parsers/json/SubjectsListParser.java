package com.pearl.parsers.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import android.support.v4.app.FragmentActivity;

import com.pearl.books.SubjectList;
import com.pearl.chat.server.response.BaseResponse;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class SubjectsListParser.
 */
public class SubjectsListParser {
    
    /** The Constant tag. */
    private static final String tag = "SubjectsListParser";

    /**
     * Gets the subjects list.
     *
     * @param content the content
     * @param activity the activity
     * @return the subjects list
     */
    public static SubjectList getSubjectsList(String content,
	    FragmentActivity activity) {
	final ObjectMapper mapper = new ObjectMapper();
	Logger.warn(tag, "Response string is:" + content);
	BaseResponse response = null;
	SubjectList subjectList = null;
	try {
	    response = mapper.readValue(content, BaseResponse.class);
	} catch (final JsonParseException e) {
	    //activity.ToastMessageForExceptions(R.string.JSON_PARSING_EXCEPTION);
	    e.printStackTrace();
	    // TODO uncomment below line after updating sahitya's code
	    // activity.backToDashboard();
	} catch (final JsonMappingException e) {
	    e.printStackTrace();
	    //	activity.ToastMessageForExceptions(R.string.JSON_MAPPING_EXCEPTION);
	    // TODO uncomment below line after updating sahitya's code
	    // activity.backToDashboard();
	} catch (final IOException e) {
	    e.printStackTrace();
	    //	activity.ToastMessageForExceptions(R.string.IO_EXCEPTION);
	    // TODO uncomment below line after updating sahitya's code
	    // activity.backToDashboard();
	}
	Logger.warn(tag, "response data is:" + response);
	if (response != null && response.getData() != null) {
	    Logger.warn(tag, "response data is not null");
	    final String result = response.getData().toString();
	    try {
		subjectList = new SubjectList();
		final ObjectReader reader = mapper.reader(SubjectList.class);
		subjectList = reader.readValue(result);
		Logger.warn(tag,
			"subject list is" + subjectList.getSubjectList());
	    } catch (final JsonParseException e) {
		//activity.ToastMessageForExceptions(R.string.JSON_PARSING_EXCEPTION);
		// TODO uncomment below line after updating sahitya's code
		// activity.backToDashboard();
		e.printStackTrace();
	    } catch (final JsonMappingException e) {
		//activity.ToastMessageForExceptions(R.string.JSON_MAPPING_EXCEPTION);
		e.printStackTrace();
		// TODO uncomment below line after updating sahitya's code
		// activity.backToDashboard();
	    } catch (final IOException e) {
		//activity.ToastMessageForExceptions(R.string.IO_EXCEPTION);
		e.printStackTrace();
		// TODO uncomment below line after updating sahitya's code
		// activity.backToDashboard();
	    }

	    Logger.info(
		    tag,
		    "subject list is ---- in onCreate"
			    + subjectList.getSubjectList());
	} else {
	    Logger.warn(tag, "Content is null..");
	}
	return subjectList;
    }
}
