package com.suntech.selfServiceKiosk.transaction.karnival;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class Params {

	private String entityID;
	private String checkoutOtp;

	@JsonProperty("entity.id")
	public String getEntityID() {
		return entityID;
	}

	@JsonProperty("entity.id")
	public void setEntityID(String value) {
		this.entityID = value;
	}

	@JsonProperty("checkout.otp")
	public String getCheckoutOtp() {
		return checkoutOtp;
	}

	@JsonProperty("checkout.otp")
	public void setCheckoutOtp(String value) {
		this.checkoutOtp = value;
	}
}
