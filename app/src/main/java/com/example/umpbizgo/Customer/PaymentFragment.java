package com.example.umpbizgo.Customer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umpbizgo.Customer.Order.CustomerOrderViewActivity;
import com.example.umpbizgo.Fragments.LogOutFragment;
import com.example.umpbizgo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
public class PaymentFragment extends Fragment {
    View view;
    ImageView visabutton, mastercardbutton, paypalbutton , expiration;
    String orderID,userID,ProductID, productName, productprice,sellerID, sellerbusinessname,
            quantity, imageUrl, name, homeaddress, cityaddress, phoneenumber, date, time, state;
    String totalAmount = "";
    TextView paymentname, cardnumber, cvv, expirationdate;
    EditText nameinput, cardnumberinput, cvvinput, expirationdateinput;
    Button Done;
    DatabaseReference OrderReference;
    FirebaseAuth mAuth;

    public PaymentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payment, container, false);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        OrderReference = FirebaseDatabase.getInstance().getReference().child("Unpaid Orders");

        paymentname = view.findViewById(R.id.accountcardname);
        nameinput = view.findViewById(R.id.cardaccountnameinput);
        cardnumber = (TextView) view.findViewById(R.id.cardnumber);
        cardnumberinput = view.findViewById(R.id.cardaccountnumberinput);
        cvv = (TextView) view.findViewById(R.id.CVVMSOS);
        cvvinput = view.findViewById(R.id.cvvinput);
        expirationdate = view.findViewById(R.id.expiredate);
        expirationdateinput = view.findViewById(R.id.expiredateinput);
        expiration = view.findViewById(R.id.expirelayout);
        visabutton = view.findViewById(R.id.frame);
        mastercardbutton = view.findViewById(R.id.frame1);
        Done = view.findViewById(R.id.paymentdone);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            orderID = bundle.getString("oid");
            ProductID = bundle.getString("pid");
            productName = bundle.getString("productname");
            productprice = bundle.getString("price");
            quantity = bundle.getString("quantity");
            sellerbusinessname = bundle.getString("sellerbusinessname");
            imageUrl = bundle.getString("productImage");
            sellerID = bundle.getString("sellerID");
            totalAmount = bundle.getString("totalamount");
            name = bundle.getString("name");
            homeaddress = bundle.getString("homeaddress");
            cityaddress = bundle.getString("cityaddress");
            phoneenumber = bundle.getString("phonenumber");
            date = bundle.getString("date");
            time = bundle.getString("time");
            state = bundle.getString("state");
        }

        visabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visabutton.setBackgroundColor(Color.parseColor("#FFC107"));
                mastercardbutton.setBackgroundColor(Color.parseColor("#f1f1f3"));
            }
        });

        mastercardbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mastercardbutton.setBackgroundColor(Color.parseColor("#FFC107"));
                visabutton.setBackgroundColor(Color.parseColor("#f1f1f3"));
            }
        });

        //Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Payment");

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Payment();
            }
        });


        return view;
    }


    private void Payment() {
        if(TextUtils.isEmpty(nameinput.getText().toString()))
        {
            Toast.makeText(getActivity(), "Please fill up required credentials.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cardnumberinput.getText().toString()))
        {
            Toast.makeText(getActivity(), "Please fill up required credentials.", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(cvvinput.getText().toString()))
        {
            Toast.makeText(getActivity(), "Please fill up required credentials.", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(expirationdateinput.getText().toString()))
        {
            Toast.makeText(getActivity(), "Please fill up required credentials.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            CompletePayment();
        }
    }

    private void CompletePayment() {
        DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference().child("Orders");
                OrderReference.child(orderID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderReference.child(orderID).setValue(snapshot.getValue());
                        OrderReference.child(orderID).removeValue();
                        Toast.makeText(getActivity(),"Payment is successfull",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), CustomerOrderViewActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}