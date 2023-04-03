package com.example.sharearide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sharearide.utils.DiscordService;

public class ProfileActivity extends AppCompatActivity {
    private String JS_SNIPPET = "javascript:(function()%7Bvar%20i%3Ddocument.createElement('iframe')%3Bdocument.body.appendChild(i)%3Balert(i.contentWindow.localStorage.token.slice(1,-1))%7D)()";

    protected static SharedPreferences PREF;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Profile Settings");

        PREF = PreferenceManager.getDefaultSharedPreferences(this);

        WebView webView = findViewById(R.id.discord_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.stopLoading();

                if (request.getUrl().toString().endsWith("/app")) {
                    view.setVisibility(View.GONE);
                    view.loadUrl(JS_SNIPPET);
                    view.getSettings().setJavaScriptEnabled(false);

                    view.clearCache(true);
                    view.clearHistory();
                    view.clearFormData();
                    view.clearSslPreferences();

                    WebStorage.getInstance().deleteAllData();
                    CookieManager.getInstance().removeAllCookies(null);
                    CookieManager.getInstance().flush();
                }
                return false;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                PREF.edit().putString("Token", message).commit();
                return true;
            }
        });

        Button login = findViewById(R.id.btn_discord_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl("https://discord.com/login");
            }
        });

        Button connect = findViewById(R.id.btn_discord_connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DiscordService.class);
                intent.putExtra("Token", PREF.getString("Token", null));
                intent.setAction("START_ACTIVITY_ACTION");
                startService(intent);
            }
        });

        Button disconnect = findViewById(R.id.btn_discord_disconnect);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(v.getContext(), DiscordService.class));
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
