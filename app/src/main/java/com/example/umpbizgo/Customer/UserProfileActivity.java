package com.example.umpbizgo.Customer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "TAG" ;
    TextView username, email, password;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        username = findViewById(R.id.usernameview);
        email = findViewById(R.id.emailladressview);
        password = findViewById(R.id.passwordview);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "Error" + error);
                    return;
                }

                if(value.exists()){
                    username.setText(value.getString("username"));
                    email.setText(value.getString("email"));
                    password.setText(value.getString("password"));
                }
            }
        });


    }



    public void LogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(UserProfileActivity.this, MainActivity.class));
        finish();
    }
}