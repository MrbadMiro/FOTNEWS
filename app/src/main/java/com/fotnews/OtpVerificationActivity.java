package com.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class OtpVerificationActivity extends AppCompatActivity {

    private EditText etOtp;
    private Button btnVerifyOtp, btnResendOtp, btnBack;
    private TextView tvEmail, tvTimer;

    private String userEmail;
    private String generatedOtp;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        // Get email and OTP from previous activity
        userEmail = getIntent().getStringExtra("email");
        generatedOtp = getIntent().getStringExtra("otp");

        initViews();
        setupClickListeners();
        startTimer();
    }

    private void initViews() {
        etOtp = findViewById(R.id.et_otp);
        btnVerifyOtp = findViewById(R.id.btn_verify_otp);
        btnResendOtp = findViewById(R.id.btn_resend_otp);
        btnBack = findViewById(R.id.btn_back);
        tvEmail = findViewById(R.id.tv_email);
        tvTimer = findViewById(R.id.tv_timer);

        tvEmail.setText("Enter OTP sent to: " + userEmail);
    }

    private void setupClickListeners() {
        btnVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp();
            }
        });

        btnResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void verifyOtp() {
        String enteredOtp = etOtp.getText().toString().trim();

        if (enteredOtp.isEmpty()) {
            etOtp.setError("OTP is required");
            etOtp.requestFocus();
            return;
        }

        if (enteredOtp.length() != 6) {
            etOtp.setError("OTP must be 6 digits");
            etOtp.requestFocus();
            return;
        }

        if (enteredOtp.equals(generatedOtp)) {
            Toast.makeText(this, "OTP verified successfully", Toast.LENGTH_SHORT).show();

            // Navigate to reset password screen
            Intent intent = new Intent(OtpVerificationActivity.this, ResetPasswordActivity.class);
            intent.putExtra("email", userEmail);
            startActivity(intent);
            finish();
        } else {
            etOtp.setError("Invalid OTP");
            etOtp.requestFocus();
            Toast.makeText(this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void resendOtp() {
        // Generate new OTP
        generatedOtp = generateOTP();

        // In real app, send new OTP via email
        Toast.makeText(this, "New OTP sent: " + generatedOtp, Toast.LENGTH_LONG).show();

        // Restart timer
        startTimer();
    }

    private String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        btnResendOtp.setEnabled(false);

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("Resend OTP in: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                tvTimer.setText("You can resend OTP now");
                btnResendOtp.setEnabled(true);
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
