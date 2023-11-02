package com.suntech.selfServiceKiosk.transaction.karnival;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Data;


@Data
public class CustomerParameters  {

	private String salutation;
	private String customerFName;
	private String customerLName;
	private String customerEmail;
	private String mobileNumber;
	private String delieveryAddress;
	private String buildingEntryTiming;
	private String serviceMode;
	private String dimensionsOfLift;
	private String delieveryDate;
	private String billingAddress;
	private String additionalInstructions;
	private String vipCustomer;
	
	/* Nadia Arora : Added fields starts */
	private String transId;
	private String customerID;
	private HashMap<String, String> feedbackShared;
	private HashMap<String, String> answersToQuestionarrie;
	private String partyId;
	private String company;
	private String gender;
	private String longitude;
	private String latitute;
	
	CustomerParameters ticcustomer;
	boolean capillaryCustomerSuccessResponse;
	boolean ticCustomerVisibleFlag;
	ArrayList<?> setcustoffers;

	//puja changes--to send LMR customer info to client
	private String balancePoint;
	private String customerTier;
	private String balancePointLastUpdationDate;
	private String pointsExpiringNextMonth;
	//puja changes--to send LMR customer info to client
	
	

}
