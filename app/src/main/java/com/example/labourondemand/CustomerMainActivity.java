package com.example.labourondemand;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomerMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton carpenter, plumber, electrician, housemaid, constructionWorker, painter;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Customer customer ;
    private String Tags = CustomerMainActivity.class.getName();
    protected DrawerLayout drawerLayout;
    private Intent intent;
    private String current;
    protected  FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        frameLayout = findViewById(R.id.content_main_fl);
        carpenter = findViewById(R.id.dashboard_ib_carpenter);
        plumber = findViewById(R.id.dashboard_ib_plumber);
        painter = findViewById(R.id.dashboard_ib_painter);
        electrician = findViewById(R.id.dashboard_ib_electrician);
        housemaid = findViewById(R.id.dashboard_ib_housemaid);
        constructionWorker = findViewById(R.id.dashboard_ib_construction_worker);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        intent = new Intent(this,FormActivity.class);

        if(getIntent().getExtras() != null) {
            customer = (Customer) getIntent().getExtras().get("customer");
            current = getIntent().getExtras().getString("currentService");
        }
        if(customer == null) {
            fetchFromFirebase();
        }else if(current != null){
            Intent intent = new Intent(CustomerMainActivity.this,CustomerDashboard2Activity.class);
            intent.putExtra("customer",customer);
            startActivity(intent);
            finish();
        }else{
            intent.putExtra("customer",customer);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawerLayout = findViewById(R.id.customer_main_dl);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        carpenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("skill","carpenter");
                startActivity(intent);
            }
        });
        painter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("skill","painter");
                startActivity(intent);
            }
        });
        electrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("skill","electrician");
                startActivity(intent);
            }
        });
        plumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("skill","plumber");
                startActivity(intent);
            }
        });
        housemaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("skill","housemaid");
                startActivity(intent);
            }
        });
        constructionWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("skill","constructionWorker");
                startActivity(intent);
            }
        });


    }

    private void fetchFromFirebase() {
        firebaseFirestore.collection("customer").document(firebaseAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //labourer = new Labourer();
                        customer = documentSnapshot.toObject(Customer.class);
                        Log.d(Tags, documentSnapshot.getData().toString() + "!"+customer.getCurrentService());
                        intent.putExtra("customer",customer);
                        if(customer.getCurrentService() != null && customer.getCurrentService().length() > 0){
                            // go todifferent activity
                            Intent intent = new Intent(CustomerMainActivity.this,CustomerDashboard2Activity.class);
                            intent.putExtra("customer",customer);
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

    /*public void loadFragment(Fragment fragment, int i) {
        // load fragment
        if (i == 0) {
            removeAllFragments(getSupportFragmentManager());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.customer_main_fl, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.customer_main_fl, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }*/
/*
    private static void removeAllFragments(FragmentManager fragmentManager) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }*/

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            // Handle the camera action
        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_person) {

        } else if (id == R.id.nav_manage) {
            Intent settings = new Intent(CustomerMainActivity.this,SettingsActivity.class);
            startActivity(settings);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
