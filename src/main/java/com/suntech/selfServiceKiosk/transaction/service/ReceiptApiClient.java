package com.suntech.selfServiceKiosk.transaction.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.suntech.selfServiceKiosk.transaction.request.AdditionalInfo;
import com.suntech.selfServiceKiosk.transaction.request.BillfreeRequest;
import com.suntech.selfServiceKiosk.transaction.request.GstSummary;
import com.suntech.selfServiceKiosk.transaction.request.LoyaltyPoints;
import com.suntech.selfServiceKiosk.transaction.request.OtpRequest;
import com.suntech.selfServiceKiosk.transaction.request.Particulars;
import com.suntech.selfServiceKiosk.transaction.request.PaymentInfoBillFree;
import com.suntech.selfServiceKiosk.transaction.request.TokenModel;
import com.suntech.selfServiceKiosk.transaction.utils.SelfServiceKioskSaleReturnLineItem;
import com.suntech.selfServiceKiosk.transaction.utils.SelfServiceKioskTaxLineItmBrkpRequest;
import com.suntech.selfServiceKiosk.transaction.utils.TransactionReturnParameters;


@Service
public class ReceiptApiClient {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${billFree.Auth.Token}") 
	private String apiToken;


	@Value("${digital.bill}") 
	private String digitalBillUrl;

	@Value("${get.loyalty.points}") 
	private String loyaltyPointsUrl;

	@Value("${get.loyalty.points.discount}") 
	private String loyaltyPointsDiscountUrl;

	@Value("${create.otp}") 
	private String createOtpUrl;

	@Value("${verify.otp}") 
	private String verifyOtpUrl;


	public String createReceipt(TransactionReturnParameters trnxParameters)  {
		final String API_URL = digitalBillUrl;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		BillfreeRequest billfreeRequest= new BillfreeRequest();
		billfreeRequest.setAuth_token(apiToken);
		billfreeRequest.setInv_no(String.valueOf(trnxParameters.getInvoiceNumber()));
		billfreeRequest.setUser_phone(trnxParameters.getCustomer().getMobileNumber());
		billfreeRequest.setCust_name(trnxParameters.getCustomer().getCustomerFName()+ " "+trnxParameters.getCustomer().getCustomerLName());
		billfreeRequest.setCust_bday(" ");
		billfreeRequest.setCust_anniv(" ");
		billfreeRequest.setBill_date(trnxParameters.getBizDate());
		billfreeRequest.setBill_time(trnxParameters.getTransStart());
		billfreeRequest.setStore_identifier(trnxParameters.getStoreId());
		billfreeRequest.setIs_printed("NO");//has to be change if bill is printed on paper
		billfreeRequest.setPts_redeemed(trnxParameters.getPts_redeemed());//has to be changed if loyalty points are redeemed
		billfreeRequest.setCoupon_redeemed("");// has to be changed if coupon in redeemed with coupon number.
		billfreeRequest.setBill_amount(String.valueOf(trnxParameters.getTransactionNetTotal()));
		billfreeRequest.setDiscount_amount(String.valueOf(trnxParameters.getTransactionDiscountTotal()));
		billfreeRequest.setReferrer_phone("");

		ArrayList<Particulars> particularsList = new ArrayList<>();
		ArrayList<GstSummary>gstSummaries= new ArrayList<>();
		for(SelfServiceKioskSaleReturnLineItem particularData : trnxParameters.getReturnLineItems())
		{
			SelfServiceKioskTaxLineItmBrkpRequest taxLineItmBrkpRequest = new SelfServiceKioskTaxLineItmBrkpRequest();
			SelfServiceKioskTaxLineItmBrkpRequest brkpRequest= new SelfServiceKioskTaxLineItmBrkpRequest();

			Double gst=	taxLineItmBrkpRequest.getTX_BRKUP_TX_RT() + brkpRequest.getTX_BRKUP_TX_AMT();

			Particulars particulars= new Particulars();
			particulars.setSku_id(particularData.getItemId());
			particulars.setDescription(particularData.getDeItmShrtRcpt());
			particulars.setHsn(""); // has to be filled if found
			particulars.setGst(String.valueOf(gst));
			particulars.setQty(String.valueOf(particularData.getQuantity()));
			particulars.setRate(String.valueOf(particularData.getMoMrpLnItmRtn()));
			particulars.setAmount(String.valueOf(particularData.getMoMrpLnItmRtn()));
			particularsList.add(particulars);	

			GstSummary gstSummary= new GstSummary();
			gstSummary.setCgst(String.valueOf(taxLineItmBrkpRequest.getTX_BRKUP_TX_RT()));//TX_BRKUP_TX_AMT
			gstSummary.setSgst(String.valueOf(taxLineItmBrkpRequest.getTX_BRKUP_TX_RT()));
			gstSummary.setGst(String.valueOf(gst)); //needs to be done
			gstSummary.setIgst("");
			gstSummary.setTotal(String.valueOf(gst));
			gstSummaries.add(gstSummary);

		}
		billfreeRequest.setParticulars(particularsList);
		billfreeRequest.setGst_summary(gstSummaries);        

		ArrayList<AdditionalInfo>additionalInfos= new ArrayList<>();
		AdditionalInfo subtotal=new AdditionalInfo();

		subtotal.setText("SUBTOTAL");
		subtotal.setValue(String.valueOf(trnxParameters.getTransactionNetTotal()));

		additionalInfos.add(subtotal);

		AdditionalInfo loyality=new AdditionalInfo();
		loyality.setText("LOYALTY DISCOUNT");
		loyality.setValue(String.valueOf(trnxParameters.getPts_redeemed()));

		additionalInfos.add(loyality);

		float totalAmount= trnxParameters.getTransactionNetTotal() - trnxParameters.getPts_redeemed();

		AdditionalInfo total=new AdditionalInfo();
		total.setText("TOTAL");
		total.setValue(String.valueOf(totalAmount));

		additionalInfos.add(total);

		billfreeRequest.setAdditional_info(additionalInfos);

		ArrayList<PaymentInfoBillFree> infoBillFree= new ArrayList<>();
		PaymentInfoBillFree billFree= new PaymentInfoBillFree();
		billFree.setText("Payment Mode");
		billFree.setValue("Upi");

		infoBillFree.add(billFree);

		PaymentInfoBillFree billFree2 = new PaymentInfoBillFree();
		billFree2.setText("Voucher");
		billFree2.setValue("0.00");
		infoBillFree.add(billFree2);

		PaymentInfoBillFree billFree3 = new PaymentInfoBillFree();
		billFree3.setText("Amount");
		billFree3.setValue(String.valueOf(totalAmount));
		infoBillFree.add(billFree3);

		billfreeRequest.setPayment_info(infoBillFree);

		HttpEntity<BillfreeRequest> requestEntity = new HttpEntity<>(billfreeRequest, headers);

		/* Make the request */
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(API_URL, requestEntity, String.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) 
		{
			String responseBody = responseEntity.getBody();
			return responseBody;
		} 
		else
		{
			return "Error: " + responseEntity.getStatusCode();
		}
	}
	public String getLoyaltyPoints(LoyaltyPoints loyaltyPoints)  {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		final String API_URL = loyaltyPointsUrl;

		loyaltyPoints.setAuth_token(apiToken);
		loyaltyPoints.setUser_phone(loyaltyPoints.getUser_phone());//trnxParameters.getCustomer().getMobileNumber());

		HttpEntity<LoyaltyPoints> requestEntity = new HttpEntity<>(loyaltyPoints, headers); 
		/* Make the request */
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(API_URL, requestEntity, String.class);
		if (responseEntity.getStatusCode().is2xxSuccessful()) 
		{
			String responseBody = responseEntity.getBody();
			return responseBody;
		} 
		else
		{
			return "Error: " + responseEntity.getStatusCode();
		}
	}

