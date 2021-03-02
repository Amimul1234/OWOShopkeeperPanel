package com.owoShopKeeperPanel.login;

import com.owoShopKeeperPanel.Model.Shops;
import java.io.Serializable;

public class ShopKeeperPermissions implements Serializable {
    private Long id;
    private Long permittedCategoryId;
    private Shops shops;

    public ShopKeeperPermissions() {
    }

    public ShopKeeperPermissions(Long id, Long permittedCategoryId, Shops shops) {
        this.id = id;
        this.permittedCategoryId = permittedCategoryId;
        this.shops = shops;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPermittedCategoryId() {
        return permittedCategoryId;
    }

    public void setPermittedCategoryId(Long permittedCategoryId) {
        this.permittedCategoryId = permittedCategoryId;
    }

    public Shops getShops() {
        return shops;
    }

    public void setShops(Shops shops) {
        this.shops = shops;
    }
}
