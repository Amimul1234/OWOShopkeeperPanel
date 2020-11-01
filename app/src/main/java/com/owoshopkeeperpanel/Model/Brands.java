package com.owoshopkeeperpanel.Model;

public class Brands {
    private String brand_name;
    private String brand_image;
    private String category;

    public Brands() {
    }

    public Brands(String brand_name, String brand_image, String category) {
        this.brand_name = brand_name;
        this.brand_image = brand_image;
        this.category = category;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_image() {
        return brand_image;
    }

    public void setBrand_image(String brand_image) {
        this.brand_image = brand_image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
