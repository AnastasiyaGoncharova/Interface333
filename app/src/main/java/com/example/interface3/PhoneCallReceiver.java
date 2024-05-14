/*package com.example.interface3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PhoneCallReceiver extends BroadcastReceiver {
    private String phoneNumber;
    private String applicantId;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            if (state != null && state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                phoneNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

                SharedPreferences sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("phoneNumber", phoneNumber);
                editor.apply();

                new Thread(() -> searchInCRM(context)).start();
            } else if (state != null && state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                resetData(context);
            }
        }
    }

    private void searchInCRM(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String applicantId = "-1";

        try {
            String phoneNumber = sharedPref.getString("phoneNumber", "");
            if (phoneNumber.isEmpty()) {
                Log.d("PhoneCallReceiver", "Error: phoneNumber is empty. Aborting CRM search.");
                return;
            }
            String url = "https://crm.elcity.ru/api/v1/Contact?offset=0&maxSize=20&phone=" + URLEncoder.encode(phoneNumber, "UTF-8");
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestProperty("X-Api-Key", "35394ef793765af61ffc73725315ff2f");
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();
            String response = responseBuilder.toString();

            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.has("list")) {
                JSONArray contacts = jsonResponse.getJSONArray("list");
                if (contacts.length() > 0) {
                    JSONObject contact = contacts.getJSONObject(0);
                    applicantId = contact.getString("id");
                }
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("applicantId", applicantId);
            editor.putString("phoneNumber", phoneNumber);
            editor.apply();

            Log.d("PhoneCallReceiver", "Data updated in SharedPreferences - phoneNumber: " + phoneNumber + ", applicantId: " + applicantId);
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(context, FullApplicant.class);
        intent.putExtra("applicantId", applicantId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void resetData(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("phoneNumber");
        editor.remove("applicantId");
        editor.apply();
    }
}*/
