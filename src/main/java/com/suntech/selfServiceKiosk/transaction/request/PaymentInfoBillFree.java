package com.suntech.selfServiceKiosk.transaction.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class PaymentInfoBillFree {
	private String text;
	private String value;
}
