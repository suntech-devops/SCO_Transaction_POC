package com.suntech.selfServiceKiosk.transaction.request;

import lombok.Data;

@Data
public class SelfServiceKioskRetailTransactionLineItem {
	private short retailTransactionLineItemSequence;

	private String typeCode;
}
