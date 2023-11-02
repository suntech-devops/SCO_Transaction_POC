package com.suntech.selfServiceKiosk.transaction.karnival;

import lombok.Data;

@Data
public class SelfServiceKioskSaleReturnTaxLineItem {

	private int taxAuthorityId;
	private String taxType;
	private char taxHolidayFlag;
	private float taxableAmount;
	private float taxAmountPerAuthority;
	private float taxAmountTotal;
	private float shippingRecordsTaxAmountTotal;
	private String taxAuthorityName;
	private String taxRuleName;
	private float taxPercent;
	private int taxMode;
	private float inclusiveTaxAmountTotal;
	private char inclusiveTaxFlag;
	
	

}
