/**
 * 
 */
package com.pearl.ui.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class BaseRequest.
 *
 * @author kpamulapati
 */
public class BaseRequest implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The auth. */
    private String auth;
    
    /** The data. */
    private String data;

    /**
     * Gets the auth.
     *
     * @return the auth
     */
    public String getAuth() {
	return auth;
    }

    /**
     * Sets the auth.
     *
     * @param auth the auth to set
     */
    public void setAuth(String auth) {
	this.auth = auth;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "BaseRequest [auth=" + auth + "]";
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public String getData() {
	return data;
    }

    /**
     * Sets the data.
     *
     * @param data the data to set
     */
    public void setData(String data) {
	this.data = data;
    }

}
