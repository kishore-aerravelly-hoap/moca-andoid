/**
 * 
 */
package com.pearl.ui.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

// TODO: Auto-generated Javadoc
/**
 * The Class Parent.
 *
 * @author kpamulapati
 */
public class Parent implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The id. */
    private String id;
    
    /** The parent id. */
    private String parentId;
    
    /** The parent name. */
    private String parentName;
    
    /** The parent mobile. */
    private String parentMobile;
    
    /** The parent email. */
    private String parentEmail;
    
    /** The gender. */
    private String gender;


    /** The parent school details. */
    @SuppressWarnings("unchecked")
    private List<AcadamicBackGroundInformation> parentSchoolDetails=LazyList.decorate(new ArrayList<AcadamicBackGroundInformation>(),FactoryUtils.instantiateFactory(AcadamicBackGroundInformation.class));
    
    /** The religion. */
    private String religion;
    
    /** The occupation. */
    private String occupation;
    
    /** The firm. */
    private String firm;
    
    /** The office address. */
    private String officeAddress;
    
    /** The office number. */
    private String officeNumber;
    
    /** The residence. */
    private String residence;
    
    /** The persons count. */
    private String personsCount;
    
    /** The grade year. */
    private String gradeYear;
    
    /** The image url. */
    private String imageUrl;


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
     * Gets the parent id.
     *
     * @return the parentId
     */
    public String getParentId() {
	return parentId;
    }
    
    /**
     * Sets the parent id.
     *
     * @param parentId the parentId to set
     */
    public void setParentId(String parentId) {
	this.parentId = parentId;
    }
    
    /**
     * Gets the parent name.
     *
     * @return the parentName
     */
    public String getParentName() {
	return parentName;
    }
    
    /**
     * Sets the parent name.
     *
     * @param parentName the parentName to set
     */
    public void setParentName(String parentName) {
	this.parentName = parentName;
    }
    
    /**
     * Gets the parent mobile.
     *
     * @return the parentMobile
     */
    public String getParentMobile() {
	return parentMobile;
    }
    
    /**
     * Sets the parent mobile.
     *
     * @param parentMobile the parentMobile to set
     */
    public void setParentMobile(String parentMobile) {
	this.parentMobile = parentMobile;
    }
    
    /**
     * Gets the parent email.
     *
     * @return the parentEmail
     */
    public String getParentEmail() {
	return parentEmail;
    }
    
    /**
     * Sets the parent email.
     *
     * @param parentEmail the parentEmail to set
     */
    public void setParentEmail(String parentEmail) {
	this.parentEmail = parentEmail;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Parent [id=" + id + ", parentId=" + parentId + ", parentName="
		+ parentName + ", parentMobile=" + parentMobile
		+ ", parentEmail=" + parentEmail + ", gender=" + gender
		+ ", parentSchoolDetails=" + parentSchoolDetails
		+ ", religion=" + religion + ", occupation=" + occupation
		+ ", firm=" + firm + ", officeAddress=" + officeAddress
		+ ", officeNumber=" + officeNumber + ", residence=" + residence
		+ ", personsCount=" + personsCount + ", gradeYear=" + gradeYear
		+ ", imageUrl=" + imageUrl + "]";
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
     * Gets the parent school details.
     *
     * @return the parentSchoolDetails
     */
    public List<AcadamicBackGroundInformation> getParentSchoolDetails() {
	return parentSchoolDetails;
    }
    
    /**
     * Sets the parent school details.
     *
     * @param parentSchoolDetails the parentSchoolDetails to set
     */
    public void setParentSchoolDetails(
	    List<AcadamicBackGroundInformation> parentSchoolDetails) {
	this.parentSchoolDetails = parentSchoolDetails;
    }
    
    /**
     * Gets the occupation.
     *
     * @return the occupation
     */
    public String getOccupation() {
	return occupation;
    }
    
    /**
     * Sets the occupation.
     *
     * @param occupation the occupation to set
     */
    public void setOccupation(String occupation) {
	this.occupation = occupation;
    }
    
    /**
     * Gets the firm.
     *
     * @return the firm
     */
    public String getFirm() {
	return firm;
    }
    
    /**
     * Sets the firm.
     *
     * @param firm the firm to set
     */
    public void setFirm(String firm) {
	this.firm = firm;
    }
    
    /**
     * Gets the office address.
     *
     * @return the officeAddress
     */
    public String getOfficeAddress() {
	return officeAddress;
    }
    
    /**
     * Sets the office address.
     *
     * @param officeAddress the officeAddress to set
     */
    public void setOfficeAddress(String officeAddress) {
	this.officeAddress = officeAddress;
    }
    
    /**
     * Gets the office number.
     *
     * @return the officeNumber
     */
    public String getOfficeNumber() {
	return officeNumber;
    }
    
    /**
     * Sets the office number.
     *
     * @param officeNumber the officeNumber to set
     */
    public void setOfficeNumber(String officeNumber) {
	this.officeNumber = officeNumber;
    }
    
    /**
     * Gets the residence.
     *
     * @return the residence
     */
    public String getResidence() {
	return residence;
    }
    
    /**
     * Sets the residence.
     *
     * @param residence the residence to set
     */
    public void setResidence(String residence) {
	this.residence = residence;
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
     * Gets the persons count.
     *
     * @return the personsCount
     */
    public String getPersonsCount() {
	return personsCount;
    }
    
    /**
     * Sets the persons count.
     *
     * @param personsCount the personsCount to set
     */
    public void setPersonsCount(String personsCount) {
	this.personsCount = personsCount;
    }
    
    /**
     * Gets the grade year.
     *
     * @return the gradeYear
     */
    public String getGradeYear() {
	return gradeYear;
    }
    
    /**
     * Sets the grade year.
     *
     * @param gradeYear the gradeYear to set
     */
    public void setGradeYear(String gradeYear) {
	this.gradeYear = gradeYear;
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


}
