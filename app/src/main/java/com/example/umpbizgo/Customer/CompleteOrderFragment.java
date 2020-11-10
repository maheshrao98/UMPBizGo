package com.example.umpbizgo.Customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.umpbizgo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class CompleteOrderFragment extends Fragment {
    private EditText nameEditText, phoneEditText, homeEditText, cityEditText;
    private Button confirmButton;
    private DatabaseReference UserReference, OrderReference;
    private ImageButton backtoOrders;
    private String userID,ProductID, productName, productprice,sellerID, sellerbusinessname, quantity, imageUrl;;
    private String totalAmount = "";
    View view;

    public CompleteOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_compete_order, container, false);

        UserReference = FirebaseDatabase.getInstance().getReference().child("Users");
        OrderReference = FirebaseDatabase.getInstance().getReference().child("Orders");

        Bundle bundle = this.getArguments();
        if(bundle!= null){
            ProductID = bundle.getString("pid");
            productName = bundle.getString("productname");
            productprice = bundle.getString("price");
            quantity = bundle.getString("quantity");
            sellerbusinessname = bundle.getString("sellerbusinessname");
            imageUrl = bundle.getString("image");
            sellerID = bundle.getString("sellerID");
            totalAmount = bundle.getString("totalprice");
        }

        backtoOrders = (ImageButton)view.findViewById(R.id.BacktoOrdersButton);
        confirmButton = (Button) view.findViewById(R.id.confirm_final_order_button);
        nameEditText = (EditText) view.findViewById(R.id.shipment_name);
        phoneEditText = (EditText) view.findViewById(R.id.shipment_phone_number);
        homeEditText = (EditText) view.findViewById(R.id.shipment_address);
        cityEditText = (EditText) view.findViewById(R.id.shipment_city_address);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
            }
        });

        backtoOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackToOrderPage();
            }
        });

        UserReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            userID = snapshot.child("uid").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        return view;
    }

    private void BackToOrderPage() {
        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
        CustomerViewOrdersFragment fragordercomplete = new CustomerViewOrdersFragment();
        ft3.replace(R.id.frame_complete_order, fragordercomplete);
        ft3.commit();
    }

    private void Check() {
        if(TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(getActivity(), "Please fill up your name.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(getActivity(), "Please fill up your phone number.", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(homeEditText.getText().toString()))
        {
            Toast.makeText(getActivity(), "Please fill up your home address.", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(cityEditText.getText().toString()))
        {
            Toast.makeText(getActivity(), "Please fill up your city address.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ConfirmOrder();
        }
    }

    private void ConfirmOrder() {
        final String saveCurrentDate,saveCurrenttime, orderKey;
        Calendar callfordate = Calendar.getInstance();
        SimpleDateFormat currentdateformat = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentdateformat.format(callfordate.getTime());

        SimpleDateFormat currenttimeformat = new SimpleDateFormat("HH:mm:ss a");
        saveCurrenttime = currenttimeformat.format(callfordate.getTime());

        DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference().child("Orders");

        orderKey = orderReference.push().getKey();

        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("oid",orderKey);
        orderMap.put("totalAmount", totalAmount);
        orderMap.put("name", nameEditText.getText().toString());
        orderMap.put("uid",userID);
        orderMap.put("phonenumber", phoneEditText.getText().toString());
        orderMap.put("homeaddress", homeEditText.getText().toString());
        orderMap.put("cityaddress", cityEditText.getText().toString());
        orderMap.put("pid",ProductID);
        orderMap.put("productname", productName);
        orderMap.put("price",productprice);
        orderMap.put("quantity",quantity);
        orderMap.put("sellerID",sellerID);
        orderMap.put("sellerbusinessname", sellerbusinessname);
        orderMap.put("productImage",imageUrl);


        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrenttime);
        orderMap.put("state","Not Shipped");



        orderReference.child(orderKey).updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                                    Toast.makeText(getActivity(),"Your final order has been placed",Toast.LENGTH_SHORT).show();

                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    BrowseProductFragment fragcompleteorder = new BrowseProductFragment();
                                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    ft.replace(R.id.frame_complete_order, fragcompleteorder);
                                    ft.commit();
                                }
                }
        });
    }
}
