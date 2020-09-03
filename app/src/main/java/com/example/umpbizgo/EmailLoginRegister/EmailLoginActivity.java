package com.example.umpbizgo.EmailLoginRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.R;

public class EmailLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        ///// Hide Status Bar Start//////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ///// Hide Status Bar End//////
    }

    public void goToRegister(View view) {

            Intent intent = new Intent(EmailLoginActivity.this, EmailRegisterActivity.class);
            startActivity(intent);
            Animatoo.animateSwipeLeft(this);
            finish();

    }

    public void BackToMainPage(View view) {
        Intent intent = new Intent(EmailLoginActivity.this, MainActivity.class);
        startActivity(intent);
        Animatoo.animateSlideUp(this);
        finish();
    }
}