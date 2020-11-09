package com.owoshopkeeperpanel.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Shop_keeper_orders implements Serializable {
    private int order_number;
    private String additional_comments;
    private double coupon_discount;
    private String date;
    private String delivery_address;
    private String method;
    private String receiver_phone;
    private String shop_phone;
    private String shipping_state;
    private String time_slot;
    private String order_time;
    private double total_amount;

    private List<Shop_keeper_ordered_products> shop_keeper_ordered_products = new ArrayList<>();

    public Shop_keeper_orders() {
    }

    public Shop_keeper_orders(int order_number, String additional_comments, double coupon_discount,
                              String date, String delivery_address, String method, String receiver_phone,
                              String shop_phone, String shipping_state, String time_slot, String order_time,
                              double total_amount, List<Shop_keeper_ordered_products> shop_keeper_ordered_products) {

        this.order_number = order_number;
        this.additional_comments = additional_comments;
        this.coupon_discount = coupon_discount;
        this.date = date;
        this.delivery_address = delivery_address;
        this.method = method;
        this.receiver_phone = receiver_phone;
        this.shop_phone = shop_phone;
        this.shipping_state = shipping_state;
        this.time_slot = time_slot;
        this.order_time = order_time;
        this.total_amount = total_amount;
        this.shop_keeper_ordered_products = shop_keeper_ordered_products;
    }


    public int getOrder_number() {
        return order_number;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }

    public String getAdditional_comments() {
        return additional_comments;
    }

    public void setAdditional_comments(String additional_comments) {
        this.additional_comments = additional_comments;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getReceiver_phone() {
        return receiver_phone;
    }

    public void setReceiver_phone(String receiver_phone) {
        this.receiver_phone = receiver_phone;
    }

    public String getShop_phone() {
        return shop_phone;
    }

    public void setShop_phone(String shop_phone) {
        this.shop_phone = shop_phone;
    }

    public String getShipping_state() {
        return shipping_state;
    }

    public void setShipping_state(String shipping_state) {
        this.shipping_state = shipping_state;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public List<Shop_keeper_ordered_products> getShop_keeper_ordered_products() {
        return shop_keeper_ordered_products;
    }

    public void setShop_keeper_ordered_products(List<Shop_keeper_ordered_products> shop_keeper_ordered_products) {
        this.shop_keeper_ordered_products = shop_keeper_ordered_products;
    }
}
