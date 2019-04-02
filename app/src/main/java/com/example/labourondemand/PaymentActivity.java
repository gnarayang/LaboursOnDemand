package com.example.labourondemand;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labourondemand.notifications.Api;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentActivity extends AppCompatActivity {

    private Context context = this;
    private Toolbar toolbar;
    private ServicesFinal servicesFinal;
    private Button pay;
    private FirebaseFirestore firebaseFirestore;
    private CustomerFinal customerFinal;
    private RecyclerView recyclerView;
    private LabourerAdapter labourerAdapter;
    private ProgressBar progressBar;
    private SessionManager sessionManager;
    private TextView amount, title, endTime, numOfLabourers;
    private ImageView skill;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        sessionManager = new SessionManager(getApplicationContext());

        progressBar = findViewById(R.id.payment_pb);
        title = findViewById(R.id.payment_title_tv);
        amount = findViewById(R.id.payment_amount_tv);
        endTime = findViewById(R.id.payment_end_time);
        numOfLabourers = findViewById(R.id.payment_numOfLabourers);
        skill = findViewById(R.id.payment_iv);

        toolbar = findViewById(R.id.payment_tb);
        toolbar.setTitle("Make Payment");
        setSupportActionBar(toolbar);
       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        firebaseFirestore = FirebaseFirestore.getInstance();
        servicesFinal = (ServicesFinal) getIntent().getExtras().getSerializable("services");
        customerFinal = (CustomerFinal) getIntent().getExtras().getSerializable("customer");
        Log.d("customer Payment", customerFinal.toString() + "!");
        //Log.d("services Payment ",servicesFinal.toString()+"!");

        recyclerView = findViewById(R.id.payment_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if (servicesFinal == null) {
            firebaseFirestore.collection("services").document(customerFinal.getNotPaidService())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            servicesFinal = documentSnapshot.toObject(ServicesFinal.class);
                            servicesFinal.setServiceId(documentSnapshot.getId());

                            title.setText(servicesFinal.getTitle());
                            numOfLabourers.setText(servicesFinal.getNumOfLabourers() + " Labourers");
                            amount.setText((servicesFinal.getNumOfLabourers() * servicesFinal.getCustomerAmount()) + "");
                            if (servicesFinal.getSkill().equals("Carpenter")) {
                                skill.setImageDrawable(getDrawable(R.drawable.ic_carpenter_tools_colour));
                            } else if (servicesFinal.getSkill().equals("Plumber")) {
                                skill.setImageDrawable(getDrawable(R.drawable.ic_plumber_tools));
                            } else if (servicesFinal.getSkill().equals("Electrician")) {
                                skill.setImageDrawable(getDrawable(R.drawable.ic_electric_colour));
                            } else if (servicesFinal.getSkill().equals("Painter")) {
                                skill.setImageDrawable(getDrawable(R.drawable.ic_paint_roller));
                            } else if (servicesFinal.getSkill().equals("Constructor")) {
                                skill.setImageDrawable(getDrawable(R.drawable.ic_construction_colour));
                            } else if (servicesFinal.getSkill().equals("Chef")) {
                                skill.setImageDrawable(getDrawable(R.drawable.ic_cooking_colour));
                            }

                            servicesFinal.setSelectedLabourers(new ArrayList<>());
                            Log.d("servic Payment", servicesFinal.toString() + "!");
                            labourerAdapter = new LabourerAdapter(getApplicationContext(), servicesFinal);
                            recyclerView.setAdapter(labourerAdapter);
                            for (String s : servicesFinal.getSelectedLabourerUID()) {

                                firebaseFirestore.collection("labourer").document(s)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                LabourerFinal labourerFinal = documentSnapshot.toObject(LabourerFinal.class);
                                                labourerFinal.setId(documentSnapshot.getId());
                                                labourerAdapter.added(labourerFinal);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } else {

            title.setText(servicesFinal.getTitle());
            numOfLabourers.setText(servicesFinal.getNumOfLabourers() + " Labourers");
            amount.setText((servicesFinal.getNumOfLabourers() * servicesFinal.getCustomerAmount()) + "");
            if (servicesFinal.getSkill().equals("Carpenter")) {
                skill.setImageDrawable(getDrawable(R.drawable.ic_carpenter_tools_colour));
            } else if (servicesFinal.getSkill().equals("Plumber")) {
                skill.setImageDrawable(getDrawable(R.drawable.ic_plumber_tools));
            } else if (servicesFinal.getSkill().equals("Electrician")) {
                skill.setImageDrawable(getDrawable(R.drawable.ic_electric_colour));
            } else if (servicesFinal.getSkill().equals("Painter")) {
                skill.setImageDrawable(getDrawable(R.drawable.ic_paint_roller));
            } else if (servicesFinal.getSkill().equals("Constructor")) {
                skill.setImageDrawable(getDrawable(R.drawable.ic_construction_colour));
            } else if (servicesFinal.getSkill().equals("Chef")) {
                skill.setImageDrawable(getDrawable(R.drawable.ic_cooking_colour));
            }

            servicesFinal.setSelectedLabourers(new ArrayList<LabourerFinal>());
            Log.d("payment SELECTEDLABOUR ", servicesFinal.toString() + "!");
            labourerAdapter = new LabourerAdapter(this, servicesFinal);
            recyclerView.setAdapter(labourerAdapter);

            for (String s : servicesFinal.getSelectedLabourerUID()) {

                firebaseFirestore.collection("labourer").document(s)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                LabourerFinal labourerFinal = documentSnapshot.toObject(LabourerFinal.class);
                                labourerFinal.setId(documentSnapshot.getId());
                                labourerAdapter.added(labourerFinal);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }

        }/* else {
            labourerAdapter = new LabourerAdapter(this, servicesFinal);
            recyclerView.setAdapter(labourerAdapter);
        }*/


        pay = findViewById(R.id.payment_pay_btn);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(v);
            }
        });

    }

    public void check(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = LayoutInflater.from(v.getContext());
        final View dialogView = inflater.inflate(R.layout.dialog_confirm_password, null);
        builder.setView(dialogView);

        builder.setTitle("Confirm Password");
                /*AlertDialog alertDialog = builder.create();
                alertDialog.show();*/

        builder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText password = dialogView.findViewById(R.id.wallet_et_password);

                if (password.getText().toString().equals(customerFinal.getPassword())) {
                    // do payent
                    compute();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_LONG).show();

                }


            }

        });
        builder.setView(dialogView);
        builder.show();
    }

    public void compute()
    {
        progressBar.setVisibility(View.VISIBLE);

        firebaseFirestore.collection("customer").document(customerFinal.getId())
                .update("wallet", customerFinal.getWallet() - (servicesFinal.getNumOfLabourers() * servicesFinal.getCustomerAmount()),
                        "notPaidService", null,
                        "notReviewedService", servicesFinal.getServiceId())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        for (LabourerFinal labourerFinal : servicesFinal.getSelectedLabourers()) {
                            firebaseFirestore.collection("labourer").document(labourerFinal.getId())
                                    .update("wallet", labourerFinal.getWallet() + servicesFinal.getCustomerAmount())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            firebaseFirestore.collection("labourer").document(labourerFinal.getId())
                                                    .get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            String token = documentSnapshot.getString("token");
                                                            Retrofit retrofit = new Retrofit.Builder()
                                                                    .baseUrl("https://labourondemand-8e636.firebaseapp.com/api/")
                                                                    .addConverterFactory(GsonConverterFactory.create())
                                                                    .build();

                                                            Api api = retrofit.create(Api.class);
                                                            String title = "Payment Received from " + customerFinal.getName();
                                                            String body = "Job : " + servicesFinal.getTitle();
                                                            Call<ResponseBody> call = api.sendNotification(token, title, body);

                                                            call.enqueue(new Callback<ResponseBody>() {
                                                                @Override
                                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                    try {
                                                                        Toast.makeText(context, response.body().string(), Toast.LENGTH_LONG).show();
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                                }
                                                            });
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });
                                            if (servicesFinal.getSelectedLabourers().indexOf(labourerFinal) == servicesFinal.getSelectedLabourers().size() - 1) {
                                                customerFinal.setNotPaidService(null);
                                                customerFinal.setWallet(customerFinal.getWallet() - (servicesFinal.getNumOfLabourers() * servicesFinal.getCustomerAmount()));
                                                sessionManager.saveCustomer(customerFinal);
                                                progressBar.setVisibility(View.GONE);
                                                Intent intent = new Intent(PaymentActivity.this, ThankYouActivity.class);
                                                intent.putExtra("customer", customerFinal);
                                                intent.putExtra("services", labourerAdapter.getService());
                                                startActivity(intent);
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {


                    }
                });

    }
}
