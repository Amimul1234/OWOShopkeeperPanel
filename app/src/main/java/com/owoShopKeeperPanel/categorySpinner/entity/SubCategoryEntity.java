package com.owoShopKeeperPanel.categorySpinner.entity;

import com.owoShopKeeperPanel.homeComponents.brandsComponent.entity.Brands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubCategoryEntity implements Serializable {
    private Long sub_category_id;
    private String sub_category_name;
    private String sub_category_image;
    private CategoryEntity categoryEntity;
    private List<Brands> brandsList = new ArrayList<>();

    public SubCategoryEntity() {
    }

    public SubCategoryEntity(Long sub_category_id, String sub_category_name, String sub_category_image,
                             CategoryEntity categoryEntity, List<Brands> brandsList) {
        this.sub_category_id = sub_category_id;
        this.sub_category_name = sub_category_name;
        this.sub_category_image = sub_category_image;
        this.categoryEntity = categoryEntity;
        this.brandsList = brandsList;
    }

    public Long getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(Long sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    public String getSub_category_image() {
        return sub_category_image;
    }

    public void setSub_category_image(String sub_category_image) {
        this.sub_category_image = sub_category_image;
    }

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public List<Brands> getBrandsList() {
        return brandsList;
    }

    public void setBrandsList(List<Brands> brandsList) {
        this.brandsList = brandsList;
    }
}
