package com.MobShop.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Segarceanu Calin on 2/25/14.
 */
public class ListViewProductsAdapter extends ArrayAdapter<Product> {

    public Context ctxt = null;
    public Product[] data = null;
    public int layoutResourceId;

    public ListViewProductsAdapter(Context context, int resource, Product[] objects){
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

        // object item based on the position
        Product objectItem = null;
        objectItem = data[position];
        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.listViewProductSubCategoryTextView);
        String name = objectItem.getProductName();
        textViewItem.setText(name);

        return convertView;
    }
}
