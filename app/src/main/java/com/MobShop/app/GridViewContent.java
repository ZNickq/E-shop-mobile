package com.MobShop.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Glontz on 2/24/14.
 */
public class GridViewContent extends BaseAdapter {

    private Context context;
    int loader = R.drawable.loader;
    ImageLoader imgLoader;


    public String[] gv_fill = {
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg",
            "http://www.bugetulpersonal.ro/blog/wp-content/uploads/2011/06/categorii-de-cheltuieli41-300x240.jpg"

    };

    public GridViewContent(Context c){

        context = c;
        imgLoader = new ImageLoader( c );
    }

    @Override
    public int getCount() {

        // TODO Auto-generated method stub
        return (gv_fill.length);
    }

    @Override
    public Object getItem( int position ){

        return gv_fill[position];
    }

    @Override
    public long getItemId(int arg0) {

        // TODO Auto-generated method stub
        return 0;
    }



    @Override
    public View getView( int position, View arg1, ViewGroup arg2){

        ImageView imageView = new ImageView(context);

        imgLoader.SetImage(gv_fill[position], loader, imageView);

        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(new GridView.LayoutParams(350, 200));

        return imageView;
    }

}
