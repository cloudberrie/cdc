package com.ui.model;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Model class of a customer
 * 
 * @author marcelbirkner
 *
 */
public class Customer {

	private Integer customerId;
	private String firstName;
	private String lastName;
	
	public Customer(Integer id, String firstName, String lastName) {
		this.customerId = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
