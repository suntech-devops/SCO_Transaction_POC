package com.suntech.selfServiceKiosk.transaction.request;

import java.sql.Timestamp;

import com.suntech.selfServiceKiosk.transaction.utils.SelfServiceKioskTaxLineItmBrkpRequest;

import lombok.Data;

@Data
public class SelfServiceKioskItemRequest 
{
	private String itemId;
	
	private String storeId;
	
	private Timestamp toTimestamp;
	
	//For saveTaxesBreakup TaxableData
	private SelfServiceKioskTaxLineItmBrkpRequest lineItmBrkpRequest ;
	

}
