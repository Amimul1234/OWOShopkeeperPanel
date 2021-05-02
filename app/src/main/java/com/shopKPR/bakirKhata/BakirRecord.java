package com.shopKPR.bakirKhata;

import java.io.Serializable;

public class BakirRecord implements Serializable
{
    private Long bakirRecordId;
    private String shopMobileNumber;
    private String customerName;
    private String productName;
    private String productPrice;
    private String date;
    private String paymentMethod;

    public BakirRecord() {
    }

    public BakirRecord(Long bakirRecordId, String shopMobileNumber, String customerName,
                       String productName, String productPrice, String date, String paymentMethod) {
        this.bakirRecordId = bakirRecordId;
        this.shopMobileNumber = shopMobileNumber;
        this.customerName = customerName;
        this.productName = productName;
        this.productPrice = productPrice;
        this.date = date;
        this.paymentMethod = paymentMethod;
    }

    public Long getBakirRecordId() {
        return bakirRecordId;
    }

    public void setBakirRecordId(Long bakirRecordId) {
        this.bakirRecordId = bakirRecordId;
    }

    public String getShopMobileNumber() {
        return shopMobileNumber;
    }

    public void setShopMobileNumber(String shopMobileNumber) {
        this.shopMobileNumber = shopMobileNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
