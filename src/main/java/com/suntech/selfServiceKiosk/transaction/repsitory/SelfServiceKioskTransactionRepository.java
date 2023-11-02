package com.suntech.selfServiceKiosk.transaction.repsitory;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.suntech.selfServiceKiosk.transaction.model.Transaction;

@Repository
public interface SelfServiceKioskTransactionRepository extends JpaRepository<Transaction, String> {


	static final String UPDATE_WORKSTATION = "UPDATE AS_WS SET AI_TRN = :transNo WHERE ID_STR_RT = :storeId AND ID_WS = :registerId";

	static final String SAVE_RETAIL_TRANSACTION = "INSERT INTO TR_RTL ( ID_STR_RT, ID_WS, AI_TRN, DC_DY_BSN, RC_RSN_SPN, MO_SLS_TOT, MO_DSC_TOT, MO_TAX_TOT, MO_TAX_INC_TOT, MO_NT_TOT, MO_TND_TOT, ID_MSK_PRSL, TY_ID_PRSL_RQ, ST_PRSL, CO_PRSL, TS_MDF_RCRD, TS_CRT_RCRD, CNT_SND_PKG, CD_CO_ISO, MO_OFF_TOT, TY_SND_CT, FL_SND_CT_PHY, TR_LVL_SND, ID_EM, ID_REGISTRY) VALUES (:storeId, :registerId, :transNo, :bizDate, :suspendedTransactionReasonCode, :transactionSalesTotal, :transactionDiscountTotal, :transactionTaxTotal, :transactionInclusiveTaxTotal, :transactionNetTotal, :transactionTenderTotal, null, :personalMaskId, :personalIdReqType, :personalIdState, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, :personalIdCountry, :sendPackageCount,:CD_CO_ISO, :roundedOffTotalAmount, :sendCustomerType, :employeeId, :giftRegistryId)";

	// List will start from here
	static final String SAVE_RETAIL_TRANSACTION_LINE_ITEM = "INSERT INTO TR_LTM_RTL_TRN ( ID_STR_RT, ID_WS, AI_TRN, DC_DY_BSN, AI_LN_ITM, TY_LN_ITM, TS_MDF_RCRD, TS_CRT_RCRD) VALUES (:storeId, :registerId, :transNo, :bizDate, :originalRetailTransactionLineItemSequenceNo, :typeCode, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

	static final String SAVE_SALE_RETURN_LINE_ITEM = "INSERT INTO TR_LTM_SLS_RTN ( ID_STR_RT, ID_WS, AI_TRN, DC_DY_BSN, AI_LN_ITM, ID_REGISTRY, ID_ITM, ID_ITM_POS, ID_NMB_SRZ, ID_GP_TX, QU_ITM_LM_RTN_SLS, MO_EXTN_LN_ITM_RTN, MO_EXTN_DSC_LN_ITM, MO_VAT_LN_ITM_RTN, MO_TAX_INC_LN_ITM_RTN, CNT_SND_LAB, FL_RTN_MR, RC_RTN_MR, RC_ITM_CND_RTN_MR, ID_TRN_ORG, DC_DY_BSN_ORG, AI_LN_ITM_ORG, ID_STR_RT_ORG, ID_DPT_POS, FL_SND, FL_SHP_CHG, TS_MDF_RCRD, TS_CRT_RCRD, FL_RCV_GF, ID_ORD, AI_ORD_LN_ITM, LU_MTH_ID_ENR, ED_SZ, FL_RLTD_ITM_RTN, AI_LN_ITM_RLTD, FL_RLTD_ITM_RM, FL_SLS_ASSC_MDF, MO_PRN_PRC, DE_ITM_SHRT_RCPT, DE_ITM_LCL, FL_FE_RSTK, FL_VLD_SRZ_ITM, FL_VLD_SRZ_ITM_EXT, FL_SRZ_CRT_EXT, LU_HRC_MR_LV, FL_ITM_SZ_REQ, LU_UOM_SLS, TY_ITM, FL_RTN_PRH, FL_DSC_EM_ALW, FL_TX, FL_ITM_DSC, FL_ITM_DSC_DMG, ID_MRHRC_GP, ID_ITM_MF_UPC, FL_CLRNC, ID_NON_RTVD_ORG_RCPT, IDN_SLS_AG_RST, FL_ENTR_PRC_RQ, MO_LN_DS, MO_MRP_LN_ITM_RTN, ID_CLN, LU_KT_HDR_RFN_ID) VALUES (:storeId, :registerId, :transNo, :bizDate,:retailTransactionLineItemSequence, :registryId, :itemId, :posItemId, :serialNo, :taxGroupId, :quantity, :returnExtendedAmount, :saleReturnLineItem, :saleReturnLineItemVatAmount, :saleReturnLineItemInclusiveTaxAmount, :sendLabelCount, :merchandiseReturnFlag, :returnReasonCode, :rcItmCndRtnMr, :originalSequenceNo, :originalBizDayDate, :originalRetailTransactionLineItemSequenceNo, :originalReatailStoreId, :posDepartmentId, :sendFlag, :flShpChg, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, :giftReceiptFlag, :webOrderNo,:aiOrdLnItm, :itemIdEntryMethodCode, :sizeCode, :returnRelatedItemFlag, :relateItemTransactionLineItemSeqNumb, :removeRelatedItemFlag, :salesAssociateModifiedFlag, :permanentRetailPriceAtTimeOfSale, :deItmShrtRcpt, :deItmLcl, :restockingFeeFlag,  :flVldSezItm, :flVldSrzItmExt, :flSrzCrtExt, :merchandiseHierarchyLevelCode, :itemSizeRequiredFlag, :stockItemSaleUnitOfMeasureCode,:transactionTypeCode, :prohibitReturnFlag, :employeeDiscountAllowedFlag, :flTx, :discountFlag, :damageDiscountFlag,  :merchandisehierarchyGroupId, :manufacturerUPC, :flClrc, :idNonRtvdOrgRcpt, :salesAgeRestrictionIdentifier, :priceEnteryRequiredFlag, :moLnDs, :moMrpLnItmRtn, :itemCollectionId, :itemKitHeaderReferenceId  )";
	static final String SAVE_SALE_RETURN_TAX_LINE_ITEM = "INSERT INTO TR_LTM_SLS_RTN_TX ( ID_STR_RT, ID_WS, AI_TRN, DC_DY_BSN, AI_LN_ITM, ID_ATHY_TX, ID_GP_TX, TY_TX, FLG_TX_HDY, MO_TXBL_RTN_SLS, MO_TX_RTN_SLS, MO_TX_RTN_SLS_TOT, MO_TX_INC_RTN_SLS_TOT, NM_ATHY_TX, NM_RU_TX, PE_TX, TX_MOD, FL_TX_INC, TS_MDF_RCRD, TS_CRT_RCRD) VALUES (:storeId, :registerId, :transNo, :bizDate,:retailTransactionLineItemSequence, :taxAuthorityId,:taxGroupId,:taxType, :taxHolidayFlag, :taxableAmount, :taxAmountPerAuthority, :taxAmountTotal, :shippingRecordsTaxAmountTotal, :taxAuthorityName, :taxRuleName, :taxPercent,:taxMode, :inclusiveTaxFlag, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

	static final String TENDER_LINE_ITEM = "INSERT INTO TR_LTM_TND ( ID_STR_RT, ID_WS, AI_TRN, AI_LN_ITM, DC_DY_BSN, TY_TND, MO_ITM_LN_TND, ID_CNY_ICD, TS_MDF_RCRD, TS_CRT_RCRD) VALUES (:storeId, :registerId, :transNo, :retailTransactionLineItemSequence, :bizDate, :tenderTypeCode, :amount, :currencyId, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

	
	
	static final String TRANSACTION = "INSERT INTO TR_TRN ( ID_STR_RT, ID_WS, AI_TRN, DC_DY_BSN, ID_OPR,TS_TM_SRT, TS_TRN_BGN, TS_TRN_END, "
			+ "TY_TRN, FL_TRG_TRN, ID_EM, INF_CT, TY_INF_CT, SC_TRN, ID_RPSTY_TND, SC_PST_PRCS, TS_MDF_RCRD, TS_CRT_RCRD,"
			+ " FL_TRE_TRN, FL_SLS_ASSC_MDF, CAPILLARY_REQ_STATUS, CAPILLARY_STATUS_MESSAGE, SUB_INV_FL) "
			+ "VALUES (:storeId, :registerId, :transNo, :bizDate, :operatorId,TO_TIMESTAMP(:startDateTimestamp, 'YYYY-MM-DD HH24:MI:SS.FF'), TO_TIMESTAMP(:transStart, 'YYYY-MM-DD HH24:MI:SS.FF')"
			+ ", TO_TIMESTAMP(:transEnd, 'YYYY-MM-DD HH24:MI:SS.FF'), "
			+ ":transactionTypeCode, :trainingFlag, :employeeId,:customerInfo, :customerInfoType,:transStatus, :tenderRepositoryId, :transactionPostProcessingStatusCode,"
			+ " CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, :transactionReentryFlag, :salesAssociateModifiedFlag, '0', '', :subInvFl)";
	
	@Modifying
	@Transactional
	@Query(value = TRANSACTION, nativeQuery = true)
	public void saveTransaction(@Param("storeId") String storeId, @Param("registerId") String registerId,
			@Param("transNo") int transNo, @Param("bizDate") String bizDate, @Param("operatorId") String operatorId,
			@Param("startDateTimestamp") String startDateTimestamp,
			@Param("transStart") String transStart, @Param("transEnd") String transEnd,
			@Param("transactionTypeCode") String transactionTypeCode, @Param("trainingFlag") char trainingFlag,
			@Param("employeeId") String employeeId, @Param("customerInfo") String customerInfo,
			@Param("customerInfoType") short customerInfoType, @Param("transStatus") int transStatus,
			@Param("tenderRepositoryId") String tenderRepositoryId,
			@Param("transactionPostProcessingStatusCode") short transactionPostProcessingStatusCode,
			@Param("transactionReentryFlag") char transactionReentryFlag,
			@Param("salesAssociateModifiedFlag") char salesAssociateModifiedFlag, @Param("subInvFl") String subInvFl);

	// this is done 2nd query @Modifying
	@Query(value = "UPDATE AS_WS SET AI_TRN =:transNo,TS_TM_SRT=TO_TIMESTAMP(:startDateTimestamp, 'YYYY-MM-DD HH24:MI:SS.FF'), TS_CRT_RCRD=CURRENT_TIMESTAMP, TS_MDF_RCRD=CURRENT_TIMESTAMP WHERE ID_STR_RT =:storeId AND ID_WS =:registerId", nativeQuery = true)
	@Modifying
	@Transactional
	void updateAsWs(@Param("transNo") int transNo, 
			@Param("startDateTimestamp")String startDateTimestamp,
			@Param("storeId") String storeId,
			@Param("registerId") String registerId)
	        ;
	
	//Save_Retail_Transaction
	@Modifying
	@Query(value = SAVE_RETAIL_TRANSACTION, nativeQuery = true)
	@Transactional
	void saveTrRtl(@Param("storeId") String storeId, @Param("registerId") String registerId,
			@Param("transNo") int transNo, @Param("bizDate") String bizDate,
			@Param("suspendedTransactionReasonCode") String suspendedTransactionReasonCode,
			@Param("transactionSalesTotal") int transactionSalesTotal,
			@Param("transactionDiscountTotal") float transactionDiscountTotal,
			@Param("transactionTaxTotal") float transactionTaxTotal,
			@Param("transactionInclusiveTaxTotal") float transactionInclusiveTaxTotal,
			@Param("transactionNetTotal") float transactionNetTotal,
			@Param("transactionTenderTotal") float transactionTenderTotal,
			@Param("personalMaskId") String personalMaskId, @Param("personalIdReqType") String personalIdReqType,
			@Param("personalIdState") String personalIdState, @Param("personalIdCountry") String personalIdCountry,
			@Param("sendPackageCount") int sendPackageCount, @Param("CD_CO_ISO") int CD_CO_ISO,
			@Param("roundedOffTotalAmount") BigDecimal roundedOffTotalAmount,
			@Param("sendCustomerType") char sendCustomerType, @Param("employeeId") String employeeId,
			@Param("giftRegistryId") String giftRegistryId);

	@Modifying
	@Query(value = SAVE_RETAIL_TRANSACTION_LINE_ITEM, nativeQuery = true)
	@Transactional
	void saveTR_LTM_RTL_TRN(@Param("storeId") String storeId, @Param("registerId") String registerId,
			@Param("transNo") int transNo, @Param("bizDate") String bizDate,
			@Param("originalRetailTransactionLineItemSequenceNo") short originalRetailTransactionLineItemSequenceNo,
			@Param("typeCode") String typeCode);

	@Modifying
	@Query(value = SAVE_SALE_RETURN_LINE_ITEM, nativeQuery = true)
	@Transactional
	void saveTR_LTM_SLS_RTN(@Param("storeId") String storeId, @Param("registerId") String registerId,
			@Param("transNo") int transNo, @Param("bizDate") String bizDate,
			@Param("retailTransactionLineItemSequence") short retailTransactionLineItemSequence,
			@Param("registryId") String registryId, @Param("itemId") String itemId,
			@Param("posItemId") String posItemId, @Param("serialNo") String serialNo,
			@Param("taxGroupId") int taxGroupId, @Param("quantity") float quantity,
			@Param("returnExtendedAmount") int returnExtendedAmount,
			@Param("saleReturnLineItem") float saleReturnLineItem,
			@Param("saleReturnLineItemVatAmount") float saleReturnLineItemVatAmount,
			@Param("saleReturnLineItemInclusiveTaxAmount") float saleReturnLineItemInclusiveTaxAmount,
			@Param("sendLabelCount") short sendLabelCount, @Param("merchandiseReturnFlag") char merchandiseReturnFlag,
			@Param("returnReasonCode") String returnReasonCode, @Param("rcItmCndRtnMr") String rcItmCndRtnMr,
			@Param("originalSequenceNo") String originalSequenceNo,
			@Param("originalBizDayDate") String originalBizDayDate,
			@Param("originalRetailTransactionLineItemSequenceNo") short originalRetailTransactionLineItemSequenceNo,
			@Param("originalReatailStoreId") String originalReatailStoreId,
			@Param("posDepartmentId") String posDepartmentId, @Param("sendFlag") char sendFlag,
			@Param("flShpChg") String flShpChg, @Param("giftReceiptFlag") char giftReceiptFlag,
			@Param("webOrderNo") String webOrderNo, @Param("aiOrdLnItm") String aiOrdLnItm,
			@Param("itemIdEntryMethodCode") String itemIdEntryMethodCode, @Param("sizeCode") String sizeCode,
			@Param("returnRelatedItemFlag") char returnRelatedItemFlag,
			@Param("relateItemTransactionLineItemSeqNumb") int relateItemTransactionLineItemSeqNumb,
			@Param("removeRelatedItemFlag") char removeRelatedItemFlag,
			@Param("salesAssociateModifiedFlag") char salesAssociateModifiedFlag,
			@Param("permanentRetailPriceAtTimeOfSale") float permanentRetailPriceAtTimeOfSale,
			@Param("deItmShrtRcpt") String deItmShrtRcpt, @Param("deItmLcl") String deItmLcl,
			@Param("restockingFeeFlag") char restockingFeeFlag, @Param("flVldSezItm") String flVldSezItm,
			@Param("flVldSrzItmExt") String flVldSrzItmExt, @Param("flSrzCrtExt") String flSrzCrtExt,
			@Param("merchandiseHierarchyLevelCode") String merchandiseHierarchyLevelCode,
			@Param("itemSizeRequiredFlag") String itemSizeRequiredFlag,
			@Param("stockItemSaleUnitOfMeasureCode") String stockItemSaleUnitOfMeasureCode,
			@Param("transactionTypeCode") String transactionTypeCode , @Param("prohibitReturnFlag") String prohibitReturnFlag,
			@Param("employeeDiscountAllowedFlag") String employeeDiscountAllowedFlag, @Param("flTx") String flTx,
			@Param("discountFlag") String discountFlag, @Param("damageDiscountFlag") String damageDiscountFlag,
			@Param("merchandisehierarchyGroupId") String merchandisehierarchyGroupId,
			@Param("manufacturerUPC") String manufacturerUPC, @Param("flClrc") String flClrc,
			@Param("idNonRtvdOrgRcpt") String idNonRtvdOrgRcpt,
			@Param("salesAgeRestrictionIdentifier") String salesAgeRestrictionIdentifier,
			@Param("priceEnteryRequiredFlag") String priceEnteryRequiredFlag, @Param("moLnDs") String moLnDs,
			@Param("moMrpLnItmRtn") String moMrpLnItmRtn, @Param("itemCollectionId") String itemCollectionId,
			@Param("itemKitHeaderReferenceId") String itemKitHeaderReferenceId);

