package com.example.labourondemand;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.darwindeveloper.horizontalscrollmenulibrary.custom_views.HorizontalScrollMenuView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.GeoQueryEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomerHomeActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private HorizontalScrollMenuView horizontalScrollMenuView;
    private SupportMapFragment mapFragment;
    private HashMap<String, Integer> icons = new HashMap<String, Integer>();
    private ArrayList<String> skills = new ArrayList<>();
    private HorizontalScrollViewAdapter horizontalScrollViewAdapter;
    private RecyclerView recyclerView;
    private Context context;
    private LocationManager locationManager;
    private static final long MIN_TIME = 10;
    private static final float MIN_DISTANCE = 100;
    private Query geoFirestoreRef;
    private GeoFirestore geoFirestore;
    private Location lastLocation;
    private View mapView;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String tag = LabourerMainActivity.class.getName();
    private BottomNavigationView navigation;
    private SessionManager session;
    private CustomerFinal customer ;
    private Button bookButton;
    private GeoPoint destination, geoPoint;
    private FirebaseInstanceId firebaseInstanceId;

    private static final String TAG = "CustomerHomeActivity";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Bundle bundle = new Bundle();
        Log.d("tag", "onSaveInstanceState");

        outState.putString("skill", horizontalScrollViewAdapter.getSkill());
        //outState.putSerializable("labourersLocation", horizontalScrollViewAdapter.getLabourersLocation());
        //customer.setLabourersLocation(horizontalScrollViewAdapter.getLabourersLocation());
        outState.putSerializable("customer", customer);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.d("tag", "customer Home Activity onRestoreInstanceState");

        /*Location loc1 = new Location("");
        loc1.setLatitude(customer.getDestinationLatitude());
        loc1.setLongitude(customer.getDestinationLongitude());
        customer = savedInstanceState.getParcelable("customer");*/
        //Log.d("customer",customer.getDestination()+"!");
        /*horizontalScrollViewAdapter = new HorizontalScrollViewAdapter(context, recyclerView, mMap,
                savedInstanceState.getString("skill"), loc1, customer.getLabourersLocation());*/

    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        Log.d("tag", "customer Home Activity onCreate");

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseMessaging.getInstance().subscribeToTopic("sample");

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("not Successful token", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        firebaseFirestore.collection("customer").document(firebaseAuth.getUid()).update("token",token)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TOKEN","SUCCESS");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TOKEN Failure",e.toString());
                                    }
                                });
                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("dcs", token);
                        Toast.makeText(CustomerHomeActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TOKEN Failure111",e.toString());

                    }
                });

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapView = mapFragment.getView();

        toolbar = findViewById(R.id.customer_home_tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawerLayout = findViewById(R.id.customer_home_dl);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        //toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        navigationView = findViewById(R.id.customer_home_nv);
        navigation = findViewById(R.id.bottom_nav_view);

        bookButton = findViewById(R.id.customer_home_btn_book);
        session = new SessionManager(getApplicationContext());
        context = getApplicationContext();
        recyclerView = findViewById(R.id.customer_home_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(false);

        session = new SessionManager(context);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (savedInstanceState != null) {
            Log.d("savedInstance", "customer Home Activity onSavedInstanceState");

           /* Location loc1 = new Location("");
            loc1.setLatitude(customer.getDestinationLatitude());
            loc1.setLongitude(customer.getDestinationLongitude());
            customer = savedInstanceState.getParcelable("customer");*/
            /*horizontalScrollViewAdapter = new HorizontalScrollViewAdapter(context, recyclerView, mMap,
                    savedInstanceState.getString("skill"), loc1, customer.getLabourersLocation());*/
        }else if (getIntent().getExtras() != null) {
            Log.d("tag", "customer Home Activity getExtras "+ getIntent().getExtras().toString());

            customer = (CustomerFinal) getIntent().getExtras().get("customer");
            Log.d("customer in CustomerHome",customer.getName()+customer.getId());
            //customer = new CustomerFinal();

        }else {
            Log.d("tag", "customer Home Activity null is customer");
            customer = new CustomerFinal();

        }
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //horizontalScrollViewAdapter.removeListener();
                customer.setDestinationLatitude(geoPoint.getLatitude());
                customer.setDestinationLongitude(geoPoint.getLongitude());
                Intent intent = new Intent(CustomerHomeActivity.this, Form2Activity.class);
                intent.putExtra("customer",customer);
                intent.putExtra("skill", horizontalScrollViewAdapter.getSkill());
                startActivity(intent);

            }
        });
       /* SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("bool", true); // Storing boolean - true/false
        editor.putString("string", "string value"); // Storing string
        *//*editor.putInt("key_name", 0); // Storing integer
        editor.putFloat("key_name", 2.3f); // Storing float
        editor.putLong("key_name", 23L); // Storing long*//*

        editor.commit(); // commit changes

        Log.d("shared", String.valueOf(pref.getLong("key_name",23151L)));*/

        /*ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_notifications_black_24dp);
        actionbar.setDisplayHomeAsUpEnabled(true);*/
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/


        navigationView.setCheckedItem(1);
        navigationView.setNavigationItemSelectedListener(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true);

        /*FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);*/


        /*if (!runtime_permissions(context)) {
            Log.d("service", "yes");
            //Intent i = new Intent(getApplicationContext(), MyService.class);
            //startService(i);
        }*/


        /*geoFirestoreRef = FirebaseFirestore.getInstance().collection("PlumberLocation");
        geoFirestore = new GeoFirestore((CollectionReference) geoFirestoreRef);*/

        /*geoFirestore.setLocation("plumber1", new GeoPoint(13.006351609303422,74.79635816792011));
        geoFirestore.setLocation("plumber2", new GeoPoint(13.006361609303422, 74.79645816792011), new GeoFirestore.CompletionListener() {
            @Override
            public void onComplete(Exception exception) {
                if (exception == null){
                    System.out.println("Location saved on server successfully!");
                }else{
                    Log.d("tag",exception.toString());
                }
            }
        });*/

      /*  geoFirestore.setLocation("plumber1", new GeoPoint(37.785388, -122.4056973), new GeoFirestore.CompletionListener() {
            @Override
            public void onComplete(Exception exception) {
                if (exception == null){
                    System.out.println("Location saved on server successfully!");
                }else{
                    Log.d("tag",exception.toString());
                }
            }
        });


        geoFirestore.getLocation("que8B9fxxjcvbC81h32VRjeBSUW2", new GeoFirestore.LocationCallback() {
            @Override
            public void onComplete(GeoPoint location, Exception exception) {
                if (exception == null && location != null){
                    System.out.println(String.format("The location for this document is [%f,%f]", location.getLatitude(), location.getLongitude()));
                }
            }
        });*/

       /* GeoQuery geoQuery = geoFirestore.queryAtLocation(new GeoPoint(13.004787, 74.794481), 20);
        Log.d("tag","geoquery"+geoQuery.getCenter()+"!"+geoQuery.getRadius());

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String documentID, GeoPoint location) {
                System.out.println(String.format("Document %s entered the search area at [%f,%f]", documentID, location.getLatitude(), location.getLongitude()));
            }

            @Override
            public void onKeyExited(String documentID) {
                System.out.println(String.format("Document %s is no longer in the search area", documentID));
            }

            @Override
            public void onKeyMoved(String documentID, GeoPoint location) {
                System.out.println(String.format("Document %s moved within the search area to [%f,%f]", documentID, location.getLatitude(), location.getLongitude()));
            }

            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(Exception exception) {
                System.err.println("There was an error with this query: " + exception.getLocalizedMessage());
            }
        });*/

       /* icons.put("carpenter", R.drawable.carpenter);
        icons.put("plumber", R.drawable.plumber);
        icons.put("electrician", R.drawable.electrician);
        icons.put("worker", R.drawable.worker);
        icons.put("painter", R.drawable.painter);
        *//*icons.put("csd", R.drawable.worker);
        icons.put("csda", R.drawable.worker);
        icons.put("csdb", R.drawable.worker);*//*
        skills.add("carpenter");
        skills.add("plumber");
        skills.add("electrician");
        skills.add("worker");
        skills.add("painter");
        skills.add("chef");
        *//*skills.add("csd");
        skills.add("csda");
        skills.add("csdb");*/



        /*horizontalScrollMenuView = (HorizontalScrollMenuView) findViewById(R.id.customer_home_hsmv);
        initMenu();*/

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_home:
                    return true;
                case R.id.bottom_navigation_history:
                    Intent intent = new Intent(CustomerHomeActivity.this,CustomerHistoryActivity.class);
                    intent.putExtra("customer",customer);
                    startActivity(intent);
                    finish();
                    return true;
                case R.id.bottom_navigation_jobs:
                    Intent intent1 = new Intent(CustomerHomeActivity.this,CustomerJobsActivity.class);
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
            Intent intent = new Intent(CustomerHomeActivity.this,CustomerHistoryActivity.class);
            intent.putExtra("customer",customer);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_jobs) {
            Intent intent = new Intent(CustomerHomeActivity.this,CustomerJobsActivity.class);
            intent.putExtra("customer",customer);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("customer", customer);
            intent.putExtra("type","customer");
            Log.d(tag, "labourer : " + customer.getAddressLine1());
            startActivity(intent);
        }  else if (id == R.id.nav_wallet) {
            Intent intent = new Intent(this, WalletActivity.class);
            Log.d("cbuidbcidbysi",customer.toString());
            intent.putExtra("customer", customer);
            intent.putExtra("type","customer");
            Log.d(tag, "labourer : " + customer.getAddressLine1());
            startActivity(intent);
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            session.logoutUser();
            Intent intent = new Intent(CustomerHomeActivity.this, LoginActivity.class);
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
    protected void onDestroy() {
        super.onDestroy();
        Log.d("tag", "destroy");
    }

    private Boolean runtime_permissions(Context context) {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
            mMap.setOnMyLocationClickListener(onMyLocationClickListener);
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

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
                //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
                if (mMap != null) {
                    mMap.setMyLocationEnabled(true);
                    mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
                    mMap.setOnMyLocationClickListener(onMyLocationClickListener);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.getUiSettings().setCompassEnabled(true);
                }
            } else {
                runtime_permissions(context);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapFragment.getMapAsync(this);
    }

    private void initMenu() {

       /* horizontalScrollMenuView.addItem("blah",R.drawable.ic_attach_money_black_24dp);
        horizontalScrollMenuView.addItem("cscsdcds",R.drawable.ic_home_black_24dp);
        horizontalScrollMenuView.addItem("ccsdds",R.drawable.ic_cake_black_24dp);
        horizontalScrollMenuView.addItem("cscsdcds",R.drawable.ic_home_black_24dp);
        horizontalScrollMenuView.addItem("ccsdds",R.drawable.ic_cake_black_24dp);
        horizontalScrollMenuView.addItem("cscsdcds",R.drawable.ic_home_black_24dp);
        horizontalScrollMenuView.addItem("ccsdds",R.drawable.ic_cake_black_24dp);

        horizontalScrollMenuView.showItems();

        horizontalScrollMenuView.setOnHSMenuClickListener(new HorizontalScrollMenuView.OnHSMenuClickListener() {
            @Override
            public void onHSMClick(MenuItem menuItem, int position) {

            }
        });*/
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMaxZoomPreference(17);
        Log.d("tag", "customer Home Activity onMapReady");

        if (!runtime_permissions(context)) {
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

        LatLng midLatLng = mMap.getCameraPosition().target;
        Location loc1 = new Location("");
        loc1.setLatitude(midLatLng.latitude);
        loc1.setLongitude(midLatLng.longitude);

        if (horizontalScrollViewAdapter == null) {
            Log.d("null","horiazontal");
            Log.d("onMap",midLatLng.toString());
            horizontalScrollViewAdapter = new HorizontalScrollViewAdapter(context, recyclerView, mMap, "Carpenter", loc1, new HashMap<String, Marker>());
        } else {
            customer = session.getCustomer(customer.getId());
            /*Log.d("cus",customer.toString());*/
            Log.d("NOTnull","horiazontal"+(Boolean)(horizontalScrollViewAdapter.getMap() ==  null));

        }

        recyclerView.setAdapter(horizontalScrollViewAdapter);

       /* if (customer.getDestination() != null) {

        }*/

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng midLatLng = mMap.getCameraPosition().target;
                Log.d("idle", midLatLng + "!");
                Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_show);
                Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_hide);

                navigation.setVisibility(View.VISIBLE);
                //navigation.startAnimation(slideUp);

                toolbar.setVisibility(View.VISIBLE);
                //toolbar.startAnimation(slideDown);

                geoPoint = new GeoPoint(midLatLng.latitude, midLatLng.longitude);

                if (destination == null) {
                    destination = geoPoint;
                    Location loc1 = new Location("");
                    loc1.setLatitude(destination.getLatitude());
                    loc1.setLongitude(destination.getLongitude());
                    //horizontalScrollViewAdapter.locationChanged(loc1);
                } else {
                    Location loc1 = new Location("");
                    loc1.setLatitude(geoPoint.getLatitude());
                    loc1.setLongitude(geoPoint.getLongitude());

                    Location loc2 = new Location("");
                    loc2.setLatitude(destination.getLatitude());
                    loc2.setLongitude(destination.getLongitude());

                    float distanceInMeters = loc1.distanceTo(loc2);
                    if (distanceInMeters >= 1000) {
                        Log.d("idleDistance", midLatLng + "!" + distanceInMeters);
                        destination = geoPoint;
                        horizontalScrollViewAdapter.locationChanged(loc2);
                    }
                }
            }
        });

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                Log.d("start moving", i + "!");

                Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_show);
                Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_hide);

