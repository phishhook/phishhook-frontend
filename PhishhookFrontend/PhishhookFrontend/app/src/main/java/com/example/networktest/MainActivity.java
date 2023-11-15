package com.example.networktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String ApiURL = "https://www.google.com";
        StringRequest jsonObjectRequest = new StringRequest (Request.Method.GET, ApiURL ,dataListener,errorListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
        System.out.println(getIntent().getData() + "+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+**+*+*++**+");

        Uri my_uri= getIntent().getData();

        if (my_uri == null) {
            return;
        }

        Intent i = new Intent();
        i.setPackage("com.android.chrome");
        i.setAction(Intent.ACTION_VIEW);
        System.out.println(getIntent().getData() + "  ***********");
        //Log.d("LINK", getIntent().getData().toString());
        i.setData(getIntent().getData());
        startActivity(i);

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("TEST #######");
    }

    Response.Listener<String> dataListener  = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

            //System.out.println(response.toString());
        }
    };

    Response.ErrorListener errorListener  = new Response.ErrorListener () {

        @Override
        public void onErrorResponse(VolleyError error) {
            System.out.println(error);
        }
    };
}