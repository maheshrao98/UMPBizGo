package com.example.umpbizgo.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.umpbizgo.Customer.CartFragment;
import com.example.umpbizgo.Customer.CustomerViewOrdersFragment;
import com.example.umpbizgo.CustomerLoginRegister.CustomerLoginActivity;
import com.example.umpbizgo.Holder.CartViewHolder;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.Models.Cart;
import com.example.umpbizgo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ViewOrderProductsFragment extends Fragment {
    private DatabaseReference orderReference;
    private RecyclerView recyclerView;
    private String userID;
    private ImageButton backtoOrderPageButton;
    private FirebaseAuth firebaseAuth;
    RecyclerView.LayoutManager layoutManager;
    private String OrderID = "";
    View view;

    public ViewOrderProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_order_products, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null){
            OrderID = bundle.getString("oid");
        }
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        orderReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(userID).child(OrderID).child("Order Products");

        backtoOrderPageButton = (ImageButton)view.findViewById(R.id.backtoorderButton);
        backtoOrderPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackToOrderPage();
            }
        });

        recyclerView = view.findViewById(R.id.products_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    public void BackToOrderPage() {
        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
        CustomerViewOrdersFragment fragorderproducts = new CustomerViewOrdersFragment();
        ft3.replace(R.id.frame_order_products, fragorderproducts);
        ft3.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Cart>options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(orderReference, Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtProductQuantity.setText("Ouantity =" + model.getQuantity());
                holder.txtProductPrice.setText("Price = RM " + model.getPrice());
                holder.txtProductName.setText(model.getProductname());
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}