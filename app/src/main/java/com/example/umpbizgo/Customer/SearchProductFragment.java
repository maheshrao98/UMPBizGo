package com.example.umpbizgo.Customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.umpbizgo.Adapters.SearchAdapter;
import com.example.umpbizgo.Holder.ProductViewHolder;
import com.example.umpbizgo.MainActivity;
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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class SearchProductFragment extends Fragment {
    private DatabaseReference reference;
    SearchAdapter searchAdapter;
    EditText searchText;
    private RecyclerView searchList;
    RecyclerView.LayoutManager layoutManager;
    View view;
    ArrayList<Products> arrayList;

    public SearchProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_product, container, false);
        reference = FirebaseDatabase.getInstance().getReference().child("Products");
        searchText=view.findViewById(R.id.search_text);
        searchList = view.findViewById(R.id.search_list);
        searchList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        searchList.setLayoutManager(layoutManager);

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
                if(editable.toString()!=null)
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
        Query query = reference.orderByChild("productname").startAt(product).endAt(product+ "\uf8ff");
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
                                ProductDetailsFragment fragsearchproduct = new ProductDetailsFragment();
                                Bundle bundle =new Bundle();
                                bundle.putString("pid",model.getPid());
                                fragsearchproduct.setArguments(bundle);
                                ft.replace(R.id.frame_search_product, fragsearchproduct);
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
        searchList.setAdapter(adapter);
        adapter.startListening();
    }
}
