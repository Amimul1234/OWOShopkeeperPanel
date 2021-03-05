package com.owoShopKeeperPanel.prevalent;

import com.owoShopKeeperPanel.userRegistration.ShopKeeperUser;

import java.util.ArrayList;
import java.util.List;

public class Prevalent {
    public static ShopKeeperUser currentOnlineUser;
    public final static List<Long> category_to_display = new ArrayList<>();
    public static final String UserPhoneKey = "UserPhone";
    public static final String UserPinKey = "UserPin";
    public static int locale = 0;
}
