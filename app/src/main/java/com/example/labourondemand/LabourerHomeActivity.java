package com.example.labourondemand;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LabourerHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CardVIewJobs.OnFragmentInteractionListener {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String tag = LabourerHomeActivity.class.getName();
    private BottomNavigationView navigation;
    private LabourerFinal labourerFinal;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labourer_home);

        toolbar = findViewById(R.id.labourer_home_tb);
        drawerLayout = findViewById(R.id.labourer_home_dl);
        navigationView = findViewById(R.id.labourer_home_nv);
        navigation = findViewById(R.id.bottom_nav_view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(2);
        navigationView.setNavigationItemSelectedListener(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ViewPager viewPager = findViewById(R.id.labourer_home_vp);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        ArrayList<Bundle> bundles = new ArrayList<>();

        Intent intent;

        labourerFinal = (LabourerFinal) getIntent().getExtras().get("labourer")

        for(int i = 0; i < 5; i++) {
            bundles.add(new Bundle());
            bundles.get(i).putString("key",  Integer.toString(i));
        }

        ArrayList<CardVIewJobs> cardViewJobs = new ArrayList<CardVIewJobs>();


        for(int i = 0; i < 5; i++) {
            cardViewJobs.add(new CardVIewJobs());
            cardViewJobs.get(i).setArguments(bundles.get(i));
        }


        //viewPagerAdapter.addFragment(hello1, "Hello1");
        for(int i = 0; i < 5; i++) {
            viewPagerAdapter.addFragment(cardViewJobs.get(i), "hello" + i);
        }
        //viewPagerAdapter.addFragment(hello2, "Hello2");

        viewPager.setAdapter(viewPagerAdapter);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_home:

                    return true;
                case R.id.bottom_navigation_history:

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

        } else if (id == R.id.nav_history) {
            //Toast.makeText(this,"History yet to be Developed",)
            Intent intent = new Intent(this, PreviousActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_jobs) {

        }else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            /*Bundle bundle = new Bundle();
            bundle.putParcelable("labourer",labourer);*//*
            intent.putExtra("user", labourer);
            intent.putExtra("type","labourer");
            Log.d(tag, "labourer : " + labourer.getAddressLine1());*/
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            //Intent settings = new Intent(LabourerMainActivity.this,SettingsActivity.class);
            //startActivity(settings);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {

            firebaseAuth.signOut();
            Intent intent = new Intent(LabourerHomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_wallet) {
            /*Intent intent = new Intent(CustomerHistoryActivity.this, NAME.class);
            startActivity(intent);*/
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void fetchFromFirebase() {

        firebaseFirestore.collection("labourer").document(firebaseAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //labourer = new Labourer();
                        if (documentSnapshot.getData() != null) {
                            labourerFinal = documentSnapshot.toObject(LabourerFinal.class);
                            Log.d(tag, documentSnapshot.getData().toString() + "!");

                            if (labourerFinal.getCurrentService() == null) {
                                Log.d("tagggg",labourerFinal.getSkill()+"!");
                                fetchServices();
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

    private void fetchServices() {

        firebaseFirestore.collection("services").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            Services services = new Services() ;
                            Log.d("tag",labourerFinal.getSkill()+"!"+documentSnapshot.get("skill")+"!"+documentSnapshot.getData().toString());
                            if(documentSnapshot.getString("skill").equals(labourerFinal.getSkill())){
                                services = documentSnapshot.toObject(Services.class);
                                services.setServiceID(documentSnapshot.getId());
                                final Services finalServices = services;
                                firebaseFirestore.collection("customer").document(services.getCustomerUID()).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                // To add code to add to viewPager


                                                //
                                                //finalServices.setCustomer(documentSnapshot.toObject(Customer.class));
                                                //dashboardAdapter.added(finalServices);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(tag,"error fetchService2 : "+e.toString());
                                            }
                                        });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(tag,"error fetchService1 : "+e.toString());
                    }
                });
    }
}

