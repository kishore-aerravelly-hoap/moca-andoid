/**
 * 
 */
package com.pearl.ui.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class EmployerDetails.
 */
public class EmployerDetails implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    private String id;
    
    /** The login name. */
    private String loginName;
    
    /** The password. */
    private String password;
    
    /** The company name. */
    private String companyName;
    
    /** The company address. */
    private String companyAddress;
    
    /** The phone number. */
    private String phoneNumber;
    
    /** The user id. */
    private String userId;
    
    /** The e mail. */
    private String eMail;

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
     * Gets the login name.
     *
     * @return the loginName
     */
    public String getLoginName() {
	return loginName;
    }

    /**
     * Sets the login name.
     *
     * @param loginName the loginName to set
     */
    public void setLoginName(String loginName) {
	this.loginName = loginName;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
	return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
	this.password = password;
    }

    /**
     * Gets the company name.
     *
     * @return the companyName
     */
    public String getCompanyName() {
	return companyName;
    }

    /**
     * Sets the company name.
     *
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
	this.companyName = companyName;
    }

    /**
     * Gets the company address.
     *
     * @return the companyAddress
     */
    public String getCompanyAddress() {
	return companyAddress;
    }

    /**
     * Sets the company address.
     *
     * @param companyAddress the companyAddress to set
     */
    public void setCompanyAddress(String companyAddress) {
	this.companyAddress = companyAddress;
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
     * Gets the user id.
     *
     * @return the userId
     */
    public String getUserId() {
	return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
	this.userId = userId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "EmployerDetails [id=" + id + ", loginName=" + loginName
		+ ", password=" + password + ", companyName=" + companyName
		+ ", companyAddress=" + companyAddress + ", phoneNumber="
		+ phoneNumber + ", userId=" + userId + ", eMail=" + eMail + "]";
    }

    /**
     * Gets the e mail.
     *
     * @return the eMail
     */
    public String geteMail() {
	return eMail;
    }

    /**
     * Sets the e mail.
     *
     * @param eMail the eMail to set
     */
    public void seteMail(String eMail) {
	this.eMail = eMail;
    }

}
