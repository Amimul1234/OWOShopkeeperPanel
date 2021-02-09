package com.owoShopKeeperPanel.login;

import com.owoShopKeeperPanel.Model.Shops;

public class ShopKeeperPermissions {
    private Long id;
    private String permittedCategory;
    private Shops shops;

    public ShopKeeperPermissions() {
    }

    public ShopKeeperPermissions(Long id, String permittedCategory, Shops shops) {
        this.id = id;
        this.permittedCategory = permittedCategory;
        this.shops = shops;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermittedCategory() {
        return permittedCategory;
    }

    public void setPermittedCategory(String permittedCategory) {
        this.permittedCategory = permittedCategory;
    }

    public Shops getShops() {
        return shops;
    }

    public void setShops(Shops shops) {
        this.shops = shops;
    }
}
