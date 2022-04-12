package com.example.numberbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.numberbook.classes.Contact;
import com.example.numberbook.services.ContactService;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.numberbook.services.PaysService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    final String TAG="Hey";
    ContactService service=null;
    RequestQueue requestQueue;
    RequestQueue requestQueued;
    PaysService servicep;
    CountryCodePicker ccp;
    Button btnR;
    TextView txtResult;
    EditText txtTele;


    String insertUrl = "http://192.168.11.110:8080/Contacts";
    String SearchUrl = "http://192.168.11.110:8080/Contacts/search";

    String iddev;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iddev = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)+"";
        btnR= findViewById(R.id.btnRecherche);
        service=ContactService.getInstance();
        requestQueued = Volley.newRequestQueue(this);

        txtResult = findViewById(R.id.show);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        txtTele = findViewById(R.id.editText);
        servicep = PaysService.getInstance();
        Bundle extras = getIntent().getExtras();
        btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                JSONObject js = new JSONObject();
                try {
                    js.put("telephone",txtTele.getText().toString().substring(1) );
                    js.put("prefix", ccp.getSelectedCountryCodeWithPlus()+"");

                    Log.d(TAG, "onClick: "+txtTele.getText().toString().substring(1)+"--------"+ccp.getSelectedCountryCodeWithPlus()+"");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Make request for JSONObject
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                        Request.Method.POST, SearchUrl, js,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    txtResult.setText("Le nom avec le numero "+response.get("telephone").toString() +" est :"+response.get("nom").toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        txtResult.setText("on a rien trouver");
                    }
                }) {

                    /**
                     * Passing some request headers
                     */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }

                };

                // Adding request to request queue
                requestQueued = Volley.newRequestQueue(getApplicationContext());

                requestQueued.add(jsonObjReq);








            }
        });
        if (extras != null) {
             value = extras.getString("firsttime").toString();
            Log.d(TAG, "onCreate: "+value);
            if(value.equals("1")){
                Thread t1 = new Thread(){
                    @Override
                    public void run() {
                        try {

                            loadContacts();
                            sleep(4000);


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Thread t2 = new Thread(){
                    @Override
                    public void run() {
                        try {

                            sleep(3000);
                            addToDataBase();


                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };


                try {
                    t1.start();
                    t1.join();
                    t2.start();
                    t2.join();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }else {
                Log.d(TAG, "onCreate: false");
            }

        }




    }


    public void loadContacts()
    {
        Log.d(TAG, "IMEI : "+iddev);
        service.getInstance().getContacts().clear();
        ContentResolver cr = this.getContentResolver();
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI.buildUpon().appendQueryParameter(ContactsContract.REMOVE_DUPLICATE_ENTRIES,"1").build(), null, null, null,null );
        while (phones.moveToNext()) {
            @SuppressLint("Range") String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if(phoneNumber.charAt(0)!='+'){
                phoneNumber="+212"+phoneNumber.substring(1);
                Log.d(TAG, "loadContacts: "+phoneNumber);
            }
            phoneNumber=phoneNumber.replace("-","");
            phoneNumber=phoneNumber.replace(" ","");
            String prfx="+212";
            if (phoneNumber.length()==12) {
                prfx = phoneNumber.substring(0, 3);
                phoneNumber=phoneNumber.substring(3);

            }
            else if(phoneNumber.length()==13) {
                prfx = phoneNumber.substring(0, 4);
                phoneNumber=phoneNumber.substring(4);

            }

            Log.d(TAG, "loadContacts: "+  prfx);
            Log.d(TAG, "loadContacts: "+  servicep.findIdByPrefix(prfx).getIdP());


            service.getInstance().getContacts().add(new Contact(name,phoneNumber,iddev,servicep.findIdByPrefix(prfx).getIdP(),prfx));
        }

        phones.close();

    }
    public void addToDataBase(){
        for(Contact c : service.getInstance().getContacts()) {


            StringRequest request = new StringRequest(Request.Method.POST,
                    insertUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("nom", c.getNom());
                    params.put("telephone", c.getTelephone());
                    params.put("imei", c.getImei());
                    params.put("pays",c.getPays()+"");
                    params.put("prefix",c.getPrefix());
                    return params;
                }
            };
            requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(request);
        }
    }





    public void search(View v)
    {
        final CharSequence[] options = { "SMS", "Appel","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Information Contacts!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("SMS"))
                {


                }
                else if (options[item].equals("Appel"))
                {



                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }




}