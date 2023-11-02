package com.suntech.selfServiceKiosk.transaction.service;

import com.suntech.selfServiceKiosk.transaction.Response.LoyaltyResponse;
import com.suntech.selfServiceKiosk.transaction.request.LoyaltyRequest;

public interface LoyaltyService {
	
	public LoyaltyResponse customerInfo(LoyaltyRequest loyaltyRequest);


}
