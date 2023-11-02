package com.suntech.selfServiceKiosk.transaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suntech.selfServiceKiosk.transaction.Response.LoyaltyResponse;
import com.suntech.selfServiceKiosk.transaction.request.LoyaltyRequest;

@Service
public class LoyaltyServiceImpl implements LoyaltyService {
	
	Logger log = LoggerFactory.getLogger("logger");
	
	@Value("${API.LOYALTYURL:mockurl}")
	private String loyaltyUrl;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public LoyaltyResponse customerInfo(LoyaltyRequest loyaltyRequest) {
				
		log.info("saveDetails method started--");


			LoyaltyResponse loyaltyResponse=new LoyaltyResponse();
			
			ObjectMapper objectMapper=new ObjectMapper();
			
			try {
				try {
					String request = objectMapper.writeValueAsString(loyaltyRequest);
					log.info("Print Request {}", request);
					System.out.println(request);
				}

				catch (JsonProcessingException e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}

				HttpHeaders headers = new HttpHeaders();
				log.info("HttpHeaders Object Created");

				headers.setContentType(MediaType.APPLICATION_JSON);
				log.info("Headers Set {}", headers);
				HttpEntity<LoyaltyRequest> entity = new HttpEntity<LoyaltyRequest>(loyaltyRequest, headers);

				loyaltyResponse = restTemplate.exchange(loyaltyUrl, HttpMethod.POST, entity, LoyaltyResponse.class).getBody();
			} 
			catch (Exception ex) {
				loyaltyResponse.setMessage(ex.getMessage());
				log.error("Exception Occurred In", ex);
			}
			try {
				String response = objectMapper.writeValueAsString(loyaltyResponse);
				log.info("Loyalty Response " + response);
			} catch (JsonProcessingException e) {
				e.printStackTrace();


		}

		log.info("customerInfo method ended--");
		return loyaltyResponse;

	}

		
	}
	
