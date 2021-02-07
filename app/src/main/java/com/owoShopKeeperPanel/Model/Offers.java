package com.owoShopKeeperPanel.Model;

import java.io.Serializable;

public class Offers implements Serializable {

    private String name, startdate, enddate, image, offerid, date, time;

    public Offers() {

    }

    public Offers(String name, String startdate, String enddate, String image, String offerid, String date, String time) {
        this.name = name;
        this.startdate = startdate;
        this.enddate = enddate;
        this.image = image;
        this.offerid = offerid;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOfferid() {
        return offerid;
    }

    public void setOfferid(String offerid) {
        this.offerid = offerid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
