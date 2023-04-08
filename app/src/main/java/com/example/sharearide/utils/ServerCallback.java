package com.example.sharearide.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public interface ServerCallback {
    void onDone(String response);
    void onSuccess(JSONObject response) throws JSONException;
    Context getContext();
}
