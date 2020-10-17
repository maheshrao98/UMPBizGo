package com.example.umpbizgo.Customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.umpbizgo.Holder.ProductViewHolder;
import com.example.umpbizgo.Models.Products;
import com.example.umpbizgo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.yalantis.jellytoolbar.widget.JellyToolbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class BrowseProductFragment extends Fragment {
    private DatabaseReference ProductReference;
    private EditText searchText;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    View view;

    public BrowseProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_browse_product, container, false);

        ProductReference = FirebaseDatabase.getInstance().getReference().child("Products");
        searchText=view.findViewById(R.id.search_text);
        recyclerView = view.findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        LoadData("");
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString() != null)
                {
                    LoadData(editable.toString());
                }
                else
                {
                    LoadData("");
                }
            }
        });

        return view;
    }

    private void LoadData(String product) {
        Query query = ProductReference.orderByChild("productname").startAt(product).endAt(product+ "\uf8ff");
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
                                ft.replace(R.id.frame_browse_product, fragbrowseproduct);
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