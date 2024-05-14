package com.example.interface3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneCallReceiver extends BroadcastReceiver {
    private static final String TAG = "PhoneCallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            // Запуск поиска соискателя по номеру телефона
            AddApplicant addApplicant = new AddApplicant(new AddApplicant.SearchResponseListener() {
                @Override
                public void onApplicantFound(String applicantId) {
                    if (applicantId != null) {
                        Intent intent = new Intent(context, FullApplicant.class);
                        intent.putExtra("applicantId", applicantId);
                        context.startActivity(intent);
                    } else {
                        Log.d(TAG, "Applicant not found for phone number: " + phoneNumber);
                    }
                }
            });
            addApplicant.execute(phoneNumber);
        }
    }
}