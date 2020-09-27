/**
 * 
 */
package com.pearl.ui.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class PearlConfigKeyValues.
 *
 * @author Eng1
 */
public class PearlConfigKeyValues implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The configtype. */
    private String configtype;
    
    /** The configkey. */
    private String configkey;
    
    /** The configvalue. */
    private String configvalue;
    
    /** The status. */
    private String status;

    /**
     * Gets the configkey.
     *
     * @return the configkey
     */
    public String getConfigkey() {
	return configkey;
    }

    /**
     * Sets the configkey.
     *
     * @param configkey the configkey to set
     */
    public void setConfigkey(String configkey) {
	this.configkey = configkey;
    }

    /**
     * Gets the configvalue.
     *
     * @return the configvalue
     */
    public String getConfigvalue() {
	return configvalue;
    }

    /**
     * Sets the configvalue.
     *
     * @param configvalue the configvalue to set
     */
    public void setConfigvalue(String configvalue) {
	this.configvalue = configvalue;
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
     * Sets the status.
     *
     * @param status the status to set
     */
    public void setStatus(String status) {
	this.status = status;
    }

    /**
     * Gets the configtype.
     *
     * @return the configtype
     */
    public String getConfigtype() {
	return configtype;
    }

    /**
     * Sets the configtype.
     *
     * @param configtype the configtype to set
     */
    public void setConfigtype(String configtype) {
	this.configtype = configtype;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "PearlConfigKeyValues [configtype=" + configtype
		+ ", configkey=" + configkey + ", configvalue=" + configvalue
		+ ", status=" + status + "]";
    }

}
