package com.pdm.check_assistance_master;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button nfc_session;
    private Button set_session;
    private EditText session;
    private Button get_clase;
    private Button get_qr;

    public final static String ACTIVIDAD_NFC = "com.pdm.check_assistance_master.NFC";

    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nfc_session=(Button) findViewById(R.id.nfc_session);
        get_clase=(Button) findViewById(R.id.get_clase);
        get_qr=(Button) findViewById(R.id.get_qr);

        session= (EditText) findViewById(R.id.clave);
        set_session = (Button) findViewById(R.id.set_clave);

        set_session.setOnClickListener(this);
        nfc_session.setOnClickListener(this);
        get_clase.setOnClickListener(this);
        get_qr.setOnClickListener(this);

        c=this.getBaseContext();

        gestorGlobal.setListaAlumnos(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.nfc_session: {
                Intent intent = new Intent(this, SetSession.class);
                intent.putExtra(ACTIVIDAD_NFC, session.getText().toString());
                startActivity(intent);
                break;
            }
            case R.id.get_clase: {
               Intent intent = new Intent(this, ListaClase.class);
               startActivity(intent);
               break;
            }
            case R.id.get_qr: {
                Intent intent = new Intent(this, VisualizaSession.class);
                startActivity(intent);
                break;
            }
            case R.id.set_clave: {
                String URL_BASE = "http://dispositivosmoviles.herokuapp.com";
                String URL_JSON = "/setClaveAndroid/";
                Map<String, String> params = new HashMap<String, String>();

                params.put("flag", "True");
                params.put("clave",session.getText().toString());



                CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response: ", response.toString());


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError response) {
                        Log.d("Response: ", response.toString());
                    }
                });
                // RequestQueue requestQueue;
                //requestQueue.add(jsObjRequest);
                gestorPeticiones.setCola(c);
                gestorPeticiones.getCola().add(jsObjRequest);
                Toast.makeText(this, "Estableciendo Clave de Session", Toast.LENGTH_SHORT).show();

                break;
            }




        }

    }
}
