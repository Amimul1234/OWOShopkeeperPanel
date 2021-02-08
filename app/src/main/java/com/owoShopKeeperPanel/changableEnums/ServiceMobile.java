package com.owoShopKeeperPanel.changableEnums;

public enum ServiceMobile {
    SERVICE_MOBILE("+8801612201602");
    private final String serviceMobile;

    ServiceMobile(String s) {
        this.serviceMobile = s;
    }

    public String getServiceMobile() {
        return serviceMobile;
    }
}
