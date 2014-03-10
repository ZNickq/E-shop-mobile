package com.MobShop.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String STATE_SELECTED_ITEM = "selected_navigation_drawer_item";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;
    public DefaultHttpClient httpClient;
    public WebApiModel api;

    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerExpandableListView;
    private View mFragmentContainerView;
    public RelativeLayout item;
    public LinearLayout subcategoryView;
    public View firstView;
    public Button backNavigationDrawer;
    public ImageButton cartSlidingButton;
    public ListView listSubcategory;

    public List<String> groupList;
    public ArrayList<HashMap<String, String>> childList;
    public Map<String, ArrayList<HashMap<String, String>>> menuCollection;
    public SubCategory[] subCategoriesArray;
    public ArrayList<HashMap<String, String>> pubCategories;

    private int mCurrentSelectedPosition = 0;
    private String menuItemSelected = "";
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    public boolean ok = true;
    public String groupName;
    public HashMap<String, String> category = null;
    public String categoryString;

    public static final String SUBCATEGORY_ID = "id";
    public static final String SUBCATEGORY_NAME = "name";
    public static String URL = "http://dragomircristian.net/calin/api/";

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            menuItemSelected = savedInstanceState.getString(STATE_SELECTED_ITEM);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(menuItemSelected, mCurrentSelectedPosition);
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
        /*
            This is the first view of the slide menu
            It inflates the relative layout, and checks the expandablelist for various clicks
            Also it checks the cart button activity
            Coded by: Calin Segarceanu: csegarceanu@gmail.com
        */

        firstView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        item = (RelativeLayout) firstView.findViewById(R.id.relativeLayoutNavigationDrawer);
        mDrawerExpandableListView = (ExpandableListView) item.findViewById(R.id.slideMenuExpandableListView);
        subcategoryView = (LinearLayout) firstView.findViewById(R.id.layout_drawer_subcategory);

        cartSlidingButton = (ImageButton) firstView.findViewById(R.id.cartSlidingButton);
        listSubcategory = (ListView) firstView.findViewById(R.id.listViewNavigationDrawer);
        createGroupList();
        try {
            createCollection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(getActivity(), groupList, menuCollection);
        mDrawerExpandableListView.setAdapter(expListAdapter);

        mDrawerExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                groupName = groupList.get(i);

                if (groupName.equals("Acasa")) {
                    mDrawerLayout.closeDrawers();
                    mCallbacks.onNavigationDrawerItemSelected("Acasa", 1);
                }else if(groupName.equals("Log in")){
                    mDrawerLayout.closeDrawers();
                    mCallbacks.onNavigationDrawerItemSelected("Log in", 6);
                }
                return false;
            }
        });

        mDrawerExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int group_position, int child_position, long id) {
                groupName = groupList.get(group_position);
                if (groupName.equals("Categorii")) {
                    category = pubCategories.get(child_position);
                    categoryString = category.get(WebApiModel.CATEGORY_NAME);
                    mDrawerExpandableListView.setVisibility(View.GONE);
                    //cartSlidingButton.setVisibility(View.GONE);
                    GetSubCategoriesTask getSub = new GetSubCategoriesTask();
                    getSub.execute(new String[]{"getsubcategorybyname"});
                    subcategoryView.setVisibility(View.VISIBLE);
                    backNavigationDrawer.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        backNavigationDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subcategoryView.setVisibility(View.GONE);
                backNavigationDrawer.setVisibility(View.GONE);
                mDrawerExpandableListView.setVisibility(View.VISIBLE);
                //cartSlidingButton.setVisibility(View.VISIBLE);
            }
        });

        listSubcategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubCategory sub = subCategoriesArray[position];
                mDrawerLayout.closeDrawers();
                mCallbacks.onNavigationDrawerItemSelected("Subcategories: " + sub.getSubCategoryName(), sub.getSubCategoryId());
            }
        });

        cartSlidingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onNavigationDrawerItemSelected("Cart", 7);
            }
        });
        return item;
    }

    private void createCollection() throws IOException {
        api = new WebApiModel("http://dragomircristian.net/calin/api/");
        ArrayList<HashMap<String, String>> categories =  api.getCategories("getallcategories");
        backNavigationDrawer = (Button) firstView.findViewById(R.id.backButtonNavigationDrawer);
        this.pubCategories = categories;

        menuCollection = new HashMap<String, ArrayList<HashMap<String, String>>>();

        for (String item : groupList) {
            childList = new ArrayList<HashMap<String, String>>();
            if (item.equals("Categorii")) {
                menuCollection.put(item, pubCategories);
            } else {
                menuCollection.put(item, childList);
            }
        }
        backNavigationDrawer.setVisibility(View.GONE);
        backNavigationDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subcategoryView.setVisibility(View.GONE);
                mDrawerExpandableListView.setVisibility(View.VISIBLE);
                cartSlidingButton.setVisibility(View.VISIBLE);
            }
        });

    }

    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add("Acasa"); //1
        groupList.add("Categorii"); //2
        groupList.add("Cont"); //3
        groupList.add("Comenzi"); //4
        groupList.add("Harta"); //5
        User user = new User();
        boolean loggedIn = user.getLoggedIn();
        Log.d("URL", String.valueOf(loggedIn));
        if(loggedIn == true){
            groupList.add("Log out"); //8
        }else {
            groupList.add("Log in"); //6
        }
    }


    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(String menuItem, int position) {
        mCurrentSelectedPosition = position;
        menuItemSelected = menuItem;
        if (mDrawerExpandableListView != null) {
            mDrawerExpandableListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(menuItem, position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return getActivity().getActionBar();
    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(String item, int position);
    }


    private class GetSubCategoriesTask extends AsyncTask<String, Void, ArrayList<SubCategory>> {
        @Override
        protected ArrayList<SubCategory> doInBackground(String... functions) {
            StringBuilder builder = new StringBuilder();

            for (String function : functions) {
                //create url from base url
                String url = NavigationDrawerFragment.URL + function;
                //connect to server
                httpClient = new DefaultHttpClient();
                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;
                HttpPost httpPost = new HttpPost(url);

                try {
                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("category", categoryString));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    try {
                        httpResponse = httpClient.execute(httpPost);
                        StatusLine statusLine = httpResponse.getStatusLine();
                        int statusCode = statusLine.getStatusCode();
                        //if ok get data from server
                        if (statusCode == 200) {
                            httpEntity = httpResponse.getEntity();
                            InputStream content = httpEntity.getContent();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                            String line = "";
                            while ((line = reader.readLine()) != null) {
                                builder.append(line);
                            }
                        } else {
                            Log.e("==>", "Failed to download file");
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.i("HTTP Failed", e.toString());
                }
            }
            // Parse String to JSON object
            JSONArray jarray = null;
            try {
                jarray = new JSONArray(builder.toString());
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            ArrayList<SubCategory> jsonlist = new ArrayList<SubCategory>();
            //add data to jsonlist, in order to easily proccessing
            try {
                for (int i = 0; i < jarray.length(); i++) {

                    try {
                        JSONObject c = jarray.getJSONObject(i);
                        String id = c.getString(SUBCATEGORY_ID);
                        String name = c.getString(SUBCATEGORY_NAME);
                        SubCategory sC = new SubCategory(Integer.valueOf(id), name);
                        jsonlist.add(sC);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return jsonlist;
        }

        @Override
        protected void onPostExecute(ArrayList<SubCategory> result) {
            subCategoriesArray = new SubCategory[result.size()];
            subCategoriesArray = result.toArray(subCategoriesArray);
            ListViewNavigationDrawerAdapter listViewAdapter = new ListViewNavigationDrawerAdapter(getActivity(), R.layout.list_view_subcategory_row, subCategoriesArray);
            listSubcategory.setAdapter(listViewAdapter);
        }
    }

}

