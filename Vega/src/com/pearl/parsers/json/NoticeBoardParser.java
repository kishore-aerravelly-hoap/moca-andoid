package com.pearl.parsers.json;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import com.pearl.application.ApplicationData;
import com.pearl.logger.Logger;
import com.pearl.ui.models.Notice;
import com.pearl.ui.models.NoticeBoardResponse;
import com.pearl.ui.models.NoticeList;

// TODO: Auto-generated Javadoc
/**
 * The Class NoticeBoardParser.
 */
public class NoticeBoardParser {

    /**
     * Gets the notice board content.
     *
     * @param file the file
     * @return the notice board content
     * @throws Exception the exception
     */
    public static NoticeBoardResponse getNoticeBoardContent(File file)
	    throws Exception {
	String content = null;
	content = ApplicationData.readFile(file);
	return getNoticeBoardContent(content);
    }

    /**
     * Gets the notice board content.
     *
     * @param content the content
     * @return the notice board content
     * @throws JsonProcessingException the json processing exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static NoticeBoardResponse getNoticeBoardContent(String content)
	    throws JsonProcessingException, IOException {

	final ObjectMapper mapper = new ObjectMapper();
	final ObjectReader reader = mapper.reader(NoticeBoardResponse.class);
	Logger.warn("vega notification", "content is:-------------------"
		+ content);

	NoticeBoardResponse noticeBoardResponse = null;

	noticeBoardResponse = reader.readValue(content);
	return noticeBoardResponse;
    }

    /**
     * Gets the notice.
     *
     * @param content the content
     * @return the notice
     * @throws JsonProcessingException the json processing exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static List<Notice> getNotice(String content)
	    throws JsonProcessingException, IOException {

	NoticeList noticeList = null;
	final ObjectMapper objMapper = new ObjectMapper();
	content = "{\"_noticeList\":" + content + "}";

	final ObjectReader objreader = objMapper.reader(NoticeList.class);
	try {

	    noticeList = objreader.readValue(content);
	} catch (final JsonProcessingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (final IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return noticeList.get_noticeList();

    }


}
