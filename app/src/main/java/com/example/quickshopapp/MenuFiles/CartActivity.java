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

import com.example.quickshopapp.HomeActivity;
import com.example.quickshopapp.MenuFiles.BaseActivity;
import com.example.quickshopapp.PlaceOrderActivity;
import com.example.quickshopapp.ProductDetails;
import com.example.quickshopapp.R;
import com.example.quickshopapp.model.Cart;
import com.example.quickshopapp.viewholder.CartViewHolder;
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

public class CartActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private AppCompatButton nextBtn;
    RecyclerView.LayoutManager layoutManager;
    TextView totalprice;
    private int overallPrice=0;
    Toolbar cartToolbar;

    FirebaseAuth auth;
    LinearLayout dynamicContent, bottonNavBar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottomNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_cart, null);
        dynamicContent.addView(wizard);


        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.bottom_cart);

        rb.setBackgroundColor(R.color.item_selected);
        rb.setTextColor(Color.parseColor("#3F51B5"));

        cartToolbar=findViewById(R.id.cart_toolbar);
        cartToolbar.setBackgroundResource(R.drawable.bg_color);

        auth=FirebaseAuth.getInstance();

        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        nextBtn=findViewById(R.id.next_button);
        totalprice=findViewById(R.id.totalprice);

        nextBtn.setBackgroundResource(R.drawable.bg_color);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalprice.getText().toString().equals("₹0") || totalprice.getText().toString().length()==0) {
                    Toast.makeText(CartActivity.this, "Cannot place order with 0 items", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(CartActivity.this, PlaceOrderActivity.class);
                    intent.putExtra("totalAmount", totalprice.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        overallPrice = 0;

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View").child(auth.getCurrentUser().getUid())
                        .child("Products"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull @NotNull CartViewHolder holder, int position, @NonNull @NotNull Cart model) {
                        String name=model.getName().replaceAll("\n"," ");
                        holder.cartProductName.setText(name);
                        holder.cartProductPrice.setText(model.getPrice());
                        String intPrice= model.getPrice().replace("₹","");
                        if (!TextUtils.isEmpty(intPrice)) {
                            try {
                                overallPrice += Integer.parseInt(intPrice);
                                totalprice.setText("₹" + String.valueOf(overallPrice));
                            } catch (NumberFormatException e) {
                            }
                        }


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Delete item");
                                builder.setMessage("Do you want to remove this product from cart?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                cartListRef.child("User View")
                                                        .child(auth.getCurrentUser().getUid())
                                                        .child("Products")
                                                        .child(model.getPid())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    String intPrice = model.getPrice().replace("₹", "");
                                                                    overallPrice -= Integer.valueOf(intPrice);
                                                                    totalprice.setText("₹" + String.valueOf(overallPrice));
                                                                    Toast.makeText(CartActivity.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
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
                    public CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                        CartViewHolder holder=new CartViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}