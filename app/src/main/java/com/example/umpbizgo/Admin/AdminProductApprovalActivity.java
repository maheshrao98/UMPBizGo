package com.example.umpbizgo.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.umpbizgo.Customer.ProductDetailsFragment;
import com.example.umpbizgo.Holder.AdminProductViewHolder;
import com.example.umpbizgo.Holder.ProductViewHolder;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.Models.Products;
import com.example.umpbizgo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminProductApprovalActivity extends AppCompatActivity {
    private DatabaseReference ProductReference, AuthorizedProductReference, DissapprovedProducts;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_approval);

        // Toolbar //
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.top_app_bar_seller);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.logout:
                        Intent intent = new Intent(AdminProductApprovalActivity.this,
                                MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    case R.id.sellerhome:
                        Intent intent2 = new Intent(AdminProductApprovalActivity.this,
                                AdminHomeActivity.class);
                        startActivity(intent2);
                }

                return false;
            }
        });

        ProductReference = FirebaseDatabase.getInstance().getReference().child("Unauthorized Products");
        AuthorizedProductReference = FirebaseDatabase.getInstance().getReference().child("Authorized Products");
        DissapprovedProducts = FirebaseDatabase.getInstance().getReference().child("Unapproved Products");

        recyclerView = findViewById(R.id.admin_product_recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductReference,Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, AdminProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, AdminProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.textProductName.setText(model.getProductname());
                        holder.textProductPrice.setText("Price = RM" + model.getPrice());
                        holder.textSellerName.setText(model.getsellerbusinessname());
                        Picasso.get().load(model.getImage()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "View",
                                                "Approve",
                                                "Disapprove"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminProductApprovalActivity.this);
                                builder.setTitle("Cart Options");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i==0)
                                        {
                                            FragmentManager fm = getSupportFragmentManager();
                                            final FragmentTransaction ft = fm.beginTransaction();
                                            final ProductDetailsFragment fragbrowseproduct = new ProductDetailsFragment();
                                            Bundle bundle =new Bundle();
                                            bundle.putString("pid",model.getPid());
                                            fragbrowseproduct.setArguments(bundle);
                                            ft.replace(R.id.frameapprovrproduct, fragbrowseproduct);
                                            ft.commit();
                                        }
                                        if (i==1)
                                        {
                                            ProductReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    AuthorizedProductReference.setValue(snapshot.getValue());
                                                    ProductReference.removeValue();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                        if (i==2)
                                        {
                                            ProductReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    DissapprovedProducts.setValue(snapshot.getValue());
                                                    ProductReference.removeValue();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

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
                    public AdminProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_product_item_layout, parent, false);
                        AdminProductViewHolder holder = new AdminProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}