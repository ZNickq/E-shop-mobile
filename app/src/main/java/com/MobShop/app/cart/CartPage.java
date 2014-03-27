package com.MobShop.app.cart;

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
import android.widget.Toast;

import com.MobShop.app.MainActivity;
import com.MobShop.app.R;
import com.MobShop.app.models.Cart;
import com.MobShop.app.models.Product;
import com.MobShop.app.models.User;
import com.MobShop.app.navdrawer.NavigationDrawerFragment;
import com.MobShop.app.util.SharedPreferencesExecutor;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
        CartPage fragment = new CartPage();
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

        SharedPreferencesExecutor<Cart> cartSharedPreferencesExecutor = new SharedPreferencesExecutor<Cart>(ctxt);
        Cart cart = cartSharedPreferencesExecutor.retreive("eshop", Cart.class);
        cartSharedPreferencesExecutor.save("eshop", cart);
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
                if (user.getLoggedIn()) {
                    CheckoutTask sendData = new CheckoutTask();
                    sendData.execute("checkout");
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
            SharedPreferencesExecutor<Cart> cartSharedPreferencesExecutor = new SharedPreferencesExecutor<Cart>(ctxt);
            Cart cart = cartSharedPreferencesExecutor.retreive("eshop", Cart.class);
            cartSharedPreferencesExecutor.save("eshop", cart);
            ArrayList<Product> product = cart.getProducts();
            Gson gson = new Gson();
            String json = gson.toJson(product);
            User user = new User();
            String email = user.getEmail();
            String function = functions[0];

            StringBuilder builder = new StringBuilder();
            //create url from base url
            String url = NavigationDrawerFragment.URL + function;
            //connect to server
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity;
            HttpResponse httpResponse;
            HttpPost httpPost = new HttpPost(url);
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("json", json));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("numberOfProducts", String.valueOf(product.size())));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                try {
                    httpResponse = httpClient.execute(httpPost);
                    StatusLine statusLine = httpResponse.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    //if ok get data from server
                    if (statusCode == 200) {
                        httpEntity = httpResponse.getEntity();
                        InputStream content = httpEntity.getContent();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                    } else {
                        Log.e("==>", "Failed to download file");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.i("HTTP Failed", e.toString());
            }
            String res = "";
            //add data to jsonlist, in order to easily proccessing
            try {
                try {
                    JSONObject c = new JSONObject(builder.toString());
                    res = c.getString("result");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Log.d("URL", result);
            if(result.equals("1")){
                Toast toast = Toast.makeText(rootViewContext, "Comanda trimisa cu succes", Toast.LENGTH_LONG);
                toast.show();
                SharedPreferencesExecutor<Cart> cartSharedPreferencesExecutor = new SharedPreferencesExecutor<Cart>(rootViewContext);
                cartSharedPreferencesExecutor.delete("eshop");
                mCallbacks.onNavigationDrawerItemSelected("CartPage", 7);
            }
        }
    }
}
