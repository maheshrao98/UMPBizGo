package com.example.umpbizgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.umpbizgo.Adapters.PlateAdapter;
import com.example.umpbizgo.Admin.AdminLoginActivity;
import com.example.umpbizgo.Customer.HomeActivity;
import com.example.umpbizgo.CustomerLoginRegister.CustomerLoginActivity;
import com.example.umpbizgo.Models.PlateModel;
import com.example.umpbizgo.Seller.Registration.SellerLoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<PlateModel> plateModelList;
    private PlateAdapter plateAdapter;
    private LinearLayout customerContinueBtn,sellerContinueBtn,adminContinue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///// Maintain the screen orientation portrait ///////
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ///// Hide Status Bar Start//////
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ///// Hide Status Bar End//////

        customerContinueBtn = (LinearLayout) findViewById(R.id.linearlayoutcustomer) ;
        sellerContinueBtn = (LinearLayout) findViewById(R.id.linearlayoutseller) ;
        adminContinue = findViewById(R.id.linearlayoutadmin);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setKeepScreenOn(true);
        recyclerView.setHasFixedSize(true);

        plateModelList = new ArrayList<>();
        plateModelList.add(new PlateModel(R.drawable.foodplate));
        plateModelList.add(new PlateModel(R.drawable.foodplate));
        plateModelList.add(new PlateModel(R.drawable.foodplate));
        plateModelList.add(new PlateModel(R.drawable.foodplate));
        plateModelList.add(new PlateModel(R.drawable.foodplate));
        plateModelList.add(new PlateModel(R.drawable.foodplate));
        plateModelList.add(new PlateModel(R.drawable.foodplate));
        plateModelList.add(new PlateModel(R.drawable.foodplate));

        plateAdapter = new PlateAdapter(plateModelList,this);
        recyclerView.setAdapter(plateAdapter);
        plateAdapter.notifyDataSetChanged();

        ////////////call autoscroll////////////////
        autoScroll();

        ////////////continue with email/////////////////
       customerContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CustomerLoginActivity.class);
                startActivity(intent);
                Animatoo.animateSlideDown(MainActivity.this);
            }
        });

        ////////////continue with email/////////////////
        sellerContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SellerLoginActivity.class);
                startActivity(intent);
                Animatoo.animateSlideDown(MainActivity.this);
            }
        });

        ////////////continue with email/////////////////
        adminContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AdminLoginActivity.class);
                startActivity(intent);
                Animatoo.animateSlideDown(MainActivity.this);
            }
        });

    }

    public void autoScroll(){
        final int speedScroll = 0;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                if(count == plateAdapter.getItemCount())
                    count = 0;
                if (count < plateAdapter.getItemCount()){
                    recyclerView.smoothScrollToPosition(++count);
                    handler.postDelayed(this,speedScroll);
                }
            }
        };
        handler.postDelayed(runnable,speedScroll);
    }

    public void goToHomePage(View view) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        Animatoo.animateSwipeLeft(MainActivity.this);
    }
}