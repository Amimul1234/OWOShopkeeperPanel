package com.owoShopKeeperPanel.pagination.userDebts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import com.owoShopKeeperPanel.Model.UserDebts;

public class UserDebtViewModel extends ViewModel {

    public LiveData<PagedList<UserDebts>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, UserDebts>> liveDataSource;

    public UserDebtViewModel() {

        UserDebtDataSourceFactory userDebtDataSourceFactory = new UserDebtDataSourceFactory();
        liveDataSource = userDebtDataSourceFactory.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(10)
                        .setEnablePlaceholders(false)
                        .build();

        itemPagedList = (new LivePagedListBuilder(userDebtDataSourceFactory, config)).build();
    }

    public void clear(){
        liveDataSource.getValue().invalidate();
    }
}
