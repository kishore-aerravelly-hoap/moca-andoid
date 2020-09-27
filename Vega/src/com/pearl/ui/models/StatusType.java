/**
 * 
 */
package com.pearl.ui.models;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Enum StatusType.
 *
 * @author kpamulapati
 */
public enum StatusType {
    
    /** The success. */
    SUCCESS(1), 
 /** The failure. */
 FAILURE(2), 
 /** The notautherized. */
 NOTAUTHERIZED(3);

    /**
     * Instantiates a new status type.
     *
     * @param value the value
     */
    private StatusType(long value) {
	status = value;
    }

    /** The status. */
    private final long status;

    /**
     * Gets the status.
     *
     * @return the status
     */
    public long getStatus() {
	return status;
    }

    /**
     * Gets the status types.
     *
     * @return the status types
     */
    public static List<String> getStatusTypes() {
	final List<String> list = new ArrayList<String>();
	final StatusType statusTypes[] = StatusType.values();
	for (final StatusType statusType : statusTypes) {
	    list.add(statusType.name());
	}

	return list;
    }
}
