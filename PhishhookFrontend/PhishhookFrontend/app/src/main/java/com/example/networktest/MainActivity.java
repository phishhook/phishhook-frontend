package com.example.networktest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private String MLApiURL = " http://ec2-34-226-215-44.compute-1.amazonaws.com/?url=";

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_main);
        String LinkApiURL = "http://ec2-18-224-251-242.us-east-2.compute.amazonaws.com:8080/links";
        StringRequest jsonLinkRequest = new StringRequest (Request.Method.GET, LinkApiURL , linkDataListener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // TODO: store key in env file
                params.put("X-API-KEY", "phishhookRyJHCenIz97Q5LIDPmHhDyg9eddxaBO29omDuzM1D5BsDRKH5mo3j8pmBehoO2Roj0Z4zWuDHlNW4AJVrSnLZF6lUravmyje13YB1LBriXHxYlxLUDYeXmV");
                return params;
            }
        };


        queue.add(jsonLinkRequest);
        System.out.println(getIntent().getData() + "+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+**+*+*++**+");

        Uri my_uri= getIntent().getData();

        if (my_uri != null) {
            String url = my_uri.toString();


            String extractedUrl = extractUrl(url);

            Uri uri = Uri.parse(extractedUrl);


            Log.d("Received link: ", uri.toString());

            // Launch NotificationSystemActivity
            Intent notificationIntent = new Intent(MainActivity.this, NotificationSystemActivity.class);
            notificationIntent.setData(uri);
            startActivity(notificationIntent);
        }
        Intent historyIntent = new Intent(MainActivity.this, LinkHistoryActivity.class);
        startActivity(historyIntent);


    }


    private static String extractUrl(String inputUrl) {
        String patternString = "q=([^&]+)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(inputUrl);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return inputUrl;
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        //System.out.println("TEST #######");
    }

    protected void linkAnalysis(String url) {
        StringRequest jsonMLRequest = new StringRequest (Request.Method.POST, MLApiURL + url, MLDataListener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // TODO: store key in env file
                params.put("X-API-KEY", "phishhookRyJHCenIz97Q5LIDPmHhDyg9eddxaBO29omDuzM1D5BsDRKH5mo3j8pmBehoO2Roj0Z4zWuDHlNW4AJVrSnLZF6lUravmyje13YB1LBriXHxYlxLUDYeXmV");
                return params;

            }
        };
        // ML model can take around 10 seconds to process a url; timeout set to 15 seconds just to be safe
        jsonMLRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonMLRequest);
    }

    Response.Listener<String> linkDataListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

            //System.out.println(response.toString());
        }
    };

    Response.Listener<String> MLDataListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {

            System.out.println(response.toString() + "&&&&&&&&&&&&&&&");
        }
    };

    Response.ErrorListener errorListener  = new Response.ErrorListener () {

        @Override
        public void onErrorResponse(VolleyError error) {
            System.out.println(error + "#########");
        }
    };
}