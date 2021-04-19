package com.shopKPR.Model;

import java.io.Serializable;

public class add_product_model implements Serializable {

    private long product_id;
    private double product_price;
    private String product_image, product_name,
            product_quantity, product_description;

    public add_product_model() {
    }

    public add_product_model(long product_id, double product_price, String product_image, String product_name, String product_quantity, String product_description) {
        this.product_id = product_id;
        this.product_price = product_price;
        this.product_image = product_image;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_description = product_description;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
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
}
