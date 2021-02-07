package com.owoShopKeeperPanel.Model;

import java.io.Serializable;

public class Cart implements Serializable {

    private int product_id;
    private double product_price;
    private String product_image, product_name, needed_quantity, date, time;

    public Cart() {
    }

    public Cart(int product_id, String product_image, String product_name, double product_price, String needed_quantity, String date, String time, String product_category) {
        this.product_id = product_id;
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_price = product_price;
        this.needed_quantity = needed_quantity;
        this.date = date;
        this.time = time;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getNeeded_quantity() {
        return needed_quantity;
    }

    public void setNeeded_quantity(String needed_quantity) {
        this.needed_quantity = needed_quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