	@Modifying
	@Query(value = SAVE_SALE_RETURN_TAX_LINE_ITEM, nativeQuery = true)
	@Transactional
	void saveTRLTMSLSRTNTX(@Param("storeId") String storeId, @Param("registerId") String registerId,
			@Param("transNo") int transNo, @Param("bizDate") String bizDate,
			@Param("retailTransactionLineItemSequence") short retailTransactionLineItemSequence,
			@Param("taxAuthorityId") int taxAuthorityId, @Param("taxGroupId") int taxGroupId,
			@Param("taxType") String taxType, @Param("taxHolidayFlag") char taxHolidayFlag,
			@Param("taxableAmount") float taxableAmount, @Param("taxAmountPerAuthority") float taxAmountPerAuthority,
			@Param("taxAmountTotal") float taxAmountTotal,
			@Param("shippingRecordsTaxAmountTotal") float shippingRecordsTaxAmountTotal,
			@Param("taxAuthorityName") String taxAuthorityName, @Param("taxRuleName") String taxRuleName,
			@Param("taxPercent") float taxPercent, @Param("taxMode") int taxMode,
			@Param("inclusiveTaxFlag") char inclusiveTaxFlag);

	@Transactional
	@Modifying
	@Query(value = TENDER_LINE_ITEM, nativeQuery = true)
	void saveTenderLineItem(@Param("storeId") String storeId, @Param("registerId") String registerId,
			@Param("transNo") int transNo,
			@Param("retailTransactionLineItemSequence") short retailTransactionLineItemSequence,
			@Param("bizDate") String bizDate, @Param("tenderTypeCode") String tenderTypeCode,
			@Param("amount") BigDecimal amount, @Param("currencyId") int currencyId);

	static final String CREDIT_DEBIT_CARD_TENDER_LINE_ITEM = "INSERT INTO TR_LTM_CRDB_CRD_TN ( ID_STR_RT, ID_WS, AI_TRN, AI_LN_ITM,"
			+ " DC_DY_BSN, TY_TND, ID_MSK_ACNT_CRD, LU_ACNT_CRD_TKN, LU_NMB_CRD_SWP_KY, LU_MTH_AZN, ID_ISSR_TND_MD, TY_CRD, TY_ID,"
			+ " CNTRY_ID, ST_ID, DC_EP_DB_ID, LU_AZN_STLM_DT, TS_AZN, ACQ_BK_COD, LU_AJD_CR_DB, AI_MSG_AZN, DC_AZN_CRD, TM_AZN_CRD,"
			+ " ID_RTRVD_REF, CD_RSPS, CD_AZN_DT_ACNT_SRC_ORGL, FL_AZN_PYM_SV_ORGL, ID_AZN_TRN_ORGL, CD_AZN_VLD_ORGL, CD_AZN_SRC_ORGL,"
			+ " ID_REF_TRN_HOST_ORGL, ID_TRC_TRN, MO_BLNC_PPD_RMN, ID_NMB_SRZ_KY, LV_ACC_AFT_PRM_APR, TY_ACC_AFT_PRM_APR,"
			+ " LV_DRNG_PRM_APR, TY_DRNG_PRM_APR, DE_PRM, DE_DRNG_PRM, LU_AZN_JL_KY, TRN_ACQ, RTRVL_REF_NO, BK_ID, CRD_HOLD_NAME)"
			+ " VALUES (:storeId, :registerId, :transNo, :retailTransactionLineItemSequence, :bizDate, :tenderTypeCode,"
			+ " :maskedAccountId, null,:cardNumberSwipedOrKeyedCode, :authorizationMethodCode, :tenderMediaIssuerId, :cardType,"
			+ " 0, '', '', null, null, null,:AdjudicationCode,:creditDebitCardAdjudicationCode, null, null, null, null, 'APPROVED', "
			+ "'', '', '', '', '', '','', null, '', '', '', '', '', '', '', null, :tenderMediaIssuerId, null, :bankId, null)";

	@Transactional
	@Modifying
	@Query(value = CREDIT_DEBIT_CARD_TENDER_LINE_ITEM, nativeQuery = true)
	void saveCreditDebitCardTender(
			@Param("storeId") String storeId,
			@Param("registerId") String registerId,
			@Param("transNo") int transNo,
			@Param("retailTransactionLineItemSequence") short retailTransactionLineItemSequence,
			@Param("bizDate") String bizDate, 
			@Param("tenderTypeCode") String tenderTypeCode,
			@Param("maskedAccountId") String maskedAccountId, 
			@Param("cardNumberSwipedOrKeyedCode") String cardNumberSwipedOrKeyedCode,
			@Param("authorizationMethodCode") String authorizationMethodCode,
			@Param("tenderMediaIssuerId") String tenderMediaIssuerId,
			@Param("cardType")String cardType, 
			@Param("bankId") String bankId,
			@Param("creditDebitCardAdjudicationCode")String creditDebitCardAdjudicationCode,
			@Param("AdjudicationCode") String AdjudicationCode

	);
	// For saveTaxes through Transaction Objects
	static final String SAVE_TAXES_BREAKUP = "INSERT INTO TR_LTM_TX_BRKUP ( ID_STR_RT, ID_WS, AI_TRN, DC_DY_BSN, AI_LN_ITM, ID_ITM, ID_ITM_POS, QU_ITM_LM_RTN_SLS, TX_BRKUP_TX_CD,"
			+ "TX_BRKUP_TX_CD_DSCR, TX_BRKUP_TX_RT, TX_BRKUP_TXBL_AMT, TX_BRKUP_TX_AMT, TX_FCT, TXBL_FCT, TS_MDF_RCRD, TS_CRT_RCRD) "
			+ "VALUES (:storeId,:registerId, :transNo,:bizDate,:retailTrnsLineItmSeqNo,:itemId,:posItemId,:saleRtrnLineItmQty,:TX_BRKUP_TX_CD,:TX_BRKUP_TX_CD_DSCR,:TX_BRKUP_TX_RT,:TX_BRKUP_TXBL_AMT,:TX_BRKUP_TX_AMT,:TX_FCT,:TXBL_FCT,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)\r\n";

	@Modifying
	@Transactional
	@Query(value = SAVE_TAXES_BREAKUP, nativeQuery = true)
	public void saveTaxLineItmBreakup(@Param("storeId") String storeId, @Param("registerId") String registerId,
			@Param("transNo") int transNo, @Param("bizDate") String bizDate,
			@Param("retailTrnsLineItmSeqNo") int retailTrnsLineItmSeqNo, @Param("itemId") String itemId,
			@Param("posItemId") String posItemId, @Param("saleRtrnLineItmQty") float saleRtrnLineItmQty,
			@Param("TX_BRKUP_TX_CD") String TX_BRKUP_TX_CD, @Param("TX_BRKUP_TX_CD_DSCR") String TX_BRKUP_TX_CD_DSCR,
			@Param("TX_BRKUP_TX_RT") double TX_BRKUP_TX_RT, @Param("TX_BRKUP_TXBL_AMT") double TX_BRKUP_TXBL_AMT,
			@Param("TX_BRKUP_TX_AMT") double TX_BRKUP_TX_AMT, @Param("TX_FCT") BigDecimal TX_FCT,
			@Param("TXBL_FCT") BigDecimal TXBL_FCT

	);

	static final String SAVE_SALES_TAX = "INSERT INTO TR_LTM_TX ( ID_STR_RT, ID_WS, AI_TRN, DC_DY_BSN, AI_LN_ITM, MO_TX, MO_TX_INC, TY_TX, PE_TX, PE_TX_OVRD, MO_TX_OVRD, RC_TX, TS_MDF_RCRD, TS_CRT_RCRD) VALUES (:storeId, :registerId,:transNo,:bizDate,:retailTransItemSeqNo,:taxAmt,:inclTaxAmount,:taxTypeCode,:taxPercent,:taxOverridePercent,:taxOverrideAmount,:taxReasonCode,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

	@Modifying
	@Query(value = SAVE_SALES_TAX, nativeQuery = true)
	@Transactional
	public void saveSalesTax(@Param("storeId") String storeId, @Param("registerId") String registerId,
			@Param("transNo") int transNo, @Param("bizDate") String bizDate,
			@Param("retailTransItemSeqNo") short retailTransItemSeqNo, @Param("taxAmt") double taxAmt,
			@Param("inclTaxAmount") double inclTaxAmount, @Param("taxTypeCode") int taxTypeCode,
			@Param("taxPercent") double taxPercent, @Param("taxOverridePercent") double taxOverridePercent,
			@Param("taxOverrideAmount") double TaxOverrideAmount, @Param("taxReasonCode") String taxReasonCode);

	static final String POS_DEPARTMENT_HISTORY = "UPDATE LE_HST_PS_DPT SET CP_RFD_TOT=CP_RFD_TOT+ 0.0,QU_RFD_TOT=QU_RFD_TOT+ 0,CP_SLS_NT_ITM_TOT=CP_SLS_NT_ITM_TOT+ :saleReturnLineItem,QU_ITM_LN_SLS_TOT=QU_ITM_LN_SLS_TOT+ 1.0,CP_ITM_LN_NO_TX_TOT=CP_ITM_LN_NO_TX_TOT+ 0.0,QU_ITM_LN_NO_TX_TOT=QU_ITM_LN_NO_TX_TOT+ 0.0,CP_TX_EXM_TOT=CP_TX_EXM_TOT+ 0.0,QU_TRN_TX_EXM=QU_TRN_TX_EXM+ 0.0,CP_RTN_TOT=CP_RTN_TOT+ 0.0,QU_RTN_TOT=QU_RTN_TOT+ 0.0,CP_RTN_NO_TX_TOT=CP_RTN_NO_TX_TOT+ 0.0,QU_RTN_NO_TX_TOT=QU_RTN_NO_TX_TOT+ 0.0,CP_RTN_TX_EXM_TOT=CP_RTN_TX_EXM_TOT+ 0.0,QU_RTN_TX_EXM_TOT=QU_RTN_TX_EXM_TOT+ 0.0,CP_SLS_GS_ITM_TX=CP_SLS_GS_ITM_TX+  :returnExtendedAmount ,QU_SLS_GS_ITM_TX=QU_SLS_GS_ITM_TX+ 1.0,CP_RTN_GS_ITM_TX=CP_RTN_GS_ITM_TX+ 0.0,QU_RTN_GS_ITM_TX=QU_RTN_GS_ITM_TX+ 0.0,CP_TX_TOT=CP_TX_TOT+ 0.0,CP_INC_TX_TOT=CP_INC_TX_TOT+ :saleReturnLineItemInclusiveTaxAmount ,CP_RTN_TX_TOT=CP_RTN_TX_TOT+ 0.0,CP_RTN_INC_TX_TOT=CP_RTN_INC_TX_TOT+ 0.0,CP_SLS_ITM_TX=CP_SLS_ITM_TX+ 0.0,CP_SLS_ITM_INC_TX=CP_SLS_ITM_INC_TX+ :saleReturnLineItemInclusiveTaxAmount,CP_DSC_MSC_TOT=CP_DSC_MSC_TOT+ 0.0,QU_DSC_MSC_TOT=QU_DSC_MSC_TOT+ 0,CP_MKD_TOT=CP_MKD_TOT+ 0.0,QU_MKD_TOT=QU_MKD_TOT+ 0,CP_VD_PST_TRN_TOT=CP_VD_PST_TRN_TOT+ 0.0,QU_VD_PST_TRN_TOT=QU_VD_PST_TRN_TOT+ 0,CP_VD_LN_ITM_TOT=CP_VD_LN_ITM_TOT+ 0.0,QU_VD_LN_ITM_TOT=QU_VD_LN_ITM_TOT+ 0.0,CP_VD_TRN_TOT=CP_VD_TRN_TOT+ 0.0,QU_VD_TRN_TOT=QU_VD_TRN_TOT+ 0,CP_FE_RSTK_TOT=CP_FE_RSTK_TOT+ 0.0,QU_FE_RSTK_TOT=QU_FE_RSTK_TOT+ 0.0 WHERE ID_DPT_POS = :posDepartmentId AND FY = :year AND TY_PR_PRD = 'DY' AND ID_PR_PRD = :reportingPeriodId";

	@Transactional
	@Modifying
	@Query(value = POS_DEPARTMENT_HISTORY, nativeQuery = true)
	void savePosDepartmetHistory(

			@Param("saleReturnLineItem") double saleReturnLineItem,
			@Param("returnExtendedAmount") double returnExtendedAmount,
			@Param("saleReturnLineItemInclusiveTaxAmount") double saleReturnLineItemInclusiveTaxAmount,
			@Param("posDepartmentId") String posDepartmentId, @Param("year") String year,
			@Param("reportingPeriodId") int reportingPeriodId);
	


