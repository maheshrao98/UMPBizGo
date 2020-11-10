package com.example.umpbizgo.Seller.Orders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.umpbizgo.Holder.OrderViewHolder;
import com.example.umpbizgo.Models.Orders;
import com.example.umpbizgo.R;
import com.example.umpbizgo.Seller.Orders.EditUnshippedOrdersFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class SellerOrderViewFragment extends Fragment {
    View view;
    private DatabaseReference orderReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String sellerID;

    public SellerOrderViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_seller_order_view, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        sellerID = firebaseAuth.getCurrentUser().getUid();
        orderReference = FirebaseDatabase.getInstance().getReference().child("Orders");


        recyclerView = view.findViewById(R.id.recycler_seller_order);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        LoadData();
        return view;
    }

    private void LoadData() {
        Query query = orderReference.orderByChild("sellerID").equalTo(sellerID);
        FirebaseRecyclerOptions<Orders>options =
                new FirebaseRecyclerOptions.Builder<Orders>()
                        .setQuery(query, Orders.class)
                        .build();

        FirebaseRecyclerAdapter<Orders, OrderViewHolder> adapter = new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Orders model) {
                holder.sellerorderID.setText(model.getOid());
                holder.sellerorderproductname.setText(model.getProductname());
                holder.sellerproductquantity.setText(model.getQuantity());
                holder.userName.setText(model.getName());
                holder.userShippingAddress.setText("Shipping Address : " + model.getHomeaddress()+ "," + model.getCityaddress());
                holder.userDateTime.setText("Order At : " + model.getDate()+ "" + model.getTime());
                Picasso.get().load(model.getProductImage()).into(holder.sellerorderProductImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        EditUnshippedOrdersFragment fragsellerorderview = new EditUnshippedOrdersFragment();
                        Bundle bundle =new Bundle();
                        bundle.putString("orderID",model.getOid());
                        fragsellerorderview.setArguments(bundle);
                        ft.replace(R.id.frame_seller_order, fragsellerorderview);
                        ft.commit();
                    }
                });
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                OrderViewHolder holder = new OrderViewHolder(view);
                return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}