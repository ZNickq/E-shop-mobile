package com.MobShop.app;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Segarceanu Calin on 2/16/14.
 */
public class ListViewNavigationDrawerAdapter extends ArrayAdapter<ArrayList<HashMap<String, String>>> {

    public ListViewNavigationDrawerAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);
    }
}
