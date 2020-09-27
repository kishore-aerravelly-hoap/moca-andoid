package com.pearl.network.request.parameters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.pearl.chat.server.response.BaseResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class StringParameter.
 */
public class StringParameter implements RequestParameter {
    
    /** The field name. */
    String fieldName;
    
    /** The value. */
    String value;
    
    /** The encoding. */
    String encoding = "UTF-8";
    
    /** The list. */
    List<String> list = new ArrayList<String>();
    
    /** The baseresponse. */
    BaseResponse baseresponse;

    /**
     * Gets the baseresponse.
     *
     * @return the baseresponse
     */
    public BaseResponse getBaseresponse() {
	return baseresponse;
    }

    /**
     * Sets the baseresponse.
     *
     * @param baseresponse the new baseresponse
     */
    public void setBaseresponse(BaseResponse baseresponse) {
	this.baseresponse = baseresponse;
    }

    /**
     * Instantiates a new string parameter.
     *
     * @param fieldName the field name
     * @param value the value
     */
    public StringParameter(String fieldName, String value) {
	this.fieldName = fieldName;
	this.value = value;
    }

    /**
     * Instantiates a new string parameter.
     *
     * @param fieldName the field name
     * @param value the value
     */
    public StringParameter(String fieldName, List<String> value) {
	this.fieldName = fieldName;
	list = value;
	final Iterator<String> ite = list.iterator();
	String str = "";
	while (ite.hasNext()) {
	    str += ite.next() + ",";
	}
	this.value = str;
    }

    /**
     * Instantiates a new string parameter.
     *
     * @param fieldName the field name
     * @param value the value
     * @param encoding the encoding
     */
    public StringParameter(String fieldName, String value, String encoding) {
	this.fieldName = fieldName;
	this.value = value;
	this.encoding = encoding;
    }

    /**
     * Instantiates a new string parameter.
     *
     * @param fieldName the field name
     * @param data the data
     */
    public StringParameter(String fieldName,BaseResponse data) {
	this.fieldName=fieldName;
	baseresponse=data;

	value=data.getData().toString();

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
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
	return value;
    }

    /**
     * Gets the encoding.
     *
     * @return the encoding
     */
    public String getEncoding() {
	return encoding;
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
     * Sets the value.
     *
     * @param value the value to set
     */
    public void setValue(String value) {
	this.value = value;
    }

    /**
     * Sets the encoding.
     *
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
	this.encoding = encoding;
    }

    /* (non-Javadoc)
     * @see com.pearl.network.request.parameters.RequestParameter#getParameterType()
     */
    @Override
    public String getParameterType() {
	return RequestParameter.STRING_PARAM;
    }

    /**
     * Gets the list.
     *
     * @return the list
     */
    public List<String> getList() {
	return list;
    }

    /**
     * Sets the list.
     *
     * @param list the new list
     */
    public void setList(List<String> list) {
	this.list = list;
    }

}