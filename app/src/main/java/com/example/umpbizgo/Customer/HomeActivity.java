package com.example.umpbizgo.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.umpbizgo.Fragments.LogOutFragment;
import com.example.umpbizgo.Fragments.MyAccountBeforeLogin;
import com.example.umpbizgo.Holder.ProductViewHolder;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.Models.Products;
import com.example.umpbizgo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    private DatabaseReference ProductReference;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    private String userID;
    TextView usernameview;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ///// Maintain the screen orientation portrait ///////
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //////////Change the color of status text/////////
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottonnavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);

        ProductReference = FirebaseDatabase.getInstance().getReference().child("Products");
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        usernameview = findViewById(R.id.usernamedisplay);
        usernamedisplay(usernameview);

        recyclerView = findViewById(R.id.products_list_2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    private void usernamedisplay(TextView usernameview) {
        DatabaseReference UserReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        UserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue().toString();
                usernameview.setText(username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductReference,Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.textProductName.setText(model.getProductname());
                        holder.textProductPrice.setText("RM" + model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.imageView);
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout_2, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigation =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {

                        case R.id.products:
                            selectedFragment = new BrowseProductFragment();
                            break;

                        case R.id.orders:
                            selectedFragment = new CustomerViewOrdersFragment();
                            break;

                        case R.id.home:
                            selectedFragment = new HomeFragment();
                            break;

                        case R.id.feed:
                            selectedFragment = new LogOutFragment();
                            break;
                        case R.id.myaccount:
                            selectedFragment = new MyAccountBeforeLogin();
                            break;
                    }
                    ///////////////Replacing by default fragment on home activity/////////////////
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                            selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.cart:
                selectedFragment = new CartFragment();
                break;
            case R.id.logout:
                selectedFragment = new LogOutFragment();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        ///////////////Replacing by default fragment on home activity/////////////////
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,
                selectedFragment).commit();
        return true;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, MainActivity.class));
    }
}