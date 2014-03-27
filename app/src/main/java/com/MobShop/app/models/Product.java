package com.MobShop.app.models;

public class Product {
    private int productId;
    private String productName;
    private String productDescription;
    private double productPrice;
    private int productQuantity;
    private int productCategories;
    private int productSubcategories;
    private String productCategoryName;
    private String productSubCategoryName;
    private int productSale;
    private int productDiscount;
    private String productPhotoURL;
    private String[] productPhotoURLS;//adica mai multe fotografii

    public Product(int id, String name) {
        this.productId = id;
        this.productName = name;
    }

    public void setQuantity(int quantity) {
        this.productQuantity = quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return productDescription;
    }

    public void setDescription(String description) {
        this.productDescription = description;
    }

    public Double getPrice() {
        return productPrice;
    }

    public void setPrice(Double price) {
        this.productPrice = price;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setCategories(int categories) {
        this.productCategories = categories;
    }

    public void setSubCategories(int subCategories) {
        this.productSubcategories = subCategories;
    }

    public void setProductSale(int productSale) {
        this.productSale = productSale;
    }

    public void setProductDiscount(int productDiscount) {
        this.productDiscount = productDiscount;
    }

    public String getProductPhotoURL() {
        return productPhotoURL;
    }

    public void setProductPhotoURL(String photoURL) {
        this.productPhotoURL = photoURL;
    }

    public String[] getProductPhotoURLS() {
        return productPhotoURLS;
    }

    public void setProductPhotoURLS(String[] photoURLS) {
        productPhotoURLS = photoURLS;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String name) {
        this.productCategoryName = name;
    }

    public String getProductSubCategoryName() {
        return productSubCategoryName;
    }

    public void setProductSubCategoryName(String name) {
        this.productSubCategoryName = name;
    }

}
