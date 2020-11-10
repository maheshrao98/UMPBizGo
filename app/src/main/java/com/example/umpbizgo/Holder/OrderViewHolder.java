package com.example.umpbizgo.Holder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.umpbizgo.Interface.ItemClickListener;
import com.example.umpbizgo.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public ItemClickListener listener;
    public TextView orderstatus, sellerproductquantity, orderID, sellerorderID, userTotalPrice, userShippingAddress, userDateTime, sellername, productName, productquantity, sellerorderproductname, userName;
    public ImageView orderproductImage, sellerorderProductImage;


    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        orderstatus = itemView.findViewById(R.id.order_status);
        orderID = itemView.findViewById(R.id.order_id);
        sellerorderID = itemView.findViewById(R.id.seller_order_id);
        userName = itemView.findViewById(R.id.seller_customer_name);
        userTotalPrice = itemView.findViewById(R.id.order_product_price);
        userDateTime = itemView.findViewById(R.id.seller_order_datetime);
        userShippingAddress = itemView.findViewById(R.id.seller_customer_address);
        sellername = itemView.findViewById(R.id.order_seller_business);
        orderproductImage = (ImageView)itemView.findViewById(R.id.order_product_image);
        productName = itemView.findViewById(R.id.order_product_name);
        productquantity = itemView.findViewById(R.id.order_product_quantity);
        sellerorderProductImage = itemView.findViewById(R.id.seller_order_product_image);
        sellerorderproductname =itemView.findViewById(R.id.seller_order_product_name);
        sellerproductquantity = itemView.findViewById(R.id.seller_order_product_quantity);
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
