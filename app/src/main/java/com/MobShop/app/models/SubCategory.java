package com.MobShop.app.models;

public class SubCategory {
    public int id;
    public String name;

    public SubCategory(int i, String n) {
        this.id = i;
        this.name = n;
    }

    public int getSubCategoryId() {
        return this.id;
    }

    public String getSubCategoryName() {
        return this.name;
    }

    public String toString() {
        return name;
    }
}
