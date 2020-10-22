package com.owoshopkeeperpanel.Model;

import com.owoshopkeeperpanel.Model.Ordered_products;

import java.io.Serializable;
import java.util.List;

public class Ordered_products_model implements Serializable{
    private String additional_comments;
    private String delivery_method;
    private double coupon_discount;
    private String date, delivery_address, name, order_number;
    private List<Ordered_products> product_ids;
    private String receiver_phone, shop_number, state, time, totalAmount;

    public Ordered_products_model() {
    }

    public Ordered_products_model(String additional_comments, String delivery_method, double coupon_discount, String date, String delivery_address, String name, String order_number, List<Ordered_products> product_ids, String receiver_phone, String shop_number, String state, String time, String totalAmount) {
        this.additional_comments = additional_comments;
        this.delivery_method = delivery_method;
        this.coupon_discount = coupon_discount;
        this.date = date;
        this.delivery_address = delivery_address;
        this.name = name;
        this.order_number = order_number;
        this.product_ids = product_ids;
        this.receiver_phone = receiver_phone;
        this.shop_number = shop_number;
        this.state = state;
        this.time = time;
        this.totalAmount = totalAmount;
    }

    public String getAdditional_comments() {
        return additional_comments;
    }

    public void setAdditional_comments(String additional_comments) {
        this.additional_comments = additional_comments;
    }

    public String getDelivery_method() {
        return delivery_method;
    }

    public void setDelivery_method(String delivery_method) {
        this.delivery_method = delivery_method;
    }

    public double getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(double coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public List<Ordered_products> getProduct_ids() {
        return product_ids;
    }

    public void setProduct_ids(List<Ordered_products> product_ids) {
        this.product_ids = product_ids;
    }

    public String getReceiver_phone() {
        return receiver_phone;
    }

    public void setReceiver_phone(String receiver_phone) {
        this.receiver_phone = receiver_phone;
    }

    public String getShop_number() {
        return shop_number;
    }

    public void setShop_number(String shop_number) {
        this.shop_number = shop_number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
