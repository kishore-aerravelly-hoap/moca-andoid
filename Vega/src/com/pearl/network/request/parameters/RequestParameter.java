package com.pearl.network.request.parameters;

// TODO: Auto-generated Javadoc
/**
 * The Interface RequestParameter.
 */
public interface RequestParameter {
    
    /** The Constant FILE_PARAM. */
    public static final String FILE_PARAM = "file_parameter";
    
    /** The Constant STRING_PARAM. */
    public static final String STRING_PARAM = "string_parameter";

    /**
     * Gets the parameter type.
     *
     * @return the parameter type
     */
    String getParameterType();
}