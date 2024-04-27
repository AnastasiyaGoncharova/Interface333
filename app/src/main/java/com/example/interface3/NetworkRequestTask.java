package com.example.interface3;

import android.os.AsyncTask;

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
public class NetworkRequestTask extends AsyncTask<Void, Void, List<Applicant>> {
    private NetworkResponseListener listener;

    public NetworkRequestTask(NetworkResponseListener listener) {
        this.listener = listener;
    }

    @Override
    protected List<Applicant> doInBackground(Void... params) {
        List<Applicant> applicants = new ArrayList<>();
        String urlString = "https://crm.elcity.ru/api/v1/Contact?offset=0&maxSize=20";

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
                JSONArray jsonArray = jsonResponse.getJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonApplicant = jsonArray.getJSONObject(i);
                    String firstName = jsonApplicant.optString("firstName", "");
                    String lastName = jsonApplicant.optString("lastName", "");
                    String middleName = jsonApplicant.optString("middleName", "");
                    int eGE = jsonApplicant.optInt("eGE", 0);
                    int priority = jsonApplicant.optInt("priority", 0);
                    String profile = jsonApplicant.optString("profile", "");

                    // Получение id каждого абитуриента
                    String id = String.valueOf(jsonApplicant.optInt("id", 0));

                    Applicant applicant = new Applicant(firstName, lastName, middleName, eGE, priority, profile, id);
                    applicant.setId(id); // Установка id абитуриента

                    applicants.add(applicant);
                }
            }

            connection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return applicants;
    }

    @Override
    protected void onPostExecute(List<Applicant> result) {
        super.onPostExecute(result);
        if (listener != null) {
            listener.onDataReceived(result);
        }
    }

    public interface NetworkResponseListener {
        void onDataReceived(List<Applicant> applicants);
    }
}