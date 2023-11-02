package com.suntech.selfServiceKiosk.transaction.utils;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DiscountInformation
{
	 public static final String DISCOUNT_METHOD_AMOUNT = "Amount";
	 public static final String DISCOUNT_METHOD_PERCENT = "Percent";
	 public static final String DISCOUNT_TYPE_MANUAL = "Manual";
	 public static final String DISCOUNT_TYPE_DAMAGE = "Damage";
	 public static final String DISCOUNT_TYPE_EMPLOYEE = "Employee";
	 private String name;
	 private String type;
	 private String method;
	 private BigDecimal amount;
	 private String percent;
	 private String reasonCode;	

    
}
