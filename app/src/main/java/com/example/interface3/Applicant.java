package com.example.interface3;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class Applicant implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String middleName;
    private int eGE;
    private int priority;
    private String profile;
    private boolean checked;
    private List<String> statuses;
    private Drawable applicantImageResource;

    public Applicant(String firstName, String lastName, String middleName, int eGE, int priority, String profile, String id, List<String>  statuses) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.eGE = eGE;
        this.priority = priority;
        this.profile = profile;
        this.checked = checked;
        this.id = id;
        if (statuses != null && !statuses.isEmpty()) {
            this.statuses = statuses;
        } else {
            this.statuses = Collections.singletonList("Статус не указан");
        }
    }

    public Drawable getImageResource(Context context, List<String> statuses) {
        if (statuses != null) {
            for (String status : statuses) {
                switch (status) {
                    case "Буду поступать!":
                        return ContextCompat.getDrawable(context, R.drawable.go);
                    case "Не буду поступать":
                        return ContextCompat.getDrawable(context, R.drawable.notgo);
                    case "Донесу документы":
                        return ContextCompat.getDrawable(context, R.drawable.docnest);
                    case "Уже зачислен":
                        return ContextCompat.getDrawable(context, R.drawable.zach);
                    case "Документы сданы":
                        return ContextCompat.getDrawable(context, R.drawable.docsdan);
                    case "Иногородние":
                    case "Временно в отъезде":
                    case "Перезвонить":
                        return ContextCompat.getDrawable(context, R.drawable.perezon);
                    default:
                        return ContextCompat.getDrawable(context, R.drawable.etstatus);
                }
            }
        }
        return ContextCompat.getDrawable(context, R.drawable.etstatus);
    }

    public void setApplicantImageResource(Drawable resource) {
        this.applicantImageResource = resource;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(String status) {
        this.statuses = statuses;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getMiddleName() { return middleName; }

    public int geteGE() { return eGE; }

    public int getPriority() {
        return priority;
    }

    public String getProfile() {
        return profile;
    }
}