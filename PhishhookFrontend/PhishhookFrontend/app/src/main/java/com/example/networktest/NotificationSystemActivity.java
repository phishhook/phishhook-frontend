package com.example.networktest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;



import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NotificationSystemActivity extends Activity {

    private TextView resultTextView;

    private TextView urlView;

    private Button actionButton;

    private ImageView resultImageView;

    private RequestQueue queue;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notification_system);
        resultImageView = findViewById(R.id.resultImageView);
        resultTextView= findViewById(R.id.resultTextView);
        urlView = findViewById(R.id.linkResultTextView);
        actionButton = findViewById(R.id.actionButton);

        this.queue = Volley.newRequestQueue(this);


        // Receive the URL from the intent
        url = getIntent().getDataString();

        // Simulate link analysis (replace with your actual implementation)
        Intent linkAnalysisIntent = new Intent(this, LinkAnalysisActivity.class);
        linkAnalysisIntent.putExtra("url", url);
        startActivityForResult(linkAnalysisIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                // Link analysis succeeded
                // Retrieve the result data
                String resultData = data.getStringExtra("resultData");
                Log.d("Received resultData: ", resultData);
                displayResult(resultData);
                // Handle the result data accordingly
                // You can now use the 'resultData' in NotificationSystemActivity
            } else {
                // Link analysis failed or was canceled
                // Handle the result accordingly
            }
        }
    }

    // Update this method in NotificationSystemActivity
    private void displayResult(String result) {
        resultImageView.setVisibility(View.VISIBLE);
        resultTextView.setVisibility(View.VISIBLE);
        urlView.setVisibility(View.VISIBLE);
        urlView.setText(url);

        // Customize the logic based on your result values
        // Customize the logic based on your result values
        if (!result.equals("N/A")) {
            // Parse the result as a percentage
            float percent = Float.parseFloat(result.replace("%", ""));

            // Display logic based on the percentage
            if (percent > 50) {
                // Display a message and image for a safe link
                resultTextView.setText("This link is safe");
                resultImageView.setImageResource(R.drawable.check);
            } else {
                // Display a message and image for an unsafe link
                resultTextView.setText("This link is not safe");
                resultImageView.setImageResource(R.drawable.phishing);
            }
        } else {
            // Display a message and image for an undetermined link
            resultTextView.setText("Unable to determine safety");
        }
    }




}
