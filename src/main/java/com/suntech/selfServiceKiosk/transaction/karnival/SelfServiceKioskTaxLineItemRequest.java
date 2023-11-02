package com.suntech.selfServiceKiosk.transaction.karnival;

import lombok.Data;

@Data
public class SelfServiceKioskTaxLineItemRequest {

	private double taxAmt;
	private double inclTaxAmount;
	private int taxTypeCode;
	private double taxPercent;
	private double taxOverridePercent;
	private double taxOverrideAmount;
	private String taxReasonCode;

}
