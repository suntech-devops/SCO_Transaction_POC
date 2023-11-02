package com.suntech.selfServiceKiosk.transaction.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@SuppressWarnings("serial")
@Data
@XmlRootElement
public class TransactionReturnParameters implements Serializable {

	// Parent Transaction
	private String storeId;

	private String registerId;

	private String tillOpenTimeStamp;

	private String invoiceNumber;

	private int transNo;

	private String bizDate;

	private String operatorId;

	private String transStart;

	private String transEnd;

	private String transactionTypeCode;

	private char trainingFlag;

	private String employeeId;

	private int transStatus;

	private String tenderRepositoryId;

	private short transactionPostProcessingStatusCode;

	private Timestamp recordLastModifiedTimestamp;

	private Timestamp recordCreatedTimestamp;

	private char transactionReentryFlag;

	private char salesAssociateModifiedFlag;

	private String subInvFl;

	// Retail Transaction
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

	@JsonProperty("cd_CO_ISO")
	private int CD_CO_ISO;
	private BigDecimal roundedOffTotalAmount;
	private char sendCustomerType;
	private char flagSendCustomerPhysicallyPresent;
	private String giftRegistryId;

	////////////////////////////////////

//	private int transactionStatus

	private int transactionType;

	private BigDecimal quantity;

	private BigDecimal subtotal;

	private BigDecimal discountTotal;

//	private BigDecimal taxTotal
//
//	private BigDecimal grandTotal

	private BigDecimal amountDue;

	private CustomerParameters customer;

	private ArrayList<String> discountCards;

	private String balanceGCAmount;

	private String itemDiscOverrideFlag;

	private String maxUINnum;

	private String panDetailsCaptured;

	ArrayList<String> mCouponList;

	private boolean isMcouponEreciptDialog;

	private String easyExchangeMessage;

	private String onlineCreditCardNum;

	private String amountPaidInCash;

	private boolean isPaytmTender;

	private String latitute;

	private String longitude;

	private String changeDuetenderName;

	private String status;

	private String otpValidation;

	private int tenderDeleteIndex = -1;

	private String discountType;

	private String reasonCode;

	private BigDecimal discCriteria;

	private boolean referenceNoRequired;

	private boolean gstEnabled;

	private boolean IgstEnabled;

	private String homeState;

	private String toState;

	private boolean showManagerOverride = false;

	private String managerOverrideStatus = "";

	private int accessFunctionId;

	private boolean mcouponIssued;

	private boolean displayMobileNumberPrompt;

	private Object returnTrxs;

	private Object captureCustomer;

	private Object refundDetails;

	private List<String> nonReturnableItems;

	private String suspendRetrieveMobileNumebr;

	private int numberOfSuspendDone;

	private String eReceiptConf;

	private String eReceiptTrantype;

	private boolean isGSTINValidated;

	private boolean GSTINFlagEnable;

	private String gstinCustomerLegalName;

	private String invoiceDiscountMessage;

	private ArrayList<SelfServiceKioskSaleReturnLineItem> returnLineItems;

	private ArrayList<TenderLineItemReturnParameters> tenderLineItems;

	private ArrayList<DiscountInformation> discounts;

	private SelfServiceKioskTaxLineItemRequest taxLineItem;

}