
// Copyright 2013 Google Inc. All Rights Reserved.

package com.example.mapdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.google.android.m4b.maps.GoogleMap;
import com.google.android.m4b.maps.GoogleMap.OnMapsEngineFeatureClickListener;
import com.google.android.m4b.maps.MapFragment;
import com.google.android.m4b.maps.OnMapReadyCallback;
import com.google.android.m4b.maps.model.MapsEngineFeature;
import com.google.android.m4b.maps.model.MapsEngineLayer;
import com.google.android.m4b.maps.model.MapsEngineLayerOptions;

import java.util.List;

/**
 * Displays a maps engine layer on a map.
 */
public final class MapsEngineDemoActivity extends Activity implements
        OnMapsEngineFeatureClickListener, OnMapReadyCallback {

    private MapsEngineLayer mSydneyLayer;
    private MapsEngineLayer mUrbanLayer;
    private MapsEngineLayer mCoastalLayer;
    private MapsEngineLayer mAirportsLayer;
    private MapsEngineLayer mCamerasLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_engine_demo);

        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setOnMapsEngineFeatureClickListener(this);

        // This shows general regions of Sydney. It is a vector layer and demonstrates clickable
        // regions.
        mSydneyLayer = map.addMapsEngineLayer(new MapsEngineLayerOptions().layerInMap(
                "16150640193109660958-06229419571706175761", "sydney").defaultUi(true));
        toggleSydney(findViewById(R.id.sydney));

        // This shows cities in the US. It is a vector layer and demonstrates clickable point
        // features. It is called us-cities-for-android-api-testing in M4B playground.
        mUrbanLayer = map.addMapsEngineLayer(
                new MapsEngineLayerOptions().layerId("14182859561222861561-13622931834983879294"));
        toggleUrban(findViewById(R.id.urban));

        // This shows US coastal satellite imagery. It is an imagery layer.
        mCoastalLayer = map.addMapsEngineLayer(
                new MapsEngineLayerOptions().layerId("10446176163891957399-12677872887550376890"));
        toggleCoastal(findViewById(R.id.coastal));

        // This shows airports in the US (bent-styled-airportx010g)
        mAirportsLayer = map.addMapsEngineLayer(
                new MapsEngineLayerOptions().layerId("14182859561222861561-12012127489080757244"));
        toggleAirports(findViewById(R.id.airports));

        // This layer is a public layer which can only be loaded by MapID/Key/Version. It uses icons
        // as tap elements which have HTML descriptions containing tourist location web cams.
        mCamerasLayer = map.addMapsEngineLayer(new MapsEngineLayerOptions().layerInMap(
                "10446176163891957399-09362451985983837383", "layer_00004"));
        toggleCameras(findViewById(R.id.cameras));
    }

    @Override
    public void onFeatureClick(List<MapsEngineFeature> features) {
        Log.i("MapsEngineActivity", "Feature clicked (" + features.size() + ")");
    }

    @Override
    public void onFeatureInformationReceived(List<MapsEngineFeature> features) {
        Log.i("MapsEngineActivity", "HTML received (" + features.size() + ")");
    }

    //
    // These callbacks are set in the XML layout file.
    //

    public void toggleSydney(View v) {
        if (mSydneyLayer != null) {
            mSydneyLayer.setVisible(((CheckBox) v).isChecked());
        }
    }

    public void toggleUrban(View v) {
        if (mUrbanLayer != null) {
            mUrbanLayer.setVisible(((CheckBox) v).isChecked());
        }
    }

    public void toggleCoastal(View v) {
        if (mCoastalLayer != null) {
            mCoastalLayer.setVisible(((CheckBox) v).isChecked());
        }
    }

    public void toggleAirports(View v) {
        if (mAirportsLayer != null) {
            mAirportsLayer.setVisible(((CheckBox) v).isChecked());
        }
    }

    public void toggleCameras(View v) {
        if (mCamerasLayer != null) {
            mCamerasLayer.setVisible(((CheckBox) v).isChecked());
        }
    }
}

