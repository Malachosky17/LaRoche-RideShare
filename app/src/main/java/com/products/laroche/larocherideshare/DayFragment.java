package com.products.laroche.larocherideshare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DayFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = DayFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    public DayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DayFragment newInstance(String param1, String param2) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        // Inflate the layout for this fragment
        try {
            switch (getArguments().getString("from classschedulercontainer")) {
                case "Monday": {
                    view = inflater.inflate(R.layout.fragment_monday, container, false);
                    Button btnMondayNext = (Button) view.findViewById(R.id.btn_monday_next);
                    btnMondayNext.setOnClickListener(this);
                    Button btnMondayPrev = (Button) view.findViewById(R.id.btn_monday_previous);
                    btnMondayPrev.setOnClickListener(this);
                    break;
                }
                case "Tuesday": {
                    view = inflater.inflate(R.layout.fragment_tuesday, container, false);
                    Button btnTuesdayNext = (Button) view.findViewById(R.id.btn_tuesday_next);
                    btnTuesdayNext.setOnClickListener(this);
                    Button btnTuesdayPrev = (Button) view.findViewById(R.id.btn_tuesday_previous);
                    btnTuesdayPrev.setOnClickListener(this);
                    break;
                }
                case "Wednesday": {
                    view = inflater.inflate(R.layout.fragment_wednesday, container, false);
                    Button btnWednesdayNext = (Button) view.findViewById(R.id.btn_wednesday_next);
                    btnWednesdayNext.setOnClickListener(this);
                    Button btnWednesdayPrev = (Button) view.findViewById(R.id.btn_wednesday_previous);
                    btnWednesdayPrev.setOnClickListener(this);
                    break;
                }
                case "Thursday": {
                    view = inflater.inflate(R.layout.fragment_thursday, container, false);
                    Button btnThursdayNext = (Button) view.findViewById(R.id.btn_thursday_next);
                    btnThursdayNext.setOnClickListener(this);
                    Button btnThursdayPrev = (Button) view.findViewById(R.id.btn_thursday_previous);
                    btnThursdayPrev.setOnClickListener(this);
                    break;
                }
                case "Friday": {
                    view = inflater.inflate(R.layout.fragment_friday, container, false);
                    Button btnFridayNext = (Button) view.findViewById(R.id.btn_friday_finish);
                    btnFridayNext.setOnClickListener(this);
                    Button btnFridayPrev = (Button) view.findViewById(R.id.btn_friday_previous);
                    btnFridayPrev.setOnClickListener(this);
                    break;
                }
            }
        } catch (NullPointerException npe) {
            Log.e(TAG, "Argument was: " + getArguments().getString("from classschedulercontainer") + " in " + TAG);
            npe.printStackTrace();
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        DayFragment dayFragment = new DayFragment();
        final FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        switch(v.getId()) {
            case R.id.btn_monday_next: {
                bundle.putString("from classschedulercontainer", "Tuesday");
                dayFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.activity_class_scheduler_container, dayFragment);
                fragmentTransaction.addToBackStack(dayFragment.toString());
                fragmentTransaction.commit();
                break;
            }
            case R.id.btn_monday_previous: {
                fragmentTransaction.remove(this).commit();
                break;
            }
            case R.id.btn_tuesday_next: {
                bundle.putString("from classschedulercontainer", "Wednesday");
                dayFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.activity_class_scheduler_container, dayFragment);
                fragmentTransaction.addToBackStack(dayFragment.toString());
                fragmentTransaction.commit();
                break;
            }
            case R.id.btn_tuesday_previous: {
                bundle.putString("from classschedulercontainer", "Monday");
                dayFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.activity_class_scheduler_container, dayFragment);
                fragmentTransaction.addToBackStack(dayFragment.toString());
                fragmentTransaction.commit();
                break;
            }
            case R.id.btn_wednesday_next: {
                bundle.putString("from classschedulercontainer", "Thursday");
                dayFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.activity_class_scheduler_container, dayFragment);
                fragmentTransaction.addToBackStack(dayFragment.toString());
                fragmentTransaction.commit();
                break;
            }
            case R.id.btn_wednesday_previous: {
                bundle.putString("from classschedulercontainer", "Tuesday");
                dayFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.activity_class_scheduler_container, dayFragment);
                fragmentTransaction.addToBackStack(dayFragment.toString());
                fragmentTransaction.commit();
                break;
            }
            case R.id.btn_thursday_next: {
                bundle.putString("from classschedulercontainer", "Friday");
                dayFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.activity_class_scheduler_container, dayFragment);
                fragmentTransaction.addToBackStack(dayFragment.toString());
                fragmentTransaction.commit();
                break;
            }
            case R.id.btn_thursday_previous: {
                bundle.putString("from classschedulercontainer", "Wednesday");
                dayFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.activity_class_scheduler_container, dayFragment);
                fragmentTransaction.addToBackStack(dayFragment.toString());
                fragmentTransaction.commit();
                break;
            }
            case R.id.btn_friday_previous: {
                bundle.putString("from classschedulercontainer", "Thursday");
                dayFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.activity_class_scheduler_container, dayFragment);
                fragmentTransaction.addToBackStack(dayFragment.toString());
                fragmentTransaction.commit();
                break;
            }
            case R.id.btn_friday_finish: {
                fragmentTransaction.remove(this).commit();
                break;
            }
            default: {
                break;
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
}
