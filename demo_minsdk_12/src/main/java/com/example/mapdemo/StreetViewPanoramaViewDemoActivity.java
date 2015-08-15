package com.example.mapdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;

import com.google.android.m4b.maps.StreetViewPanoramaOptions;
import com.google.android.m4b.maps.StreetViewPanoramaView;
import com.google.android.m4b.maps.model.LatLng;

/**
 * This shows how to create a simple activity with streetview
 */
public class StreetViewPanoramaViewDemoActivity extends Activity {

    // George St, Sydney
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);

    private StreetViewPanoramaView mStreetViewPanoramaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StreetViewPanoramaOptions options = new StreetViewPanoramaOptions();
        if (savedInstanceState == null) {
            options.position(SYDNEY);
        }

        mStreetViewPanoramaView = new StreetViewPanoramaView(this, options);
        addContentView(mStreetViewPanoramaView,
            new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mStreetViewPanoramaView.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        mStreetViewPanoramaView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mStreetViewPanoramaView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mStreetViewPanoramaView.onDestroy();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mStreetViewPanoramaView.onSaveInstanceState(outState);
    }
}
