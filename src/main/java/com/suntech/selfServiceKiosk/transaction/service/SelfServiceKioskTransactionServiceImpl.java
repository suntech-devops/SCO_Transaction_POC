package com.suntech.selfServiceKiosk.transaction.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.suntech.selfServiceKiosk.transaction.Response.SelfServiceKioskTransactionResponse;
import com.suntech.selfServiceKiosk.transaction.constants.SelfServiceKioskErrorCodeConstants;
import com.suntech.selfServiceKiosk.transaction.constants.SelfServiceKioskErrorMessageConstants;
import com.suntech.selfServiceKiosk.transaction.constants.SelfServiceKioskResponseMessageConstants;
import com.suntech.selfServiceKiosk.transaction.karnival.BillPostingServiceImpl;
import com.suntech.selfServiceKiosk.transaction.karnival.KarnivalResponse;
import com.suntech.selfServiceKiosk.transaction.repsitory.SelfServiceKioskTransactionRepository;
import com.suntech.selfServiceKiosk.transaction.utils.SelfServiceKioskDateTimeConstants;
import com.suntech.selfServiceKiosk.transaction.utils.SelfServiceKioskSaleRetailPriceModifier;
import com.suntech.selfServiceKiosk.transaction.utils.SelfServiceKioskSaleReturnLineItem;
import com.suntech.selfServiceKiosk.transaction.utils.SelfServiceKioskSaleReturnTaxLineItem;
import com.suntech.selfServiceKiosk.transaction.utils.SelfServiceKioskTaxLineItmBrkpRequest;
import com.suntech.selfServiceKiosk.transaction.utils.TenderLineItemReturnParameters;
import com.suntech.selfServiceKiosk.transaction.utils.TransactionReturnParameters;

@Service
public class SelfServiceKioskTransactionServiceImpl implements SelfServiceKioskTransactionService {

	private static final Logger logger = LoggerFactory.getLogger(SelfServiceKioskTransactionServiceImpl.class);

	@Autowired
	private SelfServiceKioskTransactionRepository transactionRepository;

	@Autowired
	private BillPostingServiceImpl billPostingServiceImpl;

	String taxGroupId = null;

	String tranaction = null;

	SelfServiceKioskDateTimeConstants dateTimeConstants = new SelfServiceKioskDateTimeConstants();
	SelfServiceKioskTransactionResponse transactionResponse = new SelfServiceKioskTransactionResponse();

	@Value("${transaction.queue.path:C:/SelfServiceKioskTransactionService/TransactionQueue/}")
	String xmlPath;

	@Override
	public SelfServiceKioskTransactionResponse getTransactionStatus(
			TransactionReturnParameters transactionReturnParameters) {
		TenderLineItemReturnParameters tenderLineItemReturnParameter = new TenderLineItemReturnParameters();
		SelfServiceKioskSaleReturnLineItem saleReturnLineItem = new SelfServiceKioskSaleReturnLineItem();
		SelfServiceKioskSaleReturnTaxLineItem returnTaxLineItem = new SelfServiceKioskSaleReturnTaxLineItem();
		SelfServiceKioskSaleReturnLineItem returnLineItem = new SelfServiceKioskSaleReturnLineItem();

		try {
			Integer transactionStatus = transactionRepository.getTransactionStatus(
					transactionReturnParameters.getTransNo(), transactionReturnParameters.getRegisterId(),
					transactionReturnParameters.getBizDate());
			if (transactionStatus == null) {

				transactionXmlCreation(transactionReturnParameters);

				// Parent method to call multiple query
				saveSaleTransaction(transactionReturnParameters, tenderLineItemReturnParameter, saleReturnLineItem,
						returnTaxLineItem, returnLineItem);
			} else {
				transactionResponse.setMessage("Please Use Diffrent Transation Number");
				transactionResponse.setErrorCode(null);
				transactionResponse.setStatus("Repeated Transaction number");

			}
		} catch (Exception e) {
			logger.error("exception occured in get Transaction status ", e);
		}

		return transactionResponse;
	}

	private void transactionXmlCreation(TransactionReturnParameters transactionReturnParameters) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(TransactionReturnParameters.class);

			Marshaller marshaller = jaxbContext.createMarshaller();

			// create xml
			marshaller.marshal(transactionReturnParameters,
					new FileWriter(xmlPath.concat(transactionReturnParameters.getInvoiceNumber().concat(".xml"))));

