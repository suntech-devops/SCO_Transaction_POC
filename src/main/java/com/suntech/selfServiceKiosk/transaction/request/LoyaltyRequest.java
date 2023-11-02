package com.suntech.selfServiceKiosk.transaction.request;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class LoyaltyRequest {
	
	private String mobileNumber;
	private String messageId;
	private String cardNumber;
	
	

}
