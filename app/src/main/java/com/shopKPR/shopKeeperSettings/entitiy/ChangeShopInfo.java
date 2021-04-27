package com.shopKPR.shopKeeperSettings.entitiy;

public class ChangeShopInfo {

    private Long changeShopInfoId;
    private String shopOwnerMobileNumber;
    private String newShopImageURL;
    private String newShopName;
    private String newShopAddress;
    private String newShopOwnerName;
    private String newShopServiceMobile;
    private String newShopOwnerNidFront;
    private String newShopTradeLicenseURI;

    public ChangeShopInfo() {
    }

    public ChangeShopInfo(Long changeShopInfoId, String shopOwnerMobileNumber, String newShopImageURL,
                          String newShopName, String newShopAddress, String newShopOwnerName,
                          String newShopServiceMobile, String newShopOwnerNidFront, String newShopTradeLicenseURI)
    {
        this.changeShopInfoId = changeShopInfoId;
        this.shopOwnerMobileNumber = shopOwnerMobileNumber;
        this.newShopImageURL = newShopImageURL;
        this.newShopName = newShopName;
        this.newShopAddress = newShopAddress;
        this.newShopOwnerName = newShopOwnerName;
        this.newShopServiceMobile = newShopServiceMobile;
        this.newShopOwnerNidFront = newShopOwnerNidFront;
        this.newShopTradeLicenseURI = newShopTradeLicenseURI;
    }

    public Long getChangeShopInfoId() {
        return changeShopInfoId;
    }

    public void setChangeShopInfoId(Long changeShopInfoId) {
        this.changeShopInfoId = changeShopInfoId;
    }

    public String getShopOwnerMobileNumber() {
        return shopOwnerMobileNumber;
    }

    public void setShopOwnerMobileNumber(String shopOwnerMobileNumber) {
        this.shopOwnerMobileNumber = shopOwnerMobileNumber;
    }

    public String getNewShopImageURL() {
        return newShopImageURL;
    }

    public void setNewShopImageURL(String newShopImageURL) {
        this.newShopImageURL = newShopImageURL;
    }

    public String getNewShopName() {
        return newShopName;
    }

    public void setNewShopName(String newShopName) {
        this.newShopName = newShopName;
    }

    public String getNewShopAddress() {
        return newShopAddress;
    }

    public void setNewShopAddress(String newShopAddress) {
        this.newShopAddress = newShopAddress;
    }

    public String getNewShopOwnerName() {
        return newShopOwnerName;
    }

    public void setNewShopOwnerName(String newShopOwnerName) {
        this.newShopOwnerName = newShopOwnerName;
    }

    public String getNewShopServiceMobile() {
        return newShopServiceMobile;
    }

    public void setNewShopServiceMobile(String newShopServiceMobile) {
        this.newShopServiceMobile = newShopServiceMobile;
    }

    public String getNewShopOwnerNidFront() {
        return newShopOwnerNidFront;
    }

    public void setNewShopOwnerNidFront(String newShopOwnerNidFront) {
        this.newShopOwnerNidFront = newShopOwnerNidFront;
    }

    public String getNewShopTradeLicenseURI() {
        return newShopTradeLicenseURI;
    }

    public void setNewShopTradeLicenseURI(String newShopTradeLicenseURI) {
        this.newShopTradeLicenseURI = newShopTradeLicenseURI;
    }
}
