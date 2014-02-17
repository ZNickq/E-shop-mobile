package com.MobShop.app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Segarceanu Calin on 2/16/14.
 */
public class ListViewNavigationDrawerAdapter extends ArrayAdapter<SubCategory> {

    public Context ctxt = null;
    public SubCategory[] data = null;
    public int layoutResourceId;

    public ListViewNavigationDrawerAdapter(Context context, int resource, SubCategory[] objects) {
        super(context, resource, objects);
        this.ctxt = context;
        this.data = objects;
        this.layoutResourceId = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) ctxt).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        Log.d("URL", "1");
        // object item based on the position
        SubCategory objectItem = data[position];
        Log.d("URL", "2");
        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.listViewSubCategoryTextView);
        String str = objectItem.getSubCategoryName();
        Log.d("URL", str);
        textViewItem.setText(objectItem.getSubCategoryName());
        textViewItem.setTag(objectItem.getSubCategoryId());

        return convertView;
    }

}