	static final String WORKSTATION_TIME_ACTIVITY_HISTORY = "UPDATE LE_HST_WS_TMACV SET QU_RFD_TOT = QU_RFD_TOT+0, QU_RFD_NO_TX_TOT = QU_RFD_NO_TX_TOT+0, QU_TRN_TOT = QU_TRN_TOT+1, QU_SLS_TOT = QU_SLS_TOT+1, CP_SLS_NT = CP_SLS_NT+ :transactionNetTotal, QU_SLS_NO_TX_TOT = QU_SLS_NO_TX_TOT+0, QU_SLS_LN_ITM = QU_SLS_LN_ITM+:lineItemSequence, CP_ITM_LN_NO_TX_TOT = CP_ITM_LN_NO_TX_TOT+0.00, QU_ITM_LN_NO_TX_TOT = QU_ITM_LN_NO_TX_TOT+0, CP_RTN_TOT = CP_RTN_TOT+0.00, QU_RTN_TOT = QU_RTN_TOT+0, CP_RTN_NO_TX_TOT = CP_RTN_NO_TX_TOT+0.00, QU_RTN_NO_TX_TOT = QU_RTN_NO_TX_TOT+0, CP_HP_TOT = CP_HP_TOT+0.00, QU_HP_TOT = QU_HP_TOT+0, MO_FE_RSTK_TOT = MO_FE_RSTK_TOT+0.00, QU_FE_RSTK_TOT = QU_FE_RSTK_TOT+0, QU_RFD_TX_TOT = QU_RFD_TX_TOT+0, QU_SLS_TX_TOT = QU_SLS_TX_TOT+:lineItemSequence, CP_ITM_LN_TX_TOT = CP_ITM_LN_TX_TOT+:transactionTaxTotal, QU_ITM_LN_TX_TOT = QU_ITM_LN_TX_TOT+:lineItemSequence, CP_RTN_TX_TOT = CP_RTN_TX_TOT+0.00, CP_SLS_TX_TOT = CP_SLS_TX_TOT+:transactionTaxTotal, CP_SLS_NO_TX_TOT = CP_SLS_NO_TX_TOT+0.00, QU_RTN_TX_TOT = QU_RTN_TX_TOT+0, QU_PYM_LY_CLT_TOT = QU_PYM_LY_CLT_TOT+0, MO_PYM_LY_CLT_TOT = MO_PYM_LY_CLT_TOT+0.00, QU_DLT_LY_DSB_TOT = QU_DLT_LY_DSB_TOT+0, MO_DLT_LY_DSB_TOT = MO_DLT_LY_DSB_TOT+0.00, MO_LY_FE_CRT_TOT = MO_LY_FE_CRT_TOT+0.00, QU_LY_FE_CRT_TOT = QU_LY_FE_CRT_TOT+0, MO_LY_FE_DLT_TOT = MO_LY_FE_DLT_TOT+0.00, QU_LY_FE_DLT_TOT = QU_LY_FE_DLT_TOT+0, QU_PYM_OR_CLT_TOT = QU_PYM_OR_CLT_TOT + 0, MO_PYM_OR_CLT_TOT = MO_PYM_OR_CLT_TOT + 0.00, QU_CNC_OR_DSB_TOT = QU_CNC_OR_DSB_TOT + 0, MO_CNC_OR_DSB_TOT = MO_CNC_OR_DSB_TOT + 0.00, MO_SHP_CHR_TOT = MO_SHP_CHR_TOT + 0.00, QU_SHP_CHR_TOT = QU_SHP_CHR_TOT + 0, TS_MDF_RCRD = CURRENT_TIMESTAMP WHERE ID_STR_RT = :storeId AND DC_WS = :bizDate AND IN_PRD_TM_HR = :timePeriodIntervalPerHourCount AND QU_INTV_PR_MN_PRD = 0 AND QU_INTV_PR_HR_PRD = 1";

	@Transactional
	@Modifying
	@Query(value = WORKSTATION_TIME_ACTIVITY_HISTORY, nativeQuery = true)
	void updateWorkstationTimeActivityHistory(@Param("transactionNetTotal") double transactionNetTotal,
			@Param("lineItemSequence") int lineItemSequence,
			
			@Param("transactionTaxTotal") double transactionTaxTotal, @Param("storeId") String storeId,
			@Param("bizDate") String bizDate,
			@Param("timePeriodIntervalPerHourCount") String timePeriodIntervalPerHourCount);

	static final String WORKSTATION_TIME_ACTIVITY_HISTORY1 = "INSERT INTO LE_HST_WS_TMACV ( ID_STR_RT, ID_WS, DC_WS, IN_PRD_TM_HR, QU_INTV_PR_MN_PRD, QU_INTV_PR_HR_PRD, QU_RFD_TOT, QU_RFD_NO_TX_TOT, QU_TRN_TOT, QU_SLS_TOT, CP_SLS_NT, QU_SLS_NO_TX_TOT, QU_SLS_LN_ITM, CP_ITM_LN_NO_TX_TOT, QU_ITM_LN_NO_TX_TOT, CP_RTN_TOT, QU_RTN_TOT, CP_RTN_NO_TX_TOT, QU_RTN_NO_TX_TOT, CP_HP_TOT, QU_HP_TOT, QU_SLS_TX_TOT, QU_RFD_TX_TOT, CP_ITM_LN_TX_TOT, QU_ITM_LN_TX_TOT, CP_SLS_NO_TX_TOT, CP_SLS_TX_TOT, CP_RTN_TX_TOT, QU_RTN_TX_TOT, QU_PYM_LY_CLT_TOT, MO_PYM_LY_CLT_TOT, QU_DLT_LY_DSB_TOT, MO_DLT_LY_DSB_TOT, MO_LY_FE_CRT_TOT, QU_LY_FE_CRT_TOT, MO_LY_FE_DLT_TOT, QU_LY_FE_DLT_TOT, QU_PYM_OR_CLT_TOT, MO_PYM_OR_CLT_TOT, QU_CNC_OR_DSB_TOT, MO_CNC_OR_DSB_TOT, MO_SHP_CHR_TOT, QU_SHP_CHR_TOT, TS_CRT_RCRD, TS_MDF_RCRD) VALUES (:storeId, :registerId, :bizDate, :timePeriodIntervalPerHourCount, 0, 1, 0, 0, 1, 1, :transactionNetTotal, 0, :lineItemSequence, 0.00, 0, 0.00, 0, 0.00, 0, 0.00, 0, :lineItemSequence, 0, :transactionTaxTotal, :lineItemSequence, 0.00, :transactionTaxTotal, 0.00, 0, 0, 0.00, 0, 0.00, 0.00, 0, 0.00, 0, 0, 0.00, 0, null, 0.00, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

	@Transactional
	@Modifying
	@Query(value = WORKSTATION_TIME_ACTIVITY_HISTORY1, nativeQuery = true)
	void insertWorkstationTimeActivityHistory(@Param("storeId") String storeId, @Param("registerId") String registerId,
			@Param("bizDate") String bizDate, @Param("transactionNetTotal") double transactionNetTotal,
			
			@Param("lineItemSequence") int lineItemSequence,
			@Param("transactionTaxTotal") double transactionTaxTotal,
			@Param("timePeriodIntervalPerHourCount") String timePeriodIntervalPerHourCount

	);

	static final String SALES_ASSOCIATE_PRODUCTIVITY = "UPDATE SLS_ASOC_ACTV SET CP_SLS_NT = CP_SLS_NT + :transactionNetTotal, TS_MDF_RCRD = CURRENT_TIMESTAMP, TS_CRT_RCRD = CURRENT_TIMESTAMP WHERE ID_STR_RT = :storeId AND ID_WS = :registerId AND ID_EM = :employeeId AND DC_DY_BSN = :bizDate";

	@Transactional
	@Modifying
	@Query(value = SALES_ASSOCIATE_PRODUCTIVITY, nativeQuery = true)
	void updateSalesAssociateProductivity(@Param("transactionNetTotal") double transactionNetTotal,
			@Param("storeId") String storeId, @Param("registerId") String registerId,
			@Param("employeeId") String employeeId, @Param("bizDate") String bizDate
			);

	static final String GET_TILL_HISTORY="select * from LE_HST_TL WHERE ID_RPSTY_TND = :registerId AND ID_STR_RT = :storeId AND TS_TM_SRT = TO_TIMESTAMP(:tillOpenTimeStamp, 'YYYY-MM-DD HH24:MI:SS.FF')";
	@Transactional
	@Query(value = GET_TILL_HISTORY, nativeQuery = true)
	String getTillHistory(
			@Param("registerId") String registerId,
			@Param("storeId") String storeId,
			@Param("tillOpenTimeStamp") String tillOpenTimeStamp);  
 
	static final String INSERT_TILL_HISTORY="insert into LE_HST_TL( ID_RPSTY_TND,ID_STR_RT,TS_TM_SRT)values(:registerId,:storeId,TO_TIMESTAMP(:tillOpenTimeStamp, 'YYYY-MM-DD HH24:MI:SS.FF'))";
		
