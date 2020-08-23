package com.owoshopkeeperpanel.Model;

public class PendingShops {

    private String Image_of_the_shop, Name_of_the_shop, Shop_address, Shop_service_mobile_number,
        Shop_owner_name, Shop_owner_mobile_number, Shop_owner_nid_picture, Image_of_the_shop_trade_license;

    private Boolean Availability;

    public PendingShops() {
    }

    public PendingShops(String image_of_the_shop, String name_of_the_shop, String shop_address, String shop_service_mobile_number, String shop_owner_name, String shop_owner_mobile_number, String shop_owner_nid_picture, String image_of_the_shop_trade_license, Boolean availability) {
        Image_of_the_shop = image_of_the_shop;
        Name_of_the_shop = name_of_the_shop;
        Shop_address = shop_address;
        Shop_service_mobile_number = shop_service_mobile_number;
        Shop_owner_name = shop_owner_name;
        Shop_owner_mobile_number = shop_owner_mobile_number;
        Shop_owner_nid_picture = shop_owner_nid_picture;
        Image_of_the_shop_trade_license = image_of_the_shop_trade_license;
        Availability = availability;
    }

    public String getImage_of_the_shop() {
        return Image_of_the_shop;
    }

    public void setImage_of_the_shop(String image_of_the_shop) {
        Image_of_the_shop = image_of_the_shop;
    }

    public String getName_of_the_shop() {
        return Name_of_the_shop;
    }

    public void setName_of_the_shop(String name_of_the_shop) {
        Name_of_the_shop = name_of_the_shop;
    }

    public String getShop_address() {
        return Shop_address;
    }

    public void setShop_address(String shop_address) {
        Shop_address = shop_address;
    }

    public String getShop_service_mobile_number() {
        return Shop_service_mobile_number;
    }

    public void setShop_service_mobile_number(String shop_service_mobile_number) {
        Shop_service_mobile_number = shop_service_mobile_number;
    }

    public String getShop_owner_name() {
        return Shop_owner_name;
    }

    public void setShop_owner_name(String shop_owner_name) {
        Shop_owner_name = shop_owner_name;
    }

    public String getShop_owner_mobile_number() {
        return Shop_owner_mobile_number;
    }

    public void setShop_owner_mobile_number(String shop_owner_mobile_number) {
        Shop_owner_mobile_number = shop_owner_mobile_number;
    }

    public String getShop_owner_nid_picture() {
        return Shop_owner_nid_picture;
    }

    public void setShop_owner_nid_picture(String shop_owner_nid_picture) {
        Shop_owner_nid_picture = shop_owner_nid_picture;
    }

    public String getImage_of_the_shop_trade_license() {
        return Image_of_the_shop_trade_license;
    }

    public void setImage_of_the_shop_trade_license(String image_of_the_shop_trade_license) {
        Image_of_the_shop_trade_license = image_of_the_shop_trade_license;
    }

    public Boolean getAvailability() {
        return Availability;
    }

    public void setAvailability(Boolean availability) {
        Availability = availability;
    }
}
