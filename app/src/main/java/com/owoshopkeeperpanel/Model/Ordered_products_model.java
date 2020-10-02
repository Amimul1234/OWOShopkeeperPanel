package com.owoshopkeeperpanel.Model;

import java.io.Serializable;
import java.util.List;

public class Ordered_products_model implements Serializable {
    private String date, delivery_address,
        name,  order_number, receiver_phone, state, time, totalAmount, shop_name;

    private Double coupon_discount;

    private List<Ordered_products> product_ids;

    public Ordered_products_model() {
    }

    public Ordered_products_model(Double coupon_discount, String date, String delivery_address, String name, String order_number, String receiver_phone, String state, String time, String totalAmount, String shop_name, List<Ordered_products> product_ids) {
        this.coupon_discount = coupon_discount;
        this.date = date;
        this.delivery_address = delivery_address;
        this.name = name;
        this.order_number = order_number;
        this.receiver_phone = receiver_phone;
        this.state = state;
        this.time = time;
        this.totalAmount = totalAmount;
        this.shop_name = shop_name;
        this.product_ids = product_ids;
    }

    public Double getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(Double coupon_discount) {
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

    public String getReceiver_phone() {
        return receiver_phone;
    }

    public void setReceiver_phone(String receiver_phone) {
        this.receiver_phone = receiver_phone;
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

    public List<Ordered_products> getProduct_ids() {
        return product_ids;
    }

    public void setProduct_ids(List<Ordered_products> product_ids) {
        this.product_ids = product_ids;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }
}
