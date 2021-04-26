package com.shopKPR.qupon;

import java.util.Date;

public class Qupon
{
    private Long quponId;
    private double discount;
    private Date quponStartDate;
    private Date quponEndDate;
    private boolean enabled;

    public Qupon() {
    }

    public Qupon(Long quponId, double discount, Date quponStartDate, Date quponEndDate, boolean enabled) {
        this.quponId = quponId;
        this.discount = discount;
        this.quponStartDate = quponStartDate;
        this.quponEndDate = quponEndDate;
        this.enabled = enabled;
    }

    public Long getQuponId() {
        return quponId;
    }

    public void setQuponId(Long quponId) {
        this.quponId = quponId;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Date getQuponStartDate() {
        return quponStartDate;
    }

    public void setQuponStartDate(Date quponStartDate) {
        this.quponStartDate = quponStartDate;
    }

    public Date getQuponEndDate() {
        return quponEndDate;
    }

    public void setQuponEndDate(Date quponEndDate) {
        this.quponEndDate = quponEndDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
