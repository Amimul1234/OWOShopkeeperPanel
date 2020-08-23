package com.owoshopkeeperpanel.Model;

public class Complains {
    private String complain_subject;
    private String complain_details;

    public Complains() {
    }

    public Complains(String complain_subject, String complain_details) {
        this.complain_subject = complain_subject;
        this.complain_details = complain_details;
    }

    public String getComplain_subject() {
        return complain_subject;
    }

    public void setComplain_subject(String complain_subject) {
        this.complain_subject = complain_subject;
    }

    public String getComplain_details() {
        return complain_details;
    }

    public void setComplain_details(String complain_details) {
        this.complain_details = complain_details;
    }
}
