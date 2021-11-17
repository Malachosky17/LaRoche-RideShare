package com.products.laroche.larocherideshare.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.products.laroche.larocherideshare.R;
import com.products.laroche.larocherideshare.model.Constants;
import com.products.laroche.larocherideshare.ui.maps.MapsActivity;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class TransportationFragment extends Fragment implements View.OnClickListener {

    private static final String LOGTAG = TransportationFragment.class.getSimpleName();
    private int PLACE_PICKER_REQUEST = 1;
    //UI elements
    private Button btnFood;
    private Button btnEntertainment;
    private Button btnSchool_Home;
    private Button btnUtilities;


    public TransportationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_transportation, container, false);
        btnFood = (Button)v.findViewById(R.id.btnFood);
        btnEntertainment = (Button)v.findViewById(R.id.btnEntertainment);
        btnSchool_Home = (Button)v.findViewById(R.id.btnSchool_Home);
        btnUtilities = (Button)v.findViewById(R.id.btnUtilities);
        activateButtons();
        return v;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), MapsActivity.class);
        String urlExtension;
        switch(v.getId()) {
            case R.id.btnFood: {
                urlExtension = "restaurants";
                intent.putExtra(Constants.MAP_SEARCH_EXTRAS, urlExtension);
                break;
            }
            case R.id.btnEntertainment: {
                urlExtension = "entertainment";
                intent.putExtra(Constants.MAP_SEARCH_EXTRAS, urlExtension);
                break;
            }
            case R.id.btnSchool_Home: {
                intent.putExtra(Constants.MAP_SEARCH_EXTRAS,"school_home");
                break;
            }
            case R.id.btnUtilities: {
                urlExtension = "utilities";
                intent.putExtra(Constants.MAP_SEARCH_EXTRAS, urlExtension);
                break;
            }
            //Add more cases per button when code is available...
            //Need to add a bundle that will pass parameters to determine what MapsActivity.class will show...
        }
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                //locationButton.setText(String.format("Place: %s", place.getName()));
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void activateButtons() {
        btnFood.setOnClickListener(this);
        btnEntertainment.setOnClickListener(this);
        btnSchool_Home.setOnClickListener(this);
        btnUtilities.setOnClickListener(this);
    }
}
