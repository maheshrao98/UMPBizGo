package com.example.umpbizgo.Customer.AdPosts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.umpbizgo.Customer.CartFragment;
import com.example.umpbizgo.Fragments.LogOutFragment;
import com.example.umpbizgo.Holder.PostViewHolder;
import com.example.umpbizgo.Models.Posts;
import com.example.umpbizgo.R;
import com.example.umpbizgo.Seller.SellerHomeActivity;
import com.example.umpbizgo.Seller.SocialPosts.ManagePostsActivity;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ViewAdPostsFragment extends Fragment {
    View view;
    private DatabaseReference PostsReference, SellerReference;
    private StorageReference PostImageStorageReference;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public ViewAdPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_ad_posts, container, false);
        //Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.top_app_bar);
        toolbar.setTitle("Ads");
        setHasOptionsMenu(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cart:
                        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                        CartFragment fragbrowseproduct = new CartFragment();
                        ft3.replace(R.id.frame_customer_post, fragbrowseproduct);
                        ft3.commit();
                        break;
                    case R.id.logout:
                        FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                        LogOutFragment fragbrowseproduct2 = new LogOutFragment();
                        ft2.replace(R.id.frame_customer_post, fragbrowseproduct2);
                        ft2.commit();
                        break;
                }
                return false;
            }
        });

        PostImageStorageReference = FirebaseStorage.getInstance().getReference().child("Post Images");
        PostsReference = FirebaseDatabase.getInstance().getReference().child("Authorized Posts");
        SellerReference = FirebaseDatabase.getInstance().getReference().child("Sellers");

        recyclerView = view.findViewById(R.id.recycler_user_posts);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        LoadData();
        return  view;
    }

    private void LoadData() {
            FirebaseRecyclerOptions<Posts> options =
                    new FirebaseRecyclerOptions.Builder<Posts>()
                            .setQuery(PostsReference, Posts.class)
                            .build();

            FirebaseRecyclerAdapter<Posts, PostViewHolder> adapter =
                    new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull final Posts model) {
                            holder.title.setText(model.getTitle());
                            holder.desc.setText(model.getDescription());
                            holder.name.setText(model.getDate());
                            Picasso.get().load(model.getImage()).into(holder.image);
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
