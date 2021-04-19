package com.shopKPR.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDebts implements Serializable
{
    private long userId;
    private String userName;
    private String userMobileNumber;
    private double userTotalDebt;
    private double userPaid;
    private final List<User_debt_details> userDebtDetails = new ArrayList<>();

    public UserDebts() {
    }

    public UserDebts(long userId, String userName, String userMobileNumber, double userTotalDebt, double userPaid) {
        this.userId = userId;
        this.userName = userName;
        this.userMobileNumber = userMobileNumber;
        this.userTotalDebt = userTotalDebt;
        this.userPaid = userPaid;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
    }

    public double getUserTotalDebt() {
        return userTotalDebt;
    }

    public void setUserTotalDebt(double userTotalDebt) {
        this.userTotalDebt = userTotalDebt;
    }

    public double getUserPaid() {
        return userPaid;
    }

    public void setUserPaid(double userPaid) {
        this.userPaid = userPaid;
    }

    public List<User_debt_details> getUserDebtDetails() {
        return userDebtDetails;
    }

    public void setUserDebtDetails(List<User_debt_details> userDebtDetails) {
        this.userDebtDetails.addAll(userDebtDetails);
    }
}
