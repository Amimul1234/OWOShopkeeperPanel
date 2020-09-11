package com.owoshopkeeperpanel.Model;

import java.io.Serializable;

public class Qupon implements Serializable {

    private String qupon_code,qupon_discount;

    public Qupon() {
    }

    public Qupon(String qupon_code, String qupon_discount) {
        this.qupon_code = qupon_code;
        this.qupon_discount = qupon_discount;
    }

    public String getQupon_code() {
        return qupon_code;
    }

    public void setQupon_code(String qupon_code) {
        this.qupon_code = qupon_code;
    }

    public String getQupon_discount() {
        return qupon_discount;
    }

    public void setQupon_discount(String qupon_discount) {
        this.qupon_discount = qupon_discount;
    }
}
