package com.example.sharearide;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class RegisterActivity extends AppCompatActivity {

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
                registerPost(etemail.getText().toString(), etpassword.getText().toString(), etfirstname.getText().toString(),
                        etlastname.getText().toString(), etphone.getText().toString(), etaddress.getText().toString(), etdob.getText().toString());
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

    private void registerPost(String email, String password, String firstname, String lastname, String phonenumber, String address, String DOB){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            //String URL = "https://sharearide-backend-production.up.railway.app/registeraccount";
            String URL = "http://192.168.0.144:5050/registeraccount";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("firstname", firstname);
            jsonBody.put("lastname", lastname);
            jsonBody.put("phonenumber", phonenumber);
            jsonBody.put("address", address);
            jsonBody.put("DOB", DOB);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}