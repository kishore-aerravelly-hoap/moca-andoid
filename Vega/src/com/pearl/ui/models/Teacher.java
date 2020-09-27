/**
 * 
 */
package com.pearl.ui.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Teacher.
 *
 * @author kpamulapati
 */
public class Teacher implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id. */
    private String id;
    
    /** The teacher id. */
    private String teacherId;
    
    /** The sur name. */
    private String surName;
    
    /** The first name. */
    private String firstName;
    
    /** The second name. */
    private String secondName;
    
    /** The father name. */
    private String fatherName;
    
    /** The image url. */
    private String imageUrl;
    
    /** The address. */
    private Address address;
    
    /** The teacher full name. */
    private String teacherFullName;
    
    /** The email. */
    private String email;
    
    /** The phone number. */
    private String phoneNumber;
    
    /** The gender. */
    private String gender;
    
    /** The class rooms list. */
    List<ClassRoom> classRoomsList = new ArrayList<ClassRoom>();
    
    /** The permanent address. */
    private String permanentAddress;
    
    /** The present address. */
    private String presentAddress;

    /** The acr no. */
    private String acrNo;
    
    /** The acr no when issued. */
    private String acrNoWhenIssued;
    
    /** The acr no where issued. */
    private String acrNoWhereIssued;
    
    /** The perminent resident. */
    private boolean perminentResident;
    
    /** The temporary resident. */
    private boolean  temporaryResident;
    
    /** The temporary resident details. */
    private TemporaryResident temporaryResidentDetails;

    /** The language. */
    private String language;
    
    /** The computers. */
    private String computers;
    
    /** The driving. */
    private String driving;
    
    /** The cpr. */
    private String cpr;
    
    /** The sports. */
    private String sports;

    /** The rotary. */
    private String rotary;
    
    /** The toastmasters. */
    private String toastmasters;
    
    /** The others. */
    private String others;

    /** The trainings attended. */
    private String trainingsAttended; 



    /**
     * Gets the class rooms list.
     *
     * @return the classRoomsList
     */
    public List<ClassRoom> getClassRoomsList() {
	return classRoomsList;
    }

    /**
     * Sets the class rooms list.
     *
     * @param classRoomsList the classRoomsList to set
     */
    public void setClassRoomsList(List<ClassRoom> classRoomsList) {
	this.classRoomsList = classRoomsList;
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
     * @param id the id to set
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * Gets the teacher id.
     *
     * @return the teacherId
     */
    public String getTeacherId() {
	return teacherId;
    }

    /**
     * Sets the teacher id.
     *
     * @param teacherId the teacherId to set
     */
    public void setTeacherId(String teacherId) {
	this.teacherId = teacherId;
    }

    /**
     * Gets the sur name.
     *
     * @return the surName
     */
    public String getSurName() {
	return surName;
    }

    /**
     * Sets the sur name.
     *
     * @param surName the surName to set
     */
    public void setSurName(String surName) {
	this.surName = surName;
    }

    /**
     * Gets the first name.
     *
     * @return the firstName
     */
    public String getFirstName() {
	return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    /**
     * Gets the second name.
     *
     * @return the secondName
     */
    public String getSecondName() {
	return secondName;
    }

    /**
     * Sets the second name.
     *
     * @param secondName the secondName to set
     */
    public void setSecondName(String secondName) {
	this.secondName = secondName;
    }

    /**
     * Gets the father name.
     *
     * @return the fatherName
     */
    public String getFatherName() {
	return fatherName;
    }

    /**
     * Sets the father name.
     *
     * @param fatherName the fatherName to set
     */
    public void setFatherName(String fatherName) {
	this.fatherName = fatherName;
    }


    /**
     * Gets the image url.
     *
     * @return the imageUrl
     */
    public String getImageUrl() {
	return imageUrl;
    }

    /**
     * Sets the image url.
     *
     * @param imageUrl the imageUrl to set
     */
    public void setImageUrl(String imageUrl) {
	this.imageUrl = imageUrl;
    }

    /**
     * Gets the address.
     *
     * @return the address
     */
    public Address getAddress() {
	return address;
    }

    /**
     * Sets the address.
     *
     * @param address the address to set
     */
    public void setAddress(Address address) {
	this.address = address;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
	return email;
    }

    /**
     * Sets the email.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
	this.email = email;
    }

    /**
     * Gets the phone number.
     *
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
	return phoneNumber;
    }

    /**
     * Sets the phone number.
     *
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the gender.
     *
     * @return the gender
     */
    public String getGender() {
	return gender;
    }

    /**
     * Sets the gender.
     *
     * @param gender the gender to set
     */
    public void setGender(String gender) {
	this.gender = gender;
    }



    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Teacher [id=" + id + ", teacherId=" + teacherId + ", surName="
		+ surName + ", firstName=" + firstName + ", secondName="
		+ secondName + ", fatherName=" + fatherName + ", imageUrl="
		+ imageUrl + ", address=" + address + ", email=" + email
		+ ", phoneNumber=" + phoneNumber + ", gender=" + gender
		+ ", classRoomsList=" + classRoomsList + ", permanentAddress="
		+ permanentAddress + ", presentAddress=" + presentAddress
		+ ", acrNo=" + acrNo + ", acrNoWhenIssued=" + acrNoWhenIssued
		+ ", acrNoWhereIssued=" + acrNoWhereIssued
		+ ", perminentResident=" + perminentResident
		+ ", temporaryResident=" + temporaryResident
		+ ", temporaryResidentDetails=" + temporaryResidentDetails
		+ ", language=" + language + ", computers=" + computers
		+ ", driving=" + driving + ", cpr=" + cpr + ", sports="
		+ sports + ", rotary=" + rotary + ", toastmasters="
		+ toastmasters + ", others=" + others + ", trainingsAttended="
		+ trainingsAttended + ", getTeacherFullName()="
		+ "]";
    }

    /**
     * Gets the teacher full name.
     *
     * @return the teacher full name
     */
    public String getTeacherFullName() {
	return teacherFullName;
    }

    /**
     * Sets the teacher full name.
     *
     * @param teacherFullName the new teacher full name
     */
    public void setTeacherFullName(String teacherFullName) {
	this.teacherFullName = teacherFullName;
    }

    /**
     * Gets the acr no.
     *
     * @return the acrNo
     */
    public String getAcrNo() {
	return acrNo;
    }

    /**
     * Sets the acr no.
     *
     * @param acrNo the acrNo to set
     */
    public void setAcrNo(String acrNo) {
	this.acrNo = acrNo;
    }

    /**
     * Gets the acr no when issued.
     *
     * @return the acrNoWhenIssued
     */
    public String getAcrNoWhenIssued() {
	return acrNoWhenIssued;
    }

    /**
     * Sets the acr no when issued.
     *
     * @param acrNoWhenIssued the acrNoWhenIssued to set
     */
    public void setAcrNoWhenIssued(String acrNoWhenIssued) {
	this.acrNoWhenIssued = acrNoWhenIssued;
    }

    /**
     * Gets the acr no where issued.
     *
     * @return the acrNoWhereIssued
     */
    public String getAcrNoWhereIssued() {
	return acrNoWhereIssued;
    }

    /**
     * Sets the acr no where issued.
     *
     * @param acrNoWhereIssued the acrNoWhereIssued to set
     */
    public void setAcrNoWhereIssued(String acrNoWhereIssued) {
	this.acrNoWhereIssued = acrNoWhereIssued;
    }

    /**
     * Checks if is perminent resident.
     *
     * @return the perminentResident
     */
    public boolean isPerminentResident() {
	return perminentResident;
    }

    /**
     * Sets the perminent resident.
     *
     * @param perminentResident the perminentResident to set
     */
    public void setPerminentResident(boolean perminentResident) {
	this.perminentResident = perminentResident;
    }

    /**
     * Checks if is temporary resident.
     *
     * @return the temporaryResident
     */
    public boolean isTemporaryResident() {
	return temporaryResident;
    }

    /**
     * Sets the temporary resident.
     *
     * @param temporaryResident the temporaryResident to set
     */
    public void setTemporaryResident(boolean temporaryResident) {
	this.temporaryResident = temporaryResident;
    }

    /**
     * Gets the temporary resident details.
     *
     * @return the temporaryResidentDetails
     */
    public TemporaryResident getTemporaryResidentDetails() {
	return temporaryResidentDetails;
    }

    /**
     * Sets the temporary resident details.
     *
     * @param temporaryResidentDetails the temporaryResidentDetails to set
     */
    public void setTemporaryResidentDetails(
	    TemporaryResident temporaryResidentDetails) {
	this.temporaryResidentDetails = temporaryResidentDetails;
    }

    /**
     * Gets the permanent address.
     *
     * @return the permanentAddress
     */
    public String getPermanentAddress() {
	return permanentAddress;
    }

    /**
     * Sets the permanent address.
     *
     * @param permanentAddress the permanentAddress to set
     */
    public void setPermanentAddress(String permanentAddress) {
	this.permanentAddress = permanentAddress;
    }

    /**
     * Gets the present address.
     *
     * @return the presentAddress
     */
    public String getPresentAddress() {
	return presentAddress;
    }

    /**
     * Sets the present address.
     *
     * @param presentAddress the presentAddress to set
     */
    public void setPresentAddress(String presentAddress) {
	this.presentAddress = presentAddress;
    }

    /**
     * Gets the language.
     *
     * @return the language
     */
    public String getLanguage() {
	return language;
    }

    /**
     * Sets the language.
     *
     * @param language the language to set
     */
    public void setLanguage(String language) {
	this.language = language;
    }

    /**
     * Gets the computers.
     *
     * @return the computers
     */
    public String getComputers() {
	return computers;
    }

    /**
     * Sets the computers.
     *
     * @param computers the computers to set
     */
    public void setComputers(String computers) {
	this.computers = computers;
    }

    /**
     * Gets the driving.
     *
     * @return the driving
     */
    public String getDriving() {
	return driving;
    }

    /**
     * Sets the driving.
     *
     * @param driving the driving to set
     */
    public void setDriving(String driving) {
	this.driving = driving;
    }

    /**
     * Gets the cpr.
     *
     * @return the cpr
     */
    public String getCpr() {
	return cpr;
    }

    /**
     * Sets the cpr.
     *
     * @param cpr the cpr to set
     */
    public void setCpr(String cpr) {
	this.cpr = cpr;
    }

    /**
     * Gets the sports.
     *
     * @return the sports
     */
    public String getSports() {
	return sports;
    }

    /**
     * Sets the sports.
     *
     * @param sports the sports to set
     */
    public void setSports(String sports) {
	this.sports = sports;
    }

    /**
     * Gets the rotary.
     *
     * @return the rotary
     */
    public String getRotary() {
	return rotary;
    }

    /**
     * Sets the rotary.
     *
     * @param rotary the rotary to set
     */
    public void setRotary(String rotary) {
	this.rotary = rotary;
    }

    /**
     * Gets the toastmasters.
     *
     * @return the toastmasters
     */
    public String getToastmasters() {
	return toastmasters;
    }

    /**
     * Sets the toastmasters.
     *
     * @param toastmasters the toastmasters to set
     */
    public void setToastmasters(String toastmasters) {
	this.toastmasters = toastmasters;
    }

    /**
     * Gets the others.
     *
     * @return the others
     */
    public String getOthers() {
	return others;
    }

    /**
     * Sets the others.
     *
     * @param others the others to set
     */
    public void setOthers(String others) {
	this.others = others;
    }

    /**
     * Gets the trainings attended.
     *
     * @return the trainingsAttended
     */
    public String getTrainingsAttended() {
	return trainingsAttended;
    }

    /**
     * Sets the trainings attended.
     *
     * @param trainingsAttended the trainingsAttended to set
     */
    public void setTrainingsAttended(String trainingsAttended) {
	this.trainingsAttended = trainingsAttended;
    }




}
