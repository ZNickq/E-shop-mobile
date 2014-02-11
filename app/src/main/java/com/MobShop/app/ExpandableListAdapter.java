package com.MobShop.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Activity context;
    private Map<String, List<String>> menuCollection;
    private List<String> groupParent;

    public ExpandableListAdapter(Activity context, List<String> groupParent, Map<String, List<String>> menuCollection) {
        this.context = context;
        this.menuCollection = menuCollection;
        this.groupParent = groupParent;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return menuCollection.get(groupParent.get(groupPosition)).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String laptop = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_item_navigation_drawer, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.childNavigationDrawer);
        item.setText(laptop);

        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        int var;
        try{
            var = menuCollection.get(groupParent.get(groupPosition)).size();
        }catch(NullPointerException npe){
            var = 0;
        }
        return var;
    }

    public Object getGroup(int groupPosition) {
        return groupParent.get(groupPosition);
    }

    public int getGroupCount() {
        return groupParent.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item_navigation_drawer, null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.parentNavigationDrawer);
       // item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
