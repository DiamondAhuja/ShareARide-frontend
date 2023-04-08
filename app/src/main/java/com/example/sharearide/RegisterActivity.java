package com.example.sharearide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sharearide.utils.QueryServer;
import com.example.sharearide.utils.ServerCallback;

import org.json.JSONObject;

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
        register.setOnClickListener(view -> {
            if (!etpassword.getText().toString().equals(etrepassword.getText().toString())) {
                Toast.makeText(RegisterActivity.this, "Please ensure passwords match!", Toast.LENGTH_LONG).show();
                etpassword.setText("");
                etrepassword.setText("");
                return;
            }
            else if (etpassword.getText().toString().length() == 0){
                Toast.makeText(RegisterActivity.this, "Please write a password!", Toast.LENGTH_LONG).show();
                return;
            }
            registerPost();
        });

        configureButton();
    }

    private void configureButton(){
        Button registerButton = (Button) findViewById(R.id.regloginbtn);
        registerButton.setOnClickListener(view -> finish());
    }

    private void registerPost(){
        QueryServer.register(this, etemail.getText().toString(), etpassword.getText().toString(),
                etfirstname.getText().toString(), etlastname.getText().toString(), etphone.getText().toString(),
                etaddress.getText().toString(), etdob.getText().toString());
    }

    @Override
    public void onDone(String response) {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }

    public void onSuccess(JSONObject response) {}

    @Override
    public Context getContext() {
        return this;
    }
}