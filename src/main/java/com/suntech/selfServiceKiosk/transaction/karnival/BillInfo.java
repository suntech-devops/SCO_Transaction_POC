package com.suntech.selfServiceKiosk.transaction.karnival;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class BillInfo {

	private String billType;
	private String billNumber;
	private String purchaseDate;
	private long billStatus;
	private String purchaseTime;

	@JsonProperty("billType")
	public String getBillType() {
		return billType;
	}

	@JsonProperty("billType")
	public void setBillType(String value) {
		this.billType = value;
	}

	@JsonProperty("billNumber")
	public String getBillNumber() {
		return billNumber;
	}

	@JsonProperty("billNumber")
	public void setBillNumber(String value) {
		this.billNumber = value;
	}

	@JsonProperty("purchaseDate")
	public String getPurchaseDate() {
		return purchaseDate;
	}

	@JsonProperty("purchaseDate")
	public void setPurchaseDate(String value) {
		this.purchaseDate = value;
	}

	@JsonProperty("billStatus")
	public long getBillStatus() {
		return billStatus;
	}

	@JsonProperty("billStatus")
	public void setBillStatus(long value) {
		this.billStatus = value;
	}

	@JsonProperty("purchaseTime")
	public String getPurchaseTime() {
		return purchaseTime;
	}

	@JsonProperty("purchaseTime")
	public void setPurchaseTime(String value) {
		this.purchaseTime = value;
	}
}
