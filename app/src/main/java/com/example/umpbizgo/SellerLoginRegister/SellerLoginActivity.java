package com.example.umpbizgo.SellerLoginRegister;

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
import com.example.umpbizgo.Customer.UserProfileActivity;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {
    private EditText emailinput, passwordinput ;
    private Button sellerloginbtn;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        mAuth = FirebaseAuth.getInstance();

        emailinput= findViewById(R.id.sellerloginemailinput);
        passwordinput = findViewById(R.id.sellerloginpasswordinput);
        sellerloginbtn = findViewById(R.id.buttonsellerlogin);

        progressBar = findViewById(R.id.progressBarsellerlog);

        ///// Maintain the screen orientation portrait ///////
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ///// Hide Status Bar Start//////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ///// Hide Status Bar End//////

        sellerloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginSellerwithEmail();
            }
        });
    }

    private void loginSellerwithEmail() {
        String emailAddress,password;
        emailAddress=emailinput.getText().toString().trim();
        password= passwordinput.getText().toString().trim();

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

        mAuth.signInWithEmailAndPassword(emailAddress,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(SellerLoginActivity.this,"User logged in successful",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                    Intent intent = new Intent(SellerLoginActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SellerLoginActivity.this,"Error!!!" + task.getException().getMessage() ,Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void goToRegister(View view) {
        Intent intent = new Intent(SellerLoginActivity.this, SellerRegisterActivity.class);
        startActivity(intent);
        Animatoo.animateSwipeLeft(this);
        finish();
    }

    public void BackToMainPage(View view) {
        Intent intent = new Intent(SellerLoginActivity.this, MainActivity.class);
        startActivity(intent);
        Animatoo.animateSlideUp(this);
        finish();
    }
}