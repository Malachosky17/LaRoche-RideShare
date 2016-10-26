package com.products.laroche.larocherideshare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.products.laroche.larocherideshare.model.Constants;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Transportation.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Transportation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Transportation extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int PLACE_PICKER_REQUEST = 1;
    //UI elements
    private Button btnFood;
    private Button btnEntertainment;
    private Button btnSchool_Home;
    private Button btnUtilities;

    private OnFragmentInteractionListener mListener;

    public Transportation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Transportation.
     */
    // TODO: Rename and change types and number of parameters
    public static Transportation newInstance(String param1, String param2) {
        Transportation fragment = new Transportation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch(v.getId()) {
            case R.id.btnFood: {
                intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra(Constants.MAP_SEARCH_EXTRAS,"food");
                break;
            }
            case R.id.btnEntertainment: {
                intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra(Constants.MAP_SEARCH_EXTRAS, "entertainment");
                break;
            }
            case R.id.btnSchool_Home: {
                intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra(Constants.MAP_SEARCH_EXTRAS,"school_home");
                break;
            }
            case R.id.btnUtilities: {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    getActivity().startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch(GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch(GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
//                intent.putExtra(Constants.MAP_SEARCH_EXTRAS,"utilities");
//                break;
                return;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void activateButtons() {
        btnFood.setOnClickListener(this);
        btnEntertainment.setOnClickListener(this);
        btnSchool_Home.setOnClickListener(this);
        btnUtilities.setOnClickListener(this);
    }
}
