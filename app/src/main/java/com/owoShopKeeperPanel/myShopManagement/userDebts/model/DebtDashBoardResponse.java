package com.owoShopKeeperPanel.myShopManagement.userDebts.model;

import java.io.Serializable;

public class DebtDashBoardResponse implements Serializable {
    private Double totalLoan;
    private Double totalPaid;

    public DebtDashBoardResponse() {
    }

    public DebtDashBoardResponse(Double totalLoan, Double totalPaid) {
        this.totalLoan = totalLoan;
        this.totalPaid = totalPaid;
    }

    public Double getTotalLoan() {
        return totalLoan;
    }

    public void setTotalLoan(Double totalLoan) {
        this.totalLoan = totalLoan;
    }

    public Double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(Double totalPaid) {
        this.totalPaid = totalPaid;
    }
}
