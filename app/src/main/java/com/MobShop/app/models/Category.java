package com.MobShop.app.models;

public class Category {

    private String categoryName;
    private Integer categoryId;
    private String photoURL;
    private SubCategory[] subCategories;

    public Category(Integer id, String name) {
        this.categoryId = id;
        this.categoryName = name;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public String getPhotoURL() {
        return this.photoURL;
    }

    public void setPhotoURL(String url) {
        this.photoURL = url;
    }

    public SubCategory[] getSubCategories() {
        return this.subCategories;
    }

    public void setSubCategories(SubCategory[] array) {
        this.subCategories = array;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(categoryName);
        for (SubCategory subCategory : subCategories) {
            builder.append(subCategory.toString());
        }
        return builder.toString();
    }
}
