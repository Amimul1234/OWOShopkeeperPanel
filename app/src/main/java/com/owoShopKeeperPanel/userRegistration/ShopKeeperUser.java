package com.owoShopKeeperPanel.userRegistration;

import java.io.Serializable;

public class ShopKeeperUser implements Serializable {
    private Long shopKeeperId;
    private String name;
    private String mobileNumber;
    private String pin;
    private String imageUri;
    private Boolean accountEnabled;

    public ShopKeeperUser() {
    }

    public ShopKeeperUser(Long shopKeeperId, String name, String mobileNumber,
                          String pin, String imageUri, Boolean accountEnabled)
    {
        this.shopKeeperId = shopKeeperId;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.pin = pin;
        this.imageUri = imageUri;
        this.accountEnabled = accountEnabled;
    }

    public Long getShopKeeperId() {
        return shopKeeperId;
    }

    public void setShopKeeperId(Long shopKeeperId) {
        this.shopKeeperId = shopKeeperId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public Boolean getAccountEnabled() {
        return accountEnabled;
    }

    public void setAccountEnabled(Boolean accountEnabled) {
        this.accountEnabled = accountEnabled;
    }
}
