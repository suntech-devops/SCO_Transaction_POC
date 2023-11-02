package com.suntech.selfServiceKiosk.transaction.karnival;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suntech.selfServiceKiosk.transaction.utils.TransactionReturnParameters;

@Service
public class BillPostingServiceImpl {

	Logger log = LoggerFactory.getLogger("logger");

	@Value("${API.URL:mockurl}")
	private String url;

	@Value("${API.AUTHTOKEN:mocktoken}")
	private String authToken;

	public KarnivalResponse saveDetails(TransactionReturnParameters transactionReturnParameters) {
		log.info("saveDetails method started--");
		RestTemplate restTemplate = new RestTemplate();
		KarnivalResponse karnivalResponse = new KarnivalResponse();
		ObjectMapper objectMapper = new ObjectMapper();

		KarnivalRequest karnivalRequest = new KarnivalRequest();

		karnivalRequest.setTotalQuantity(transactionReturnParameters.getReturnLineItems().size());

		Store store = new Store();
		store.setStoreID(transactionReturnParameters.getStoreId());

		karnivalRequest.setStore(store);

		karnivalRequest.setTotalSaving(0);

		CustomerInfo customerInfo = new CustomerInfo();
		customerInfo.setCustomerNumber(transactionReturnParameters.getCustomer().getAdditionalInstructions());

		karnivalRequest.setCustomerInfo(customerInfo);

		Discount Discount = new Discount();

		karnivalRequest.setBillDiscount(Discount);

		PosInfo posInfo = new PosInfo();
		posInfo.setPosNumber(transactionReturnParameters.getStoreId());
		posInfo.setUserID(transactionReturnParameters.getRegisterId());

		karnivalRequest.setPosInfo(posInfo);

		PaymentInfo paymentInfo = new PaymentInfo();

		paymentInfo.setTotalTender((long) transactionReturnParameters.getTransactionTenderTotal());
		// paymentInfo.setChangeDue(Long.valueOf(transactionReturnParameters.getChangeDuetenderName()));
		paymentInfo.setRoundoff(0);
		// paymentInfo.setRoundedOffAmount(transactionReturnParameters.getRoundedOffTotalAmount());
		// paymentInfo.setPaidAmount(Long.valueOf(transactionReturnParameters.getAmountPaidInCash()));

		// Payment Mode

		List<PaymentMode> paymentModeList = new ArrayList<>();
		for (com.suntech.selfServiceKiosk.transaction.utils.TenderLineItemReturnParameters tender : transactionReturnParameters
				.getTenderLineItems()) {
			PaymentMode paymentMode = new PaymentMode();
			paymentMode.setAmount(tender.getAmount());

			Details details = new Details();
			details.setPaymentDateTime(transactionReturnParameters.getTransEnd());
			paymentMode.setDetails(details);
			paymentMode.setMode("CARD");
			paymentModeList.add(paymentMode);
			paymentInfo.setPaymentMode(paymentModeList);

		}
		karnivalRequest.setPaymentInfo(paymentInfo);

		// Item

		List<Item> itemList = new ArrayList<Item>();

		for (com.suntech.selfServiceKiosk.transaction.utils.SelfServiceKioskSaleReturnLineItem itemLine : transactionReturnParameters
				.getReturnLineItems()) {
			Item item = new Item();
			item.setAmount((itemLine.getMoMrpLnItmRtn()));
			item.setSellingPrice((itemLine.getMoMrpLnItmRtn()));
			item.setDescription(itemLine.getDeItmShrtRcpt());
			item.setMrp((itemLine.getMoMrpLnItmRtn()));
			item.setQuantity((long) itemLine.getQuantity());
			item.setHsnCode(itemLine.getItemCollectionId());
			item.setCode(itemLine.getPosItemId());

			List<ItemTax> itemTaxList = new ArrayList<ItemTax>();

			for (com.suntech.selfServiceKiosk.transaction.utils.SelfServiceKioskTaxLineItmBrkpRequest tax : itemLine
					.getTaxLineItmBrkp()) {

				ItemTax itemTax = new ItemTax();

				itemTax.setAmount(tax.getTX_BRKUP_TX_AMT());
				itemTax.setTaxableAmount(tax.getTX_BRKUP_TXBL_AMT());
				itemTax.setPercentage(String.valueOf(tax.getTX_BRKUP_TX_RT()));
				itemTax.setDescription(tax.getTX_BRKUP_TX_CD_DSCR());
				itemTax.setCode("A");
				itemTaxList.add(itemTax);
				item.setTaxes(itemTaxList);
			}

//			Discount discount = new Discount();
//			for (DiscountInformation discountInfo : transactionReturnParameters.getDiscounts()) {
//				// discount.setAmount(discountInfo.getAmount().longValue());
//				discount.setDescription(discountInfo.getName());
//				// discount.setDiscountableAmount(discountInfo.getAmount().longValue());
//				item.setDiscount(discount);
//			}

			itemList.add(item);

		}

		karnivalRequest.setItems(itemList);

		karnivalRequest.setSubTotal(0);

		karnivalRequest.setOtherDiscounts(null);

		BillInfo billInfo = new BillInfo();
		billInfo.setBillNumber(Integer.toString(transactionReturnParameters.getTransNo()));
		billInfo.setBillStatus(0);
		billInfo.setBillType(null);
		billInfo.setPurchaseDate(transactionReturnParameters.getBizDate());
		billInfo.setPurchaseTime(transactionReturnParameters.getTransStart());

		karnivalRequest.setBillInfo(billInfo);

		Attributes attributes = new Attributes();
		attributes.setBillMode("E-RECEIPT");

		karnivalRequest.setAttributes(attributes);

		List<Tax> taxList = new ArrayList<Tax>();

		Tax taxes = new Tax();
		taxes.setAmount(transactionReturnParameters.getTaxLineItem().getTaxAmt());
		taxes.setCode(Integer.toString(transactionReturnParameters.getTaxLineItem().getTaxTypeCode()));
		taxes.setDescription(null);
		taxes.setPercentage(transactionReturnParameters.getTaxLineItem().getTaxPercent());
		taxes.setTaxableAmount(transactionReturnParameters.getTaxLineItem().getInclTaxAmount());

		taxList.add(taxes);

		karnivalRequest.setTaxes(taxList);

		try {
			String req = objectMapper.writeValueAsString(karnivalRequest);
			System.out.println(req);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		try {
			try {
				String request = objectMapper.writeValueAsString(karnivalRequest);
				log.info("Print Request {}", request);
				System.out.println(request);
			}

			catch (JsonProcessingException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}

			HttpHeaders headers = new HttpHeaders();
			log.info("HttpHeaders Object Created");

			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", authToken);
			log.info("Headers Set {}", headers);
			HttpEntity<KarnivalRequest> entity = new HttpEntity<KarnivalRequest>(karnivalRequest, headers);
			System.out.println("url" + url);
			karnivalResponse = restTemplate.exchange(url, HttpMethod.POST, entity, KarnivalResponse.class).getBody();
		} catch (Exception ex) {
			karnivalResponse.setMessage(ex.getMessage());
			karnivalResponse.setStatus("failed");
			log.error("Exception Occurred In", ex);
		}
		try {
			String response = objectMapper.writeValueAsString(karnivalResponse);
			log.info("Karnival Response " + response);
		} catch (JsonProcessingException e) {
			e.printStackTrace();

		}

		log.info("saveDetails method ended--");
		return karnivalResponse;
	}
}
