package com.suntech.selfServiceKiosk.transaction.karnival;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class PaymentMode {

	private BigDecimal amount;
	private Details details;
	private String mode;

	@JsonProperty("amount")
	public BigDecimal getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	public void setAmount(BigDecimal value) {
		this.amount = value;
	}

	@JsonProperty("details")
	public Details getDetails() {
		return details;
	}

	@JsonProperty("details")
	public void setDetails(Details value) {
		this.details = value;
	}

	@JsonProperty("mode")
	public String getMode() {
		return mode;
	}

	@JsonProperty("mode")
	public void setMode(String value) {
		this.mode = value;
	}
}
