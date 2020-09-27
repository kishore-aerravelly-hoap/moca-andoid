package com.pearl.network.request;

// TODO: Auto-generated Javadoc
/**
 * The Class ResponseStatus.
 */
public class ResponseStatus {
    
    /** The Constant NOT_DONE. */
    public static final String NOT_DONE = "NOT_DONE";
    
    /** The Constant IN_PROGRESS. */
    public static final String IN_PROGRESS = "IN_PROGRESS";
    
    /** The Constant SUCCESS. */
    public static final String SUCCESS = "SUCCESS";
    
    /** The Constant FAIL. */
    public static final String FAIL = "FAIL";
    
    /** The Constant ABORT. */
    public static final String ABORT = "ABORT";
    
    /** The Constant FAILURE. */
    public static final String FAILURE = "FAILURE";
    
    /** The Constant You_are_not_authorized_user. */
    public static final String You_are_not_authorized_user = "You are not authorized user.";
    
    /** The Constant Please_enter_valid_Username_Password. */
    public static final String Please_enter_valid_Username_Password = "Please enter valid Username/Password.";
    
    /** The Constant Please_enter_username. */
    public static final String Please_enter_username = " Please enter username.";
    
    /** The Constant Please_enter_password. */
    public static final String Please_enter_password = "Please enter password.";

    /** The status. */
    private String status;
    
    /** The message. */
    private String message;

    /**
     * Instantiates a new response status.
     */
    public ResponseStatus() {
	status = ResponseStatus.IN_PROGRESS;
	message = "";
    }

    /**
     * Sets the staus.
     *
     * @param s the s
     * @return the response status
     */
    public ResponseStatus setStaus(String s) {
	status = s;

	return this;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
	return status;
    }

    /**
     * Sets the message.
     *
     * @param msg the new message
     */
    public void setMessage(String msg) {
	message = msg;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
	return message;
    }
}
