package com.MobShop.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class SharedPreferencesExecutor<Cart> {

    private Context context;

    public SharedPreferencesExecutor(Context context) {
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
        return gson.fromJson(json, clazz);
    }

    public void delete(String key) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        appSharedPrefs.edit().remove(key).commit();
    }
}
