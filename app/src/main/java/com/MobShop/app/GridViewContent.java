package com.MobShop.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Glontz on 2/24/14.
 */
public class GridViewContent extends BaseAdapter {

    private Context context;
    int loader = R.drawable.loader;
    ImageLoader imgLoader;
    public ArrayList<Category> categories;

    public GridViewContent(Context c, ArrayList<Category> data) {

        context = c;
        imgLoader = new ImageLoader(c);
        this.categories = data;

    }

    @Override
    public int getCount() {

        // TODO Auto-generated method stub
        return (categories.size());
    }

    @Override
    public Object getItem(int position) {

        return categories.get(position);
    }

    @Override
    public long getItemId(int arg0) {

        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        Category category = categories.get(position);
        if (convertView == null) {

            LayoutInflater li = LayoutInflater.from(context);
            v = li.inflate(R.layout.grid_view_row, null);
            ImageView categoryImage = (ImageView) v.findViewById(R.id.imageGridViewContent);
            TextView categoryText = (TextView) v.findViewById(R.id.categoryGridViewContent);

            ImageView imageView = new ImageView(context);

            String URL = categories.get(position).getPhotoURL();
            if (URL.equals("null")) {
                categoryImage.setImageResource(R.drawable.ic_launcher);
            } else {
                imgLoader.SetImage(URL, loader, categoryImage);
            }
            categoryText.setText(category.getCategoryName());

        } else {
            v = convertView;
        }
        return v;
    }
}
