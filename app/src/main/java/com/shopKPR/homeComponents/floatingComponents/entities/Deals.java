package com.shopKPR.homeComponents.floatingComponents.entities;

import java.io.Serializable;

public class Deals implements Serializable {
    private Long dealsId;
    private String dealDetails;
    private String dealImage;

    public Deals() {
    }

    public Deals(Long dealsId, String dealDetails, String dealImage) {
        this.dealsId = dealsId;
        this.dealDetails = dealDetails;
        this.dealImage = dealImage;
    }

    public Long getDealsId() {
        return dealsId;
    }

    public void setDealsId(Long dealsId) {
        this.dealsId = dealsId;
    }

    public String getDealDetails() {
        return dealDetails;
    }

    public void setDealDetails(String dealDetails) {
        this.dealDetails = dealDetails;
    }

    public String getDealImage() {
        return dealImage;
    }

    public void setDealImage(String dealImage) {
        this.dealImage = dealImage;
    }
}
