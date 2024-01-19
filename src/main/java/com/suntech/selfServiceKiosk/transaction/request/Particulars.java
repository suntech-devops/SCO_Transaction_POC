package com.suntech.selfServiceKiosk.transaction.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Particulars {
	private String sku_id;
	private String description;
	private String hsn;
	private String gst;
	private String qty;
	private String rate;
	private String amount;

}
