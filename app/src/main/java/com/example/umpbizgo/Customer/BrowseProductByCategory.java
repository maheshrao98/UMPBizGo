package com.example.umpbizgo.Customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.umpbizgo.Fragments.LogOutFragment;
import com.example.umpbizgo.Holder.ProductViewHolder;
import com.example.umpbizgo.Models.Products;
import com.example.umpbizgo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class BrowseProductByCategory extends Fragment {
    private DatabaseReference ProductReference;
    private RecyclerView recyclerView;
    private String category = "";
    RecyclerView.LayoutManager layoutManager;
    View view;

    public BrowseProductByCategory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_browse_product_by_category, container, false);

        Bundle bundle = getArguments();
        if(bundle!=null) {
            category = bundle.getString("category");
        }
        //Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.top_app_bar);
        toolbar.setTitle(category);
        setHasOptionsMenu(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cart:
                        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                        CartFragment fragbrowseproduct = new CartFragment();
                        ft3.replace(R.id.frame_browse_product_category, fragbrowseproduct);
                        ft3.commit();
                        break;
                    case R.id.logout:
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        LogOutFragment fragbrowseproduct2 = new LogOutFragment();
                        ft2.replace(R.id.frame_browse_product_category, fragbrowseproduct2);
                        ft2.commit();
                        break;
                }
                return false;
            }
        });

        ProductReference = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = view.findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        LoadData();

        return view;
    }


    private void LoadData() {
        Query query = ProductReference.orderByChild("category").equalTo(category);
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
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ProductDetailsFragment fragbrowseproduct = new ProductDetailsFragment();
                                Bundle bundle =new Bundle();
                                bundle.putString("pid",model.getPid());
                                fragbrowseproduct.setArguments(bundle);
                                ft.replace(R.id.frame_browse_product_category, fragbrowseproduct);
                                ft.commit();
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