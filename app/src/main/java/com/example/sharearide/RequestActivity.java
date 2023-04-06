package com.example.sharearide;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

public class RequestActivity extends AppCompatActivity {

    private EditText departure, destination;
    private RecyclerView departure_list, destination_list;
//    Button LocationBtn;
//    TextView AddressText;
//    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_page);

        // set toolbar format
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find your Carpool");

        String apiKey = "AIzaSyCvOEcPKVyfbtE0WOA9ZD1R0X13gK9PNLc";
        // Initialize the SDK
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        departure = (EditText) findViewById(R.id.departure);
        departure_list = (RecyclerView) findViewById(R.id.departure_list);
        LinearLayoutManager layoutManager_1 = new LinearLayoutManager(RequestActivity.this);
        departure_list.setLayoutManager(layoutManager_1);

        destination = (EditText) findViewById(R.id.destination);
        destination_list = (RecyclerView) findViewById(R.id.destination_list);
        LinearLayoutManager layoutManager_2 = new LinearLayoutManager(RequestActivity.this);
        destination_list.setLayoutManager(layoutManager_2);

        departure.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
                // and once again when the user makes a selection (for example when calling fetchPlace()).
                AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

                // Create a RectangularBounds object.
                RectangularBounds bounds = RectangularBounds.newInstance(
                        new LatLng(-33.880490, 151.184363),
                        new LatLng(-33.858754, 151.229596));
                // Use the builder to create a FindAutocompletePredictionsRequest.
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        // Call either setLocationBias() OR setLocationRestriction().
                        .setLocationBias(bounds)
                        //.setLocationRestriction(bounds)
                        .setOrigin(new LatLng(-33.8749937,151.2041382))
                        .setSessionToken(token)
                        .setQuery(editable.toString())
                        .build();

                placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
                    RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(RequestActivity.this, departure, response.getAutocompletePredictions());
                    departure_list.setAdapter(recyclerviewAdapter);
                    departure_list.setVisibility(View.VISIBLE);
                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        departure_list.setVisibility(View.GONE);
                    }
                });
            }
        });


        departure.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Hide the RecyclerView when the EditText loses focus
                    departure_list.setVisibility(View.GONE);
                }
            }
        });


        destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
                // and once again when the user makes a selection (for example when calling fetchPlace()).
                AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

                // Create a RectangularBounds object.
                RectangularBounds bounds = RectangularBounds.newInstance(
                        new LatLng(-33.880490, 151.184363),
                        new LatLng(-33.858754, 151.229596));
                // Use the builder to create a FindAutocompletePredictionsRequest.
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        // Call either setLocationBias() OR setLocationRestriction().
                        .setLocationBias(bounds)
                        //.setLocationRestriction(bounds)
                        .setOrigin(new LatLng(-33.8749937,151.2041382))
                        .setSessionToken(token)
                        .setQuery(editable.toString())
                        .build();

                placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
                    destination_list.setVisibility(View.VISIBLE);
                    RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(RequestActivity.this, destination, response.getAutocompletePredictions());
                    destination_list.setAdapter(recyclerviewAdapter);
                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        destination_list.setVisibility(View.GONE);
                    }
                });
            }
        });

        destination.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Hide the RecyclerView when the EditText loses focus
                    destination_list.setVisibility(View.GONE);
                }
            }
        });

//        AddressText = (TextView) findViewById(R.id.address_text);
//        LocationBtn = (Button) findViewById(R.id.location_btn);

//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(5000);
//        locationRequest.setFastestInterval(2000);

//        LocationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                getCurrentLocation();
//            }
//        });

        AutoCompleteTextView exposedDropdown_1 = findViewById(R.id.trip_type);
        String[] items_1 = new String[]{"Temporary", "Recurring"};
        ArrayAdapter<String> adapter_1 = new ArrayAdapter<>(this, R.layout.dropdown_item, items_1);
        exposedDropdown_1.setAdapter(adapter_1);

        AutoCompleteTextView exposedDropdown_2 = findViewById(R.id.rating);
        String[] items_2= new String[]{"0 Star", "1 Star", "2 Star", "3 Star", "4 Star"};
        ArrayAdapter<String> adapter_2 = new ArrayAdapter<>(this, R.layout.dropdown_item, items_2);
        exposedDropdown_2.setAdapter(adapter_2);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == 1){
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
//
//                if (isGPSEnabled()) {
//
//                    getCurrentLocation();
//
//                }else {
//
//                    turnOnGPS();
//                }
//            }
//        }
//
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 2) {
//            if (resultCode == Activity.RESULT_OK) {
//
//                getCurrentLocation();
//            }
//        }
//    }
//
//    private void getCurrentLocation() {
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ActivityCompat.checkSelfPermission(RequestActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
//                if (isGPSEnabled()) {
//
//                    LocationServices.getFusedLocationProviderClient(RequestActivity.this)
//                            .requestLocationUpdates(locationRequest, new LocationCallback() {
//                                @Override
//                                public void onLocationResult(@NonNull LocationResult locationResult) {
//                                    super.onLocationResult(locationResult);
//
//                                    LocationServices.getFusedLocationProviderClient(RequestActivity.this)
//                                            .removeLocationUpdates(this);
//
//                                    if (locationResult != null && locationResult.getLocations().size() >0){
//
//                                        int index = locationResult.getLocations().size() - 1;
//                                        double latitude = locationResult.getLocations().get(index).getLatitude();
//                                        double longitude = locationResult.getLocations().get(index).getLongitude();
//
//                                        AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
//                                    }
//                                }
//                            }, Looper.getMainLooper());
//
//                } else {
//                    turnOnGPS();
//                }
//
//            } else {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            }
//        }
//    }
//
//    private void turnOnGPS() {
//
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//        builder.setAlwaysShow(true);
//
//        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
//                .checkLocationSettings(builder.build());
//
//        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
//            @Override
//            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
//
//                try {
//                    LocationSettingsResponse response = task.getResult(ApiException.class);
//                    Toast.makeText(RequestActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();
//
//                } catch (ApiException e) {
//
//                    switch (e.getStatusCode()) {
//                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//
//                            try {
//                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
//                                resolvableApiException.startResolutionForResult(RequestActivity.this, 2);
//                            } catch (IntentSender.SendIntentException ex) {
//                                ex.printStackTrace();
//                            }
//                            break;
//
//                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                            //Device does not have location
//                            break;
//                    }
//                }
//            }
//        });
//
//    }
//
//    private boolean isGPSEnabled() {
//        LocationManager locationManager = null;
//        boolean isEnabled = false;
//
//        if (locationManager == null) {
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        }
//
//        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        return isEnabled;
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
