package com.MobShop.app.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.MobShop.app.R;
import com.MobShop.app.models.Category;
import com.MobShop.app.util.download.ImageLoader;

import java.util.ArrayList;

public class ListViewDashAdapter extends BaseAdapter {
    public ArrayList<Category> categories; //Original
    public ArrayList<Category> fitems;
    public ArrayList<Category> refitems;
    public Filter filter;
    int loader = R.drawable.loader;
    ImageLoader imgLoader;
    private Context context;

    public ListViewDashAdapter(Context c, ArrayList<Category> data) {

        context = c;
        imgLoader = new ImageLoader(c);
        this.categories = data;
        this.fitems = new ArrayList<Category>(data);
        this.refitems = new ArrayList<Category>(data);

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
        Category category = categories.get(position);
        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(context);

            convertView = li.inflate(R.layout.list_view_dash_row, null);
        }
        ImageView categoryImage = (ImageView) convertView.findViewById(R.id.imageListViewDash);
        TextView categoryText = (TextView) convertView.findViewById(R.id.categoryListViewDash);

        String URL = category.getPhotoURL();
        if (URL.equals("null")) {
            categoryImage.setImageResource(R.drawable.ic_launcher);
        } else {
            imgLoader.SetImage(URL, loader, categoryImage);
        }
        categoryText.setText(category.getCategoryName());
        return convertView;
    }


    public Filter getFilter() {
        if (filter == null)
            filter = new SearchFilter();

        return filter;
    }

    public void add(Category item) {
        categories.add(item);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    private class SearchFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            String prefix = constraint.toString().toLowerCase();

            if (prefix == null || prefix.length() == 0) {

                ArrayList<Category> ArrayList = new ArrayList<Category>(refitems);
                results.values = ArrayList;
                results.count = ArrayList.size();
            } else {

                final ArrayList<Category> myList = new ArrayList<Category>(refitems);

                int count = myList.size();
                final ArrayList<Category> nArrayList = new ArrayList<Category>(count);

                for (Category pkmn : myList) {
                    final String value = pkmn.toString().toLowerCase();

                    if (value.startsWith(prefix)) {
                        nArrayList.add(pkmn);
                    }
                }
                results.values = nArrayList;
                results.count = nArrayList.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            fitems = (ArrayList<Category>) results.values;
            categories.clear();
            for (Category pkmn : fitems) {
                add(pkmn);
            }

            if (fitems.size() > 0)
                notifyDataSetChanged();
            else {
                categories = new ArrayList<Category>(refitems);
                notifyDataSetInvalidated();
            }
        }

    }
}


