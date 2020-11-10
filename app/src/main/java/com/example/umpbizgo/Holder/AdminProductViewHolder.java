package com.example.umpbizgo.Holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.umpbizgo.Interface.ItemClickListener;
import com.example.umpbizgo.R;

public class AdminProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textProductName, textProductDescription, textProductPrice, textSellerName, productnamedisplay;
    public ImageView imageView;
    public ImageButton approveButton, disapprovebutton;
    public ItemClickListener listener;
    public AdminProductViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView =  (ImageView) itemView.findViewById(R.id.aproduct_image);
        textProductName =  (TextView) itemView.findViewById(R.id.aproduct_name);
        textProductPrice =  (TextView) itemView.findViewById(R.id.aproduct_price);
        textSellerName = (TextView) itemView.findViewById(R.id.aseller_business_name);
        approveButton = itemView.findViewById(R.id.imageButtonapprove);
        disapprovebutton = itemView.findViewById(R.id.imageButtondisapprove);

    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
