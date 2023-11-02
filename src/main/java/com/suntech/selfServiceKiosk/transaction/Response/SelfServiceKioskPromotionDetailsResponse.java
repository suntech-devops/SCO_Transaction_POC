package com.suntech.selfServiceKiosk.transaction.Response;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SelfServiceKioskPromotionDetailsResponse {
	private Object promoId;

	private Object storeId;

	private Object promoType;

	private Object promoName;

	private Object promoStatus;

	private Object effectiveDate;

	private Object endDate;

	private Date modfRcrd;

	private Date crtRcrd;

	private Object methodCode;

	private Object scopeCode;

	private Object applicationLimit;

	private Object sourceThresholdAmount;

	private Object sourceLimitAmount;

	private Object targetThresholdAmount;

	private Object targetLimitAmount;

	private Object allowRepeatingSourcesFlag;

	private Object sourceAnyQuantity;

	private Object targetAnyQuantity;

//	private List<PromoApplication> promoApplications

	// @Transient
	private String srcItemId;

	// @Transient
	private double discountedAmount;

}
