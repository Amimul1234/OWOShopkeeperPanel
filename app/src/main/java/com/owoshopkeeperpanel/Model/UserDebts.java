package com.owoshopkeeperpanel.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDebts implements Serializable {
    private long user_id;
    private String user_name;
    private String user_mobile_number;
    private double user_total_debt;
    private List<User_debt_details> userDebtDetails = new ArrayList<>();

    public UserDebts() {
    }

    public UserDebts(long user_id, String user_name, String user_mobile_number,
                     double user_total_debt) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_mobile_number = user_mobile_number;
        this.user_total_debt = user_total_debt;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_mobile_number() {
        return user_mobile_number;
    }

    public void setUser_mobile_number(String user_mobile_number) {
        this.user_mobile_number = user_mobile_number;
    }

    public double getUser_total_debt() {
        return user_total_debt;
    }

    public void setUser_total_debt(double user_total_debt) {
        this.user_total_debt = user_total_debt;
    }
}
