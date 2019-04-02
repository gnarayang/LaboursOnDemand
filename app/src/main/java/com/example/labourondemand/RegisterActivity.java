package com.example.labourondemand;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText reg_email_field;
    private EditText reg_pass_field;
    private EditText reg_confirm_pass_field;
    private Button reg_btn;
    private Button reg_login_btn;
    private ProgressBar reg_progress;
    private RadioGroup radioGroup;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private String selectedId;
    private String TAG = RegisterActivity.class.getName();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        sessionManager = new SessionManager(getApplicationContext());

        reg_email_field = findViewById(R.id.register_et_email);
        reg_pass_field =  findViewById(R.id.register_et_password);
        reg_confirm_pass_field = findViewById(R.id.register_et_confirm_password);
        reg_btn = findViewById(R.id.register_btn_register);
        reg_login_btn = findViewById(R.id.register_btn_login);
        reg_progress = findViewById(R.id.register_pb);
        radioGroup = findViewById(R.id.register_rg);

        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendToLogin();

            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = reg_email_field.getText().toString();
                String pass = reg_pass_field.getText().toString();
                String confirm_pass = reg_confirm_pass_field.getText().toString();

                if(radioGroup.getCheckedRadioButtonId() == R.id.register_rg_customer){
                    selectedId = "customer";
                }else{
                    selectedId = "labourer";
                }

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(confirm_pass) ){

                    if(pass.equals(confirm_pass)){

                        reg_progress.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("type",selectedId);
                                    firebaseFirestore.collection("uid").document(mAuth.getUid())
                                            .set(map)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        sessionManager.putEmailAndPassword(email,pass);
                                                        Log.d("sessionregi",sessionManager.getEmail()+"!");
                                                        Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
                                                        setupIntent.putExtra("type",selectedId);
                                                        startActivity(setupIntent);
                                                        finish();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });


                                } else {

                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();

                                }

                                reg_progress.setVisibility(View.INVISIBLE);

                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Confirm Password and Password Field doesn't match.", Toast.LENGTH_LONG).show();
                    }

                }else{
                    if(email.equals("")){
                        reg_email_field.setError("Fill");
                    }
                    if(pass.equals("")){
                        reg_pass_field.setError("Fill");
                    }
                    if(confirm_pass.equals("")){
                        reg_confirm_pass_field.setError("Fill");
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
            sendToChecking();
        }

    }

    private void sendToChecking() {

        Intent mainIntent = new Intent(RegisterActivity.this, CheckingActivity.class);
        startActivity(mainIntent);
        finish();

    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }
}