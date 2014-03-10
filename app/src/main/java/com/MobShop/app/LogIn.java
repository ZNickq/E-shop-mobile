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
public class LogIn extends Fragment {

    public Context ctxt;
    public Context rootViewContext;
    private NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;

    public LogIn(){

    }

    public static LogIn newInstance(Context context) {
        LogIn fragment = new LogIn();
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
        View rootView = inflater.inflate(R.layout.log_in_page, container, false);
        Button registerButton = (Button) rootView.findViewById(R.id.logInPageRegister);
        Button loginButton = (Button) rootView.findViewById(R.id.logInPageLogin);
        final EditText emailEditText = (EditText) rootView.findViewById(R.id.logInPageEmail);
        final EditText passwordEditText = (EditText) rootView.findViewById(R.id.logInPagePassword);
        String email, password;
        rootViewContext = rootView.getContext();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = emailEditText.getText().toString();
                String p = passwordEditText.getText().toString();
                LoginTask sendData = new  LoginTask();
                sendData.execute(new String[]{"loginuser", e, p});
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onNavigationDrawerItemSelected("Register", 9);
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

    private class LoginTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;

        protected void onPreExecute() {
            this.dialog = new ProgressDialog(rootViewContext);
            this.dialog.setMessage("Log In");
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
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("password", password));
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
                Toast toast = Toast.makeText(rootViewContext, "Logat cu succes", Toast.LENGTH_LONG);
                toast.show();
            }else{
                Toast toast = Toast.makeText(rootViewContext, "Nu am putut gasi datele furnizate", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
