package com.example.interface3;

import android.content.Intent;
import android.os.Bundle;
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
            String fullName = editTextFullName.getText().toString().trim(); // Получаем текст из EditText и удаляем лишние пробелы
            String lastName = "";
            String firstName = "";
            String middleName = "";
            String[] parts = fullName.split(" "); // Разбиваем полное имя на части по пробелу

            if (parts.length >= 3) {
                lastName = parts[0];
                firstName = parts[1];
                middleName = parts[2];
            } else if (parts.length == 2) {
                lastName = parts[0];
                firstName = parts[1];
                middleName = ""; }

            String phoneNumber = editTextPhoneNumber.getText().toString();
            int eGE = Integer.parseInt(editTextEgeScore.getText().toString());
            int priority = Integer.parseInt(editTextPriority.getText().toString());
            String profile = editTextProfile.getText().toString();

            List<String> checkbox = new ArrayList<>();
            if (checkBoxWillGetIn.isChecked()) {
                checkbox.add("WillGetIn");
            }
            if (checkBoxWillNotGetIn.isChecked()) {
                checkbox.add("WillNotGetIn");
            }
            if (checkBoxSubmitDocuments.isChecked()) {
                checkbox.add("SubmitDocuments");
            }
            if (checkBoxAlreadyEnrolled.isChecked()) {
                checkbox.add("AlreadyEnrolled");
            }
            if (checkBoxDocumentsSubmitted.isChecked()) {
                checkbox.add("DocumentsSubmitted");
            }
            if (checkBoxOutsider.isChecked()) {
                checkbox.add("Outsider");
            }
            if (checkBoxTemporaryLeave.isChecked()) {
                checkbox.add("TemporaryLeave");
            }
            if (checkBoxCallback.isChecked()) {
                checkbox.add("Callback");
            }

            String comment = editTextComments.getText().toString();

            FullApplicantDetails updatedApplicantDetails = new FullApplicantDetails(firstName, lastName, middleName, phoneNumber, eGE, priority,
                    profile, checkbox, comment);

            ApplicantDetailsUpdaterTask task = new ApplicantDetailsUpdaterTask(applicantId, new ApplicantDetailsUpdaterTask.NetworkResponseListener() {
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
    }

        @Override
        public void onDataReceived(FullApplicantDetails fullApplicantDetails) {

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

                        checkBoxWillGetIn.setChecked(selectedFullApplicantDetails.getCheckbox().contains("WillGetIn"));
                        checkBoxWillNotGetIn.setChecked(selectedFullApplicantDetails.getCheckbox().contains("WillNotGetIn"));
                        checkBoxSubmitDocuments.setChecked(selectedFullApplicantDetails.getCheckbox().contains("SubmitDocuments"));
                        checkBoxAlreadyEnrolled.setChecked(selectedFullApplicantDetails.getCheckbox().contains("AlreadyEnrolled"));
                        checkBoxDocumentsSubmitted.setChecked(selectedFullApplicantDetails.getCheckbox().contains("DocumentsSubmitted"));
                        checkBoxOutsider.setChecked(selectedFullApplicantDetails.getCheckbox().contains("Outsider"));
                        checkBoxTemporaryLeave.setChecked(selectedFullApplicantDetails.getCheckbox().contains("TemporaryLeave"));
                        checkBoxCallback.setChecked(selectedFullApplicantDetails.getCheckbox().contains("Callback"));
                        editTextComments.setText(selectedFullApplicantDetails.getComment());
                    }
                }
            });
    }
}
