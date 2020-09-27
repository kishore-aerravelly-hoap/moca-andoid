package com.pearl.parsers.json;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import com.pearl.logger.Logger;
import com.pearl.ui.models.BaseResponse1;
import com.pearl.ui.models.NotificationCount;

// TODO: Auto-generated Javadoc
/**
 * The Class NotificationParser.
 */
public class NotificationParser {
    
    /** The Constant tag. */
    private static final String tag = "NotifParser";

    /**
     * Gets the notification file.
     *
     * @param content the content
     * @return the notification file
     * @throws Exception the exception
     */
    public static NotificationCount getNotificationFile(String content)
	    throws Exception {
	final ObjectMapper mapper = new ObjectMapper();
	BaseResponse1 response;
	NotificationCount nCount = null;
	String data = "";
	if (content != null && content != "") {
	    Logger.warn(tag, "in IF as content is not null");
	    response = mapper.readValue(content, BaseResponse1.class);
	    if(response != null && response.getData() != null)
		data = response.getData().toString();
	} else {
	    // the code will reach here when we don't get response from the
	    // server handle it
	    Logger.warn(tag, "Content is null");
	}
	final ObjectReader reader = mapper.reader(NotificationCount.class);
	nCount = reader.readValue(data);

	return nCount;
    }
}
