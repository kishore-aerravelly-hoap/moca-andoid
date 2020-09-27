package com.pearl.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.pearl.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class AttendanceDatesComparision.
 */
public class AttendanceDatesComparision {

	/** The server date. */
	private static Calendar serverDate;
	
	/** The current date. */
	private static Calendar currentDate;
	
	/** The mst. */
	public static int MST;
	
	/** The pd. */
	private static int PD;
	
	/** The mnpt. */
	public static int MNPT;
	
	/** The est. */
	public static int EST;
	
	/** The db time. */
	private static int dbTime;
	
	/** The current time. */
	private static int currentTime;
	
	/** The db hour. */
	private static int dbHour;
	
	/** The current hour. */
	private static int currentHour;
	
	/** The db min. */
	private static int dbMin;
	
	/** The current min. */
	private static int currentMin;
	
	/** The pd count. */
	private static int pdCount;
	
	/** The period. */
	public static int period;
	
	/** The session. */
	private static String session;
	
	/** The tag. */
	private static String tag = "AttendanceDatesComparision";
	
	/** The Constant TAKE_MORNING_ATT. */
	public static final String TAKE_MORNING_ATT = "Take morning attendance";
	
	/** The Constant MORNING_ATT_ALREADY_TAKEN. */
	public static final String MORNING_ATT_ALREADY_TAKEN = "Attendance has been taken for morning session";
	
	/** The Constant MORNING_ATT_MISSED. */
	public static final String MORNING_ATT_MISSED = "You have missed morning attendance";
	
	/** The Constant TAKE_AFTERNOON_ATT. */
	public static final String TAKE_AFTERNOON_ATT = "Take afternoon attendance";
	
	/** The Constant AFTERNOON_ATT_ALREADY_TAKEN. */
	public static final String AFTERNOON_ATT_ALREADY_TAKEN = "Attendance has been taken for afternoon session";
	
	/** The Constant NOT_SCHOOL_TIME. */
	public static final String NOT_SCHOOL_TIME = "This is not school time";
	
	/** The Constant DAILY. */
	public static final String DAILY = "DAY";
	
	/** The Constant SESSIONLY. */
	public static final String SESSIONLY = "SESSION";
	
	/** The Constant DAILY_BASED_ATTENDANCE_ALREADY_TAKEN. */
	public static final String DAILY_BASED_ATTENDANCE_ALREADY_TAKEN = "Attendance has been taken for the day";
	
	/** The Constant TAKE_DAILY_BASED_ATTENDANCE. */
	public static final String TAKE_DAILY_BASED_ATTENDANCE = "Take attendance";
	
	/** The Constant PERIODICALLY. */
	public static final String PERIODICALLY = "PERIOD";
	
	/** The Constant TAKE_PERIODIC_ATTENDANCE. */
	public static final String TAKE_PERIODIC_ATTENDANCE = "Take periodic attendance";
	
	/** The Constant TAKE_ATTENDANCE. */
	public static final String TAKE_ATTENDANCE = "Take Attendance";
	
	/** The Constant PERIODIC_ATTENDANCE_ALREADY_TAKEN. */
	public static final String PERIODIC_ATTENDANCE_ALREADY_TAKEN = "Attendance has been taken for the period";
	
	/** The Constant TIME_DIFF. */
	public static final String TIME_DIFF = "Server time and your tab time are mis matching, Please set it and take attendance";

