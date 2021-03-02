package com.owoShopKeeperPanel.registerRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ShopPendingRequest implements Serializable {
    private Double latitude;
    private Double longitude;
    private String shopAddress;
    private String shopImageUri;
    private String shopKeeperNidFrontUri;
    private String shopName;
    private String shopOwnerName;
    private String shopOwnerMobile;
    private String shopServiceMobile;
    private String tradeLicenseUrl;
    private List<Long> categoryPermissionsId;

    public ShopPendingRequest() {
    }

    public ShopPendingRequest(Double latitude, Double longitude, String shopAddress,
                              String shopImageUri, String shopKeeperNidFrontUri, String shopName,
                              String shopOwnerName, String shopOwnerMobile, String shopServiceMobile, String tradeLicenseUrl) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.shopAddress = shopAddress;
        this.shopImageUri = shopImageUri;
        this.shopKeeperNidFrontUri = shopKeeperNidFrontUri;
        this.shopName = shopName;
        this.shopOwnerName = shopOwnerName;
        this.shopOwnerMobile = shopOwnerMobile;
        this.shopServiceMobile = shopServiceMobile;
        this.tradeLicenseUrl = tradeLicenseUrl;
    }

    public List<Long> duplicateProtection(List<Long> permissionListUnFiltered)
    {
        return new ArrayList<>(new HashSet<>(permissionListUnFiltered));
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
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

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public String getShopOwnerMobile() {
        return shopOwnerMobile;
    }

    public void setShopOwnerMobile(String shopOwnerMobile) {
        this.shopOwnerMobile = shopOwnerMobile;
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

    public List<Long> getCategoryPermissions() {
        return categoryPermissionsId;
    }

    public void setCategoryPermissions(List<Long> categoryPermissionsId) {
        this.categoryPermissionsId = categoryPermissionsId;
    }
}

