package com.owoShopKeeperPanel.changableEnums;

public enum TermsAndConditions {
    TERMS_AND_CONDITIONS("This needed to be filled with terms and conditions");
    private final String termsAndConditions;

    TermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }
}
