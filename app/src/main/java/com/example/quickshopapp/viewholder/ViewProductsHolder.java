package com.example.quickshopapp.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickshopapp.Interfaces.ItemClickListener;
import com.example.quickshopapp.R;
import com.example.quickshopapp.model.AddProdModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ViewProductsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView addProductName, addProductPrice;
    private ItemClickListener itemClickListener;
    public ImageView addProductImg;

    public ViewProductsHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        addProductName = itemView.findViewById(R.id.prod_name);
        addProductPrice = itemView.findViewById(R.id.prod_price);
        addProductImg = itemView.findViewById(R.id.prod_img);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        if (itemClickListener != null) {
            itemClickListener.OnClick(view, getAdapterPosition(), false);
        }
    }

    public void bindData(AddProdModel model) {
        String name = model.getName().replaceAll("\n", " ");
        String price = model.getPrice();
        String imgUri = model.getImg();

        addProductName.setText(name);
        addProductPrice.setText(price);

        Picasso.get()
                .load(imgUri)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(addProductImg, new Callback() {
                    @Override
                    public void onSuccess() {
                        // Image loaded successfully
                    }

                    @Override
                    public void onError(Exception e) {
                        // Image loading failed, log the error
                        e.printStackTrace();
                    }
                });
    }
}
