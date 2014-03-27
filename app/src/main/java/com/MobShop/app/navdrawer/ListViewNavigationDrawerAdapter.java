package com.MobShop.app.navdrawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.MobShop.app.R;
import com.MobShop.app.models.SubCategory;

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
        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) ctxt).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        // object item based on the position
        SubCategory objectItem = data[position];
        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.listViewSubCategoryTextView);
        String str = objectItem.getSubCategoryName();
        textViewItem.setText(objectItem.getSubCategoryName());
        textViewItem.setTag(objectItem.getSubCategoryId());

        return convertView;
    }

}
