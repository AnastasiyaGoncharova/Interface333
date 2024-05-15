package com.example.interface3;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ApplicantDetailsUpdaterTask extends AsyncTask<FullApplicantDetails, Void, Boolean> {

    private NetworkResponseListener listener;
    private String applicantId;
    private List<String> checkbox;

    public interface NetworkResponseListener {
        void onDataUpdated(boolean success);
    }

    public ApplicantDetailsUpdaterTask(String applicantId, List<String> checkbox, NetworkResponseListener listener) {
        this.listener = listener;
        this.applicantId = applicantId;
        this.checkbox = checkbox;
    }

    @Override
    protected Boolean doInBackground(FullApplicantDetails... applicantDetails) {
        boolean success = false;

        FullApplicantDetails updatedApplicantDetails = applicantDetails[0];

        try {
            success = updateDataInCRM(updatedApplicantDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

    private boolean updateDataInCRM(FullApplicantDetails updatedApplicantDetails) {
        boolean success = false;
        HttpURLConnection connection = null;

        try {
            URL url = new URL("https://crm.elcity.ru/api/v1/Contact/" + applicantId);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-Api-Key", "35394ef793765af61ffc73725315ff2f");

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("lastName", updatedApplicantDetails.getLastName());
            jsonRequest.put("firstName", updatedApplicantDetails.getFirstName());
            jsonRequest.put("middleName", updatedApplicantDetails.getMiddleName());
            jsonRequest.put("phoneNumber", updatedApplicantDetails.getPhoneNumber());
            jsonRequest.put("eGE", updatedApplicantDetails.geteGE());
            jsonRequest.put("priority", updatedApplicantDetails.getPriority());
            jsonRequest.put("profile", updatedApplicantDetails.getProfile());
            jsonRequest.put("checkbox", new JSONArray(checkbox));
            jsonRequest.put("comment", updatedApplicantDetails.getComment());
            OutputStream outputStream = connection.getOutputStream();
            String utf8JsonRequest = jsonRequest.toString();
            String unicodeEscapedJsonRequest = escapeUnicode(utf8JsonRequest);
            outputStream.write(unicodeEscapedJsonRequest.getBytes(StandardCharsets.UTF_8)); // Отправка данных в формате Unicode-escape
            outputStream.close();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                success = true;
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return success;
    }

    public static String escapeUnicode(String input) {
        StringBuilder builder = new StringBuilder();

        for (char ch : input.toCharArray()) {
            if (ch < 128) {
                builder.append(ch);
            } else {
                builder.append("\\u").append(String.format("%04x", (int) ch));
            }
        }

        return builder.toString();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (listener != null) {
            listener.onDataUpdated(success);
        }
    }
}