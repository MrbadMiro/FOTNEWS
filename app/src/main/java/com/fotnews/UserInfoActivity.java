package com.fotnews;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {

    private TextView tvUsername, tvEmail, tvJoinDate;
    private Button btnEditInfo, btnSignOut, btnBack;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Check if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(UserInfoActivity.this, SignInActivity.class));
            finish();
            return;
        }

        currentUserId = currentUser.getUid();

        initViews();
        setupClickListeners();
        loadUserInfo();
    }

    private void initViews() {
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        tvJoinDate = findViewById(R.id.tv_join_date);
        btnEditInfo = findViewById(R.id.btn_edit_info);
        btnSignOut = findViewById(R.id.btn_sign_out);
        btnBack = findViewById(R.id.btn_back);
    }

    private void setupClickListeners() {
        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditInfoDialog();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignOutDialog();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showSignOutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sign_out);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnConfirmSignOut = dialog.findViewById(R.id.btn_confirm_signout);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirmSignOut.setOnClickListener(v -> {
            signOut();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showEditInfoDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_info);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText etUsername = dialog.findViewById(R.id.et_username);
        EditText etEmail = dialog.findViewById(R.id.et_email);
        Button btnCancelEdit = dialog.findViewById(R.id.btn_cancel_edit);
        Button btnOkEdit = dialog.findViewById(R.id.btn_ok_edit);

        // Pre-fill current data
        etUsername.setText(tvUsername.getText().toString());
        etEmail.setText(tvEmail.getText().toString());

        btnCancelEdit.setOnClickListener(v -> dialog.dismiss());

        btnOkEdit.setOnClickListener(v -> {
            String newUsername = etUsername.getText().toString().trim();
            if (!newUsername.isEmpty()) {
                updateUsername(newUsername);
                dialog.dismiss();
            } else {
                Toast.makeText(UserInfoActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void loadUserInfo() {
        mDatabase.child("users").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String createdAt = dataSnapshot.child("createdAt").getValue(String.class);

                    tvUsername.setText(username != null ? username : "N/A");
                    tvEmail.setText(email != null ? email : "N/A");
                    tvJoinDate.setText("Joined: " + (createdAt != null ? createdAt : "N/A"));
                } else {
                    createUserData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserInfoActivity.this, "Failed to load user info: " +
                        databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            String username = email != null ? email.split("@")[0] : "User";

            Map<String, Object> userData = new HashMap<>();
            userData.put("username", username);
            userData.put("email", email);
            userData.put("createdAt", java.text.DateFormat.getDateTimeInstance().format(new java.util.Date()));

            mDatabase.child("users").child(currentUserId).setValue(userData)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            loadUserInfo();
                        } else {
                            Toast.makeText(UserInfoActivity.this, "Failed to create user data",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void updateUsername(String newUsername) {
        mDatabase.child("users").child(currentUserId).child("username").setValue(newUsername)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tvUsername.setText(newUsername);
                        Toast.makeText(UserInfoActivity.this, "Username updated successfully",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserInfoActivity.this, "Failed to update username",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        Intent intent = new Intent(UserInfoActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
