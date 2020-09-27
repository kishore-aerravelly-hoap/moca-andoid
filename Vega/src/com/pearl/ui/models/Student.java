package com.pearl.ui.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

// TODO: Auto-generated Javadoc
/**
 * The Class Student.
 */
public class Student implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id. */
    private String id;
    
    /** The student id. */
    private String studentId;
    
    /** The student id. */
    private String studentID;
    
    /** The first name. */
    private String firstName;
    
    /** The second name. */
    private String secondName;
    
    /** The sur name. */
    private String surName;
    
    /** The father name. */
    private String fatherName;
    
    /** The image url. */
    private String imageUrl;
    
    /** The gender. */
    private String gender;
    
    /** The email. */
    private String email;
    
    /** The phone number. */
    private String phoneNumber;
    
    /** The address. */
    private Address address;
    
    /** The class room. */
    private ClassRoom classRoom;
    
    /** The date. */
    private Date date;
    
    /** The mobile number. */
    private String mobileNumber;
    
    /** The citizenship. */
    private String citizenship;
    
    /** The religion. */
    private String religion;
    
    /** The date of birth. */
    private Date dateOfBirth;
    
    /** The place of birth. */
    private String placeOfBirth;
    
    /** The special section. */
    private String specialSection;
    
    /** The student full name. */
    private String studentFullName;
    
    /** The student image. */
    private String studentImage;

    /** The permanent address. */
    private String permanentAddress;
    
    /** The present address. */
    private String presentAddress;
    
    /** The school last attended. */
    private String schoolLastAttended;
    
    /** The grade or year completed. */
    private String gradeOrYearCompleted;
    
    /** The school year. */
    private String schoolYear;
    
    /** The address of school. */
    private String addressOfSchool;
    
    /** The monitor. */
    private boolean monitor;
    
    /** The prep school information. */
    private List<AcadamicBackGroundInformation> prepSchoolInformation;
    
    /** The grade school information. */
    private List<AcadamicBackGroundInformation> gradeSchoolInformation;
    
    /** The high school information. */
    private List<AcadamicBackGroundInformation> highSchoolInformation;

    /** The acr no. */
    private String acrNo;
    
    /** The acr no when issued. */
    private String acrNoWhenIssued;
    
    /** The acr no where issued. */
    private String acrNoWhereIssued;
    
    /** The perminent resident. */
    private boolean perminentResident;
    
    /** The temporary resident. */
    private boolean temporaryResident;
    
    /** The temporary resident details. */
    private TemporaryResident temporaryResidentDetails;

    /** The employer details. */
    private EmployerDetails employerDetails;

    /**
     * Gets the class room.
     *
     * @return the classRoom
     */

    @JsonIgnore
    public ClassRoom getClassRoom() {
	return classRoom;
    }

    /**
     * Sets the class room.
     *
     * @param classRoom the classRoom to set
     */
    public void setClassRoom(ClassRoom classRoom) {
	this.classRoom = classRoom;
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
     * Gets the student id.
     *
     * @return the studentId
     */
    public String getStudentId() {
	return studentId;
    }

    /**
     * Sets the student id.
     *
     * @param studentId the studentId to set
     */
    public void setStudentId(String studentId) {
	this.studentId = studentId;
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Student [id=" + id + ", studentId=" + studentId + ",studentID="
		+ studentID + ", firstName=" + firstName + ", secondName="
		+ secondName + ", surName=" + surName + ", fatherName="
		+ fatherName + ", imageUrl=" + imageUrl + ", gender=" + gender
		+ ", email=" + email + ", phoneNumber=" + phoneNumber
		+ ", address=" + address + ", classRoom=" + classRoom
		+ ", date=" + date + ", mobileNumber=" + mobileNumber
		+ ", citizenship=" + citizenship + ", religion=" + religion
		+ ", dateOfBirth=" + dateOfBirth + ", placeOfBirth="
		+ placeOfBirth + ", specialSection=" + specialSection
		+ ", permanentAddress=" + permanentAddress
		+ ", presentAddress=" + presentAddress
		+ ", schoolLastAttended=" + schoolLastAttended
		+ ", gradeOrYearCompleted=" + gradeOrYearCompleted
		+ ", schoolYear=" + schoolYear + ", addressOfSchool="
		+ addressOfSchool + ", monitor=" + monitor
		+ ", prepSchoolInformation=" + prepSchoolInformation
		+ ", gradeSchoolInformation=" + gradeSchoolInformation
		+ ", highSchoolInformation=" + highSchoolInformation
		+ ", acrNo=" + acrNo + ", acrNoWhenIssued=" + acrNoWhenIssued
		+ ", acrNoWhereIssued=" + acrNoWhereIssued
		+ ", perminentResident=" + perminentResident
		+ ", temporaryResident=" + temporaryResident
		+ ", temporaryResidentDetails=" + temporaryResidentDetails
		+ ", employerDetails=" + employerDetails
		+ ", getStudentFullName()=" + getStudentFullName() + "]";
    }

    /**
     * Sets the date.
     *
     * @param date the date to set
     */
    public void setDate(Date date) {
	this.date = date;
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public Date getDate() {
	return date;
    }

    /*
     * public String getStudentFullName(){ return
     * this.firstName+" "+this.secondName+" "+this.surName; }
     */
    /*
     * public String getStudentImage(){ return
     * "<img src=imageControllerById?userId="
     * +this.studentId+" width='40' height='40'>"; }
     */
    /**
     * Gets the mobile number.
     *
     * @return the mobileNumber
     */
    public String getMobileNumber() {
	return mobileNumber;
    }

    /**
     * Sets the mobile number.
     *
     * @param mobileNumber the mobileNumber to set
     */
    public void setMobileNumber(String mobileNumber) {
	this.mobileNumber = mobileNumber;
    }

    /**
     * Gets the citizenship.
     *
     * @return the citizenship
     */
    public String getCitizenship() {
	return citizenship;
    }

    /**
     * Sets the citizenship.
     *
     * @param citizenship the citizenship to set
     */
    public void setCitizenship(String citizenship) {
	this.citizenship = citizenship;
    }

    /**
     * Gets the religion.
     *
     * @return the religion
     */
    public String getReligion() {
	return religion;
    }

    /**
     * Sets the religion.
     *
     * @param religion the religion to set
     */
    public void setReligion(String religion) {
	this.religion = religion;
    }

    /**
     * Gets the place of birth.
     *
     * @return the placeOfBirth
     */
    public String getPlaceOfBirth() {
	return placeOfBirth;
    }

    /**
     * Sets the place of birth.
     *
     * @param placeOfBirth the placeOfBirth to set
     */
    public void setPlaceOfBirth(String placeOfBirth) {
	this.placeOfBirth = placeOfBirth;
    }

    /**
     * Gets the date of birth.
     *
     * @return the dateOfBirth
     */
    public Date getDateOfBirth() {
	return dateOfBirth;
    }

    /**
     * Sets the as date of birth.
     *
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setAsDateOfBirth(Date dateOfBirth) {
	this.dateOfBirth = dateOfBirth;
    }

    /**
     * Sets the date of birth.
     *
     * @param dateOfBirth the new date of birth
     */
    public void setDateOfBirth(Date dateOfBirth) {
	this.dateOfBirth = dateOfBirth;// CommonUtility.convertStringToDate(dateOfBirth,
	// "MM/dd/yyyy");
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
     * Gets the school last attended.
     *
     * @return the schoolLastAttended
     */
    public String getSchoolLastAttended() {
	return schoolLastAttended;
    }

    /**
     * Sets the school last attended.
     *
     * @param schoolLastAttended the schoolLastAttended to set
     */
    public void setSchoolLastAttended(String schoolLastAttended) {
	this.schoolLastAttended = schoolLastAttended;
    }

    /**
     * Gets the grade or year completed.
     *
     * @return the gradeOrYearCompleted
     */
    public String getGradeOrYearCompleted() {
	return gradeOrYearCompleted;
    }

    /**
     * Sets the grade or year completed.
     *
     * @param gradeOrYearCompleted the gradeOrYearCompleted to set
     */
    public void setGradeOrYearCompleted(String gradeOrYearCompleted) {
	this.gradeOrYearCompleted = gradeOrYearCompleted;
    }

    /**
     * Gets the school year.
     *
     * @return the schoolYear
     */
    public String getSchoolYear() {
	return schoolYear;
    }

    /**
     * Sets the school year.
     *
     * @param schoolYear the schoolYear to set
     */
    public void setSchoolYear(String schoolYear) {
	this.schoolYear = schoolYear;
    }

    /**
     * Gets the address of school.
     *
     * @return the addressOfSchool
     */
    public String getAddressOfSchool() {
	return addressOfSchool;
    }

    /**
     * Sets the address of school.
     *
     * @param addressOfSchool the addressOfSchool to set
     */
    public void setAddressOfSchool(String addressOfSchool) {
	this.addressOfSchool = addressOfSchool;
    }

    /**
     * Checks if is monitor.
     *
     * @return the monitor
     */
    public boolean isMonitor() {
	return monitor;
    }

    /**
     * Sets the monitor.
     *
     * @param monitor the monitor to set
     */
    public void setMonitor(boolean monitor) {
	this.monitor = monitor;
    }

    /**
     * Gets the prep school information.
     *
     * @return the prepSchoolInformation
     */
    public List<AcadamicBackGroundInformation> getPrepSchoolInformation() {
	return prepSchoolInformation;
    }

    /**
     * Sets the prep school information.
     *
     * @param prepSchoolInformation the prepSchoolInformation to set
     */
    public void setPrepSchoolInformation(
	    List<AcadamicBackGroundInformation> prepSchoolInformation) {
	this.prepSchoolInformation = prepSchoolInformation;
    }

    /**
     * Gets the grade school information.
     *
     * @return the gradeSchoolInformation
     */
    public List<AcadamicBackGroundInformation> getGradeSchoolInformation() {
	return gradeSchoolInformation;
    }

    /**
     * Sets the grade school information.
     *
     * @param gradeSchoolInformation the gradeSchoolInformation to set
     */
    public void setGradeSchoolInformation(
	    List<AcadamicBackGroundInformation> gradeSchoolInformation) {
	this.gradeSchoolInformation = gradeSchoolInformation;
    }

    /**
     * Gets the high school information.
     *
     * @return the highSchoolInformation
     */
    public List<AcadamicBackGroundInformation> getHighSchoolInformation() {
	return highSchoolInformation;
    }

    /**
     * Sets the high school information.
     *
     * @param highSchoolInformation the highSchoolInformation to set
     */
    public void setHighSchoolInformation(
	    List<AcadamicBackGroundInformation> highSchoolInformation) {
	this.highSchoolInformation = highSchoolInformation;
    }

    /**
     * Gets the employer details.
     *
     * @return the employerDetails
     */
    public EmployerDetails getEmployerDetails() {
	return employerDetails;
    }

    /**
     * Sets the employer details.
     *
     * @param employerDetails the employerDetails to set
     */
    public void setEmployerDetails(EmployerDetails employerDetails) {
	this.employerDetails = employerDetails;
    }

    /**
     * Gets the special section.
     *
     * @return the specialSection
     */
    public String getSpecialSection() {
	return specialSection;
    }

    /**
     * Sets the special section.
     *
     * @param specialSection the specialSection to set
     */
    public void setSpecialSection(String specialSection) {
	this.specialSection = specialSection;
    }

    /**
     * Gets the student id.
     *
     * @return the studentID
     */
    public String getStudentID() {
	return studentID;
    }

    /**
     * Sets the student id.
     *
     * @param studentID the studentID to set
     */
    public void setStudentID(String studentID) {
	this.studentID = studentID;
    }

    /**
     * Sets the student full name.
     *
     * @param studentFullName the studentFullName to set
     */
    public void setStudentFullName(String studentFullName) {
	this.studentFullName = studentFullName;
    }

    /**
     * Gets the student full name.
     *
     * @return the student full name
     */
    public String getStudentFullName() {
	return studentFullName;
    }

    /**
     * Gets the student image.
     *
     * @return the studentImage
     */
    public String getStudentImage() {
	return studentImage;
    }

    /**
     * Sets the student image.
     *
     * @param studentImage the studentImage to set
     */
    public void setStudentImage(String studentImage) {
	this.studentImage = studentImage;
    }

}
