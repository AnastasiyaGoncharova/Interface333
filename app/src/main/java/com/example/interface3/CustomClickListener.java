/*package com.example.interface3;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.List;
import java.util.Map;

public class CustomClickListener implements Applicant.OnClickListener {
    @Override
    public void onApplicantClicked(Applicant applicant) {
        Log.d("ApplicantClick", "Clicked on Applicant ID: " + applicant.getId());
    }

    public void bind(final Map<String, Object> item, final CustomAdapter.OnItemClickListener listener, View itemView) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Applicant applicant = new Applicant(
                        (String) item.get("firstName"),
                        (String) item.get("lastName"),
                        (String) item.get("middleName"),
                        (int) item.get("eGE"),
                        (int) item.get("priority"),
                        (String) item.get("profile"),
                        String.valueOf(item.get("id"))
                );

                onApplicantClicked(applicant);
                listener.onItemClick(applicant);
            }
        });
    }
}*/


