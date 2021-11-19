package com.products.laroche.larocherideshare.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.products.laroche.larocherideshare.R;
import com.products.laroche.larocherideshare.model.Constants;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    //Int to recognize sign in
    private static final int RC_SIGN_IN = 9001;
    //Used to get the token ID of session
    private static final int RC_GET_TOKEN = 9002;

    //Log Tag used for debugging
    private static final String TAG = LoginActivity.class.getSimpleName();

    // UI references.
    private GoogleApiClient mGoogleApiClient;
    // TODO Replace GoogleApiClient with individual Google Clients
    private GoogleSignInClient mSignInClient;
    private ProgressDialog mProgressDialog;

    private SignInButton signInButton = null;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }
        if (sharedPreferences.getBoolean(Constants.PREFERENCES_LOGIN_STATUS, false)) {
            startActivity(new Intent(this, MainActivity.class));
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        setContentView(R.layout.activity_login);

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult: " + result.isSuccess());
        hideProgressDialog();
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            sharedPreferences.edit().putString(Constants.PREFERENCES_USER_EMAIL, acct.getEmail()).apply();
            Log.i(TAG, "Email login: " + acct.getEmail());

            if (!sharedPreferences.getBoolean(Constants.PREFERENCES_LOGIN_STATUS, false)) {
                sharedPreferences.edit().putBoolean(Constants.PREFERENCES_LOGIN_STATUS, true).apply();
                startActivity(new Intent(this, MainActivity.class));
            }
        } else {
            // Signed out, show unauthenticated UI.
            sharedPreferences.edit().putBoolean(Constants.PREFERENCES_LOGIN_STATUS, false).apply();
            updateUI();
        }
    }

    @Override
    public void onBackPressed() {
        if (!sharedPreferences.getBoolean(Constants.PREFERENCES_LOGIN_STATUS, false)) {
            //Make user kill application without returning to the MainActivity
            moveTaskToBack(true);
        } else {
            finish();
        }
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI() {
        Log.i(TAG, "Value of shared sharedPreferences: " + sharedPreferences.getBoolean(Constants.PREFERENCES_LOGIN_STATUS, false));
        if (sharedPreferences.getBoolean(Constants.PREFERENCES_LOGIN_STATUS, false)) {
            signInButton.setVisibility(View.GONE);
            findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
            findViewById(R.id.disconnect_button).setVisibility(View.VISIBLE);
        } else {
            signInButton.setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
            findViewById(R.id.disconnect_button).setVisibility(View.GONE);
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        sharedPreferences.edit().putBoolean(Constants.PREFERENCES_LOGIN_STATUS, false).apply();
                        updateUI();
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        sharedPreferences.edit().putBoolean(Constants.PREFERENCES_LOGIN_STATUS, false).apply();
                        updateUI();
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        showProgressDialog();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}