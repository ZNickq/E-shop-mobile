package com.MobShop.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

/**
 * Created by Segarceanu Calin on 3/10/14.
 */
public class Register extends Fragment {
    public Context ctxt;
    public Context rootViewContext;
    private NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;

    public Register(){

    }

    public static Register newInstance(Context context) {
        Register fragment = new Register();
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
        View rootView = inflater.inflate(R.layout.register_page, container, false);
        Button registerButton = (Button) rootView.findViewById(R.id.registerPageRegister);
        Button resetButton = (Button) rootView.findViewById(R.id.registerPageReset);

        final EditText emailEditText = (EditText) rootView.findViewById(R.id.registerPageEmail);
        final EditText passwordEditText = (EditText) rootView.findViewById(R.id.registerPagePassword);

        final EditText nameEditText = (EditText) rootView.findViewById(R.id.registerPageName);
        final EditText surNameEditText = (EditText) rootView.findViewById(R.id.registerPageSurName);

        final EditText cityEditText = (EditText) rootView.findViewById(R.id.registerPageCity);
        final EditText districtEditText = (EditText) rootView.findViewById(R.id.registerPageDistrict);

        final EditText addressEditText = (EditText) rootView.findViewById(R.id.registerPageAddress);
        final EditText phoneNumberEditText = (EditText) rootView.findViewById(R.id.registerPagePhone);

        rootViewContext = rootView.getContext();

        String email, password;
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = emailEditText.getText().toString();
                String p = passwordEditText.getText().toString();
                String n = nameEditText.getText().toString();
                String s = surNameEditText.getText().toString();

                String c = cityEditText.getText().toString();
                String d = districtEditText.getText().toString();
                String a = addressEditText.getText().toString();
                String ph = phoneNumberEditText.getText().toString();
                if(e.contains("@") && p.length() >= 4 && n.length() >= 4 && s.length() >= 4 && c.length() >= 4 && d.length() >= 4 && a.length() >= 4 && ph.length() >= 4 ) {
                    RegisterTask sendData = new RegisterTask();
                    sendData.execute(new String[]{"registeruser", e, p, n, s, c, d, a, ph});
                }else{
                    Toast toast = Toast.makeText(rootViewContext, "Va rugam completati formularul cu responsabilitate", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailEditText.setText("");
                passwordEditText.setText("");
                nameEditText.setText("");
                surNameEditText.setText("");
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
            throw new ClassCastException("Activity must implement ListOfProductsCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private class RegisterTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;

        protected void onPreExecute() {
            this.dialog = new ProgressDialog(rootViewContext);
            this.dialog.setMessage("Registering");
            this.dialog.show();
        }

        protected String doInBackground(String... functions) {
            String function = functions[0];
            String email = functions[1];
            String password = null;
            try {
                password = SimpleCrypto.md5(functions[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String name = functions[3];
            String surName = functions[4];

            String city = functions[5];
            String district = functions[6];

            String address = functions[7];
            String phoneNumber = functions[8];

            StringBuilder builder = new StringBuilder();
            //create url from base url
            String url = NavigationDrawerFragment.URL + function;
            //connect to server
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
            HttpPost httpPost = new HttpPost(url);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("surname", surName));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("password", password));

                nameValuePairs.add(new BasicNameValuePair("city", city));
                nameValuePairs.add(new BasicNameValuePair("district", district));
                nameValuePairs.add(new BasicNameValuePair("address", address));
                nameValuePairs.add(new BasicNameValuePair("phoneNumber", phoneNumber));

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
            if(result.equals("1")){
                Toast toast = Toast.makeText(rootViewContext, "Inregstrat cu succes", Toast.LENGTH_LONG);
                toast.show();
            }else{
                Toast toast = Toast.makeText(rootViewContext, "Adresa de email este in uz", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
