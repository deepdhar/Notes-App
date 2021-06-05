package com.example.android.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText mSignUpEmail, mSignUpPass;
    private Button mSignUpBn;
    private TextView mGoBackToLogin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mSignUpEmail = findViewById(R.id.signup_email_et);
        mSignUpPass = findViewById(R.id.signup_password_et);
        mSignUpBn = findViewById(R.id.signup_button);
        mGoBackToLogin = findViewById(R.id.goBackToLogin_signup_tv);

        firebaseAuth = FirebaseAuth.getInstance();



        mGoBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mSignUpBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mSignUpEmail.getText().toString().trim();
                String pass = mSignUpPass.getText().toString().trim();

                if(email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if(pass.length()<7) {
                    Toast.makeText(SignUpActivity.this, "Password should be greater than 7 digits", Toast.LENGTH_SHORT).show();

                } else {
                    //register the user in firebase
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "You are registered successfully", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    //send email verification
    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(SignUpActivity.this, "Verification Email is sent to email ID", Toast.LENGTH_LONG).show();
                    firebaseAuth.signOut();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            });
        } else {
            Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
        }
    }
}