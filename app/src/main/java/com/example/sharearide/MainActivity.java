package com.example.sharearide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.my.kizzyrpc.KizzyRPC;
import com.my.kizzyrpc.model.Activity;
import com.my.kizzyrpc.model.Assets;
import com.my.kizzyrpc.model.Metadata;
import com.my.kizzyrpc.model.Timestamps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private WebChromeClient webChrome;
    protected static SharedPreferences PREF;

    String JS_SNIPPET =
            "javascript:(function()%7Bvar%20i%3Ddocument.createElement('iframe')%3Bdocument.body.appendChild(i)%3Balert(i.contentWindow.localStorage.token.slice(1,-1))%7D)()";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PREF = PreferenceManager.getDefaultSharedPreferences(this);

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d("Web", "Attempt to enter " + request.getUrl().toString());
                view.stopLoading();
                if (request.getUrl().toString().endsWith("/app")) {
                    webView.setVisibility(View.GONE);
                    webView.loadUrl(JS_SNIPPET);
                }
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.v("THE TOKEN", message);
                PREF.edit().putString("Token", message).commit();
                return true;
            }
        });

        Button connect = findViewById(R.id.connect);
        Log.v("RUNNING", "RUNNING");
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("RUNNING", "RUNNING");
                Intent intent = new Intent(v.getContext(), MyService.class);
                intent.putExtra("Token", PREF.getString("Token", null));
                intent.setAction("START_ACTIVITY_ACTION");
                startService(intent);
            }
        });

        Button disconnect = findViewById(R.id.disconnect);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(v.getContext(), MyService.class));
            }
        });

    }

    public void login(View v) {
        if (webView.getVisibility() == View.VISIBLE) {
            webView.stopLoading();
            webView.setVisibility(View.GONE);
            return;
        }

        webView.setVisibility(View.VISIBLE);
        webView.loadUrl("https://discord.com/login");
    }
}