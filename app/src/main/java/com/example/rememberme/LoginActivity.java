package com.example.rememberme;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;
import android.widget.TextView;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {
    private IconEditText usernameInput;
    private IconEditText passwordInput;
    private TextView incorrectUsername;
    private TextView incorrectPassword;
    private View signUpPrompt;

    private MaterialButton loginButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        databaseHelper = new DatabaseHelper(this);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        incorrectUsername = findViewById(R.id.incorrectUsername);
        incorrectPassword = findViewById(R.id.incorrectPassword);
        signUpPrompt = findViewById(R.id.signUpPrompt);
        loginButton = findViewById(R.id.loginButton);

        signUpPrompt.setOnClickListener(view -> openCreateAccountDialog());
        loginButton.setOnClickListener(view -> attemptLogin());
    }

    private void openCreateAccountDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_account, null);

        IconEditText createUsernameInput = dialogView.findViewById(R.id.createUsernameInput);
        IconEditText createPasswordInput = dialogView.findViewById(R.id.createPasswordInput);
        IconEditText createConfirmPasswordInput = dialogView.findViewById(R.id.createConfirmPasswordInput);
        MaterialButton createAccountButton = dialogView.findViewById(R.id.createAccountButton);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        createAccountButton.setOnClickListener(v -> {
            String username = createUsernameInput.getText();
            String password = createPasswordInput.getText();
            String confirmPassword = createConfirmPasswordInput.getText();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (databaseHelper.userExists(username)) {
                Toast.makeText(this, "That username is already taken.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean created = databaseHelper.createUser(username, password);

            if (!created) {
                Toast.makeText(this, "Unable to create account.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Account created successfully.", Toast.LENGTH_SHORT).show();

            usernameInput.setText(username);
            passwordInput.setText(password);

            dialog.dismiss();
        });

        dialog.show();
    }

    private void attemptLogin() {
        String username = usernameInput.getText();
        String password = passwordInput.getText();

        incorrectUsername.setVisibility(View.GONE);
        incorrectPassword.setVisibility(View.GONE);

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter both username and password.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!databaseHelper.userExists(username)) {
            incorrectUsername.setVisibility(View.VISIBLE);
            return;
        }

        if (!databaseHelper.validateLogin(username, password)) {
            incorrectPassword.setVisibility(View.VISIBLE);
            return;
        }

        openCalendarScreen(username);
    }

    private void openCalendarScreen(String currentUsername) {
        Intent intent = new Intent(this, CalendarActivity.class);
        intent.putExtra("currentUsername", currentUsername);

        startActivity(intent);
        finish();
    }
}