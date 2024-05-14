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
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String middleName;
    private int eGE;
    private int priority;
    private String profile;
    private boolean checked;
    private boolean willGetIn;
    private boolean willNotGetIn;
    private boolean submitDocuments;
    private boolean alreadyEnrolled;
    private boolean documentsSubmitted;
    private boolean isOutsider;
    private boolean isTemporaryLeave;
    private boolean isCallback;
    private String comments;
    private List<String> statuses;
    private int imageResource;
    private Drawable applicantImageResource;
   /* private OnClickListener onClickListener;*/

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
        Log.d("Status", "Status value: " + statuses);
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

    public Drawable getApplicantImageResource() {
        return applicantImageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(String status) {
        this.statuses = statuses;
    }

    public String getId() { return id; } // Getter for id

    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getMiddleName() { return middleName; }

    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public int geteGE() { return eGE; }

    public void seteGE(int eGE) { this.eGE = eGE; }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getPriority() {
        return priority;
    }

    public String getProfile() {
        return profile;
    }

    public boolean isChecked() { return checked; }

    public boolean isWillGetIn() {
        return willGetIn;
    }

    public void setWillGetIn(boolean willGetIn) {
        this.willGetIn = willGetIn;
    }

    public boolean isWillNotGetIn() {
        return willNotGetIn;
    }

    public void setWillNotGetIn(boolean willNotGetIn) {
        this.willNotGetIn = willNotGetIn;
    }

    public boolean isSubmitDocuments() {
        return submitDocuments;
    }

    public void setSubmitDocuments(boolean submitDocuments) {
        this.submitDocuments = submitDocuments;
    }

    public boolean isAlreadyEnrolled() {
        return alreadyEnrolled;
    }

    public void setAlreadyEnrolled(boolean alreadyEnrolled) {
        this.alreadyEnrolled = alreadyEnrolled;
    }

    public boolean isDocumentsSubmitted() {
        return documentsSubmitted;
    }

    public void setDocumentsSubmitted(boolean documentsSubmitted) {
        this.documentsSubmitted = documentsSubmitted;
    }

    public boolean isOutsider() {
        return isOutsider;
    }

    public void setOutsider(boolean outsider) {
        isOutsider = outsider;
    }

    public boolean isTemporaryLeave() {
        return isTemporaryLeave;
    }

    public void setTemporaryLeave(boolean temporaryLeave) {
        isTemporaryLeave = temporaryLeave;
    }

    public boolean isCallback() {
        return isCallback;
    }

    public void setCallback(boolean callback) {
        isCallback = callback;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }


/*    public interface OnClickListener {
        void onApplicantClicked(Applicant applicant);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.onClickListener = listener;
    }

    public void performClick() {
        if (onClickListener != null) {
            onClickListener.onApplicantClicked(this);
        }
    }*/
}