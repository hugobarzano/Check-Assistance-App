package com.pdm.check_assistance_master;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugo on 10/05/16.
 */
public class AlumnoAdapter extends ArrayAdapter {
    //private RequestQueue requestQueue;
    // Atributos

    private static final String TAG = "PostAdapterAlumno";
    List<Alumno> alumnos;


    public AlumnoAdapter(Context context, JSONObject jsonObject) {
        super(context, 0);

        alumnos = parseJson(jsonObject);
        //Funcionaaaaaaaaaaaaaaaaaaaaaaaaaaaa///////////////////////
        // requestQueue = Volley.newRequestQueue(context);

    }




    public List<Alumno> parseJson(JSONObject jsonObject){
        // Variables locales
        List<Alumno> alumnos = new ArrayList();
        JSONArray jsonArray= null;

        try {
            // Obtener el array del objeto
            jsonArray = jsonObject.getJSONArray("alumnos");

            for(int i=0; i<jsonArray.length(); i++){

                try {
                    JSONObject objeto= jsonArray.getJSONObject(i);

                    Alumno alumno = new Alumno(
                            objeto.getString("NOMBRE"),
                            objeto.getString("DNI"),
                            objeto.getString("assitencia"));


                    alumnos.add(alumno);

                } catch (JSONException e) {
                    Log.e(TAG, "Error de parsing: " + e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return alumnos;
    }

    @Override
    public int getCount() {
        return alumnos != null ? alumnos.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // View auxiliar
        View listAlumnosView;

        //Comprobando si el View no existe
        if (null == convertView) {
            //Si no existe, entonces inflarlo
            listAlumnosView = layoutInflater.inflate(
                    R.layout.alumno,
                    parent,
                    false);
        } else listAlumnosView = convertView;


        // Obtener el item actual
        Alumno alumno = alumnos.get(position);
        //Item item = items.getItems().get(position);

        // Obtener Views
        TextView textoNombre = (TextView) listAlumnosView.findViewById(R.id.nombre);
        TextView textoDni = (TextView) listAlumnosView.findViewById(R.id.dni);
        TextView textoAsistencia = (TextView) listAlumnosView.findViewById(R.id.asistencia);
        //textoAsistencia.setBackgroundColor(6);
        // Actualizar los Views
        textoNombre.setText(alumno.getNombre());
        textoDni.setText(alumno.getDni());
        textoAsistencia.setText(alumno.getAsistencia());
        if (alumno.getAsistencia().equals("True")) {

            //textoNombre.setBackgroundColor(Color.GREEN);
            //textoDni.setBackgroundColor(Color.GREEN);
            textoAsistencia.setTextColor(Color.GREEN);
        }
        else {
            //textoNombre.setBackgroundColor(Color.RED);
            //textoDni.setBackgroundColor(Color.RED);
            textoAsistencia.setTextColor(Color.RED);
        }






        return listAlumnosView;
    }
    //metodos para acceder a la lista de objetos items
    public List<Alumno> getListaItems(){
        return alumnos;
    }
    public Alumno getItemFromAdapter(int posicion){
        return alumnos.get(posicion);
    }
}
