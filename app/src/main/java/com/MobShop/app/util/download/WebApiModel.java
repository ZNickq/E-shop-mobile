package com.MobShop.app.util.download;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Segarceanu Calin on 2/12/14.
 */
public class WebApiModel {

    public static final String CATEGORY_ID = "id";
    public static final String CATEGORY_NAME = "name";
    public static String URL = "http://dragomircristian.net/calin/api/";
    public InputStream iStream = null;
    public String json = "";
    public DefaultHttpClient httpClient;
    public ArrayList<HashMap<String, String>> jsonGetCategoriesList = new ArrayList<HashMap<String, String>>();
    private HttpClient client;

    public WebApiModel(String URL) {
        this.URL = URL;
    }

    public ArrayList<HashMap<String, String>> getCategories(String function) throws IOException {

        GetCategoriesTask task = new GetCategoriesTask();
        task.execute(new String[]{function});

        return jsonGetCategoriesList;
    }

    private class GetCategoriesTask extends AsyncTask<String, Void, ArrayList<HashMap<String, String>>> {
        ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();

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
            JSONArray jarray = null;
            try {
                jarray = new JSONArray(builder.toString());
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();
            //add data to jsonlist, in order to easily proccessing
            try {
                for (int i = 0; i < jarray.length(); i++) {

                    try {
                        JSONObject c = jarray.getJSONObject(i);
                        String id = c.getString(CATEGORY_ID);
                        String name = c.getString(CATEGORY_NAME);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(CATEGORY_ID, id);
                        map.put(CATEGORY_NAME, name);
                        jsonlist.add(map);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            for (HashMap<String, String> map : jsonlist) {
                jsonGetCategoriesList.add(map);
            }

            return jsonlist;
        }
        //add data to jsonlist, in order to easily proccess it
    }
}