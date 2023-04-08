package com.example.sharearide.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class QueryServer {
    private static final String BASE_URL = Constants.URL;

    private QueryServer() {}

    public static void login(ServerCallback serverCallback, String email, String password) {
        String url = BASE_URL + Constants.LOGIN;
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        connectToServer(serverCallback, jsonBody.toString(), url);
    }

    public static void register(ServerCallback serverCallback, String email, String password, String firstname,
                                String lastname, String phonenumber, String address, String DOB) {
        String url = BASE_URL + Constants.REGISTER;

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("firstname", firstname);
            jsonBody.put("lastname", lastname);
            jsonBody.put("phonenumber", phonenumber);
            jsonBody.put("address", address);
            jsonBody.put("DOB", DOB);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        connectToServer(serverCallback, jsonBody.toString(), url);
    }

    private static void connectToServer(ServerCallback serverCallback, String requestBody, String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(serverCallback.getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);

                JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

                serverCallback.onDone(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return super.parseNetworkResponse(response);
            }
        };

        requestQueue.add(stringRequest);

    }
}
