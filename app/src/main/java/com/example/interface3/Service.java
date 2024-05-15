package com.example.interface3;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Service {
    private static final String TAG = "Service";
    private Context context;

    public Service(Context context) {
        this.context = context;
    }

    public void checkNumberInCRM(String phoneNumber) {
        new AddApplicantService(context, new AddApplicantService.OnApplicantIdReceivedListener() {
            @Override
            public void onApplicantIdReceived(String applicantId) {
                if (applicantId != null) {
                    Log.d(TAG, "Received applicantId: " + applicantId);
                    Intent intent = new Intent(context, FullApplicant.class);
                    intent.putExtra("applicantId", applicantId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    Log.d(TAG, "Applicant not found.");
                }
            }
        }).execute(phoneNumber);
    }
}