			File file = new File(xmlPath.concat(transactionReturnParameters.getInvoiceNumber().concat(".xml")));
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// write xml
			transactionReturnParameters = (TransactionReturnParameters) jaxbUnmarshaller.unmarshal(file);
			logger.info(transactionReturnParameters.getBizDate());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public SelfServiceKioskTransactionResponse saveSaleTransaction(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameter,
			SelfServiceKioskSaleReturnLineItem saleReturnLineItem,
			SelfServiceKioskSaleReturnTaxLineItem returnTaxLineItem, SelfServiceKioskSaleReturnLineItem returnLineItem)
			throws IOException, InterruptedException {
		try {
						if (transactionReturnParameters.getTransStatus() == 2) {
				
				saveTransaction(transactionReturnParameters);

				updateWorkstation(transactionReturnParameters);

				saveRetailTransaction(transactionReturnParameters);

				for (SelfServiceKioskSaleReturnLineItem lineItems : transactionReturnParameters.getReturnLineItems()) {

					saveRetailTransactionLineItem(transactionReturnParameters,
							lineItems.getRelateItemTransactionLineItemSeqNumb(), lineItems.getTypeCode());
					saveSaleReturnLineItem(transactionReturnParameters, lineItems);

					saveSaleReturnTaxLineItem(transactionReturnParameters, lineItems.getReturnTaxLineItem(), lineItems);

					if (lineItems.getReturnExtendedAmount() != lineItems.getSaleReturnLineItem()) {
						// Method to call ItemPromotion
						savePromotionsWithItem(transactionReturnParameters, lineItems.getRetailPriceModifier(),
								lineItems);
					}

					for (SelfServiceKioskTaxLineItmBrkpRequest taxeBreakItems : lineItems.getTaxLineItmBrkp()) {
						saveTaxLineItemBrkp(transactionReturnParameters, taxeBreakItems, lineItems);
					}
				}
//
				saveSalesTax(transactionReturnParameters);
				saveRetailTransactionLineItem(transactionReturnParameters,
						(short) transactionReturnParameters.getReturnLineItems().size(), "TX");
//
				short listSize = (short) (transactionReturnParameters.getReturnLineItems().size() + 1);

				for (TenderLineItemReturnParameters tenderLineItemReturnParameters : transactionReturnParameters
						.getTenderLineItems()) {
					saveRetailTransactionLineItem(transactionReturnParameters, listSize, "TN");

					saveTenderLineItem(transactionReturnParameters, tenderLineItemReturnParameters, listSize);

					saveCreditDebitCardTender(transactionReturnParameters, tenderLineItemReturnParameters, listSize);

					getTillTenderHistory(transactionReturnParameters, tenderLineItemReturnParameters);
					getWorkStaionTenderHistory(transactionReturnParameters, tenderLineItemReturnParameters);
					getStoreTenderHistory(transactionReturnParameters, tenderLineItemReturnParameters);
					listSize = (short) (listSize + 1);
				}

				for (SelfServiceKioskSaleReturnLineItem lineItems : transactionReturnParameters.getReturnLineItems()) {
					savePosDepartmentHistory1(transactionReturnParameters, lineItems);
				}

				// call WorkStationTimeActivityHistory method to check data is there or not
				getWorkstationTimeActitvityHistory(transactionReturnParameters);

				updateSalesAssociateProductivity(transactionReturnParameters);
				getTillHistory(transactionReturnParameters, tenderLineItemReturnParameter);

				getWorkstationHistory(transactionReturnParameters, saleReturnLineItem);

				getStoreHistory(transactionReturnParameters);
				SelfServiceKioskTransactionResponse taxHistory=getTaxHistory( transactionReturnParameters, returnTaxLineItem,returnLineItem);
				
				String getTaxHistory=   transactionRepository.getTaxHistory(transactionReturnParameters.getStoreId(),
						transactionReturnParameters.getRegisterId(), transactionReturnParameters.getBizDate());
				
				if(getTaxHistory==null)
				{
					insertTaxHistory( transactionReturnParameters,returnTaxLineItem,returnLineItem);
					
					updateTaxHistory(transactionReturnParameters, returnTaxLineItem, returnLineItem);
				}
				else
				{
					updateTaxHistory(transactionReturnParameters, returnTaxLineItem, returnLineItem);
				}
				
				/* inserting record for loyalty points */
				insertLoyaltyRecord(transactionReturnParameters, returnLineItem);
				
				transactionResponse
						.setMessage(SelfServiceKioskResponseMessageConstants.SUCCESSFUL_SAVE_SALE_TRANSACTION_001);
				transactionResponse.setErrorCode(null);
				transactionResponse
						.setStatus(SelfServiceKioskResponseMessageConstants.STATUS_SAVE_SALE_TRANSACTION_002);
				logger.info(SelfServiceKioskResponseMessageConstants.STATUS_SAVE_SALE_TRANSACTION_002);

			} else if (transactionReturnParameters.getTransStatus() == 3) {

				saveTransaction(transactionReturnParameters);
				updateWorkstation(transactionReturnParameters);

				transactionResponse.setMessage("Save Canceled Transaction");
				transactionResponse.setErrorCode(null);
				transactionResponse
						.setStatus(SelfServiceKioskResponseMessageConstants.FAILED_SAVE_SALE_TRANSACTION_003);
			}

		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_002_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setStatus(SelfServiceKioskResponseMessageConstants.FAILED_SAVE_SALE_TRANSACTION_003);

			logger.error("Exception occured in saveSaleTransaction :", ex);

		} finally {

			KarnivalResponse resp = billPostingServiceImpl.saveDetails(transactionReturnParameters);
			logger.info("karnival api executed message={} otp={}", resp.getMessage());

		}

		transactionReturnParameters = null;
		
		return transactionResponse;
	}

