package com.suntech.selfServiceKiosk.transaction.karnival;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.ToString;

@Component
@ToString
public class KarnivalResponse {

	private String status;
	private String message;
	private Params params;

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String value) {
		this.status = value;
	}

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String value) {
		this.message = value;
	}

	@JsonProperty("params")
	public Params getParams() {
		return params;
	}

	@JsonProperty("params")
	public void setParams(Params value) {
		this.params = value;
	}
}
