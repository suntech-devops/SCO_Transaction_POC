package com.suntech.selfServiceKiosk.transaction.utils;

import lombok.Data;

@Data
public class SelfServiceKioskTaxLineItemRequest {

	private double taxAmt;
	private double inclTaxAmount;
	private int taxTypeCode;
	private double taxPercent;
	private double taxOverridePercent;
	private double TaxOverrideAmount;
	private String taxReasonCode;

}
