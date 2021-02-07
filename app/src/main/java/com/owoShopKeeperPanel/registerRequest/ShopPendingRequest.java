package com.owoShopKeeperPanel.registerRequest;

import java.io.Serializable;
import java.util.List;

public class ShopPendingRequest implements Serializable {
    private Double latitude;
    private Double longitude;
    private String shopAddress;
    private String shopImageUri;
    private String shopKeeperNidFrontUri;
    private String shopName;
    private String shopOwnerMobile;
    private String shopOwnerName;
    private String shopServiceMobile;
    private String tradeLicenseUrl;
    private List<String> categoryPermissions;

    public ShopPendingRequest() {
    }

    public ShopPendingRequest(Double latitude, Double longitude, String shopAddress,
                              String shopImageUri, String shopKeeperNidFrontUri, String shopName,
                              String shopOwnerMobile, String shopOwnerName,
                              String shopServiceMobile, String tradeLicenseUrl,
                              List<String> categoryPermissions) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.shopAddress = shopAddress;
        this.shopImageUri = shopImageUri;
        this.shopKeeperNidFrontUri = shopKeeperNidFrontUri;
        this.shopName = shopName;
        this.shopOwnerMobile = shopOwnerMobile;
        this.shopOwnerName = shopOwnerName;
        this.shopServiceMobile = shopServiceMobile;
        this.tradeLicenseUrl = tradeLicenseUrl;
        this.categoryPermissions = categoryPermissions;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopImageUri() {
        return shopImageUri;
    }

    public void setShopImageUri(String shopImageUri) {
        this.shopImageUri = shopImageUri;
    }

    public String getShopKeeperNidFrontUri() {
        return shopKeeperNidFrontUri;
    }

    public void setShopKeeperNidFrontUri(String shopKeeperNidFrontUri) {
        this.shopKeeperNidFrontUri = shopKeeperNidFrontUri;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopOwnerMobile() {
        return shopOwnerMobile;
    }

    public void setShopOwnerMobile(String shopOwnerMobile) {
        this.shopOwnerMobile = shopOwnerMobile;
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public String getShopServiceMobile() {
        return shopServiceMobile;
    }

    public void setShopServiceMobile(String shopServiceMobile) {
        this.shopServiceMobile = shopServiceMobile;
    }

    public String getTradeLicenseUrl() {
        return tradeLicenseUrl;
    }

    public void setTradeLicenseUrl(String tradeLicenseUrl) {
        this.tradeLicenseUrl = tradeLicenseUrl;
    }

    public List<String> getCategoryPermissions() {
        return categoryPermissions;
    }

    public void setCategoryPermissions(List<String> categoryPermissions) {
        this.categoryPermissions = categoryPermissions;
    }
}

