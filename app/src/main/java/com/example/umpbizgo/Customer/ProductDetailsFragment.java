package com.example.umpbizgo.Customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.umpbizgo.Models.Products;
import com.example.umpbizgo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ProductDetailsFragment extends Fragment {
    private Button addtocartButton;
    private ImageView productImage;
    private ImageButton backtoproductbutton;
    private ElegantNumberButton numberButton;
    private TextView productPrice, productDescription, productName, brandName, productNameToolbar;
    private String productID = "";
    private FirebaseAuth firebaseAuth;
    View view;


    public ProductDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_details, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            productID = bundle.getString("pid");
        }

        firebaseAuth = FirebaseAuth.getInstance();


        backtoproductbutton = view.findViewById(R.id.backtoproductpage);
        addtocartButton = (Button)view.findViewById(R.id.pd_add_to_cart_button);
        numberButton = (ElegantNumberButton)view.findViewById(R.id.number_btn);
        productNameToolbar = view.findViewById(R.id.productnamedisplay);
        productImage = (ImageView)view.findViewById(R.id.detailImageView);
        productPrice = (TextView)view.findViewById(R.id.product_price_details);
        productDescription = (TextView)view.findViewById(R.id.productdetailsdescription);
        productName = (TextView)view.findViewById(R.id.product_name_details);
        brandName = view.findViewById(R.id.brand_name);


        getProductDetails(productID);
        addtocartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtoCartList();
            }
        });

        backtoproductbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackToProductPage();
            }
        });

        return view;
    }

    private void BackToProductPage() {
        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
        BrowseProductFragment fragproductsdetails = new BrowseProductFragment();
        ft3.replace(R.id.frame_prod_detail, fragproductsdetails);
        ft3.commit();
    }

    private void addtoCartList() {
        String saveCurrentDate, saveCurrenttime;

        Calendar callfordate = Calendar.getInstance();
        SimpleDateFormat currentdateformat = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentdateformat.format(callfordate.getTime());

        SimpleDateFormat currenttimeformat = new SimpleDateFormat("HH:mm:ss a");
        saveCurrenttime = currenttimeformat.format(callfordate.getTime());

        final DatabaseReference cartListReference = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("productname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrenttime);
        cartMap.put("quantity", numberButton.getNumber());
        cartMap.put("sellerbusinessname", brandName.getText().toString());

        cartListReference.child("User Cart View").child(firebaseAuth.getCurrentUser().getUid())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), " Added To Cart List", Toast.LENGTH_SHORT).show();

                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            BrowseProductFragment fragdetailsproduct = new BrowseProductFragment();
                            ft.replace(R.id.frame_prod_detail, fragdetailsproduct);
                            ft.commit();

                        }
                    }
                });

    }

    private void getProductDetails(String productID) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Products products = dataSnapshot.getValue(Products.class);
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