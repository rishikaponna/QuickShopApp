package com.example.quickshopapp.MenuFiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.quickshopapp.model.Favorite;
import com.example.quickshopapp.viewholder.FavoriteProductViewHolder;


import com.example.quickshopapp.HomeActivity;
import com.example.quickshopapp.MenuFiles.BaseActivity;
import com.example.quickshopapp.PlaceOrderActivity;
import com.example.quickshopapp.ProductDetails;
import com.example.quickshopapp.R;
import com.example.quickshopapp.model.Cart;
import com.example.quickshopapp.viewholder.CartViewHolder;
import com.example.quickshopapp.viewholder.FavoriteProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class FavoriteActivity extends BaseActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    Toolbar favToolbar;

    FirebaseAuth auth;
    LinearLayout dynamicContent, bottonNavBar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottomNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_fav, null);
        dynamicContent.addView(wizard);


        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.bottom_cart);

        rb.setBackgroundColor(R.color.item_selected);
        rb.setTextColor(Color.parseColor("#3F51B5"));

        favToolbar=findViewById(R.id.fav_toolbar);
        favToolbar.setBackgroundResource(R.drawable.bg_color);

        auth=FirebaseAuth.getInstance();

        recyclerView=findViewById(R.id.fav_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference wishlistRef= FirebaseDatabase.getInstance().getReference().child("Wish List");

        FirebaseRecyclerOptions<Favorite> options= new FirebaseRecyclerOptions.Builder<Favorite>()
                .setQuery(wishlistRef.child("User View").child(auth.getCurrentUser().getUid())
                        .child("Products"),Favorite.class).build();

        FirebaseRecyclerAdapter<Favorite, FavoriteProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Favorite, FavoriteProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull @NotNull FavoriteProductViewHolder holder, int position, @NonNull @NotNull Favorite model) {
                        String name=model.getName().replaceAll("\n"," ");
                        holder.favProductName.setText(name);
                        holder.favProductPrice.setText(model.getPrice());

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder=new AlertDialog.Builder(FavoriteActivity.this);
                                builder.setTitle("Delete item");
                                builder.setMessage("Remove item from Favorites?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                wishlistRef.child("User View")
                                                        .child(auth.getCurrentUser().getUid())
                                                        .child("Products")
                                                        .child(model.getPid())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(FavoriteActivity.this, "Item removed successfully", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();
                                            }
                                        });
                                builder.show();
                            }
                        });
                    }
                    @NonNull
                    @NotNull
                    @Override
                    public FavoriteProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_items,parent,false);
                        return new FavoriteProductViewHolder(view);
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}