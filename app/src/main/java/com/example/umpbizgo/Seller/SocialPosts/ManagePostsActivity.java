package com.example.umpbizgo.Seller.SocialPosts;

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

import com.example.umpbizgo.Admin.AdminProductApprovalActivity;
import com.example.umpbizgo.Admin.AdminProductUnauthorizeActivity;
import com.example.umpbizgo.Fragments.LogOutFragment;
import com.example.umpbizgo.Holder.PostViewHolder;
import com.example.umpbizgo.Holder.ProductViewHolder;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.Models.Posts;
import com.example.umpbizgo.Models.Products;
import com.example.umpbizgo.R;
import com.example.umpbizgo.Seller.Products.AddProductActivity;
import com.example.umpbizgo.Seller.Products.EditAuthorizedProductFragment;
import com.example.umpbizgo.Seller.SellerHomeActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

public class ManagePostsActivity extends AppCompatActivity {
    private DatabaseReference PostsReference, SellerReference;
    private StorageReference PostImageStorageReference;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_posts);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.top_app_bar_seller);
        toolbar.setTitle("Manage Post");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sellerhome:
                        Intent intent = new Intent(ManagePostsActivity.this, SellerHomeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.logout:
                        Intent intent2 = new Intent(ManagePostsActivity.this, MainActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });

        PostImageStorageReference = FirebaseStorage.getInstance().getReference().child("Post Images");
        PostsReference = FirebaseDatabase.getInstance().getReference().child("Authorized Posts");
        SellerReference = FirebaseDatabase.getInstance().getReference().child("Sellers");

        recyclerView = findViewById(R.id.recycler_seller_posts);
        layoutManager = new LinearLayoutManager(ManagePostsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        LoadData();
    }

    private void LoadData() {
        Query query = PostsReference.orderByChild("sellerid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        FirebaseRecyclerOptions<Posts> options =
                new FirebaseRecyclerOptions.Builder<Posts>()
                        .setQuery(query,Posts.class)
                        .build();

        FirebaseRecyclerAdapter<Posts, PostViewHolder> adapter =
                new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull final Posts model) {
                        holder.title.setText(model.getTitle());
                        holder.desc.setText(model.getDescription());
                        holder.name.setText(model.getDate());
                        Picasso.get().load(model.getImage()).into(holder.image);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ManagePostsActivity.this, EditPostActivity.class);
                                intent.putExtra("pid", model.getPoid());
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_layout, parent, false);
                        PostViewHolder holder = new PostViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}