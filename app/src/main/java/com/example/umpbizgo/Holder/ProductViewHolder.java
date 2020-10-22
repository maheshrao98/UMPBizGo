package com.example.umpbizgo.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.umpbizgo.Interface.ItemClickListener;
import com.example.umpbizgo.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textProductName, textProductDescription, textProductPrice, textSellerName, productnamedisplay;
    public ImageView imageView;
    public ItemClickListener listener;

    public ProductViewHolder(View itemView)
    {
        super(itemView);

        imageView =  (ImageView) itemView.findViewById(R.id.product_image);
        textProductName =  (TextView) itemView.findViewById(R.id.product_name);
        textProductDescription = itemView.findViewById(R.id.product_description);
        productnamedisplay = itemView.findViewById(R.id.productnamedisplay);
        textProductPrice =  (TextView) itemView.findViewById(R.id.product_price);
        textSellerName = (TextView) itemView.findViewById(R.id.seller_business_name);
    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view)
    {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
