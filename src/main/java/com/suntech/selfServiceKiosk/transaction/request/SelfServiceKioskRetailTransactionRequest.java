package com.suntech.selfServiceKiosk.transaction.request;

import lombok.Data;

@Data
public class SelfServiceKioskRetailTransactionRequest 
{

	private String suspendedTransactionReasonCode;
	
	private int transactionSalesTotal; 
	
	private float transactionDiscountTotal;
	
	private float transactionTaxTotal;
	
	private float transactionInclusiveTaxTotal;
	
	private float transactionNetTotal;
	
	private float transactionTenderTotal;
	

	private String personalMaskId;
	private String personalIdReqType;
	private String personalIdState;
	private String personalIdCountry;
	private int sendPackageCount;
	private int CD_CO_ISO;
	private int MO_OFF_TOT;
	private char sendCustomerType;
	private char flagSendCustomerPhysicallyPresent;
	private String employeeId;
	private String giftRegistryId;
	
	private SelfServiceKioskRetailTransactionLineItem lineItem;

	
}
