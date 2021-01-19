package com.example.umpbizgo.Customer.Order;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umpbizgo.Customer.HomeActivity;
import com.example.umpbizgo.Models.Orders;
import com.example.umpbizgo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class RateOrderFragment extends Fragment {
    View view;
    ImageButton backtoorder;
    TextView toolbarorderid;
    EditText rateproduct;
    Button nextratedone;
    DatabaseReference orderreference, productreference, UserReference, productRatingsreference;
    String orderID, productID, userID, username, rateproductkey;


    public RateOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rate_order, container, false);
        backtoorder = view.findViewById(R.id.backtomainpage3Button);
        toolbarorderid = view.findViewById(R.id.od_rateshipped_order_id_toolbar);
        rateproduct = view.findViewById(R.id.rate_edit_text);
        nextratedone = view.findViewById(R.id.od_shipped_rate_done_button);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            orderID = bundle.getString("orderID");
        }

        orderreference = FirebaseDatabase.getInstance().getReference().child("Shipped Orders").child(orderID);
        productreference = FirebaseDatabase.getInstance().getReference().child("Authorized Products");
        UserReference = FirebaseDatabase.getInstance().getReference().child("Users");
        productRatingsreference = FirebaseDatabase.getInstance().getReference().child("Ratings");
        rateproductkey = productRatingsreference.push().getKey();
        backtoorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });

        UserReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            userID = snapshot.child("uid").getValue().toString();
                            username = snapshot.child("username").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        orderreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productID = snapshot.child("pid").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        nextratedone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RateProduct();
            }
        });

        return view;
    }

    private void RateProduct() {
        if(TextUtils.isEmpty(rateproduct.getText().toString()))
        {
            Toast.makeText(getActivity(), "Please add your thoughts.", Toast.LENGTH_SHORT).show();
        }
        else

            productRatingsreference.child(rateproductkey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    HashMap<String, Object> productMap = new HashMap<>();
                    productMap.put("rateproduct",rateproduct.getText().toString());
                    productMap.put("raterusername",username);
                    productMap.put("pid",productID);
                    productRatingsreference.child(rateproductkey).updateChildren(productMap);
                    Toast.makeText(getActivity(),"Rating recorded", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), CustomerOrderViewActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

}