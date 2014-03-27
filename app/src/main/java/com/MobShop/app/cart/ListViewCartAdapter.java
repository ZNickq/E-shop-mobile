package com.MobShop.app.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.MobShop.app.R;
import com.MobShop.app.models.Cart;
import com.MobShop.app.models.Product;
import com.MobShop.app.navdrawer.NavigationDrawerFragment;
import com.MobShop.app.util.SharedPreferencesExecutor;
import com.MobShop.app.util.download.ImageLoader;

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
            LayoutInflater inflater = (LayoutInflater) ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        Product objectItem;
        objectItem = data[position];
        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.listViewCartPageTextView);
        TextView priceViewItem = (TextView) convertView.findViewById(R.id.listViewCartPagePrice);
        ImageView img = (ImageView) convertView.findViewById(R.id.listViewCartPageImageView);
        ImageButton delete = (ImageButton) convertView.findViewById(R.id.deleteProductCartPage);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesExecutor<Cart> cartSharedPreferencesExecutor = new SharedPreferencesExecutor<Cart>(ctxt);
                Cart cart = cartSharedPreferencesExecutor.retreive("eshop", Cart.class);
                cart.deleteProduct(position);
                cartSharedPreferencesExecutor.save("eshop", cart);
                mCallBack.onNavigationDrawerItemSelected("Cart", 7);
            }
        });
        String name = objectItem.getProductName();
        int quantity = objectItem.getProductQuantity();
        Double price = objectItem.getPrice();

        String URL = objectItem.getProductPhotoURL();

        if (!URL.equals("null")) {
            imgLoader.SetImage(URL, loader, img);
        }


        textViewItem.setText(name);
        //quantityTextView.setText(quantity);
        priceViewItem.setText("Price: " + quantity * price + " lei");

        return convertView;
    }

}
