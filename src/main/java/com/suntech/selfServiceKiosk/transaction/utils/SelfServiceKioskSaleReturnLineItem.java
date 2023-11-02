package com.suntech.selfServiceKiosk.transaction.utils;

import java.util.ArrayList;

import lombok.Data;

@Data
public class SelfServiceKioskSaleReturnLineItem {
	
	private int lineNumber;
	
	private String typeCode;

	private String registryId;

	private String itemId;

	private String posItemId;

	private String serialNo;

	private int taxGroupId;

	private float quantity;

	private int returnExtendedAmount;

	private float saleReturnLineItem;

	private float saleReturnLineItemVatAmount;

	private float saleReturnLineItemInclusiveTaxAmount;

	private short sendLabelCount;

	private char merchandiseReturnFlag;

	private String returnReasonCode;
	private String rcItmCndRtnMr;
	private String originalSequenceNo;
	private String originalBizDayDate;
	private short originalRetailTransactionLineItemSequenceNo;
	private String originalReatailStoreId;
	private String posDepartmentId;
	private char sendFlag;
	private String flShpChg;
	private char giftReceiptFlag;
	private String webOrderNo;
	private String aiOrdLnItm;
	private String itemIdEntryMethodCode;
	private String sizeCode;
	private char returnRelatedItemFlag;
	private short relateItemTransactionLineItemSeqNumb;
	private char removeRelatedItemFlag;
	private char salesAssociateModifiedFlag;
	private float permanentRetailPriceAtTimeOfSale;
	private String deItmShrtRcpt;
	private String deItmLcl;
	private char restockingFeeFlag;
	private String flVldSezItm;
	private String flVldSrzItmExt;
	private String flSrzCrtExt;
	private String merchandiseHierarchyLevelCode;
	private String flOumSls;

	private String itemSizeRequiredFlag;
	private String stockItemSaleUnitOfMeasureCode;
	private String prohibitReturnFlag;
	private String employeeDiscountAllowedFlag;
	private String flTx;

	private String discountFlag;
	private String damageDiscountFlag;
	private String merchandisehierarchyGroupId;
	private String manufacturerUPC;
	private String flClrc;
	private String idNonRtvdOrgRcpt;
	private String salesAgeRestrictionIdentifier;
	private String priceEnteryRequiredFlag;
	private String moLnDs;
	private String moMrpLnItmRtn;
	private String itemCollectionId;
	private String itemKitHeaderReferenceId;
	
//	private ArrayList<SelfServiceKioskSaleReturnTaxLineItem> returnTaxLineItem
	private SelfServiceKioskSaleReturnTaxLineItem returnTaxLineItem;
	
	
	//For RetailPriceModifier
    private SelfServiceKioskSaleRetailPriceModifier retailPriceModifier; 
	
	private ArrayList< SelfServiceKioskTaxLineItmBrkpRequest> taxLineItmBrkp;
	

}
