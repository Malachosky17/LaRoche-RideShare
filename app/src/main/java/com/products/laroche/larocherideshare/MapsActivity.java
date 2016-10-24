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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //Adding retro-style to google maps.
        if(getCurrentTime() > 17) {
            MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.google_maps_nighttime);
            mMap.setMapStyle(styleOptions);
        } else {
            MapStyleOptions styleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.google_maps_daytime);
            mMap.setMapStyle(styleOptions);
        }
        try {
            mMap.setMyLocationEnabled(true);
        } catch(SecurityException sEx) {
            Log.v(TAG, sEx.getMessage() + ": " + sEx.getCause());
        }
        // Add a marker near La Roche College and move the camera
        LatLng laRocheCollege = new LatLng(40.5683, -80.0141);
        mMap.addMarker(new MarkerOptions().position(laRocheCollege).title("La Roche College Area"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(laRocheCollege));
    }

    private int getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm");
        date.setTimeZone(TimeZone.getTimeZone("America/New_York"));

        String localTime = date.format(currentLocalTime);
        int hour = Integer.parseInt(localTime.substring(0,2));
        Toast.makeText(this, "Local Time: " + localTime + "    Hour Value: " + hour, Toast.LENGTH_LONG).show();
        return hour;
    }
}