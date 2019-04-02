package com.example.labourondemand;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailAcceptedServiceActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AppliedServiceAmountFragment.OnFragmentInteractionListener,
        ServiceAddressFragment.OnFragmentInteractionListener,ServiceDescriptionFragment.OnFragmentInteractionListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ServicesFinal services ;
    private EditText description, addressLine1, addressLine2, landmark, city;
    private Button submitButton;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Uri filePath;
    private FloatingActionButton floatingActionButton;
    private FirebaseStorage storage;
    private Uri mainImageURI;
    private ArrayList<Uri> pictures = new ArrayList<>();
    private Customer customer;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private Slide slide;
    private Bitmap compressedImageFile;
    private EditText amount;
    private Button submit;
    private String TAG = ProfileActivity.class.getName();
    private Boolean isLabourer = false, isEditting;
    private Labourer labourer = new Labourer();
    private TextView name;
    private CircleImageView photo;
    private ProgressBar progressBar;
    private String type;
    private ViewPager viewPagerImages, viewPagerData;
    private TabLayout tabs, tabsImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_service);

        toolbar = findViewById(R.id.detail_service_tb);
        drawerLayout =  findViewById(R.id.detail_service_dl);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.detail_service_nav);
        navigationView.setNavigationItemSelectedListener(this);

        services = (ServicesFinal) getIntent().getExtras().get("service");
        viewPagerImages = findViewById(R.id.detail_service_vp_images);
        viewPagerData = findViewById(R.id.detail_service_vp_data);
        tabs = findViewById(R.id.detail_service_tl);
        tabsImages = findViewById(R.id.detail_service_tl_images);
        tabsImages.setupWithViewPager(viewPagerImages,true);

        slide = new Slide(this, services.getImages());
        viewPagerImages.setAdapter(slide);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putSerializable("services", services);

        ServiceDescriptionFragment serviceDescriptionFragment = new ServiceDescriptionFragment();
        ServiceAddressFragment serviceAddressFragment = new ServiceAddressFragment();
        AppliedServiceAmountFragment serviceAmountFragment = new AppliedServiceAmountFragment();

        serviceAddressFragment.setArguments(bundle);
        serviceAmountFragment.setArguments(bundle);
        serviceDescriptionFragment.setArguments(bundle);

        viewPagerAdapter.addFragment(serviceDescriptionFragment,"Description");
        viewPagerAdapter.addFragment(serviceAddressFragment,"Location");
        viewPagerAdapter.addFragment(serviceAmountFragment,"Amount");

        viewPagerData.setAdapter(viewPagerAdapter);
        //viewPagerData.setOffscreenPageLimit(3);
        tabs.setupWithViewPager(viewPagerData);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle menu_bottom_navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if(id == R.id.nav_logout){

            firebaseAuth.signOut();
            Intent intent = new Intent(DetailAcceptedServiceActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
