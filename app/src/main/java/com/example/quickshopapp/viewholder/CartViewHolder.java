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

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView cartProductName, cartProductPrice;
    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        cartProductName=itemView.findViewById(R.id.cart_product_name);
        cartProductPrice=itemView.findViewById(R.id.cart_product_price);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.OnClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
