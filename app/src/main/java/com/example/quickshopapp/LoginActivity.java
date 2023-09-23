package com.example.quickshopapp;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private static final int PERMISSION_REQUEST_CODE = 123; // You can use any value here
    private static final int REQUEST_CODE_BIOMETRIC_ENROLL = 123; // Unique request code for biometric enrollment

    FirebaseAuth auth;
    EditText emailEditText, passwordEditText;
    Button signInButton;
    LinearLayout signUpText;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        emailEditText = findViewById(R.id.loginEmail);
        passwordEditText = findViewById(R.id.loginPassword);
        signInButton = findViewById(R.id.signInButton);
        signInButton.setBackgroundResource(R.drawable.intro_signin);
        signUpText = findViewById(R.id.signUpText);

        // Initialize biometric components
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                // Biometric authentication succeeded, proceed to home screen
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .setNegativeButtonText("Use account password")
                .build();

        // Set up the biometricLoginButton click listener
        Button biometricLoginButton = findViewById(R.id.biometric_login);
        biometricLoginButton.setOnClickListener(view -> {
            // Check if biometric authentication is available and initiate it
            checkAndInitiateBiometricLogin();
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String email = emailEditText.getEditableText().toString();
                String password = passwordEditText.getEditableText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    progressDialog.dismiss();
                    emailEditText.setError("Invalid Email");
                    Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                } else if (password.length() <= 6) {
                    progressDialog.dismiss();
                    passwordEditText.setError("Invalid password");
                    Toast.makeText(LoginActivity.this, "Please enter more than 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Biometric authentication succeeded, proceed to home screen
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Error in login. Try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkAndInitiateBiometricLogin() {
        BiometricManager biometricManager = BiometricManager.from(this);

        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // Biometric authentication is available, attempt it
                biometricPrompt.authenticate(promptInfo);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                // No biometric features available on this device
                Toast.makeText(this, "Biometric authentication not available", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                // Biometric features are currently unavailable
                Toast.makeText(this, "Biometric authentication is temporarily unavailable", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // The user hasn't enrolled any biometric credentials
                // You can provide an option to enroll here
                initiateBiometricEnrollment();
                break;
        }
    }

    private void initiateBiometricEnrollment() {
        // Implement the logic to initiate biometric enrollment
        // For example, you can start an enrollment activity or show a dialog
        // Make sure to handle the result in onActivityResult()
        // In this example, we simply show a message to the user
        Toast.makeText(this, "Enroll a biometric credential first", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of biometric enrollment here
        if (requestCode == REQUEST_CODE_BIOMETRIC_ENROLL) {
            // Check resultCode and perform any necessary actions
            if (resultCode == RESULT_OK) {
                // Biometric enrollment was successful
                // You can update UI or take other actions here
                Toast.makeText(this, "Biometric enrollment successful", Toast.LENGTH_SHORT).show();
            } else {
                // Biometric enrollment was not successful
                // Handle the error or inform the user
                Toast.makeText(this, "Biometric enrollment failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
