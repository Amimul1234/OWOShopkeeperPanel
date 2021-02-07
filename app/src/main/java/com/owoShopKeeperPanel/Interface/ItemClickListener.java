package com.owoShopKeeperPanel.Interface;

import android.view.View;

//A listener for firebase recyclerview

public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
}
