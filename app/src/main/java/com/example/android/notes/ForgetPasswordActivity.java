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
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText mForgotPassword;
    private Button mPasswordRecoverBn;
    private TextView mGoBackToLogin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mForgotPassword = findViewById(R.id.forgot_email_et);
        mPasswordRecoverBn = findViewById(R.id.recover_password_button);
        mGoBackToLogin = findViewById(R.id.goBackToLogin_forgot_tv);

        firebaseAuth = FirebaseAuth.getInstance();

        mGoBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mPasswordRecoverBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mForgotPassword.getText().toString().trim();
                if(email.isEmpty()) {
                    Toast.makeText(ForgetPasswordActivity.this, "Enter your email!", Toast.LENGTH_SHORT).show();
                } else {
                    //we have to send email
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(ForgetPasswordActivity.this, "Email Sent, chek your inbox", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, "Account doesn't exist or Incorrect Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}