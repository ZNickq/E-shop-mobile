package com.MobShop.app;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Segarceanu Calin on 2/24/14.
 */
public class ListOfProducts extends Fragment{

    private static final String ARG_SUB_CATEGORY_NUMBER = "section_number";
    private static final String ARG_SUB_CATEGORY_NAME = "section_name";

    public static final String PRODUCT_ID = "id";
    public static final String PRODUCT_NAME = "name";
    public static final String PRODUCT_DESCRIPTION = "description";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_QUANTITY = "quantity";
    public static final String PRODUCT_CATEGORIES = "categories_id";
    public static final String PRODUCT_SUBCATEGORIES = "subcategories_id";
    public static final String PRODUCT_SALE = "sale";
    public static final String PRODUCT_DISCOUNT = "discount";
    public static final String PRODUCT_PHOTOURL = "photo_url";

    private int mSubCategoryID = 0;
    private String mSubCategoryName = "";
    private boolean mFromSavedInstanceState;
    public static String URL = "http://dragomircristian.net/calin/api/";

    public ListView listOfProductsView;

    public Product[] productArray;

    public ListOfProducts(){

    }

    public static ListOfProducts newInstance(int subCategoryNumber, String subCategoryName) {
        ListOfProducts fragment = new ListOfProducts();
        Bundle args = new Bundle();
        args.putInt(ARG_SUB_CATEGORY_NUMBER, subCategoryNumber);
        args.putString(ARG_SUB_CATEGORY_NAME, subCategoryName);
        fragment.setArguments(args);
        fragment.setUp(subCategoryNumber, subCategoryName);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (savedInstanceState != null) {
            mSubCategoryID = savedInstanceState.getInt(ARG_SUB_CATEGORY_NUMBER);
            mSubCategoryName = savedInstanceState.getString(ARG_SUB_CATEGORY_NAME);
            mFromSavedInstanceState = true;
        }
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
        View rootView = inflater.inflate(R.layout.list_of_products_by_subcategory, container, false);
        listOfProductsView = (ListView) rootView.findViewById(R.id.listViewProductBySubCategory);

        ListOfProductsBySubCategories getSub = new  ListOfProductsBySubCategories();
        getSub.execute(new String[] { "getproductsbysubcategory"});



        return rootView;
    }

    public void setUp(int number, String name){
        //ActionBar actionBar = getActionBar();
        mSubCategoryID = number;
        mSubCategoryName = name;
    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    private class ListOfProductsBySubCategories extends AsyncTask<String, Void, ArrayList<Product>> {
        @Override
        protected ArrayList<Product> doInBackground(String... functions) {
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
                    nameValuePairs.add(new BasicNameValuePair("subCategory", String.valueOf(mSubCategoryID)));
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
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.i("HTTP Failed", e.toString());
                }
            }
            // Parse String to JSON object
            JSONArray jarray = null;
            try {
                jarray = new JSONArray( builder.toString());
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            ArrayList<Product> jsonlist = new ArrayList<Product>();
            //add data to jsonlist, in order to easily proccessing
            try{
                for (int i = 0; i < jarray.length(); i++) {
                    try {
                        JSONObject c = jarray.getJSONObject(i);
                        Integer id = c.getInt(PRODUCT_ID);
                        String name = c.getString(PRODUCT_NAME);
                        String description = c.getString(PRODUCT_DESCRIPTION );
                        Double price = c.getDouble(PRODUCT_PRICE);
                        Integer quantity = c.getInt(PRODUCT_QUANTITY);
                        Integer categories = c.getInt(PRODUCT_CATEGORIES);
                        Integer subcategories = c.getInt(PRODUCT_SUBCATEGORIES);
                        Integer sale = c.getInt(PRODUCT_SALE);
                        Integer discount = c.getInt(PRODUCT_DISCOUNT);
                        String photourl = c.getString(PRODUCT_PHOTOURL);
                        Product product = new Product(id, name);
                        product.setDescription(description);
                        product.setPrice(price);
                        product.setQuantity(quantity);
                        product.setCategories(categories);
                        product.setSubCategories(subcategories);
                        product.setProductSale(sale);
                        product.setProductDiscount(discount);
                        product.setProductPhotoURL(photourl);
                        jsonlist.add(product);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            return jsonlist;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> result) {
            productArray = new Product[result.size()];
            productArray = result.toArray(productArray);
            ListViewProductsAdapter listViewAdapter = new ListViewProductsAdapter(getActivity(),R.layout.list_view_products_by_subcategory_row, productArray);
           listOfProductsView.setAdapter(listViewAdapter);
        }
    }

}
