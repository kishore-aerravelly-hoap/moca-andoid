/**
 * 
 */
package com.pearl.ui.models;

import java.io.Serializable;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class User.
 *
 * @author kpamulapati
 */
public class User implements Serializable {

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
    
    /** The role. */
    private List<Role> role;

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
     * Gets the role.
     *
     * @return the role
     */
    public List<Role> getRole() {
	return role;
    }

    /**
     * Sets the role.
     *
     * @param role the role to set
     */
    public void setRole(List<Role> role) {
	this.role = role;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "User [id=" + id + ", password=" + password + ", firstName="
		+ firstName + ", secondName=" + secondName + ", fatherName="
		+ fatherName + ", motherName=" + motherName + ", address="
		+ address + ", role=" + role + "]";
    }

}
