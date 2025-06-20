package com.fotnews;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DevInfoActivity extends AppCompatActivity {

    private Button btnBack, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devinfo);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnExit = findViewById(R.id.btn_exit);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnExit.setOnClickListener(v -> goToNewsPage());
    }

    private void goToNewsPage() {
        Intent intent = new Intent(DevInfoActivity.this, NewsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
