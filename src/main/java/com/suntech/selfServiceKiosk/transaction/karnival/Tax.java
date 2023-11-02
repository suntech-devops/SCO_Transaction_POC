package com.suntech.selfServiceKiosk.transaction.karnival;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class Tax {
	
	private double amount;
    private double taxableAmount;
    private double percentage;
    private String description;
    private String code;

    @JsonProperty("amount")
    public double getAmount() { return amount; }
    @JsonProperty("amount")
    public void setAmount(double value) { this.amount = value; }

    @JsonProperty("taxableAmount")
    public double getTaxableAmount() { return taxableAmount; }
    @JsonProperty("taxableAmount")
    public void setTaxableAmount(double value) { this.taxableAmount = value; }

    @JsonProperty("percentage")
    public double getPercentage() { return percentage; }
    @JsonProperty("percentage")
    public void setPercentage(double value) { this.percentage = value; }

    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    @JsonProperty("code")
    public String getCode() { return code; }
    @JsonProperty("code")
    public void setCode(String value) { this.code = value; }
}