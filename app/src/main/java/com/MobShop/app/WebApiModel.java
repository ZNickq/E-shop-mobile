package com.MobShop.app;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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
import java.util.HashMap;
import java.util.List;

/**
 * Created by Segarceanu Calin on 2/12/14.
 */
public class WebApiModel {

    public static String URL = "http://dragomircristian.net/calin/api/";
    private HttpClient client;
    public InputStream iStream = null;
    public JSONArray jarray = null;
    public String json = "";
    public static final String CATEGORY_ID = "id";
    public static final String CATEGORY_NAME = "name";
    public static final String SUBCATEGORY_ID = "id";
    public static final String SUBCATEGORY_NAME = "name";

    public DefaultHttpClient httpClient;

    public ArrayList<HashMap<String, String>> jsonGetCategoriesList = new ArrayList<HashMap<String, String>>();;

    public WebApiModel(String URL ){
        this.URL = URL;
    }

    public ArrayList<SubCategory> getSubCategories(String function, String categoryString){
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //create url from base url
        String url = this.URL + function;
        StringBuilder builder = new StringBuilder();

        //connect to server
        //DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpEntity httpEntity = null;
        HttpResponse httpResponse = null;
        HttpPost httpPost = new HttpPost(url);

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("category", categoryString));
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
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i("HTTP Failed", e.toString());
        }
        // Parse String to JSON object
        try {
            jarray = new JSONArray( builder.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
       ArrayList<SubCategory> jsonlist = new ArrayList<SubCategory>();
        try{
        //add data to jsonlist, in order to easily proccessing
        for (int i = 0; i < jarray.length(); i++) {
            try {
                JSONObject c = jarray.getJSONObject(i);
                String id = c.getString(SUBCATEGORY_ID);
                String name = c.getString(SUBCATEGORY_NAME);
                SubCategory sC = new SubCategory(Integer.valueOf(id), name);
                jsonlist.add(sC);
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



    public ArrayList<HashMap<String, String>> getCategories(String function) throws IOException {

        GetCategoriesTask task = new  GetCategoriesTask();
        task.execute(new String[] { function});

        return jsonGetCategoriesList;
    }

    private class GetCategoriesTask extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {
        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... functions) {
            StringBuilder builder = new StringBuilder();

            for (String function : functions) {
                //create url from base url
                String url = WebApiModel.URL + function;

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
                            builder.append(line);
                        }
                    } else {
                        Log.e("==>", "Failed to download file");
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // Parse String to JSON object
            try {
                jarray = new JSONArray( builder.toString());
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();
            //add data to jsonlist, in order to easily proccessing
            try{
                for (int i = 0; i < jarray.length(); i++) {

                    try {
                        JSONObject c = jarray.getJSONObject(i);
                        String id = c.getString(CATEGORY_ID);
                        String name = c.getString(CATEGORY_NAME);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(CATEGORY_ID, id);
                        map.put(CATEGORY_NAME, name);
                        jsonlist.add(map);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            for(HashMap<String, String> map : jsonlist){
                jsonGetCategoriesList.add(map);
            }


            //Collections.copy(jsonGetCategoriesList, jsonlist);

            return jsonlist;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {

        }
    }
}