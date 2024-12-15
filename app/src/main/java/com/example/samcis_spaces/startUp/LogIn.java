package com.example.samcis_spaces.startUp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.samcis_spaces.R;
import com.example.samcis_spaces.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogIn extends AppCompatActivity {

    EditText email, password;
    Button loginButton;
    TextView signUp;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        // Initialize Views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signUp);

        // Initialize Firebase Auth and Firestore
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Auto-login if user is already authenticated
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        // Login Button Logic
        loginButton.setOnClickListener(v -> {
            if (!validateFields()) return;

            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            fAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = fAuth.getCurrentUser();
                                if (user != null) {
                                    // Fetch user data from Firestore
                                    DocumentReference dReference = fStore.collection("Users").document(user.getUid());
                                    dReference.get()
                                            .addOnSuccessListener(documentSnapshot -> {
                                                Toast.makeText(LogIn.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                finish();
                                            })
                                            .addOnFailureListener(e ->
                                                    Toast.makeText(LogIn.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show()
                                            );
                                }
                            } else {
                                Toast.makeText(LogIn.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        // Sign-Up Navigation
        signUp.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SignUp.class));
        });
    }

    private boolean validateFields() {
        boolean valid = true;

        if (email.getText().toString().trim().isEmpty()) {
            email.setError("Enter a valid email.");
            email.requestFocus();
            valid = false;
        }
        if (password.getText().toString().trim().isEmpty()) {
            password.setError("Enter a valid password.");
            password.requestFocus();
            valid = false;
        }

        return valid;
    }
}
