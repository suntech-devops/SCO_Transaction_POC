package com.suntech.selfServiceKiosk.transaction.request;

import lombok.Data;

@Data
public class SelfServiceKioskRegisterRequest 
{
	private int transNo;
	
	private String storeId;
	
	private String registerId;
}
