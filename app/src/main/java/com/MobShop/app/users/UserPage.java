package com.MobShop.app.users;

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
import android.widget.EditText;
import android.widget.Toast;

import com.MobShop.app.R;
import com.MobShop.app.models.User;
import com.MobShop.app.navdrawer.NavigationDrawerFragment;

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

public class UserPage extends Fragment {
    public Context ctxt, rootViewContext;
    public NavigationDrawerFragment.NavigationDrawerCallbacks mCallbacks;
    public EditText emailTextView, nameEditText, surNameEditText, cityEditText, districtEditText, addressEditText, phoneNumberEditText;

    public UserPage() {

    }

    public static UserPage newInstance(Context context) {
        UserPage fragment = new UserPage();
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
        View rootView = inflater.inflate(R.layout.user_page, container, false);
        rootViewContext = rootView.getContext();
        emailTextView = (EditText) rootView.findViewById(R.id.userPageEmail);

        nameEditText = (EditText) rootView.findViewById(R.id.userPageName);
        surNameEditText = (EditText) rootView.findViewById(R.id.userPageSurName);

        cityEditText = (EditText) rootView.findViewById(R.id.userPageCity);
        districtEditText = (EditText) rootView.findViewById(R.id.userPageDistrict);

        addressEditText = (EditText) rootView.findViewById(R.id.userPageAddress);
        phoneNumberEditText = (EditText) rootView.findViewById(R.id.userPagePhone);

        Button update = (Button) rootView.findViewById(R.id.updateUserPageButton);

        emailTextView.setKeyListener(null);
        User user = new User();
        emailTextView.setText(user.getEmail());
        nameEditText.setText(user.getName());
        surNameEditText.setText(user.getSurname());
        cityEditText.setText(user.getCity());
        districtEditText.setText(user.getDistrict());
        addressEditText.setText(user.getAddress());
        phoneNumberEditText.setText(user.getPhoneNumber());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(rootViewContext)
                        .setTitle("Schimbari cont")
                        .setMessage("Sunteti sigur ca doriti sa modificat datele contului?")
                        .setPositiveButton(R.string.yes_user, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String e = emailTextView.getText().toString();
                                Log.d("URL", e);
                                String n = nameEditText.getText().toString();
                                String s = surNameEditText.getText().toString();

                                String c = cityEditText.getText().toString();
                                String d = districtEditText.getText().toString();
                                String a = addressEditText.getText().toString();
                                String ph = phoneNumberEditText.getText().toString();
                                if (e.contains("@") && n.length() >= 4 && s.length() >= 4 && c.length() >= 4 && d.length() >= 4 && a.length() >= 4 && ph.length() >= 4) {
                                    UpdateTask sendData = new UpdateTask();
                                    sendData.execute("updateuser", e, n, s, c, d, a, ph);
                                } else {
                                    Toast toast = Toast.makeText(rootViewContext, "Va rugam completati formularul cu responsabilitate", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.no_login, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
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
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    private class UpdateTask extends AsyncTask<String, Void, String> {
        private ProgressDialog dialog;

        protected void onPreExecute() {
            this.dialog = new ProgressDialog(rootViewContext);
            this.dialog.setMessage("Updating");
            this.dialog.show();
        }

        protected String doInBackground(String... functions) {
            String function = functions[0];
            String email = functions[1];

            String name = functions[2];
            String surName = functions[3];

            String city = functions[4];
            String district = functions[5];

            String address = functions[6];
            String phoneNumber = functions[7];

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
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("surname", surName));
                nameValuePairs.add(new BasicNameValuePair("email", email));

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
            User user = new User();
            user.setName(name);
            user.setSurname(surName);


            user.setAddress(address);
            user.setCity(city);
            user.setDistrict(district);

            user.setPhoneNumber(phoneNumber);
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result.equals("1")) {
                Toast toast = Toast.makeText(rootViewContext, "Updatat cu succes", Toast.LENGTH_LONG);
                toast.show();
                mCallbacks.onNavigationDrawerItemSelected("Cont", 3);
            }
        }
    }
}
