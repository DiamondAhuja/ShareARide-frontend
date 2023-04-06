package com.example.sharearide;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;

public class TempOfferActivity extends AppCompatActivity {

    private Button submit_btn;
    private EditText departure, destination;
    private RecyclerView departure_list, destination_list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_offer_page);

        String apiKey = "AIzaSyCvOEcPKVyfbtE0WOA9ZD1R0X13gK9PNLc";
        // Initialize the SDK
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        // set toolbar format
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        departure = (EditText) findViewById(R.id.departure);
        departure_list = (RecyclerView) findViewById(R.id.departure_list);
        LinearLayoutManager layoutManager_1 = new LinearLayoutManager(TempOfferActivity.this);
        departure_list.setLayoutManager(layoutManager_1);

        destination = (EditText) findViewById(R.id.destination);
        destination_list = (RecyclerView) findViewById(R.id.destination_list);
        LinearLayoutManager layoutManager_2 = new LinearLayoutManager(TempOfferActivity.this);
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
                    RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(TempOfferActivity.this, departure, response.getAutocompletePredictions());
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
                    RecyclerviewAdapter recyclerviewAdapter = new RecyclerviewAdapter(TempOfferActivity.this, destination, response.getAutocompletePredictions());
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

        AutoCompleteTextView exposedDropdown_1 = findViewById(R.id.num_of_carpool);
        String[] items_1 = new String[]{"1", "2", "3", "4"};
        ArrayAdapter<String> adapter_1 = new ArrayAdapter<>(this, R.layout.dropdown_item, items_1);
        exposedDropdown_1.setAdapter(adapter_1);

        submit_btn = (Button) findViewById(R.id.submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TempOfferActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
