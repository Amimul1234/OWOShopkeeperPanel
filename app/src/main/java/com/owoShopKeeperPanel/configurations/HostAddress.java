package com.owoShopKeeperPanel.configurations;

public enum HostAddress {

    HOST_ADDRESS("http://192.168.0.2");

    private final String hostAddress;

    HostAddress(String s) {
        this.hostAddress = s;
    }

    public String getHostAddress() {
        return hostAddress;
    }
}
