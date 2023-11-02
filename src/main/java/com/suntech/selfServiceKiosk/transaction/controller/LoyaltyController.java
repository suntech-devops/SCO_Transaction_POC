package com.suntech.selfServiceKiosk.transaction.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suntech.selfServiceKiosk.transaction.Response.LoyaltyResponse;
import com.suntech.selfServiceKiosk.transaction.request.LoyaltyRequest;
import com.suntech.selfServiceKiosk.transaction.service.LoyaltyServiceImpl;


@RestController
@RequestMapping(value= "/karnival")
public class LoyaltyController {
	
	private static final Logger log = LoggerFactory.getLogger(LoyaltyController.class);
	
	@Autowired
	private LoyaltyServiceImpl loyaltyServiceImpl;

	
	@PostMapping("/searchCustomer")
	public LoyaltyResponse customerInfo(@RequestBody LoyaltyRequest loyaltyRequest) {
		log.info("Saving The Details Of The Loyalty Customer");
		return loyaltyServiceImpl.customerInfo(loyaltyRequest);
	}


}
