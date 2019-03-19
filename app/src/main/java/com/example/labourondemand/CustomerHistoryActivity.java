package com.example.labourondemand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class CustomerHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String tag = CustomerMainActivity.class.getName();
    private BottomNavigationView navigation;
    private CustomerFinal customer;
    private RecyclerView recyclerView;
    private Context context;
    private static final String TAG = "CustomerHistoryActivity";
    private CustomerHistoryAdapter customerHistoryAdapter;
//    private CustomerFinal customer;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_history);

        customer = (CustomerFinal) getIntent().getExtras().getSerializable("customer");

        toolbar = findViewById(R.id.customer_history_tb);
        drawerLayout = findViewById(R.id.customer_history_dl);
        navigationView = findViewById(R.id.customer_history_nv);
        navigation = findViewById(R.id.bottom_nav_view);
        recyclerView = findViewById(R.id.customer_history_rv);
        context = this;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(1);
        navigationView.setNavigationItemSelectedListener(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(0).setChecked(true);

        customer = (CustomerFinal) getIntent().getExtras().getSerializable("customer");
        Log.d("ygy",customer.toString()+"!");
        customerHistoryAdapter = new CustomerHistoryAdapter(context, new ArrayList<ServicesFinal>());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if(customer != null && customer.getHistoryServices() != null){
            recyclerView.setAdapter(new CustomerHistoryAdapter(context,customer.getHistoryServices()));
        }
        else {
            recyclerView.setAdapter(customerHistoryAdapter);
//            for(String s : customer.getServices())
            firebaseFirestore.collection("services").whereEqualTo("status","incoming").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                    ServicesFinal service = documentSnapshot.toObject(ServicesFinal.class);
                                    customerHistoryAdapter.added(service);
                                }
                            }
                        }
                    });
        }

        /*if(firebaseAuth.getUid()==null){
            Log.d(TAG,"None");
        }
        else {
            Log.d(TAG, firebaseAuth.getUid());
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("services");
        Query query = databaseReference;
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    List<Service> services = new ArrayList<>();
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Service service = snapshot.getValue(Service.class);
//                        services.add(service);
//                    }
//                    recyclerView.setAdapter(new LabourerHistoryRVAdapter(context,services));
//                    //recyclerView.setLayoutManager(new LinearLayoutManager(CustomerHistoryActivity.this));
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        DocumentReference docRef = firebaseFirestore.collection("services").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null ){
                    Log.w(TAG,"Listen Failed",e);
                    return;
                }

                List<Service> services = new ArrayList<>();
                for(QueryDocumentSnapshot doc :queryDocumentSnapshots){
                    Service service = doc.
                }
            }
        })*/
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_home:
                    Intent intent = new Intent(CustomerHistoryActivity.this,CustomerHomeActivity.class);
                    intent.putExtra("customer",customer);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.bottom_navigation_history:

                    return true;
                case R.id.bottom_navigation_jobs:
                    Intent intent1 = new Intent(CustomerHistoryActivity.this,CustomerJobsActivity.class);
                    intent1.putExtra("customer",customer);
                    startActivity(intent1);
                    finish();
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
        } else if (id == R.id.nav_profile) {
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
            Intent intent = new Intent(CustomerHistoryActivity.this, LoginActivity.class);
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
}