	@Modifying
	@Query(value = INSERT_TILL_HISTORY, nativeQuery = true)
	@Transactional
	void insertTillHistory(
			@Param("registerId") String registerId,
			@Param("storeId") String storeId,
			@Param("tillOpenTimeStamp") String tillOpenTimeStamp);
	
	
	static final String TILL_HISTORY = "UPDATE LE_HST_TL SET AI_TRN = :transNo, SC_HST_TL = 1, DC_DY_BSN = :bizDate, ID_WS = :registerId, ID_CNY_ICD = :currencyId, QU_TRN_TOT = QU_TRN_TOT + 1, MO_NO_TX_TOT = MO_NO_TX_TOT + 0.00, QU_TRN_NO_TX_TOT = QU_TRN_NO_TX_TOT + 0, QU_TRN_TX_TOT = QU_TRN_TX_TOT + :lineItemSequence, CP_TX_EXM_TOT = CP_TX_EXM_TOT + 0.00, QU_TRN_TX_EXM_TOT = QU_TRN_TX_EXM_TOT + 0, CP_RFD_TOT = CP_RFD_TOT + 0.00, TP_SLS_EX_TX_TOT = TP_SLS_EX_TX_TOT + :transactionNetTotal-:transactionTaxTotal, QU_RFD_TOT = QU_RFD_TOT + 0, MO_RFD_NO_TX_TOT = MO_RFD_NO_TX_TOT + 0.00, QU_RFD_NO_TX_TOT = QU_RFD_NO_TX_TOT + 0, MO_RFD_TX_EXM_TOT = MO_RFD_TX_EXM_TOT + 0.00, QU_RFD_TX_EXM_TOT = QU_RFD_TX_EXM_TOT + 0, CP_SLS_GS_TRN_TX = CP_SLS_GS_TRN_TX + :transactionNetTotal+:transactionDiscountTotal, QU_SLS_GS_TRN_TX = QU_SLS_GS_TRN_TX + :lineItemSequence, CP_SLS_GS_TRN_NO_TX = CP_SLS_GS_TRN_NO_TX + 0.00, QU_SLS_GS_TRN_NO_TX = QU_SLS_GS_TRN_NO_TX + 0, CP_SLS_GS_TRN_TX_EXM = CP_SLS_GS_TRN_TX_EXM + 0.00, QU_SLS_GS_TRN_TX_EXM = QU_SLS_GS_TRN_TX_EXM + 0, CP_RTN_GS_TRN_TX = CP_RTN_GS_TRN_TX + 0.00, QU_RTN_GS_TRN_TX = QU_RTN_GS_TRN_TX + 0, MO_ITM_LN_SLS_TOT = MO_ITM_LN_SLS_TOT + :transactionSalesTotal, QU_ITM_LN_SLS_TOT = QU_ITM_LN_SLS_TOT + :lineItemSequence, MO_ITM_LN_NO_TX_TOT = MO_ITM_LN_NO_TX_TOT + 0.00, QU_ITM_LN_NO_TX_TOT = QU_ITM_LN_NO_TX_TOT + 0, MO_ITM_LN_TX_EXM_TOT = MO_ITM_LN_TX_EXM_TOT + 0.00, QU_ITM_LN_TX_EXM_TOT = QU_ITM_LN_TX_EXM_TOT + 0, CP_RTN_TOT = CP_RTN_TOT + 0.00, QU_RTN_TOT = QU_RTN_TOT + 0, MO_RTN_NO_TX_TOT = MO_RTN_NO_TX_TOT + 0.00, QU_RTN_NO_TX_TOT = QU_RTN_NO_TX_TOT + 0, MO_RTN_TX_EXM_TOT = MO_RTN_TX_EXM_TOT + 0.00, QU_RTN_TX_EXM_TOT = QU_RTN_TX_EXM_TOT + 0, MO_TRN_NO_TX_NO_MRCH_TOT = MO_TRN_NO_TX_NO_MRCH_TOT + 0.00, QU_TRN_NO_TX_NO_MRCH_TOT = QU_TRN_NO_TX_NO_MRCH_TOT + 0, MO_RTN_NO_TX_NO_MRCH_TOT = MO_RTN_NO_TX_NO_MRCH_TOT + 0.00, QU_RTN_NO_TX_NO_MRCH_TOT = QU_RTN_NO_TX_NO_MRCH_TOT + 0, MO_TX_NO_MRCH_TOT = MO_TX_NO_MRCH_TOT + 0.00, QU_TRN_TX_NO_MRCH_TOT = QU_TRN_TX_NO_MRCH_TOT + 0, MO_RTN_TX_NO_MRCH_TOT = MO_RTN_TX_NO_MRCH_TOT + 0.00, QU_RTN_TX_NO_MRCH_TOT = QU_RTN_TX_NO_MRCH_TOT + 0, MO_GFT_CRD_TOT = MO_GFT_CRD_TOT + 0.00, QU_GFT_CRD_TOT = QU_GFT_CRD_TOT + 0, MO_RTN_GFT_CRD_TOT = MO_RTN_GFT_CRD_TOT + 0.00, QU_RTN_GFT_CRD_TOT = QU_RTN_GFT_CRD_TOT + 0, CP_SLS_GS_ITM_TX = CP_SLS_GS_ITM_TX + :transactionNetTotal- :transactionDiscountTotal, QU_SLS_GS_ITM_TX = QU_SLS_GS_ITM_TX + :lineItemSequence, CP_RTN_GS_ITM_TX = CP_RTN_GS_ITM_TX + 0.00, QU_RTN_GS_ITM_TX = QU_RTN_GS_ITM_TX + 0, CP_SLS_GS_NO_MRCH_TX = CP_SLS_GS_NO_MRCH_TX + 0.00, QU_SLS_GS_NO_MRCH_TX = QU_SLS_GS_NO_MRCH_TX + 0, CP_SLS_GS_NO_MRCH_NO_TX = CP_SLS_GS_NO_MRCH_NO_TX + 0.00, QU_SLS_GS_NO_MRCH_NO_TX = QU_SLS_GS_NO_MRCH_NO_TX + 0, CP_SLS_GS_ITM_GFT_CRD = CP_SLS_GS_ITM_GFT_CRD + 0.00, QU_SLS_GS_ITM_GFT_CRD = QU_SLS_GS_ITM_GFT_CRD + 0, CP_TX_TOT = CP_TX_TOT + :transactionTaxTotal, CP_INC_TX_TOT = CP_INC_TX_TOT + :inclusiveTaxAmountTotal, MO_RFD_TX_TOT = MO_RFD_TX_TOT + 0.00, MO_RFD_INC_TX_TOT = MO_RFD_INC_TX_TOT + 0.00, MO_RTN_TX_TOT = MO_RTN_TX_TOT + 0.00, MO_RTN_INC_TX_TOT = MO_RTN_INC_TX_TOT + 0.00, CP_SLS_ITM_TX = CP_SLS_ITM_TX + :transactionTaxTotal, CP_SLS_ITM_INC_TX = CP_SLS_ITM_INC_TX + :inclusiveTaxAmountTotal, CP_TRN_SLS_TX = CP_TRN_SLS_TX + :transactionTaxTotal, CP_TRN_SLS_INC_TX = CP_TRN_SLS_INC_TX + :inclusiveTaxAmountTotal, MO_DITM_STCPN_TOT = MO_DITM_STCPN_TOT + 0.00, QU_DITM_STCPN_TOT = QU_DITM_STCPN_TOT + 0, MO_DTRN_STCPN_TOT = MO_DTRN_STCPN_TOT + 0.00, QU_DTRN_STCPN_TOT = QU_DTRN_STCPN_TOT + 0, CP_DSC_MSC_TOT = CP_DSC_MSC_TOT + 0.00, QU_DSC_MSC_TOT = QU_DSC_MSC_TOT + 0, CP_DSC_TOT = CP_DSC_TOT + 0.00, CP_MKD_TOT = CP_MKD_TOT + 0.00, QU_DSC_TOT = QU_DSC_TOT + 0, QU_MKD_TOT = QU_MKD_TOT + 0, CP_VD_PS_TRN = CP_VD_PS_TRN + 0.00, QU_VD_PST_TRN_TOT = QU_VD_PST_TRN_TOT + 0, CP_VD_LN_ITM_TOT = CP_VD_LN_ITM_TOT + 0.00, QU_VD_LN_ITM_TOT = QU_VD_LN_ITM_TOT + 0, CP_VD_TRN_TOT = CP_VD_TRN_TOT + 0.00, QU_VD_TRN_TOT = QU_VD_TRN_TOT + 0, QU_TRN_NO_SLS_TOT = QU_TRN_NO_SLS_TOT + 0, QU_PKP_TND_TOT = QU_PKP_TND_TOT + 0, CP_PKP_TND_TOT = CP_PKP_TND_TOT + 0.00, QU_LON_TND_TOT = QU_LON_TND_TOT + 0, CP_LON_TND_TOT = CP_LON_TND_TOT + 0.00, MO_HP_TOT = MO_HP_TOT + 0.00, QU_HP_TOT = QU_HP_TOT + 0, MO_FE_RSTK_TOT = MO_FE_RSTK_TOT + 0.00, QU_FE_RSTK_TOT = QU_FE_RSTK_TOT + 0, MO_FE_RSTK_NO_TX_TOT = MO_FE_RSTK_NO_TX_TOT + 0.00, QU_FE_RSTK_NO_TX_TOT = QU_FE_RSTK_NO_TX_TOT + 0, MO_GFT_CRF_ISD = MO_GFT_CRF_ISD + 0.00, QU_GFT_CRF_ISD = QU_GFT_CRF_ISD + 0, MO_GFT_CRD_ISD = MO_GFT_CRD_ISD + 0.00, QU_GFT_CRD_ISD = QU_GFT_CRD_ISD + 0, MO_GFT_CRD_RLD = MO_GFT_CRD_RLD + 0.00, QU_GFT_CRD_RLD = QU_GFT_CRD_RLD + 0, MO_GFT_CRD_RDM = MO_GFT_CRD_RDM + 0.00, QU_GFT_CRD_RDM = QU_GFT_CRD_RDM + 0, MO_GFT_CRD_ISD_VD = MO_GFT_CRD_ISD_VD + 0.00, QU_GFT_CRD_ISD_VD = QU_GFT_CRD_ISD_VD + 0, MO_GFT_CRD_RLD_VD = MO_GFT_CRD_RLD_VD + 0.00, QU_GFT_CRD_RLD_VD = QU_GFT_CRD_RLD_VD + 0, MO_GFT_CRD_RDM_VD = MO_GFT_CRD_RDM_VD + 0.00, QU_GFT_CRD_RDM_VD = QU_GFT_CRD_RDM_VD + 0, QU_HA_APP = QU_HA_APP + 0, QU_HA_DCL = QU_HA_DCL + 0, MO_GS_GFT_CRD_ITM_CR = MO_GS_GFT_CRD_ITM_CR + 0.00, QU_GS_GFT_CRD_ITM_CR = QU_GS_GFT_CRD_ITM_CR + 0, MO_GS_GFT_CRD_ITM_CR_VD = MO_GS_GFT_CRD_ITM_CR_VD + 0.00, QU_GS_GFT_CRD_ITM_CR_VD = QU_GS_GFT_CRD_ITM_CR_VD + 0, MO_GS_GFT_CRF_RDM = MO_GS_GFT_CRF_RDM + 0.00, QU_GS_GFT_CRF_RDM = QU_GS_GFT_CRF_RDM + 0, MO_GS_GFT_CRF_RDM_VD = MO_GS_GFT_CRF_RDM_VD + 0.00, QU_GS_GFT_CRF_RDM_VD = QU_GS_GFT_CRF_RDM_VD + 0, MO_GS_STR_CR_ISD = MO_GS_STR_CR_ISD + 0.00, QU_GS_STR_CR_ISD = QU_GS_STR_CR_ISD + 0, MO_GS_STR_CR_ISD_VD = MO_GS_STR_CR_ISD_VD + 0.00, QU_GS_STR_CR_ISD_VD = QU_GS_STR_CR_ISD_VD + 0, MO_GS_STR_CR_RDM = MO_GS_STR_CR_RDM + 0.00, QU_GS_STR_CR_RDM = QU_GS_STR_CR_RDM + 0, MO_GS_STR_CR_RDM_VD = MO_GS_STR_CR_RDM_VD + 0.00, QU_GS_STR_CR_RDM_VD = QU_GS_STR_CR_RDM_VD + 0, MO_GS_ITM_EM_DSC = MO_GS_ITM_EM_DSC + 0.00, QU_GS_ITM_EM_DSC = QU_GS_ITM_EM_DSC + 0, MO_GS_ITM_EM_DSC_VD = MO_GS_ITM_EM_DSC_VD + 0.00, QU_GS_ITM_EM_DSC_VD = QU_GS_ITM_EM_DSC_VD + 0, MO_GS_TRN_EM_DSC = MO_GS_TRN_EM_DSC + 0.00, QU_GS_TRN_EM_DSC = QU_GS_TRN_EM_DSC + 0, MO_GS_TRN_EM_DSC_VD = MO_GS_TRN_EM_DSC_VD + 0.00, QU_GS_TRN_EM_DSC_VD = QU_GS_TRN_EM_DSC_VD + 0, MO_GS_GFT_CRF_ISD_VD = MO_GS_GFT_CRF_ISD_VD + 0.00, QU_GS_GFT_CRF_ISD_VD = QU_GS_GFT_CRF_ISD_VD + 0, MO_GS_GFT_CRF_TND = MO_GS_GFT_CRF_TND + 0.00, QU_GS_GFT_CRF_TND = QU_GS_GFT_CRF_TND + 0, MO_GS_GFT_CRF_TND_VD = MO_GS_GFT_CRF_TND_VD + 0.00, QU_GS_GFT_CRF_TND_VD = QU_GS_GFT_CRF_TND_VD + 0, CP_DS_EM_TOT = CP_DS_EM_TOT + 0.00, QU_DSC_EM = QU_DSC_EM + 0, MO_GS_CT_DSC = MO_GS_CT_DSC + 0.00, QU_GS_CT_DSC = QU_GS_CT_DSC + 0, MO_PRC_OVRD = MO_PRC_OVRD + 0.00, QU_PRC_OVRD = QU_PRC_OVRD + 0, QU_PRC_AJT_CT = QU_PRC_AJT_CT + 0, QU_TRN_RTN_ITM = QU_TRN_RTN_ITM + 0, QU_PYM_LY_CLT_TOT = QU_PYM_LY_CLT_TOT + 0, MO_PYM_LY_CLT_TOT = MO_PYM_LY_CLT_TOT + 0.00, MO_NEW_LY_TOT = MO_NEW_LY_TOT + 0.00, MO_PKP_LY_TOT = MO_PKP_LY_TOT + 0.00, QU_DLT_LY_DSB_TOT = QU_DLT_LY_DSB_TOT + 0, MO_DLT_LY_DSB_TOT = MO_DLT_LY_DSB_TOT + 0.00, MO_LY_FE_CRT_TOT = MO_LY_FE_CRT_TOT + 0.00, QU_LY_FE_CRT_TOT = QU_LY_FE_CRT_TOT + 0, MO_LY_FE_DLT_TOT = MO_LY_FE_DLT_TOT + 0.00, QU_LY_FE_DLT_TOT = QU_LY_FE_DLT_TOT + 0, MO_SOR_NEW_TOT = MO_SOR_NEW_TOT + 0.00, MO_SOR_PTL_TOT = MO_SOR_PTL_TOT + 0.00, QU_PYM_OR_CLT_TOT = QU_PYM_OR_CLT_TOT + 0, MO_PYM_OR_CLT_TOT = MO_PYM_OR_CLT_TOT + 0.00, QU_CNC_OR_DSB_TOT = QU_CNC_OR_DSB_TOT + 0, MO_CNC_OR_DSB_TOT = MO_CNC_OR_DSB_TOT + 0.00, MO_SHP_CHR_TOT = MO_SHP_CHR_TOT + 0.00, QU_SHP_CHR_TOT = QU_SHP_CHR_TOT + 0, MO_SHP_CHR_TX_TOT = MO_SHP_CHR_TX_TOT + 0.00, MO_SHP_CHR_INC_TX_TOT = MO_SHP_CHR_INC_TX_TOT + 0.00, MO_RCV_FND_IN_TOT = MO_RCV_FND_IN_TOT + 0.00, MO_RCV_FND_OUT_TOT = MO_RCV_FND_OUT_TOT + 0.00, QU_RCV_FND_IN_TOT = QU_RCV_FND_IN_TOT + 0, QU_RCV_FND_OUT_TOT = QU_RCV_FND_OUT_TOT + 0, QU_VD_SLS_GS_TX = QU_VD_SLS_GS_TX + 0, QU_VD_RTN_GS_TX = QU_VD_RTN_GS_TX + 0, QU_VD_SLS_GS_NO_TX = QU_VD_SLS_GS_NO_TX + 0, QU_VD_RTN_GS_NO_TX = QU_VD_RTN_GS_NO_TX + 0, MO_BPAY_TOT = MO_BPAY_TOT + 0.00, QU_BPAY_TOT = QU_BPAY_TOT + 0, MO_CHG_RND_IN = MO_CHG_RND_IN + 0.00, MO_CHG_RND_OUT = MO_CHG_RND_OUT + 0.00, TS_MDF_RCRD = CURRENT_TIMESTAMP WHERE ID_RPSTY_TND = :registerId AND ID_STR_RT = :storeId AND TS_TM_SRT =TO_TIMESTAMP(:tillOpenTimeStamp, 'YYYY-MM-DD HH24:MI:SS.FF')";

	@Modifying
	@Query(value = TILL_HISTORY, nativeQuery = true)
	@Transactional
	void updateTilHistory(@Param("transNo") int transNo, @Param("bizDate") String bizDate,
			@Param("registerId") String registerId, @Param("currencyId") int currencyId,
			@Param("lineItemSequence") int lineItemSequence,
			@Param("transactionNetTotal") double transactionNetTotal,
			@Param("transactionTaxTotal") double transactionTaxTotal,
			@Param("transactionDiscountTotal") double transactionDiscountTotal,
			@Param("transactionSalesTotal") double transactionSalesTotal,
			@Param("inclusiveTaxAmountTotal") double inclusiveTaxAmountTotal, @Param("storeId") String storeId,
			@Param("tillOpenTimeStamp") String tillOpenTimeStamp);


	static final String TILL_TENDER_HISTORY = "UPDATE LE_HST_TL_TND SET CP_DS_TND_TOT = 0, CP_MD_TND_LON = CP_MD_TND_LON + 0, CP_TND_OVR_TOT = CP_TND_OVR_TOT + 0.00, CP_MD_TND_PKP_TOT = CP_MD_TND_PKP_TOT + 0, CP_TND_SHRT_TOT = CP_TND_SHRT_TOT + :transactionTenderTotal, QU_MD_BGN_TND = QU_MD_BGN_TND + 0, QU_MD_TND_DS_TOT = 0, QU_MD_TND_LN_TOT = QU_MD_TND_LN_TOT + 0, QU_MD_TND_TOT = QU_MD_TND_TOT ++"
			+ "1, QU_MD_TND_OVR_TOT = QU_MD_TND_OVR_TOT + 0, QU_MD_TND_SHRT_TOT = QU_MD_TND_SHRT_TOT + 1, QU_MD_TND_PKP_TOT = QU_MD_TND_PKP_TOT + 0, QU_MD_TND_RFD_TOT = QU_MD_TND_RFD_TOT + 0, CP_RFD_TND_TOT = CP_RFD_TND_TOT + 0.00, CP_TND_TOT = CP_TND_TOT + :transactionTenderTotal, CP_OPN_TND = CP_OPN_TND + 0, CP_CL_TND_TOT = CP_CL_TND_TOT + 0, QU_CL_TND_MD = QU_CL_TND_MD + 0, CP_MD_RCVF_IN_TOT = CP_MD_RCVF_IN_TOT + 0, CP_MD_RCVF_OUT_TOT = CP_MD_RCVF_OUT_TOT + 0, QU_MD_RCVF_IN_TOT = QU_MD_RCVF_IN_TOT + 0, QU_MD_RCVF_OUT_TOT = QU_MD_RCVF_OUT_TOT + 0, TS_MDF_RCRD = CURRENT_TIMESTAMP, MAX_IN_AMT = CP_TND_SHRT_TOT + :transactionTenderTotal, MAX_OUT_AMT = CP_RFD_TND_TOT + 0.00 WHERE ID_RPSTY_TND = :registerId AND ID_STR_RT = :storeId AND TS_TM_SRT = TO_TIMESTAMP(:tillOpenTimeStamp, 'YYYY-MM-DD HH24:MI:SS.FF') AND TY_TND = :tenderTypeCode AND TY_SB_TND = :tenderMediaIssuerId AND LU_CNY_ISSG_CY = 'IN'";

	@Transactional
	@Modifying
	@Query(value = TILL_TENDER_HISTORY, nativeQuery = true)
	void updateTillTenderHistory(@Param("transactionTenderTotal") double transactionTenderTotal, @Param("registerId") String registerId,
			@Param("storeId") String storeId, @Param("tenderTypeCode") String tenderTypeCode,
			@Param("tillOpenTimeStamp") String tillOpenTimeStamp,
			@Param("tenderMediaIssuerId") String tenderMediaIssuerId

	);

