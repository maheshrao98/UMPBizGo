package com.example.umpbizgo.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.umpbizgo.Models.Products;
import com.example.umpbizgo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder>
{

    public ArrayList<Products> arrayList;

    public  SearchAdapter(ArrayList<Products> arrayList)
    {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public SearchAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
        return new SearchAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapterViewHolder holder, int position) {
        holder.textProductName.setText(arrayList.get(position).getProductname());
        holder.textProductPrice.setText(arrayList.get(position).getPrice());
        holder.textSellerName.setText(arrayList.get(position).getsellerbusinessname());
        Picasso.get().load(arrayList.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


        public class SearchAdapterViewHolder extends  RecyclerView.ViewHolder
        {
            public TextView textProductName, textProductPrice, textSellerName;
            public ImageView imageView;
            public SearchAdapterViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView =  (ImageView) itemView.findViewById(R.id.product_image);
                textProductName =  (TextView) itemView.findViewById(R.id.product_name);
                textProductPrice =  (TextView) itemView.findViewById(R.id.product_price);
                textSellerName = (TextView) itemView.findViewById(R.id.seller_business_name);
            }
        }
}
