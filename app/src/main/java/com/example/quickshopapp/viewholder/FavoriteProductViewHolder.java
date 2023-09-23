package com.example.quickshopapp.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickshopapp.Interfaces.ItemClickListener;
import com.example.quickshopapp.R;
import com.example.quickshopapp.Interfaces.ItemClickListener;
import com.example.quickshopapp.R;

import org.jetbrains.annotations.NotNull;

public class FavoriteProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView favProductName, favProductPrice;
    private ItemClickListener itemClickListener;

    public FavoriteProductViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        favProductName=itemView.findViewById(R.id.fav_product_name);
        favProductPrice=itemView.findViewById(R.id.fav_product_price);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.OnClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
