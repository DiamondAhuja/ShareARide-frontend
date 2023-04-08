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
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

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

        login.setOnClickListener(view -> {
            if (etemail.getText().toString().isEmpty() && etpassword.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter both the values", Toast.LENGTH_SHORT).show();
                return;
            }
            loginPost(etemail.getText().toString(), etpassword.getText().toString());
        });

        configureRegisterButton();
    }

    private void configureRegisterButton(){
        Button loginButton = (Button) findViewById(R.id.switchregister);
        loginButton.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
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
        else{
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    public void onSuccess(JSONObject response) {}

    @Override
    public Context getContext() {
        return this;
    }
}