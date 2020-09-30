package com.owoshopkeeperpanel.Model;

import java.io.Serializable;

public class Ordered_products implements Serializable {
    private int product_id;
    private int product_quantity;

    public Ordered_products(int product_id, int product_quantity) {
        this.product_id = product_id;
        this.product_quantity = product_quantity;
    }

    public Ordered_products() {
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
}
