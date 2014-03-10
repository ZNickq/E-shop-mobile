package com.MobShop.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Created by Segarceanu Calin on 3/9/14.
 */
public class CartPage extends Fragment {
    public ArrayList<Product> products;
    public Context ctxt;
    public NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;

    public CartPage(){

    }

    public static CartPage newInstance(Context context) {
        CartPage fragment = new CartPage();        ;
        fragment.setUp(context);
        return fragment;
    }

    private void setUp(Context context) {
        this.ctxt = context;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.cart_page, container, false);
        SharedPerferencesExecutor<Cart> cartSharedPerferencesExecutor = new SharedPerferencesExecutor<Cart>(ctxt);
        Cart cart = cartSharedPerferencesExecutor.retreive("eshop", Cart.class);
        cartSharedPerferencesExecutor.save("eshop", cart);
        products = cart.getProducts();
        Product[] productArray = new Product[products.size()];
        productArray = products.toArray(productArray);
        ListViewCartAdapter adapter = new ListViewCartAdapter(ctxt,R.layout.list_view_cart_page_row, productArray, mCallbacks);
        ListView listView = (ListView) rootView.findViewById(R.id.listViewCartPage);
        listView.setAdapter(adapter);


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerFragment.NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement ListOfProductsCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

}
