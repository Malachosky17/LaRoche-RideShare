package com.products.laroche.larocherideshare;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.products.laroche.larocherideshare.model.Constants;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static String TAG = MapsActivity.class.getSimpleName();

    private String mapSearchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                mapSearchInfo = null;
            } else {
                Log.v(TAG, "Extra passed into MapsActivity: " + extras.getString(Constants.MAP_SEARCH_EXTRAS));
                mapSearchInfo = extras.getString(Constants.MAP_SEARCH_EXTRAS);
            }
        } else {
            mapSearchInfo = (String) savedInstanceState.getSerializable(Constants.MAP_SEARCH_EXTRAS);
        }
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Set daytime or nighttime maps determined by time of day.
        if(getCurrentTime() > 19) {
            MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.google_maps_nighttime);
            mMap.setMapStyle(styleOptions);
        } else {
            MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.google_maps_daytime);
            mMap.setMapStyle(styleOptions);
        }
        // Add a marker near La Roche College and move the camera
        try {
            mMap.setMyLocationEnabled(true);
            LatLng laRocheCollege = new LatLng(40.5683, -80.0141);
            mMap.addMarker(new MarkerOptions().position(laRocheCollege).title("La Roche College Area"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(laRocheCollege));
        } catch(SecurityException sEx) {
            Log.v(TAG, sEx.getMessage() + ": " + sEx.getCause());
        }
        if(mapSearchInfo != null) {
            //Here specifiy the places marked on map.
            if(mapSearchInfo.contentEquals("food")) {
                addFoodToMap();
                Log.v(TAG, "You wanna search for food!");
            } else if(mapSearchInfo.contentEquals("entertainment")) {
                addEntertainmentToMap();
                Log.v(TAG, "You wanna search for entertainment");
            } else if(mapSearchInfo.contentEquals("utilities")) {
                addUtilitiesToMap();
                Log.v(TAG, "You wanna search for utilities");
            }
        }
    }

    private int getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm");
        date.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        String localTime = date.format(currentLocalTime);
        int hour = Integer.parseInt(localTime.substring(0,2));
        return hour;
    }

    /*
     * Add "food" places to mMap
     * Examples: Local Restaurants, Ice cream, Pizza, etc.
     */
    private void addFoodToMap() {

    }

    /*
     * Add "entertainment" places to mMap
     * Examples: Movies, North Park, Laser Tag, etc.
     */
    private void addEntertainmentToMap() {

    }

    /*
     * Add "utilities" places to mMap
     * Examples: Hospital, gas stations, med express, etc.
     */
    private void addUtilitiesToMap() {

    }
}