package com.products.laroche.larocherideshare;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.R.attr.button;

public class ClassSchedulerContainer extends AppCompatActivity {

    private Handler mHandler;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_scheduler_container);
        mHandler = new Handler();
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if(count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    private Fragment getHomeFragment(String day) {
        Bundle bundle = new Bundle();
        bundle.putString("from classschedulercontainer", day);
        switch(day) {
            case "Monday":
                //Monday Schedule
                DayFragment mondayFragment = new DayFragment();
                mondayFragment.setArguments(bundle);
                return mondayFragment;
            case "Tuesday":
                DayFragment tuesdayFragment = new DayFragment();
                tuesdayFragment.setArguments(bundle);
                return tuesdayFragment;
                //Tuesday Schedule
            case "Wednesday":
                DayFragment wednesdayFragment = new DayFragment();
                wednesdayFragment.setArguments(bundle);
                return wednesdayFragment;
                //Wednesday Schedule
            case "Thursday":
                DayFragment thursdayFragment = new DayFragment();
                thursdayFragment.setArguments(bundle);
                return thursdayFragment;
                //Thursday Schedule
            case "Friday":
                DayFragment fridayFragment = new DayFragment();
                fridayFragment.setArguments(bundle);
                return fridayFragment;
                //Friday Schedule
            default:
                return null;
        }
    }

    public void handleButtonPress(View view) {
        button = (Button)findViewById(view.getId());
        final Fragment fragment = getHomeFragment((String)button.getText());

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.activity_class_scheduler_container, fragment, null);
                fragmentTransaction.addToBackStack((String)button.getText());
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if(mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
    }
}
