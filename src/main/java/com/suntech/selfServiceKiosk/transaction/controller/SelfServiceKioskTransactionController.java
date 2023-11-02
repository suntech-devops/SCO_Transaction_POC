package com.suntech.selfServiceKiosk.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suntech.selfServiceKiosk.transaction.Response.SelfServiceKioskTransactionResponse;
import com.suntech.selfServiceKiosk.transaction.service.SelfServiceKioskTransactionService;
import com.suntech.selfServiceKiosk.transaction.utils.TransactionReturnParameters;

@RestController
@RequestMapping(value="/transaction")
public class SelfServiceKioskTransactionController {

	@Autowired
	private SelfServiceKioskTransactionService selfServiceKioskTransactionService;

	@PostMapping("/sale")
	public SelfServiceKioskTransactionResponse saveTrTrn(
			@RequestBody TransactionReturnParameters transactionReturnParameters)
			 {

		return selfServiceKioskTransactionService.getTransactionStatus(transactionReturnParameters);
	}
	
	
	@PostMapping("/sale/xml")
	public SelfServiceKioskTransactionResponse saveTrTrnByXml(@RequestHeader("invoiceNo") String invoiceNo )
			 {

		return selfServiceKioskTransactionService.saveTransactionByXml(invoiceNo);
	}
}