	@Override
	public SelfServiceKioskTransactionResponse saveTransaction(TransactionReturnParameters req) {
		try {

			String contactInfo = "";
			if (req.getCustomer() != null) {
				contactInfo = req.getCustomer().getMobileNumber();
				logger.info("customer data-{}" , req.getCustomer());

			}
			transactionRepository.saveTransaction(req.getStoreId(), req.getRegisterId(), req.getTransNo(),
					req.getBizDate(), req.getOperatorId(), req.getTillOpenTimeStamp(), req.getTransStart(),
					req.getTransEnd(), req.getTransactionTypeCode(), req.getTrainingFlag(), req.getEmployeeId(),
					contactInfo, (short) 1, req.getTransStatus(), req.getTenderRepositoryId(),
					req.getTransactionPostProcessingStatusCode(), req.getTransactionReentryFlag(),
					req.getSalesAssociateModifiedFlag(), req.getSubInvFl());
			logger.info(SelfServiceKioskResponseMessageConstants.SUCCESSFUL_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setMessage(SelfServiceKioskResponseMessageConstants.SUCCESSFUL_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setErrorCode(null);
			transactionResponse.setStatus("Success");

		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_002_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setStatus(SelfServiceKioskResponseMessageConstants.FAILED_SAVE_SALE_TRANSACTION_003);

			logger.error("Exception occured in saveTransaction :", ex);
		}
		return transactionResponse;
	}

	@Override
	public SelfServiceKioskTransactionResponse updateWorkstation(TransactionReturnParameters req) {
		try {

			transactionRepository.updateAsWs(req.getTransNo(),req.getTillOpenTimeStamp() ,req.getStoreId(), req.getRegisterId());

			logger.info("Workstation updated secessfully");
			transactionResponse.setMessage(SelfServiceKioskResponseMessageConstants.SUCCESSFUL_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setErrorCode(null);
			transactionResponse.setStatus("Success");

		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_002_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setStatus(SelfServiceKioskResponseMessageConstants.FAILED_SAVE_SALE_TRANSACTION_003);

			logger.error("Exception occured in updateWorkstation :", ex);
		}
		return transactionResponse;
	}

	@Override
	public SelfServiceKioskTransactionResponse saveRetailTransaction(TransactionReturnParameters req) {
		try {
			transactionRepository.saveTrRtl(req.getStoreId(), req.getRegisterId(), req.getTransNo(), req.getBizDate(),
					req.getSuspendedTransactionReasonCode(), req.getTransactionSalesTotal(),
					req.getTransactionDiscountTotal(), req.getTransactionTaxTotal(),
					req.getTransactionInclusiveTaxTotal(), req.getTransactionNetTotal(),
					req.getTransactionTenderTotal(), req.getPersonalMaskId(), req.getPersonalIdReqType(),
					req.getPersonalIdState(), req.getPersonalIdCountry(), req.getSendPackageCount(), req.getCD_CO_ISO(),
					req.getRoundedOffTotalAmount(), req.getSendCustomerType(), req.getEmployeeId(),
					req.getGiftRegistryId());

			logger.info("Retail Transaction Saved Sucessfully");
			transactionResponse.setMessage(SelfServiceKioskResponseMessageConstants.SUCCESSFUL_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setErrorCode(null);
			transactionResponse.setStatus("Success");

		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_002_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setStatus(SelfServiceKioskResponseMessageConstants.FAILED_SAVE_SALE_TRANSACTION_003);

			logger.error("Exception occured in saveRetailTransaction :", ex);
		}
		return transactionResponse;
	}

	@Override
	public SelfServiceKioskTransactionResponse saveRetailTransactionLineItem(TransactionReturnParameters req,
			short originalRetailTransactionLineItemSequenceNo, String typeCode) {
		try {

			transactionRepository.saveTR_LTM_RTL_TRN(req.getStoreId(), req.getRegisterId(), req.getTransNo(),
					req.getBizDate(), originalRetailTransactionLineItemSequenceNo, typeCode);

			logger.info("Retail Transaction Line Item Saved Sucessfully");

			transactionResponse.setMessage(SelfServiceKioskResponseMessageConstants.SUCCESSFUL_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setErrorCode(null);
			transactionResponse.setStatus("Success");

		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_002_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setStatus(SelfServiceKioskResponseMessageConstants.FAILED_SAVE_SALE_TRANSACTION_003);

			logger.error("Exception occured in saveRetailTransactionLineItem :", ex);
		}
		return transactionResponse;
	}

	@Override
	public SelfServiceKioskTransactionResponse saveSaleReturnLineItem(TransactionReturnParameters req,
			SelfServiceKioskSaleReturnLineItem returnLineItem) {
		try {

			transactionRepository.saveTR_LTM_SLS_RTN(req.getStoreId(), req.getRegisterId(), req.getTransNo(),
					req.getBizDate(), returnLineItem.getRelateItemTransactionLineItemSeqNumb(),
					returnLineItem.getRegistryId(), returnLineItem.getItemId(), returnLineItem.getPosItemId(),
					returnLineItem.getSerialNo(), returnLineItem.getTaxGroupId(), returnLineItem.getQuantity(),
					returnLineItem.getReturnExtendedAmount(), returnLineItem.getSaleReturnLineItem(),
					returnLineItem.getSaleReturnLineItemVatAmount(),
					returnLineItem.getSaleReturnLineItemInclusiveTaxAmount(), returnLineItem.getSendLabelCount(),
					returnLineItem.getMerchandiseReturnFlag(), returnLineItem.getReturnReasonCode(),
					returnLineItem.getRcItmCndRtnMr(), returnLineItem.getOriginalSequenceNo(),
					returnLineItem.getOriginalBizDayDate(),
					returnLineItem.getOriginalRetailTransactionLineItemSequenceNo(),
					returnLineItem.getOriginalReatailStoreId(), returnLineItem.getPosDepartmentId(),
					returnLineItem.getSendFlag(), returnLineItem.getFlShpChg(), returnLineItem.getGiftReceiptFlag(),
					returnLineItem.getWebOrderNo(), returnLineItem.getAiOrdLnItm(),
					returnLineItem.getItemIdEntryMethodCode(), returnLineItem.getSizeCode(),
					returnLineItem.getReturnRelatedItemFlag(), returnLineItem.getRelateItemTransactionLineItemSeqNumb(),
					returnLineItem.getRemoveRelatedItemFlag(), returnLineItem.getSalesAssociateModifiedFlag(),
					returnLineItem.getPermanentRetailPriceAtTimeOfSale(), returnLineItem.getDeItmShrtRcpt(),
					returnLineItem.getDeItmLcl(), returnLineItem.getRestockingFeeFlag(),
					returnLineItem.getFlVldSezItm(), returnLineItem.getFlVldSrzItmExt(),
					returnLineItem.getFlSrzCrtExt(), returnLineItem.getMerchandiseHierarchyLevelCode(),
					returnLineItem.getItemSizeRequiredFlag(), returnLineItem.getStockItemSaleUnitOfMeasureCode(),
					req.getTransactionTypeCode(), returnLineItem.getProhibitReturnFlag(),
					returnLineItem.getEmployeeDiscountAllowedFlag(), returnLineItem.getFlTx(),
					returnLineItem.getDiscountFlag(), returnLineItem.getDamageDiscountFlag(),
					returnLineItem.getMerchandisehierarchyGroupId(), returnLineItem.getManufacturerUPC(),
					returnLineItem.getFlClrc(), returnLineItem.getIdNonRtvdOrgRcpt(),
					returnLineItem.getSalesAgeRestrictionIdentifier(), returnLineItem.getPriceEnteryRequiredFlag(),
					returnLineItem.getMoLnDs(), returnLineItem.getMoMrpLnItmRtn(), returnLineItem.getItemCollectionId(),
					returnLineItem.getItemKitHeaderReferenceId());

			logger.info("Sale Retail Line Item Saved Sucessfully");

			transactionResponse.setMessage(SelfServiceKioskResponseMessageConstants.SUCCESSFUL_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setErrorCode(null);
			transactionResponse.setStatus("Success");

		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_002_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setStatus(SelfServiceKioskResponseMessageConstants.FAILED_SAVE_SALE_TRANSACTION_003);

			logger.error("Exception occured in saveSaleReturnLineItem :", ex);
		}

		return transactionResponse;
	}

	@Override
	public SelfServiceKioskTransactionResponse saveSaleReturnTaxLineItem(TransactionReturnParameters req,
			SelfServiceKioskSaleReturnTaxLineItem returnTaxLineItem,
			SelfServiceKioskSaleReturnLineItem returnLineItem) {
		try {

			transactionRepository.saveTRLTMSLSRTNTX(req.getStoreId(), req.getRegisterId(), req.getTransNo(),
					req.getBizDate(), returnLineItem.getRelateItemTransactionLineItemSeqNumb(),

					returnTaxLineItem.getTaxAuthorityId(), returnLineItem.getTaxGroupId(),
					returnTaxLineItem.getTaxType(), returnTaxLineItem.getTaxHolidayFlag(),
					returnTaxLineItem.getTaxableAmount(), returnTaxLineItem.getTaxAmountPerAuthority(),
					returnTaxLineItem.getTaxAmountTotal(), returnTaxLineItem.getShippingRecordsTaxAmountTotal(),
					returnTaxLineItem.getTaxAuthorityName(), returnTaxLineItem.getTaxRuleName(),
					returnTaxLineItem.getTaxPercent(), returnTaxLineItem.getTaxMode(),
					returnTaxLineItem.getInclusiveTaxFlag());
			logger.info("Sale Return TaxLine Item Saved Sucessfully");
			transactionResponse.setMessage(SelfServiceKioskResponseMessageConstants.SUCCESSFUL_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setErrorCode(null);
			transactionResponse.setStatus("Success");

		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_002_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setStatus(SelfServiceKioskResponseMessageConstants.FAILED_SAVE_SALE_TRANSACTION_003);

			logger.error("Exception occured in saveSaleReturnTaxLineItem :", ex);
		}

		return transactionResponse;
	}

	// For Item Promotions
	public SelfServiceKioskTransactionResponse savePromotionsWithItem(TransactionReturnParameters req,
			SelfServiceKioskSaleRetailPriceModifier retailPriceModifier,
			SelfServiceKioskSaleReturnLineItem returnLineItem) {
		try {
			transactionRepository.saveRetailPriceModifier(req.getStoreId(), req.getRegisterId(), req.getTransNo(),
					req.getBizDate(), returnLineItem.getRelateItemTransactionLineItemSeqNumb(),

					retailPriceModifier.getRetailPriceModifierSequenceNumber(),
					retailPriceModifier.getPricederivationRuleID(), retailPriceModifier.getAmount(),
					retailPriceModifier.getReasonCode(), retailPriceModifier.getPercent(),
					retailPriceModifier.getMethodCode(), retailPriceModifier.getAssignmentBasis(),
					retailPriceModifier.getDamageDiscountFlag(), retailPriceModifier.getPCDAdvancedDealAppliedFlag(),
					retailPriceModifier.getPriceDerivationRuleDiscountFlag(),

					retailPriceModifier.getDiscountReferenceID(), retailPriceModifier.getDiscountReferenceTypeCode(),
					retailPriceModifier.getTypeCode(), retailPriceModifier.getAccountingDispositionCode(),
					retailPriceModifier.getPromotionID(), retailPriceModifier.getPromotionComponentID(),
					retailPriceModifier.getPromotionComponentDetailID(), retailPriceModifier.getRTLOG_AMT(),
					retailPriceModifier.getVFP_AMT(), retailPriceModifier.getVENDOR_ID());

			transactionResponse.setMessage(SelfServiceKioskResponseMessageConstants.SUCCESSFUL_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setErrorCode(null);
			transactionResponse.setStatus("Success");

		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setStatus(SelfServiceKioskResponseMessageConstants.FAILED_SAVE_SALE_TRANSACTION_003);

			logger.error("Exception occured in saveSaleReturnTaxLineItem :", ex);
		}

		return transactionResponse;
	}

	@Override
	public SelfServiceKioskTransactionResponse saveTenderLineItem(TransactionReturnParameters req,
			TenderLineItemReturnParameters tenderLineItemReturnParameters, short lineNumber) {
		try {
			transactionRepository.saveTenderLineItem(req.getStoreId(), req.getRegisterId(), req.getTransNo(),
					lineNumber, req.getBizDate(), tenderLineItemReturnParameters.getTenderTypeCode(),
					tenderLineItemReturnParameters.getAmount(), tenderLineItemReturnParameters.getCurrencyId());

			transactionResponse.setMessage(SelfServiceKioskResponseMessageConstants.SUCCESSFUL_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setErrorCode(null);
			transactionResponse.setStatus("Success");

		} catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_002_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setStatus(SelfServiceKioskResponseMessageConstants.FAILED_SAVE_SALE_TRANSACTION_003);

			logger.error("Exception occured in saveTenderLineItem :", e);
		}

		return transactionResponse;
	}

	@Override
	public SelfServiceKioskTransactionResponse saveCreditDebitCardTender(TransactionReturnParameters req,
			TenderLineItemReturnParameters tenderLineItemReturnParameters, short lineNumber) {
		try {

			transactionRepository.saveCreditDebitCardTender(req.getStoreId(), req.getRegisterId(), req.getTransNo(),
					lineNumber, req.getBizDate(), tenderLineItemReturnParameters.getTenderTypeCode(),
					tenderLineItemReturnParameters.getCardNumber(),
					tenderLineItemReturnParameters.getCardNumberSwipedOrKeyedCode(),
					tenderLineItemReturnParameters.getAuthorizationMethodCode(),
					tenderLineItemReturnParameters.getTenderMediaIssuerId(),
					tenderLineItemReturnParameters.getCardType(), tenderLineItemReturnParameters.getBankId(),
					tenderLineItemReturnParameters.getCreditDebitCardAdjudicationCode(),
					tenderLineItemReturnParameters.getAdjudicationCode()

			);

			transactionResponse.setMessage(SelfServiceKioskResponseMessageConstants.SUCCESSFUL_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setErrorCode(null);
			transactionResponse.setStatus("Success");

		} catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_002_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setStatus(SelfServiceKioskResponseMessageConstants.FAILED_SAVE_SALE_TRANSACTION_003);

			logger.error("Exception occured in saveCreditDebitCardTender :", e);
		}
		return transactionResponse;
	}

	// SaveTaxLineItemBrkp method
	@Override
	public SelfServiceKioskTransactionResponse saveTaxLineItemBrkp(TransactionReturnParameters req,
			SelfServiceKioskTaxLineItmBrkpRequest breakUplineItems, SelfServiceKioskSaleReturnLineItem lineItems) {
		try {

			transactionRepository.saveTaxLineItmBreakup(req.getStoreId(), req.getRegisterId(), req.getTransNo(),
					req.getBizDate(), lineItems.getLineNumber(), lineItems.getItemId(), lineItems.getPosItemId(),
					lineItems.getQuantity(), breakUplineItems.getTX_BRKUP_TX_CD(),
					breakUplineItems.getTX_BRKUP_TX_CD_DSCR(), breakUplineItems.getTX_BRKUP_TX_RT(),
					breakUplineItems.getTX_BRKUP_TXBL_AMT(), breakUplineItems.getTX_BRKUP_TX_AMT(),
					breakUplineItems.getTX_FCT(), breakUplineItems.getTXBL_FCT());

			transactionResponse.setMessage(SelfServiceKioskResponseMessageConstants.SUCCESSFUL_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setErrorCode(null);
			transactionResponse.setStatus("Success");

		} catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setStatus(SelfServiceKioskResponseMessageConstants.FAILED_SAVE_SALE_TRANSACTION_003);

			logger.error("Exception occured in saveTaxLineItemBrkp :", e);

		}

		return transactionResponse;
	}

	// Save TaxLineItem Method started
	@Override
	public SelfServiceKioskTransactionResponse saveSalesTax(TransactionReturnParameters req) {

		try {
			logger.info("saveSalesTax method started");
			logger.info("Print Request {}", req);
			transactionRepository.saveSalesTax(req.getStoreId(), req.getRegisterId(), req.getTransNo(),
					req.getBizDate(), (short) req.getReturnLineItems().size(), req.getTaxLineItem().getTaxAmt(),
					req.getTaxLineItem().getInclTaxAmount(), req.getTaxLineItem().getTaxTypeCode(),
					req.getTaxLineItem().getTaxPercent(), req.getTaxLineItem().getTaxOverridePercent(),
					req.getTaxLineItem().getTaxOverrideAmount(), req.getTaxLineItem().getTaxReasonCode());

			transactionResponse.setMessage(SelfServiceKioskResponseMessageConstants.SUCCESSFUL_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setErrorCode(null);
			transactionResponse.setStatus("Success");

		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setStatus(SelfServiceKioskResponseMessageConstants.FAILED_SAVE_SALE_TRANSACTION_003);

			logger.error("Exception occured in saveSalesTax :", ex);
		}
		return transactionResponse;

	}

	@Override
	public SelfServiceKioskTransactionResponse updateSalesAssociateProductivity(TransactionReturnParameters req) {

		try {

			transactionRepository.updateSalesAssociateProductivity(req.getTransactionNetTotal(), req.getStoreId(),
					req.getRegisterId(), req.getEmployeeId(), req.getBizDate());

			transactionResponse.setMessage(SelfServiceKioskResponseMessageConstants.SUCCESSFUL_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setErrorCode(null);
			transactionResponse.setStatus("Success");

		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setStatus(SelfServiceKioskResponseMessageConstants.FAILED_SAVE_SALE_TRANSACTION_003);

			logger.error("Exception occured in updateSalesAssociateProductivity :", ex);

		}

		return transactionResponse;
	}

	@Override
	public SelfServiceKioskTransactionResponse savePosDepartmetHistory(
			TransactionReturnParameters transactionReturnParameters,
			SelfServiceKioskSaleReturnLineItem returnLineItem) {
		try {

			int reportingPeriodId = dateTimeConstants.getNumberOfDayInYear(transactionReturnParameters);
			String year = dateTimeConstants.getFiscalYear(transactionReturnParameters);

			transactionRepository.savePosDepartmetHistory(returnLineItem.getSaleReturnLineItem(),
					returnLineItem.getReturnExtendedAmount(), returnLineItem.getSaleReturnLineItemInclusiveTaxAmount(),
					returnLineItem.getPosDepartmentId(), year, reportingPeriodId);

		}

		catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in savePosDepartmetHistory :", ex);
		}

		return transactionResponse;
	}

	@Override
	public SelfServiceKioskTransactionResponse updateWorkstationTimeActivityHistory(
			TransactionReturnParameters transactionReturnParameters) {

		try {

			String timePeriodIntervalPerHourCount = dateTimeConstants
					.timePeriodIntervalPerCount(transactionReturnParameters);

			int lineItemSequence = dateTimeConstants.returnLineItemSequence(transactionReturnParameters);

			transactionRepository.updateWorkstationTimeActivityHistory(
					transactionReturnParameters.getTransactionNetTotal(), lineItemSequence,
					transactionReturnParameters.getTransactionTaxTotal(), transactionReturnParameters.getStoreId(),
					transactionReturnParameters.getBizDate(), timePeriodIntervalPerHourCount);
			transactionResponse.setMessage("WorkStation time Activity history updated");
		} catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);

			logger.error("Exception occured in updateWorkstationTimeActivityHistory  ", e);
		}
		return transactionResponse;

	}

	@Override
	public SelfServiceKioskTransactionResponse insertWorkstationTimeActivityHistory(
			TransactionReturnParameters transactionReturnParameters) {
		try {

			String timePeriodIntervalPerHourCount = dateTimeConstants
					.timePeriodIntervalPerCount(transactionReturnParameters);

			int lineItemSequence = dateTimeConstants.returnLineItemSequence(transactionReturnParameters);

			transactionRepository.insertWorkstationTimeActivityHistory(transactionReturnParameters.getStoreId(),
					transactionReturnParameters.getRegisterId(), transactionReturnParameters.getBizDate(),
					transactionReturnParameters.getTransactionNetTotal(), lineItemSequence,
					transactionReturnParameters.getTransactionTaxTotal(), timePeriodIntervalPerHourCount);
			transactionResponse.setMessage("Data inserted Successfully in register time activity history1");
		} catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);

			logger.error("Exception occured in insert register time activity history1  ", e);
		}

		return transactionResponse;
	}

	public SelfServiceKioskTransactionResponse getTillHistory(TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters) {
		String getData = transactionRepository.getTillHistory(transactionReturnParameters.getRegisterId(),
				transactionReturnParameters.getStoreId(), transactionReturnParameters.getTillOpenTimeStamp());

		if (getData == null) {
			insertTillHistory(transactionReturnParameters);
			updateTillHistory(transactionReturnParameters, tenderLineItemReturnParameters);

		} else {
			updateTillHistory(transactionReturnParameters, tenderLineItemReturnParameters);
		}
		return transactionResponse;

	}

	public SelfServiceKioskTransactionResponse insertTillHistory(
			TransactionReturnParameters transactionReturnParameters) {
		try {
			transactionRepository.insertTillHistory(transactionReturnParameters.getRegisterId(),
					transactionReturnParameters.getStoreId(), transactionReturnParameters.getTillOpenTimeStamp());
		} catch (Exception e) {

			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in insert till history  ", e);
		}
		return transactionResponse;
	}

	@Override
	public SelfServiceKioskTransactionResponse updateTillHistory(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters) {

		int lineItemSequence = dateTimeConstants.returnLineItemSequence(transactionReturnParameters);
		try {
			transactionRepository.updateTilHistory(transactionReturnParameters.getTransNo(),
					transactionReturnParameters.getBizDate(), transactionReturnParameters.getRegisterId(),
					tenderLineItemReturnParameters.getCurrencyId(), lineItemSequence,
					transactionReturnParameters.getTransactionNetTotal(),
					transactionReturnParameters.getTransactionTaxTotal(),
					transactionReturnParameters.getTransactionDiscountTotal(),
					transactionReturnParameters.getTransactionSalesTotal(),
					transactionReturnParameters.getTransactionInclusiveTaxTotal(),
					transactionReturnParameters.getStoreId(), transactionReturnParameters.getTillOpenTimeStamp());
			transactionResponse.setMessage("Data updated  Successfully in Till history");
		} catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);

			logger.error("Exception occured in updating till history  ", e);
		}

		return transactionResponse;
	}

	public SelfServiceKioskTransactionResponse insertWorkstationHistory(
			TransactionReturnParameters transactionReturnParameters) throws ParseException {
		int reportingPeriodId = dateTimeConstants.getNumberOfDayInYear(transactionReturnParameters);
		String year = dateTimeConstants.getFiscalYear(transactionReturnParameters);

		try {
			transactionRepository.insertWorkstationHistory(transactionReturnParameters.getStoreId(),
					transactionReturnParameters.getRegisterId(), year, reportingPeriodId);
		} catch (Exception e) {

			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in insert workstation history  ", e);
		}
		return transactionResponse;
	}

	public SelfServiceKioskTransactionResponse getWorkstationHistory(
			TransactionReturnParameters transactionReturnParameters,
			SelfServiceKioskSaleReturnLineItem saleReturnLineItem) throws ParseException {
		int reportingPeriodId = dateTimeConstants.getNumberOfDayInYear(transactionReturnParameters);
		String year = dateTimeConstants.getFiscalYear(transactionReturnParameters);

		String getData = transactionRepository.getWorkstationHistory(transactionReturnParameters.getStoreId(),
				transactionReturnParameters.getRegisterId(), year, reportingPeriodId);

		if (getData == null) {
			insertWorkstationHistory(transactionReturnParameters);
			updateWorkstationHistory(transactionReturnParameters, saleReturnLineItem);

		} else {
			updateWorkstationHistory(transactionReturnParameters, saleReturnLineItem);
		}
		return transactionResponse;

	}

	@Override
	public SelfServiceKioskTransactionResponse updateWorkstationHistory(
			TransactionReturnParameters transactionReturnParameters,
			SelfServiceKioskSaleReturnLineItem saleReturnLineItem) {
		try {

			int reportingPeriodId = dateTimeConstants.getNumberOfDayInYear(transactionReturnParameters);
			String year = dateTimeConstants.getFiscalYear(transactionReturnParameters);
			int lineItemSequence = dateTimeConstants.returnLineItemSequence(transactionReturnParameters);

			transactionRepository.updateWorkstationHistory(lineItemSequence,
					transactionReturnParameters.getTransactionNetTotal(),
					transactionReturnParameters.getTransactionTaxTotal(),
					transactionReturnParameters.getTransactionDiscountTotal(),
					transactionReturnParameters.getTransactionInclusiveTaxTotal(),
					transactionReturnParameters.getStoreId(), transactionReturnParameters.getRegisterId(), year,
					reportingPeriodId);

			transactionResponse.setMessage("Data updated  Successfully in Workstation History");
		} catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in update workstation history  ", e);
		}
		return transactionResponse;
	}

	@Override
	public SelfServiceKioskTransactionResponse updateStoreHistory(
			TransactionReturnParameters transactionReturnParameters) {
		try {
			int reportingPeriodId = dateTimeConstants.getNumberOfDayInYear(transactionReturnParameters);
			String year = dateTimeConstants.getFiscalYear(transactionReturnParameters);
			int lineItemSequence = dateTimeConstants.returnLineItemSequence(transactionReturnParameters);

			transactionRepository.updateStoreHistory(lineItemSequence,
					transactionReturnParameters.getTransactionNetTotal(),
					transactionReturnParameters.getTransactionDiscountTotal(),
					transactionReturnParameters.getTransactionTaxTotal(),
					transactionReturnParameters.getTransactionInclusiveTaxTotal(),
					transactionReturnParameters.getStoreId(), year, reportingPeriodId);
		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_002_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in updateStoreHistory :", ex);

		}
		return transactionResponse;
	}

	@Override
	public SelfServiceKioskTransactionResponse updateTillTenderHistory(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters) {
		try {
			transactionRepository.updateTillTenderHistory(transactionReturnParameters.getTransactionTenderTotal(),
					transactionReturnParameters.getRegisterId(), transactionReturnParameters.getStoreId(),
					tenderLineItemReturnParameters.getTenderTypeCode(),
					transactionReturnParameters.getTillOpenTimeStamp(),
					tenderLineItemReturnParameters.getTenderMediaIssuerId());
		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in updateTillTenderHistory :", ex);
		}
		return transactionResponse;
	}

	@Override
	public SelfServiceKioskTransactionResponse saveTillTenderHistory(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters) {
		try {
			transactionRepository.saveTillTenderHistory(transactionReturnParameters.getRegisterId(),
					transactionReturnParameters.getStoreId(), transactionReturnParameters.getTillOpenTimeStamp(),
					tenderLineItemReturnParameters.getTenderTypeCode(),
					tenderLineItemReturnParameters.getTenderMediaIssuerId(),
					tenderLineItemReturnParameters.getCurrencyId(),
					transactionReturnParameters.getTransactionTenderTotal());
		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in saveTillTenderHistory :", ex);
		}
		return transactionResponse;
	}

	public SelfServiceKioskTransactionResponse updateWorkstationTenderHistory(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters) {
		try {

			int reportingPeriodId = dateTimeConstants.getNumberOfDayInYear(transactionReturnParameters);
			String year = dateTimeConstants.getFiscalYear(transactionReturnParameters);

			transactionRepository.updateWorkstationTenderHistory(
					transactionReturnParameters.getTransactionTenderTotal(), transactionReturnParameters.getStoreId(),
					transactionReturnParameters.getRegisterId(), tenderLineItemReturnParameters.getTenderTypeCode(),
					year, reportingPeriodId, tenderLineItemReturnParameters.getTenderMediaIssuerId());
		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in updateWorkstationTenderHistory :", ex);
		}
		return transactionResponse;
	}

	@Override
	public SelfServiceKioskTransactionResponse updateStoreTenderHistory(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters) {
		try {

			int reportingPeriodId = dateTimeConstants.getNumberOfDayInYear(transactionReturnParameters);
			String year = dateTimeConstants.getFiscalYear(transactionReturnParameters);

			transactionRepository.updateStoreTenderHistory(transactionReturnParameters.getTransactionSalesTotal(),
					transactionReturnParameters.getTransactionTenderTotal(), transactionReturnParameters.getStoreId(),
					tenderLineItemReturnParameters.getTenderTypeCode(),
					tenderLineItemReturnParameters.getTenderMediaIssuerId(), year, reportingPeriodId);
		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in updateStoreTenderHistory :", ex);

		}

		return transactionResponse;
	}

	public SelfServiceKioskTransactionResponse saveStoreTenderHistory(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters) throws ParseException {
		int reportingPeriodId = dateTimeConstants.getNumberOfDayInYear(transactionReturnParameters);
		String year = dateTimeConstants.getFiscalYear(transactionReturnParameters);

		try {

			transactionRepository.updateStoreTenderHistory1(transactionReturnParameters.getStoreId(), year,
					reportingPeriodId, tenderLineItemReturnParameters.getTenderTypeCode(),
					tenderLineItemReturnParameters.getTenderMediaIssuerId(),
					tenderLineItemReturnParameters.getCurrencyId(),
					transactionReturnParameters.getTransactionSalesTotal(),
					transactionReturnParameters.getTransactionTenderTotal());
		} catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);

			logger.error("Exception occured in update Store Tender HIstory" , e);
		}

		return transactionResponse;
	}

