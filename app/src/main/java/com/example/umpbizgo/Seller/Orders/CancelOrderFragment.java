package com.example.umpbizgo.Seller.Orders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umpbizgo.Customer.Order.CustomerOrderViewActivity;
import com.example.umpbizgo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class CancelOrderFragment extends Fragment {
    View view;
    Button cancelButton;
    ImageButton backButton;
    EditText cancelReason;
    DatabaseReference orderReference,cancelorderReference;
    String orderID;

    public CancelOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cancel_order, container, false);
        cancelButton = view.findViewById(R.id.od_cancel_cancel_order_button);
        cancelReason = view.findViewById(R.id.cancelordereason);
        backButton = view.findViewById(R.id.od_cancel_order_toolbar);

        orderReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        cancelorderReference = FirebaseDatabase.getInstance().getReference().child("Cancelled Orders");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            orderID =bundle.getString("orderID");
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackButton();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitReason();
            }
        });

        return view;
    }

    private void BackButton() {
        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
        SellerOrderViewFragment fragorderproductview = new SellerOrderViewFragment();
        ft3.replace(R.id.frame_cancel_order, fragorderproductview);
        ft3.commit();
    }

    private void SubmitReason() {
        HashMap<String, Object> reasonmaps = new HashMap<>();
        reasonmaps.put("reason", cancelReason.getText().toString());
        orderReference.child(orderID).updateChildren(reasonmaps).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    orderReference.child(orderID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            cancelorderReference.child(orderID).setValue(snapshot.getValue());
                            orderReference.child(orderID).removeValue();

                            Toast.makeText(getActivity(), "Order is successfully cancelled.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), SellerOrderViewActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}