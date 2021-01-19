package com.example.umpbizgo.Customer.Order;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.umpbizgo.Customer.HomeActivity;
import com.example.umpbizgo.Models.Orders;
import com.example.umpbizgo.R;
import com.example.umpbizgo.Seller.Orders.SellerOrderViewFragment;
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
public class ViewShippedCustomerOrderDetailsFragment extends Fragment {
    View view;
    ImageButton backtoorder;
    TextView toolbarorderid, orderid, orderdate, productname, quantity, price, totalprice, trackingno, orderstatus;
    ImageView productimage;
    Button requestrefund, rateproduct;
    DatabaseReference orderreference, requestrefundreference;
    String orderID, rejectreason;

    public ViewShippedCustomerOrderDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view =  inflater.inflate(R.layout.fragment_view_shipped_order_details, container, false);

        backtoorder = view.findViewById(R.id.backtocuordershippedButton);
        toolbarorderid = view.findViewById(R.id.od_custshipped_order_id_toolbar);
        orderdate = view.findViewById(R.id.custorder_madetime);
        orderid = view.findViewById(R.id.custorder_id);
        productname = view.findViewById(R.id.custorder_product_name);
        quantity = (TextView) view.findViewById(R.id.custorder_product_quantity);
        price = view.findViewById(R.id.custorder_product_price);
        totalprice = view.findViewById(R.id.custorder_totalprice);
        trackingno = view.findViewById(R.id.tracking_no);
        orderstatus = view.findViewById(R.id.order_state);
        productimage = view.findViewById(R.id.custorder_product_image);
        requestrefund = view.findViewById(R.id.od_shipped_requestrefund_order_button);
        rateproduct = view.findViewById(R.id.od_shipped_rate_order_button);
        orderreference = FirebaseDatabase.getInstance().getReference().child("Shipped Orders");
        requestrefundreference = FirebaseDatabase.getInstance().getReference().child("Refund Orders");

        backtoorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                ViewShippedOrdersFragment fragorderproductview = new ViewShippedOrdersFragment();
                ft3.replace(R.id.frame_order_shipping, fragorderproductview);
                ft3.commit();
            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            orderID = bundle.getString("orderID");
        }

        GetOrderDetails(orderID);
        requestrefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestRefund();
            }
        });

        rateproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RateProduct();
            }
        });


        return view;
    }

    private void RateProduct() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        RateOrderFragment fragcustorderview = new RateOrderFragment();
        Bundle bundle =new Bundle();
        bundle.putString("orderID",orderID);
        fragcustorderview.setArguments(bundle);
        ft.replace(R.id.frame_customer_shipped_order, fragcustorderview);
        ft.commit();
    }

    private void RequestRefund() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        RequestRefundOrderFragment fragcustorderview = new RequestRefundOrderFragment();
        Bundle bundle =new Bundle();
        bundle.putString("orderID",orderID);
        fragcustorderview.setArguments(bundle);
        ft.replace(R.id.frame_customer_shipped_order, fragcustorderview);
        ft.commit();
    }


    private void GetOrderDetails(String orderID) {
        orderreference.child(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Orders orders = snapshot.getValue(Orders.class);
                    toolbarorderid.setText(orderID);
                    productname.setText(orders.getProductname());
                    quantity.setText(orders.getQuantity());
                    price.setText(orders.getPrice());
                    totalprice.setText(orders.getTotalAmount());
                    Picasso.get().load(orders.getProductImage()).into(productimage);
                    orderid.setText("Order ID : " + orderID);
                    orderdate.setText("Order at : " + orders.getDate() + "" + orders.getTime());
                    trackingno.setText("Tracking No : " + orders.getTrackingno());
                    orderstatus.setText("Order Status : " + orders.getState());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}