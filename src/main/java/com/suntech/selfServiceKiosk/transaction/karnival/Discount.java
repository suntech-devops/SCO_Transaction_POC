package com.suntech.selfServiceKiosk.transaction.karnival;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class Discount {
	
	private long amount;
    private String description;
    private long discountableAmount;

    @JsonProperty("amount")
    public long getAmount() { return amount; }
    @JsonProperty("amount")
    public void setAmount(long value) { this.amount = value; }

    @JsonProperty("description")
    public String getDescription() { return description; }
    @JsonProperty("description")
    public void setDescription(String value) { this.description = value; }

    @JsonProperty("discountableAmount")
    public long getDiscountableAmount() { return discountableAmount; }
    @JsonProperty("discountableAmount")
    public void setDiscountableAmount(long value) { this.discountableAmount = value; }
}


