package com.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etNewPassword, etConfirmPassword;
    private Button btnResetPassword, btnBack;
    private TextView tvEmail;
    private FirebaseAuth mAuth;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mAuth = FirebaseAuth.getInstance();
        userEmail = getIntent().getStringExtra("email");

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnResetPassword = findViewById(R.id.btn_reset_password);
        btnBack = findViewById(R.id.btn_back);
        tvEmail = findViewById(R.id.tv_email);

        tvEmail.setText("Reset password for: " + userEmail);
    }

    private void setupClickListeners() {
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void resetPassword() {
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validation
        if (newPassword.isEmpty()) {
            etNewPassword.setError("New password is required");
            etNewPassword.requestFocus();
            return;
        }

        if (newPassword.length() < 6) {
            etNewPassword.setError("Password must be at least 6 characters");
            etNewPassword.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Please confirm your password");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords don't match");
            etConfirmPassword.requestFocus();
            return;
        }

        // Disable button to prevent multiple clicks
        btnResetPassword.setEnabled(false);
        btnResetPassword.setText("Resetting...");

        // First sign in the user with temporary credentials to update password
        // In a real app, you would use Firebase Admin SDK or custom token
        // For demo, we'll use the password reset email approach
        mAuth.sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(task -> {
                    btnResetPassword.setEnabled(true);
                    btnResetPassword.setText("Reset Password");

                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this,
                                "Password reset email sent. Please check your email and follow the link to reset your password.",
                                Toast.LENGTH_LONG).show();

                        // Navigate back to Sign In screen
                        Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = "Failed to send password reset email";
                        if (task.getException() != null) {
                            errorMessage = task.getException().getMessage();
                        }
                        Toast.makeText(ResetPasswordActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
