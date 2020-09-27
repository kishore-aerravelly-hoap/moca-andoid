package com.pearl.books;

import java.io.Serializable;
import java.util.Arrays;

// TODO: Auto-generated Javadoc
/**
 * The Class AdditionalInfo.
 */
public class AdditionalInfo implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The title. */
    private String title;
    
    /** The desc. */
    private String desc;
    
    /** The file data. */
    private byte[] fileData;

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * Sets the title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * Gets the desc.
     *
     * @return the desc
     */
    public String getDesc() {
	return desc;
    }

    /**
     * Sets the desc.
     *
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
	this.desc = desc;
    }

    /**
     * Gets the file data.
     *
     * @return the fileData
     */
    public byte[] getFileData() {
	return fileData;
    }

    /**
     * Sets the file data.
     *
     * @param fileData the fileData to set
     */
    public void setFileData(byte[] fileData) {
	this.fileData = fileData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "AdditionalInfoModel [title=" + title + ", desc=" + desc
		+ ", fileData=" + Arrays.toString(fileData) + "]";
    }

}
