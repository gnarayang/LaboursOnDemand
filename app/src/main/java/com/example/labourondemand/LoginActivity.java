package com.example.labourondemand;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmailText;
    private EditText loginPassText;
    private Button loginBtn;
    private Button loginRegBtn;
    private Button forgotPassword;
    private FirebaseAuth mAuth;
    private SessionManager session;
    private ProgressBar loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        session = new SessionManager(getApplicationContext());

        loginEmailText = findViewById(R.id.login_et_email);
        loginPassText = findViewById(R.id.login_et_password);
        loginBtn = findViewById(R.id.login_btn);
        loginRegBtn = findViewById(R.id.login_reg_btn);
        loginProgress = findViewById(R.id.login_progress);
        forgotPassword =findViewById(R.id.login_forgot_password_btn);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPassword = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(forgotPassword);
                finish();
            }
        });

        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
                finish();

            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginEmail = loginEmailText.getText().toString();
                String loginPass = loginPassText.getText().toString();

                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)){
                    loginProgress.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(loginEmail, loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                session.putEmailAndPassword(loginEmail,loginPass);
                                sendToCheck();

                            } else {

                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();

                            }

                            loginProgress.setVisibility(View.INVISIBLE);

                        }
                    });

                }else{
                    if(loginEmail.equals("")){
                        loginEmailText.setError("Fill");
                    }
                    if(loginPass.equals("")){
                        loginPassText.setError("Fill");
                    }

                }

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            sendToCheck();
        }

    }

    private void sendToCheck() {

        Intent mainIntent = new Intent(LoginActivity.this, CheckingActivity.class);
        startActivity(mainIntent);
        finish();

    }
}

