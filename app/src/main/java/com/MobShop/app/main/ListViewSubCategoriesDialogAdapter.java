package com.MobShop.app.main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.MobShop.app.R;
import com.MobShop.app.models.SubCategory;

public class ListViewSubCategoriesDialogAdapter extends ArrayAdapter<SubCategory> {
    public Context ctxt = null;
    public SubCategory[] data = null;
    public int layoutResourceId;


    public ListViewSubCategoriesDialogAdapter(Context context, int resource, SubCategory[] objects) {
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
        SubCategory objectItem;
        objectItem = data[position];
        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.listViewSubCategoriesDialogTextView);
        String name = objectItem.getSubCategoryName();
        textViewItem.setText(name);

        return convertView;
    }
}
