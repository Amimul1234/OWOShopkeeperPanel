package com.owoShopKeeperPanel.pagination.userDebts;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import com.owoShopKeeperPanel.Model.UserDebts;
import org.jetbrains.annotations.NotNull;

public class UserDebtDataSourceFactory extends DataSource.Factory{

    private MutableLiveData<PageKeyedDataSource<Integer, UserDebts>> itemLiveDataSource = new MutableLiveData<>();

    @NotNull
    @Override
    public DataSource create() {
        UserDebtDataSource userDebtDataSource = new UserDebtDataSource();
        itemLiveDataSource.postValue(userDebtDataSource);
        return userDebtDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, UserDebts>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
