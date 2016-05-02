package com.pdm.check_assistance;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity implements View.OnClickListener {

    public static final String REGISTER_URL = "http://192.168.43.104:8000/androidRegister/";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_DNI = "dni";


    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextDni;

    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextDni = (EditText) findViewById(R.id.editTextDni);


        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(this);
    }

    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String dni = editTextDni.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")) {
                            Toast.makeText(Register.this, "Register OK", Toast.LENGTH_LONG).show();
                            openLogin();
                        } else
                            Toast.makeText(Register.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Register.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME, username);
                params.put(KEY_EMAIL, email);
                params.put(KEY_PASSWORD, password);
                params.put(KEY_DNI, dni);
                return params;
            }

        };

        // RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        gestorPeticiones.getCola().add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegister) {
            registerUser();
        }
    }

    private void openLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
