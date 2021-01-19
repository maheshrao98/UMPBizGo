package com.example.umpbizgo.Holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.umpbizgo.Interface.ItemClickListener;
import com.example.umpbizgo.R;

public class ProductRatingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ItemClickListener listener;
    public TextView username, rating;


    public ProductRatingViewHolder(@NonNull View itemView) {
        super(itemView);
        username = (TextView) itemView.findViewById(R.id.rating_user_name);
        rating = (TextView) itemView.findViewById(R.id.rating);
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);
    }
}
