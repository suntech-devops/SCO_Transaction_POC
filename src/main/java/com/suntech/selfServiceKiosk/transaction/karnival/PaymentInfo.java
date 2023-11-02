package com.suntech.selfServiceKiosk.transaction.karnival;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class PaymentInfo {

	private long totalTender;
	private List<PaymentMode> paymentMode;
	private long changeDue;
	private long roundoff;
	private long roundedOffAmount;
	private long paidAmount;
	private String paymentStatus;

	@JsonProperty("totalTender")
	public long getTotalTender() {
		return totalTender;
	}

	@JsonProperty("totalTender")
	public void setTotalTender(long value) {
		this.totalTender = value;
	}

	@JsonProperty("paymentMode")
	public List<PaymentMode> getPaymentMode() {
		return paymentMode;
	}

	@JsonProperty("paymentMode")
	public void setPaymentMode(List<PaymentMode> value) {
		this.paymentMode = value;
	}

	@JsonProperty("changeDue")
	public long getChangeDue() {
		return changeDue;
	}

	@JsonProperty("changeDue")
	public void setChangeDue(long value) {
		this.changeDue = value;
	}

	@JsonProperty("roundoff")
	public long getRoundoff() {
		return roundoff;
	}

	@JsonProperty("roundoff")
	public void setRoundoff(long value) {
		this.roundoff = value;
	}

	@JsonProperty("roundedOffAmount")
	public long getRoundedOffAmount() {
		return roundedOffAmount;
	}

	@JsonProperty("roundedOffAmount")
	public void setRoundedOffAmount(long value) {
		this.roundedOffAmount = value;
	}

	@JsonProperty("paidAmount")
	public long getPaidAmount() {
		return paidAmount;
	}

	@JsonProperty("paidAmount")
	public void setPaidAmount(long value) {
		this.paidAmount = value;
	}

	@JsonProperty("paymentStatus")
	public String getPaymentStatus() {
		return paymentStatus;
	}

	@JsonProperty("paymentStatus")
	public void setPaymentStatus(String value) {
		this.paymentStatus = value;
	}
}
