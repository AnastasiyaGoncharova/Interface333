package com.example.interface3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {
    private static final String TAG = "CallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state == null || !state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            return;
        }

        String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        if (phoneNumber != null) {
            Log.d(TAG, "Incoming call: " + phoneNumber);
            new Service(context).checkNumberInCRM(phoneNumber);
        }
    }
}