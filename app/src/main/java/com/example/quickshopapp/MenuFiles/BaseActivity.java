package com.example.quickshopapp.MenuFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.quickshopapp.HomeActivity;
import com.example.quickshopapp.R;

public class BaseActivity extends AppCompatActivity {

    RadioGroup radioGroup1;
    RadioButton home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        radioGroup1=(RadioGroup)findViewById(R.id.radioGroup1);
        home = (RadioButton)findViewById(R.id.bottom_home);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(RadioGroup group, int i)
            {

                if (i == R.id.bottom_home) {
                    Log.i("home", "inside home" + i);
                    Intent in = new Intent(getBaseContext(), HomeActivity.class);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                } else if (i == R.id.bottom_addprod) {
                    Log.i("addprod", "inside addprod" + i);
                    Intent in = new Intent(getBaseContext(), AddProduct.class);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                } else if (i == R.id.bottom_search) {
                    Log.i("search", "inside search" + i);
                    Intent in = new Intent(getBaseContext(), SearchActivity.class);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                } else if (i == R.id.bottom_cart) {
                    Log.i("cart", "inside cart" + i);
                    Intent in = new Intent(getBaseContext(), CartActivity.class);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }else if (i == R.id.bottom_fav) {
                    Log.i("favorites", "inside favorites" + i);
                    Intent in = new Intent(getBaseContext(), FavoriteActivity.class);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }else if (i == R.id.bottom_profile) {
                    Log.i("profile", "inside profile" + i);
                    Intent in = new Intent(getBaseContext(), ProfileActivity.class);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                }

            }
        });
    }
}