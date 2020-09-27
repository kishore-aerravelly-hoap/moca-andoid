package com.pearl.analytics.models;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class Percentage.
 */
public class Percentage implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	private String id;
	
	/** The percentage grade. */
	private String percentageGrade;
	
	/** The to value. */
	private String toValue;
	
	/** The from value. */
	private String fromValue;
	
	/**
	 * Instantiates a new percentage.
	 */
	public Percentage(){
		
	}
	
	/**
	 * Instantiates a new percentage.
	 *
	 * @param id the id
	 * @param percentageGrade the percentage grade
	 * @param toValue the to value
	 * @param fromValue the from value
	 */
	public Percentage(String id, String percentageGrade, String toValue,
			String fromValue) {
		super();
		this.id = id;
		this.percentageGrade = percentageGrade;
		this.toValue = toValue;
		this.fromValue = fromValue;
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
	 * Gets the percentage grade.
	 *
	 * @return the percentage grade
	 */
	public String getPercentageGrade() {
		return percentageGrade;
	}
	
	/**
	 * Sets the percentage grade.
	 *
	 * @param percentageGrade the new percentage grade
	 */
	public void setPercentageGrade(String percentageGrade) {
		this.percentageGrade = percentageGrade;
	}
	
	/**
	 * Gets the to value.
	 *
	 * @return the to value
	 */
	public String getToValue() {
		return toValue;
	}
	
	/**
	 * Sets the to value.
	 *
	 * @param toValue the new to value
	 */
	public void setToValue(String toValue) {
		this.toValue = toValue;
	}
	
	/**
	 * Gets the from value.
	 *
	 * @return the from value
	 */
	public String getFromValue() {
		return fromValue;
	}
	
	/**
	 * Sets the from value.
	 *
	 * @param fromValue the new from value
	 */
	public void setFromValue(String fromValue) {
		this.fromValue = fromValue;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Percentage [id=" + id + ", percentageGrade=" + percentageGrade
				+ ", toValue=" + toValue + ", fromValue=" + fromValue + "]";
	}
	
	
	
	

}
