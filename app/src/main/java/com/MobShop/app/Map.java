package com.MobShop.app;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by haplea on 3/16/14.
 */
public class Map extends FragmentActivity {

    public Context context;
    private GoogleMap googleMap;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        lat = 44.458632; lon = 26.080234;
        try {
            // Loading map
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUp(Context context) {
        this.context = context;
    }

    private void initilizeMap() {
        // TODO Auto-generated method stub
        if (googleMap == null) {

            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lon)).title("Coordonate magazinului");
            // adding marker
            googleMap.addMarker(marker);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(lat, lon)).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            googleMap.setMyLocationEnabled(true);


            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Nu am putut incarca Harta", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }




    //@Override
    /*
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map, container, false);
        Context rootViewContext = rootView.getContext();
        Log.d("URL", "1");
        lat = 44.458632; lon = 26.080234;
        if (googleMap == null) {
            Log.d("URL", "2");
            googleMap = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lon)).title("Coordonate magazinului");
            // adding marker
           // googleMap.addMarker(marker);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(lat, lon)).zoom(12).build();
            //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // check if map is created successfully or not
            if (googleMap == null) {
               Toast toast =  Toast.makeText(rootViewContext, "Nu am putut incarca Harta", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        return rootView;
    }
*/

    @Override
    protected void onResume() {
        super.onResume();
    }
}
