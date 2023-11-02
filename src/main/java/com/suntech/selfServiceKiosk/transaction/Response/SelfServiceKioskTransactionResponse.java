package com.suntech.selfServiceKiosk.transaction.Response;

import lombok.Data;

@Data
public class SelfServiceKioskTransactionResponse {
	private String message;

	private String errorCode;

	private String status;

}
