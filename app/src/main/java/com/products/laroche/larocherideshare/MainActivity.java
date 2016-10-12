package com.products.laroche.larocherideshare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.products.laroche.larocherideshare.model.Constants;


public class MainActivity extends AppCompatActivity {

    private static String CURRENT_TAG = Constants.TAG_HOME;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private Handler mHandler;
    private NavigationView navigationView;
    private boolean shouldLoadHomeFragOnBackPress = true;
    SharedPreferences preferences;

    public static int navItemIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        /*
         * Uncomment the if-statement below when testing release apk.
         * --Commented out to test debug builds--
         */
        if(!preferences.getBoolean(Constants.PREFERENCES_LOGIN_STATUS, false)) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        initializeNavigationView();

        if(savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = Constants.TAG_HOME;
            loadHomeFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = Constants.TAG_HOME;
                loadHomeFragment();
                return;
            }
            if(navItemIndex == 0) {
                if(preferences.getBoolean(Constants.PREFERENCES_LOGIN_STATUS, false)) {
                    finish();
                    return;
                }
            }
        }

        super.onBackPressed();
    }

    private void initializeNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    //Replace the itemIndex and tags
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = Constants.TAG_HOME;
                        break;
                    case R.id.nav_school:
                        navItemIndex = 1;
                        CURRENT_TAG = Constants.TAG_SCHOOL;
                        break;
                    case R.id.nav_lunch:
                        navItemIndex = 2;
                        CURRENT_TAG = Constants.TAG_LUNCH;
                        break;
                    case R.id.nav_scheduler:
                        startActivity(new Intent(MainActivity.this, ClassSchedulerContainer.class));
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        drawer.closeDrawers();
                        break;
                    case R.id.nav_share:
                        //Start an app/activity that can send the apk to someone.
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_send:
                        //Start the default email host on the device.
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_logout:
                        //Find out a way to logout from another activity...
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                if(item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                item.setChecked(true);

                loadHomeFragment();
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private Fragment getHomeFragment() {
        switch(navItemIndex) {
            case 0:
                HomeDescription homeFragment = new HomeDescription();
                return homeFragment;
            case 1:
                SchoolScheduler schoolFragment = new SchoolScheduler();
                return schoolFragment;
            case 2:
                //Lunch Scheduler
                LunchScheduler lunchFragment = new LunchScheduler();
                return lunchFragment;
            default:
                return new HomeDescription();
        }
    }

    private void loadHomeFragment() {
        if(getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.content_main, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if(mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        drawer.closeDrawers();
        invalidateOptionsMenu();
    }
}
