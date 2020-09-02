package com.example.umpbizgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.umpbizgo.Adapters.PlateAdapter;
import com.example.umpbizgo.EmailLoginRegister.EmailLoginActivity;
import com.example.umpbizgo.EmailLoginRegister.EmailRegisterActivity;
import com.example.umpbizgo.Models.PlateModel;
import com.example.umpbizgo.PhoneLoginRegister.PhoneLoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<PlateModel> plateModelList;
    private PlateAdapter plateAdapter;
    private LinearLayout emailContinueBtn,phoneContinueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///// Hide Status Bar Start//////
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        ///// Hide Status Bar End//////

        emailContinueBtn = (LinearLayout) findViewById(R.id.linearlayoutemail) ;
        phoneContinueBtn = (LinearLayout) findViewById(R.id.linearlayoutphone) ;

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

        ////////////call autoscroll start////////////////
        autoScroll();
        ////////////call autoscroll end/////////////////

        ////////////continue with email/////////////////
        emailContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EmailLoginActivity.class);
                startActivity(intent);
                Animatoo.animateSlideDown(MainActivity.this);
            }
        });

        ////////////continue with email/////////////////
        phoneContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PhoneLoginActivity.class);
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
}