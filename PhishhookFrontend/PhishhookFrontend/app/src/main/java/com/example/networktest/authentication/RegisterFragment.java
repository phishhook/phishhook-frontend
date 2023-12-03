package com.example.networktest.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import com.example.networktest.LinkHistoryActivity;
import com.example.networktest.R;
import com.google.android.material.textfield.TextInputEditText;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    private TextInputEditText editTextPhoneNumber;
    private TextInputEditText editUsername;
    private Button buttonRegister;
    private TextView loginText;
    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_register.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.register_fragment, container, false);

        CountryCodePicker ccp = view.findViewById(R.id.ccp);
        TextInputEditText editTextPhoneNumber = view.findViewById(R.id.editTextPhoneNumber);
        editUsername = view.findViewById(R.id.editTextUsername);

        // Register the phone number EditText with the country code picker
        ccp.registerCarrierNumberEditText(editTextPhoneNumber);

        buttonRegister = view.findViewById(R.id.buttonLogin);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = editTextPhoneNumber.getText().toString().replace("-", "");
                String username = editUsername.getText().toString();
                if (isValidPhoneNumber(phoneNumber)) {
                    createUser(phoneNumber, username, new CreateUserCallback() {
                        @Override
                        public void onUserCreated(boolean success) {
                            // Run the code on the UI thread
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (success) {
                                        Log.d("User Check", "User creation success " + username);
                                        openHomeScreen(); // Open Home Screen on successful login
                                        Toast.makeText(getActivity(), "Welcome, " + username, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.d("Create User", "Create User: "+ username + " - " + phoneNumber + ": Failed.");
                                        Toast.makeText(getActivity(), "Credentials already exist.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    });
                } else {
                    // Show error to the user
                    editTextPhoneNumber.setError("Invalid phone number");
                }
            }
        });

        loginText = view.findViewById(R.id.sign_up_text);
        String fullText = "Already have an account? Log In Here";
        SpannableString spannableString = new SpannableString(fullText);
        // Set the color for the "Sign Up Here" part
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.secondary));
        int start = fullText.indexOf("Log In Here");
        int end = start + "Log In Here".length();
        spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the SpannableString back to the TextView
        loginText.setText(spannableString);

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginScreen();
            }
        });

        return view;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Add logic to validate the phone number here
        // For example, check if it's not empty and has 10 digits (modify as needed for your requirements)
        return phoneNumber != null && !phoneNumber.isEmpty() && phoneNumber.matches("\\d{10}");
    }

    private boolean isValidUsername(String username) {
        // Add logic to validate the phone number here
        // For example, check if it's not empty and has 10 digits (modify as needed for your requirements)
        return username != null && !username.isEmpty();
    }

    private void openLoginScreen() {
        LoginFragment loginFragment = new LoginFragment();

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, loginFragment, "LoginFragment")
                .addToBackStack(null)
                .commit();
    }

    private void openHomeScreen() {
        Intent intent = new Intent(getActivity(), LinkHistoryActivity.class);
        startActivity(intent);
    }

    public interface CreateUserCallback {
        void onUserCreated(boolean success);
    }

    private void createUser(String phoneNumber, String username, CreateUserCallback callback) {
        // Create OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Check if phoneNumber starts with 1, if not, prepend it for the USA code.
        // 651_252_9620
        if (!phoneNumber.startsWith("1") || phoneNumber.length() == 10) {
            phoneNumber = "1" + phoneNumber;
        }

        String url = "http://ec2-18-224-251-242.us-east-2.compute.amazonaws.com:8080/user";

        // Prepare the JSON data
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("phone_number", phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a request body with the appropriate content type and JSON data
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                json.toString()
        );

        // Build the request with the required header
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("X-API-Key", "phishhookRyJHCenIz97Q5LIDPmHhDyg9eddxaBO29omDuzM1D5BsDRKH5mo3j8pmBehoO2Roj0Z4zWuDHlNW4AJVrSnLZF6lUravmyje13YB1LBriXHxYlxLUDYeXmV")
                .build();

        // Execute the request in an AsyncTask or background thread
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("API Error", "Call failed", e);
                // Invoke the callback with false since the call failed
                callback.onUserCreated(false);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    Log.d("API Success", "Response received successfully");
                    // Parse the responseData to JSON
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        String user_id = jsonObject.optString("id", null);
                        if (user_id != null && !user_id.isEmpty()) {
                            // Invoke the callback with true and the username
                            callback.onUserCreated(true);
                        } else {
                            // Username not found, invoke the callback with false
                            callback.onUserCreated(false);
                        }
                    } catch (JSONException e) {
                        Log.e("API Error", "Failed to parse JSON", e);
                        callback.onUserCreated(false);
                    }
                } else {
                    Log.e("API Error", "Response received but request not successful. Response code: " + response.code());
                    // Invoke the callback with false since the request was not successful
                    callback.onUserCreated(false);
                }
            }
        });
    }
}