// Copyright 2013 Google Inc. All Rights Reserved.

package com.example.mapdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.google.android.m4b.maps.GoogleMap;
import com.google.android.m4b.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.m4b.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.m4b.maps.MapFragment;
import com.google.android.m4b.maps.OnMapReadyCallback;

/**
 * This shows how to take a snapshot of the map.
 */
public class SnapshotDemoActivity extends Activity implements OnMapReadyCallback {

    /**
     * Note that this may be null if the Google Play services APK is not available.
     */
    private GoogleMap mMap;

    private CheckBox mWaitForMapLoadCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snapshot_demo);
        mWaitForMapLoadCheckBox = (CheckBox) findViewById(R.id.wait_for_map_load);

        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
    }

    /**
     * Called when the snapshot button is clicked.
     */
    public void onScreenshot(View view) {
        takeSnapshot();
    }

    private void takeSnapshot() {
        if (mMap == null) {
            return;
        }

        final ImageView snapshotHolder = (ImageView) findViewById(R.id.snapshot_holder);

        final SnapshotReadyCallback callback = new SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                // Callback is called from the main thread, so we can modify the ImageView safely.
                snapshotHolder.setImageBitmap(snapshot);
            }
        };

        if (mWaitForMapLoadCheckBox.isChecked()) {
            mMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.snapshot(callback);
                }
            });
        } else {
            mMap.snapshot(callback);
        }
    }

    /**
     * Called when the clear button is clicked.
     */
    public void onClearScreenshot(View view) {
        ImageView snapshotHolder = (ImageView) findViewById(R.id.snapshot_holder);
        snapshotHolder.setImageDrawable(null);
    }
}
