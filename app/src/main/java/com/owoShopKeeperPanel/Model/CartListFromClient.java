package com.owoShopKeeperPanel.Model;

public class CartListFromClient {
    private String mobile_number;
    private Cart_list_product cart_list_product;

    public CartListFromClient(String mobile_number, Cart_list_product cart_list_product) {
        this.mobile_number = mobile_number;
        this.cart_list_product = cart_list_product;
    }

    public CartListFromClient() {
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public Cart_list_product getCart_list_product() {
        return cart_list_product;
    }

    public void setCart_list_product(Cart_list_product cart_list_product) {
        this.cart_list_product = cart_list_product;
    }
}
