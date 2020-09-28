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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    EditText email, password;
    private FirebaseAuth mAuth;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        findViewById(R.id.gotologin).setOnClickListener(this);
        findViewById(R.id.signupbtn).setOnClickListener(this);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        progressbar = findViewById(R.id.progressbar);

    }

    private void gotologin() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void registerUser() {

        String email_user = email.getText().toString().trim();
        String pass_user = password.getText().toString().trim();

        if (email_user.isEmpty()) {
            email.setError("Email is required.");
            email.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email_user).matches()) {
            email.setError("Please enter a valid Email.");
            email.requestFocus();
            return;
        }
        else if (pass_user.isEmpty()) {
            password.setError("Please enter your password.");
            password.requestFocus();
            return;
        }
        else if (pass_user.length() < 6) {
            password.setError("Password should be more than 6 characters long.");
            password.requestFocus();
            return;
        }
        else {
            progressbar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email_user, pass_user)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressbar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        user.sendEmailVerification();
                        Toast.makeText(getApplicationContext(),
                                "We sent you a verification Email, please check your inbox.",
                                Toast.LENGTH_SHORT).show();
                        gotologin();
                        finish();
                    }
                    else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "You are already registered, try logging in.", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gotologin:
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.signupbtn:
                registerUser();
                break;
        }
    }
}