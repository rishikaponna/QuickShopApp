package com.example.quickshopapp.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quickshopapp.R;
import com.example.quickshopapp.constant.Constant;
import com.example.quickshopapp.model.Product;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private void fetchProductsFromFirebase() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");
        productsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Product product = dataSnapshot.getValue(Product.class);
                products.add(product);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                // Handle changes to individual products if needed
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Handle removal of products if needed
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                // Handle changes in order if needed
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors if needed
            }
        });
    }

    private List<Product> products = new ArrayList<>();
    private final Context context;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    public void updateProducts(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.activity_product_adapter, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvProductName = convertView.findViewById(R.id.tvProductName);
            viewHolder.tvProductPrice = convertView.findViewById(R.id.tvProductPrice);
            viewHolder.ivProductImage = convertView.findViewById(R.id.ivProductImage);
            viewHolder.ivFavorite = convertView.findViewById(R.id.ivFavorite);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Product product = getItem(position);
        viewHolder.tvProductName.setText(product.getName());
        viewHolder.tvProductPrice.setText(Constant.CURRENCY + String.valueOf(product.getPrice().setScale(0, BigDecimal.ROUND_HALF_UP)));
        viewHolder.ivProductImage.setImageResource(context.getResources().getIdentifier(
                product.getImageName(), "drawable", context.getPackageName()));

        if (product.isFavorite()) {
            viewHolder.ivFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            viewHolder.ivFavorite.setImageResource(R.drawable.ic_favorite_empty);
        }

        viewHolder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle favorite status
                product.setFavorite(!product.isFavorite());
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView tvProductName;
        TextView tvProductPrice;
        ImageView ivProductImage;
        ImageView ivFavorite;
    }
}
