package com.example.umpbizgo.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.umpbizgo.Customer.HomeFragment;
import com.example.umpbizgo.Fragments.LogOutFragment;
import com.example.umpbizgo.Fragments.MyAccountBeforeLogin;
import com.example.umpbizgo.Fragments.PickCategoryFragment;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.R;
import com.example.umpbizgo.Seller.Orders.SellerOrderViewActivity;
import com.example.umpbizgo.Seller.Orders.SellerOrderViewFragment;
import com.example.umpbizgo.Seller.Products.AddProductFragment;
import com.example.umpbizgo.Seller.Products.SellerProductViewActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerHomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private FirebaseAuth firebaseAuth;
    private String userID;
    private CardView AddNewProduct, ManageProducts, ManageOrder;
    TextView shopnameview;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        ///// Maintain the screen orientation portrait ///////
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Toolbar //
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Firebase //
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomnavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);

        //final Fragment SellerOrderFragment = new SellerOrderViewFragment();
        //final Fragment AddProductFragment = new PickCategoryFragment();
        //final Fragment SellerHomeFragment = new HomeFragment();
        //final FragmentManager fm = getSupportFragmentManager();
        //Fragment active = SellerHomeFragment;

        //fm.beginTransaction().add(R.id.frameLayout, SellerOrderFragment, "3").hide(SellerOrderFragment).commit();
        //fm.beginTransaction().add(R.id.frameLayout, AddProductFragment, "2").hide(AddProductFragment).commit();
        //fm.beginTransaction().add(R.id.frameLayout, SellerHomeFragment, "1").hide(SellerHomeFragment).commit();

        shopnameview = findViewById(R.id.shopnamedisplay);
        usernamedisplay(shopnameview);

        //Shop Management Tools//
        AddNewProduct = findViewById(R.id.addnewproduct);
        ManageProducts = findViewById(R.id.manageproducts);
        ManageOrder = findViewById(R.id.manageorders);

        AddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager ft = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction = ft.beginTransaction();
                final AddProductFragment fragsellerhomeactivity = new AddProductFragment();
                fragmentTransaction.add(R.id.framesellerLayout, fragsellerhomeactivity).commit();
            }
        });

        ManageProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SellerHomeActivity.this, SellerProductViewActivity.class);
                startActivity(intent);
            }
        });

        ManageOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SellerHomeActivity.this, SellerOrderViewActivity.class);
                startActivity(intent);
            }
        });
    }

    private void usernamedisplay(TextView shopnameview) {
        DatabaseReference SellerReference = FirebaseDatabase.getInstance().getReference().child("Sellers").child(userID);
        SellerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String shopname = snapshot.child("businessname").getValue().toString();
                shopnameview.setText(shopname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigation =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {

                        case R.id.orders:
                            selectedFragment = new SellerOrderViewFragment();
                            break;

                        case R.id.products:
                            Intent productview = new Intent(SellerHomeActivity.this, SellerProductViewActivity.class);
                            startActivity(productview);
                            break;

                        case R.id.home:
                            selectedFragment = new HomeFragment();
                            break;

                        case R.id.feed:
                            selectedFragment = new PickCategoryFragment();
                            break;
                        case R.id.myaccount:
                            selectedFragment = new MyAccountBeforeLogin();
                            break;
                    }
                    ///////////////Replacing by default fragment on home activity/////////////////
                    getSupportFragmentManager().beginTransaction().replace(R.id.framesellerLayout,
                            selectedFragment).commit();
                    return true;
                }
            };

    // Toolbar //
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar_seller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.logout:
                selectedFragment = new LogOutFragment();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        ///////////////Replacing by default fragment on home activity/////////////////
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                selectedFragment).commit();
        return true;
    }

    //LogOut//
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, MainActivity.class));
    }
}