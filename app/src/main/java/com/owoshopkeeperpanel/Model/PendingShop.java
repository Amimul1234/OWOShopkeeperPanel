package com.owoshopkeeperpanel.Model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PendingShop {

    private String shop_name, shop_address, shop_owner_name, shop_service_mobile, shop_owner_mobile,
            shop_image_uri, shop_keeper_nid_front_uri, trade_license_uri;

    private LatLng latLng;

    private List<String> haveAccess = new ArrayList<>();


    public PendingShop() {
    }

    public PendingShop(String shop_name, String shop_address, String shop_owner_name,
                       String shop_service_mobile, String shop_owner_mobile,
                       String shop_image_uri, String shop_keeper_nid_front_uri, String trade_license_uri,
                       LatLng latLng, List<String> haveAccess) {

        this.shop_name = shop_name;
        this.shop_address = shop_address;
        this.shop_owner_name = shop_owner_name;
        this.shop_service_mobile = shop_service_mobile;
        this.shop_owner_mobile = shop_owner_mobile;
        this.shop_image_uri = shop_image_uri;
        this.shop_keeper_nid_front_uri = shop_keeper_nid_front_uri;
        this.trade_license_uri = trade_license_uri;
        this.latLng = latLng;
        this.haveAccess = haveAccess;
    }

    public List<String> getHaveAccess() {
        return haveAccess;
    }

    public void addAccess(String access)
    {
        haveAccess.add(access);
    }

    public void setHaveAccess(List<String> haveAccess) {
        this.haveAccess = haveAccess;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getShop_owner_name() {
        return shop_owner_name;
    }

    public void setShop_owner_name(String shop_owner_name) {
        this.shop_owner_name = shop_owner_name;
    }

    public String getShop_service_mobile() {
        return shop_service_mobile;
    }

    public void setShop_service_mobile(String shop_service_mobile) {
        this.shop_service_mobile = shop_service_mobile;
    }

    public String getShop_owner_mobile() {
        return shop_owner_mobile;
    }

    public void setShop_owner_mobile(String shop_owner_mobile) {
        this.shop_owner_mobile = shop_owner_mobile;
    }

    public String getShop_image_uri() {
        return shop_image_uri;
    }

    public void setShop_image_uri(String shop_image_uri) {
        this.shop_image_uri = shop_image_uri;
    }

    public String getShop_keeper_nid_front_uri() {
        return shop_keeper_nid_front_uri;
    }

    public void setShop_keeper_nid_front_uri(String shop_keeper_nid_front_uri) {
        this.shop_keeper_nid_front_uri = shop_keeper_nid_front_uri;
    }

    public String getTrade_license_uri() {
        return trade_license_uri;
    }

    public void setTrade_license_uri(String trade_license_uri) {
        this.trade_license_uri = trade_license_uri;
    }
}
