package com.example.umpbizgo.CustomerLoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.umpbizgo.Customer.HomeActivity;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CustomerRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TAG" ;
    private EditText emailinput, passwordinput , usernameinput;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);

        ///// Maintain the screen orientation portrait ///////
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ///// Hide Status Bar Start//////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ///// Hide Status Bar End//////

        mAuth = FirebaseAuth.getInstance();

        usernameinput= findViewById(R.id.emailusernameinput);
        emailinput= findViewById(R.id.emailinput);
        passwordinput = findViewById(R.id.emailpasswordinput);

        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.customerregisterbutton).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        ///// Check if the user is already logged in/////
        if(mAuth.getCurrentUser() != null){
            ///// If User already logged in, then direct user to Home Activity/////
            Intent intent = new Intent(CustomerRegisterActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void registerCustomer() {
        String emailAddress,password,username;
        emailAddress=emailinput.getText().toString();
        password= passwordinput.getText().toString().trim();
        username = usernameinput.getText().toString();

        ///Customer Information Validation///
        if(TextUtils.isEmpty(emailAddress)){
            emailinput.setError("Email address is required.");
            return;
        }
        if(TextUtils.isEmpty(password)){
           passwordinput.setError("Password is required.");
            return;
        }

        if(password.length() < 6){
            passwordinput.setError("Password must be longer than 6 characters.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //Register the user with FIREBASE Email Authentication and store information in Realtime Database//
        mAuth.createUserWithEmailAndPassword(emailAddress,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CustomerRegisterActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                    userID = mAuth.getCurrentUser().getUid();
                    final DatabaseReference CustomerReference;
                    CustomerReference = FirebaseDatabase.getInstance().getReference();
                    HashMap<String, Object> user = new HashMap<>();
                    user.put("uid", userID);
                    user.put("username", username);
                    user.put("email",emailAddress);
                    user.put("password",password);
                    CustomerReference.child("Users").child(userID).updateChildren(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "onSuccess: User profile is created for" + userID);
                        }
                    });

                    progressBar.setVisibility(View.GONE);

                    Intent intent = new Intent(CustomerRegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(CustomerRegisterActivity.this,"Error!!!" + task.getException().getMessage() ,Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    /////Already have an account/////
    public void goToLogin(View view) {

        Intent intent = new Intent(CustomerRegisterActivity.this, CustomerLoginActivity.class);
        startActivity(intent);
        Animatoo.animateSwipeRight(this);
        finish();
    }

    /////Back to Main Page/////
    public void BackToMainPage(View view) {
        Intent intent = new Intent(CustomerRegisterActivity.this, MainActivity.class);
        startActivity(intent);
        Animatoo.animateSlideUp(this);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.customerregisterbutton:
                registerCustomer();
                break;
        }
    }
}