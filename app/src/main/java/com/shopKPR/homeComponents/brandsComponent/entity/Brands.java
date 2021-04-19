package com.shopKPR.homeComponents.brandsComponent.entity;

import com.shopKPR.Model.OwoProduct;
import com.shopKPR.categorySpinner.entity.SubCategoryEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Brands implements Serializable {
    private Long brandId;
    private String brandName;
    private String brandImage;
    private SubCategoryEntity subCategoryEntity;
    private List<OwoProduct> owoProductList = new ArrayList<>();

    public Brands() {
    }

    public Brands(Long brandId, String brandName, String brandImage,
                  SubCategoryEntity subCategoryEntity, List<OwoProduct> owoProductList) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.brandImage = brandImage;
        this.subCategoryEntity = subCategoryEntity;
        this.owoProductList = owoProductList;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }

    public SubCategoryEntity getSubCategoryEntity() {
        return subCategoryEntity;
    }

    public void setSubCategoryEntity(SubCategoryEntity subCategoryEntity) {
        this.subCategoryEntity = subCategoryEntity;
    }

    public List<OwoProduct> getOwoProductList() {
        return owoProductList;
    }

    public void setOwoProductList(List<OwoProduct> owoProductList) {
        this.owoProductList = owoProductList;
    }
}
