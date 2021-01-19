package com.example.umpbizgo.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.umpbizgo.Models.Products;
import com.example.umpbizgo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminProductUnauthorizeActivity extends AppCompatActivity {
    private DatabaseReference ProductReference, AuthorizedProductReference;
    private Button unauthorizebtn;
    private ImageView productImage;
    private ImageButton backtoproductbutton;
    private TextView productPrice, productDescription, productName, brandName, productNameToolbar;
    private String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_unauthorize);

        Intent intent = getIntent();
        productID = intent.getExtras().getString("pid");

        ProductReference = FirebaseDatabase.getInstance().getReference().child("Unauthorized Products");
        AuthorizedProductReference = FirebaseDatabase.getInstance().getReference().child("Authorized Products");

        backtoproductbutton = findViewById(R.id.backtoadminproductpage);
        productNameToolbar = findViewById(R.id.adminproductnamedisplay);
        productName = findViewById(R.id.adminproduct_name_details);
        productDescription = findViewById(R.id.adminproductdetailsdescription);
        productPrice = findViewById(R.id.adminproduct_price_details);
        brandName = findViewById(R.id.adminproduct_brand_name);
        unauthorizebtn = findViewById(R.id.admin_unauthorize_button);
        productImage = findViewById(R.id.adminproductImageView);

        GetProductDetails(productID);

        backtoproductbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backtoproductpage();
            }
        });

        unauthorizebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unauthorizeproduct();
            }
        });

    }

    private void unauthorizeproduct() {
        AuthorizedProductReference.child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProductReference.child(productID).setValue(snapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()) {
                            AuthorizedProductReference.child(productID).removeValue();
                            Intent intent2 = new Intent(AdminProductUnauthorizeActivity.this, AdminProductApprovalActivity.class);
                            startActivity(intent2);
                        }
                        else {

                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void backtoproductpage() {
        Intent intent2 = new Intent(AdminProductUnauthorizeActivity.this, AdminProductApprovalActivity.class);
        startActivity(intent2);
    }

    private void GetProductDetails(String productID) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Authorized Products");
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Products products = snapshot.getValue(Products.class);
                    productNameToolbar.setText(products.getProductname());
                    productName.setText(products.getProductname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    brandName.setText(products.getsellerbusinessname());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}