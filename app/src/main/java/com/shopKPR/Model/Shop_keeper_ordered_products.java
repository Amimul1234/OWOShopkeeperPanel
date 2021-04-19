package com.shopKPR.Model;

import java.io.Serializable;

public class Shop_keeper_ordered_products implements Serializable {
    private long product_id;
    private String product_name;
    private String product_category;
    private double product_price;
    private double product_discount;
    private int product_quantity;
    private String product_creation_date;
    private String product_creation_time;
    private String product_sub_category;
    private String product_image;

    public Shop_keeper_ordered_products() {
    }

    public Shop_keeper_ordered_products(int product_id, String product_name, String product_category,
                                        double product_price, double product_discount, int product_quantity, String product_creation_date,
                                        String product_creation_time, String product_sub_category, String product_image) {

        this.product_id = product_id;
        this.product_name = product_name;
        this.product_category = product_category;
        this.product_price = product_price;
        this.product_discount = product_discount;
        this.product_quantity = product_quantity;
        this.product_creation_date = product_creation_date;
        this.product_creation_time = product_creation_time;
        this.product_sub_category = product_sub_category;
        this.product_image = product_image;
    }


    public Shop_keeper_ordered_products(CartListProduct cartListProduct) {
        this.product_id = cartListProduct.getProductId();
        this.product_name = cartListProduct.getProductName();
        this.product_category = String.valueOf(cartListProduct.getProductCategoryId());
        this.product_price = cartListProduct.getProductPrice();
        this.product_discount = cartListProduct.getProductDiscount();
        this.product_quantity = cartListProduct.getProductQuantity();
        this.product_creation_date = cartListProduct.getProductAddingDate();
        this.product_creation_time = cartListProduct.getProductAddingTime();
        this.product_sub_category = String.valueOf(cartListProduct.getProductSubCategory());
        this.product_image = cartListProduct.getProductImage();
    }


    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
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

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public double getProduct_discount() {
        return product_discount;
    }

    public void setProduct_discount(double product_discount) {
        this.product_discount = product_discount;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }


    public String getProduct_creation_date() {
        return product_creation_date;
    }

    public void setProduct_creation_date(String product_creation_date) {
        this.product_creation_date = product_creation_date;
    }

    public String getProduct_creation_time() {
        return product_creation_time;
    }

    public void setProduct_creation_time(String product_creation_time) {
        this.product_creation_time = product_creation_time;
    }

    public String getProduct_sub_category() {
        return product_sub_category;
    }

    public void setProduct_sub_category(String product_sub_category) {
        this.product_sub_category = product_sub_category;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }
}