	static final String SAVE_TILL_TENDER_HISTORY = "INSERT INTO LE_HST_TL_TND ( ID_RPSTY_TND, ID_STR_RT, TS_TM_SRT, TY_TND, TY_SB_TND, LU_CNY_ISSG_CY, ID_CNY_ICD, CP_DS_TND_TOT, CP_MD_TND_LON, CP_TND_OVR_TOT, CP_MD_TND_PKP_TOT, CP_TND_SHRT_TOT, QU_MD_BGN_TND, QU_MD_TND_DS_TOT, QU_MD_TND_LN_TOT, QU_MD_TND_TOT, QU_MD_TND_OVR_TOT, QU_MD_TND_SHRT_TOT, QU_MD_TND_PKP_TOT, QU_MD_TND_RFD_TOT, CP_RFD_TND_TOT, CP_TND_TOT, CP_OPN_TND, CP_CL_TND_TOT, QU_CL_TND_MD, CP_MD_RCVF_IN_TOT, CP_MD_RCVF_OUT_TOT, QU_MD_RCVF_IN_TOT, QU_MD_RCVF_OUT_TOT, TS_CRT_RCRD, TS_MDF_RCRD, MAX_IN_AMT, MAX_OUT_AMT) VALUES (:registerId, :storeId, TO_TIMESTAMP(:tillOpenTimeStamp, 'YYYY-MM-DD HH24:MI:SS.FF'), :tenderTypeCode, :tenderMediaIssuerId, 'IN', :currencyId, 0, 0, 0.00, 0, :transactionTenderTotal, 0, 0, 0, 1, 0, 1, 0, 0, 0.00, :transactionTenderTotal, 0, 0, 0, 0, 0, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, :transactionTenderTotal, 0.00)";

	@Transactional
	@Modifying
	@Query(value = SAVE_TILL_TENDER_HISTORY, nativeQuery = true)
	void saveTillTenderHistory(@Param("registerId") String registerId, @Param("storeId") String storeId,
			
			@Param("tillOpenTimeStamp") String tillOpenTimeStamp,
			@Param("tenderTypeCode") String tenderTypeCode, @Param("tenderMediaIssuerId") String tenderMediaIssuerId,
			@Param("currencyId") int currencyId, @Param("transactionTenderTotal") double transactionTenderTotal);

	
	static final String GET_WORKSTATION_HISTORY="select * from LE_HST_WS where ID_STR_RT=:storeId AND ID_WS =:registerId AND FY =:year  AND ID_PR_PRD =:reportingPeriodId";
	
	@Query(value = GET_WORKSTATION_HISTORY, nativeQuery = true)
	String getWorkstationHistory(
			@Param("storeId") String storeId,
			@Param("registerId") String registerId,
			@Param("year") String year,
			@Param("reportingPeriodId") int reportingPeriodId
			);
	
	static final String INSERT_WORKSTATION_HISTORY="insert into LE_HST_WS(ID_STR_RT,ID_WS,FY,TY_PR_PRD,ID_PR_PRD) values(:storeId,:registerId,:year,'DY',:reportingPeriodId)";
	
	@Modifying
	@Query(value = INSERT_WORKSTATION_HISTORY, nativeQuery = true)
	@Transactional
	void insertWorkstationHistory(
			@Param("storeId") String storeId,
			@Param("registerId") String registerId,
			@Param("year") String year,
			@Param("reportingPeriodId") int reportingPeriodId
			);
	
	
	static final String WORKSTATION_HISTORY = "UPDATE LE_HST_WS SET SC_HST_WS = 1, QU_TRN_TOT = QU_TRN_TOT+1, MO_NO_TX_TOT = MO_NO_TX_TOT+0.00, QU_TRN_NO_TX_TOT = QU_TRN_NO_TX_TOT+0, QU_TRN_TX_TOT = QU_TRN_TX_TOT+:lineItemSequence, CP_TX_EXM_TOT = CP_TX_EXM_TOT+0.00, QU_TRN_TX_EXM_TOT = QU_TRN_TX_EXM_TOT+0, CP_RFD_TOT = CP_RFD_TOT+0.00, TP_SLS_EX_TX_TOT = TP_SLS_EX_TX_TOT+:transactionNetTotal-:transactionTaxTotal, QU_RFD_TOT = QU_RFD_TOT+0, MO_RFD_NO_TX_TOT = MO_RFD_NO_TX_TOT+0.00, QU_RFD_NO_TX_TOT = QU_RFD_NO_TX_TOT+0, MO_RFD_TX_EXM_TOT = MO_RFD_TX_EXM_TOT+0.00, QU_RFD_TX_EXM_TOT = QU_RFD_TX_EXM_TOT+0, CP_SLS_GS_TRN_TX = CP_SLS_GS_TRN_TX+:transactionNetTotal-:transactionDiscountTotal, QU_SLS_GS_TRN_TX = QU_SLS_GS_TRN_TX+:lineItemSequence, CP_SLS_GS_TRN_NO_TX = CP_SLS_GS_TRN_NO_TX+0.00, QU_SLS_GS_TRN_NO_TX = QU_SLS_GS_TRN_NO_TX+0, CP_SLS_GS_TRN_TX_EXM = CP_SLS_GS_TRN_TX_EXM+0.00, QU_SLS_GS_TRN_TX_EXM = QU_SLS_GS_TRN_TX_EXM+0, CP_RTN_GS_TRN_TX = CP_RTN_GS_TRN_TX+0.00, QU_RTN_GS_TRN_TX = QU_RTN_GS_TRN_TX+0, MO_ITM_LN_SLS_TOT = MO_ITM_LN_SLS_TOT+:transactionNetTotal, QU_ITM_LN_SLS_TOT = QU_ITM_LN_SLS_TOT+:lineItemSequence, MO_ITM_LN_NO_TX_TOT = MO_ITM_LN_NO_TX_TOT+0.00, QU_ITM_LN_NO_TX_TOT = QU_ITM_LN_NO_TX_TOT+0, MO_ITM_LN_TX_EXM_TOT = MO_ITM_LN_TX_EXM_TOT+0.00, QU_ITM_LN_TX_EXM_TOT = QU_ITM_LN_TX_EXM_TOT+0, CP_RTN_TOT = CP_RTN_TOT+0.00, QU_RTN_TOT = QU_RTN_TOT+0, MO_RTN_NO_TX_TOT = MO_RTN_NO_TX_TOT+0.00, QU_RTN_NO_TX_TOT = QU_RTN_NO_TX_TOT+0, MO_RTN_TX_EXM_TOT = MO_RTN_TX_EXM_TOT+0.00, QU_RTN_TX_EXM_TOT = QU_RTN_TX_EXM_TOT+0, MO_TRN_NO_TX_NO_MRCH_TOT = MO_TRN_NO_TX_NO_MRCH_TOT+0.00, QU_TRN_NO_TX_NO_MRCH_TOT = QU_TRN_NO_TX_NO_MRCH_TOT+0, MO_RTN_NO_TX_NO_MRCH_TOT = MO_RTN_NO_TX_NO_MRCH_TOT+0.00, QU_RTN_NO_TX_NO_MRCH_TOT = QU_RTN_NO_TX_NO_MRCH_TOT+0, MO_TX_NO_MRCH_TOT = MO_TX_NO_MRCH_TOT+0.00, QU_TRN_TX_NO_MRCH_TOT = QU_TRN_TX_NO_MRCH_TOT+0, MO_RTN_TX_NO_MRCH_TOT = MO_RTN_TX_NO_MRCH_TOT+0.00, QU_RTN_TX_NO_MRCH_TOT = QU_RTN_TX_NO_MRCH_TOT+0, MO_GFT_CRD_TOT = MO_GFT_CRD_TOT+0.00, QU_GFT_CRD_TOT = QU_GFT_CRD_TOT+0, MO_RTN_GFT_CRD_TOT = MO_RTN_GFT_CRD_TOT+0.00, QU_RTN_GFT_CRD_TOT = QU_RTN_GFT_CRD_TOT+0, MO_HP_TOT = MO_HP_TOT+0.00, QU_HP_TOT = QU_HP_TOT+0, MO_FE_RSTK_TOT = MO_FE_RSTK_TOT+0.00, QU_FE_RSTK_TOT = QU_FE_RSTK_TOT+0, MO_FE_RSTK_NO_TX_TOT = MO_FE_RSTK_NO_TX_TOT+0.00, QU_FE_RSTK_NO_TX_TOT = QU_FE_RSTK_NO_TX_TOT+0, CP_SLS_GS_NO_MRCH_TX = CP_SLS_GS_NO_MRCH_TX+0.00, QU_SLS_GS_NO_MRCH_TX = QU_SLS_GS_NO_MRCH_TX+0, CP_SLS_GS_NO_MRCH_NO_TX = CP_SLS_GS_NO_MRCH_NO_TX+0.00, QU_SLS_GS_NO_MRCH_NO_TX = QU_SLS_GS_NO_MRCH_NO_TX+0, CP_SLS_GS_ITM_GFT_CRD = CP_SLS_GS_ITM_GFT_CRD+0.00, QU_SLS_GS_ITM_GFT_CRD = QU_SLS_GS_ITM_GFT_CRD+0, MO_GFT_CRF_ISD = MO_GFT_CRF_ISD+0.00, QU_GFT_CRF_ISD = QU_GFT_CRF_ISD+0, MO_GFT_CRD_ISD = MO_GFT_CRD_ISD+0.00, QU_GFT_CRD_ISD = QU_GFT_CRD_ISD+0, MO_GFT_CRD_RLD = MO_GFT_CRD_RLD+0.00, QU_GFT_CRD_RLD = QU_GFT_CRD_RLD+0, MO_GFT_CRD_RDM = MO_GFT_CRD_RDM+0.00, QU_GFT_CRD_RDM = QU_GFT_CRD_RDM+0, MO_GFT_CRD_ISD_VD = MO_GFT_CRD_ISD_VD+0.00, QU_GFT_CRD_ISD_VD = QU_GFT_CRD_ISD_VD+0, MO_GFT_CRD_RLD_VD = MO_GFT_CRD_RLD_VD+0.00, QU_GFT_CRD_RLD_VD = QU_GFT_CRD_RLD_VD+0, MO_GFT_CRD_RDM_VD = MO_GFT_CRD_RDM_VD+0.00, QU_GFT_CRD_RDM_VD = QU_GFT_CRD_RDM_VD+0, QU_HA_APP = QU_HA_APP+0, QU_HA_DCL = QU_HA_DCL+0, MO_GS_GFT_CRD_ITM_CR = MO_GS_GFT_CRD_ITM_CR+0.00, QU_GS_GFT_CRD_ITM_CR = QU_GS_GFT_CRD_ITM_CR+0, MO_GS_GFT_CRD_ITM_CR_VD = MO_GS_GFT_CRD_ITM_CR_VD+0.00, QU_GS_GFT_CRD_ITM_CR_VD = QU_GS_GFT_CRD_ITM_CR_VD+0, MO_GS_GFT_CRF_RDM = MO_GS_GFT_CRF_RDM+0.00, QU_GS_GFT_CRF_RDM = QU_GS_GFT_CRF_RDM+0, MO_GS_GFT_CRF_RDM_VD = MO_GS_GFT_CRF_RDM_VD+0.00, QU_GS_GFT_CRF_RDM_VD = QU_GS_GFT_CRF_RDM_VD+0, MO_GS_STR_CR_ISD = MO_GS_STR_CR_ISD+0.00, QU_GS_STR_CR_ISD = QU_GS_STR_CR_ISD+0, MO_GS_STR_CR_ISD_VD = MO_GS_STR_CR_ISD_VD+0.00, QU_GS_STR_CR_ISD_VD = QU_GS_STR_CR_ISD_VD+0, MO_GS_STR_CR_RDM = MO_GS_STR_CR_RDM+0.00, QU_GS_STR_CR_RDM = QU_GS_STR_CR_RDM+0, MO_GS_STR_CR_RDM_VD = MO_GS_STR_CR_RDM_VD+0.00, QU_GS_STR_CR_RDM_VD = QU_GS_STR_CR_RDM_VD+0, MO_GS_ITM_EM_DSC = MO_GS_ITM_EM_DSC+0.00, QU_GS_ITM_EM_DSC = QU_GS_ITM_EM_DSC+0, MO_GS_ITM_EM_DSC_VD = MO_GS_ITM_EM_DSC_VD+0.00, QU_GS_ITM_EM_DSC_VD = QU_GS_ITM_EM_DSC_VD+0, MO_GS_TRN_EM_DSC = MO_GS_TRN_EM_DSC+0.00, QU_GS_TRN_EM_DSC = QU_GS_TRN_EM_DSC+0, MO_GS_TRN_EM_DSC_VD = MO_GS_TRN_EM_DSC_VD+0.00, QU_GS_TRN_EM_DSC_VD = QU_GS_TRN_EM_DSC_VD+0, MO_GS_GFT_CRF_ISD_VD = MO_GS_GFT_CRF_ISD_VD+0.00, QU_GS_GFT_CRF_ISD_VD = QU_GS_GFT_CRF_ISD_VD+0, MO_GS_GFT_CRF_TND = MO_GS_GFT_CRF_TND+0.00, QU_GS_GFT_CRF_TND = QU_GS_GFT_CRF_TND+0, MO_GS_GFT_CRF_TND_VD = MO_GS_GFT_CRF_TND_VD+0.00, QU_GS_GFT_CRF_TND_VD = QU_GS_GFT_CRF_TND_VD+0, MO_GS_EM_DSC = MO_GS_EM_DSC+0.00, QU_GS_EM_DSC = QU_GS_EM_DSC+0, MO_GS_CT_DSC = MO_GS_CT_DSC+0.00, QU_GS_CT_DSC = QU_GS_CT_DSC+0, MO_PRC_OVRD = MO_PRC_OVRD+0.00, QU_PRC_OVRD = QU_PRC_OVRD+0, QU_PRC_AJT_CT = QU_PRC_AJT_CT+0, QU_TRN_RTN_ITM = QU_TRN_RTN_ITM+0, QU_PYM_LY_CLT_TOT = QU_PYM_LY_CLT_TOT+0, MO_PYM_LY_CLT_TOT = MO_PYM_LY_CLT_TOT+0.00, MO_NEW_LY_TOT = MO_NEW_LY_TOT+0.00, MO_PKP_LY_TOT = MO_PKP_LY_TOT+0.00, QU_DLT_LY_DSB_TOT = QU_DLT_LY_DSB_TOT+0, MO_DLT_LY_DSB_TOT = MO_DLT_LY_DSB_TOT+0.00, MO_LY_FE_CRT_TOT = MO_LY_FE_CRT_TOT+0.00, QU_LY_FE_CRT_TOT = QU_LY_FE_CRT_TOT+0, MO_LY_FE_DLT_TOT = MO_LY_FE_DLT_TOT+0.00, QU_LY_FE_DLT_TOT = QU_LY_FE_DLT_TOT+0, MO_RCV_FND_IN_TOT = MO_RCV_FND_IN_TOT + 0.00, MO_RCV_FND_OUT_TOT = MO_RCV_FND_OUT_TOT + 0.00, QU_RCV_FND_IN_TOT = QU_RCV_FND_IN_TOT + 0, QU_RCV_FND_OUT_TOT = QU_RCV_FND_OUT_TOT + 0, MO_SOR_NEW_TOT = MO_SOR_NEW_TOT + 0.00, MO_SOR_PTL_TOT = MO_SOR_PTL_TOT + 0.00, QU_PYM_OR_CLT_TOT = QU_PYM_OR_CLT_TOT + 0, MO_PYM_OR_CLT_TOT = MO_PYM_OR_CLT_TOT + 0.00, QU_CNC_OR_DSB_TOT = QU_CNC_OR_DSB_TOT + 0, MO_CNC_OR_DSB_TOT = MO_CNC_OR_DSB_TOT + 0.00, MO_SHP_CHR_TOT = MO_SHP_CHR_TOT + 0.00, QU_SHP_CHR_TOT = QU_SHP_CHR_TOT + 0, MO_SHP_CHR_TX_TOT = MO_SHP_CHR_TX_TOT + 0.00, MO_SHP_CHR_INC_TX_TOT = MO_SHP_CHR_INC_TX_TOT + 0.00, MO_BPAY_TOT = MO_BPAY_TOT + 0.00, QU_BPAY_TOT = QU_BPAY_TOT + 0, MO_CHG_RND_IN = MO_CHG_RND_IN + 0.00, MO_CHG_RND_OUT = MO_CHG_RND_OUT + 0.00, CP_SLS_GS_ITM_TX = CP_SLS_GS_ITM_TX+:transactionNetTotal- :transactionDiscountTotal, QU_SLS_GS_ITM_TX = QU_SLS_GS_ITM_TX+:lineItemSequence, CP_RTN_GS_ITM_TX = CP_RTN_GS_ITM_TX+0.00, QU_RTN_GS_ITM_TX = QU_RTN_GS_ITM_TX+0, CP_TX_TOT = CP_TX_TOT+:transactionTaxTotal, CP_INC_TX_TOT = CP_INC_TX_TOT+:transactionInclusiveTaxTotal, MO_RFD_TX_TOT = MO_RFD_TX_TOT+0.00, MO_RFD_INC_TX_TOT = MO_RFD_INC_TX_TOT+0.00, MO_RTN_TX_TOT = MO_RTN_TX_TOT+0.00, MO_RTN_INC_TX_TOT = MO_RTN_INC_TX_TOT+0.00, CP_SLS_ITM_TX = CP_SLS_ITM_TX+0.00, CP_SLS_ITM_INC_TX = CP_SLS_ITM_INC_TX+:transactionTaxTotal, CP_TRN_SLS_TX = CP_TRN_SLS_TX+:transactionTaxTotal, CP_TRN_SLS_INC_TX = CP_TRN_SLS_INC_TX+:transactionTaxTotal, MO_DITM_STCPN_TOT = MO_DITM_STCPN_TOT+0.00, QU_DITM_STCPN_TOT = QU_DITM_STCPN_TOT+0, MO_DTRN_STCPN_TOT = MO_DTRN_STCPN_TOT+0.00, QU_DTRN_STCPN_TOT = QU_DTRN_STCPN_TOT+0, CP_DSC_MSC_TOT = CP_DSC_MSC_TOT+0.00, QU_DSC_MSC_TOT = QU_DSC_MSC_TOT+0, CP_DSC_TOT = CP_DSC_TOT+0.00, QU_DSC_TOT = QU_DSC_TOT+0, CP_MKD_TOT = CP_MKD_TOT+0.00, QU_MKD_TOT = QU_MKD_TOT+0, CP_VD_PST_TRN_TOT = CP_VD_PST_TRN_TOT+0.00, QU_VD_PST_TRN_TOT = QU_VD_PST_TRN_TOT+0, CP_VD_LN_ITM_TOT = CP_VD_LN_ITM_TOT+0.00, QU_VD_LN_ITM_TOT = QU_VD_LN_ITM_TOT+0, CP_VD_TRN_TOT = CP_VD_TRN_TOT+0.00, QU_VD_TRN_TOT = QU_VD_TRN_TOT+0, QU_TRN_NO_SLS_TOT = QU_TRN_NO_SLS_TOT+0, QU_PKP_TND_TOT = QU_PKP_TND_TOT+0, CP_PKP_TND_TOT = CP_PKP_TND_TOT+0.00, QU_LON_TND_TOT = QU_LON_TND_TOT+0, CP_LON_TND_TOT = CP_LON_TND_TOT+0.00, QU_VD_SLS_GS_TX = QU_VD_SLS_GS_TX + 0, QU_VD_RTN_GS_TX = QU_VD_RTN_GS_TX + 0, QU_VD_SLS_GS_NO_TX = QU_VD_SLS_GS_NO_TX + 0, QU_VD_RTN_GS_NO_TX = QU_VD_RTN_GS_NO_TX + 0, TS_MDF_RCRD = CURRENT_TIMESTAMP WHERE ID_STR_RT = :storeId AND ID_WS = :registerId AND FY = :year AND TY_PR_PRD = 'DY' AND ID_PR_PRD = :reportingPeriodId";

