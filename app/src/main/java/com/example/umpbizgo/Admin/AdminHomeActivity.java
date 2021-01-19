package com.example.umpbizgo.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.Models.Admin;
import com.example.umpbizgo.R;
import com.example.umpbizgo.Seller.SellerHomeActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminHomeActivity extends AppCompatActivity {
    private CardView ApproveProduct, RegisterAdmin;
    private TextView AdminName;
    private DatabaseReference adminReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // Toolbar //
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AdminName = findViewById(R.id.adminnamedisplay);
        ApproveProduct = findViewById(R.id.approveproduct);
        RegisterAdmin = findViewById(R.id.registeradmin);

        adminnamedisplay(AdminName);

        ApproveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminProductApprovalActivity.class);
                startActivity(intent);
            }
        });

        RegisterAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminRegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void adminnamedisplay(TextView adminName) {
        adminReference = FirebaseDatabase.getInstance().getReference().child("Admin");
        adminReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childData : snapshot.getChildren()) {
                    String userID = childData.getKey();
                    if (snapshot.child(userID).exists()) {
                        Admin adminData = snapshot.child(userID).getValue(Admin.class);
                        adminName.setText(adminData.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Toolbar //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_logout_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.logout2:
                Intent intent2 = new Intent(AdminHomeActivity.this, MainActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;
        }
        return false;
    }
}