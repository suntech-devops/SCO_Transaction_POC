package com.suntech.selfServiceKiosk.transaction.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpRequest {
	private String auth_token;
	private String user_phone;
	private TokenModel[] data;
	private String purpose;
	private String token;
	private String otp;

}
