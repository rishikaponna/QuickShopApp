package com.example.quickshopapp.constant;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.net.Uri;
        import android.util.Log;
        import android.widget.Toast;

        import androidx.annotation.NonNull;

        import com.example.quickshopapp.HomeActivity;
        import com.example.quickshopapp.MenuFiles.AddProduct;
        import com.example.quickshopapp.model.Product;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

        import org.jetbrains.annotations.NotNull;

        import java.math.BigDecimal;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.HashMap;
        import java.util.List;

public final class Constant {

    public static final List<Integer> QUANTITY_LIST = new ArrayList<Integer>();

    static {
        for (int i = 1; i < 11; i++) QUANTITY_LIST.add(i);
    }

    public static final Product PRODUCT1 = new Product("1", "Redmi Note 11T", BigDecimal.valueOf(16999),
            "Product 1 Description", "redmi_note_11t");
    public static final Product PRODUCT2 = new Product("2", "OnePlus Nord CE", BigDecimal.valueOf(24999),
            "Product 2 Description", "oneplus_nord_ce");
    public static final Product PRODUCT3 = new Product("3", "Samsung Galaxy F42", BigDecimal.valueOf(17899),
            "Product 3 Description", "samsung_galaxy_f42");

    public static final List<Product> PRODUCT_LIST = new ArrayList<Product>();

    static {
        PRODUCT_LIST.add(PRODUCT1);
        PRODUCT_LIST.add(PRODUCT2);
        PRODUCT_LIST.add(PRODUCT3);
    }

    public static final String CURRENCY = "₹";

    private Constant() {
        // Private constructor to prevent instantiation
    }
}

