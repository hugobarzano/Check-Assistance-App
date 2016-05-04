package com.pdm.check_assistance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener{

    public static final String LOGIN_URL = "http://192.168.1.33:8000/androidLogin/";
    public static final String KEY_USERNAME="username";
    public static final String KEY_PASSWORD="password";

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;
    private Button buttonRegister;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        SharedPreferences preferences = getSharedPreferences("temp", getApplicationContext().MODE_PRIVATE);
        String login_name = preferences.getString("username", "");
        String login_dni = preferences.getString("dni","");


        if(login_name != ""&&login_dni!=""){
            Intent i = new Intent(this, Principal.class);
            datosUsuario.setNombre_usuario(login_name);
            datosUsuario.setDni(login_dni);
            startActivity(i);
            finish();
        }
    }


    private void userLogin() {
        username = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Your No-Inventory account is disabled."))
                            Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                        else if (response.trim().equals("Invalid login details supplied."))
                            Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                        else{
                            //Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();
                            datosUsuario.setNombre_usuario(username);
                            datosUsuario.setDni(response.trim());
                            SharedPreferences preferences = getSharedPreferences("temp", getApplicationContext().MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("username", datosUsuario.getNombre_usuario());
                            editor.putString("dni", datosUsuario.getDni());
                            editor.commit();
                            openPrincipal();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_USERNAME,username);
                map.put(KEY_PASSWORD,password);
                return map;
            }
        };

        //RequestQueue requestQueue = Volley.newRequestQueue(this);
        //requestQueue.add(stringRequest);
        gestorPeticiones.setCola(this);
        gestorPeticiones.getCola().add(stringRequest);
    }

    private void openPrincipal(){
        Intent intent = new Intent(this, Principal.class);
        //intent.putExtra(KEY_USERNAME, username);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogin){
            userLogin();
        }
        if(v == buttonRegister){
            startActivity(new Intent(this,Register.class));
        }

    }
}