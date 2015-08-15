
// Copyright 2014 Google Inc. All Rights Reserved.

package com.example.mapdemo;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.m4b.maps.GoogleMap;
import com.google.android.m4b.maps.GoogleMap.OAuthTokenProvider;
import com.google.android.m4b.maps.MapFragment;
import com.google.android.m4b.maps.OnMapReadyCallback;
import com.google.android.m4b.maps.model.MapsEngineLayerOptions;

import java.io.IOException;

/**
 * Displays a private maps engine layer on a map, using OAuth for authentication.
 */
public final class MapsEngineOAuthDemoActivity extends Activity implements
        OAuthTokenProvider, OnMapReadyCallback {

    private static final String TAG = "MapsEngineOAuthDemoActivity";

    /** Request code for the AccountPicker dialog. */
    private static final int CHOOSE_ACCOUNT_REQUEST_CODE = 0;

    /** Request code for the OAuth permission dialog. */
    private static final int OAUTH_PERMISSION_REQUEST_CODE = 1;

    /** Required OAuth scope to access Maps Engine layers. */
    private static final String OAUTH_SCOPE = "oauth2:https://www.googleapis.com/auth/mapsengine";

    /** The name of the account the user chose to access the Maps Engine layer. */
    private String mAccountName;

    /**
     * Indicates whether we are currently in the process of asking the user for permission to access
     * their Maps Engine data.
     */
    private boolean mWaitingForUserPermission = false;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_engine_oauth_demo);

        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Start AccountPicker intent to get account name.
        Intent intent = AccountPicker.newChooseAccountIntent(
                null /* selectedAccount */,
                null /* allowableAccounts */,
                new String[] {"com.google"} /* allowableAccountTypes */,
                false /* alwaysPromptForAccount */,
                null /* descriptionOverrideText */,
                null /* addAccountAuthTokenType */,
                null /* addAccountRequiredFeatures */,
                null /* addAccountOptions */);
        startActivityForResult(intent, CHOOSE_ACCOUNT_REQUEST_CODE);
    }

    private void showMapsEngineLayer() {
        mMap.setOAuthTokenProvider(this);

        // Add a Maps Engine layer. Here we specify a layer via Layer ID. The layer is marked
        // private, so only users with the correct access can view it.
        mMap.addMapsEngineLayer(
                new MapsEngineLayerOptions().layerId("14182859561222861561-11587494691875060566"));
    }

    private void onUserAccountChosen(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // The user selected an account, continue with loading the map.
            mAccountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            showMapsEngineLayer();
        } else {
            // User did not choose an account. Quit.
            finish();
        }
    }

    private void onUserPermissionsGranted(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // User approved the permissions request. Notify.
            mWaitingForUserPermission = false;
            synchronized (this) {
                notifyAll();
            }
        } else {
            // User denied the permissions request. Quit.
            finish();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {
        if (requestCode == CHOOSE_ACCOUNT_REQUEST_CODE) {
            onUserAccountChosen(resultCode, data);
        } else if (requestCode == OAUTH_PERMISSION_REQUEST_CODE) {
            onUserPermissionsGranted(resultCode, data);
        }
    }

    /**
     * Starts an intent to ask the user for permission for this app to use their Maps Engine data.
     * Blocks with wait() until they have responded to the dialog.
     *
     * @param requestIntent an intent to start the permission request Activity
     */
    private synchronized void requestUserPermission(Intent requestIntent) {
        mWaitingForUserPermission = true;
        startActivityForResult(requestIntent, OAUTH_PERMISSION_REQUEST_CODE);
        try {
            synchronized (this) {
                while (mWaitingForUserPermission) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            // Interrupted, just return.
        }
    }

    /**
     * Grab an OAuth authentication token. Blocking.
     */
    @Override
    public synchronized String getOAuthToken() {
        try {
            return GoogleAuthUtil.getToken(getBaseContext(), mAccountName, OAUTH_SCOPE);
        } catch (UserRecoverableAuthException e) {
            // Need user permission first. Get it, then try again.
            requestUserPermission(e.getIntent());
            return getOAuthToken();
        } catch (GoogleAuthException e) {
            Log.e(TAG, "Unrecoverable authentication exception: " + e.getMessage(), e);
            return null;
        } catch (IOException e) {
            Log.e(TAG, "IO exception in authentication: " + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void invalidateOAuthToken(String token) {
        GoogleAuthUtil.invalidateToken(getBaseContext(), token);
    }
}

