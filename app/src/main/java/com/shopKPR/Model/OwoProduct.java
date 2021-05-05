package com.shopKPR.Model;

import com.shopKPR.homeComponents.brandsComponent.entity.Brands;
import java.io.Serializable;
import java.util.Objects;

public class OwoProduct implements Serializable {
    private Long productId;
    private String productName;
    private Long productCategoryId;
    private Long productSubCategoryId;
    private Double productPrice;
    private Double productDiscount;
    private Integer productQuantity;
    private String productDescription;
    private String productCreationDate;
    private String productCreationTime;
    private String productImage;
    private Brands brands;
    private String productExpireDate;

    public OwoProduct() {
    }

    public OwoProduct(Long productId, String productName, Long productCategoryId,
                      Long productSubCategoryId, Double productPrice, Double productDiscount,
                      Integer productQuantity, String productDescription, String productCreationDate,
                      String productCreationTime, String productImage, Brands brands, String productExpireDate) {
        this.productId = productId;
        this.productName = productName;
        this.productCategoryId = productCategoryId;
        this.productSubCategoryId = productSubCategoryId;
        this.productPrice = productPrice;
        this.productDiscount = productDiscount;
        this.productQuantity = productQuantity;
        this.productDescription = productDescription;
        this.productCreationDate = productCreationDate;
        this.productCreationTime = productCreationTime;
        this.productImage = productImage;
        this.brands = brands;
        this.productExpireDate = productExpireDate;
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

    public Long getProductSubCategoryId() {
        return productSubCategoryId;
    }

    public void setProductSubCategoryId(Long productSubCategoryId) {
        this.productSubCategoryId = productSubCategoryId;
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

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductCreationDate() {
        return productCreationDate;
    }

    public void setProductCreationDate(String productCreationDate) {
        this.productCreationDate = productCreationDate;
    }

    public String getProductCreationTime() {
        return productCreationTime;
    }

    public void setProductCreationTime(String productCreationTime) {
        this.productCreationTime = productCreationTime;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Brands getBrands() {
        return brands;
    }

    public void setBrands(Brands brands) {
        this.brands = brands;
    }

    public String getProductExpireDate() {
        return productExpireDate;
    }

    public void setProductExpireDate(String productExpireDate) {
        this.productExpireDate = productExpireDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OwoProduct that = (OwoProduct) o;

        if (!Objects.equals(productId, that.productId))
            return false;
        if (!Objects.equals(productName, that.productName))
            return false;
        if (!Objects.equals(productCategoryId, that.productCategoryId))
            return false;
        if (!Objects.equals(productSubCategoryId, that.productSubCategoryId))
            return false;
        if (!Objects.equals(productPrice, that.productPrice))
            return false;
        if (!Objects.equals(productDiscount, that.productDiscount))
            return false;
        if (!Objects.equals(productQuantity, that.productQuantity))
            return false;
        if (!Objects.equals(productDescription, that.productDescription))
            return false;
        if (!Objects.equals(productCreationDate, that.productCreationDate))
            return false;
        if (!Objects.equals(productCreationTime, that.productCreationTime))
            return false;
        if (!Objects.equals(productImage, that.productImage))
            return false;
        if (!Objects.equals(brands, that.brands)) return false;
        return Objects.equals(productExpireDate, that.productExpireDate);
    }

    @Override
    public int hashCode() {
        int result = productId != null ? productId.hashCode() : 0;
        result = 31 * result + (productName != null ? productName.hashCode() : 0);
        result = 31 * result + (productCategoryId != null ? productCategoryId.hashCode() : 0);
        result = 31 * result + (productSubCategoryId != null ? productSubCategoryId.hashCode() : 0);
        result = 31 * result + (productPrice != null ? productPrice.hashCode() : 0);
        result = 31 * result + (productDiscount != null ? productDiscount.hashCode() : 0);
        result = 31 * result + (productQuantity != null ? productQuantity.hashCode() : 0);
        result = 31 * result + (productDescription != null ? productDescription.hashCode() : 0);
        result = 31 * result + (productCreationDate != null ? productCreationDate.hashCode() : 0);
        result = 31 * result + (productCreationTime != null ? productCreationTime.hashCode() : 0);
        result = 31 * result + (productImage != null ? productImage.hashCode() : 0);
        result = 31 * result + (brands != null ? brands.hashCode() : 0);
        result = 31 * result + (productExpireDate != null ? productExpireDate.hashCode() : 0);
        return result;
    }
}
