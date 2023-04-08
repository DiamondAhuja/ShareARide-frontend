package com.example.sharearide;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.sharearide.utils.QueryServer;
import com.example.sharearide.utils.ServerCallback;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.android.libraries.places.api.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TripInformationActivity extends AppCompatActivity implements OnMapReadyCallback, ServerCallback {
    private GoogleMap mMap;
    private String apiKey = "AIzaSyCvOEcPKVyfbtE0WOA9ZD1R0X13gK9PNLc";
    private GeoApiContext mGeoApiContext = null;
    private Button submit;
    private TextView departure, destination, stops, ETA, fare, distance;
    private ArrayList<Place> stops_list = new ArrayList<>();
    private Place origin, end;
    private String rideid = "QEjZ3sHZ7G1krqy7mgFM";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripinfo_page);

        // set toolbar format
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_close_24);
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Trip Information");
        }

        submit = (Button) findViewById(R.id.submit);
        ArrayList<String> id = new ArrayList<>();
        id.add("ChIJwSr3RKyELIgRHAFDYeoAnjk");
        id.add("ChIJHfVL0gw5K4gRvKTcAQhsK6w");
        id.add("ChIJmzrzi9Y0K4gRgXUc3sTY7RU");
        retrievePlace(id);

        ETA = (TextView) findViewById(R.id.ETA);
        fare = (TextView) findViewById(R.id.fare);
        distance = (TextView) findViewById(R.id.distance);
        ETA.setText("ETA: " + "22:00PM");
        fare.setText("Estimated Fare: $" + "$50");
        distance.setText("Total Distance: " + "100km");
    }

    private void getRideInfo(String rideid) {
        QueryServer.getRideInfo(this, rideid);
    }

    public void onSuccess(JSONObject response) throws JSONException {
        ETA = (TextView) findViewById(R.id.ETA);
        fare = (TextView) findViewById(R.id.fare);
        distance = (TextView) findViewById(R.id.distance);
        ETA.setText("ETA: " + response.getString("ETA"));
        fare.setText("Estimated Fare: $" + response.getString("fare"));
        distance.setText("Total Distance: " + response.getString("distance"));

        ArrayList<String> id = new ArrayList<>();
        id.add(response.getString("startlocation"));
        JSONArray stringArrayJson = response.getJSONArray("stops");
        for (int i = 0; i < stringArrayJson.length(); i++) {
            String string = stringArrayJson.getString(i);
            id.add(string);
        }
        id.add(response.getString("endlocation"));
        retrievePlace(id);
    }

    private void retrievePlace(ArrayList<String> placeId) {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        PlacesClient placesClient = Places.createClient(this);

        for (String id : placeId) {
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);
            FetchPlaceRequest request = FetchPlaceRequest.newInstance(id, placeFields);

            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                Place place = response.getPlace();
                stops_list.add(place);
                Log.d(TAG, "Place found: " + place.getName());

                if (stops_list.size() == placeId.size()) {
                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
                    if (mGeoApiContext == null) {
                        mGeoApiContext = new GeoApiContext.Builder()
                                .apiKey(apiKey)
                                .build();
                    }
                }
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    final ApiException apiException = (ApiException) exception;
                    Log.e(TAG, "Place not found: " + exception.getMessage());
                    final int statusCode = apiException.getStatusCode();
                }
            });
        }
    }

    private void setText() {
        departure = (TextView) findViewById(R.id.departure);
        destination = (TextView) findViewById(R.id.destination);
        stops = (TextView) findViewById(R.id.stops);

        departure.setText("Departure: " + origin.getName());
        destination.setText("Destination: " + end.getName());
        String text = "Stops: ";
        for (int i = 0; i < stops_list.size(); i++) {
            if (i == stops_list.size() - 1) {
                text += stops_list.get(i).getName();
                break;
            }
            text += stops_list.get(i).getName() + " - ";
        }
        stops.setText("Stops: " + text);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (Place place : stops_list) {
            mMap.addMarker(new MarkerOptions()
                    .position(place.getLatLng())
                    .title(place.getName()));
        }
        origin = stops_list.get(0);
        end = stops_list.get(stops_list.size() - 1);

        CameraUpdate initialLocation = CameraUpdateFactory.newLatLngZoom(origin.getLatLng(), 10);
        mMap.moveCamera(initialLocation);
        setText();

        // Calculate the bounds of the route and zoom in to fit the bounds
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(origin.getLatLng());
        builder.include(end.getLatLng());
        LatLngBounds bounds = builder.build();
        CameraUpdate routeZoom = CameraUpdateFactory.newLatLngBounds(bounds, 120);
        mMap.animateCamera(routeZoom, 600, null);
        for (int i = 0; i < stops_list.size() - 1; i++) {
            Log.d(TAG, "current route:"+ stops_list.get(i).getName() + "to" + stops_list.get(i+1).getName());
            calculateDirections(stops_list.get(i).getLatLng(), stops_list.get(i+1).getLatLng());
        }
    }

    private void addPolylinesToMap(final DirectionsResult result) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                double shortestDistance = Double.MAX_VALUE;
                DirectionsRoute shortestPolyline = null;
                for (DirectionsRoute route : result.routes) {
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    double distance = route.legs[0].distance.inMeters;

                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                        shortestPolyline = route;
                    }
                }

                List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(shortestPolyline.overviewPolyline.getEncodedPath());

                List<LatLng> newDecodedPath = new ArrayList<>();

                for (com.google.maps.model.LatLng latLng : decodedPath) {
                    newDecodedPath.add(new LatLng(
                            latLng.lat,
                            latLng.lng
                    ));
                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(TripInformationActivity.this, R.color.red));
                    polyline.setClickable(true);
                }
            }
        });
    }

    private void calculateDirections(LatLng mOrigin, LatLng mDestination){
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                mDestination.latitude,
                mDestination.longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(true);
        directions.origin(
                new com.google.maps.model.LatLng(
                        mOrigin.latitude,
                        mOrigin.longitude
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "onResult: routes: " + result.routes[0].toString());
                Log.d(TAG, "onResult: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());

                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "onFailure: " + e.getMessage() );

            }
        });
    }

    public void onDone(String response) {}
    public Context getContext() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
