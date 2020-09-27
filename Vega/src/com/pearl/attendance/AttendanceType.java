package com.pearl.attendance;

// TODO: Auto-generated Javadoc
/**
 * The Enum AttendanceType.
 */
public enum AttendanceType {

    /** The submit daily. */
    SUBMIT_DAILY("DAY"),/** The submit sessionly. */
SUBMIT_SESSIONLY("SESSION"), /** The morning session time key. */
 MORNING_SESSION_TIME_KEY("MORNINGSESSIONTIME"),
    
    /** The afternoon session time key. */
    AFTERNOON_SESSION_TIME_KEY("AFTERNOONSESSIONTIME"),
/** The attendance type. */
ATTENDANCE_TYPE("ATTENDANCETYPE"),
    
    /** The period duration key. */
    PERIOD_DURATION_KEY("PERIODDURATION"),
    
    /** The submit periodically. */
    SUBMIT_PERIODICALLY("PERIOD"), 
 /** The last attendace taken date key. */
 LAST_ATTENDACE_TAKEN_DATE_KEY("LASTATTENDANCETAKENDATE");

    /** The attendance type. */
    private final String attendanceType;

    /**
     * Instantiates a new attendance type.
     *
     * @param attendanceType the attendance type
     */
    private AttendanceType(String attendanceType) {
	this.attendanceType = attendanceType;
    }

    /**
     * Gets the attendance type.
     *
     * @return the attendance type
     */
    public String getAttendanceType(){
	return attendanceType;
    }
}

