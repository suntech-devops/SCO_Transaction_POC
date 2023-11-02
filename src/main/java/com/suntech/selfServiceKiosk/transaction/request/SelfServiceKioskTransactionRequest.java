package com.suntech.selfServiceKiosk.transaction.request;

import java.sql.Timestamp;

import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.suntech.selfServiceKiosk.transaction.utils.SelfServiceKioskTaxLineItmBrkpRequest;

import lombok.Data;

@Component
@Data
public class SelfServiceKioskTransactionRequest {

	private String storeId;

	private String registerId;

	private int transNo;
	
	@Transient
	private String invoiceNumber;

	private String bizDate;

	private String operatorId;

	private String transStart;

	private String transEnd; 

	private String transactionTypeCode;
	
	private char trainingFlag;
	
	private String employeeId;
	
	private String customerInfo;
	
	private short customerInfoType;
	
	private short transStatus;
	
	private String tenderRepositoryId;
	
	private short transactionPostProcessingStatusCode;
	
	private Timestamp recordLastModifiedTimestamp;
	
	private Timestamp recordCreatedTimestamp;
	
	private char transactionReentryFlag;
	
	private char salesAssociateModifiedFlag;
	
	//CAPILLARY_REQ_STATUS
	private String capillaryReqStatus; 
	
//	CAPILLARY_STATUS_MESSAGE
	private String capillaryStatusMessage; 
	
	@JsonProperty("subInvFl")
	private String subInvFl;

	private SelfServiceKioskRetailTransactionRequest retailTransaction;

	private SelfServiceKioskRetailTransactionLineItem retailTransactionLineItem;

	private SelfServiceKioskSaleReturnLineItem saleReturnLineItem;
	
	// object for SaleReturnTaxLineItem
	private SelfServiceKioskSaleReturnTaxLineItem saleReturnTaxLineItem;
	
	//Object Reference for SelfServiceKioskTaxLineItmBrkpRequest 
	private SelfServiceKioskTaxLineItmBrkpRequest taxLineItmBrkp;
	
	private SelfServiceKioskTaxLineItemRequest taxLineItem;
	
	

}
