package com.suntech.selfServiceKiosk.transaction.karnival;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class PosInfo {

	private String posNumber;
	private String userID;
	private String userName;

	@JsonProperty("posNumber")
	public String getPosNumber() {
		return posNumber;
	}

	@JsonProperty("posNumber")
	public void setPosNumber(String value) {
		this.posNumber = value;
	}

	@JsonProperty("userId")
	public String getUserID() {
		return userID;
	}

	@JsonProperty("userId")
	public void setUserID(String value) {
		this.userID = value;
	}

	@JsonProperty("userName")
	public String getUserName() {
		return userName;
	}

	@JsonProperty("userName")
	public void setUserName(String value) {
		this.userName = value;
	}
}
