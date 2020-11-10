package com.example.umpbizgo.CustomerLoginRegister;

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
import com.example.umpbizgo.Customer.HomeActivity;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.Models.Customer;
import com.example.umpbizgo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class CustomerLoginActivity extends AppCompatActivity {
    private EditText emailinput, passwordinput ;
    private Button emailloginbtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        emailinput= findViewById(R.id.emailaddresslogininput);
        passwordinput = findViewById(R.id.emailloginpasswordinput);
        emailloginbtn = findViewById(R.id.emailloginbutton);

        mAuth = FirebaseAuth.getInstance();


        progressBar = findViewById(R.id.progressBar);

        ///// Maintain the screen orientation portrait ///////
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ///// Hide Status Bar Start//////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ///// Hide Status Bar End//////

        emailloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUserwithEmail();
            }
        });
    }



    private void loginUserwithEmail() {
        String emailAddress,password;
        emailAddress=emailinput.getText().toString();
        password= passwordinput.getText().toString();

        ///Error Message Display when User Fail to Comply with the Required Information input///
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

        //Authenticate the user with FIREBASE Email Authentication//
        mAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CustomerLoginActivity.this, "User logged in successful", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                    Intent intent = new Intent(CustomerLoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CustomerLoginActivity.this, "Error!!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void BackToMainPage(View view) {
        Intent intent = new Intent(CustomerLoginActivity.this, MainActivity.class);
        startActivity(intent);
        Animatoo.animateSlideUp(this);
        finish();
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(CustomerLoginActivity.this, CustomerRegisterActivity.class);
        startActivity(intent);
        Animatoo.animateSwipeLeft(this);
        finish();
    }
}