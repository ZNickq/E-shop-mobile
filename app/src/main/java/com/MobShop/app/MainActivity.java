package com.MobShop.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import java.lang.reflect.Field;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, ListOfProducts.ListOfProductsCallbacks, LogIn.LogInCallbacks {

    public static Bitmap originalScreen;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    public DrawerLayout mDrawerLayout;

    public Menu mymenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean webConnection = isInternetConnection();
        if (webConnection == false) {
            //In case that there isn't any internet connection, it displays a message and retry button
            setContentView(R.layout.internet_connection);

            Button retry = (Button) findViewById(R.id.retryInternetConnection);

            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, MainActivity.class);

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(i);
                }
            });

        } else {
            setContentView(R.layout.activity_main);

            View mainView = findViewById(android.R.id.content);
            mainView = mainView.getRootView();

            mainView.refreshDrawableState();
            mainView.setDrawingCacheEnabled(true);
            mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
            //mTitle = getTitle();
            mTitle = "Dashbord";
            // Set up the drawer.
            mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            setDrawerLeftEdgeSize(this, mDrawerLayout, 0.1f);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, MainMenuFragment.newInstance(1))
                    .commit();
        }
    }

    private boolean isInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void setDrawerLeftEdgeSize(Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null)
            return;

        try {
            // find ViewDragHelper and set it accessible
            Field leftDraggerField = drawerLayout.getClass().getDeclaredField("mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);
            // find edgesize and set is accessible
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);
            // set new edgesize
            Point displaySize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (displaySize.x * displayWidthPercentage)));
        } catch (NoSuchFieldException e) {
            // ignore
        } catch (IllegalArgumentException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(String menuItem, int position) {
        // update the main content by replacing fragments
        if (menuItem.contains("Subcategories")) {
            StringBuilder subCategory = new StringBuilder();
            subCategory.append(menuItem);
            int start, end;
            menuItem = "Subcategories: ";
            start = subCategory.indexOf(menuItem);
            end = start + menuItem.length();
            subCategory.replace(start, end, "");
            String categoryToWeb = "";
            categoryToWeb = subCategory.toString();
            mTitle = categoryToWeb;
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ListOfProducts.newInstance(position, categoryToWeb, getApplicationContext()))
                    .addToBackStack(null)
                    .commit();
        } else {
            Intent i;
            FragmentManager fragmentManager;
            switch (position) {
                case 1:
                    i = new Intent(MainActivity.this, MainActivity.class);

                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(i);
                    break;

                case 6:
                    //Login
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, LogIn.newInstance(getApplicationContext()))
                            .addToBackStack(null)
                            .commit();
                    break;
                case 7:
                    //CartPage
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, CartPage.newInstance(getApplicationContext()))
                            .addToBackStack(null)
                            .commit();
                    break;
                case 8:
                     //Log out
                    break;
                case 9:
                    //Register
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, Register.newInstance(getApplicationContext()))
                            .addToBackStack(null)
                            .commit();
                    break;
            }
        }
    }
    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }
    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(mTitle);
        actionBar.setLogo(R.drawable.logodash);
        actionBar.setIcon(R.drawable.arrow);
        mymenu.findItem(R.id.action_list).setVisible(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            if (!mNavigationDrawerFragment.isDrawerOpen()) {
                // Only show items in the action bar relevant to this screen
                // if the drawer is not showing. Otherwise, let the drawer
                // decide what to show in the action bar.
                getMenuInflater().inflate(R.menu.main, menu);

                mymenu = menu;

                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


                restoreActionBar();
                return true;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent i;
        switch (item.getItemId()){

            case R.id.action_search:

                return true;

            case R.id.action_cart:
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, CartPage.newInstance(getApplicationContext()))
                        .addToBackStack(null)
                        .commit();
                return true;

            case R.id.action_filter:

                return true;

            case R.id.action_list:
                item.setVisible(false);
                mymenu.findItem(R.id.action_grid).setVisible(true);
                findViewById(R.id.listview).setVisibility(View.VISIBLE);
                findViewById(R.id.gridview).setVisibility(View.GONE);
                return true;

            case R.id.action_grid:
                item.setVisible(false);
                mymenu.findItem(R.id.action_list).setVisible(true);
                findViewById(R.id.listview).setVisibility(View.GONE);
                findViewById(R.id.gridview).setVisibility(View.VISIBLE);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onListOfProductsItemSelected(Integer id) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, ProductPage.newInstance(id, getApplicationContext()))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLogIn() {
        // Set up the drawer.
        Log.d("URL", "1");
        NavigationDrawerFragment mNavigationDrawerFragment2 = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment2.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        setDrawerLeftEdgeSize(this, mDrawerLayout, 0.1f);
    }
}