	/**
	 * Compare dates.
	 *
	 * @param lastAttendanceTakenDate the last attendance taken date
	 * @param morningSessionTime the morning session time
	 * @param eveSessionTime the eve session time
	 * @param userTimeFormat the user time format
	 * @param attendanceFrequency the attendance frequency
	 * @param PeriodDuration the period duration
	 * @return the string
	 */
	public static String compareDates(String lastAttendanceTakenDate, String morningSessionTime,
			String eveSessionTime, int userTimeFormat,
			String attendanceFrequency, int PeriodDuration) {
		Logger.debug(tag, "attendanceFrequency is;" + attendanceFrequency);
		String result = null;
		PD = PeriodDuration;
		serverDate = Calendar.getInstance();
		currentDate = Calendar.getInstance();

		serverDate.setTimeInMillis(Long.parseLong(lastAttendanceTakenDate));
		Logger.warn("AttendanceDatesComparision",
				"\n dbdate is:" + serverDate.getTime());

		Logger.warn("AttendanceDatesComparision", "\n currentdate is:"
				+ currentDate.getTime());
		dbHour = serverDate.get(Calendar.HOUR_OF_DAY);
		currentHour = currentDate.get(Calendar.HOUR_OF_DAY);
		dbMin = serverDate.get(Calendar.MINUTE);
		currentMin = currentDate.get(Calendar.MINUTE);
		dbTime = getTimeInMinutes(dbHour, dbMin);
		currentTime = getTimeInMinutes(currentHour, currentMin);

		if (!attendanceFrequency.equals(DAILY)) {
			convertESTToInt(eveSessionTime);
			convertMSTToInt(morningSessionTime);
			MNPT = (MST + PD) / 60;
		}

		if (attendanceFrequency.equals(DAILY)) {
			Logger.error(tag, "daily based attendance");
			if (currentDate.get(Calendar.DAY_OF_MONTH) == serverDate
					.get(Calendar.DAY_OF_MONTH)) {
				result = DAILY_BASED_ATTENDANCE_ALREADY_TAKEN;
			} else {
				result = TAKE_DAILY_BASED_ATTENDANCE;
			}
		} else if (attendanceFrequency.equals(SESSIONLY)) {
			if (currentDate.after(serverDate) || currentDate.equals(serverDate)) {
				Logger.warn("AttendanceDatesComparision", "greater");
			}
			Logger.warn(tag, "Session based attendance");
			if (currentDate.after(serverDate) || currentDate.equals(serverDate)) {
				if (isMorningSlot()) {
					setSession("M");
					if (isMorningAttendanceApplicable()) {
						result = TAKE_MORNING_ATT;
					} else {
						result = MORNING_ATT_ALREADY_TAKEN;
					}
				} else {
					if (isAfternoonSlot()) {
						setSession("A");
						if (isAfternoonAttendanceApplicable()) {
							if (currentDate.get(Calendar.DAY_OF_MONTH) > serverDate
									.get(Calendar.DAY_OF_MONTH)) {
								result = MORNING_ATT_MISSED;
							} else {
								result = TAKE_AFTERNOON_ATT;
							}
						} else {
							result = AFTERNOON_ATT_ALREADY_TAKEN;
						}
					} else {
						Logger.warn("AttendanceDatesComparision", "wierd");
						result = NOT_SCHOOL_TIME;
					}
				}
			} else {
				Logger.warn("AttendanceDatesComparision", "wierd 1");
				if(currentDate.get(Calendar.YEAR) == serverDate.get(Calendar.YEAR)
						&& currentDate.get(Calendar.MONTH) == serverDate.get(Calendar.MONTH)
						&& currentDate.get(Calendar.HOUR_OF_DAY) == serverDate.get(Calendar.HOUR_OF_DAY)){
					if (isAfternoonSlot()) {
						result = AFTERNOON_ATT_ALREADY_TAKEN;
					}else if(isMorningSlot()){
						result = MORNING_ATT_ALREADY_TAKEN;
					}
				}else
					result = TIME_DIFF;
			}
		} else if (attendanceFrequency.equals(PERIODICALLY)) {
			Logger.warn("AttendanceDatesComparision", "periodic based");
			if (currentTime > MST && currentTime < EST) {
				pdCount = (currentTime - MST) / PD;
				MNPT = MST + (pdCount * PD);
			} else if (currentTime >= EST) {
				if (PD != 0) {
					pdCount = (currentTime - EST) / PD;
				} else
					pdCount = 0;

				MNPT = EST + (pdCount * PD);
			} else {
				Logger.warn("AttendanceDatesComparision", "school not started");
			}
			period = pdCount + 1;
			Logger.warn(tag, "Period is:" + period);
			if (currentDate.after(serverDate) || currentDate.equals(serverDate)) {
				if (currentTime >= MNPT
						&& currentTime < (MNPT + ((pdCount + 1) * PD))) {
					if (dbTime >= MNPT
							&& dbTime < (MNPT + ((pdCount + 1) * PD))
							&& currentDate.get(Calendar.DAY_OF_MONTH) == serverDate
									.get(Calendar.DAY_OF_MONTH)) {
						result = PERIODIC_ATTENDANCE_ALREADY_TAKEN;
					} else {
						result = TAKE_PERIODIC_ATTENDANCE;
					}
				} else {
					Logger.warn("AttendanceDatesComparision", "Wierd");
					result = NOT_SCHOOL_TIME;
				}
			}else{
				if(currentDate.get(Calendar.YEAR) == serverDate.get(Calendar.YEAR)
						&& currentDate.get(Calendar.MONTH) == serverDate.get(Calendar.MONTH)
						&& currentDate.get(Calendar.HOUR_OF_DAY) == serverDate.get(Calendar.HOUR_OF_DAY)
						&& currentDate.get(Calendar.MINUTE) == serverDate.get(Calendar.MINUTE))
					result = PERIODIC_ATTENDANCE_ALREADY_TAKEN;
				else
					result = TIME_DIFF;
			}
		} else {
			Logger.warn("AttendanceDatesComparision", "Wierd 1");
			result = NOT_SCHOOL_TIME;
		}

		return result;
	}

