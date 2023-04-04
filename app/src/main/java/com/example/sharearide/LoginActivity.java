package com.example.sharearide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sharearide.utils.QueryServer;
import com.example.sharearide.utils.ServerCallback;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements ServerCallback {
    EditText etemail;
    EditText etpassword;
    MaterialButton login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etemail = findViewById(R.id.loginemail);
        etpassword = findViewById(R.id.loginpassword);
        login = findViewById(R.id.loginbtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etemail.getText().toString().isEmpty() && etpassword.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both the values", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginPost(etemail.getText().toString(), etpassword.getText().toString());
            }
        });

        configureRegisterButton();
    }

    private void configureRegisterButton(){
        Button loginButton = (Button) findViewById(R.id.switchregister);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginPost(String email, String password){
        QueryServer.login(this, email, password);
    }

    @Override
    public void onDone(String response) {
        if (!response.contains("{\"Message\":")) {
            if (response.equals("Login failed The password is invalid or the user does not have a password.")) {
                Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                etpassword.setText("");
            }
            else if(response.equals("Login failed The email address is badly formatted.")){
                Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                etemail.setText("");
            }
            else{
                Toast.makeText(this, "Email or password not recognized please try again", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}