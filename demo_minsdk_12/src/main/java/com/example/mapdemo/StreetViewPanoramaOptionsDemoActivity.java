package com.example.mapdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.m4b.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.m4b.maps.StreetViewPanorama;
import com.google.android.m4b.maps.StreetViewPanoramaFragment;
import com.google.android.m4b.maps.model.LatLng;

/**
 * This shows how to create an activity with static streetview (all options have been switched off)
 */
public class StreetViewPanoramaOptionsDemoActivity extends Activity {

    // Cole St, San Fran
    private static final LatLng SAN_FRAN = new LatLng(37.765927, -122.449972);

    private StreetViewPanorama mStreetViewPanorama;

    private CheckBox mStreetNameCheckbox;
    private CheckBox mNavigationCheckbox;
    private CheckBox mZoomCheckbox;
    private CheckBox mPanningCheckbox;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.street_view_panorama_options_demo);

        mStreetNameCheckbox = (CheckBox) findViewById(R.id.streetnames);
        mNavigationCheckbox = (CheckBox) findViewById(R.id.navigation);
        mZoomCheckbox = (CheckBox) findViewById(R.id.zoom);
        mPanningCheckbox = (CheckBox) findViewById(R.id.panning);

        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment)
                        getFragmentManager().findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(
            new OnStreetViewPanoramaReadyCallback() {
                @Override
                public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                    mStreetViewPanorama = panorama;
                    mStreetViewPanorama.setStreetNamesEnabled(mStreetNameCheckbox.isChecked());
                    mStreetViewPanorama.setUserNavigationEnabled(mNavigationCheckbox.isChecked());
                    mStreetViewPanorama.setZoomGesturesEnabled(mZoomCheckbox.isChecked());
                    mStreetViewPanorama.setPanningGesturesEnabled(mPanningCheckbox.isChecked());

                    // Only set the panorama to SAN_FRAN on startup (when no panoramas have been
                    // loaded which is when the savedInstanceState is null).
                    if (savedInstanceState == null) {
                        mStreetViewPanorama.setPosition(SAN_FRAN);
                    }
                }
            });
    }

    private boolean checkReady() {
        if (mStreetViewPanorama == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onStreetNamesToggled(View view) {
        if (!checkReady()) {
            return;
        }
        mStreetViewPanorama.setStreetNamesEnabled(mStreetNameCheckbox.isChecked());
    }

    public void onNavigationToggled(View view) {
        if (!checkReady()) {
            return;
        }
        mStreetViewPanorama.setUserNavigationEnabled(mNavigationCheckbox.isChecked());
    }

    public void onZoomToggled(View view) {
        if (!checkReady()) {
            return;
        }
        mStreetViewPanorama.setZoomGesturesEnabled(mZoomCheckbox.isChecked());
    }

    public void onPanningToggled(View view) {
        if (!checkReady()) {
            return;
        }
        mStreetViewPanorama.setPanningGesturesEnabled(mPanningCheckbox.isChecked());
    }
}
