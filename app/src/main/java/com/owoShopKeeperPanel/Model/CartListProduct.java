package com.owoShopKeeperPanel.Model;

import java.io.Serializable;

public class CartListProduct implements Serializable {
    private Long productId;
    private String productName;
    private Long productCategoryId;
    private Long productSubCategoryId;
    private Double productPrice;
    private Double productDiscount;
    private int productQuantity;
    private String productAddingDate;
    private String productAddingTime;
    private String productImage;

    public CartListProduct() {

    }

    public CartListProduct(Long productId, String productName, Long productCategoryId, Long productSubCategory,
                           Double productPrice, Double productDiscount, int productQuantity, String productAddingDate,
                           String productAddingTime, String productImage) {
        this.productId = productId;
        this.productName = productName;
        this.productCategoryId = productCategoryId;
        this.productSubCategoryId = productSubCategory;
        this.productPrice = productPrice;
        this.productDiscount = productDiscount;
        this.productQuantity = productQuantity;
        this.productAddingDate = productAddingDate;
        this.productAddingTime = productAddingTime;
        this.productImage = productImage;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Long getProductSubCategory() {
        return productSubCategoryId;
    }

    public void setProductSubCategory(Long productSubCategory) {
        this.productSubCategoryId = productSubCategory;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Double getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(Double productDiscount) {
        this.productDiscount = productDiscount;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductAddingDate() {
        return productAddingDate;
    }

    public void setProductAddingDate(String productAddingDate) {
        this.productAddingDate = productAddingDate;
    }

    public String getProductAddingTime() {
        return productAddingTime;
    }

    public void setProductAddingTime(String productAddingTime) {
        this.productAddingTime = productAddingTime;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
