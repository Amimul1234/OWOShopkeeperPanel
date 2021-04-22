package com.shopKPR.pagination.subCategory;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.shopKPR.Model.OwoProduct;


public class ItemDataSourceFactorySubCategory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> itemLiveDataSource = new MutableLiveData<>();
    private Long subCategoryId;

    public ItemDataSourceFactorySubCategory(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    @Override
    public DataSource create() {
        ItemDataSourceSubCategory itemDataSource = new ItemDataSourceSubCategory(subCategoryId);
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
