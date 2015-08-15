package com.example.mapdemo;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.m4b.maps.GoogleMap;
import com.google.android.m4b.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.m4b.maps.MapFragment;
import com.google.android.m4b.maps.OnMapReadyCallback;
import com.google.android.m4b.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.m4b.maps.StreetViewPanorama;
import com.google.android.m4b.maps.StreetViewPanorama.OnStreetViewPanoramaChangeListener;
import com.google.android.m4b.maps.StreetViewPanoramaFragment;
import com.google.android.m4b.maps.model.BitmapDescriptorFactory;
import com.google.android.m4b.maps.model.LatLng;
import com.google.android.m4b.maps.model.Marker;
import com.google.android.m4b.maps.model.MarkerOptions;
import com.google.android.m4b.maps.model.StreetViewPanoramaLocation;

/**
 * This shows how to create a simple activity with streetview and a map
 */
public class SplitStreetViewPanoramaAndMapDemoActivity extends Activity
    implements OnMarkerDragListener, OnStreetViewPanoramaChangeListener {

    private static final String MARKER_POSITION_KEY = "MarkerPosition";

    // George St, Sydney
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);

    private StreetViewPanorama mStreetViewPanorama;
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.split_street_view_panorama_and_map_demo);

        final LatLng markerPosition;
        if (savedInstanceState == null) {
            markerPosition = SYDNEY;
        } else {
            markerPosition = savedInstanceState.getParcelable(MARKER_POSITION_KEY);
        }

        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment)
                        getFragmentManager().findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(
                new OnStreetViewPanoramaReadyCallback() {
            @Override
            public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                mStreetViewPanorama = panorama;
                mStreetViewPanorama.setPosition(markerPosition);
                mStreetViewPanorama.setOnStreetViewPanoramaChangeListener(
                        SplitStreetViewPanoramaAndMapDemoActivity.this);
            }
        });

        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                map.setOnMarkerDragListener(SplitStreetViewPanoramaAndMapDemoActivity.this);
                // Creates a draggable marker. Long press to drag.
                mMarker = map.addMarker(new MarkerOptions()
                        .position(markerPosition)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pegman))
                        .draggable(true));
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MARKER_POSITION_KEY, mMarker.getPosition());
    }

    @Override
    public void onStreetViewPanoramaChange(StreetViewPanoramaLocation location) {
        if (location != null) {
          mMarker.setPosition(location.position);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mStreetViewPanorama.setPosition(marker.getPosition(), 150);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }
}
