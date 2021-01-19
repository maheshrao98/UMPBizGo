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

import com.example.umpbizgo.Customer.CartFragment;
import com.example.umpbizgo.Customer.HomeFragment;
import com.example.umpbizgo.Fragments.LogOutFragment;
import com.example.umpbizgo.Fragments.MyAccountBeforeLogin;
import com.example.umpbizgo.Fragments.PickCategoryFragment;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.R;
import com.example.umpbizgo.Seller.Orders.SellerOrderViewActivity;
import com.example.umpbizgo.Seller.Orders.SellerOrderViewFragment;
import com.example.umpbizgo.Seller.Products.AddProductActivity;
import com.example.umpbizgo.Seller.Products.SellerProductViewActivity;
import com.example.umpbizgo.Seller.SocialPosts.AddSocialPostsActivity;
import com.example.umpbizgo.Seller.SocialPosts.ManagePostsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerHomeActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private String userID;
    private CardView AddNewProduct, AddPosts, ManageProducts, ManageOrder, ManagePost, ManageShop;
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


        FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
        if(mFirebaseUser != null) {
            userID = mFirebaseUser.getUid(); //Do what you need to do with the id
        }

        shopnameview = findViewById(R.id.shopnamedisplay);
        usernamedisplay(shopnameview);

        //Shop Management Tools//
        AddNewProduct = findViewById(R.id.addnewproduct);
        ManageProducts = findViewById(R.id.manageproducts);
        ManageOrder = findViewById(R.id.manageorders);
        ManagePost = findViewById(R.id.manageposts);
        AddPosts = findViewById(R.id.addposts);
        ManageShop = findViewById(R.id.myshop);

        AddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SellerHomeActivity.this, AddProductActivity.class);
                startActivity(intent);
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

        AddPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SellerHomeActivity.this, AddSocialPostsActivity.class);
                startActivity(intent);
            }
        });

        ManagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SellerHomeActivity.this, ManagePostsActivity.class);
                startActivity(intent);
            }
        });

        ManageShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SellerHomeActivity.this, ShopProfileActivity.class);
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
                    Intent intent2 = new Intent(SellerHomeActivity.this, MainActivity.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);
                    break;
            }
        return false;
    }
}