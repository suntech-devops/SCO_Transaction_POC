package com.suntech.selfServiceKiosk.transaction.karnival;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class Item {

	private String amount;
	private String sellingPrice;
	private String description;
	private String mrp;
	private long quantity;
	private String hsnCode;
	private String code;
	private List<ItemTax> taxes;
	private Discount discount;

	@JsonProperty("amount")
	public String getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	public void setAmount(String string) {
		this.amount = string;
	}

	@JsonProperty("sellingPrice")
	public String getSellingPrice() {
		return sellingPrice;
	}

	@JsonProperty("sellingPrice")
	public void setSellingPrice(String string) {
		this.sellingPrice = string;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String value) {
		this.description = value;
	}

	@JsonProperty("mrp")
	public String getMrp() {
		return mrp;
	}

	@JsonProperty("mrp")
	public void setMrp(String string) {
		this.mrp = string;
	}

	@JsonProperty("quantity")
	public long getQuantity() {
		return quantity;
	}

	@JsonProperty("quantity")
	public void setQuantity(long value) {
		this.quantity = value;
	}

	@JsonProperty("hsnCode")
	public String getHsnCode() {
		return hsnCode;
	}

	@JsonProperty("hsnCode")
	public void setHsnCode(String value) {
		this.hsnCode = value;
	}

	@JsonProperty("code")
	public String getCode() {
		return code;
	}

	@JsonProperty("code")
	public void setCode(String value) {
		this.code = value;
	}

	@JsonProperty("taxes")
	public List<ItemTax> getTaxes() {
		return taxes;
	}

	@JsonProperty("taxes")
	public void setTaxes(List<ItemTax> itemTaxList) {
		this.taxes = itemTaxList;
	}

	@JsonProperty("discount")
	public Discount getDiscount() {
		return discount;
	}

	@JsonProperty("discount")
	public void setDiscount(Discount value) {
		this.discount = value;
	}

	public void setTaxes(boolean add) {
		// TODO Auto-generated method stub
		
	}
}
