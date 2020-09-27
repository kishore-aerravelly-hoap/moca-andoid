package com.pearl.helpers.calendar;

import java.util.Date;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Events.
 */
public class Events {

    /** The id. */
    String id;
    
    /** The event name. */
    String eventName;
    
    /** The from date. */
    Date fromDate;
    
    /** The to date. */
    Date toDate;
    
    /** The location. */
    String location;
    
    /** The calendar. */
    String calendar;
    
    /** The description. */
    String description;
    
    /** The reminders. */
    List<Integer> reminders;
    
    /** The repeat. */
    String repeat;
    
    /** The event sent to user. */
    private int eventSentToUser;
    
    /** The has alarm. */
    int hasAlarm;

    /**
     * Gets the checks for alarm.
     *
     * @return the checks for alarm
     */
    public int getHasAlarm() {
	return hasAlarm;
    }

    /**
     * Sets the checks for alarm.
     *
     * @param hasAlarm the new checks for alarm
     */
    public void setHasAlarm(int hasAlarm) {
	this.hasAlarm = hasAlarm;
    }

    /**
     * Gets the repeat.
     *
     * @return the repeat
     */
    public String getRepeat() {
	return repeat;
    }

    /**
     * Sets the repeat.
     *
     * @param repeat the new repeat
     */
    public void setRepeat(String repeat) {
	this.repeat = repeat;
    }

    /**
     * Gets the event name.
     *
     * @return the event name
     */
    public String getEventName() {
	return eventName;
    }

    /**
     * Sets the event name.
     *
     * @param eventName the new event name
     */
    public void setEventName(String eventName) {
	this.eventName = eventName;
    }

    /**
     * Gets the from date.
     *
     * @return the from date
     */
    public Date getFromDate() {
	return fromDate;
    }

    /**
     * Sets the from date.
     *
     * @param fromDate the new from date
     */
    public void setFromDate(Date fromDate) {
	this.fromDate = fromDate;
    }

    /**
     * Gets the to date.
     *
     * @return the to date
     */
    public Date getToDate() {
	return toDate;
    }

    /**
     * Sets the to date.
     *
     * @param toDate the new to date
     */
    public void setToDate(Date toDate) {
	this.toDate = toDate;
    }

    /**
     * Gets the location.
     *
     * @return the location
     */
    public String getLocation() {
	return location;
    }

    /**
     * Sets the location.
     *
     * @param location the new location
     */
    public void setLocation(String location) {
	this.location = location;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
	return description;
    }

    /**
     * Sets the description.
     *
     * @param description the new description
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Gets the reminders.
     *
     * @return the reminders
     */
    public List<Integer> getReminders() {
	return reminders;
    }

    /**
     * Sets the reminders.
     *
     * @param reminders the new reminders
     */
    public void setReminders(List<Integer> reminders) {
	this.reminders = reminders;
    }

    /**
     * Gets the calendar.
     *
     * @return the calendar
     */
    public String getCalendar() {
	return calendar;
    }

    /**
     * Sets the calendar.
     *
     * @param calendar the new calendar
     */
    public void setCalendar(String calendar) {
	this.calendar = calendar;
    }

    /**
     * Gets the event sent to user.
     *
     * @return the event sent to user
     */
    public int getEventSentToUser() {
	return eventSentToUser;
    }

    /**
     * Sets the event sent to user.
     *
     * @param eventSentToUser the new event sent to user
     */
    public void setEventSentToUser(int eventSentToUser) {
	this.eventSentToUser = eventSentToUser;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
	return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(String id) {
	this.id = id;
    }

}
