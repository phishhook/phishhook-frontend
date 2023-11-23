package com.example.networktest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LinkAnalysisActivity extends Activity {

    private String MLApiURL = "http://ec2-34-226-215-44.compute-1.amazonaws.com/?url=";
    private RequestQueue queue;

    private LottieAnimationView animationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Volley request queue
        queue = Volley.newRequestQueue(this);
        setContentView(R.layout.link_analysis);

        animationView = findViewById(R.id.animationView);
        // Example URL for link analysis
        String urlToAnalyze = getIntent().getStringExtra("url");

        // Perform link analysis
        linkAnalysis(urlToAnalyze);
    }

    protected void linkAnalysis(String url) {
        StringRequest jsonMLRequest = new StringRequest(Request.Method.POST, MLApiURL + url, MLDataListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                // TODO: store key in env file
                params.put("X-API-KEY", "phishhookRyJHCenIz97Q5LIDPmHhDyg9eddxaBO29omDuzM1D5BsDRKH5mo3j8pmBehoO2Roj0Z4zWuDHlNW4AJVrSnLZF6lUravmyje13YB1LBriXHxYlxLUDYeXmV");
                return params;

            }
        };
        // ML model can take around 10 seconds to process a URL; timeout set to 15 seconds just to be safe
        jsonMLRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonMLRequest);
    }

    Response.Listener<String> MLDataListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            // Set the result with data before finishing
            Intent resultIntent = new Intent();
            resultIntent.putExtra("resultData", response);
            setResult(Activity.RESULT_OK, resultIntent);

            animationView.setVisibility(View.GONE);
            // Finish the activity
            finish();
        }

    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            System.out.println(error + "#########");
            // Set the result with an error code
            Intent resultIntent = new Intent();
            resultIntent.putExtra("error", error);
            setResult(Activity.RESULT_CANCELED, resultIntent);

            animationView.setVisibility(View.GONE);
            // Finish the activity
            finish();
        }
    };
}
