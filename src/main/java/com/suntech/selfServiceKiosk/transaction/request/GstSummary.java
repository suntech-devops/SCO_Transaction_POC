package com.suntech.selfServiceKiosk.transaction.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class GstSummary {
	
	private String gst;
	private String taxable;
	private String cgst;
	private String sgst;
	private String igst;
	private String total;
	

}
