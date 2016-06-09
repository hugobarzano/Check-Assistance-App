package com.pdm.check_assistance_master;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ListaClase extends ActionBarActivity {
    public static ListView getListView() {
        return listView;
    }

    public static void setListView(ListView listView) {
        ListaClase.listView = listView;
    }

    // Atributos
    public static ListView listView;
    //adaptador con la lista de objetos item
    AlumnoAdapter adapter;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clase);
        c=this.getBaseContext();
        // Obtener instancia de la lista
        listView = (ListView) findViewById(R.id.listView);
        String URL_BASE = "http://dispositivosmoviles.herokuapp.com/";
        String URL_JSON = "/alumnosJson/";
        Map<String, String> params = new HashMap<String, String>();

        params.put("flag", "True");
        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response: ", response.toString());
                //actualizar lista
                adapter = new AlumnoAdapter(c,response);
                listView.setAdapter(adapter);
                //Asocio el menu contextual a la vista de la lista
                registerForContextMenu(listView);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError response) {
                Log.d("Response: ", response.toString());
            }
        });
        gestorPeticiones.setCola(c);
        gestorPeticiones.getCola().add(jsObjRequest);

        // Crear y setear adaptador
        //adapter = new ItemAdapter(this,"username",datosUsuario.getNombre_usuario());
        //gestorGlobal.setListaAlumnos(this);
       // adapter = new AlumnoAdapter(this,gestorGlobal.getListaAlumnos());
        //listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                //peticion
                String URL_BASE = "http://dispositivosmoviles.herokuapp.com/";
                String URL_JSON = "/alumnosJson/";
                Map<String, String> params = new HashMap<String, String>();

                params.put("flag", "True");
                CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response: ", response.toString());
                        //actualizar lista
                        adapter = new AlumnoAdapter(c,response);
                        listView.setAdapter(adapter);
                        //Asocio el menu contextual a la vista de la lista
                        registerForContextMenu(listView);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError response) {
                        Log.d("Response: ", response.toString());
                    }
                });
                gestorPeticiones.setCola(c);
                gestorPeticiones.getCola().add(jsObjRequest);





                Toast.makeText(this, "Lista Actualizada", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.inicializar:
                URL_BASE = "http://dispositivosmoviles.herokuapp.com/";
                URL_JSON = "/inicializarClase/";
                params = new HashMap<String, String>();

                params.put("flag", "True");
                jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response: ", response.toString());
                        //actualizar lista
                        AlumnoAdapter adapter = new AlumnoAdapter(c,response);
                        ListaClase.getListView().setAdapter(adapter);
                        //Asocio el menu contextual a la vista de la lista
                        //registerForContextMenu(listView);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError response) {
                        Log.d("Response: ", response.toString());
                    }
                });
                gestorPeticiones.setCola(c);
                gestorPeticiones.getCola().add(jsObjRequest);

                Toast.makeText(this, "Inicializacion Completada", Toast.LENGTH_SHORT).show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
