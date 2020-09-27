/**
 * 
 */
package com.pearl.baseresponse.login;

import java.io.Serializable;
import java.util.List;

import com.pearl.ui.models.Address;
import com.pearl.ui.models.Role;


// TODO: Auto-generated Javadoc
/**
 * The Class AndroidUser.
 */
public class AndroidUser implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    private String id;
    
    /** The password. */
    private String password;
    
    /** The first name. */
    private String firstName;
    
    /** The second name. */
    private String secondName;
    
    /** The father name. */
    private String fatherName;
    
    /** The mother name. */
    private String motherName;
    
    /** The address. */
    private Address address;
    
    /** The grade id. */
    private String gradeId;
    
    /** The grade name. */
    private String gradeName;
    
    /** The section name. */
    private String sectionName;
    
    /** The auth. */
    private String auth;
    
    /** The present address. */
    private String presentAddress;
    
    /** The permanent address. */
    private String permanentAddress;
    
    /** The class monitor. */
    private boolean classMonitor;
    
    /** The roles. */
    private List<Role> roles;
    
    /** The user type. */
    private String userType;
    /*private List<String> questionType;
	public List<String> getQuestionType() {
		return questionType;
	}
	public void setQuestionType(List<String> questionType) {
		this.questionType = questionType;
	}*/
    //private String userName;
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
     * Gets the mother name.
     *
     * @return the motherName
     */
    public String getMotherName() {
	return motherName;
    }
    
    /**
     * Sets the mother name.
     *
     * @param motherName the motherName to set
     */
    public void setMotherName(String motherName) {
	this.motherName = motherName;
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
     * Gets the roles.
     *
     * @return the role
     */
    public List<Role> getRoles() {
	return roles;
    }
    
    /**
     * Sets the roles.
     *
     * @param roles the new roles
     */
    public void setRoles(List<Role> roles) {
	this.roles = roles;
    }
    
    /**
     * Gets the grade id.
     *
     * @return the gradeId
     */
    public String getGradeId() {
	return gradeId;
    }
    
    /**
     * Sets the grade id.
     *
     * @param gradeId the gradeId to set
     */
    public void setGradeId(String gradeId) {
	this.gradeId = gradeId;
    }
    
    /**
     * Gets the grade name.
     *
     * @return the gradeName
     */
    public String getGradeName() {
	return gradeName;
    }
    
    /**
     * Sets the grade name.
     *
     * @param gradeName the gradeName to set
     */
    public void setGradeName(String gradeName) {
	this.gradeName = gradeName;
    }
    
    /**
     * Gets the section name.
     *
     * @return the sectionName
     */
    public String getSectionName() {
	return sectionName;
    }
    
    /**
     * Sets the section name.
     *
     * @param sectionName the sectionName to set
     */
    public void setSectionName(String sectionName) {
	this.sectionName = sectionName;
    }
    
    /**
     * Gets the auth.
     *
     * @return the auth
     */
    public String getAuth() {
	return auth;
    }
    
    /**
     * Sets the auth.
     *
     * @param auth the auth to set
     */
    public void setAuth(String auth) {
	this.auth = auth;
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
     * Checks if is class monitor.
     *
     * @return the classMonitor
     */
    public boolean isClassMonitor() {
	return classMonitor;
    }
    
    /**
     * Sets the class monitor.
     *
     * @param classMonitor the classMonitor to set
     */
    public void setClassMonitor(boolean classMonitor) {
	this.classMonitor = classMonitor;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "AndroidUser [id=" + id + ", password=" + password
		+ ", firstName=" + firstName + ", secondName=" + secondName
		+ ", fatherName=" + fatherName + ", motherName=" + motherName
		+ ", address=" + address + ", gradeId=" + gradeId
		+ ", gradeName=" + gradeName + ", sectionName=" + sectionName
		+ ", auth=" + auth + ", presentAddress=" + presentAddress
		+ ", permanentAddress=" + permanentAddress + ", classMonitor="
		+ classMonitor + ", roles=" + roles + ", userType=" + userType
		+ "]";
    }
    
    /**
     * Gets the user type.
     *
     * @return the userType
     */
    public String getUserType() {
	return userType;
    }
    
    /**
     * Sets the user type.
     *
     * @param userType the userType to set
     */
    public void setUserType(String userType) {
	this.userType = userType;
    }
    /**
     * @return the userName
     */
    /*public String getUserName() {
		return userName;
	}*/
    /**
     * @param userName the userName to set
     */
    /*public void setUserName(String userName) {
		this.userName = userName;
	}*/

}
