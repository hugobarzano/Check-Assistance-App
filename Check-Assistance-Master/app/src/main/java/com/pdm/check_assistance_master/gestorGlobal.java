package com.pdm.check_assistance_master;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hugo on 12/04/16.
 */
public class gestorGlobal {

    private static JSONObject listaAlumnos;



    public static JSONObject getListaAlumnos() {
        return listaAlumnos;
    }

    public static void setListaAlumnos(Context c) {
        String URL_BASE = "http://dispositivosmoviles.herokuapp.com";
        String URL_JSON = "/alumnosJson/";
        Map<String, String> params = new HashMap<String, String>();

        params.put("flag", "True");



        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response: ", response.toString());
                gestorGlobal.listaAlumnos = response;
                //adapter = new ItemAdapter(c,response);

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


    }




}
