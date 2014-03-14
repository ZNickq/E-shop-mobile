package com.MobShop.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Segarceanu Calin on 3/9/14.
 */
public class CartPage extends Fragment {
    public ArrayList<Product> products;
    public Context ctxt, rootViewContext;
    public NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;
    public ListView listView;
    public Button checkoutButton;
    public ListViewCartAdapter adapter;
    public View rootView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.cart_page, container, false);
        rootViewContext = rootView.getContext();

        SharedPerferencesExecutor<Cart> cartSharedPerferencesExecutor = new SharedPerferencesExecutor<Cart>(ctxt);
        Cart cart = cartSharedPerferencesExecutor.retreive("eshop", Cart.class);
        cartSharedPerferencesExecutor.save("eshop", cart);
        products = cart.getProducts();
        Product[] productArray = new Product[products.size()];
        productArray = products.toArray(productArray);
        adapter = new ListViewCartAdapter(ctxt,R.layout.list_view_cart_page_row, productArray, mCallbacks);
        listView = (ListView) rootView.findViewById(R.id.listViewCartPage);
        listView.setAdapter(adapter);
        checkoutButton = (Button) rootView.findViewById(R.id.buttonCartPageCheckout);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                if(user.getLoggedIn() == true){
                    CheckoutTask sendData = new CheckoutTask();
                    sendData.execute(new String[]{"checkout"});
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
        ((MainActivity) getActivity()).setActionBar(7);
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

    private class CheckoutTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;

        protected void onPreExecute() {
            this.dialog = new ProgressDialog(rootViewContext);
            this.dialog.setMessage("Comanda se trimite catre server");
            this.dialog.show();
        }
        protected String doInBackground(String... functions) {
            return "";
        }
        @Override
        protected void onPostExecute(String result) {

        }
    }
    private class GetCartTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }

        protected void onPreExecute() {
            this.dialog = new ProgressDialog(rootViewContext);
            this.dialog.setMessage("Loading");
            this.dialog.show();
        }


        protected Void onPostExecute() {

            dialog.dismiss();
            return null;
        }
    }

}
