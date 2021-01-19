package com.example.umpbizgo.Customer.Order;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
public class ViewUnshippedOrderDetailsFragment extends Fragment {
    View view;
    ImageButton backtoorder;
    TextView toolbarorderid, orderid, orderdate, productname, quantity, price, totalprice, trackingno, orderstatus;
    Button cancelorder;
    ImageView productimage;
    DatabaseReference orderreference, cancelorderreference;
    String orderID, cancelreason;

    public ViewUnshippedOrderDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_order_details, container, false);

        backtoorder = view.findViewById(R.id.backtoviewcustomerorderunshippedButton);
        toolbarorderid = view.findViewById(R.id.od_unshipped_order_id_toolbar);
        orderdate = view.findViewById(R.id.unorder_madetime);
        orderid = view.findViewById(R.id.unorder_id);
        productname = view.findViewById(R.id.unorder_product_name);
        quantity = (TextView) view.findViewById(R.id.unorder_product_quantity);
        price = view.findViewById(R.id.unorder_product_price);
        totalprice = view.findViewById(R.id.unorder_totalprice);
        trackingno = view.findViewById(R.id.untracking_no);
        orderstatus = view.findViewById(R.id.unorder_state);
        productimage = view.findViewById(R.id.unorder_product_image);
        cancelorder = view.findViewById(R.id.od_unshipped_cancel_order_button);

        orderreference = FirebaseDatabase.getInstance().getReference().child("Orders");
        cancelorderreference = FirebaseDatabase.getInstance().getReference().child("Cancelled Orders");

        backtoorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                ViewUnshippedOrdersFragment fragorderproductview = new ViewUnshippedOrdersFragment();
                ft3.replace(R.id.frame_cust_view_order_details, fragorderproductview);
                ft3.commit();
            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            orderID = bundle.getString("orderID");
        }

        GetOrderDetails(orderID);

        cancelorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CancelOrder();
            }
        });

        return view;
    }

    private void CancelOrder() {
        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
        CustomerCancelOrderFragment fragorderproductview = new CustomerCancelOrderFragment();
        Bundle bundle =new Bundle();
        bundle.putString("orderID",orderID);
        fragorderproductview.setArguments(bundle);
        ft3.replace(R.id.frame_cust_view_order_details, fragorderproductview);
        ft3.commit();
    }

    private void GetOrderDetails(String orderID) {
        orderreference.child(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Orders orders = snapshot.getValue(Orders.class);
                    toolbarorderid.setText(orderID);
                    productname.setText(orders.getProductname());
                    quantity.setText("Quantity : " +orders.getQuantity());
                    price.setText("Price: " +orders.getPrice());
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