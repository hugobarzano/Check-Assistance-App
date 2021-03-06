package com.pdm.check_assistance;

    import android.content.Intent;
    import android.os.Bundle;
    import android.support.v7.app.AppCompatActivity;
    import android.util.Log;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.android.volley.Request;
    import com.android.volley.Response;
    import com.android.volley.VolleyError;

    import org.json.JSONObject;

    import java.util.HashMap;
    import java.util.Map;


    public class CheckQR  extends AppCompatActivity implements View.OnClickListener {

        private Button scanBtn;
        private TextView formatTxt, contentTxt;

        IntentResult scanningResult;
        String scanContent;


        //private RequestQueue requestQueue;
        String item;
        // Atributos
        private String URL_BASE = "http://dispositivosmoviles.herokuapp.com";
        private static final String URL_JSON = "/CheckFromQr/";


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_check_qr);
            scanBtn = (Button)findViewById(R.id.scan_button);
            formatTxt = (TextView)findViewById(R.id.scan_format);
            contentTxt = (TextView)findViewById(R.id.scan_content);
            Intent intent = getIntent();
            scanBtn.setOnClickListener(this);


        }

        public void onClick(View v){
            if(v.getId()==R.id.scan_button){
                IntentIntegrator scanIntegrator = new IntentIntegrator(this);
                scanIntegrator.initiateScan();
            }

        }
        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
            scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanningResult != null) {
                scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                formatTxt.setText("FORMAT: " + scanFormat);
                contentTxt.setText("CONTENT: " + scanContent);

                //requestQueue = Volley.newRequestQueue(this);
                Map<String, String> params = new HashMap<String, String>();
                params.put("scaner", scanContent);
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
            else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No hay datos tio!", Toast.LENGTH_SHORT);
                toast.show();
            }
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
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }
