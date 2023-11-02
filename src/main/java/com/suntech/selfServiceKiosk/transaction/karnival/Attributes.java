package com.suntech.selfServiceKiosk.transaction.karnival;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class Attributes {
	
	 private String billMode;
	    private long easyBuyTotal;

	    @JsonProperty("Bill-Mode")
	    public String getBillMode() { return billMode; }
	    @JsonProperty("Bill-Mode")
	    public void setBillMode(String value) { this.billMode = value; }

	    @JsonProperty("easyBuyTotal")
	    public long getEasyBuyTotal() { return easyBuyTotal; }
	    @JsonProperty("easyBuyTotal")
	    public void setEasyBuyTotal(long value) { this.easyBuyTotal = value; }
	}

