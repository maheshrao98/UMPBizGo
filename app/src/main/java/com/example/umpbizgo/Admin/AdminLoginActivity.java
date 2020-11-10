package com.example.umpbizgo.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.Models.Admin;
import com.example.umpbizgo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {
    private EditText InputEmailAddress , InputPassword;
    private Button LoginButton;
    private DatabaseReference RootReference;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        ///// Maintain the screen orientation portrait ///////
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ///// Hide Status Bar Start//////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ///// Hide Status Bar End//////

        LoginButton = findViewById(R.id.adminloginbutton);
        InputEmailAddress = findViewById(R.id.adminaddresslogininput);
        InputPassword = findViewById(R.id.adminloginpasswordinput);
        progressBar = findViewById(R.id.progressBaradlog);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginAccount();
            }
        });

    }

    private void LoginAccount() {
        String email = InputEmailAddress.getText().toString();
        String password = InputPassword.getText().toString().trim();

        ///Error Message Display when User Fail to Comply with the Required Information input///
        if(TextUtils.isEmpty(email)){
            InputEmailAddress.setError("Email address is required.");
            return;
        }
        if(TextUtils.isEmpty(password)){
            InputPassword.setError("Password is required.");
            return;
        }

        if(password.length() < 6){
            InputPassword.setError("Password must be longer than 6 characters.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        RootReference = FirebaseDatabase.getInstance().getReference().child("Admin");
        RootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    String userID = childDataSnapshot.getKey();
                    if (snapshot.child(userID).exists()) {
                        Admin adminData = snapshot.child(userID).getValue(Admin.class);

                        if (adminData.getEmail().equals(email)) {
                            if (adminData.getPassword().equals(password)) {
                                Toast.makeText(AdminLoginActivity.this, "User logged in successful", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                                Intent intent = new Intent(AdminLoginActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(AdminLoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AdminLoginActivity.this, "Error! This account do not exist.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void BackToMainPage(View view) {
        Intent intent = new Intent(AdminLoginActivity.this, MainActivity.class);
        startActivity(intent);
        Animatoo.animateSlideUp(this);
        finish();
    }
}