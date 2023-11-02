package com.suntech.selfServiceKiosk.transaction.karnival;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class Store {

	private String storeID;

	@JsonProperty("storeId")
	public String getStoreID() {
		return storeID;
	}

	@JsonProperty("storeId")
	public void setStoreID(String value) {
		this.storeID = value;
	}
}
