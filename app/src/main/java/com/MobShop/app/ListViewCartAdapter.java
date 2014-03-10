package com.MobShop.app;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Segarceanu Calin on 3/10/14.
 */
public class ListViewCartAdapter extends ArrayAdapter<Product> {
    public Context ctxt = null;
    public NavigationDrawerFragment.NavigationDrawerCallbacks mCallBack;
    public Product[] data = null;
    public int layoutResourceId;
    ImageLoader imgLoader;
    int loader = R.drawable.loader;


    public ListViewCartAdapter(Context context, int resource, Product[] objects, NavigationDrawerFragment.NavigationDrawerCallbacks callBack) {
        super(context, resource, objects);
        this.ctxt = context;
        this.data = objects;
        this.layoutResourceId = resource;
        imgLoader = new ImageLoader(ctxt);
        this.mCallBack = callBack;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        Product objectItem = null;
        objectItem = data[position];
        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.listViewCartPageTextView);
        TextView descriptionViewItem = (TextView) convertView.findViewById(R.id.listViewCartPageDescription);
        TextView priceViewItem = (TextView) convertView.findViewById(R.id.listViewCartPagePrice);
        ImageView img = (ImageView) convertView.findViewById(R.id.listViewCartPageImageView);
        Button delete = (Button) convertView.findViewById(R.id.deleteProductCartPage);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPerferencesExecutor<Cart> cartSharedPerferencesExecutor = new SharedPerferencesExecutor<Cart>(ctxt);
                Cart cart = cartSharedPerferencesExecutor.retreive("eshop", Cart.class);
                cart.deleteProduct(position);
                cartSharedPerferencesExecutor.save("eshop", cart);
                mCallBack.onNavigationDrawerItemSelected("Cart", 7);
            }
        });
        String name = objectItem.getProductName();
        String description = objectItem.getDescription();
        Double price = objectItem.getPrice();

        String URL = objectItem.getProductPhotoURL();
        if (URL.equals("null")) {
            img.setImageResource(R.drawable.ic_launcher);
        } else {
            //imgLoader.SetImage(URL, loader, img);
        }
        textViewItem.setText(name);
        descriptionViewItem.setText(Html.fromHtml(description));
        priceViewItem.setText("Price: "+price+" lei");

        return convertView;
    }
}
