package com.example.labourondemand;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LabourerJobsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String tag = LabourerHomeActivity.class.getName();
    private BottomNavigationView navigation;
    private TextView nameHeader;
    private ImageView photoHeader;
    private LabourerFinal labourer;

    private SessionManager sessionManager;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labourer_jobs);

        toolbar = findViewById(R.id.labourer_jobs_tb);
        drawerLayout = findViewById(R.id.labourer_jobs_dl);
        navigationView = findViewById(R.id.labourer_jobs_nv);
        navigation = findViewById(R.id.bottom_nav_view);

        sessionManager = new SessionManager(getApplicationContext());

        labourer = (LabourerFinal) getIntent().getExtras().get("labourer");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.getMenu().getItem(2).setChecked(true);
        View header = navigationView.getHeaderView(0);
        nameHeader = header.findViewById(R.id.nav_header_tv);
        photoHeader = header.findViewById(R.id.nav_header_iv);
        nameHeader.setText(labourer.getName());
        Glide.with(getApplicationContext()).load(labourer.getImage()).into(photoHeader);
        navigationView.setNavigationItemSelectedListener(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(2).setChecked(true);




    }

   /* private void fetchFromFirebase() {

        firebaseFirestore.collection("labourer").document(firebaseAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //labourer = new Labourer();
                        if (documentSnapshot.getData() != null) {
                            labourer = documentSnapshot.toObject(LabourerFinal.class);
                            Log.d(tag, documentSnapshot.getData().toString() + "!");

                            if (labourer.getServices() != null) {
                                Log.d("tagggg",labourer.getSkill()+"!");
                                ArrayList<String> s = labourer.getServices();
                                Log.d("LabourMainActivity",s+"!");
                                fetchServices(s);
                            }else{

                            }

                        } else {
                            Log.d(tag, "null");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void fetchServices(ArrayList<String> labourServices) {

        if(labourServices != null && labourServices.size()>0) {
            visibleText.setVisibility(View.GONE);
            firebaseFirestore.collection("services").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                ServicesFinal services;
                                Log.d("tag", labourer.getSkill() + "!" + documentSnapshot.get("skill") + "!" + documentSnapshot.getData().toString());
                                for (int i = 0; i < labourServices.size(); i++) {
                                    if (documentSnapshot.getId().equals(labourServices.get(i))) {
                                        services = documentSnapshot.toObject(ServicesFinal.class);
                                        services.setServiceId(documentSnapshot.getId());

                                        final ServicesFinal finalServices = services;
                                        firebaseFirestore.collection("customer").document(services.getCustomerUID()).get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                        finalServices.setCustomer(documentSnapshot.toObject(CustomerFinal.class));
                                                        dashboardAdapter.added(finalServices);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(tag, "error fetchService2 : " + e.toString());
                                                    }
                                                });
                                    }
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(tag, "error fetchService1 : " + e.toString());
                        }
                    });
        }else{
            visibleText.setVisibility(View.VISIBLE);

        }
    }*/

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_home:
                    Intent intent1 = new Intent(LabourerJobsActivity.this, LabourerHomeActivity.class);
                    intent1.putExtra("labourer",labourer);
                    startActivity(intent1);
                    return true;
                case R.id.bottom_navigation_history:
                    Intent intent = new Intent(LabourerJobsActivity.this, LabourerHistoryActivity.class);
                    intent.putExtra("labourer",labourer);
                    startActivity(intent);
                    return true;
                case R.id.bottom_navigation_jobs:

                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle menu_bottom_navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(LabourerJobsActivity.this,LabourerHomeActivity.class);
            intent.putExtra("labourer",labourer);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(LabourerJobsActivity.this,LabourerHistoryActivity.class);
            intent.putExtra("labourer",labourer);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_jobs) {

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(LabourerJobsActivity.this, ProfileActivity.class);
            intent.putExtra("labourer",labourer);
            intent.putExtra("type","labourer");
            Log.d(tag, "labourer : " + labourer.getAddressLine1());
            startActivity(intent);
        }  else if (id == R.id.nav_wallet) {
            Intent intent = new Intent(this, WalletActivity.class);
            Log.d("wallet",labourer.toString());
            intent.putExtra("labourer",labourer);
            intent.putExtra("type","labourer");
            Log.d(tag, "labourer : " + labourer.getAddressLine1());
            startActivity(intent);
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            sessionManager.logoutUser();
            Intent intent = new Intent(LabourerJobsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notifications) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
