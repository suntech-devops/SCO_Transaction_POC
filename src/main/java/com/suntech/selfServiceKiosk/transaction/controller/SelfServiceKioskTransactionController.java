package com.suntech.selfServiceKiosk.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suntech.selfServiceKiosk.transaction.Response.SelfServiceKioskTransactionResponse;
import com.suntech.selfServiceKiosk.transaction.request.LoyaltyPoints;
import com.suntech.selfServiceKiosk.transaction.request.OtpRequest;
import com.suntech.selfServiceKiosk.transaction.service.ReceiptApiClient;
import com.suntech.selfServiceKiosk.transaction.service.SelfServiceKioskTransactionService;
import com.suntech.selfServiceKiosk.transaction.utils.TransactionReturnParameters;

@RestController
@RequestMapping(value="/transaction")
public class SelfServiceKioskTransactionController {

	@Autowired
	private SelfServiceKioskTransactionService selfServiceKioskTransactionService;
	
	@Autowired
	private ReceiptApiClient apiClient;
	
	@PostMapping("/sale")
	public SelfServiceKioskTransactionResponse saveTrTrn(
	@RequestBody TransactionReturnParameters transactionReturnParameters){
		return selfServiceKioskTransactionService.getTransactionStatus(transactionReturnParameters);
	}
	
	@PostMapping("/sale/xml")
	public SelfServiceKioskTransactionResponse saveTrTrnByXml(@RequestHeader("invoiceNo") String invoiceNo ){
		return selfServiceKioskTransactionService.saveTransactionByXml(invoiceNo);
	}
	
	@PostMapping("send/invoice/bill/free")
	public String sendBillfreeInvoice(@RequestBody TransactionReturnParameters transactionReturnParameters ) {
		return apiClient.createReceipt(transactionReturnParameters);
	}
	
	@PostMapping("/get/balance/loyalty/points")
	public String getLoyaltyBalance(@RequestBody LoyaltyPoints loyaltyPoints)
	{
		return apiClient.getLoyaltyPoints(loyaltyPoints);			
	}
	
	@PostMapping("/get/points/discount")
	public String getPointsDiscount(@RequestBody LoyaltyPoints loyaltyPoints) {
		return apiClient.getPointsDiscount(loyaltyPoints);
	}
	
	@PostMapping("/create/otp")
	public String createOTP(@RequestBody OtpRequest otpRequest)
	{
		return apiClient.createOTP(otpRequest);
	}
	
	@PostMapping("/verify/otp")
	public String verifyOTP(@RequestBody OtpRequest otpRequest)
	{
		return apiClient.verifyOTP(otpRequest);
	}
}