	/**
	 * Checks if is morning slot.
	 *
	 * @return true, if is morning slot
	 */
	public static boolean isMorningSlot() {
		if (currentTime >= MST && currentTime < EST) {
			return true;
		} else
			return false;
	}

	/**
	 * Checks if is morning attendance applicable.
	 *
	 * @return true, if is morning attendance applicable
	 */
	private static boolean isMorningAttendanceApplicable() {
		if (currentDate.get(Calendar.DAY_OF_MONTH) == serverDate
				.get(Calendar.DAY_OF_MONTH) && dbTime >= MST && dbTime < EST) {
			return false;
		} else
			return true;
	}

	/**
	 * Checks if is afternoon slot.
	 *
	 * @return true, if is afternoon slot
	 */
	private static boolean isAfternoonSlot() {
		if (currentTime >= EST) {
			return true;
		} else
			return false;
	}

	/**
	 * Checks if is afternoon attendance applicable.
	 *
	 * @return true, if is afternoon attendance applicable
	 */
	private static boolean isAfternoonAttendanceApplicable() {
		if (currentDate.get(Calendar.DAY_OF_MONTH) == serverDate
				.get(Calendar.DAY_OF_MONTH) && dbTime >= EST) {
			return false;
		} else
			return true;
	}

	/**
	 * converts hours to minutes.
	 *
	 * @param hour the hour
	 * @param minute the minute
	 * @return the time in minutes
	 */
	public static int getTimeInMinutes(int hour, int minute) {
		// Logger.warn("AttendanceDatesComparision","converted time to minutes is:"+(hour*60+minute));
		return (hour * 60 + minute);
	}

	/**
	 * Converts 12 hour format to 24 hour format if the time format is in '12'
	 * hour format.
	 *
	 * @param d1 the d1
	 * @return the string
	 */
	public static String convert12To24Hourformat(Date d1) {
		String s = d1.toString();// "Thu Jan 01 13:30:00 IST 1970";
		return (s.substring(11, 16));
	}

	/**
	 * Converts Morning session time to int.
	 *
	 * @param s the s
	 * @return the int
	 */
	public static int convertMSTToInt(String s) {
		SimpleDateFormat sdf1;
		/*if (timeFormat == 12) {
			sdf1 = new SimpleDateFormat("hh:mm a");
		} else*/
			sdf1 = new SimpleDateFormat("hh:mm");
		try {
			//String convertedFormat = convert12To24Hourformat(sdf1.parse(s));
			if(s.substring(0,2).contains(":"))
				s = "0"+s;
			MST = getTimeInMinutes(
					Integer.parseInt(s.substring(0, 2)),
					Integer.parseInt(s.substring(3, 5)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MST;
	}

	/**
	 * Converts evening session time to int.
	 *
	 * @param s the s
	 * @return the int
	 */
	public static int convertESTToInt(String s) {
		SimpleDateFormat sdf1;
		/*if (timeFormat == 12) {
			sdf1 = new SimpleDateFormat("hh:mm a");
		} else*/
			sdf1 = new SimpleDateFormat("hh:mm");
		try {
			//String convertedFormat = convert12To24Hourformat(sdf1.parse(s));
			if(s.substring(0,2).contains(":"))
				s = "0"+s;
			EST = getTimeInMinutes(
					Integer.parseInt(s.substring(0, 2)),
					Integer.parseInt(s.substring(3, 5)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return EST;
	}

	/**
	 * Calculate time difference.
	 *
	 * @param time1 the time1
	 * @param time2 the time2
	 * @return the int
	 */
	public static int calculateTimeDifference(String time1, String time2) {

		int diffHours = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		try {
			Date d1 = sdf.parse(time1);
			Date d2 = sdf.parse(time2);

			c1.setTime(d1);
			c2.setTime(d2);
			long diff = c2.getTimeInMillis() - c1.getTimeInMillis();

			diffHours = (int) diff / (60 * 1000);
			Logger.error(tag, "time difference is:" + diffHours);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return diffHours;
	}

	/**
	 * Gets the session.
	 *
	 * @return the session
	 */
	public static String getSession() {
		return session;
	}

	/**
	 * Sets the session.
	 *
	 * @param session the new session
	 */
	public static void setSession(String session) {
		AttendanceDatesComparision.session = session;
	}
}