package com.example.umpbizgo.Seller.Orders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

public class EditShippedOrdersFragment extends Fragment {
    View view;
    TextView productname, producttotalprice, productquantity, orderidtoolbar, Orderstatus;
    EditText postalinformation;
    ImageView productimage;
    ImageButton backtoorder;
    Switch addpostal;
    DatabaseReference orderReference;
    Button updateOrderButton;
    String orderID;


    public EditShippedOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_shipped_order_seller, container, false);
        orderReference = FirebaseDatabase.getInstance().getReference().child("Shipped Orders");
        productname = view.findViewById(R.id.od_shipped_product_name);
        productimage = view.findViewById(R.id.od_shipped_product_image);
        productquantity = view.findViewById(R.id.od_shipped_product_quantity);
        producttotalprice = view.findViewById(R.id.od_shipped_product_price);
        Orderstatus = view.findViewById(R.id.od_shipped_order_status);
        updateOrderButton = view.findViewById(R.id.od_shipped_shipping_update_product_button);
        orderidtoolbar = view.findViewById(R.id.od_shipped_order_id_toolbar);
        backtoorder = view.findViewById(R.id.backtoviewsellerordershippedButton);
        postalinformation = view.findViewById(R.id.od_shipped_order_postal);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            orderID =bundle.getString("orderID");
        }

        getOrderDetails(orderID);

        backtoorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                SellerOrderViewFragment fragorderproductview = new SellerOrderViewFragment();
                ft3.replace(R.id.frame_order_shipping, fragorderproductview);
                ft3.commit();
            }
        });

        Orderstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusdialog();
            }
        });

        updateOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateData();
            }
        });

        return view;
    }

    private void UpdateData() {
        HashMap<String, Object> shippingmap = new HashMap<>();
        shippingmap.put("state",Orderstatus.getText().toString());
        shippingmap.put("trackingno",postalinformation.getText().toString());
        orderReference.child(orderID).updateChildren(shippingmap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(),"Shipping Information Updated", Toast.LENGTH_SHORT).show();
                    if(Orderstatus.getText().toString().equals("Already Shipped"))
                    {
                        orderReference.child(orderID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                FirebaseDatabase.getInstance().getReference().child("Shipped Orders").child(orderID).setValue(snapshot.getValue());
                                orderReference.child(orderID).removeValue();

                                FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                                SellerOrderViewFragment fragorderproductview = new SellerOrderViewFragment();
                                ft3.replace(R.id.frame_order_shipping, fragorderproductview);
                                ft3.commit();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else if(Orderstatus.getText().toString().equals("Cancelled"))
                    {
                        orderReference.child(orderID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                FirebaseDatabase.getInstance().getReference().child("Cancelled Orders").child(orderID).setValue(snapshot.getValue());
                                orderReference.child(orderID).removeValue();

                                FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                                SellerOrderViewFragment fragorderproductview = new SellerOrderViewFragment();
                                ft3.replace(R.id.frame_order_shipping, fragorderproductview);
                                ft3.commit();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });
    }

    private void statusdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Update Shipping information")
                .setItems(OrderStatusConstants.status, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String orderstatus = OrderStatusConstants.status[i];
                        Orderstatus.setText(orderstatus);
                    }
                })
                .show();

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
