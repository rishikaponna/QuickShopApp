package com.example.quickshopapp.MenuFiles;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.quickshopapp.HomeActivity;
import com.example.quickshopapp.IntroActivity;
import com.example.quickshopapp.R;
//import com.example.quickshopapp.ShowHistory;
import com.example.quickshopapp.ShowHistory;
import com.example.quickshopapp.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends BaseActivity {

    ImageView back,done;
    ImageView profileImg;
    EditText profileUsername;
    TextView profileEmail, profileLogout, profileHistory, displayUsername;

    String emailid;

    Toolbar profileToolbar;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    Uri setImageUri;

    Dialog dialog;

    ProgressDialog progressDialog;

    LinearLayout dynamicContent, bottonNavBar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottomNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_profile, null);
        dynamicContent.addView(wizard);

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.bottom_profile);

        rb.setBackgroundColor(R.color.item_selected);
        rb.setTextColor(Color.parseColor("#3F51B5"));

        profileToolbar=findViewById(R.id.profiletoolbar);
        profileToolbar.setBackgroundResource(R.drawable.bg_color);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        back=findViewById(R.id.profileBack);
        done=findViewById(R.id.done);
        profileImg=findViewById(R.id.profileImage);
        profileUsername=findViewById(R.id.profileUsername);
        profileEmail=findViewById(R.id.profileEmail);
        profileLogout=findViewById(R.id.profileLogout);
        profileHistory=findViewById(R.id.profileHistory);
        displayUsername=findViewById(R.id.displayUsername);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        DatabaseReference reference= database.getReference().child("users").child(auth.getUid());
        StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

        progressDialog.show();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Check if 'email' and 'name' child nodes exist
                    if (snapshot.hasChild("email") && snapshot.hasChild("name")) {
                        emailid = snapshot.child("email").getValue(String.class);
                        String name = snapshot.child("name").getValue(String.class);
                        String image = snapshot.child("imageUri").getValue(String.class);

                        if (name != null) {
                            profileUsername.setText(name);
                            displayUsername.setText(name);
                        }

                        if (image != null) {
                            Picasso.get().load(image).into(profileImg);
                        }

                        if (emailid != null) {
                            profileEmail.setText(emailid);
                        } else {
                            // Handle the case where 'email' is null
                            Toast.makeText(ProfileActivity.this, "Email is null in the database.", Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    } else {
                        // Handle the case where 'email' or 'name' child nodes are missing
                        Toast.makeText(ProfileActivity.this, "Email or name child nodes are missing in the database.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle the case where the user node doesn't exist
                    Toast.makeText(ProfileActivity.this, "User node does not exist in the database.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database read error
                progressDialog.dismiss(); // Dismiss the progress dialog if it's still showing

                // Check the type of error
                if (error.getCode() == DatabaseError.PERMISSION_DENIED) {
                    // Handle permission denied error (e.g., the user doesn't have access to the data)
                    Toast.makeText(ProfileActivity.this, "Permission denied. You don't have access to this data.", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle other database read errors
                    Toast.makeText(ProfileActivity.this, "An error occurred while fetching data from the database.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        profileHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this, ShowHistory.class);
                startActivity(intent);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name = profileUsername.getText().toString();
                String email = profileEmail.getText().toString();
                displayUsername.setText(name);

                if (setImageUri != null) {
                    storageReference.putFile(setImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalImageUri = uri.toString();
                                    Users users = new Users(auth.getUid(), name, email, finalImageUri);
                                    Log.i("Url:",storageReference.getDownloadUrl().toString());

                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(ProfileActivity.this, "Changes Saved!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                } else {
                    String imgUri = "https://firebasestorage.googleapis.com/v0/b/shopstop-9aef0.appspot.com/o/profilepicicon.jpg?alt=media&token=7ed59cae-6970-4292-a028-fde789214ac1";
                    Users users = new Users(auth.getUid(), name, email, imgUri);

                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Changes Saved!", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        profileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog= new Dialog(ProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_layout);

                TextView yesButton, noButton;
                yesButton= dialog.findViewById(R.id.yesButton);
                noButton= dialog.findViewById(R.id.noButton);

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ProfileActivity.this, IntroActivity.class));
                    }
                });

                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            setImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), setImageUri);
                profileImg.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}