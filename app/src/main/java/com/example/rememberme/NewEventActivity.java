package com.example.rememberme;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

public class NewEventActivity extends AppCompatActivity {
    private static final String TEST_PHONE_NUMBER = "5554";

    private String titleStr;
    private String currentUsername;
    private DatabaseHelper databaseHelper;

    private int eventId = -1;

    private IconEditText eventNameInput;
    private IconEditText eventDateInput;
    private IconEditText eventTimeInput;
    private IconEditText eventDescriptionInput;

    private final ActivityResultLauncher<String> smsPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    if (currentUsername != null && !currentUsername.isEmpty()) {
                        databaseHelper.setSmsPermissionState(currentUsername, 1);
                    }

                    Toast.makeText(this, "SMS permission granted", Toast.LENGTH_SHORT).show();
                    sendTestSms();
                } else {
                    if (currentUsername != null && !currentUsername.isEmpty()) {
                        databaseHelper.setSmsPermissionState(currentUsername, 0);
                    }

                    Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        titleStr = getIntent().getStringExtra("title");
        eventId = getIntent().getIntExtra("eventId", -1);

        currentUsername = getIntent().getStringExtra("username");
        databaseHelper = new DatabaseHelper(this);

        setContentView(R.layout.activity_new_event);

        TextView title = findViewById(R.id.newEventTitle);
        if (titleStr != null && !titleStr.isEmpty()) {
            title.setText(titleStr);
        }

        eventNameInput = findViewById(R.id.eventNameInput);
        eventDateInput = findViewById(R.id.eventDateInput);
        eventTimeInput = findViewById(R.id.eventTimeInput);
        eventDescriptionInput = findViewById(R.id.eventDescriptionInput);

        AppCompatImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());

        MaterialButton addAlarmButton = findViewById(R.id.addAlarmButton);
        addAlarmButton.setOnClickListener(view -> handleAddAlarm());

        MaterialButton saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> saveOrUpdateEvent());
    }

    private void saveOrUpdateEvent() {
        if (currentUsername == null || currentUsername.isEmpty()) {
            Toast.makeText(
                this,
                "No logged-in user found.",
                Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String eventName = eventNameInput.getText();
        String eventDate = eventDateInput.getText();
        String eventTime = eventTimeInput.getText();
        String eventDescription = eventDescriptionInput.getText();

        if (eventName.isEmpty() || eventDate.isEmpty() || eventTime.isEmpty()) {
            Toast.makeText(
                this,
                "Name, date, and time are required.",
                Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (eventId == -1) {
            long insertedId = databaseHelper.insertEvent(
                currentUsername,
                eventName,
                eventDate,
                eventTime,
                eventDescription
            );

            if (insertedId == -1) {
                Toast.makeText(
                    this,
                    "Unable to save event.",
                    Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Toast.makeText(this, "Event saved.", Toast.LENGTH_SHORT).show();
        } else {
            boolean updated = databaseHelper.updateEvent(
                eventId,
                currentUsername,
                eventName,
                eventDate,
                eventTime,
                eventDescription
            );

            if (!updated) {
                Toast.makeText(
                    this,
                    "Unable to update event.",
                    Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Toast.makeText(
                this,
                "Event updated.",
                Toast.LENGTH_SHORT
            ).show();
        }

        finish();
    }


    private void handleAddAlarm() {
        if (currentUsername == null || currentUsername.isEmpty()) {
            Toast.makeText(
                this,
                "No logged-in user found.",
                Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Integer smsPermissionState = databaseHelper.getSmsPermissionState(currentUsername);

        if (smsPermissionState != null && smsPermissionState == 1) {
            if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    this,
                    "Permission granted",
                    Toast.LENGTH_SHORT
                ).show();
                sendTestSms();
            } else {
                openSmsPermissionDialog();
            }
        } else {
            openSmsPermissionDialog();
        }
    }

    private void openSmsPermissionDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_sms_permission, null);

        MaterialButton allowSmsButton = dialogView.findViewById(R.id.allowSmsButton);
        MaterialButton denySmsButton = dialogView.findViewById(R.id.denySmsButton);

        AlertDialog dialog = new AlertDialog.Builder(this)
            .setView(dialogView)
            .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        allowSmsButton.setOnClickListener(v -> {
            dialog.dismiss();

            if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
            ) {
                databaseHelper.setSmsPermissionState(currentUsername, 1);
                Toast.makeText(
                    this,
                    "SMS permission allowed",
                    Toast.LENGTH_SHORT
                ).show();

                sendTestSms();
            } else {
                smsPermissionLauncher.launch(Manifest.permission.SEND_SMS);
            }
        });

        denySmsButton.setOnClickListener(v -> {
            databaseHelper.setSmsPermissionState(currentUsername, 0);
            Toast.makeText(
                this,
                "SMS permission denied",
                Toast.LENGTH_SHORT
            ).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    // test
    private void sendTestSms() {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(
                TEST_PHONE_NUMBER,
                null,
                "Reminder: Upcoming event.",
                null,
                null
            );

            Toast.makeText(this, "SMS sent", Toast.LENGTH_SHORT).show();
        } catch (Exception exception) {
            Toast.makeText(this, "SMS failed to send", Toast.LENGTH_SHORT).show();
        }
    }
}