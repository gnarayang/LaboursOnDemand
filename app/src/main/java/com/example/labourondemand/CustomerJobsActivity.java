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

public class CustomerJobsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        CustomerJobsFragment.OnFragmentInteractionListener {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String tag = LabourerMainActivity.class.getName();
    private BottomNavigationView navigation;
    private CustomerFinal customer;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ArrayList<ServicesFinal> currentServices;
    private SessionManager sessionManager;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_jobs);

        toolbar = findViewById(R.id.customer_jobs_tb);
        drawerLayout = findViewById(R.id.customer_jobs_dl);
        navigationView = findViewById(R.id.customer_jobs_nv);
        navigation = findViewById(R.id.bottom_nav_view);

        sessionManager = new SessionManager(getApplicationContext());
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(2);
        navigationView.setNavigationItemSelectedListener(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(2).setChecked(true);

        customer = (CustomerFinal) getIntent().getSerializableExtra("customer");
        Log.d("customer Jobs", customer.toString() + "!");

        viewPager = findViewById(R.id.customer_jobs_vp);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        currentServices = customer.getIncomingServices();

        if(currentServices == null)
        {
            customer.setIncomingServices(new ArrayList<>());
            currentServices = customer.getIncomingServices();

            firebaseFirestore.collection("services").whereEqualTo("customerUID",customer.getId())
                    .whereEqualTo("status","incoming")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                ServicesFinal servicesFinal = documentSnapshot.toObject(ServicesFinal.class);
                                servicesFinal.setServiceId(documentSnapshot.getId());
                                servicesFinal.setApplyable(documentSnapshot.getBoolean("isApplyable"));
                                servicesFinal.setPaid(documentSnapshot.getBoolean("isPaid"));
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("customer", customer);
                                bundle.putSerializable("service", servicesFinal);
                                CustomerJobsFragment customerJobsFragment = new CustomerJobsFragment();
                                customerJobsFragment.setArguments(bundle);
                                viewPagerAdapter.addFragment(customerJobsFragment, "Job");
                                viewPagerAdapter.notifyDataSetChanged();

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


        }else{
            for (int i = 0; i < currentServices.size(); i++) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("customer", customer);
                bundle.putSerializable("service", currentServices.get(i));
                CustomerJobsFragment customerJobsFragment = new CustomerJobsFragment();
                customerJobsFragment.setArguments(bundle);
                viewPagerAdapter.addFragment(customerJobsFragment, "Job" + i);
                viewPagerAdapter.notifyDataSetChanged();
            }
        }
        //should be inside a for loop through all fragments

        //viewPager.setAdapter(viewPagerAdapter);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_home:
                    Intent intent = new Intent(CustomerJobsActivity.this, CustomerHomeActivity.class);
                    intent.putExtra("customer", customer);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.bottom_navigation_history:
                    Intent intent1 = new Intent(CustomerJobsActivity.this, CustomerHistoryActivity.class);
                    intent1.putExtra("customer", customer);
                    startActivity(intent1);
                    finish();
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
            Intent intent = new Intent(CustomerJobsActivity.this,CustomerHomeActivity.class);
            intent.putExtra("customer",customer);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(CustomerJobsActivity.this,CustomerHistoryActivity.class);
            intent.putExtra("customer",customer);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_jobs) {

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("customer", customer);
            intent.putExtra("type","customer");
            Log.d(tag, "labourer : " + customer.getAddressLine1());
            startActivity(intent);
        }  else if (id == R.id.nav_wallet) {
            Intent intent = new Intent(this, WalletActivity.class);
            intent.putExtra("customer", customer);
            intent.putExtra("type","customer");
            Log.d(tag, "labourer : " + customer.getAddressLine1());
            startActivity(intent);
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            sessionManager.logoutUser();
            Intent intent = new Intent(CustomerJobsActivity.this, LoginActivity.class);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
