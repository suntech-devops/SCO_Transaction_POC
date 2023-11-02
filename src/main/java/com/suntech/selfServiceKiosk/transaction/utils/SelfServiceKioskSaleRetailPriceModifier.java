package com.suntech.selfServiceKiosk.transaction.utils;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SelfServiceKioskSaleRetailPriceModifier {

	private short retailPriceModifierSequenceNumber;
	private int pricederivationRuleID;

	private BigDecimal amount;
	private String reasonCode;
	private BigDecimal percent;
	private int methodCode;
	private int assignmentBasis;
	private char damageDiscountFlag;
	@JsonProperty("pCDAdvancedDealAppliedFlag")
	private char pCDAdvancedDealAppliedFlag;            
	private char priceDerivationRuleDiscountFlag;
	private String discountReferenceID;
	private String discountReferenceTypeCode;
	private int typeCode;
	private String accountingDispositionCode;
	private int promotionID;
	private int promotionComponentID;
	private int promotionComponentDetailID;
	
	@JsonProperty("RTLOG_AMT")
	private String RTLOG_AMT;
	
	@JsonProperty("VFP_AMT")
	private String VFP_AMT;
	
	@JsonProperty("VENDOR_ID")
	private String VENDOR_ID;

}
