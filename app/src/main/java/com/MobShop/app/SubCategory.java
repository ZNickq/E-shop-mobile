package com.MobShop.app;

/**
 * Created by Segarceanu Calin on 2/17/14.
 */
public class SubCategory {
    public int id;
    public String name;

    public SubCategory(int i, String n) {
        this.id = i;
        this.name = n;
    }

    public void setSubCategoryName(String n) {
        this.name = n;
    }

    public void setSubCategoryId(int i) {
        this.id = i;
    }

    public int getSubCategoryId() {
        return this.id;
    }

    public String getSubCategoryName() {
        return this.name;
    }

    public String toString(){
        return name;
    }
}
