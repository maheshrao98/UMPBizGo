package com.example.umpbizgo.Customer.Order;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.umpbizgo.Holder.OrderViewHolder;
import com.example.umpbizgo.Holder.ProductRatingViewHolder;
import com.example.umpbizgo.Models.Orders;
import com.example.umpbizgo.Models.ProductRatings;
import com.example.umpbizgo.Models.Products;
import com.example.umpbizgo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ViewReviewFragment extends Fragment {
    View view;
    private DatabaseReference orderReference, productReference ;
    private String userID;
    private FirebaseAuth firebaseAuth;
    DatabaseReference productRatingReference;
    String ratingid, productID;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public ViewReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_ratings_test, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            productID = bundle.getString("pid");
        }

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        orderReference = FirebaseDatabase.getInstance().getReference().child("Shipped Orders");
        productRatingReference = FirebaseDatabase.getInstance().getReference().child("Ratings");
        productReference = FirebaseDatabase.getInstance().getReference().child("Authorized Products");

        recyclerView = view.findViewById(R.id.ratings_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = productReference.orderByChild("pid").equalTo(productID);
        FirebaseRecyclerOptions<ProductRatings> options =
                new FirebaseRecyclerOptions.Builder<ProductRatings>()
                        .setQuery(query, ProductRatings.class)
                        .build();

        FirebaseRecyclerAdapter<ProductRatings, ProductRatingViewHolder> adapter =
                new FirebaseRecyclerAdapter<ProductRatings,ProductRatingViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductRatingViewHolder holder, int position, @NonNull ProductRatings model) {
                        holder.username.setText(model.getUsername());
                        holder.rating.setText(model.getRate());

                    }

                    @NonNull
                    @Override
                    public ProductRatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ratings_layout, parent, false);
                        ProductRatingViewHolder holder = new ProductRatingViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}