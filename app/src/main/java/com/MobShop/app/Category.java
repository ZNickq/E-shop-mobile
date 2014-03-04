package com.MobShop.app;

/**
 * Created by Segarceanu Calin on 2/28/14.
 */
public class Category {

    private String categoryName;
    private Integer categoryId;
    private String photoURL;
    private SubCategory[] subCategories;

    public Category(Integer id, String name) {
        this.categoryId = id;
        this.categoryName = name;
    }

    public void setPhotoURL(String url) {
        this.photoURL = url;
    }

    public void setSubCategories(SubCategory[] array) {
        this.subCategories = array;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public Integer getCategoryId() {
        return this.categoryId;
    }

    public String getPhotoURL() {
        return this.photoURL;
    }

    public SubCategory[] getSubCategories() {
        return this.subCategories;
    }
}
