package com.shopKPR.shopKeeperPanel;

public class Referral {
    private String referrerNumber;
    private Long referPoint;

    public Referral() {
    }

    public Referral(String referrerNumber, Long referPoint) {
        this.referrerNumber = referrerNumber;
        this.referPoint = referPoint;
    }

    public String getReferrerNumber() {
        return referrerNumber;
    }

    public void setReferrerNumber(String referrerNumber) {
        this.referrerNumber = referrerNumber;
    }

    public Long getReferPoint() {
        return referPoint;
    }

    public void setReferPoint(Long referPoint) {
        this.referPoint = referPoint;
    }
}
