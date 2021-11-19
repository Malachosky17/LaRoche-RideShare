package com.products.laroche.larocherideshare.ui.maps;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.products.laroche.larocherideshare.R;
import com.products.laroche.larocherideshare.model.Constants;
import com.products.laroche.larocherideshare.model.MyPlace;
import com.products.laroche.larocherideshare.services.RetrieveSpringDataTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static String LOGTAG = MapsActivity.class.getSimpleName();

    private String mapSearchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Log.i(LOGTAG, "onCreate");
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            mapSearchInfo = null;
        } else {
            mapSearchInfo = (String) extras.getString(Constants.MAP_SEARCH_EXTRAS);
        }
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
        Log.i(LOGTAG, "onMapReady");
        mMap = googleMap;
        //Set daytime or nighttime maps determined by time of day.
        if (getCurrentTime() > 19) {
            MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.google_maps_nighttime);
            mMap.setMapStyle(styleOptions);
        } else {
            MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.google_maps_daytime);
            mMap.setMapStyle(styleOptions);
        }
        // Add a marker near La Roche College and move the camera
        try {
            mMap.setMyLocationEnabled(true);
            LatLng laRocheCollege = new LatLng(40.5683, -80.0141);
            mMap.addMarker(new MarkerOptions().position(laRocheCollege).title("La Roche College Area"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(laRocheCollege, 15));
            addMarkersToMap(mapSearchInfo);
        } catch (SecurityException sEx) {
            Log.e(LOGTAG, sEx.getMessage() + ": " + sEx.getCause());
        }
    }

    private int getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm");
        date.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        String localTime = date.format(currentLocalTime);
        int hour = Integer.parseInt(localTime.substring(0, 2));
        return hour;
    }

    /*
     * Add "food" places to mMap
     * Examples: Local Restaurants, Ice cream, Pizza, etc.
     */
    private void addMarkersToMap(String urlTag) {
        RetrieveSpringDataTask task = new RetrieveSpringDataTask();
        try {
            ArrayList<MyPlace> places = task.execute(urlTag).get();
            if (!places.isEmpty()) {
                for (MyPlace place : places) {
                    LatLng location = new LatLng(place.getLocation()[0], place.getLocation()[1]);
                    mMap.addMarker(new MarkerOptions().position(location).title(place.getName()));
                    Log.i(LOGTAG, place.getName());
                }
            }
        } catch (InterruptedException | ExecutionException ie) {
            Log.e(LOGTAG, ie.getMessage() + ": " + ie.getCause());
        }
    }
}