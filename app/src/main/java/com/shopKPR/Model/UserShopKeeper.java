package com.shopKPR.Model;

import java.io.Serializable;

public class UserShopKeeper implements Serializable {

    private String name, phone, pin, image;

    public UserShopKeeper() {

    }

    public UserShopKeeper(String name, String phone, String pin, String image) {
        this.name = name;
        this.phone = phone;
        this.pin = pin;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
