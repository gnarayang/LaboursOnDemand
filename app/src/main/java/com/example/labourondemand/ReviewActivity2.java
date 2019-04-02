package com.example.labourondemand;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labourondemand.notifications.Api;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewActivity2 extends AppCompatActivity {

    private Toolbar toolbar;
    private SessionManager session;
    private RatingBar ratingBar;
    private Button submitButton;
    private ImageView servicePhoto;
    private TextView title, endTime, amount;
    private ServicesFinal servicesFinal;
    private RecyclerView recyclerView;
    private LabourerAdapter labourerAdapter;
    private FirebaseFirestore firebaseFirestore;
    private TextInputLayout feedback;
    private ProgressBar progressBar;
    private CustomerFinal customerFinal;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review2);


        sessionManager = new SessionManager(getApplicationContext());

        recyclerView = findViewById(R.id.review_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        servicesFinal = (ServicesFinal) getIntent().getExtras().getSerializable("services");
        customerFinal = (CustomerFinal) getIntent().getExtras().getSerializable("customer");
        toolbar = findViewById(R.id.review_tb);
        toolbar.setTitle("Please Review");
        setSupportActionBar(toolbar);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        recyclerView = findViewById(R.id.review_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        /*labourerAdapter = new LabourerAdapter(this,servicesFinal);
        recyclerView.setAdapter(labourerAdapter);*/

        ratingBar = findViewById(R.id.review_rb);
        submitButton = findViewById(R.id.review_btn_submit);
        title = findViewById(R.id.review_title_tv);
        endTime = findViewById(R.id.review_end_time);
        amount = findViewById(R.id.review_cost_tv);
        servicePhoto = findViewById(R.id.review_iv);
        feedback = findViewById(R.id.review_feedback_til);
        progressBar = findViewById(R.id.review_pb);
        firebaseFirestore = FirebaseFirestore.getInstance();

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#ff5722"), PorterDuff.Mode.SRC_ATOP);

        if (servicesFinal == null) {
            firebaseFirestore.collection("services").document(customerFinal.getNotReviewedService())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            servicesFinal = documentSnapshot.toObject(ServicesFinal.class);
                            servicesFinal.setServiceId(documentSnapshot.getId());

                            title.setText(servicesFinal.getTitle());
                            amount.setText(String.valueOf(servicesFinal.getNumOfLabourers()*servicesFinal.getCustomerAmount()));
                            endTime.setText(servicesFinal.getEndTime());
                            if(servicesFinal.getSkill().equals("Carpenter"))
                            {
                                servicePhoto.setImageDrawable(getDrawable(R.drawable.ic_carpenter_tools_colour));
                            }else if(servicesFinal.getSkill().equals("Plumber"))
                            {
                                servicePhoto.setImageDrawable(getDrawable(R.drawable.ic_plumber_tools));
                            }else if(servicesFinal.getSkill().equals("Electrician"))
                            {
                                servicePhoto.setImageDrawable(getDrawable(R.drawable.ic_electric_colour));
                            }else if(servicesFinal.getSkill().equals("Painter"))
                            {
                                servicePhoto.setImageDrawable(getDrawable(R.drawable.ic_paint_roller));
                            }else if(servicesFinal.getSkill().equals("Constructor"))
                            {
                                servicePhoto.setImageDrawable(getDrawable(R.drawable.ic_construction_colour));
                            }else if(servicesFinal.getSkill().equals("Chef"))
                            {
                                servicePhoto.setImageDrawable(getDrawable(R.drawable.ic_cooking_colour));
                            }

                            servicesFinal.setSelectedLabourers(new ArrayList<>());
                            Log.d("servic Payment",servicesFinal.toString()+"!");
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
            amount.setText(String.valueOf(servicesFinal.getNumOfLabourers()*servicesFinal.getCustomerAmount()));
            endTime.setText(servicesFinal.getEndTime());
            if(servicesFinal.getSkill().equals("Carpenter"))
            {
                servicePhoto.setImageDrawable(getDrawable(R.drawable.ic_carpenter_tools_colour));
            }else if(servicesFinal.getSkill().equals("Plumber"))
            {
                servicePhoto.setImageDrawable(getDrawable(R.drawable.ic_plumber_tools));
            }else if(servicesFinal.getSkill().equals("Electrician"))
            {
                servicePhoto.setImageDrawable(getDrawable(R.drawable.ic_electric_colour));
            }else if(servicesFinal.getSkill().equals("Painter"))
            {
                servicePhoto.setImageDrawable(getDrawable(R.drawable.ic_paint_roller));
            }else if(servicesFinal.getSkill().equals("Constructor"))
            {
                servicePhoto.setImageDrawable(getDrawable(R.drawable.ic_construction_colour));
            }else if(servicesFinal.getSkill().equals("Chef"))
            {
                servicePhoto.setImageDrawable(getDrawable(R.drawable.ic_cooking_colour));
            }

            servicesFinal.setSelectedLabourers(new ArrayList<LabourerFinal>());
            Log.d("payment SELECTEDLABOUR ",servicesFinal.toString()+"!");
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

        }


        submitButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (ratingBar.getRating() == 0.0)
                    //ratingTextView.setText("Please Enter Rating ");
                    ratingBar.setIsIndicator(false);
                else {
                    if (feedback.getEditText().getText().toString().compareTo("") == 0)
                        feedback.setError("Please enter text");
                    else if (feedback.getEditText().getText().toString().length() > 100)
                        feedback.setError("Length exceeding 100");
                    else {
                        feedback.setError(null);
                        //ratingTextView.setText("Ratings : " + ratingBar.getRating());
                        submitButton.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        submitReview();
                        for(String s : servicesFinal.getSelectedLabourerUID()){
                            firebaseFirestore.collection("labourer").document(s)
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
                                            String title = "Your work has been reviewed by Customer";
                                            String body = "Rating: " + servicesFinal.getRating().toString();
                                            Call<ResponseBody> call = api.sendNotification(token,title,body);

                                            call.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    try {
                                                        Toast.makeText(getApplicationContext(),response.body().string(),Toast.LENGTH_LONG).show();
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
                        }
                    }
                    }
                }
        });

    }

    private void submitReview() {
        progressBar.setVisibility(View.VISIBLE);
        String feedbackString = feedback.getEditText().getText().toString();
        float ratingFloat = ratingBar.getRating();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        HashMap<String, Object> map = new HashMap<>();
        map.put("rating", ratingFloat);
        map.put("feedback",feedbackString);
        map.put("status","history");
        map.put("isPaid",true);

        firebaseFirestore.collection("services").document(servicesFinal.getServiceId())
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        firebaseFirestore.collection("customer").document(customerFinal.getId())
                                .update("notReviewedService",null)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        progressBar.setVisibility(View.GONE);
                                        customerFinal.setNotReviewedService(null);
                                        sessionManager.saveCustomer(customerFinal);
                                        Log.d("success review","yes");
                                        Intent intent = new Intent(ReviewActivity2.this,CustomerHomeActivity.class);
                                        intent.putExtra("customer",customerFinal);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                        Log.d("failure",e.toString());


                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("failure",e.toString());
                    }
                });
    }
}
