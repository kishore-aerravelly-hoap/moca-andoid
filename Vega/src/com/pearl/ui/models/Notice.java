package com.pearl.ui.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


// TODO: Auto-generated Javadoc
/**
 * The Class Notice.
 *
 * @author kpamulapati
 */
public class Notice implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    private String id;
    
    /** The title. */
    private String title;
    
    /** The msg. */
    private String msg;
    
    /** The radio list. */
    private List<String> radioList = new ArrayList<String>();
    {
	radioList.add("All");
	radioList.add("Section");
	radioList.add("Student");
    }
    
    /** The check list. */
    private List<String> checkList = new ArrayList<String>();
    
    /** The checked value. */
    private String checkedValue;
    
    /** The due date. */
    private Date dueDate;
    
    /** The reminder before. */
    private int reminderBefore;
    
    /** The selected radio type. */
    private String selectedRadioType;
    
    /** The user from name. */
    private String userFromName;
    
    /** The user to name. */
    private String userToName;
    
    /** The notice to name. */
    private String noticeToName;
    
    /** The notice from name. */
    private String noticeFromName;
    
    /** The students list. */
    private List<Student> studentsList;
    
    /** The class rooms. */
    private List<ClassRoom> classRooms;
    
    /** The time. */
    private String time;
    
    /** The notice sent date. */
    private Date noticeSentDate;

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
     * Gets the msg.
     *
     * @return the msg
     */
    public String getMsg() {
	return msg;
    }

    /**
     * Sets the msg.
     *
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
	this.msg = msg;
    }

    /**
     * Gets the students list.
     *
     * @return the studentsList
     */
    public List<Student> getStudentsList() {
	return studentsList;
    }

    /**
     * Sets the students list.
     *
     * @param studentsList the studentsList to set
     */
    public void setStudentsList(List<Student> studentsList) {
	this.studentsList = studentsList;
    }

    /**
     * Gets the reminder before.
     *
     * @return the reminderBefore
     */
    public int getReminderBefore() {
	return reminderBefore;
    }

    /**
     * Sets the reminder before.
     *
     * @param reminderBefore the reminderBefore to set
     */
    public void setReminderBefore(int reminderBefore) {
	this.reminderBefore = reminderBefore;
    }

    /**
     * Gets the notice to name.
     *
     * @return the noticeToName
     */
    public String getNoticeToName() {
	return noticeToName;
    }

    /**
     * Sets the notice to name.
     *
     * @param noticeToName the noticeToName to set
     */
    public void setNoticeToName(String noticeToName) {
	this.noticeToName = noticeToName;
    }

    /**
     * Gets the notice from name.
     *
     * @return the noticeFromName
     */
    public String getNoticeFromName() {
	return noticeFromName;
    }

    /**
     * Sets the notice from name.
     *
     * @param noticeFromName the noticeFromName to set
     */
    public void setNoticeFromName(String noticeFromName) {
	this.noticeFromName = noticeFromName;
    }

    /**
     * Gets the radio list.
     *
     * @return the radio list
     */
    public List<String> getRadioList() {
	return radioList;
    }

    /**
     * Sets the radio list.
     *
     * @param radioList the new radio list
     */
    public void setRadioList(List<String> radioList) {
	this.radioList = radioList;
    }

    /**
     * Gets the check list.
     *
     * @return the check list
     */
    public List<String> getCheckList() {
	return checkList;
    }

    /**
     * Sets the check list.
     *
     * @param checkList the new check list
     */
    public void setCheckList(List<String> checkList) {
	this.checkList = checkList;
    }

    /**
     * Gets the checked value.
     *
     * @return the checked value
     */
    public String getCheckedValue() {
	return checkedValue;
    }

    /**
     * Sets the checked value.
     *
     * @param checkedValue the new checked value
     */
    public void setCheckedValue(String checkedValue) {
	this.checkedValue = checkedValue;
    }

    /**
     * Gets the class rooms.
     *
     * @return the classRooms
     */
    public List<ClassRoom> getClassRooms() {
	return classRooms;
    }

    /**
     * Sets the class rooms.
     *
     * @param classRooms the classRooms to set
     */
    public void setClassRooms(List<ClassRoom> classRooms) {
	this.classRooms = classRooms;
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

    /**
     * Gets the selected radio type.
     *
     * @return the selectedRadioType
     */
    public String getSelectedRadioType() {
	return selectedRadioType;
    }

    /**
     * Sets the selected radio type.
     *
     * @param selectedRadioType the selectedRadioType to set
     */
    public void setSelectedRadioType(String selectedRadioType) {
	this.selectedRadioType = selectedRadioType;
    }

    /**
     * Gets the time.
     *
     * @return the time
     */
    public String getTime() {
	return time;
    }

    /**
     * Sets the time.
     *
     * @param time the time to set
     */
    public void setTime(String time) {
	this.time = time;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Notice [id=" + id + ", title=" + title + ", msg=" + msg
		+ ", radioList=" + radioList + ", checkList=" + checkList
		+ ", checkedValue=" + checkedValue + ", dueDate=" + dueDate
		+ ", reminderBefore=" + reminderBefore + ", selectedRadioType="
		+ selectedRadioType + ", userFromName=" + userFromName
		+ ", userToName=" + userToName + ", noticeToName="
		+ noticeToName + ", noticeFromName=" + noticeFromName
		+ ", studentsList=" + studentsList + ", classRooms="
		+ classRooms + ", time=" + time + ", noticeSentDate="
		+ noticeSentDate + ", getDueDate()=" + getDueDate() + "]";
    }

    /**
     * Gets the due date.
     *
     * @return the dueDate
     */
    public Date getDueDate() {
	return dueDate;
    }

    /**
     * Sets the due date.
     *
     * @param dueDate the dueDate to set
     */
    /*
     * public void setDueDate(Date dueDate) {
     * 
     * this.dueDate = dueDate; }
     */

    public void setDueDate(Object dueDate) {

	Date date=null;
	try {
	    if(dueDate instanceof String)
		date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH) .parse((String)dueDate);
	    else if(dueDate instanceof  Long) {
		date =new   Date((Long)dueDate);
		date =  new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH) .parse(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date)); ;
	    }
	} catch (final ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	this.dueDate =date;

    }


    /**
     * Sets the due date as date.
     *
     * @param date the new due date as date
     */
    public void setDueDateAsDate(Date date) {
	dueDate = date;
    }

    /**
     * Gets the user from name.
     *
     * @return the userFromName
     */
    public String getUserFromName() {
	return userFromName;
    }

    /**
     * Sets the user from name.
     *
     * @param userFromName the userFromName to set
     */
    public void setUserFromName(String userFromName) {
	this.userFromName = userFromName;
    }

    /**
     * Gets the user to name.
     *
     * @return the userToName
     */
    public String getUserToName() {
	return userToName;
    }

    /**
     * Sets the user to name.
     *
     * @param userToName the userToName to set
     */
    public void setUserToName(String userToName) {
	this.userToName = userToName;
    }

    /**
     * Gets the notice sent date.
     *
     * @return the noticeSentDate
     */
    public Date getNoticeSentDate() {
	return noticeSentDate;
    }

    /**
     * Sets the notice sent date.
     *
     * @param noticeSentDate the noticeSentDate to set
     */
    public void setNoticeSentDate(Date noticeSentDate) {
	this.noticeSentDate = noticeSentDate;
    }

}
