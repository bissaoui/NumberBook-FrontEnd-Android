package com.example.numberbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.numberbook.classes.Contact;
import com.example.numberbook.classes.Pays;
import com.example.numberbook.services.ContactService;
import com.example.numberbook.services.PaysService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {
    String iddev;
    RequestQueue requestQueue;
    RequestQueue requestQueue1;
    ContactService service;
    PaysService servicep;

    String getUrl = "http://192.168.11.110:8080/Contacts";
    String getUrlPays = "http://192.168.11.110:8080/Pays";
    //String getUrl = "http://10.0.2.2:8080/Contacts";

    String firsttime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        service=ContactService.getInstance();
        servicep=PaysService.getInstance();
        iddev = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)+"";
        firsttime="0";

        Thread t1 = new Thread(){
            @Override
            public void run() {
                try {
                    StringRequest request = new StringRequest(Request.Method.GET,
                            getUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Type type = new TypeToken<Collection<Contact>>(){}.getType();
                            Collection<Contact>  contacts = new Gson().fromJson(response, type);

                            for (Contact a : contacts){
                                service.getContacts().add(new Contact(a.getNom(),a.getTelephone(),a.getImei()));
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            return null;
                        }
                    };
                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(request);

                    sleep(4000);


                    Log.d("TAG", "onCreate: " +service.getContacts().size()+iddev);

                     if(checkFirstTime()){
                         firsttime="1";
                    }
                     else {
                         firsttime="0";
                     }

                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    Log.d("TAG", "firsttime: " +firsttime);

                    intent.putExtra("firsttime",firsttime);
                    startActivity(intent);
                    SplashScreen.this.finish();




                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t2 = new Thread(){
            @Override
            public void run() {
                try {
                    StringRequest request1 = new StringRequest(Request.Method.GET,
                            getUrlPays, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Type type = new TypeToken<Collection<Pays>>(){}.getType();
                            Collection<Pays>  pays = new Gson().fromJson(response, type);

                            for (Pays a : pays){
                                servicep.getPays().add(new Pays(a.getIdP(),a.getPrefix(),a.getFlag()));
                                Log.d("TAG", "onResponse: "+a.getIdP()+a.getPrefix());

                            }
                            Log.d("TAG", "Pays: " +servicep.getPays().size());


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            return null;
                        }
                    };
                    requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                    requestQueue1.add(request1);
                    sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

            t1.start();
            t2.start();




    }
    public boolean checkFirstTime()
    {
        for (Contact c : service.getContacts()){
            Log.d("TAG", "checkFirstTime: " + c.getImei());
            if (c.getImei().equals(iddev)){

                return false;
            }
        }
        return true;
    }

}