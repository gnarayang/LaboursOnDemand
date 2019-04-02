package com.example.labourondemand;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

public class CheckingActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private String current_user_id;
    private String TAG = CheckingActivity.class.getName();
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking);

        session = new SessionManager(getApplicationContext());
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(CheckingActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.d(TAG, "send to login : Checking");
            sendToLogin();
        } else {
            Log.d("cdsc", "csdc");
            current_user_id = mAuth.getCurrentUser().getUid();
            Log.d(TAG, current_user_id);

            firebaseFirestore.collection("uid").document(current_user_id)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.get("type").equals("customer")) {
                                session.setType("customer");
                                Log.d(TAG, "customer");

                                if (session.isSetupCustomer(current_user_id)) {
                                    Log.d("isSetup", "customer");

                                    CustomerFinal customerFinal = session.getCustomer(current_user_id);
                                    Log.d("customer is checking",customerFinal.toString());
                                    if (customerFinal.getNotPaidService() != null) {
                                        Intent intent = new Intent(CheckingActivity.this, PaymentActivity.class);
                                        intent.putExtra("customer", customerFinal);
                                        Log.d("Check","PAYMENT");
                                        startActivity(intent);
                                        finish();
                                    } else if (customerFinal.getNotReviewedService() != null) {
                                        Intent intent = new Intent(CheckingActivity.this, ReviewActivity2.class);
                                        intent.putExtra("customer", customerFinal);
                                        Log.d("Check","Review2");
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(CheckingActivity.this, CustomerHomeActivity.class);
                                        intent.putExtra("customer", customerFinal);
                                        Log.d("Check","CUSTOMEHOME");
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    firebaseFirestore.collection("customer").document(current_user_id).get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists()) {

                                                        CustomerFinal customerFinal = documentSnapshot.toObject(CustomerFinal.class);
                                                        customerFinal.setId(documentSnapshot.getId());
                                                        session.saveCustomer(customerFinal);

                                                        if (customerFinal.getNotPaidService() != null) {
                                                            Intent intent = new Intent(CheckingActivity.this, PaymentActivity.class);
                                                            intent.putExtra("customer", customerFinal);
                                                            Log.d("Check","PAYMENT");
                                                            startActivity(intent);
                                                            finish();
                                                        } else if (customerFinal.getNotReviewedService() != null) {
                                                            Intent intent = new Intent(CheckingActivity.this, ReviewActivity2.class);
                                                            intent.putExtra("customer", customerFinal);
                                                            Log.d("Check","Review2");
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Intent intent = new Intent(CheckingActivity.this, CustomerHomeActivity.class);
                                                            intent.putExtra("customer", customerFinal);
                                                            Log.d("Check","CUSTOMEHOME");
                                                            startActivity(intent);
                                                            finish();
                                                        }


                                                    } else {
                                                        Intent customer = new Intent(CheckingActivity.this, SetupActivity.class);
                                                        customer.putExtra("type", "customer");
                                                        startActivity(customer);
                                                        finish();
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                }

                            } else {
                                session.setType("labourer");
                                Log.d(TAG, "labourer");
                                if (session.isSetupLabourer(current_user_id)) {
                                    Intent labourer = new Intent(CheckingActivity.this, LabourerHomeActivity.class);
                                    labourer.putExtra("labourer", session.getLabourer(current_user_id));
                                    startActivity(labourer);
                                    finish();
                                } else {
                                    firebaseFirestore.collection("labourer").document(current_user_id).get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists()) {
                                                        LabourerFinal labourer = documentSnapshot.toObject(LabourerFinal.class);
                                                        labourer.setId(documentSnapshot.getId());
                                                        session.saveLabourer(labourer);
                                                        /*session.createProfileLabourer(labourer.getName(), labourer.getImage(), labourer.getDob(), labourer.getCity()
                                                                , labourer.getState(), labourer.getPhone(), labourer.getAddressLine1(), labourer.getAddressLine2()
                                                                , labourer.getAddressLine3(), labourer.getSkill(), 9L);*/

                                                        Intent intent = new Intent(CheckingActivity.this, LabourerHomeActivity.class);
                                                        intent.putExtra("labourer", labourer);
                                                        startActivity(intent);

                                                    } else {
                                                        Intent labourer = new Intent(CheckingActivity.this, SetupActivity.class);
                                                        labourer.putExtra("type", "labourer");
                                                        startActivity(labourer);
                                                        finish();
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "error : " + e.toString());
                        }
                    });
        }
    }
}
