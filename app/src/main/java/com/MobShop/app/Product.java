package com.MobShop.app;

/**
 * Created by Segarceanu Calin on 2/25/14.
 */
public class Product {
    private int productId;
    private String productName;
    private String productDescription;
    private double productPrice;
    private int productQuantity;
    private int productCategories ;
    private int productSubcategories;
    private int productSale;
    private int productDiscount;
    private String productPhotoURL;
    private String[] productPhotoURLS;//adica mai multe fotografii

    Product(int id, String name){
        this.productId = id;
        this.productName = name;
    }

    public void setDescription(String description){
        this.productDescription = description;
    }

    public void setPrice(Double price){
        this.productPrice = price;
    }

    public void setQuantity(int quantity){
        this.productQuantity = quantity;
    }

    public void setCategories(int categories){
        this.productCategories = categories;
    }

    public void setSubCategories(int subCategories){
        this.productSubcategories = subCategories;
    }

    public void setProductSale(int productSale){
        this.productSale = productSale;
    }

    public void setProductDiscount(int productDiscount){
        this.productDiscount = productDiscount;
    }

    public void setProductPhotoURL(String photoURL){
        this.productPhotoURL = photoURL;
    }

    public Integer getProductId(){
        return productId;
    }

    public String getProductName(){
        return productName;
    }

    public String getDescription(){
        return productDescription;
    }

    public Double setPrice(){
        return productPrice;
    }

    public Integer setQuantity(){
        return productQuantity;
    }

    public Integer setCategories(){
        return productCategories;
    }

    public Integer setSubCategories(){
        return productSubcategories;
    }

    public Integer setProductSale(){
        return productSale;
    }

    public Integer setProductDiscount(){
        return productDiscount;
    }

    public String setProductPhotoURL(){
        return productPhotoURL;
    }

}
