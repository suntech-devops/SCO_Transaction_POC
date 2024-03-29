package com.suntech.selfServiceKiosk.transaction.service;

import java.io.IOException;

import com.suntech.selfServiceKiosk.transaction.Response.SelfServiceKioskTransactionResponse;
import com.suntech.selfServiceKiosk.transaction.utils.SelfServiceKioskSaleReturnLineItem;
import com.suntech.selfServiceKiosk.transaction.utils.SelfServiceKioskSaleReturnTaxLineItem;
import com.suntech.selfServiceKiosk.transaction.utils.SelfServiceKioskTaxLineItmBrkpRequest;
import com.suntech.selfServiceKiosk.transaction.utils.TenderLineItemReturnParameters;
import com.suntech.selfServiceKiosk.transaction.utils.TransactionReturnParameters;

public interface SelfServiceKioskTransactionService {

	public SelfServiceKioskTransactionResponse getTransactionStatus(
			TransactionReturnParameters transactionReturnParameters);

	public SelfServiceKioskTransactionResponse saveSaleTransaction(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters,
			SelfServiceKioskSaleReturnLineItem saleReturnLineItem,
			SelfServiceKioskSaleReturnTaxLineItem returnTaxLineItem, SelfServiceKioskSaleReturnLineItem returnLineItem)
			throws IOException, InterruptedException;

	public SelfServiceKioskTransactionResponse saveTransaction(TransactionReturnParameters transactionReturnParameters);

	public SelfServiceKioskTransactionResponse updateWorkstation(
			TransactionReturnParameters transactionReturnParameters);

	public SelfServiceKioskTransactionResponse saveRetailTransaction(
			TransactionReturnParameters transactionReturnParameters);

	public SelfServiceKioskTransactionResponse saveRetailTransactionLineItem(
			TransactionReturnParameters transactionReturnParameters, short originalRetailTransactionLineItemSequenceNo,
			String typeCode);

	public SelfServiceKioskTransactionResponse saveSaleReturnLineItem(
			TransactionReturnParameters transactionReturnParameters, SelfServiceKioskSaleReturnLineItem returnLineItem);

	public SelfServiceKioskTransactionResponse saveSaleReturnTaxLineItem(
			TransactionReturnParameters transactionReturnParameters,
			SelfServiceKioskSaleReturnTaxLineItem returnTaxLineItem, SelfServiceKioskSaleReturnLineItem returnLineItem);

	public SelfServiceKioskTransactionResponse saveTenderLineItem(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters, short lineNumber);

	public SelfServiceKioskTransactionResponse saveCreditDebitCardTender(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters, short lineNumber);

	public SelfServiceKioskTransactionResponse saveTaxLineItemBrkp(
			TransactionReturnParameters transactionReturnParameters,
			SelfServiceKioskTaxLineItmBrkpRequest lineBreakUpItems, SelfServiceKioskSaleReturnLineItem lineItems);

	public SelfServiceKioskTransactionResponse updateSalesAssociateProductivity(
			TransactionReturnParameters transactionReturnParameters);

	public SelfServiceKioskTransactionResponse savePosDepartmetHistory(
			TransactionReturnParameters transactionReturnParameters, SelfServiceKioskSaleReturnLineItem returnLineItem);

	public SelfServiceKioskTransactionResponse updateWorkstationTimeActivityHistory(
			TransactionReturnParameters transactionReturnParameters);

//	public SelfServiceKioskTransactionResponse updateWorkstationTenderHistory(TransactionReturnParameters transactionReturnParameters,TenderLineItemReturnParameters tenderLineItemReturnParameters)

	public SelfServiceKioskTransactionResponse insertWorkstationTimeActivityHistory(
			TransactionReturnParameters transactionReturnParameters);

	public SelfServiceKioskTransactionResponse updateTillHistory(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters);

	public SelfServiceKioskTransactionResponse updateTillTenderHistory(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters);

	public SelfServiceKioskTransactionResponse saveTillTenderHistory(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters);

	public SelfServiceKioskTransactionResponse updateWorkstationHistory(
			TransactionReturnParameters transactionReturnParameters,
			SelfServiceKioskSaleReturnLineItem saleReturnLineItem);

	public SelfServiceKioskTransactionResponse updateStoreHistory(
			TransactionReturnParameters transactionReturnParameters);

	public SelfServiceKioskTransactionResponse updateStoreTenderHistory(
			TransactionReturnParameters transactionReturnParameters,
			TenderLineItemReturnParameters tenderLineItemReturnParameters);

	public SelfServiceKioskTransactionResponse saveSalesTax(TransactionReturnParameters transactionReturnParameters);

	public SelfServiceKioskTransactionResponse saveTransactionByXml(String invoiceNo);

}
