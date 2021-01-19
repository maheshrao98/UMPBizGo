package com.example.umpbizgo.Customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.umpbizgo.Customer.Order.CustomerOrderViewActivity;
import com.example.umpbizgo.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class CustomerUserPageFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private String userID;
    private CardView ManageOrders, ManageUsers, Sellerregister;
    TextView shopnameview;
    FrameLayout frameLayout;
    ImageButton backtomainpage;
    View view;

    public CustomerUserPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_customer_user_page, container, false);

        backtomainpage = view.findViewById(R.id.backtoviewcustmainpageButton);
        ManageOrders = view.findViewById(R.id.cumanageorders);
        ManageUsers = view.findViewById(R.id.cuuserinformation);

       backtomainpage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getActivity(), HomeActivity.class);
               startActivity(intent);
           }
       });

       ManageOrders.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getActivity(), CustomerOrderViewActivity.class);
               startActivity(intent);
           }
       });

       ManageUsers.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               FragmentTransaction ft3 = getFragmentManager().beginTransaction();
               MyAccountCustomerFragment fragorderproductview = new MyAccountCustomerFragment();
               ft3.replace(R.id.frame_customer_home, fragorderproductview);
               ft3.commit();
           }
       });

        return view;
    }

}