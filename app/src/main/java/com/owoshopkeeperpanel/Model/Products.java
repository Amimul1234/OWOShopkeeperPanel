package com.owoshopkeeperpanel.Model;

import java.io.Serializable;

public class Products implements Serializable {

    private int product_id;
    private String product_image, product_name, product_category, product_price, product_discount,
            product_quantity, product_description, product_date, product_time;

    public Products() {
    }

    public Products(int product_id, String product_image, String product_name, String product_category, String product_price, String product_discount, String product_quantity, String product_description, String product_date, String product_time) {
        this.product_id = product_id;
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_category = product_category;
        this.product_price = product_price;
        this.product_discount = product_discount;
        this.product_quantity = product_quantity;
        this.product_description = product_description;
        this.product_date = product_date;
        this.product_time = product_time;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
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

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_discount() {
        return product_discount;
    }

    public void setProduct_discount(String product_discount) {
        this.product_discount = product_discount;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_date() {
        return product_date;
    }

    public void setProduct_date(String product_date) {
        this.product_date = product_date;
    }

    public String getProduct_time() {
        return product_time;
    }

    public void setProduct_time(String product_time) {
        this.product_time = product_time;
    }
}
