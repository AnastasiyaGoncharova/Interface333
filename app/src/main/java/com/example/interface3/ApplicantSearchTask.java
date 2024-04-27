package com.example.interface3;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ApplicantSearchTask extends AsyncTask<String, Void, String> {
    private Context context;
    private SearchResponseListener listener;
    private Applicant searchApplicant;
    private static final String CRM_URL = "https://crm.elcity.ru/api/v1/Contact?offset=0&maxSize=20";

    public interface SearchResponseListener {
        void onApplicantFound(String applicantId);
    }

    public ApplicantSearchTask(SearchResponseListener listener, Applicant searchApplicant) {
        this.listener = listener;
        this.searchApplicant = searchApplicant;
    }

    protected String doInBackground(String... params) {
        String foundApplicantId = null;

        try {
            // Make API call to CRM
            URL url = new URL(CRM_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("X-Api-Key", "35394ef793765af61ffc73725315ff2f");
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String jsonResponse = convertStreamToString(in);

            // Parse JSON response
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray applicantList = jsonObject.getJSONArray("list");

            // Find applicant by matching firstName and lastName
            for (int i = 0; i < applicantList.length(); i++) {
                JSONObject applicant = applicantList.getJSONObject(i);
                String firstName = applicant.getString("firstName");
                String lastName = applicant.getString("lastName");

                if (firstName.equals(searchApplicant.getFirstName()) && lastName.equals(searchApplicant.getLastName())) {
                    foundApplicantId = applicant.getString("id");
                    break;
                }
            }

            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return foundApplicantId;
    }

    @Override
    protected void onPostExecute(String applicantId) {
        if (listener != null) {
            listener.onApplicantFound(applicantId);
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}