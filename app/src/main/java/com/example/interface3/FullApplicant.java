package com.example.interface3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class FullApplicant extends AppCompatActivity  implements ApplicantDetailsNetworkTask.NetworkResponseListener {

    EditText editTextFullName, editTextPhoneNumber, editTextEgeScore, editTextPriority, editTextProfile, editTextComments;
    CheckBox checkBoxWillGetIn, checkBoxWillNotGetIn, checkBoxSubmitDocuments, checkBoxAlreadyEnrolled, checkBoxDocumentsSubmitted, checkBoxOutsider, checkBoxTemporaryLeave, checkBoxCallback;
    Button btnSaveChanges;

    FullApplicantDetails selectedFullApplicantDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_details);

        String applicantId = getIntent().getStringExtra("applicantId");

        ApplicantDetailsNetworkTask networkTask = new ApplicantDetailsNetworkTask(this, applicantId);
        networkTask.execute();

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEgeScore = findViewById(R.id.editTextEgeScore);
        editTextPriority = findViewById(R.id.editTextPriority);
        editTextProfile = findViewById(R.id.editTextProfile);
        editTextComments = findViewById(R.id.editTextComments);

        checkBoxWillGetIn = findViewById(R.id.checkBoxWillGetIn);
        checkBoxWillNotGetIn = findViewById(R.id.checkBoxWillNotGetIn);
        checkBoxSubmitDocuments = findViewById(R.id.checkBoxSubmitDocuments);
        checkBoxAlreadyEnrolled = findViewById(R.id.checkBoxAlreadyEnrolled);
        checkBoxDocumentsSubmitted = findViewById(R.id.checkBoxDocumentsSubmitted);
        checkBoxOutsider = findViewById(R.id.checkBoxOutsider);
        checkBoxTemporaryLeave = findViewById(R.id.checkBoxTemporaryLeave);
        checkBoxCallback = findViewById(R.id.checkBoxCallback);

        btnSaveChanges = findViewById(R.id.btnSave);

        selectedFullApplicantDetails = (FullApplicantDetails) getIntent().getSerializableExtra("selectedFullApplicantDetails");

        if (selectedFullApplicantDetails != null) {
            editTextFullName.setText(selectedFullApplicantDetails.getFirstName() + " " + selectedFullApplicantDetails.getLastName() + " " + selectedFullApplicantDetails.getMiddleName());
            editTextPhoneNumber.setText(selectedFullApplicantDetails.getPhoneNumber());
            editTextEgeScore.setText(String.valueOf(selectedFullApplicantDetails.geteGE()));
            editTextPriority.setText(String.valueOf(selectedFullApplicantDetails.getPriority()));
            editTextProfile.setText(selectedFullApplicantDetails.getProfile());
            editTextComments.setText(selectedFullApplicantDetails.getComment());

            checkBoxWillGetIn.setChecked(selectedFullApplicantDetails.getCheckbox().contains("WillGetIn"));
            checkBoxWillNotGetIn.setChecked(selectedFullApplicantDetails.getCheckbox().contains("WillNotGetIn"));
            checkBoxSubmitDocuments.setChecked(selectedFullApplicantDetails.getCheckbox().contains("SubmitDocuments"));
            checkBoxAlreadyEnrolled.setChecked(selectedFullApplicantDetails.getCheckbox().contains("AlreadyEnrolled"));
            checkBoxDocumentsSubmitted.setChecked(selectedFullApplicantDetails.getCheckbox().contains("DocumentsSubmitted"));
            checkBoxOutsider.setChecked(selectedFullApplicantDetails.getCheckbox().contains("Outsider"));
            checkBoxTemporaryLeave.setChecked(selectedFullApplicantDetails.getCheckbox().contains("TemporaryLeave"));
            checkBoxCallback.setChecked(selectedFullApplicantDetails.getCheckbox().contains("Callback"));
        }
        btnSaveChanges.setOnClickListener(v -> {
            String fullName = editTextFullName.getText().toString().trim();
            String lastName = "";
            String firstName = "";
            String middleName = "";
            String[] parts = fullName.split(" ");

            if (parts.length >= 3) {
                lastName = parts[0];
                firstName = parts[1];
                middleName = parts[2];
            } else if (parts.length == 2) {
                lastName = parts[0];
                firstName = parts[1];
                middleName = "";
            }

            String phoneNumber = editTextPhoneNumber.getText().toString();
            int eGE = Integer.parseInt(editTextEgeScore.getText().toString());
            int priority = Integer.parseInt(editTextPriority.getText().toString());
            String profile = editTextProfile.getText().toString();

            List<String> checkbox = new ArrayList<>();
            if (checkBoxWillGetIn.isChecked()) {
                checkbox.add("Буду поступать!");
            }
            if (checkBoxWillNotGetIn.isChecked()) {
                checkbox.add("Не буду поступать");
            }
            if (checkBoxSubmitDocuments.isChecked()) {
                checkbox.add("Донесу документы");
            }
            if (checkBoxAlreadyEnrolled.isChecked()) {
                checkbox.add("Уже зачислен");
            }
            if (checkBoxDocumentsSubmitted.isChecked()) {
                checkbox.add("Документы сданы");
            }
            if (checkBoxOutsider.isChecked()) {
                checkbox.add("Иногородние");
            }
            if (checkBoxTemporaryLeave.isChecked()) {
                checkbox.add("Временно в отъезде");
            }
            if (checkBoxCallback.isChecked()) {
                checkbox.add("Перезвонить");
            }

            String comment = editTextComments.getText().toString();

            FullApplicantDetails updatedApplicantDetails = new FullApplicantDetails(firstName, lastName, middleName, phoneNumber, eGE, priority,
                    profile, checkbox, comment);

            ApplicantDetailsUpdaterTask task = new ApplicantDetailsUpdaterTask(applicantId, checkbox, new ApplicantDetailsUpdaterTask.NetworkResponseListener() {
                @Override
                public void onDataUpdated(boolean success) {
                    if (success) {
                        Toast.makeText(getApplicationContext(), "Данные успешно сохранены в CRM", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Ошибка при сохранении данных в CRM", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            task.execute(updatedApplicantDetails);
        });

        editTextPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = editTextPhoneNumber.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            }
        });
    }

    @Override
    public void onDataReceived(FullApplicantDetails fullApplicantDetails, List<String> checkboxNames) {

        selectedFullApplicantDetails = fullApplicantDetails;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (selectedFullApplicantDetails != null) {
                    editTextFullName.setText(selectedFullApplicantDetails.getLastName() + " " + selectedFullApplicantDetails.getFirstName() + " " + selectedFullApplicantDetails.getMiddleName());
                    editTextPhoneNumber.setText(selectedFullApplicantDetails.getPhoneNumber());
                    editTextEgeScore.setText(String.valueOf(selectedFullApplicantDetails.geteGE()));
                    editTextPriority.setText(String.valueOf(selectedFullApplicantDetails.getPriority()));
                    editTextProfile.setText(selectedFullApplicantDetails.getProfile());

                    for (String name : checkboxNames) {
                        switch (name) {
                            case "Буду поступать!":
                                checkBoxWillGetIn.setChecked(true);
                                break;
                            case "Не буду поступать":
                                checkBoxWillNotGetIn.setChecked(true);
                                break;
                            case "Донесу документы":
                                checkBoxSubmitDocuments.setChecked(true);
                                break;
                            case "Уже зачислен":
                                checkBoxAlreadyEnrolled.setChecked(true);
                                break;
                            case "Документы сданы":
                                checkBoxDocumentsSubmitted.setChecked(true);
                                break;
                            case "Иногородние":
                                checkBoxOutsider.setChecked(true);
                                break;
                            case "Временно в отъезде":
                                checkBoxTemporaryLeave.setChecked(true);
                                break;
                            case "Перезвонить":
                                checkBoxCallback.setChecked(true);
                                break;
                        }
                    }

                    editTextComments.setText(selectedFullApplicantDetails.getComment());
                }
            }
        });
    }
}
