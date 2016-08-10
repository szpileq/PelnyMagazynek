package com.szpilkowski.android.pelnymagazynek.Item;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.szpilkowski.android.pelnymagazynek.R;

import java.util.List;
import java.util.Locale;

public class EditMapPosition extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = EditMapPosition.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    LatLng currentLocation;
    String currentGeocode;
    Marker currentMarker;
    Button getLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        double latitude = (double)intent.getFloatExtra("lat", 0);
        double longitude = (double)intent.getFloatExtra("lng", 0);
        currentLocation = new LatLng(latitude, longitude);

        getLocationButton = (Button)findViewById(R.id.acceptOnMap);
        getLocationButton.setVisibility(View.INVISIBLE);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                handleNewLocation(latLng);
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        showInitialLocation();
        Log.i(TAG, "Location services connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void handleNewLocation(LatLng location){
        currentLocation = location;
        currentMarker.setPosition(currentLocation);
        currentGeocode = getCompleteAddressString(currentLocation);
        currentMarker.setTitle(currentGeocode);
        currentMarker.showInfoWindow();
    }

    private void showInitialLocation() {
        MarkerOptions markerOptions = new MarkerOptions().position(currentLocation).title(getCompleteAddressString(currentLocation));
        currentMarker = mMap.addMarker(markerOptions);
        currentMarker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,10));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15),2000, null);

        getLocationButton.setVisibility(View.VISIBLE);
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultLocation = new Intent();
                resultLocation.putExtra("currentLatitude", currentLocation.latitude);
                resultLocation.putExtra("currentLongitude", currentLocation.longitude);
                resultLocation.putExtra("geocode", currentGeocode);
                setResult(1, resultLocation);
                finish();
            }
        });
    }

    private String getCompleteAddressString(LatLng location) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                if(null != returnedAddress.getThoroughfare())
                    strReturnedAddress.append(returnedAddress.getThoroughfare());
                if(null != returnedAddress.getSubThoroughfare())
                    strReturnedAddress.append(" ").append(returnedAddress.getSubThoroughfare());
                if(null != returnedAddress.getLocality())
                    strReturnedAddress.append(", ").append(returnedAddress.getLocality());

                strAdd = strReturnedAddress.toString();
                Log.w("Location:", "" + strReturnedAddress.toString());
            } else {
                Log.w("Location:", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Location:", "Cannot get Address!");
        }
        return strAdd;
    }
}
