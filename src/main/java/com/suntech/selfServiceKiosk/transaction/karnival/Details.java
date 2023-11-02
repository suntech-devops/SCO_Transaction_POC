package com.suntech.selfServiceKiosk.transaction.karnival;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class Details {

	private String paymentDateTime;

	@JsonProperty("paymentDateTime")
	public String getPaymentDateTime() {
		return paymentDateTime;
	}

	@JsonProperty("paymentDateTime")
	public void setPaymentDateTime(String value) {
		this.paymentDateTime = value;
	}
}
