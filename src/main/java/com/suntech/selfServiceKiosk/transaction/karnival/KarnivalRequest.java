package com.suntech.selfServiceKiosk.transaction.karnival;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class KarnivalRequest {
	
	private long totalQuantity;
    private Store store;
    private Object[] offers;
    private long totalSaving;
    private CustomerInfo customerInfo;
    private Discount billDiscount;
    private PosInfo posInfo;
    private PaymentInfo paymentInfo;
    private List<Item> items;
    private long subTotal;
    private Object[] otherDiscounts;
    private BillInfo billInfo;
    private Attributes attributes;
    private List<Tax> taxes;

    @JsonProperty("totalQuantity")
    public long getTotalQuantity() { return totalQuantity; }
    @JsonProperty("totalQuantity")
    public void setTotalQuantity(long value) { this.totalQuantity = value; }

    @JsonProperty("store")
    public Store getStore() { return store; }
    @JsonProperty("store")
    public void setStore(Store value) { this.store = value; }

    @JsonProperty("offers")
    public Object[] getOffers() { return offers; }
    @JsonProperty("offers")
    public void setOffers(Object[] value) { this.offers = value; }

    @JsonProperty("totalSaving")
    public long getTotalSaving() { return totalSaving; }
    @JsonProperty("totalSaving")
    public void setTotalSaving(long value) { this.totalSaving = value; }

    @JsonProperty("customerInfo")
    public CustomerInfo getCustomerInfo() { return customerInfo; }
    @JsonProperty("customerInfo")
    public void setCustomerInfo(CustomerInfo value) { this.customerInfo = value; }

    @JsonProperty("billDiscount")
    public Discount getBillDiscount() { return billDiscount; }
    @JsonProperty("billDiscount")
    public void setBillDiscount(Discount value) { this.billDiscount = value; }

    @JsonProperty("posInfo")
    public PosInfo getPosInfo() { return posInfo; }
    @JsonProperty("posInfo")
    public void setPosInfo(PosInfo value) { this.posInfo = value; }

    @JsonProperty("paymentInfo")
    public PaymentInfo getPaymentInfo() { return paymentInfo; }
    @JsonProperty("paymentInfo")
    public void setPaymentInfo(PaymentInfo value) { this.paymentInfo = value; }

    @JsonProperty("items")
    public List<Item> getItems() { return items; }
    @JsonProperty("items")
    public void setItems(List<Item> itemList) { this.items = itemList; }

    @JsonProperty("subTotal")
    public long getSubTotal() { return subTotal; }
    @JsonProperty("subTotal")
    public void setSubTotal(long value) { this.subTotal = value; }

    @JsonProperty("otherDiscounts")
    public Object[] getOtherDiscounts() { return otherDiscounts; }
    @JsonProperty("otherDiscounts")
    public void setOtherDiscounts(Object[] value) { this.otherDiscounts = value; }

    @JsonProperty("billInfo")
    public BillInfo getBillInfo() { return billInfo; }
    @JsonProperty("billInfo")
    public void setBillInfo(BillInfo value) { this.billInfo = value; }

    @JsonProperty("attributes")
    public Attributes getAttributes() { return attributes; }
    @JsonProperty("attributes")
    public void setAttributes(Attributes value) { this.attributes = value; }

    @JsonProperty("taxes")
    public List<Tax> getTaxes() { return taxes; }
    @JsonProperty("taxes")
    public void setTaxes(List<Tax> taxList) { this.taxes = taxList; }
}