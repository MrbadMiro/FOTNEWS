package com.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Random;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnSendOtp, btnBack;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        btnSendOtp = findViewById(R.id.btn_send_otp);
        btnBack = findViewById(R.id.btn_back);
    }

    private void setupClickListeners() {
        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtpToEmail();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendOtpToEmail() {
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }

        // Disable button to prevent multiple clicks
        btnSendOtp.setEnabled(false);
        btnSendOtp.setText("Sending OTP...");

        // Generate 6-digit OTP
        String otp = generateOTP();

        // In a real app, you would send this OTP via email service
        // For demo purposes, we'll show it in a toast and proceed
        Toast.makeText(this, "OTP sent to email: " + otp, Toast.LENGTH_LONG).show();

        // Navigate to OTP verification screen
        Intent intent = new Intent(ForgotPasswordActivity.this, OtpVerificationActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("otp", otp); // In real app, don't pass OTP like this
        startActivity(intent);
        finish();
    }

    private String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
