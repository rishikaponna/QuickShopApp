package com.example.quickshopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.location.Address;
import android.location.Geocoder;




public class PlaceOrderActivity extends AppCompatActivity implements PaymentResultListener {

    private TextView deliveryAddressTextView;
    private CheckBox useCurrentAddressCheckbox;
    private EditText shipNameEditText;
    private EditText shipPhoneEditText;
    private EditText shipAddressEditText;
    private EditText shipPincodeEditText;
    private static final int PERMISSION_REQUEST_CODE = 123; // You can use any value here


    EditText shipName, shipPhone, shipAddress, shipPincode;
    AppCompatButton confirmOrder;
    FirebaseAuth auth;
    Intent intent;
    String totalAmount;
    TextView cartpricetotal;
    Toolbar cartToolbar;
    int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        // Initialize your UI elements
        deliveryAddressTextView = findViewById(R.id.delivery_address_text);
        useCurrentAddressCheckbox = findViewById(R.id.use_current_address_checkbox);
        shipNameEditText = findViewById(R.id.shipName);
        shipPhoneEditText = findViewById(R.id.shipPhone);
        shipAddressEditText = findViewById(R.id.shipAddress);
        shipPincodeEditText = findViewById(R.id.shipPincode);
        confirmOrder = findViewById(R.id.confirmOrder);
        // Set up the checkbox listener
        useCurrentAddressCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Show the confirmation dialog
                showConfirmationDialog();
                updateDeliveryAddressFromLocation();
                disableEditTextFields();
            } else {
                enableEditTextFields();
            }
        });

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

        shipName=findViewById(R.id.shipName);
        shipPhone=findViewById(R.id.shipPhone);
        shipAddress=findViewById(R.id.shipAddress);
        shipPincode=findViewById(R.id.shipPincode);
        confirmOrder=findViewById(R.id.confirmOrder);
        cartpricetotal=findViewById(R.id.cartpricetotal);
        cartToolbar=findViewById(R.id.cart_toolbar);

        auth=FirebaseAuth.getInstance();

        cartToolbar.setBackgroundResource(R.drawable.bg_color);
        confirmOrder.setBackgroundResource(R.drawable.bg_color);

        intent=getIntent();
        totalAmount = intent.getStringExtra("totalAmount");

        cartpricetotal.setText(totalAmount);

        String sAmount="100";

        //convert and round off
        amount=Math.round(Float.parseFloat(sAmount)*100);

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });
    }
    private void updateDeliveryAddressFromLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    getAddressFromLocation(latitude, longitude);
                }
            });
        } else {
            // Request the permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                String fullAddress = address.getAddressLine(0);
                String pincode = address.getPostalCode();

                deliveryAddressTextView.setText(fullAddress);
                shipAddressEditText.setText(fullAddress);
                shipPincodeEditText.setText(pincode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disableEditTextFields() {
        shipNameEditText.setEnabled(false);
        shipPhoneEditText.setEnabled(false);
        shipAddressEditText.setEnabled(false);
        shipPincodeEditText.setEnabled(true);

    }

    private void enableEditTextFields() {
        shipNameEditText.setEnabled(true);
        shipPhoneEditText.setEnabled(true);
        shipAddressEditText.setEnabled(true);
        shipPincodeEditText.setEnabled(true);

    }


    private void check() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, proceed with location operations.
            // Here, you can pull the current address and set it as the delivery address.
        } else {
            // Request the permission.
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_REQUEST_CODE);
        }

        if(TextUtils.isEmpty(shipName.getText().toString())){
            shipName.setError("Enter name");
            Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(shipPhone.getText().toString())){
            shipPhone.setError("Enter phone no.");
            Toast.makeText(this, "Please enter your phone no.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(shipAddress.getText().toString())){
            shipAddress.setError("Enter address");
            Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(shipPincode.getText().toString())){
            shipPincode.setError("Enter pincode.");
            Toast.makeText(this, "Please enter your pincode", Toast.LENGTH_SHORT).show();
        }else if (!TextUtils.isDigitsOnly(shipPincode.getText())) {
            shipPincode.setError("Enter valid pincode.");
            Toast.makeText(this, "Please enter a valid pincode.", Toast.LENGTH_SHORT).show();
        }
        else{
            paymentFunc();
        }

    }

    private void paymentFunc(){

        Checkout checkout= new Checkout();

        //set key id
        checkout.setKeyID("rzp_test_hO909D8S4i4w39");

        //set image
        checkout.setImage(R.drawable.rzp_logo);

        //initialize JSON object
        JSONObject object= new JSONObject();

        try {
            //put name
            object.put("name","Android User");

            //put description
            object.put("description","Test Payment");

            //put currency unit
            object.put("currency","INR");

            //put amount
            object.put("amount",amount);

            //put mobile number
            object.put("prefill.contact","9876543210");

            //put email
            object.put("prefill.email","androiduser@rzp.com");

            //open razorpay checkout activity
            checkout.open(PlaceOrderActivity.this,object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void confirmOrderFunc() {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(auth.getCurrentUser().getUid()).child("History")
                .child(saveCurrentDate.replaceAll("/","-")+" "+saveCurrentTime);

        HashMap<String, Object> ordersMap= new HashMap<>();
        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("name",shipName.getText().toString());
        ordersMap.put("phone",shipPhone.getText().toString());
        ordersMap.put("address",shipAddress.getText().toString());
        ordersMap.put("pincode",shipPincode.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                //empty user's cart after confirming order
                if(task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View").child(auth.getCurrentUser().getUid())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(PlaceOrderActivity.this, "Your order has been placed successfully", Toast.LENGTH_SHORT).show();
                                        Intent intentcart= new Intent(PlaceOrderActivity.this, HomeActivity.class);
                                        intentcart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intentcart);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Address");
        builder.setMessage("Do you want to use your current location as the delivery address?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            updateDeliveryAddressFromLocation();
            disableEditTextFields();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            // Uncheck the checkbox if the user cancels
            useCurrentAddressCheckbox.setChecked(false);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @Override
    public void onPaymentSuccess(String s) {
        confirmOrderFunc();
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Payment ID");
        builder.setMessage(s);
        builder.show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, perform the location operation here
                updateDeliveryAddressFromLocation();
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}