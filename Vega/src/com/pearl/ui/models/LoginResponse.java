package com.pearl.ui.models;

import java.io.IOException;
import java.io.Serializable;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;

import com.pearl.baseresponse.login.AndroidUser;
import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class LoginResponse.
 */
public class LoginResponse extends BaseResponse implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    // private com.pearl.users.User user;
    /** The user. */
    private AndroidUser user;

    /**
     * Gets the user.
     *
     * @return the user
     * @throws JsonParseException the json parse exception
     * @throws JsonMappingException the json mapping exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public AndroidUser getUser() throws JsonParseException,
    JsonMappingException, IOException {
	if (null == user) {
	    final ObjectMapper mapper = new ObjectMapper();
	    final ObjectReader reader = mapper.reader(AndroidUser.class);

	    user = reader.readValue(data);
	}
	Logger.error("Login Response", "User id is " + user.getId()
		+ " | User data is " + data);

	return user;
    }

    /**
     * Sets the user.
     *
     * @param user the user to set
     */
    public void setUser(AndroidUser user) {
	this.user = user;
    }
}