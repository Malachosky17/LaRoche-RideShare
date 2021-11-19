package com.products.laroche.larocherideshare.ui.maps;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
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

/**
 * Created by Malac on 2/22/2017.
 *
 * @author: Malac
 */

public class MapsFragment extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;

    private static String LOGTAG = MapsFragment.class.getSimpleName();
    private String mapSearchInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOGTAG, "onCreate");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        mapSearchInfo = sharedPreferences.getString(Constants.MAP_SEARCH_EXTRAS, "everything");
        Log.i(LOGTAG, "   " + mapSearchInfo);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_fragment, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(new Bundle());
        mMapView.onResume();    //Needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap pMap) {
                Log.i(LOGTAG, "onMapReady");
                googleMap = pMap;
                //Set daytime or nighttime maps determined by time of day.
                if (getCurrentTime() > 19) {
                    MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.google_maps_nighttime);
                    googleMap.setMapStyle(styleOptions);
                } else {
                    MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.google_maps_daytime);
                    googleMap.setMapStyle(styleOptions);
                }
                // Add a marker near La Roche College and move the camera
                try {
                    googleMap.setMyLocationEnabled(true);
                    LatLng laRocheCollege = new LatLng(40.5683, -80.0141);
                    googleMap.addMarker(new MarkerOptions().position(laRocheCollege).title("La Roche College"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(laRocheCollege, 13));
                    addMarkersToMap();
                } catch (SecurityException sEx) {
                    Log.e(LOGTAG, sEx.getMessage() + ": " + sEx.getCause());
                }
            }
        });
        return rootView;
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
    private void addMarkersToMap() {
        RetrieveSpringDataTask task = new RetrieveSpringDataTask();
        try {
            Log.i(LOGTAG, "Adding Markers to map " + mapSearchInfo);
            ArrayList<MyPlace> places = task.execute(mapSearchInfo).get();
            if (!places.isEmpty()) {
                for (MyPlace place : places) {
                    LatLng location = new LatLng(place.getLocation()[0], place.getLocation()[1]);
                    googleMap.addMarker(new MarkerOptions().position(location).title(place.getName()));
                    Log.i(LOGTAG, place.getName());
                }
            }
        } catch (InterruptedException | ExecutionException ie) {
            Log.e(LOGTAG, ie.getMessage() + ": " + ie.getCause());
        }
    }
}
