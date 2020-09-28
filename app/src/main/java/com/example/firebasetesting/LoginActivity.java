package com.example.firebasetesting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText email, password;
    FirebaseAuth mAuth;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.gotosignup).setOnClickListener(this);
        findViewById(R.id.loginbtn).setOnClickListener(this);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        progressbar = findViewById(R.id.progressbar);
    }

    private void loginUser() {

        String email_user = email.getText().toString().trim();
        String pass_user = password.getText().toString().trim();

        if (email_user.isEmpty()) {
            email.setError("Email is required.");
            email.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email_user).matches()) {
            email.setError("Please enter a valid Email.");
            email.requestFocus();
            return;
        } else if (pass_user.isEmpty()) {
            password.setError("Please enter your password.");
            password.requestFocus();
            return;
        } else if (pass_user.length() < 6) {
            password.setError("Password should be more than 6 characters long.");
            password.requestFocus();
            return;
        } else {
            progressbar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email_user, pass_user).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressbar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        if (mAuth.getCurrentUser().isEmailVerified()) {
                            finish();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Please verify your Email address.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gotosignup:
                finish();
                startActivity(new Intent(this, SignupActivity.class));
                break;

            case R.id.loginbtn:
                loginUser();
                break;
        }
    }
}