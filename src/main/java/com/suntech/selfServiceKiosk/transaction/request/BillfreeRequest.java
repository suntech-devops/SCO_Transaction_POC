package com.suntech.selfServiceKiosk.transaction.request;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BillfreeRequest {
	
	private String auth_token;
	private String inv_no;
	private String user_phone;
	private String cust_name;
	private String cust_bday;
	private String cust_anniv;
	private String bill_date;
	private String bill_time;
	private String store_identifier;
	private String is_printed;
	private int pts_redeemed;
	private String coupon_redeemed;
	private String bill_amount;
	private String discount_amount;
	private String referrer_phone;
	private String purpose;
	
	
	
	
	
	private ArrayList<Particulars> particulars;
	private ArrayList<AdditionalInfo> additional_info;
	private ArrayList<GstSummary> gst_summary;
	private ArrayList<PaymentInfoBillFree> payment_info;
	
	
}