	@Transactional
	@Query(value = WORKSTATION_HISTORY, nativeQuery = true)
	@Modifying
	void updateWorkstationHistory(

			@Param("lineItemSequence") int lineItemSequence,
			@Param("transactionNetTotal") double transactionNetTotal,
			@Param("transactionTaxTotal") double transactionTaxTotal,
			@Param("transactionDiscountTotal") double transactionDiscountTotal,
			@Param("transactionInclusiveTaxTotal") double transactionInclusiveTaxTotal,
			@Param("storeId") String storeId, 
			 @Param("registerId") String registerId,
			 @Param("year") String year,
			@Param("reportingPeriodId") int reportingPeriodId);

	static final String WORKSTATION_TENDER_HISTORY = "UPDATE LE_HST_WS_TND SET CP_DS_TND_TOT = CP_DS_TND_TOT+0, CP_MD_TND_LON_TOT = CP_MD_TND_LON_TOT+0, CP_TND_OVR_TOT = CP_TND_OVR_TOT+0.00, CP_MD_TND_PKP_TOT = CP_MD_TND_PKP_TOT+0, CP_TND_SHR_TOT = CP_TND_SHR_TOT+:transactionTenderTotal, QU_MD_BGN_TND = QU_MD_BGN_TND+0, QU_MD_TND_DS_TOT = QU_MD_TND_DS_TOT+0, QU_MD_TND_LN_TOT = QU_MD_TND_LN_TOT+0, QU_MD_TND_TOT = QU_MD_TND_TOT+1, QU_MD_TND_OVR_TOT = QU_MD_TND_OVR_TOT+0, QU_MD_TND_SHRT_TOT = QU_MD_TND_SHRT_TOT+1, QU_MD_TND_PKP_TOT = QU_MD_TND_PKP_TOT+0, QU_MD_TND_RFD_TOT = QU_MD_TND_RFD_TOT+0, CP_RFD_TND_TOT = CP_RFD_TND_TOT+0.00, CP_TND_TOT = CP_TND_TOT+:transactionTenderTotal, CP_OPN_TND = CP_OPN_TND+0, CP_CL_TND_TOT = CP_CL_TND_TOT+0, QU_CL_TND_MD = QU_CL_TND_MD+0, CP_MD_RCVF_IN_TOT = CP_MD_RCVF_IN_TOT + 0, CP_MD_RCVF_OUT_TOT = CP_MD_RCVF_OUT_TOT + 0, QU_MD_RCVF_IN_TOT = QU_MD_RCVF_IN_TOT + 0, QU_MD_RCVF_OUT_TOT = QU_MD_RCVF_OUT_TOT + 0, CP_TND_RCN_TOT = CP_TND_RCN_TOT + 0.00, QU_TND_RCN_TOT = QU_TND_RCN_TOT + 0, TS_MDF_RCRD = CURRENT_TIMESTAMP WHERE ID_STR_RT = :storeId AND ID_WS = :registerId AND FY = :year AND TY_PR_PRD = 'DY' AND ID_PR_PRD = :reportingPeriodId AND TY_TND = :tenderTypeCode AND TY_SB_TND = :tenderMediaIssuerId AND LU_CNY_ISSG_CY = 'IN'";

	@Transactional
	@Modifying
	@Query(value = WORKSTATION_TENDER_HISTORY, nativeQuery = true)
	void updateWorkstationTenderHistory(@Param("transactionTenderTotal") double transactionTenderTotal,
			 @Param("registerId") String registerId,
			 @Param("storeId") String storeId,
			@Param("year") String year, @Param("tenderTypeCode") String tenderTypeCode,
			@Param("reportingPeriodId") int reportingPeriodId, @Param("tenderMediaIssuerId") String tenderMediaIssuerId);

	static final String SAVE_WORKSTATION_TENDER_HISTORY = "INSERT INTO LE_HST_WS_TND ( ID_STR_RT, ID_WS, FY, TY_PR_PRD, ID_PR_PRD, TY_TND, TY_SB_TND, LU_CNY_ISSG_CY, ID_CNY_ICD, CP_DS_TND_TOT, CP_MD_TND_LON_TOT, CP_TND_OVR_TOT, CP_MD_TND_PKP_TOT, CP_TND_SHR_TOT, QU_MD_BGN_TND, QU_MD_TND_DS_TOT, QU_MD_TND_LN_TOT, QU_MD_TND_TOT, QU_MD_TND_OVR_TOT, QU_MD_TND_SHRT_TOT, QU_MD_TND_PKP_TOT, QU_MD_TND_RFD_TOT, CP_RFD_TND_TOT, CP_TND_TOT, CP_OPN_TND, CP_CL_TND_TOT, QU_CL_TND_MD, CP_MD_RCVF_IN_TOT, CP_MD_RCVF_OUT_TOT, QU_MD_RCVF_IN_TOT, QU_MD_RCVF_OUT_TOT, CP_TND_RCN_TOT, QU_TND_RCN_TOT, TS_CRT_RCRD, TS_MDF_RCRD) VALUES(:storeId, :registerId, :year, 'DY', :reportingPeriodId, :tenderTypeCode, :tenderMediaIssuerId, 'IN', :currencyId, 0, 0, 0.00, 0, :transactionTenderTotal, 0, 0, 0, 1, 0, 1, 0, 0, 0.00, :transactionTenderTotal,0, 0, 0, 0, 0, 0, 0, 0.00, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

	@Transactional
	@Modifying
	@Query(value = SAVE_WORKSTATION_TENDER_HISTORY, nativeQuery = true)
	void updateWorkstationTenderHistory1(@Param("storeId") String storeId, @Param("registerId") String registerId,
			@Param("year") String year,
			@Param("reportingPeriodId") int reportingPeriodId,
			@Param("tenderTypeCode") String tenderTypeCode, 
			@Param("tenderMediaIssuerId") String tenderMediaIssuerId,
			@Param("currencyId") int currencyId,
			@Param("transactionTenderTotal") float transactionTenderTotal);

