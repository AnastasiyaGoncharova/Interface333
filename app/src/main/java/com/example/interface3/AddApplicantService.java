package com.example.interface3;

import android.content.Context;
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

public class AddApplicantService extends AsyncTask<String, Void, String> {
    private static final String TAG = "AddApplicantService";
    private Context mContext;
    private OnApplicantIdReceivedListener mListener;
    private String mPhoneNumber;

    public AddApplicantService(Context context, OnApplicantIdReceivedListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        mPhoneNumber = params[0];
        Log.d(TAG, "phoneNumber : " + mPhoneNumber);
        String url = "https://crm.elcity.ru/api/v1/Contact?offset=0&maxSize=20&phone=" + mPhoneNumber;
        String response = null;

        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestProperty("X-Api-Key", "35394ef793765af61ffc73725315ff2f");
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    responseBuilder.append(line);
                }
                in.close();
                response = responseBuilder.toString();
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error during HTTP request: " + e.getMessage());
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            handleResponse(result);
        }
    }

    private void handleResponse(String result) {
        String applicantId = null;
        try {
            JSONObject responseObj = new JSONObject(result);
            JSONArray applicantList = responseObj.optJSONArray("list");

            if (applicantList != null && applicantList.length() > 0) {
                for (int i = 0; i < applicantList.length(); i++) {
                    JSONObject applicant = applicantList.getJSONObject(i);
                    String phone = applicant.optString("phoneNumber");

                    if (phone.equals(mPhoneNumber)) {
                        applicantId = applicant.optString("id");
                        break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error processing request result: " + e.getMessage());
        }

        if (mListener != null) {
            mListener.onApplicantIdReceived(applicantId);
        }
    }

    public interface OnApplicantIdReceivedListener {
        void onApplicantIdReceived(String applicantId);
    }
}