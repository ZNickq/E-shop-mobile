package com.MobShop.app;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainMenuFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public GridView gridView;
    public Context context;
    public static String URL = "http://dragomircristian.net/calin/api/";
    public DefaultHttpClient httpClient;
    public static final String SUBCATEGORY_ID = "id";
    public static final String SUBCATEGORY_NAME = "name";
    public static final String CATEGORY_ID = "id";
    public static final String CATEGORY_NAME = "name";
    public static final String PHOTO_URL = "photo_url";
    public static final String SUBCATEGORIES = "subcategories";

    ArrayList<Category> categories;

    private NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;
    public GetCategoriesAndSubCategoriesTask getData;
    public static MainMenuFragment newInstance(int sectionNumber) {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainMenuFragment() {

    }

    @Override
    public  View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        context = gridView.getContext();
        getData = new GetCategoriesAndSubCategoriesTask();
        getData.execute(new String[]{"getallcategorieswithsubcategoriesandphotos"});

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category category = categories.get(i);
                final SubCategory[] subCategories = category.getSubCategories();

                if (subCategories.length != 0) {

                    // custom dialog
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_sub_categories);
                    dialog.setTitle(category.getCategoryName());
                    ListView listView = (ListView) dialog.findViewById(R.id.listViewSubCategoriesDialog);
                    ListViewSubCategoriesDialogAdapter adapter = new ListViewSubCategoriesDialogAdapter(context, R.layout.list_view_subcategories_dialog_row, subCategories);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            SubCategory sub = subCategories[i];
                            dialog.dismiss();
                            Integer id = sub.getSubCategoryId();
                            mCallbacks.onNavigationDrawerItemSelected("Subcategories: " + sub.getSubCategoryName(), id);
                        }
                    });
                    Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            }
        });
        GetCategoriesAndSubCategoriesTask getData = new  GetCategoriesAndSubCategoriesTask();
        getData.execute(new String[] { "getallcategorieswithsubcategoriesandphotos"});


        AutoScrollPager viewPager = (AutoScrollPager) rootView.findViewById(R.id.view_pager);
        ViewAdapter adapter = new ViewAdapter(this.context);
        viewPager.setAdapter(adapter);
        viewPager.startAutoScroll(2000);
        viewPager.setScrollDurationFactor(5);
        viewPager.setBorderAnimation(false);

        return rootView;
    }

    public class ViewAdapter extends PagerAdapter {
        Context context;
        private int[] pics = new int[]{
                R.drawable.cartslidingbutton,
                R.drawable.filterbutton,
                R.drawable.cartbutton,
                R.drawable.blue_team
        };
        ViewAdapter(Context context){
            this.context=context;
        }
        @Override
        public int getCount() {
            return pics.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            int padding = context.getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(pics[position]);
            (container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            (container).removeView((ImageView) object);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerFragment.NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
        /*
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
                */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
        getData.cancel(true);
    }


    private class GetCategoriesAndSubCategoriesTask extends AsyncTask<String, Void, ArrayList<Category>> {
        protected ArrayList<Category> doInBackground(String... functions) {
            StringBuilder builder = new StringBuilder();

            for (String function : functions) {
                //create url from base url
                String url = MainMenuFragment.URL + function;
                //connect to server
                httpClient = new DefaultHttpClient();
                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;
                HttpPost httpPost = new HttpPost(url);
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
                            if(isCancelled()){
                                break;
                            }
                            builder.append(line);
                        }
                    } else {
                        Log.e("==>", "Failed to download file");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.i("HTTP Failed", e.toString());
                }
            }
            // Parse String to JSON object
            JSONArray jarray = null;
            try {
                jarray = new JSONArray(builder.toString());
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            ArrayList<Category> jsonlist = new ArrayList<Category>();
            //add data to jsonlist, in order to easily proccessing
            String uri = "http://dragomircristian.net/calin/assets/uploads/categories/";
            try {
                for (int i = 0; i < jarray.length(); i++) {
                    if(isCancelled()){
                        break;
                    }
                    try {
                        JSONObject c = jarray.getJSONObject(i);
                        String categoryId = c.getString(CATEGORY_ID);
                        String categoryName = c.getString(CATEGORY_NAME);
                        String photoURL = c.getString(PHOTO_URL);
                        if (photoURL.equals("null")) {
                            // photoURL = uri + "6e020-p1020283.jpg";
                        } else {
                            photoURL = uri + photoURL;
                        }
                        JSONArray subCategories = c.getJSONArray(SUBCATEGORIES);
                        SubCategory[] subCategoriesArray;
                        subCategoriesArray = new SubCategory[subCategories.length()];
                        for (int j = 0; j < subCategories.length(); j++) {
                            JSONObject c2 = subCategories.getJSONObject(j);
                            String id = c2.getString(SUBCATEGORY_ID);
                            String name = c2.getString(SUBCATEGORY_NAME);
                            SubCategory sC = new SubCategory(Integer.valueOf(id), name);
                            subCategoriesArray[j] = sC;
                        }
                        Category category = new Category(Integer.valueOf(categoryId), categoryName);
                        category.setPhotoURL(photoURL);
                        category.setSubCategories(subCategoriesArray);
                        jsonlist.add(category);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return jsonlist;
        }

        @Override
        protected void onPostExecute(ArrayList<Category> result) {
            categories = result;
            gridView.setAdapter(new GridViewContent(context, result));
        }
    }
}