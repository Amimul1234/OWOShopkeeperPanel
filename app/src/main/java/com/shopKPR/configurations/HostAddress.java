package com.shopKPR.configurations;

public enum HostAddress {

    HOST_ADDRESS("http://40.80.80.158:8080");

    private final String hostAddress;

    HostAddress(String s) {
        this.hostAddress = s;
    }

    public String getHostAddress() {
        return hostAddress;
    }
}
