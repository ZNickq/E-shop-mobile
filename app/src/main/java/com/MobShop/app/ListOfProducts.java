package com.MobShop.app;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Segarceanu Calin on 2/24/14.
 */
public class ListOfProducts extends Fragment{

    private static final String ARG_SUB_CATEGORY_NUMBER = "section_number";
    private static final String ARG_SUB_CATEGORY_NAME = "section_name";

    private int mSubCategoryID = 0;
    private String mSubCategoryName = "";
    private boolean mFromSavedInstanceState;

    public ListOfProducts(){

    }

    public static ListOfProducts newInstance(int subCategoryNumber, String subCategoryName) {
        ListOfProducts fragment = new ListOfProducts();
        Bundle args = new Bundle();
        args.putInt(ARG_SUB_CATEGORY_NUMBER, subCategoryNumber);
        args.putString(ARG_SUB_CATEGORY_NAME, subCategoryName);
        fragment.setArguments(args);

        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());


        if (savedInstanceState != null) {
            mSubCategoryID = savedInstanceState.getInt(ARG_SUB_CATEGORY_NUMBER);
            mSubCategoryName = savedInstanceState.getString(ARG_SUB_CATEGORY_NAME);
            mFromSavedInstanceState = true;
        }
        setUp();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_of_products_by_subcategory, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getArguments().getString(ARG_SUB_CATEGORY_NAME));
        return rootView;
    }

    public void setUp(){
        ActionBar actionBar = getActionBar();

    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

}
