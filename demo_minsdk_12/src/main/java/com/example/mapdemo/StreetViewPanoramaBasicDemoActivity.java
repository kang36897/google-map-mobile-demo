package com.example.mapdemo;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.m4b.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.m4b.maps.StreetViewPanorama;
import com.google.android.m4b.maps.StreetViewPanoramaFragment;
import com.google.android.m4b.maps.model.LatLng;

/**
 * This shows how to create a simple activity with streetview
 */
public class StreetViewPanoramaBasicDemoActivity extends Activity {

    // George St, Sydney
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.street_view_panorama_basic_demo);

        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment)
                        getFragmentManager().findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(
            new OnStreetViewPanoramaReadyCallback() {
                @Override
                public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                    // Only set the panorama to SYDNEY on startup (when no panoramas have been
                    // loaded which is when the savedInstanceState is null).
                    if (savedInstanceState == null) {
                      panorama.setPosition(SYDNEY);
                    }
                }
        });
    }
}
