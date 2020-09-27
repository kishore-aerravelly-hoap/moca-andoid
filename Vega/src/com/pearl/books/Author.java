package com.pearl.books;

// TODO: Auto-generated Javadoc
/**
 * The Class Author.
 */
public class Author {
    
    /** The first name. */
    private String firstName;
    
    /** The last name. */
    private String lastName;

    /**
     * Instantiates a new author.
     */
    public Author() {
	super();
    }

    /**
     * Instantiates a new author.
     *
     * @param firstName the first name
     * @param lastName the last name
     */
    public Author(String firstName, String lastName) {
	super();
	this.firstName = firstName;
	this.lastName = lastName;
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
     * Gets the last name.
     *
     * @return the lastName
     */
    public String getLastName() {
	return lastName;
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
     * Sets the last name.
     *
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
	this.lastName = lastName;
    }
}