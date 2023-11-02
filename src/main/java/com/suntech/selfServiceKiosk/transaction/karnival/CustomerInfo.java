package com.suntech.selfServiceKiosk.transaction.karnival;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class CustomerInfo {

	private String customerName;
	private String loyaltyDescription;
	private String customerID;
	private String loyaltyNumber;
	private String customerNumber;
	private CustomerAddress customerAddress;
	private String customerEmail;

	@JsonProperty("customerName")
	public String getCustomerName() {
		return customerName;
	}

	@JsonProperty("customerName")
	public void setCustomerName(String value) {
		this.customerName = value;
	}

	@JsonProperty("loyaltyDescription")
	public String getLoyaltyDescription() {
		return loyaltyDescription;
	}

	@JsonProperty("loyaltyDescription")
	public void setLoyaltyDescription(String value) {
		this.loyaltyDescription = value;
	}

	@JsonProperty("customerId")
	public String getCustomerID() {
		return customerID;
	}

	@JsonProperty("customerId")
	public void setCustomerID(String value) {
		this.customerID = value;
	}

	@JsonProperty("loyaltyNumber")
	public String getLoyaltyNumber() {
		return loyaltyNumber;
	}

	@JsonProperty("loyaltyNumber")
	public void setLoyaltyNumber(String value) {
		this.loyaltyNumber = value;
	}

	@JsonProperty("customerNumber")
	public String getCustomerNumber() {
		return customerNumber;
	}

	@JsonProperty("customerNumber")
	public void setCustomerNumber(String value) {
		this.customerNumber = value;
	}

	@JsonProperty("customerAddress")
	public CustomerAddress getCustomerAddress() {
		return customerAddress;
	}

	@JsonProperty("customerAddress")
	public void setCustomerAddress(CustomerAddress value) {
		this.customerAddress = value;
	}

	@JsonProperty("customerEmail")
	public String getCustomerEmail() {
		return customerEmail;
	}

	@JsonProperty("customerEmail")
	public void setCustomerEmail(String value) {
		this.customerEmail = value;
	}
}