	static final String STORE_HISTORY = "UPDATE LE_HST_STR SET QU_TRN_TOT=QU_TRN_TOT+1,MO_NO_TX_TOT=MO_NO_TX_TOT+0.0,QU_TRN_NO_TX_TOT=QU_TRN_NO_TX_TOT+0,QU_TRN_TX_TOT=QU_TRN_TX_TOT+:lineItemSequence,MO_TX_EXM_TOT=MO_TX_EXM_TOT+0.0,QU_TRN_TX_EXM_TOT=QU_TRN_TX_EXM_TOT+0,MO_RFD_TOT=MO_RFD_TOT+0.0,TP_SLS_EX_TX_TOT=TP_SLS_EX_TX_TOT+:transactionNetTotal-:transactionTaxTotal,QU_RFD_TOT=QU_RFD_TOT+0,MO_RFD_NO_TX_TOT=MO_RFD_NO_TX_TOT+0.0,QU_RFD_NO_TX_TOT=QU_RFD_NO_TX_TOT+0,MO_RFD_TX_EXM_TOT=MO_RFD_TX_EXM_TOT+0.0,QU_RFD_TX_EXM_TOT=QU_RFD_TX_EXM_TOT+0,CP_SLS_GS_TRN_TX=CP_SLS_GS_TRN_TX+:transactionNetTotal-:transactionDiscountTotal,QU_SLS_GS_TRN_TX=QU_SLS_GS_TRN_TX+:lineItemSequence,CP_SLS_GS_TRN_NO_TX=CP_SLS_GS_TRN_NO_TX+0.0,QU_SLS_GS_TRN_NO_TX=QU_SLS_GS_TRN_NO_TX+0,CP_SLS_GS_TRN_TX_EXM=CP_SLS_GS_TRN_TX_EXM+0.0,QU_SLS_GS_TRN_TX_EXM=QU_SLS_GS_TRN_TX_EXM+0,CP_RTN_GS_TRN_TX=CP_RTN_GS_TRN_TX+0.0,QU_RTN_GS_TRN_TX=QU_RTN_GS_TRN_TX+0,MO_ITM_LN_SLS_TOT=MO_ITM_LN_SLS_TOT+:transactionNetTotal,QU_ITM_LN_SLS_TOT=QU_ITM_LN_SLS_TOT+:lineItemSequence,MO_ITM_LN_NO_TX_TOT=MO_ITM_LN_NO_TX_TOT+0.0,QU_ITM_LN_NO_TX_TOT=QU_ITM_LN_NO_TX_TOT+0.0,MO_ITM_LN_TX_EXM_TOT=MO_ITM_LN_TX_EXM_TOT+0.0,QU_ITM_LN_TX_EXM_TOT=QU_ITM_LN_TX_EXM_TOT+0.0,MO_RTN_TOT=MO_RTN_TOT+0.0,QU_RTN_TOT=QU_RTN_TOT+0.0,MO_RTN_NO_TX_TOT=MO_RTN_NO_TX_TOT+0.0,QU_RTN_NO_TX_TOT=QU_RTN_NO_TX_TOT+0.0,MO_RTN_TX_EXM_TOT=MO_RTN_TX_EXM_TOT+0.0,QU_RTN_TX_EXM_TOT=QU_RTN_TX_EXM_TOT+0.0,MO_TRN_NO_TX_NO_MRCH_TOT=MO_TRN_NO_TX_NO_MRCH_TOT+0.0,QU_TRN_NO_TX_NO_MRCH_TOT=QU_TRN_NO_TX_NO_MRCH_TOT+0.0,MO_RTN_NO_TX_NO_MRCH_TOT=MO_RTN_NO_TX_NO_MRCH_TOT+0.0,QU_RTN_NO_TX_NO_MRCH_TOT=QU_RTN_NO_TX_NO_MRCH_TOT+0.0,MO_TX_NO_MRCH_TOT=MO_TX_NO_MRCH_TOT+0.0,QU_TRN_TX_NO_MRCH_TOT=QU_TRN_TX_NO_MRCH_TOT+0.0,MO_RTN_TX_NO_MRCH_TOT=MO_RTN_TX_NO_MRCH_TOT+0.0,QU_RTN_TX_NO_MRCH_TOT=QU_RTN_TX_NO_MRCH_TOT+0.0,MO_GFT_CRD_TOT=MO_GFT_CRD_TOT+0.0,QU_GFT_CRD_TOT=QU_GFT_CRD_TOT+0.0,MO_RTN_GFT_CRD_TOT=MO_RTN_GFT_CRD_TOT+0.0,QU_RTN_GFT_CRD_TOT=QU_RTN_GFT_CRD_TOT+0.0,MO_HP_TOT=MO_HP_TOT+0.0,QU_HP_TOT=QU_HP_TOT+0,MO_FE_RSTK_TOT=MO_FE_RSTK_TOT+0.0,QU_FE_RSTK_TOT=QU_FE_RSTK_TOT+0.0,MO_FE_RSTK_NO_TX_TOT=MO_FE_RSTK_NO_TX_TOT+0.0,QU_FE_RSTK_NO_TX_TOT=QU_FE_RSTK_NO_TX_TOT+0.0,MO_SHP_CHR_TOT=MO_SHP_CHR_TOT+0.0,QU_SHP_CHR_TOT=QU_SHP_CHR_TOT+0,MO_SHP_CHR_TX_TOT=MO_SHP_CHR_TX_TOT+0.0,MO_SHP_CHR_INC_TX_TOT=MO_SHP_CHR_INC_TX_TOT+0.0,CP_SLS_GS_NO_MRCH_TX=CP_SLS_GS_NO_MRCH_TX+0.0,QU_SLS_GS_NO_MRCH_TX=QU_SLS_GS_NO_MRCH_TX+0.0,CP_SLS_GS_NO_MRCH_NO_TX=CP_SLS_GS_NO_MRCH_NO_TX+0.0,QU_SLS_GS_NO_MRCH_NO_TX=QU_SLS_GS_NO_MRCH_NO_TX+0.0,CP_SLS_GS_ITM_TX=CP_SLS_GS_ITM_TX+:transactionNetTotal- :transactionDiscountTotal,QU_SLS_GS_ITM_TX=QU_SLS_GS_ITM_TX+:lineItemSequence,CP_RTN_GS_ITM_TX=CP_RTN_GS_ITM_TX+0.0,QU_RTN_GS_ITM_TX=QU_RTN_GS_ITM_TX+0.0,MO_GFT_CRF_ISD=MO_GFT_CRF_ISD+0.0,QU_GFT_CRF_ISD=QU_GFT_CRF_ISD+0.0,MO_GFT_CRD_ISD=MO_GFT_CRD_ISD+0.0,QU_GFT_CRD_ISD=QU_GFT_CRD_ISD+0.0,MO_GFT_CRD_RLD=MO_GFT_CRD_RLD+0.0,QU_GFT_CRD_RLD=QU_GFT_CRD_RLD+0.0,MO_GFT_CRD_RDM=MO_GFT_CRD_RDM+0.0,QU_GFT_CRD_RDM=QU_GFT_CRD_RDM+0.0,MO_GFT_CRD_ISD_VD=MO_GFT_CRD_ISD_VD+0.0,QU_GFT_CRD_ISD_VD=QU_GFT_CRD_ISD_VD+0.0,MO_GFT_CRD_RLD_VD=MO_GFT_CRD_RLD_VD+0.0,QU_GFT_CRD_RLD_VD=QU_GFT_CRD_RLD_VD+0.0,MO_GFT_CRD_RDM_VD=MO_GFT_CRD_RDM_VD+0.0,QU_GFT_CRD_RDM_VD=QU_GFT_CRD_RDM_VD+0.0,QU_HA_APP=QU_HA_APP+0,QU_HA_DCL=QU_HA_DCL+0,MO_GS_GFT_CRD_ITM_CR=MO_GS_GFT_CRD_ITM_CR+0.0,QU_GS_GFT_CRD_ITM_CR=QU_GS_GFT_CRD_ITM_CR+0.0,MO_GS_GFT_CRD_ITM_CR_VD=MO_GS_GFT_CRD_ITM_CR_VD+0.0,QU_GS_GFT_CRD_ITM_CR_VD=QU_GS_GFT_CRD_ITM_CR_VD+0.0,MO_GS_GFT_CRF_RDM=MO_GS_GFT_CRF_RDM+0.0,QU_GS_GFT_CRF_RDM=QU_GS_GFT_CRF_RDM+0.0,MO_GS_GFT_CRF_RDM_VD=MO_GS_GFT_CRF_RDM_VD+0.0,QU_GS_GFT_CRF_RDM_VD=QU_GS_GFT_CRF_RDM_VD+0.0,MO_GS_STR_CR_ISD=MO_GS_STR_CR_ISD+0.0,QU_GS_STR_CR_ISD=QU_GS_STR_CR_ISD+0.0,MO_GS_STR_CR_ISD_VD=MO_GS_STR_CR_ISD_VD+0.0,QU_GS_STR_CR_ISD_VD=QU_GS_STR_CR_ISD_VD+0.0,MO_GS_STR_CR_RDM=MO_GS_STR_CR_RDM+0.0,QU_GS_STR_CR_RDM=QU_GS_STR_CR_RDM+0.0,MO_GS_STR_CR_RDM_VD=MO_GS_STR_CR_RDM_VD+0.0,QU_GS_STR_CR_RDM_VD=QU_GS_STR_CR_RDM_VD+0.0,MO_GS_ITM_EM_DSC=MO_GS_ITM_EM_DSC+0.0,QU_GS_ITM_EM_DSC=QU_GS_ITM_EM_DSC+0.0,MO_GS_ITM_EM_DSC_VD=MO_GS_ITM_EM_DSC_VD+0.0,QU_GS_ITM_EM_DSC_VD=QU_GS_ITM_EM_DSC_VD+0.0,MO_GS_TRN_EM_DSC=MO_GS_TRN_EM_DSC+0.0,QU_GS_TRN_EM_DSC=QU_GS_TRN_EM_DSC+0.0,MO_GS_TRN_EM_DSC_VD=MO_GS_TRN_EM_DSC_VD+0.0,QU_GS_TRN_EM_DSC_VD=QU_GS_TRN_EM_DSC_VD+0.0,MO_GS_GFT_CRF_ISD_VD=MO_GS_GFT_CRF_ISD_VD+0.0,QU_GS_GFT_CRF_ISD_VD=QU_GS_GFT_CRF_ISD_VD+0.0,MO_GS_GFT_CRF_TND=MO_GS_GFT_CRF_TND+0.0,QU_GS_GFT_CRF_TND=QU_GS_GFT_CRF_TND+0.0,MO_GS_GFT_CRF_TND_VD=MO_GS_GFT_CRF_TND_VD+0.0,QU_GS_GFT_CRF_TND_VD=QU_GS_GFT_CRF_TND_VD+0.0,MO_GS_EM_DSC=MO_GS_EM_DSC+0.0,QU_GS_EM_DSC=QU_GS_EM_DSC+0.0,MO_GS_CT_DSC=MO_GS_CT_DSC+0.0,QU_GS_CT_DSC=QU_GS_CT_DSC+0.0,MO_PRC_OVRD=MO_PRC_OVRD+0.0,QU_PRC_OVRD=QU_PRC_OVRD+0.0,QU_PRC_AJT_CT=QU_PRC_AJT_CT+0.0,QU_TRN_RTN_ITM=QU_TRN_RTN_ITM+0,QU_PYM_LY_CLT_TOT=QU_PYM_LY_CLT_TOT+0,MO_PYM_LY_CLT_TOT=MO_PYM_LY_CLT_TOT+0.0,MO_NEW_LY_TOT=MO_NEW_LY_TOT+0.0,MO_PKP_LY_TOT=MO_PKP_LY_TOT+0.0,QU_DLT_LY_DSB_TOT=QU_DLT_LY_DSB_TOT+0,MO_DLT_LY_DSB_TOT=MO_DLT_LY_DSB_TOT+0.0,MO_LY_FE_CRT_TOT=MO_LY_FE_CRT_TOT+0.0,QU_LY_FE_CRT_TOT=QU_LY_FE_CRT_TOT+0,MO_LY_FE_DLT_TOT=MO_LY_FE_DLT_TOT+0.0,QU_LY_FE_DLT_TOT=QU_LY_FE_DLT_TOT+0,MO_SOR_NEW_TOT=MO_SOR_NEW_TOT+0.0,MO_SOR_PTL_TOT=MO_SOR_PTL_TOT+0.0,QU_PYM_OR_CLT_TOT=QU_PYM_OR_CLT_TOT+0,MO_PYM_OR_CLT_TOT=MO_PYM_OR_CLT_TOT+0.0,QU_CNC_OR_DSB_TOT=QU_CNC_OR_DSB_TOT+0,MO_CNC_OR_DSB_TOT=MO_CNC_OR_DSB_TOT+0.0,MO_RCV_FND_IN_TOT=MO_RCV_FND_IN_TOT+0.0,MO_RCV_FND_OUT_TOT=MO_RCV_FND_OUT_TOT+0.0,QU_RCV_FND_IN_TOT=QU_RCV_FND_IN_TOT+0,QU_RCV_FND_OUT_TOT=QU_RCV_FND_OUT_TOT+0,MO_BPAY_TOT=MO_BPAY_TOT+0.0,QU_BPAY_TOT=QU_BPAY_TOT+0,MO_CHG_RND_IN=MO_CHG_RND_IN+0.0,MO_CHG_RND_OUT=MO_CHG_RND_OUT+0.0,MO_TX_TOT=MO_TX_TOT+:transactionTaxTotal,CP_INC_TX_TOT=CP_INC_TX_TOT+:transactionInclusiveTaxTotal,MO_RFD_TX_TOT=MO_RFD_TX_TOT+0.0,MO_RFD_INC_TX_TOT=MO_RFD_INC_TX_TOT+0.0,MO_RTN_TX_TOT=MO_RTN_TX_TOT+0.0,MO_RTN_INC_TX_TOT=MO_RTN_INC_TX_TOT+0.0,CP_SLS_ITM_TX=CP_SLS_ITM_TX+0.0,CP_SLS_ITM_INC_TX=CP_SLS_ITM_INC_TX+:transactionInclusiveTaxTotal,CP_TRN_SLS_INC_TX=CP_TRN_SLS_INC_TX+:transactionTaxTotal,CP_TRN_SLS_TX=CP_TRN_SLS_TX+:transactionTaxTotal,MO_DITM_STCPN_TOT=MO_DITM_STCPN_TOT+0.0,QU_DITM_STCPN_TOT=QU_DITM_STCPN_TOT+0,MO_DTRN_STCPN_TOT=MO_DTRN_STCPN_TOT+0.0,QU_DTRN_STCPN_TOT=QU_DTRN_STCPN_TOT+0,MO_DSC_MSC_TOT=MO_DSC_MSC_TOT+0.0,QU_DS_MSC_TOT=QU_DS_MSC_TOT+0,MO_DSC_TOT=MO_DSC_TOT+0.0,QU_DSC_TOT=QU_DSC_TOT+0,MO_MKD_TOT=MO_MKD_TOT+0.0,QU_MKD_TOT=QU_MKD_TOT+0,MO_VD_PST_TRN_TOT=MO_VD_PST_TRN_TOT+0.0,QU_VD_PST_TRN_TOT=QU_VD_PST_TRN_TOT+0,MO_VD_LN_ITM_TOT=MO_VD_LN_ITM_TOT+0.0,QU_VD_LN_ITM_TOT=QU_VD_LN_ITM_TOT+0.0,MO_VD_TRN_TOT=MO_VD_TRN_TOT+0.0,QU_VD_TRN_TOT=QU_VD_TRN_TOT+0,QU_TRN_NO_SLS_TOT=QU_TRN_NO_SLS_TOT+0,QU_PKP_TND_TOT=QU_PKP_TND_TOT+0,MO_PKP_TND_TOT=MO_PKP_TND_TOT+0.0,QU_LON_TND_TOT=QU_LON_TND_TOT+0,MO_LON_TND_TOT=MO_LON_TND_TOT+0.0,QU_VD_SLS_GS_TX=QU_VD_SLS_GS_TX+0,QU_VD_RTN_GS_TX=QU_VD_RTN_GS_TX+0,QU_VD_SLS_GS_NO_TX=QU_VD_SLS_GS_NO_TX+0,QU_VD_RTN_GS_NO_TX=QU_VD_RTN_GS_NO_TX+0,TS_MDF_RCRD = CURRENT_TIMESTAMP WHERE ID_STR_RT = :storeId AND FY = :year AND TY_PR_PRD = 'DY' AND ID_PR_PRD = :reportingPeriodId";

	@Transactional
	@Modifying
	@Query(value = STORE_HISTORY, nativeQuery = true)
	void updateStoreHistory(

			@Param("lineItemSequence") int lineItemSequence,
 			@Param("transactionNetTotal") double transactionNetTotal,
			@Param("transactionDiscountTotal") double transactionDiscountTotal,
			// @Param("saleReturnLineItem") double saleReturnLineItem,
			@Param("transactionTaxTotal") double transactionTaxTotal,
			@Param("transactionInclusiveTaxTotal") double transactionInclusiveTaxTotal,
			// @Param("saleReturnLineItemInclusiveTaxAmount") double
			// saleReturnLineItemInclusiveTaxAmount,
			@Param("storeId") String storeId, @Param("year") String year,
			@Param("reportingPeriodId") int reportingPeriodId);

	static final String STORE_TENDER_HISTORY = "UPDATE LE_HST_STR_TND SET MO_MD_LON_TND_TOT=MO_MD_LON_TND_TOT+0.0,MO_TND_OVR_TOT=MO_TND_OVR_TOT+0.0,MO_MD_TND_PKP_TOT=MO_MD_TND_PKP_TOT+0.0,MO_TND_SHRT_TOT=MO_TND_SHRT_TOT+:transactionSalesTotal,QU_MD_TND_BGN_TOT=QU_MD_TND_BGN_TOT+0,QU_MD_TND_LN_TOT=QU_MD_TND_LN_TOT+0,QU_MD_TND_TOT=QU_MD_TND_TOT+1,QU_MD_TND_OVR_TOT=QU_MD_TND_OVR_TOT+0,QU_MD_TND_SHRT_TOT=QU_MD_TND_SHRT_TOT+1,QU_MD_TND_PKP_TOT=QU_MD_TND_PKP_TOT+0,QU_MD_TND_RFD_TOT=QU_MD_TND_RFD_TOT+0,MO_RFD_TND_TOT=MO_RFD_TND_TOT+0.0,MO_TND_TOT=MO_TND_TOT+:transactionTenderTotal,MO_OPN_TND=MO_OPN_TND+0.0,MO_CL_TND_TOT=MO_CL_TND_TOT+0.0,QU_CL_TND_MD=QU_CL_TND_MD+0,MO_MD_RCVF_IN_TOT=MO_MD_RCVF_IN_TOT+0.0,MO_MD_RCVF_OUT_TOT=MO_MD_RCVF_OUT_TOT+0.0,QU_MD_RCVF_IN_TOT=QU_MD_RCVF_IN_TOT+0,QU_MD_RCVF_OUT_TOT=QU_MD_RCVF_OUT_TOT+0,CP_TND_RCN_TOT=CP_TND_RCN_TOT+0.0,QU_TND_RCN_TOT=QU_TND_RCN_TOT+0,TS_MDF_RCRD = CURRENT_TIMESTAMP WHERE ID_STR_RT = :storeId AND FY = :year AND TY_PR_PRD = 'DY' AND ID_PR_PRD = :reportingPeriodId AND TY_TND = :tenderTypeCode AND TY_SB_TND = :tenderMediaIssuerId AND LU_CNY_ISSG_CY = 'IN'";

