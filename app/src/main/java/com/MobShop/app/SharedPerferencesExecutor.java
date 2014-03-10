package com.MobShop.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Created by Segarceanu Calin on 3/10/14.
 */
public class SharedPerferencesExecutor<Cart> {

    private Context context;

    public SharedPerferencesExecutor(Context context) {
        this.context = context;
    }

    public void save(String key, Cart sharedPerferencesEntry) {

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(sharedPerferencesEntry);
        prefsEditor.putString(key, json);
        prefsEditor.commit();

    }

    public Cart retreive(String key, Class<Cart> clazz) {

        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());

        Gson gson = new Gson();
        String json = appSharedPrefs.getString(key, "");
        return (Cart) gson.fromJson(json, clazz);
    }

    public void delete(String key){
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        appSharedPrefs.edit().clear().commit();
    }
}
