package com.MobShop.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * Created by Segarceanu Calin on 3/9/14.
 */
public class CartPage extends Fragment {
    public ArrayList<Product> products;
    public Context ctxt;
    public NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;
    public ListView listView;
    public Button checkoutButton;

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
        final Context rootViewContext = rootView.getContext();
        SharedPerferencesExecutor<Cart> cartSharedPerferencesExecutor = new SharedPerferencesExecutor<Cart>(ctxt);
        Cart cart = cartSharedPerferencesExecutor.retreive("eshop", Cart.class);
        cartSharedPerferencesExecutor.save("eshop", cart);
        products = cart.getProducts();
        Product[] productArray = new Product[products.size()];
        productArray = products.toArray(productArray);
        ListViewCartAdapter adapter = new ListViewCartAdapter(ctxt,R.layout.list_view_cart_page_row, productArray, mCallbacks);
        listView = (ListView) rootView.findViewById(R.id.listViewCartPage);
        checkoutButton = (Button) rootView.findViewById(R.id.buttonCartPageCheckout);
        listView.setAdapter(adapter);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                if(user.getLoggedIn() == true){

                }else{
                    new AlertDialog.Builder(rootViewContext)
                            .setTitle("Log In")
                            .setMessage("Nu sunteti logati! Mergeti catre pagina de login")
                            .setPositiveButton(R.string.yes_login, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mCallbacks.onNavigationDrawerItemSelected("Log in", 6);
                                }
                            })
                            .setNegativeButton(R.string.no_login, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerFragment.NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

}
