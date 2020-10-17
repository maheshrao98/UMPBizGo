package com.example.umpbizgo.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.umpbizgo.Fragments.GoldFragment;
import com.example.umpbizgo.Fragments.LogOutFragment;
import com.example.umpbizgo.Fragments.MyAccountBeforeLogin;
import com.example.umpbizgo.Fragments.OrderFragment;
import com.example.umpbizgo.Fragments.VideosFragment;
import com.example.umpbizgo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SellerHomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        ///// Maintain the screen orientation portrait ///////
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
                            selectedFragment = new PickCategoryFragment();
                            break;

                        case R.id.products:
                            selectedFragment = new GoldFragment();
                            break;

                        case R.id.home:
                            selectedFragment = new VideosFragment();
                            break;

                        case R.id.feed:
                            selectedFragment = new LogOutFragment();
                            break;
                        case R.id.myaccount:
                            selectedFragment = new MyAccountBeforeLogin();
                            break;
                    }
                    ///////////////Replacing by default fragment on home activity/////////////////
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                            selectedFragment).commit();
                    return true;
                }
            };
}