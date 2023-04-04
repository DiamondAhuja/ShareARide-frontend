package com.example.sharearide.utils;

import android.content.Context;

public interface ServerCallback {
    void onDone(String response);
    Context getContext();
}
