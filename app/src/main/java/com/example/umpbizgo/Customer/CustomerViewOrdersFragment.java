package com.example.umpbizgo.Customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.umpbizgo.Fragments.ViewOrderProductsFragment;
import com.example.umpbizgo.Holder.OrderViewHolder;
import com.example.umpbizgo.Models.Orders;
import com.example.umpbizgo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class CustomerViewOrdersFragment extends Fragment {
    private DatabaseReference orderReference;
    private String userID;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    View view;


    public CustomerViewOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_customer_view_orders, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        orderReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(userID);

        recyclerView = view.findViewById(R.id.orders_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Orders> options =
                new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(orderReference, Orders.class)
                .build();

        FirebaseRecyclerAdapter<Orders, OrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Orders model) {
                        holder.username.setText("Name:" + model.getName());
                        holder.orderID.setText( "#" + model.getOid());
                        holder.userTotalPrice.setText("Total Price: RM" + model.getTotalAmount());
                        holder.userDateTime.setText("Order at :" + model.getDate()+" "+ model.getTime() );
                        holder.userShippingAddress.setText("Shipping Address:" + model.getHomeaddress()+ ", " +model.getCityaddress());

                        holder.showorderproductbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ViewOrderProductsFragment fragvieworders = new ViewOrderProductsFragment();
                                Bundle bundle =new Bundle();
                                bundle.putString("oid",model.getOid());
                                fragvieworders.setArguments(bundle);
                                ft.replace(R.id.frame_view_customer_order, fragvieworders);
                                ft.commit();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        OrderViewHolder holder = new OrderViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}