	public String getPointsDiscount(LoyaltyPoints loyaltyPoints) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		final String API_URL = loyaltyPointsDiscountUrl;
		loyaltyPoints.setAuth_token(apiToken);
		loyaltyPoints.setUser_phone(loyaltyPoints.getUser_phone());
		loyaltyPoints.setInv_no(loyaltyPoints.getInv_no());
		loyaltyPoints.setBill_date(loyaltyPoints.getBill_date());
		loyaltyPoints.setBill_amount(loyaltyPoints.getBill_amount());        


		HttpEntity<LoyaltyPoints> requestEntity = new HttpEntity<>(loyaltyPoints, headers);

		/* Make the request */
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(API_URL, requestEntity, String.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) 
		{
			String responseBody = responseEntity.getBody();
			return responseBody;
		} 
		else
		{
			return "Error: " + responseEntity.getStatusCode();
		}	
	}

	public String createOTP(OtpRequest otpRequest) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		final String API_URL = "https://billfree.in/m-api/create-otp";



		// OtpRequest otpRequest= new OtpRequest();
		// BillfreeRequest billfreeRequest= new BillfreeRequest();
		otpRequest.setAuth_token(apiToken);
		otpRequest.setUser_phone(otpRequest.getUser_phone());
		TokenModel[] data = otpRequest.getData();
		if (data != null && data.length > 0) {
			data[0].setAuth_token("18ae617086081359b47ed7f7c08c877b");
		}

		otpRequest.setPurpose("custVerify");

		HttpEntity<OtpRequest> requestEntity = new HttpEntity<>(otpRequest, headers);

		/* Make the request */
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(API_URL, requestEntity, String.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) 
		{
			String responseBody = responseEntity.getBody();
			return responseBody;
		} 
		else
		{
			return "Error: " + responseEntity.getStatusCode();
		}   
	}
	public String verifyOTP( OtpRequest otpRequest) {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		final String API_URL = verifyOtpUrl;

		otpRequest.setAuth_token(apiToken);
		otpRequest.setUser_phone(otpRequest.getUser_phone());
		otpRequest.setToken(otpRequest.getToken());
		otpRequest.setOtp(otpRequest.getOtp());

		HttpEntity<OtpRequest> requestEntity = new HttpEntity<>(otpRequest, headers);

		/* Make the request */
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(API_URL, requestEntity, String.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) 
		{
			String responseBody = responseEntity.getBody();
			return responseBody;
		} 
		else
		{
			return "Error: " + responseEntity.getStatusCode();
		}
	}
}