	@Transactional
	@Modifying
	@Query(value = STORE_TENDER_HISTORY, nativeQuery = true)
	void updateStoreTenderHistory(@Param("transactionSalesTotal") double transactionSalesTotal,
			@Param("transactionTenderTotal") double transactionTenderTotal, @Param("storeId") String storeId,
			@Param("tenderTypeCode") String tenderTypeCode, @Param("tenderMediaIssuerId") String tenderMediaIssuerId,
			@Param("year") String year, @Param("reportingPeriodId") int reportingPeriodId);

	static final String SAVE_STORE_TENDER_HISTORY1 ="INSERT INTO LE_HST_STR_TND ( ID_STR_RT, FY, TY_PR_PRD, ID_PR_PRD, TY_TND, TY_SB_TND,"
			+ " LU_CNY_ISSG_CY, ID_CNY_ICD, TS_CRT_RCRD, TS_MDF_RCRD, MO_MD_LON_TND_TOT, MO_TND_OVR_TOT, MO_MD_TND_PKP_TOT, MO_TND_SHRT_TOT,"
			+ " QU_MD_TND_BGN_TOT, QU_MD_TND_LN_TOT, QU_MD_TND_TOT, QU_MD_TND_OVR_TOT, QU_MD_TND_SHRT_TOT, QU_MD_TND_PKP_TOT, QU_MD_TND_RFD_TOT,"
			+ " MO_RFD_TND_TOT, MO_TND_TOT, MO_OPN_TND, MO_CL_TND_TOT, QU_CL_TND_MD, MO_MD_RCVF_IN_TOT, MO_MD_RCVF_OUT_TOT, QU_MD_RCVF_IN_TOT,"
			+ " QU_MD_RCVF_OUT_TOT, CP_TND_RCN_TOT, QU_TND_RCN_TOT) VALUES (:storeId, :year, 'DY', :reportingPeriodId, :tenderTypeCode, "
			+ ":tenderMediaIssuerId, 'IN', :currencyId, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0.0, 0.0, 0.0, :transactionSalesTotal, 0, 0, 1, 0, 1, 0, 0, 0.0,"
			+ " :transactionTenderTotal, 0.0, 0.0, 0, 0.0, 0.0, 0, 0, 0.0, 0)";
	
	@Transactional
	@Modifying
	@Query(value = SAVE_STORE_TENDER_HISTORY1, nativeQuery = true)
	void updateStoreTenderHistory1(
			@Param("storeId") String storeId,
			@Param("year") String year,
			@Param("reportingPeriodId") int reportingPeriodId,
			@Param("tenderTypeCode") String tenderTypeCode,
			@Param("tenderMediaIssuerId") String tenderMediaIssuerId,
			@Param("currencyId") int currencyId,
			@Param("transactionSalesTotal") double transactionSalesTotal,
			@Param("transactionTenderTotal") double transactionTenderTotal
			
			);

	static final String TAX_HISTORY = "UPDATE HST_TX SET QU_TX = QU_TX + 1, MO_TX = MO_TX + :transactionTaxTotal WHERE ID_STR_RT = :storeId AND ID_WS = :registerId AND ID_TL_CRT = :registerId AND DC_DY_BSN = :bizDate ";

	@Transactional
	@Modifying
	@Query(value = TAX_HISTORY, nativeQuery = true)
	void updateTaxHistory(
			@Param("transactionTaxTotal") float transactionTaxTotal,
			@Param("storeId") String storeId, @Param("registerId") String registerId, @Param("bizDate") String bizDate
/*			@Param("taxAuthorityId") int taxAuthorityId, @Param("taxHolidayFlag") char taxHolidayFlag,
			@Param("taxGroupId") int taxGroupId, @Param("taxType") String taxType*/);

	// For RetailPriceModifier
	static final String RETAIL_PRICE_MODIFIER = "INSERT INTO CO_MDFR_RTL_PRC ( ID_STR_RT, ID_WS, AI_TRN, DC_DY_BSN, AI_LN_ITM,"
			+ " AI_MDFR_RT_PRC, TS_CRT_RCRD, TS_MDF_RCRD, ID_RU_MDFR, MO_MDFR_RT_PRC, RC_MDFR_RT_PRC, PE_MDFR_RT_PRC, CD_MTH_PRDV,"
			+ " CD_BAS_PRDV, ID_DSC_EM, FL_DSC_DMG, FL_PCD_DL_ADVN_APLY, FL_DSC_ADVN_PRC, ID_DSC_REF, CO_ID_DSC_REF, CD_TY_PRDV, "
			+ "DP_LDG_STK_MDFR, ID_PRM, ID_PRM_CMP, ID_PRM_CMP_DTL, RTLOG_AMT, VFP_AMT, VENDOR_ID) "
			+ "VALUES (:storeId, :registerId, :transNo, :bizDate, :retailTransactionLineItemSequenceNumber,:retailPriceModifierSequenceNumber, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,"
			+ " :derivationRuleID, :amount,:reasonCode, :percent, :methodCode, :assignmentBasis, '',:damageDiscountFlag,:pCDAdvancedDealAppliedFlag,"
			+ ":priceDerivationRuleDiscountFlag,:discountReferenceID,:discountReferenceTypeCode,:typeCode,:accountingDispositionCode,:promotionID,:promotionComponentID,"
			+ ":promotionComponentDetailID,:RTLOG_AMT,:VFP_AMT,:VENDOR_ID)";

	@Modifying
	@Query(value = RETAIL_PRICE_MODIFIER, nativeQuery = true)
	@Transactional
	void saveRetailPriceModifier(@Param("storeId") String storeId, @Param("registerId") String registerId,
			@Param("transNo") int transNo, @Param("bizDate") String bizDate,
			@Param("retailTransactionLineItemSequenceNumber") short retailTransactionLineItemSequenceNumber,
			@Param("retailPriceModifierSequenceNumber") short retailPriceModifierSequenceNumber,
			@Param("derivationRuleID") int derivationRuleID, @Param("amount") BigDecimal amount,
			@Param("reasonCode") String reasonCode, @Param("percent") BigDecimal percent, @Param("methodCode") int methodCode,
			@Param("assignmentBasis") int assignmentBasis, @Param("damageDiscountFlag") char damageDiscountFlag,
			@Param("pCDAdvancedDealAppliedFlag") char pCDAdvancedDealAppliedFlag,
			@Param("priceDerivationRuleDiscountFlag") char priceDerivationRuleDiscountFlag,
			@Param("discountReferenceID") String discountReferenceID,
			@Param("discountReferenceTypeCode") String discountReferenceTypeCode, @Param("typeCode") int typeCode,
			@Param("accountingDispositionCode") String accountingDispositionCode, @Param("promotionID") int promotionID,
			@Param("promotionComponentID") int promotionComponentID,
			@Param("promotionComponentDetailID") int promotionComponentDetailID,
			@Param("RTLOG_AMT") String RTLOG_AMT, @Param("VFP_AMT") String VFP_AMT,
			@Param("VENDOR_ID") String VENDOR_ID

	);
	
	static final String GET_WORKSTATION_TIMEACTIVITY_HISTORY="select * from le_hst_ws_tmacv where id_str_rt=:storeId and dc_ws=:bizDate and in_prd_tm_hr=:timePeriodIntervalPerHourCount";
	 
	@Query(value=GET_WORKSTATION_TIMEACTIVITY_HISTORY,nativeQuery = true)
	String getWorkstationTimeActitvityHistory(
			@Param("storeId") String storeId,
			@Param("bizDate") String bizDate,
			@Param("timePeriodIntervalPerHourCount") String timePeriodIntervalPerHourCount
			);
	
	static final String GET_TILL_TENDER_HISTORY="select * from LE_HST_TL_TND where id_str_rt=:storeId and ty_tnd=:tenderTypeCode AND id_rpsty_tnd=:registerId and ts_tm_srt=TO_TIMESTAMP(:tillOpenTimeStamp, 'YYYY-MM-DD HH24:MI:SS.FF') ";

	@Query(value=GET_TILL_TENDER_HISTORY,nativeQuery = true)
	String getTillTenderHistory(
			@Param("storeId") String storeId,
			@Param("tenderTypeCode") String tenderTypeCode,
			@Param("registerId") String registerId,
			@Param("tillOpenTimeStamp") String tillOpenTimeStamp
			);
	
	
	static final String GET_WORKSTATION_TENDER_HISTORY="select * from LE_HST_WS_TND WHERE ID_STR_RT = :storeId AND ID_WS = :registerId AND FY = :year AND TY_PR_PRD = 'DY' AND ID_PR_PRD = :reportingPeriodId  AND TY_SB_TND = :tenderMediaIssuerId AND LU_CNY_ISSG_CY = 'IN'";	
	
	@Query(value=GET_WORKSTATION_TENDER_HISTORY, nativeQuery = true)
	String getWorkStaionTenderHistory(
			@Param("storeId") String storeId,
			@Param("registerId") String registerId,
			@Param("year") String year,
			@Param("reportingPeriodId") int reportingPeriodId,
			@Param("tenderMediaIssuerId") String tenderMediaIssuerId
			);
	
	
	static final String GET_STORE_TENDER_HISTORY="select * from le_hst_str_tnd WHERE ID_STR_RT = :storeId AND FY = :year AND TY_PR_PRD = 'DY' AND ID_PR_PRD = :reportingPeriodId AND TY_TND = :tenderTypeCode AND TY_SB_TND = :tenderMediaIssuerId AND LU_CNY_ISSG_CY = 'IN'";
	
	@Query(value=GET_STORE_TENDER_HISTORY, nativeQuery = true)
	String getStoreTenderHistory(
			@Param("storeId") String storeId,
			@Param("year") String year,
			@Param("reportingPeriodId") int reportingPeriodId,
			@Param("tenderTypeCode") String tenderTypeCode,
			@Param("tenderMediaIssuerId") String tenderMediaIssuerId
			);
	
	
	static final String GET_POS_DEPARTMENT_HISTORY="select * from LE_HST_PS_DPT where ID_DPT_POS=:posDepartmentId and fy=:year and TY_PR_PRD='DY' and ID_PR_PRD=:reportingPeriodId";
	
	@Query(value=GET_POS_DEPARTMENT_HISTORY, nativeQuery = true)
	String getPosDepartmentHistory(
			@Param("posDepartmentId") String posDepartmentId,
			@Param("year") String year,
			@Param("reportingPeriodId") int reportingPeriodId
			
			);
	
	
	
	
	static final String SAVE_POS_DEPARTMENT_HISTORY="insert into LE_HST_PS_DPT(ID_DPT_POS,FY,TY_PR_PRD,ID_PR_PRD)values(:posDepartmentId,:year,'DY',:reportingPeriodId)";

	
	@Transactional
	@Query(value=SAVE_POS_DEPARTMENT_HISTORY, nativeQuery = true)
	@Modifying
	void insertPosDepartmentHistory(	
		@Param("posDepartmentId") String posDepartmentId,
		@Param("year") String year,
		@Param("reportingPeriodId") int reportingPeriodId
	);
	
	
	static final String INSERT_STORE_HISTORY="insert into LE_HST_STR(ID_STR_RT,FY,TY_PR_PRD,ID_PR_PRD)values(:storeId,:year,'DY',:reportingPeriodId)";
	
	
	@Modifying
	@Transactional
	@Query(value=INSERT_STORE_HISTORY,nativeQuery = true)
	void insertStoreHistory(
			@Param("storeId") String storeId,
			@Param("year") String year,
			@Param("reportingPeriodId") int reportingPeriodId
			
			);
	
	
	static final String GET_STORE_HISTORY="select * from le_hst_str where ID_STR_RT=:storeId and fy=:year and ID_PR_PRD=:reportingPeriodId";
	@Query(value=GET_STORE_HISTORY,nativeQuery = true)
	String getStoreHistory(
			@Param("storeId") String storeId,
			@Param("year") String year,
			@Param("reportingPeriodId") int reportingPeriodId
			
			);
	
	static final String GET_TAX_HISTORY="select * from  HST_TX WHERE ID_STR_RT = :storeId AND ID_WS = :registerId AND ID_TL_CRT = :registerId AND DC_DY_BSN = :bizDate";
	@Query(value=GET_TAX_HISTORY, nativeQuery = true)
	String getTaxHistory(
			@Param("storeId") String storeId,
			@Param("registerId") String registerId,
			@Param("bizDate") String bizDate
//			@Param("taxAuthorityId") int taxAuthorityId,
//			@Param("taxHolidayFlag") char taxHolidayFlag,
//			@Param("taxGroupId") int taxGroupId,
//			@Param("taxType") String taxType
			);
	
	
	static final String INSERT_TAX_HISTORY="insert into HST_TX(ID_STR_RT,ID_WS,ID_TL_CRT,DC_DY_BSN,ID_ATHY_TX,FLG_TX_HDY,ID_GP_TX,TY_TX) values (:storeId,:registerId,:registerId,:bizDate,:taxAuthorityId,:taxHolidayFlag,:taxGroupId,0)";
	@Modifying
	@Transactional
	@Query(value=INSERT_TAX_HISTORY, nativeQuery = true)
	void insertTaxHistory(
			@Param("storeId") String storeId,
			@Param("registerId") String registerId,
			@Param("bizDate") String bizDate,
			@Param("taxAuthorityId") int taxAuthorityId,
			@Param("taxHolidayFlag") char taxHolidayFlag,
			@Param("taxGroupId") int taxGroupId
//			@Param("taxType") String taxType
			);
	
	
	//Repository method to get the status of transaction AI_TRN
	static final String GET_TRANSACTION_STATUS="SELECT AI_TRN FROM TR_TRN WHERE AI_TRN=:transNo AND ID_WS=:registerId AND DC_DY_BSN=:bizDate";
	
	@Query(value=GET_TRANSACTION_STATUS, nativeQuery = true)
	Integer getTransactionStatus(
			
			@Param("transNo") int transNo,
			@Param("registerId") String registerId,
			@Param("bizDate") String bizDate);
	        
	
	

}
