package com.pdm.check_assistance;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CheckNFC extends AppCompatActivity {
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcTag";

    private String URL_BASE = "http://192.168.1.33:8000";
    private static final String URL_JSON = "/CheckFromNfc/";

    private TextView sesion;
    private NfcAdapter mNfcAdapter;
    private Button check_assistance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_nfc);

        sesion = (TextView) findViewById(R.id.sesion);
        check_assistance= (Button) findViewById(R.id.check_assistance);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Es necesario nfc
            Toast.makeText(this, "El dispositivo no soporta NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            sesion.setText("NFC is disabled.");
            Toast.makeText(this, "Activa NFC para continuar.", Toast.LENGTH_LONG).show();

        }

        handleIntent(getIntent());
    }




    @Override
    protected void onResume() {
        super.onResume();

        //Es necesario que la actividad se desarrolle en segundo plano o se producirá una excepción
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {

        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //Metodo llamado cuando se produce un nuevo inten, es decir, cuando el cliente acerca una tag al dispositivo
        handleIntent(intent);
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("comprueba el mime type!");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }


    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }
    //El lector NFC se lleva acabo en una tarea asincrona. Las cuales se definen como una subclase privada
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF no es soportada por la tag
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Codificación no soportada", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {


            byte[] payload = record.getPayload();

            // Codificación
            String textEncoding;
            if ((payload[0] & 128) != 0) textEncoding = "UTF-16";
            else textEncoding = "UTF-8";

            // Lenguaje
            int languageCodeLength = payload[0] & 0063;
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                //Utilizamos el resultado de la tarea asincrona para pasar asistencia. Podemos ejecutar directamente aqui el
                //envio de datos al servidor o añadir un botón para realizar esta acción.
                sesion.setText(result);
                //Envio

                //requestQueue = Volley.newRequestQueue(this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("nfc", result);
                params.put("dni",datosUsuario.getDni());

                CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, URL_BASE + URL_JSON, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response: ", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError response) {

                        Log.d("Response: ", response.toString());

                        Toast toast = Toast.makeText(getApplicationContext(), "Checked! ", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });
                //requestQueue.add(jsObjRequest);
                gestorPeticiones.getCola().add(jsObjRequest);




            }
        }
    }

}
