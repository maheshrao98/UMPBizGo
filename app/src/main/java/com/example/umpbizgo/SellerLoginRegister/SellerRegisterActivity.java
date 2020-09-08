package com.example.umpbizgo.SellerLoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.umpbizgo.CustomerLoginRegister.CustomerRegisterActivity;
import com.example.umpbizgo.HomeActivity;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.R;
import com.example.umpbizgo.Models.SellerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SellerRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText Username,BusinessName,Password,EmailAddress;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore mFirestore;
    private static final String TAG = "TAG" ;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_register);

        Username = findViewById(R.id.sellerregisterusernameinput);
        BusinessName = findViewById(R.id.sellerregisterbusinessinput);
        EmailAddress = findViewById(R.id.sellerregisteremailinput);
        Password = findViewById(R.id.sellerregisterpasswordinput);

        progressBar = findViewById(R.id.progressBarsellerreg);
        progressBar.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        findViewById(R.id.registersellerbutton).setOnClickListener(this);

        ///// Hide Status Bar Start//////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ///// Hide Status Bar End//////
    }

    @Override
    protected void onStart() {
        super.onStart();
        ///// Check if the user is already logged in/////
        if(firebaseAuth.getCurrentUser() !=null){
            ///// If User already logged in, then direct user to Home Activity/////
            Intent intent = new Intent(SellerRegisterActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void registerSeller() {
        final String businessname,emailaddress,password,username;
        emailaddress=EmailAddress.getText().toString().trim();
        password= Password.getText().toString().trim();
        username = Username.getText().toString().trim();
        businessname = BusinessName.getText().toString().trim();

        ///Seller Information Validation///
        if(TextUtils.isEmpty(emailaddress)){
            EmailAddress.setError("Email address is required.");
            EmailAddress.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Password.setError("Password is required.");
            Password.requestFocus();
            return;
        }

        if(password.length() < 6){
            Password.setError("Password must be longer than 6 characters.");
            Password.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(username)){
            Username.setError("Username is required.");
            Username.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(businessname)){
            BusinessName.setError("Business Name is required.");
            BusinessName.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //Register the user with FIREBASE Email Authentication and store information in CLOUD FIRESTORE//
        firebaseAuth.createUserWithEmailAndPassword(emailaddress,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            ///Store the additional information in firebase database////
                            userID = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = mFirestore.collection("Sellers")
                            .document(userID);
                            Map<String, Object> seller = new HashMap<>();
                            seller.put("username", username);
                            seller.put("email",emailaddress);
                            seller.put("password",password);
                            seller.put("businessname",businessname);
                            documentReference.set(seller).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: User profile is created for" + userID);
                                }
                            });
                            progressBar.setVisibility(View.GONE);

                            Intent intent = new Intent(SellerRegisterActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else{
                            Toast.makeText(SellerRegisterActivity.this,"Error!!!" + task.getException().getMessage() ,Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(SellerRegisterActivity.this, SellerLoginActivity.class);
        startActivity(intent);
        Animatoo.animateSwipeRight(this);
        finish();
    }

    public void BackToMainPage(View view) {
        Intent intent = new Intent(SellerRegisterActivity.this, MainActivity.class);
        startActivity(intent);
        Animatoo.animateSlideUp(this);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registersellerbutton:
                registerSeller();
                break;
        }

    }
}