package com.example.umpbizgo.Holder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.umpbizgo.Interface.ItemClickListener;
import com.example.umpbizgo.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public ItemClickListener listener;
    public TextView username, orderID, userTotalPrice, userShippingAddress, userDateTime;
    public Button showorderproductbtn;


    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.order_user_name);
        orderID = itemView.findViewById(R.id.order_id);
        userTotalPrice = itemView.findViewById(R.id.order_total_price);
        userDateTime = itemView.findViewById(R.id.order_date_time);
        userShippingAddress = itemView.findViewById(R.id.order_address_city);
        showorderproductbtn = itemView.findViewById(R.id.show_all_order_products_btn);
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
