package com.example.interface3;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApplicantDetailsNetworkTask extends AsyncTask<Void, Void, FullApplicantDetails> {
    private NetworkResponseListener listener;
    private String applicantId;
    private List<String> checkboxNames;

    public ApplicantDetailsNetworkTask(NetworkResponseListener listener, String applicantId) {
        this.listener = listener;
        this.applicantId = applicantId;
        this.checkboxNames = new ArrayList<>();
    }

    @Override
    protected FullApplicantDetails doInBackground(Void... voids) {
        FullApplicantDetails fullApplicantDetails = null;
        String urlString = "https://crm.elcity.ru/api/v1/Contact/" + applicantId;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Api-Key", "35394ef793765af61ffc73725315ff2f");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    responseBuilder.append(line);
                }

                JSONObject jsonResponse = new JSONObject(responseBuilder.toString());
                String firstName = jsonResponse.optString("firstName", "");
                String lastName = jsonResponse.optString("lastName", "");
                String middleName = jsonResponse.optString("middleName", "");
                String phoneNumber = jsonResponse.optString("phoneNumber", "");
                int eGE = jsonResponse.optInt("eGE", 0);
                int priority = jsonResponse.optInt("priority", 0);
                String profile = jsonResponse.optString("profile", "");
                JSONArray checkBoxArray = jsonResponse.optJSONArray("checkbox");
                if (checkBoxArray != null) {
                    for (int i = 0; i < checkBoxArray.length(); i++) {
                        checkboxNames.add(checkBoxArray.getString(i));
                    }
                }
                String comment = jsonResponse.optString("comment", "");

                fullApplicantDetails = new FullApplicantDetails(firstName, lastName, middleName, phoneNumber, eGE,
                        priority, profile, checkboxNames, comment);
            }

            connection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return fullApplicantDetails;
    }

    @Override
    protected void onPostExecute(FullApplicantDetails result) {
        super.onPostExecute(result);
        if (listener != null && result != null) {
            listener.onDataReceived(result, checkboxNames);
        }
    }

    public interface NetworkResponseListener {
        void onDataReceived(FullApplicantDetails fullApplicantDetails, List<String> checkboxNames);
    }
}