package com.MobShop.app;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
 * Created by Segarceanu Calin on 2/24/14.
 */
public class ProductPage extends Fragment {

    private static final String ARG_PRODUCT_ID_NUMBER = "section_number";

    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_DESCRIPTION = "description";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_QUANTITY = "quantity";
    public static final String PRODUCT_CATEGORIES = "categories_id";
    public static final String PRODUCT_SUBCATEGORIES = "subcategories_id";
    public static final String PRODUCT_SALE = "sale";
    public static final String PRODUCT_DISCOUNT = "discount";
    public static final String PRODUCT_PHOTOSURLS = "photos_urls";
    public static final String PRODUCT_CATEGORYNAME = "category_name";
    public static final String PRODUCT_SUBCATEGORYNAME = "subcategory_name";

    private boolean mFromSavedInstanceState;
    private Integer mProductID;
    private Context ctxt;

    public TextView productCategoryTextView, productDeliveryTextView, productPriceTextView, productNameTextView;

    public ProductPage() {

    }

    public static ProductPage newInstance(int productId, Context context) {
        ProductPage fragment = new ProductPage();
        Bundle args = new Bundle();
        args.putInt(ARG_PRODUCT_ID_NUMBER, productId);
        fragment.setArguments(args);
        fragment.setUp(productId, context);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (savedInstanceState != null) {
            mProductID = savedInstanceState.getInt(ARG_PRODUCT_ID_NUMBER);
            mFromSavedInstanceState = true;
        }
        /*TabHost tabHost = new TabHost(getActivity());
        TabWidget tabWidget = new TabWidget(getActivity());
        tabWidget.setId(android.R.id.tabs);
        tabHost.addView(tabWidget);
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("Detalii");
        tabHost.addTab(tabSpec);
        tabSpec=tabHost.newTabSpec("Produse similare");
        tabHost.addTab(tabSpec);*/
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
        View rootView = inflater.inflate(R.layout.product_fragment, container, false);
        productCategoryTextView = (TextView) rootView.findViewById(R.id.productPageProductCategory);
        productNameTextView = (TextView) rootView.findViewById(R.id.productName);
        productDeliveryTextView = (TextView) rootView.findViewById(R.id.deliveryProductPageTextView);
        productPriceTextView = (TextView) rootView.findViewById(R.id.priceProductPageTextView);
        ProductByID getProduct = new ProductByID();
        getProduct.execute(new String[]{"getproductbyid"});

        return rootView;
    }

    public void setUp(int number, Context context) {
        //ActionBar actionBar = getActionBar();
        mProductID = number;
        this.ctxt = context;
    }

    private class ProductByID extends AsyncTask<String, Void, Product> {
        @Override
        protected Product doInBackground(String... functions) {
            StringBuilder builder = new StringBuilder();
            for (String function : functions) {
                //create url from base url
                String url = ListOfProducts.URL + function;
                //connect to server
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;
                HttpPost httpPost = new HttpPost(url);
                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("idProduct", String.valueOf(mProductID)));
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
                            String line = "";
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
            }
            // Parse String to JSON object
            JSONObject c = null;
            try {
                c = new JSONObject(builder.toString());
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            Product product = null;
            //add data to jsonlist, in order to easily proccessing
            try {
                Integer id = c.getInt(PRODUCT_ID);
                String name = c.getString(PRODUCT_NAME);
                String description = c.getString(PRODUCT_DESCRIPTION);
                Double price = c.getDouble(PRODUCT_PRICE);
                Integer quantity = c.getInt(PRODUCT_QUANTITY);
                Integer categories = c.getInt(PRODUCT_CATEGORIES);
                Integer subcategories = c.getInt(PRODUCT_SUBCATEGORIES);
                String categoryName = c.getString(PRODUCT_CATEGORYNAME);
                String subCategoryName = c.getString(PRODUCT_SUBCATEGORYNAME);
                Integer sale = c.getInt(PRODUCT_SALE);
                Integer discount = c.getInt(PRODUCT_DISCOUNT);
                JSONArray subCategories = c.getJSONArray(PRODUCT_PHOTOSURLS);
                String[] photosURLArray;
                photosURLArray = new String[subCategories.length()];
                for (int j = 0; j < subCategories.length(); j++) {
                    JSONObject c2 = subCategories.getJSONObject(j);
                    String URL = c2.getString("URL");
                    photosURLArray[j] = URL;
                }
                product = new Product(id, name);
                product.setDescription(description);
                product.setPrice(price);
                product.setQuantity(quantity);
                product.setCategories(categories);
                product.setSubCategories(subcategories);
                product.setProductSale(sale);
                product.setProductDiscount(discount);
                product.setProductPhotoURLS(photosURLArray);
                product.setProductCategoryName(categoryName);
                product.setProductSubCategoryName(subCategoryName);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return product;
        }

        @Override
        protected void onPostExecute(Product result) {
            String category = "Toate categoriile > " + result.getProductCategoryName() + " > " + result.getProductSubCategoryName();
            String productName = result.getProductName();
            Double price = result.getPrice();
            String productPrice = price.toString();
            productCategoryTextView.setText(category);
            productNameTextView.setText(productName);
            productPriceTextView.setText(productPrice);
            productDeliveryTextView.setText("19 lei");
        }
    }

}
