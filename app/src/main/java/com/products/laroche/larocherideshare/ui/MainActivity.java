package com.products.laroche.larocherideshare.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.products.laroche.larocherideshare.R;
import com.products.laroche.larocherideshare.model.Constants;
import com.products.laroche.larocherideshare.ui.maps.MapsFragment;
import com.products.laroche.larocherideshare.ui.settings.SettingsActivity;
import com.products.laroche.larocherideshare.ui.user.HomeDescriptionFragment;
import com.products.laroche.larocherideshare.ui.user.HowToFragment;
import com.products.laroche.larocherideshare.ui.user.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    private final int MY_FINE_LOCATION_REQUEST = 1;
    private final int MY_NETWORK_STATUS_REQUEST = 2;
    private final int MY_INTERNET_REQUEST = 3;

    private static String CURRENT_TAG = Constants.TAG_HOME;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private Handler mHandler;
    private NavigationView navigationView;
    private boolean shouldLoadHomeFragOnBackPress = true;
    SharedPreferences sharedPreferences;

    public static int navItemIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);

//        if(!sharedPreferences.getBoolean(Constants.PREFERENCES_LOGIN_STATUS, false)) {
//            if(getIntent().getBooleanExtra("NOT_LOGGED_IN", false)) {
//                finish();
//            }
//            startActivity(new Intent(this, LoginActivity.class));
//        }

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        initializeNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = Constants.TAG_HOME;
            loadHomeFragment();
        }
        checkPermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
            if (sharedPreferences.getBoolean(Constants.PREFERENCES_LOGIN_STATUS, false)) {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                return;
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
                    case R.id.nav_restaurants:
                        navItemIndex = 1;
                        sharedPreferences.edit().putString(Constants.MAP_SEARCH_EXTRAS, "locations/restaurants").apply();
                        CURRENT_TAG = Constants.TAG_RESTAURANTS;
                        break;
                    case R.id.nav_entertainment:
                        navItemIndex = 1;
                        sharedPreferences.edit().putString(Constants.MAP_SEARCH_EXTRAS, "locations/entertainment").apply();
                        CURRENT_TAG = Constants.TAG_ENTERTAINMENT;
                        break;
                    case R.id.nav_utilities:
                        navItemIndex = 1;
                        sharedPreferences.edit().putString(Constants.MAP_SEARCH_EXTRAS, "locations/utilities").apply();
                        CURRENT_TAG = Constants.TAG_UTILITIES;
                        break;
                    case R.id.nav_how_to:
                        navItemIndex = 2;
                        CURRENT_TAG = Constants.TAG_HOW_TO;
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
                        //Launch email application that is chosen by the user.
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("plain/text");
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Welcome to LaRocheRideShare!");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Body of message.");
                        startActivity(sendIntent);
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_logout:
                        //Find out a way to logout from another activity...
                        Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                        logoutIntent.putExtra("EXIT", false);
                        startActivity(logoutIntent);
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                if (item.isChecked()) {
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
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private Fragment getHomeFragment() {
        Fragment fragment = null;
        switch (navItemIndex) {
            case 0:
                fragment = new HomeDescriptionFragment();
                break;
            case 1:
                fragment = new MapsFragment();
                break;
            case 2:
                fragment = new HowToFragment();
                break;
        }
        return fragment;
    }

    private void loadHomeFragment() {
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
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
                fragmentTransaction.replace(R.id.fragment_spot, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        mHandler.post(mPendingRunnable);
        drawer.closeDrawers();
        invalidateOptionsMenu();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_FINE_LOCATION_REQUEST);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, MY_NETWORK_STATUS_REQUEST);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, MY_INTERNET_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_FINE_LOCATION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted
                } else {
                    //Permission Denied
                }
                return;
            }
            case MY_NETWORK_STATUS_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted
                } else {
                    //Permission Denied
                }
                return;
            }
            case MY_INTERNET_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted
                } else {
                    //Permission Denied
                }
                return;
            }
        }
    }

    public void handleProfileButton(View view) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}