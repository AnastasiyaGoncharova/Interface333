/*
package com.example.interface3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.example.interface3.R;
import androidx.appcompat.app.AppCompatActivity;

public class ApplicantDetailsActivity extends AppCompatActivity {

    EditText editTextFullName, editTextPhoneNumber, editTextEgeScore, editTextPriority, editTextProfile, editTextComments;
    CheckBox checkBoxWillGetIn, checkBoxWillNotGetIn, checkBoxSubmitDocuments, checkBoxAlreadyEnrolled, checkBoxDocumentsSubmitted, checkBoxOutsider, checkBoxTemporaryLeave, checkBoxCallback;
    Button btnSaveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_details);

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

        final Applicant selectedApplicant = (Applicant) getIntent().getSerializableExtra("selectedApplicant");

        if (selectedApplicant != null) {
            editTextFullName.setText(selectedApplicant.getFirstName() + selectedApplicant.getLastName() + selectedApplicant.getMiddleName());
            editTextPhoneNumber.setText(selectedApplicant.getPhoneNumber());
            editTextEgeScore.setText(String.valueOf(selectedApplicant.geteGE()));
            editTextPriority.setText(String.valueOf(selectedApplicant.getPriority()));
            editTextProfile.setText(selectedApplicant.getProfile());
            editTextComments.setText(selectedApplicant.getComments());

            checkBoxWillGetIn.setChecked(selectedApplicant.isWillGetIn());
            checkBoxWillNotGetIn.setChecked(selectedApplicant.isWillNotGetIn());
            checkBoxSubmitDocuments.setChecked(selectedApplicant.isSubmitDocuments());
            checkBoxAlreadyEnrolled.setChecked(selectedApplicant.isAlreadyEnrolled());
            checkBoxDocumentsSubmitted.setChecked(selectedApplicant.isDocumentsSubmitted());
            checkBoxOutsider.setChecked(selectedApplicant.isOutsider());
            checkBoxTemporaryLeave.setChecked(selectedApplicant.isTemporaryLeave());
            checkBoxCallback.setChecked(selectedApplicant.isCallback());
        }

        btnSaveChanges.setOnClickListener(v -> {
            String fullName = editTextFullName.getText().toString();
            String phoneNumber = editTextPhoneNumber.getText().toString();
            int egeScore = Integer.parseInt(editTextEgeScore.getText().toString());
            int priority = Integer.parseInt(editTextPriority.getText().toString());
            String profile = editTextProfile.getText().toString();
            String comments = editTextComments.getText().toString();

            selectedApplicant.setFirstName(fullName);
            selectedApplicant.setLastName(fullName);
            selectedApplicant.setMiddleName(fullName);
            selectedApplicant.setPhoneNumber(phoneNumber);
            selectedApplicant.seteGE(egeScore);
            selectedApplicant.setPriority(priority);
            selectedApplicant.setProfile(profile);
            selectedApplicant.setComments(comments);

            selectedApplicant.setWillGetIn(checkBoxWillGetIn.isChecked());
            selectedApplicant.setWillNotGetIn(checkBoxWillNotGetIn.isChecked());
            selectedApplicant.setSubmitDocuments(checkBoxSubmitDocuments.isChecked());
            selectedApplicant.setAlreadyEnrolled(checkBoxAlreadyEnrolled.isChecked());
            selectedApplicant.setDocumentsSubmitted(checkBoxDocumentsSubmitted.isChecked());
            selectedApplicant.setOutsider(checkBoxOutsider.isChecked());
            selectedApplicant.setTemporaryLeave(checkBoxTemporaryLeave.isChecked());
            selectedApplicant.setCallback(checkBoxCallback.isChecked());

            // Получение переданного объекта аппликанта
            Applicant applicant = (Applicant) getIntent().getSerializableExtra("clickedApplicant");

            EditText editTextFullName = findViewById(R.id.editTextFullName);
            editTextFullName.setText(applicant.getFirstName() + applicant.getLastName() + applicant.getMiddleName());

            EditText editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
            editTextPhoneNumber.setText(applicant.getPhoneNumber());

            EditText editTextEgeScore = findViewById(R.id.editTextEgeScore);
            editTextEgeScore.setText(String.valueOf(applicant.geteGE()));

            EditText editTextPriority = findViewById(R.id.editTextPriority);
            editTextPriority.setText(String.valueOf(applicant.getPriority()));

            EditText editTextProfile = findViewById(R.id.editTextProfile);
            editTextProfile.setText(applicant.getProfile());

            CheckBox checkBoxWillGetIn = findViewById(R.id.checkBoxWillGetIn);
            checkBoxWillGetIn.setChecked(applicant.isWillGetIn());

            CheckBox checkBoxWillNotGetIn = findViewById(R.id.checkBoxWillNotGetIn);
            checkBoxWillNotGetIn.setChecked(applicant.isWillNotGetIn());

            CheckBox checkBoxSubmitDocuments = findViewById(R.id.checkBoxSubmitDocuments);
            checkBoxSubmitDocuments.setChecked(applicant.isSubmitDocuments());

            CheckBox checkBoxAlreadyEnrolled = findViewById(R.id.checkBoxAlreadyEnrolled);
            checkBoxAlreadyEnrolled.setChecked(applicant.isAlreadyEnrolled());

            CheckBox checkBoxDocumentsSubmitted = findViewById(R.id.checkBoxDocumentsSubmitted);
            checkBoxDocumentsSubmitted.setChecked(applicant.isDocumentsSubmitted());

            CheckBox checkBoxOutsider = findViewById(R.id.checkBoxOutsider);
            checkBoxOutsider.setChecked(applicant.isOutsider());

            CheckBox checkBoxTemporaryLeave = findViewById(R.id.checkBoxTemporaryLeave);
            checkBoxTemporaryLeave.setChecked(applicant.isTemporaryLeave());

            CheckBox checkBoxCallback = findViewById(R.id.checkBoxCallback);
            checkBoxCallback.setChecked(applicant.isCallback());

            EditText editTextComments = findViewById(R.id.editTextComments);
            editTextComments.setText(applicant.getComments());

            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedApplicant", selectedApplicant);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }
}
*/
