package com.MobShop.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    public Product productMain;
    public Cart cart;

    public TextView productCategoryTextView, productDeliveryTextView, productPriceTextView, productNameTextView, productDescriptionTextView;
    public ImageView productImageView;
    public Button buttonAddToCart;

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

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_fragment, container, false);
        final Context rootViewContext = rootView.getContext();
        productCategoryTextView = (TextView) rootView.findViewById(R.id.productPageProductCategory);
        productNameTextView = (TextView) rootView.findViewById(R.id.productName);
        productDeliveryTextView = (TextView) rootView.findViewById(R.id.deliveryprice);
        productPriceTextView = (TextView) rootView.findViewById(R.id.price);
        productDescriptionTextView = (TextView) rootView.findViewById(R.id.productPageDescription);
        productImageView = (ImageView) rootView.findViewById(R.id.productImage);
        ProductByID getProduct = new ProductByID();
        getProduct.execute(new String[]{"getproductbyid"});
        buttonAddToCart = (Button) rootView.findViewById(R.id.buttonProductPageAddToCart);

        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // custom dialog
                final Dialog dialog = new Dialog(rootViewContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_add_to_cart);
                final Spinner spinner = (Spinner) dialog.findViewById(R.id.addToCartSpinner);
                Button closeButton = (Button) dialog.findViewById(R.id.dialogAddToCartCloseButton);
                Button addButton = (Button) dialog.findViewById(R.id.dialogAddToCartButton);

                final List<String> quantityList = new ArrayList<String>();
                Integer N = productMain.getProductQuantity();
                for(int i = 0; i < N; i++){
                    quantityList.add(String.valueOf(i + 1));
                    if(i >= 20){
                        i += 4;
                    }
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(rootViewContext,android.R.layout.simple_spinner_item, quantityList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int p = spinner.getSelectedItemPosition();
                        Product product = new Product(productMain.getProductId(), productMain.getProductName());
                        product.setQuantity(p);
                        product.setProductPhotoURL(productMain.getProductPhotoURL());
                        product.setDescription(productMain.getDescription());
                        product.setPrice(productMain.getPrice());
                        SharedPerferencesExecutor<Cart> cartSharedPerferencesExecutor = new SharedPerferencesExecutor<Cart>(rootViewContext);
                        cart = cartSharedPerferencesExecutor.retreive("eshop", Cart.class);
                        cart.addProduct(product);
                        cartSharedPerferencesExecutor.save("eshop", cart);
                        Toast toast = Toast.makeText(rootViewContext, quantityList.get(p) + " " + productMain.getProductName() + " au fost adaugate in cos", Toast.LENGTH_LONG);
                        toast.show();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

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
                if(photosURLArray[0] != null){
                    product.setProductPhotoURL(photosURLArray[0]);
                }else{
                    product.setProductPhotoURL("");
                }
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
            productMain = result;
            String category = result.getProductCategoryName() + "  >  " + result.getProductSubCategoryName();
            String productName = result.getProductName();
            Double price = result.getPrice();
            String description = result.getDescription();
            productCategoryTextView.setText(category);
            productNameTextView.setText(productName);
            productPriceTextView.setText(price + " lei");
            productDeliveryTextView.setText("19 lei");
            productDescriptionTextView.setText(Html.fromHtml(description));
        }

    }

}
