package com.example.umpbizgo.Customer.Order;

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
import android.widget.EditText;
import android.widget.Toast;

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
public class RequestRefundOrderFragment extends Fragment {
    View view;
    Button requestRefundButton;
    ImageButton backButton;
    EditText requestRefund;
    DatabaseReference orderReference;
    String orderID;

    public RequestRefundOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_request_refund_order, container, false);
            requestRefundButton = (Button) view.findViewById(R.id.od_request_refund_order_button);
            requestRefund = view.findViewById(R.id.requestrefundreason);
            backButton =  view.findViewById(R.id.backfromrequestrefundorder);

            orderReference = FirebaseDatabase.getInstance().getReference().child("Shipped Orders");

            Bundle bundle = this.getArguments();
            if (bundle != null) {
                orderID =bundle.getString("orderID");
            }

            requestRefundButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestOrder();
                }
            });

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoBack();
                }
            });

        return view;
    }

    private void GoBack() {
        Intent intent = new Intent(getActivity(), CustomerOrderViewActivity.class);
        startActivity(intent);
    }

    private void requestOrder() {
        HashMap<String, Object> reasonmaps = new HashMap<>();
        reasonmaps.put("refundreason", requestRefund.getText().toString());
        reasonmaps.put("status", "Request Refund");
        orderReference.child(orderID).updateChildren(reasonmaps).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    orderReference.child(orderID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            orderReference.child(orderID).setValue(snapshot.getValue());
                            Toast.makeText(getActivity(), "Request Refund is received and will be processed.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), CustomerOrderViewActivity.class);
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