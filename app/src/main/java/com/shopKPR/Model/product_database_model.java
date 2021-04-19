package com.shopKPR.Model;

public class product_database_model {
    long product_id;
    String product_quantity;
    String product_price;

    public product_database_model() {
    }

    public product_database_model(long product_id, String product_quantity, String product_price) {
        this.product_id = product_id;
        this.product_quantity = product_quantity;
        this.product_price = product_price;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }
}
