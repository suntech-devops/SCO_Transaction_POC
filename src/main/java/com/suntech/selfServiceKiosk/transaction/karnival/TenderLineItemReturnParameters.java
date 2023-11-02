package com.suntech.selfServiceKiosk.transaction.karnival;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TenderLineItemReturnParameters {

	private int tenderLineNumber;
	private String description;
	private BigDecimal amount;
	private String cardNumber;
	private String authCode;
	private String expDate;
	private String bankId;

	private String invoiceNum;
	private String dateTime;
	private String creditCardMode;

	private String tenderTypeCode;

	private int currencyId;

	private String maskedAccountId;

	private String cardType;

	private String tenderMediaIssuerId;

	private String bankAccount;
	
	private String authorizationMethodCode;
	
	private  String cardNumberSwipedOrKeyedCode;
	
	private String adjudicationCode;

}
