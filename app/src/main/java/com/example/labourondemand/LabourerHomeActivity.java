package com.example.labourondemand;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
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

import android.view.View;
import android.widget.RelativeLayout;

import com.darwindeveloper.horizontalscrollmenulibrary.custom_views.HorizontalScrollMenuView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

public class LabourerHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, CardVIewJobs.OnFragmentInteractionListener, OnMapReadyCallback {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String tag = LabourerHomeActivity.class.getName();
    private BottomNavigationView navigation;
    private LabourerFinal labourerFinal;
    private ArrayList<Bundle> bundles = new ArrayList<>();
    private ArrayList<CardVIewJobs> cardViewJobs = new ArrayList<CardVIewJobs>();
    private ViewPagerAdapterLabourer viewPagerAdapterLabourer; //= new ViewPagerAdapter(getSupportFragmentManager());
    private WrapContentViewPager viewPager;

    private TabLayout tabsImages;

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private View mapView;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labourer_home);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.labourer_home_map);
        mapView = mapFragment.getView();

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

        viewPager = findViewById(R.id.labourer_home_vp);
        viewPagerAdapterLabourer = new ViewPagerAdapterLabourer(getSupportFragmentManager());
        CardVIewJobs c = new CardVIewJobs();

        //viewPagerAdapterLabourer.addFragment(c,"deddescs");
        //viewPagerAdapterLabourer.addFragment(new CardVIewJobs(),"cdc");
        viewPager.setAdapter(viewPagerAdapterLabourer);

        labourerFinal = (LabourerFinal) getIntent().getExtras().get("labourer");

        Log.d("labourerHome", labourerFinal.toString());

       /* slide = new Slide(this, new ArrayList<String>());
        viewPager.setAdapter(slide);*/

        tabsImages = findViewById(R.id.labourer_home_tl);
        tabsImages.setupWithViewPager(viewPager, true);
        //Log.d("MAP",context.getPackageManager().getPackageInfo("com.google.android.gms",0).versionName);

//
//        if (labourerFinal.getCurrentService() == null) {
//            Log.d("tagggg",labourerFinal.getSkill()+"!");
//            fetchServices();
//        }

        fetchFromFirebase();
//        if (labourerFinal.getName() == null) {
//            fetchFromFirebase();
//        } else {
//            fetchServices();
//        }

/*
        for (int i = 0; i < 5; i++) {
            bundles.add(new Bundle());
            bundles.get(i).putString("key", Integer.toString(i));
        }


        for (int i = 0; i < 5; i++) {
            cardViewJobs.add(new CardVIewJobs());
            cardViewJobs.get(i).setArguments(bundles.get(i));
        }


        //viewPagerAdapter.addFragment(hello1, "Hello1");
        for (int i = 0; i < 5; i++) {
            viewPagerAdapter.addFragment(cardViewJobs.get(i), "hello" + i);
        }
        //viewPagerAdapter.addFragment(hello2, "Hello2");*/

        //viewPager.setAdapter(viewPagerAdapterLabourer);

    }

    private Boolean runtime_permissions(Context context) {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            // mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
            // mMap.setOnMyLocationClickListener(onMyLocationClickListener);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {

            Log.d("results",grantResults.toString()+"!");
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onReqPermissionResult", String.valueOf(requestCode));

                //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
                if (mMap != null) {
                    mMap.setMyLocationEnabled(true);
                   // mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
                    //mMap.setOnMyLocationClickListener(onMyLocationClickListener);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.getUiSettings().setCompassEnabled(true);
                }
            } else {
                runtime_permissions(getApplicationContext());
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMaxZoomPreference(17);
        Log.d("tag", "customer Home Activity onMapReady");

        LatLng sydney = new LatLng(13.006355, 74.79641);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        if (!runtime_permissions(getApplicationContext())) {
            Log.d("service onMapReady", "yessss");
        }

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 220);

            mapView.setBackgroundColor(getResources().getColor(R.color.lightRed));

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapFragment.getMapAsync(this);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_home:

                    return true;
                case R.id.bottom_navigation_history:
                    Intent intent = new Intent(LabourerHomeActivity.this,LabourerHistoryActivity.class);
                    intent.putExtra("labourer",labourerFinal);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.bottom_navigation_jobs:
                    Intent intent1 = new Intent(LabourerHomeActivity.this,LabourerJobsActivity.class);
                    intent1.putExtra("labourer",labourerFinal);
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
        } else if (id == R.id.nav_jobs) {

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

    private void fetchServices() {

        for (String skill : labourerFinal.getSkill())
            //add isApplyable later
            firebaseFirestore.collection("services").whereEqualTo("skill", skill).whereEqualTo("status", "incoming").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                Log.d("tag", labourerFinal.getSkill() + "!" + documentSnapshot.get("skill") + "!" + documentSnapshot.getData().toString());
                                // Log.d("service fetched", documentSnapshot.getString("serviceId"));
                                ServicesFinal servicesFinal = documentSnapshot.toObject(ServicesFinal.class);
                                servicesFinal.setServiceId(documentSnapshot.getId());
                                //servicesFinal.setCustomerUID(documentSnapshot.getString("customerUID"));
                                Log.d("I don't know", "+" + servicesFinal.getCustomerUID() + "!");
                                //final ServicesFinal finalServices = servicesFinal;
                                //ServicesFinal finalServicesFinal = servicesFinal;
                                //firebaseFirestore.collection("customer").document(servicesFinal.getCustomerUID())
                                firebaseFirestore.collection("customer").document(servicesFinal.getCustomerUID()).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot1) {
                                                Log.d("doc00", documentSnapshot1.getData() + "!00");
                                                //CustomerFinal customerFinal = documentSnapshot1.toObject(CustomerFinal.class);
                                                //Log.d("cus", customerFinal.toString() + "!");
                                                Log.d("service", servicesFinal.toString() + " hi");
                                                //servicesFinal.setCustomer(customerFinal);
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("services", servicesFinal);
                                                bundle.putSerializable("labourer",labourerFinal);
                                                CardVIewJobs cv = new CardVIewJobs();
                                                cv.setArguments(bundle);
                                                viewPagerAdapterLabourer.addFragment(cv, "cc");
                                                viewPagerAdapterLabourer.notifyDataSetChanged();
                                                //viewPager.setAdapter(viewPagerAdapterLabourer);

                                                // To add code to add to viewPager
                                               /* bundles.add(new Bundle());
                                                bundles.get(j).putString("key", servicesFinal.getCustomerUID());
                                                cardViewJobs.add(new CardVIewJobs());
                                                cardViewJobs.get(j).setArguments(bundles.get(j);
                                                viewPagerAdapter.addFragment(cardViewJobs.get(j), "hello" + j);
                                                j = j +1;*/
                                                //
                                                //finalServices.setCustomer(documentSnapshot.toObject(Customer.class));
                                                //dashboardAdapter.added(finalServices);
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
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(tag, "error fetchService1 : " + e.toString());
                        }
                    });
    }

    //Later to be deleted

    private void fetchFromFirebase() {

        fetchServices();

       /* firebaseFirestore.collection("labourer").document(firebaseAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //labourer = new Labourer();
                        if (documentSnapshot.getData() != null) {
                            labourerFinal = documentSnapshot.toObject(LabourerFinal.class);
                            Log.d("Labourer info fetched", firebaseAuth.getUid() + "!");

                            if (labourerFinal.getCurrentService() == null) {
                                Log.d("tagggg", labourerFinal.getSkill() + "!");
                                fetchServices();
                            } else {

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
                });*/
    }

}

