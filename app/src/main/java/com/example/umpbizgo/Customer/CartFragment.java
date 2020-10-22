package com.example.umpbizgo.Customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.umpbizgo.Holder.CartViewHolder;
import com.example.umpbizgo.Models.Cart;
import com.example.umpbizgo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton backtohomebutton;
    private Button NextprocessButton;
    private TextView txtTotalamount;
    private FirebaseAuth firebaseAuth;

    private int overallTotalPrice=0;
    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        firebaseAuth = FirebaseAuth.getInstance();
        backtohomebutton = view.findViewById(R.id.backtohomeButton);
        NextprocessButton = (Button)view.findViewById(R.id.next_process_btn);
        txtTotalamount = (TextView)view.findViewById(R.id.total_price);

        NextprocessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                CompleteOrderFragment fragcart = new CompleteOrderFragment();
                Bundle bundle =new Bundle();
                bundle.putString("Total Price",String.valueOf(overallTotalPrice));
                fragcart.setArguments(bundle);
                ft.replace(R.id.frame_cart, fragcart);
                ft.commit();
            }
        });

        backtohomebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackToHomePage();
            }
        });
        return view;
    }

    private void BackToHomePage() {
        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
        BrowseProductFragment fragcart = new BrowseProductFragment();
        ft3.replace(R.id.frame_cart, fragcart);
        ft3.commit();
    }


    @Override
    public void onStart() {
        super.onStart();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User Cart View")
                .child(firebaseAuth.getCurrentUser().getUid())
                .child("Products"), Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.txtProductQuantity.setText("Ouantity =" + model.getQuantity());
                holder.txtProductPrice.setText("Price = RM " + model.getPrice());
                holder.txtProductName.setText(model.getProductname());

                int oneTypeProductPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                overallTotalPrice = overallTotalPrice + oneTypeProductPrice;

                txtTotalamount.setText("Total Price = RM" + String.valueOf(overallTotalPrice));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i==0)
                                {
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ProductDetailsFragment fragcart2 = new ProductDetailsFragment();
                                    Bundle bundle =new Bundle();
                                    bundle.putString("pid",model.getPid());
                                    fragcart2.setArguments(bundle);
                                    ft.replace(R.id.frame_cart, fragcart2);
                                    ft.commit();
                                }
                                if(i==1)
                                {
                                    cartListRef.child("User Cart View")
                                            .child(firebaseAuth.getCurrentUser().getUid())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                        CartFragment fragcart = new CartFragment();
                                                        ft.replace(R.id.frame_cart, fragcart);
                                                        ft.commit();
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });


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