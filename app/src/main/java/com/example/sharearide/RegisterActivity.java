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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RegisterActivity extends AppCompatActivity implements ServerCallback {

    EditText etemail;
    EditText etpassword;
    EditText etrepassword;
    EditText etfirstname;
    EditText etlastname;
    EditText etphone;
    EditText etaddress;
    EditText etdob;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etemail = findViewById(R.id.email);
        etpassword = findViewById(R.id.password);
        etrepassword = findViewById(R.id.repassword);
        etfirstname = findViewById(R.id.firstname);
        etlastname = findViewById(R.id.lastname);
        etphone = findViewById(R.id.phone);
        etaddress = findViewById(R.id.address);
        etdob = findViewById(R.id.dob);
        register = findViewById(R.id.signupbtn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etpassword.getText().toString().equals(etrepassword.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Please ensure passwords match!", Toast.LENGTH_LONG).show();
                    etpassword.setText("");
                    etrepassword.setText("");
                    return;
                }
                else if (etpassword.getText().toString().length() == 0){
                    Toast.makeText(RegisterActivity.this, "Please write a password!", Toast.LENGTH_LONG).show();
                }
                registerPost();
            }
        });

        configureButton();
    }

    private void configureButton(){
        Button registerButton = (Button) findViewById(R.id.regloginbtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void registerPost(){
        QueryServer.register(this, etemail.getText().toString(), etpassword.getText().toString(),
                etfirstname.getText().toString(), etlastname.getText().toString(), etphone.getText().toString(),
                etaddress.getText().toString(), etdob.getText().toString());
    }

    @Override
    public void onDone(String response) {

    }

    @Override
    public Context getContext() {
        return this;
    }
}