package com.example.umpbizgo.Customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.umpbizgo.Fragments.LogOutFragment;
import com.example.umpbizgo.Holder.ProductViewHolder;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.Models.Products;
import com.example.umpbizgo.R;
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

        //Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.top_app_bar);
        toolbar.setTitle("Products");
        setHasOptionsMenu(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cart:
                        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                        CartFragment fragbrowseproduct = new CartFragment();
                        ft3.replace(R.id.frame_browse_product, fragbrowseproduct);
                        ft3.commit();
                        break;
                    case R.id.logout:
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        LogOutFragment fragbrowseproduct2 = new LogOutFragment();
                        ft2.replace(R.id.frame_browse_product, fragbrowseproduct2);
                        ft2.commit();
                        break;
                }
                return false;
            }
        });

        ProductReference = FirebaseDatabase.getInstance().getReference().child("Authorized Products");

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
                                bundle.putString("sellerID",model.getSellerid());
                                bundle.putString("image",model.getImage());
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