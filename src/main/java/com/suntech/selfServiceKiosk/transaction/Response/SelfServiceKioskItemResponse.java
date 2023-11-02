package com.suntech.selfServiceKiosk.transaction.Response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelfServiceKioskItemResponse {
	private Object id;

	private Object barcode;

	private Object description;

	private String size;

	private String color;

	private boolean active;

	private Date crtRcrd;

	private Date modfRcrd;

	private Object taxable;

	// private String taxStructure

	private Object priceList;

	private Object unit;

	private SelfServiceKioskPromotionDetailsResponse selfServiceKioskPromotionDetailsResponse;

	private String message;

}
