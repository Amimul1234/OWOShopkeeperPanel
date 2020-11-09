package com.owoshopkeeperpanel.adapters;

import java.io.Serializable;

public class Ordered_products implements Serializable {
    private int product_id;
    private int product_quantity;
    private String product_name;
    private String product_image;
    private Double product_price;

    public Ordered_products() {
    }

    public Ordered_products(int product_id, int product_quantity, String product_name, String product_image, Double product_price) {
        this.product_id = product_id;
        this.product_quantity = product_quantity;
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_price = product_price;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public Double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(Double product_price) {
        this.product_price = product_price;
    }
}