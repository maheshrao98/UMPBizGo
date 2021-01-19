package com.example.umpbizgo.Holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.umpbizgo.Interface.ItemClickListener;
import com.example.umpbizgo.R;

public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ItemClickListener listener;
    public TextView name, title, desc;
    public ImageView image;
    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.post_name);
        desc = itemView.findViewById(R.id.post_desc);
        name = itemView.findViewById(R.id.user_name);
        image =  (ImageView) itemView.findViewById(R.id.post_image);
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view,getAdapterPosition(),false);
    }
}
