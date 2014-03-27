package com.MobShop.app.models;

import java.util.ArrayList;

public class Cart {

    ArrayList<Product> products;

    public Cart() {
        products = new ArrayList<Product>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void deleteProduct(int position) {
        products.remove(position);
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

}
