package com.pearl.network.request.parameters;

import java.io.File;

// TODO: Auto-generated Javadoc
/**
 * The Class FileParameter.
 */
public class FileParameter implements RequestParameter {
    
    /** The field name. */
    String fieldName;
    
    /** The file. */
    File file;
    
    /** The content type. */
    String contentType;

    /**
     * Instantiates a new file parameter.
     *
     * @param fieldName the field name
     * @param file the file
     * @param contentType the content type
     */
    public FileParameter(String fieldName, File file, String contentType) {
	this.fieldName = fieldName;
	this.file = file;
	this.contentType = contentType;
    }

    /**
     * Gets the field name.
     *
     * @return the fieldName
     */
    public String getFieldName() {
	return fieldName;
    }

    /**
     * Gets the file.
     *
     * @return the file
     */
    public File getFile() {
	return file;
    }

    /**
     * Gets the content type.
     *
     * @return the contentType
     */
    public String getContentType() {
	return contentType;
    }

    /**
     * Sets the field name.
     *
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
	this.fieldName = fieldName;
    }

    /**
     * Sets the file.
     *
     * @param file the file to set
     */
    public void setFile(File file) {
	this.file = file;
    }

    /**
     * Sets the content type.
     *
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
	this.contentType = contentType;
    }

    /* (non-Javadoc)
     * @see com.pearl.network.request.parameters.RequestParameter#getParameterType()
     */
    @Override
    public String getParameterType() {
	return RequestParameter.FILE_PARAM;
    }
}