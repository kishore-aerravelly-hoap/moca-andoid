/**
 * 
 */
package com.pearl.ui.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class NoticeBoardResponse.
 *
 * @author kpamulapati
 */
public class NoticeBoardResponse extends BaseResponse1 implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The user id. */
    private String userId;
    // private Map<String,List<Notice>> noticeBoardData;
    /** The data. */
    private VegaNotices data;

    /**
     * Gets the user id.
     *
     * @return the userId
     */
    public String getUserId() {
	return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
	this.userId = userId;
    }

    /**
     * Gets the data.
     *
     * @return the notices
     */
    /*
     * public Map<String,List<Notice>> getNoticeBoardData() { if(null ==
     * noticeBoardData){ ObjectMapper om = new ObjectMapper();
     * 
     * try { noticeBoardData = om.readValue(this.getData(),
     * noticeBoardData.getClass()); } catch (Exception e) {
     * Logger.error("NoticeBoardResponse", e); } }
     * 
     * return noticeBoardData; }
     *//**
     * @param notices
     *            the notices to set
     */
    /*
     * public void setNoticeBoardData(Map<String,List<Notice>> data) {
     * //noticeBoardData ObjectMapper om = new ObjectMapper();
     * 
     * try { this.data = om.writeValueAsString(data); } catch (Exception e) {
     * this.data = "";
     * 
     * Logger.error("NoticeBoardActivity", e); } }
     */

    @Override
    public VegaNotices getData() {
	return data;
    }

    /*
     * @param notices the notices to set
     */
    /**
     * Sets the data.
     *
     * @param data the new data
     */
    public void setData(VegaNotices data) {
	// noticeBoardData
	this.data = data;
    }

}