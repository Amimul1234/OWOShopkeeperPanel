package com.owoShopKeeperPanel.Model;

import java.io.Serializable;

public class User_debt_details implements Serializable {
    private long id;
    private String description;
    private String date;
    private double taka;

    public User_debt_details() {
    }

    public User_debt_details(long id, String description, String date, double taka) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.taka = taka;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTaka() {
        return taka;
    }

    public void setTaka(double taka) {
        this.taka = taka;
    }
}
