package com.example.umpbizgo.Seller.Orders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umpbizgo.Models.Orders;
import com.example.umpbizgo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
public class ViewShippedOrderDetailsFragment extends Fragment {
    View view;
    TextView productname, producttotalprice, productquantity, orderidtoolbar , Orderstatus;
    TextView postalinformation;
    ImageView productimage;
    ImageButton backtoorder;
    DatabaseReference orderReference;
    String orderID;


    public ViewShippedOrderDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_shipped_order_details_seller, container, false);
        orderReference = FirebaseDatabase.getInstance().getReference().child("Shipped Orders");
        productname = view.findViewById(R.id.od_shipping_product_name);
        productimage = view.findViewById(R.id.od_shipping_product_image);
        productquantity = view.findViewById(R.id.od_shipping_product_quantity);
        producttotalprice = view.findViewById(R.id.od_shipping_product_price);
        Orderstatus = view.findViewById(R.id.od_shipping_order_status);
        orderidtoolbar = view.findViewById(R.id.od_unshipped_order_id_toolbar);
        postalinformation = view.findViewById(R.id.od_shipping_order_postal);
        backtoorder = view.findViewById(R.id.backtoviewsellerorderunshippedButton);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
           orderID = bundle.getString("orderID");
        }

        getOrderDetails(orderID);

        backtoorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                SellerShippedOrdersFragment fragorderproductview = new SellerShippedOrdersFragment();
                ft3.replace(R.id.frame_order_shipping, fragorderproductview);
                ft3.commit();
            }
        });

        return view;
    }


    private void getOrderDetails(String orderID) {
        orderReference.child(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Orders orders = snapshot.getValue(Orders.class);
                    orderidtoolbar.setText(orderID);
                    productname.setText(orders.getProductname());
                    productquantity.setText(orders.getQuantity());
                    producttotalprice.setText(orders.getTotalAmount());
                    Orderstatus.setText(orders.getState());
                    postalinformation.setText(orders.getTrackingno());
                    Picasso.get().load(orders.getProductImage()).into(productimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}