//                navigation.startAnimation(slideDown);
//                navigation.setVisibility(View.GONE);

                //toolbar.startAnimation(slideUp);
               /* toolbar.animate()
                        .translationY(0)
                        .alpha(1).setDuration(1000)
                        .setInterpolator(new DecelerateInterpolator());*/
                //toolbar.setVisibility(View.GONE);
            }
        });
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                    Log.d("nButtonClickListener", latLng + "!");
                    //mMap.setMaxZoomPreference(20);
                    mMap.animateCamera(cameraUpdate);

                    return false;
                }
            };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                    Log.d("onLocationClickListener", latLng + "!");
                    //mMap.setMinZoomPreference(10);
                    mMap.animateCamera(cameraUpdate);
                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(location.getLatitude(),
                            location.getLongitude()));

                    circleOptions.radius(200);
                    circleOptions.fillColor(Color.RED);
                    circleOptions.strokeWidth(6);

                    mMap.addCircle(circleOptions);
                }
            };


    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
        //mMap.addMarker(new MarkerOptions().position(latLng).title("User"));
        //mMap.animateCamera(cameraUpdate);
        //locationManager.removeUpdates(this);
        if (lastLocation != null) {

            Toast.makeText(getApplicationContext(), String.valueOf(location.distanceTo(lastLocation)), Toast.LENGTH_LONG).show();
            Log.d("distance", location.distanceTo(lastLocation) + "!");
        } else {
            mMap.animateCamera(cameraUpdate);
        }

        lastLocation = location;
        Log.d("location", location + "!");

        //horizontalScrollViewAdapter.locationChanged(lastLocation);

        /*String skill = horizontalScrollViewAdapter.getSkill();
        if(skill == null){
            skill = "carpenterLocation";
        }
        geoFirestoreRef = FirebaseFirestore.getInstance().collection(skill);
        geoFirestore = new GeoFirestore(geoFirestoreRef);*/


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("onStatus Changed", "provider:" + provider + " status:" + status + " extras:" + extras.toString());
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("onProviderEnabled", "provider:" + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("onProviderDisabled", "provider:" + provider);
    }
}
