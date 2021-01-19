package com.example.umpbizgo.Seller.Products;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.umpbizgo.Customer.CartFragment;
import com.example.umpbizgo.Holder.ProductViewHolder;
import com.example.umpbizgo.Models.Products;
import com.example.umpbizgo.R;
import com.example.umpbizgo.Seller.Products.EditAuthorizedProductFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class SellerMyProductsFragment extends Fragment {
    View view;
    private DatabaseReference ProductReference;
    private StorageReference ProductImageStorageReference;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FragmentTransaction fm;

    public SellerMyProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_seller_my_products, container, false);
        ProductImageStorageReference = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductReference = FirebaseDatabase.getInstance().getReference().child("Authorized Products");


        recyclerView = view.findViewById(R.id.recycler_seller_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        LoadData();
        return view;
    }

    private void LoadData() {
        Query query = ProductReference.orderByChild("sellerid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(query,Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.textProductName.setText(model.getProductname());
                        holder.textProductPrice.setText("Price = RM" + model.getPrice());
                        holder.textSellerName.setText(model.getsellerbusinessname());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                androidx.fragment.app.FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                                EditAuthorizedProductFragment fragunauthproducts = new EditAuthorizedProductFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("pid", model.getPid());
                                fragunauthproducts.setArguments(bundle);
                                ft3.replace(R.id.frame_seller_product, fragunauthproducts);
                                ft3.commit();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}