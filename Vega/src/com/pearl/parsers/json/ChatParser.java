package com.pearl.parsers.json;

import java.io.File;
import java.io.IOException;

import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import com.pearl.application.ApplicationData;
import com.pearl.chat.MessageList;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ChatParser.
 */
public class ChatParser {

    /** The Constant tag. */
    private static final String tag = "ChatParser";

    /**
     * Gets the chat file.
     *
     * @param file the file
     * @return the chat file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static MessageList getChatFile(File file) throws IOException {
	String content = null;
	content = ApplicationData.readFile(file);

	return getChatFile(content);
    }

    /**
     * Gets the chat file.
     *
     * @param filePath the file path
     * @return the chat file
     * @throws JsonProcessingException the json processing exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static MessageList getChatFile(String filePath)
	    throws JsonProcessingException, IOException {
	MessageList messageList = new MessageList();
	String content = "";
	try {
	    content = ApplicationData.readFile(filePath);
	} catch (final IOException e1) {
	    Logger.error(tag, e1);
	}
	Logger.error(tag, content);

	final ObjectMapper mapper = new ObjectMapper();
	final ObjectReader reader = mapper.reader(MessageList.class);
	messageList = reader.readValue(content);
	return messageList;
    }
}
