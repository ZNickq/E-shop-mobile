package com.MobShop.app.products;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.MobShop.app.R;
import com.MobShop.app.models.Product;
import com.MobShop.app.util.download.ImageLoader;

public class ListViewProductsAdapter extends ArrayAdapter<Product> {

    public Context ctxt = null;
    public Product[] data = null;
    public int layoutResourceId;
    ImageLoader imgLoader;
    int loader = R.drawable.loader;


    public ListViewProductsAdapter(Context context, int resource, Product[] objects) {
        super(context, resource, objects);
        this.ctxt = context;
        this.data = objects;
        this.layoutResourceId = resource;
        imgLoader = new ImageLoader(ctxt);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) ctxt).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        Product objectItem;
        objectItem = data[position];
        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.listViewProductSubCategoryTextView);
        TextView descriptionViewItem = (TextView) convertView.findViewById(R.id.listViewProductSubCategoryDescription);
        TextView priceViewItem = (TextView) convertView.findViewById(R.id.listViewProductsSubCategoryPrice);
        ImageView img = (ImageView) convertView.findViewById(R.id.listViewProductSubCategoryImageView);
        String name = objectItem.getProductName();
        String description = objectItem.getDescription();
        Double price = objectItem.getPrice();

        String URL = objectItem.getProductPhotoURL();
        if (URL.equals("null")) {
            img.setImageResource(R.drawable.ic_launcher);
        } else {
            imgLoader.SetImage(URL, loader, img);
        }
        textViewItem.setText(name);
        descriptionViewItem.setText(Html.fromHtml(description));
        priceViewItem.setText("Price: " + price + " lei");

        return convertView;
    }
}
