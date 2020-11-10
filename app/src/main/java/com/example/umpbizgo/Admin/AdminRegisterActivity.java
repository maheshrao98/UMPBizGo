package com.example.umpbizgo.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminRegisterActivity extends AppCompatActivity {
    private static final String TAG = "TAG" ;
    private Button createAccountButton;
    private EditText InputName, InputEmail, InputPassword;
    private ProgressBar progressBaradminreg;
    private DatabaseReference RootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);

        ///// Maintain the screen orientation portrait ///////
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ///// Hide Status Bar Start//////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ///// Hide Status Bar End//////

        createAccountButton = findViewById(R.id.adminregisterbutton);
        InputName = findViewById(R.id.adminusernameinput);
        InputEmail = findViewById(R.id.adminemailinput);
        InputPassword = findViewById(R.id.adminpasswordinput);
        progressBaradminreg = findViewById(R.id.progressBaradreg);
        RootReference = FirebaseDatabase.getInstance().getReference().child("Admin");



        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String adminname = InputName.getText().toString();
        String adminemail = InputEmail.getText().toString();
        String adminpassword = InputPassword.getText().toString().trim();

        if(TextUtils.isEmpty(adminname))
        {
            InputName.setError("Username is required");
            return;
        }
        else if(TextUtils.isEmpty(adminemail))
        {
            InputEmail.setError("Email is required");
            return;
        }
        else if(TextUtils.isEmpty(adminpassword))
        {
            InputPassword.setError("Password is required");
            return;
        }
        else if(adminpassword.length() <6 )
        {
            InputPassword.setError("Password must be longer than 6 characters.");
            return;
        }

        progressBaradminreg.setVisibility(View.VISIBLE);

        String userID = RootReference.push().getKey();
        RootReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child(userID).exists()))
                {
                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("username", adminname);
                    userData.put("email", adminemail);
                    userData.put("password", adminpassword);
                    userData.put("userID",userID);


                    RootReference.child(userID).updateChildren(userData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "onSuccess: User profile is created for" + userID);
                                    progressBaradminreg.setVisibility(View.GONE);

                                    Intent intent = new Intent(AdminRegisterActivity.this, AdminHomeActivity.class);
                                    startActivity(intent);
                                }

                            });
                }
                else{
                    progressBaradminreg.setVisibility(View.GONE);
                    Toast.makeText(AdminRegisterActivity.this,"This account already exist, Please try again with a new email." ,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void BackToMainPage(View view) {
        Intent intent = new Intent(AdminRegisterActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        Animatoo.animateSlideUp(this);
        finish();
    }
}