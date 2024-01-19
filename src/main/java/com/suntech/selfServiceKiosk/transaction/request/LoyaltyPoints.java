package com.suntech.selfServiceKiosk.transaction.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyPoints {
	
	private String auth_token;
	private String user_phone;
	private String inv_no;
	private String bill_date;
	private String bill_amount;
	
	

}
