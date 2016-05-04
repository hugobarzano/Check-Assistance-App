package com.pdm.check_assistance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Principal extends AppCompatActivity implements OnClickListener {

    public final static String ACTIVIDAD_SCANER = "com.machine.hugo.SCANER";
    private Button Scaner;

    private Button check_qr;

    private Button check_nfc;
    private Button log_out;


    private TextView username;
    private TextView dni;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //Obteniendo una instancia del boton show_pet_button
        // Scaner = (Button) findViewById(R.id.scaner);

        check_qr = (Button) findViewById(R.id.check_qr);
        check_nfc = (Button) findViewById(R.id.check_nfc);
        log_out = (Button) findViewById(R.id.logoutButt);


        //Nombre de usuario y organizacion
        username = (TextView) findViewById(R.id.usernameText);
        dni = (TextView) findViewById(R.id.dniText);

        username.setText("Usuario: " + datosUsuario.getNombre_usuario());
        dni.setText("DNI: " + (datosUsuario.getDni()));


        //Registrando la escucha sobre la actividad Main
        //Scaner.setOnClickListener(this);

        check_qr.setOnClickListener(this);
        check_nfc.setOnClickListener(this);
        log_out.setOnClickListener(this);


        ////////////////////Pruebas///////////////////////7

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.check_qr: {
                Intent intent = new Intent(this, CheckQR.class);
                startActivity(intent);
                break;
            }
            case R.id.check_nfc: {
                Intent intent = new Intent(this, CheckNFC.class);
                startActivity(intent);
                break;
            }
            case R.id.logoutButt: {
                SharedPreferences preferences = getSharedPreferences("temp", getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", "");
                editor.putString("dni", "");
                editor.commit();
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                break;
            }
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(this, "You have selected Bookmark Menu", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    //Menu de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    //Elemento del menu de opciones selecionado
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String position="hola";
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            Toast.makeText(Principal.this,
                    "Item in positn " + position + " clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}