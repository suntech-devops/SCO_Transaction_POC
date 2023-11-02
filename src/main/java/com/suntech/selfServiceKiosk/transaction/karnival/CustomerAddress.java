package com.suntech.selfServiceKiosk.transaction.karnival;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class CustomerAddress {

	private String state;
	private String addressString;
	private String country;
	private String city;

	@JsonProperty("state")
	public String getState() {
		return state;
	}

	@JsonProperty("state")
	public void setState(String value) {
		this.state = value;
	}

	@JsonProperty("addressString")
	public String getAddressString() {
		return addressString;
	}

	@JsonProperty("addressString")
	public void setAddressString(String value) {
		this.addressString = value;
	}

	@JsonProperty("country")
	public String getCountry() {
		return country;
	}

	@JsonProperty("country")
	public void setCountry(String value) {
		this.country = value;
	}

	@JsonProperty("city")
	public String getCity() {
		return city;
	}

	@JsonProperty("city")
	public void setCity(String value) {
		this.city = value;
	}
}
