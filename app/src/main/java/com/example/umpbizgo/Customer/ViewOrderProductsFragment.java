package com.example.umpbizgo.Customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.umpbizgo.Customer.CartFragment;
import com.example.umpbizgo.Customer.CustomerViewOrdersFragment;
import com.example.umpbizgo.CustomerLoginRegister.CustomerLoginActivity;
import com.example.umpbizgo.Holder.CartViewHolder;
import com.example.umpbizgo.MainActivity;
import com.example.umpbizgo.Models.Cart;
import com.example.umpbizgo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ViewOrderProductsFragment extends Fragment {
    private DatabaseReference orderReference;
    private String userID;
    private ImageButton backtoOrderPageButton;
    private FirebaseAuth firebaseAuth;
    private Button confirmButton;
    private ImageView odproductimage;
    private TextView odproductname, odproductprice, odproductquantity, odtotalprice;
    private String ProductID, productName, productprice, totalPrice, sellerID, sellerbusinessname, quantity, imageUrl;
    private Integer ProductTotalPrice;

    View view;

    public ViewOrderProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_order_products, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null){
            ProductID = bundle.getString("pid");
            productName = bundle.getString("productname");
            productprice = bundle.getString("price");
            quantity = bundle.getString("quantity");
            sellerbusinessname = bundle.getString("sellerbusinessname");
            imageUrl = bundle.getString("image");
            sellerID = bundle.getString("sellerID");
        }
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        odproductimage = (ImageView) view.findViewById(R.id.od_product_image);
        odproductname = (TextView) view.findViewById(R.id.od_product_name);
        odproductprice = (TextView) view.findViewById(R.id.od_product_price);
        odproductquantity = (TextView) view.findViewById(R.id.od_product_quantity);
        odtotalprice = (TextView) view.findViewById(R.id.od_total_price);
        confirmButton = (Button) view.findViewById(R.id.od_confirm_product_button);

        backtoOrderPageButton = (ImageButton)view.findViewById(R.id.backtoorderButton);
        backtoOrderPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackToOrderPage();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                CompleteOrderFragment fragorderproductview = new CompleteOrderFragment();
                Bundle bundle =new Bundle();
                bundle.putString("pid",ProductID);
                bundle.putString("sellerID",sellerID);
                bundle.putString("image",imageUrl);
                bundle.putString("productname", productName);
                bundle.putString("sellerbusinessname",sellerbusinessname);
                bundle.putString("price",productprice);
                bundle.putString("quantity",quantity);
                bundle.putString("totalprice", odtotalprice.getText().toString());
                fragorderproductview.setArguments(bundle);
                ft3.replace(R.id.frame_prod_detail, fragorderproductview);
                ft3.commit();
            }
        });

        return view;
    }

    public void BackToOrderPage() {
        FragmentTransaction ft3 = getFragmentManager().beginTransaction();
        BrowseProductFragment fragorderproducts = new BrowseProductFragment();
        ft3.replace(R.id.frame_order_products, fragorderproducts);
        ft3.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        Picasso.get().load(imageUrl).into(odproductimage);
        odproductname.setText(productName);
        odproductprice.setText(productprice);
        odproductquantity.setText(quantity);

        ProductTotalPrice = ((Integer.valueOf(productprice))) * Integer.valueOf(quantity);

        odtotalprice.setText("Total Price = RM" + String.valueOf(ProductTotalPrice));


    }
}