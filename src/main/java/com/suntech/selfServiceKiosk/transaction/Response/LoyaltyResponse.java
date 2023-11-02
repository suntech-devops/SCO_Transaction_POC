package com.suntech.selfServiceKiosk.transaction.Response;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class LoyaltyResponse {
	
	private String messageId;
	private String response;
	private String message;
	private String cardNumber;
	private String mobileNumber;
	private String custName;
	private String custTier;
	private String email;
	private String pointBal;
	private String pincode;
	private String sbiPointBal;
	private String missingProfile;
	
}
