package com.example.android.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mLoginEmail, mLoginPass;
    private Button mLoginBn, mSignInBn;
    private TextView mForgotPassword;
    ProgressBar mProgressBarMain;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginEmail = findViewById(R.id.login_email_et);
        mLoginPass = findViewById(R.id.login_password_et);
        mLoginBn = findViewById(R.id.login_button);
        mSignInBn = findViewById(R.id.signup_button);
        mForgotPassword = findViewById(R.id.forgot_password_tv);
        mProgressBarMain = findViewById(R.id.progressBar_main);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser!=null) {
            finish();
            startActivity(new Intent(getApplicationContext(), NotesActivity.class));
        }


        mSignInBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
            }
        });

        mLoginBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mLoginEmail.getText().toString().trim();
                String pass = mLoginPass.getText().toString().trim();

                if(email.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    //login the user
                    mProgressBarMain.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                checkEmailverification();
                            } else {
                                Toast.makeText(MainActivity.this, "Account doesn't exist or Wrong Password", Toast.LENGTH_SHORT).show();
                                mProgressBarMain.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    //check email verification
    private void checkEmailverification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()==true) {
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(), NotesActivity.class));
        } else {
            mProgressBarMain.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Verify your email first", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}