	public SelfServiceKioskTransactionResponse getWorkstationTimeActitvityHistory(
			TransactionReturnParameters transactionReturnParameters) {

		String timePeriodIntervalPerHourCount = dateTimeConstants
				.timePeriodIntervalPerCount(transactionReturnParameters);

		try {
			String getData = transactionRepository.getWorkstationTimeActitvityHistory(
					transactionReturnParameters.getStoreId(), transactionReturnParameters.getBizDate(),
					timePeriodIntervalPerHourCount);
			if (getData == null) {
				insertWorkstationTimeActivityHistory(transactionReturnParameters);

			} else {
				updateWorkstationTimeActivityHistory(transactionReturnParameters);

			}
		} catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exceptionoccured in get worktation time activity history " , e);
		}
		return transactionResponse;
	}

	public SelfServiceKioskTransactionResponse updateWorkstationTenderHistory1(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters) throws ParseException {
		int reportingPeriodId = dateTimeConstants.getNumberOfDayInYear(transactionReturnParameters);
		String year = dateTimeConstants.getFiscalYear(transactionReturnParameters);
		try {
			transactionRepository.updateWorkstationTenderHistory1(transactionReturnParameters.getStoreId(),
					transactionReturnParameters.getRegisterId(), year, reportingPeriodId,
					tenderLineItemReturnParameters.getTenderTypeCode(),
					tenderLineItemReturnParameters.getTenderMediaIssuerId(),
					tenderLineItemReturnParameters.getCurrencyId(),
					transactionReturnParameters.getTransactionTenderTotal());
		} catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in updateStoreTenderHistory :", e);
		}

		return transactionResponse;
	}

	public SelfServiceKioskTransactionResponse getWorkStaionTenderHistory(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters) throws ParseException {

		int reportingPeriodId = dateTimeConstants.getNumberOfDayInYear(transactionReturnParameters);
		String year = dateTimeConstants.getFiscalYear(transactionReturnParameters);

		String getData = transactionRepository.getWorkStaionTenderHistory(transactionReturnParameters.getStoreId(),
				transactionReturnParameters.getRegisterId(), year, reportingPeriodId,
				tenderLineItemReturnParameters.getTenderMediaIssuerId());

		if (getData == null) {
			updateWorkstationTenderHistory1(transactionReturnParameters, tenderLineItemReturnParameters);

		} else {
			updateWorkstationTenderHistory(transactionReturnParameters, tenderLineItemReturnParameters);

		}

		return transactionResponse;

	}

	public SelfServiceKioskTransactionResponse getStoreTenderHistory(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters) throws ParseException {
		int reportingPeriodId = dateTimeConstants.getNumberOfDayInYear(transactionReturnParameters);
		String year = dateTimeConstants.getFiscalYear(transactionReturnParameters);

		try {
			String getData = transactionRepository.getStoreTenderHistory(transactionReturnParameters.getStoreId(), year,
					reportingPeriodId, tenderLineItemReturnParameters.getTenderTypeCode(),
					tenderLineItemReturnParameters.getTenderMediaIssuerId());

			if (getData == null) {
				saveStoreTenderHistory(transactionReturnParameters, tenderLineItemReturnParameters);
			} else {
				updateStoreTenderHistory(transactionReturnParameters, tenderLineItemReturnParameters);
			}

		} catch (Exception e) {
			logger.error("Exception occured in get Store tnder history " , e);
		}

		return transactionResponse;
	}

	public SelfServiceKioskTransactionResponse getTillTenderHistory(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters) {
		try {
			String getData = transactionRepository.getTillTenderHistory(transactionReturnParameters.getStoreId(),
					tenderLineItemReturnParameters.getTenderTypeCode(), transactionReturnParameters.getRegisterId(),
					transactionReturnParameters.getTillOpenTimeStamp());
			if (getData == null) {
				saveTillTenderHistory(transactionReturnParameters, tenderLineItemReturnParameters);
			} else {
				updateTillTenderHistory(transactionReturnParameters, tenderLineItemReturnParameters);
			}

		} catch (Exception e) {
			logger.error("Exception occured in get Till tender history " , e);
		}
		return transactionResponse;
	}

	public SelfServiceKioskTransactionResponse insertPosDepartmentHistory(
			TransactionReturnParameters transactionReturnParameters, SelfServiceKioskSaleReturnLineItem returnLineItem)
			throws ParseException {
		int reportingPeriodId = dateTimeConstants.getNumberOfDayInYear(transactionReturnParameters);
		String year = dateTimeConstants.getFiscalYear(transactionReturnParameters);
		try {
			transactionRepository.insertPosDepartmentHistory(returnLineItem.getPosDepartmentId(), year,
					reportingPeriodId);
		} catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in insert pos Deprtment history " ,e);
		}
		return transactionResponse;
	}

	public SelfServiceKioskTransactionResponse savePosDepartmentHistory1(
			TransactionReturnParameters transactionReturnParameters, SelfServiceKioskSaleReturnLineItem returnLineItem)
			throws ParseException {

		int reportingPeriodId = dateTimeConstants.getNumberOfDayInYear(transactionReturnParameters);
		String year = dateTimeConstants.getFiscalYear(transactionReturnParameters);

		try {

			String getData = transactionRepository.getPosDepartmentHistory(returnLineItem.getPosDepartmentId(), year,
					reportingPeriodId);

			if (getData == null) {
				insertPosDepartmentHistory(transactionReturnParameters, returnLineItem);
				savePosDepartmetHistory(transactionReturnParameters, returnLineItem);
			} else {
				savePosDepartmetHistory(transactionReturnParameters, returnLineItem);

			}
		}

		catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in get pos department history " , e);
		}

		return transactionResponse;
	}

	public SelfServiceKioskTransactionResponse insertStoreHistory(
			TransactionReturnParameters transactionReturnParameters) throws ParseException {

		int reportingPeriodId = dateTimeConstants.getNumberOfDayInYear(transactionReturnParameters);
		String year = dateTimeConstants.getFiscalYear(transactionReturnParameters);

		transactionRepository.insertStoreHistory(transactionReturnParameters.getStoreId(), year, reportingPeriodId);

		return transactionResponse;

	}

	public SelfServiceKioskTransactionResponse getStoreHistory(TransactionReturnParameters transactionReturnParameters)
			throws ParseException {

		int reportingPeriodId = dateTimeConstants.getNumberOfDayInYear(transactionReturnParameters);
		String year = dateTimeConstants.getFiscalYear(transactionReturnParameters);

		String data = transactionRepository.getStoreHistory(transactionReturnParameters.getStoreId(), year,
				reportingPeriodId);
		if (data == null) {
			insertStoreHistory(transactionReturnParameters);
			updateStoreHistory(transactionReturnParameters);
		} else {
			updateStoreHistory(transactionReturnParameters);
		}

		return transactionResponse;
	}

	public SelfServiceKioskTransactionResponse updateTaxHistory(TransactionReturnParameters transactionReturnParameters,
			SelfServiceKioskSaleReturnTaxLineItem returnTaxLineItem,
			SelfServiceKioskSaleReturnLineItem returnLineItem) {
		try {

			transactionRepository.updateTaxHistory(transactionReturnParameters.getTransactionTaxTotal(),
					transactionReturnParameters.getStoreId(), transactionReturnParameters.getRegisterId(),
					transactionReturnParameters
							.getBizDate()/*
											 * , returnTaxLineItem.getTaxAuthorityId(),
											 * returnTaxLineItem.getTaxHolidayFlag(), returnLineItem.getTaxGroupId(),
											 * returnTaxLineItem.getTaxType()
											 */);
		} catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in udate tax History " ,e);
		}

		return transactionResponse;
	}
	
	
	
	
	
	public SelfServiceKioskTransactionResponse insertLoyaltyRecord(TransactionReturnParameters transactionReturnParameters,
			SelfServiceKioskSaleReturnLineItem returnLineItem) {
		try {
				transactionRepository.insertLoyaltyRecord(transactionReturnParameters.getStoreId(), transactionReturnParameters.getRegisterId(),transactionReturnParameters.getBizDate(), transactionReturnParameters.getTransNo(), transactionReturnParameters.getReturnLineItems().size(), transactionReturnParameters.getCustomer().getMobileNumber(),transactionReturnParameters.getPts_redeemed());
				System.out.println(transactionReturnParameters.getReturnLineItems().size());

			
		} catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in insert loyaltyPoints " ,e);
		}

		return transactionResponse;
	}
	
	public SelfServiceKioskTransactionResponse getTaxHistory(TransactionReturnParameters transactionReturnParameters,
			SelfServiceKioskSaleReturnTaxLineItem returnTaxLineItem,
			SelfServiceKioskSaleReturnLineItem returnLineItem) {
		try {
			transactionRepository.getTaxHistory(transactionReturnParameters.getStoreId(),
					transactionReturnParameters.getRegisterId(), transactionReturnParameters.getBizDate()
					/*returnTaxLineItem.getTaxAuthorityId(), returnTaxLineItem.getTaxHolidayFlag(),
					returnLineItem.getTaxGroupId(), returnTaxLineItem.getTaxType()*/);

		} catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in get tax History " , e);
		}
		return transactionResponse;
	}

	public SelfServiceKioskTransactionResponse insertTaxHistory(TransactionReturnParameters transactionReturnParameters,
			SelfServiceKioskSaleReturnTaxLineItem returnTaxLineItem,
			SelfServiceKioskSaleReturnLineItem returnLineItem) {
		try {
			transactionRepository.insertTaxHistory(transactionReturnParameters.getStoreId(),
					transactionReturnParameters.getRegisterId(), transactionReturnParameters.getBizDate(),
					returnTaxLineItem.getTaxAuthorityId(), returnTaxLineItem.getTaxHolidayFlag(),
					returnLineItem.getTaxGroupId());

		} catch (Exception e) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_001_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			logger.error("Exception occured in insert tax History " , e);
		}

		return transactionResponse;
	}

	// Method to call karnival api
	public void getKarnivalResponse(TransactionReturnParameters transactionReturnParameters) {
		String url = "http://103.86.176.93:1406/api/v1/selfServiceKiosk/karnival/postBill";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		String response = null;
		String xml = null;

		try {
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			// headers.

			HttpEntity<TransactionReturnParameters> entity = new HttpEntity<>(transactionReturnParameters, headers);
			response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();			
			logger.info("Body {}" , response);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public SelfServiceKioskTransactionResponse saveTransactionByXml(String invoiceNo) {

		TenderLineItemReturnParameters tenderLineItemReturnParameter = new TenderLineItemReturnParameters();
		SelfServiceKioskSaleReturnLineItem saleReturnLineItem = new SelfServiceKioskSaleReturnLineItem();
		SelfServiceKioskSaleReturnTaxLineItem returnTaxLineItem = new SelfServiceKioskSaleReturnTaxLineItem();
		SelfServiceKioskSaleReturnLineItem returnLineItem = new SelfServiceKioskSaleReturnLineItem();
		try {
			TransactionReturnParameters transactionReturnParameters = new TransactionReturnParameters();
			transactionReturnParameters.setInvoiceNumber(invoiceNo);
			System.out.println("after execute" + transactionReturnParameters.hashCode());
			JAXBContext jaxbContext = JAXBContext.newInstance(TransactionReturnParameters.class);

			File file = new File(xmlPath.concat(transactionReturnParameters.getInvoiceNumber().concat(".xml")));
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// write xml
			transactionReturnParameters = (TransactionReturnParameters) jaxbUnmarshaller.unmarshal(file);
			System.out.println(transactionReturnParameters.getBizDate());
			// Parent method to call multiple query
			Integer transactionStatus = transactionRepository.getTransactionStatus(
					transactionReturnParameters.getTransNo(), transactionReturnParameters.getRegisterId(),
					transactionReturnParameters.getBizDate());
			System.out.println("before execute" + transactionReturnParameters.hashCode());
			if (transactionStatus == null) {
				saveSaleTransaction(transactionReturnParameters, tenderLineItemReturnParameter, saleReturnLineItem,
						returnTaxLineItem, returnLineItem);
			} else {
				transactionResponse.setMessage("Please Use Diffrent Transation Number");
				transactionResponse.setErrorCode(null);
				transactionResponse.setStatus("Repeated Transaction number");
			}
		} catch (Exception ex) {
			transactionResponse.setMessage(SelfServiceKioskErrorMessageConstants.ERR_SAVE_SALE_TRANSACTION_002_MSG);
			transactionResponse.setErrorCode(SelfServiceKioskErrorCodeConstants.ERR_SAVE_SALE_TRANSACTION_001);
			transactionResponse.setStatus(SelfServiceKioskResponseMessageConstants.FAILED_SAVE_SALE_TRANSACTION_003);

			logger.error("Exception occured in saveSaleTransaction :", ex);
		}
		return transactionResponse;
	}
}
