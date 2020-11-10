package com.example.umpbizgo.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.R;

public class AdminHomeActivity extends AppCompatActivity {
    private CardView ApproveProduct, RegisterAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // Toolbar //
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.top_app_bar_seller);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        Intent intent = new Intent(AdminHomeActivity.this,
                                MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                }
                return false;
            }
        });



        ApproveProduct = findViewById(R.id.approveproduct);
        RegisterAdmin = findViewById(R.id.registeradmin);

        ApproveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminHomeActivity.this, AdminProductApprovalActivity.class);
                startActivity(intent);
            }
        });

        RegisterAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(AdminHomeActivity.this, AdminRegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}