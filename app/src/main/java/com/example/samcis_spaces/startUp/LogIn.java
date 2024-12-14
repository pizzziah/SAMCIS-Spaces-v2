package com.example.samcis_spaces.startUp;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.samcis_spaces.R;
import com.example.samcis_spaces.main.MainActivity;

public class LogIn extends AppCompatActivity {

    EditText email, password;
    Button loginButton;
    TextView signUp;
    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signUp);

        // Toggle password visibility
        password.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                togglePasswordVisibility();
            }
            return true;
        });

        // Login button logic
        loginButton.setOnClickListener(v -> {
            if (validateFields()) {
                // Proceed with login logic
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        signUp.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_slash, 0);
        } else {
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_fill, 0);
        }
        isPasswordVisible = !isPasswordVisible;
        password.setSelection(password.getText().length());
    }

    private boolean validateFields() {
        boolean valid = true;

        if (email.getText().toString().isEmpty()) {
            email.setError("Invalid.");
            valid = false;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Invalid.");
            valid = false;
        }

        return valid;
    }
}
