package com.MobShop.app;

import java.util.ArrayList;

/**
 * Created by Segarceanu Calin on 3/9/14.
 */
public class Cart{

   ArrayList<Product> products;

    Cart(){
        products = new ArrayList<Product>();
    }

    public void addProduct(Product product){
        products.add(product);
    }

    public void deleteProduct(int position){
        products.remove(position);
    }

    public ArrayList<Product> getProducts(){
        return products;
    }

}
