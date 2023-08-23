package com.project.demo;

public class ProductModel {
    private String productType, brandName, price, color;

    public ProductModel() {
    }

    public ProductModel(String productType, String brandName, String price, String color) {
        this.productType = productType;
        this.brandName = brandName;
        this.price = price;
        this.color = color;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "productType='" + productType + '\'' +
                ", brandName='" + brandName + '\'' +
                ", color='" + color + '\'' +
                ", price=" + price +
                '}';
    }
}
