package com.example.umpbizgo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.umpbizgo.Fragments.LogOutFragment;
import com.example.umpbizgo.Fragments.OrderFragment;
import com.example.umpbizgo.Fragments.PaymentFragment;
import com.example.umpbizgo.Fragments.VideosFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //////////Change the color of status text/////////
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottonnavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);


    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigation =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {

                        case R.id.orders:
                            selectedFragment = new OrderFragment();
                            break;

                        case R.id.payment:
                            selectedFragment = new PaymentFragment();
                            break;

                        case R.id.videos:
                            selectedFragment = new VideosFragment();
                            break;

                        case R.id.goout:
                            selectedFragment = new LogOutFragment();
                            break;
                    }
                    ///////////////Replacing by default fragment on home activity/////////////////
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                            selectedFragment).commit();
                    return true;
                }
            };
}