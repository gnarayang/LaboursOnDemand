package com.example.labourondemand;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.labourondemand.notifications.Api;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.okhttp.ResponseBody;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Form2Activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ServicesFinal servicesFinal;
    private EditText description, addressLine1, addressLine2, landmark, city, title, numberOfLabourers, dateTime;
    private Button submitButton;
    private ViewPager viewPager;
    private Uri filePath;
    private FloatingActionButton floatingActionButton;
    private FirebaseStorage storage;
    private Uri mainImageURI;
    private ArrayList<Uri> pictures = new ArrayList<>();
    private CustomerFinal customer;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private Slide slide;
    private Bitmap compressedImageFile;
    private EditText amount;
    private Button submit;
    private String TAG = FormActivity.class.getName();
    private String st = "";
    private SessionManager session;
    private TabLayout tabsImages;
    private ImageView choose, skillPic;
    private TextView skillText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form2);

        servicesFinal = new ServicesFinal();
        toolbar = findViewById(R.id.form2_tl);
        toolbar.setTitle("Fill Form");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();

                }
            });

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        session = new SessionManager(getApplicationContext());

        progressBar = findViewById(R.id.form2_pb);

        skillPic = findViewById(R.id.form2_skill_iv);
        skillText = findViewById(R.id.form2_skill_tv);
        choose = findViewById(R.id.form2_choose_iv);
        viewPager = findViewById(R.id.activity_form2_vp);
        floatingActionButton = findViewById(R.id.activity_form2_fab);
        description = findViewById(R.id.activity_form2_et_description);
        submit = findViewById(R.id.activity_form2_btn_submit);
        amount = findViewById(R.id.activity_form2_et_amount);
        title = findViewById(R.id.activity_form2_et_title);
        numberOfLabourers = findViewById(R.id.activity_form2_et_num);
        dateTime = findViewById(R.id.activity_form2_et_time);
        tabsImages = findViewById(R.id.form2_tl_images);
        tabsImages.setupWithViewPager(viewPager,true);

        servicesFinal.setSkill(getIntent().getExtras().getString("skill"));

        if(servicesFinal.getSkill().equals("Carpenter"))
        {
            skillText.setText("Carpenter");
            skillPic.setImageDrawable(getDrawable(R.drawable.ic_carpenter_tools_colour));
        }else if(servicesFinal.getSkill().equals("Plumber"))
        {
            skillText.setText("Plumber");
            skillPic.setImageDrawable(getDrawable(R.drawable.ic_plumber_tools));
        }else if(servicesFinal.getSkill().equals("Electrician"))
        {
            skillText.setText("Electrician");
            skillPic.setImageDrawable(getDrawable(R.drawable.ic_electric_colour));
        }else if(servicesFinal.getSkill().equals("Painter"))
        {
            skillText.setText("Painter");
            skillPic.setImageDrawable(getDrawable(R.drawable.ic_paint_roller));
        }else if(servicesFinal.getSkill().equals("Constructor"))
        {
            skillText.setText("Constructor");
            skillPic.setImageDrawable(getDrawable(R.drawable.ic_construction_colour));
        }else if(servicesFinal.getSkill().equals("Chef"))
        {
            skillText.setText("Chef");
            skillPic.setImageDrawable(getDrawable(R.drawable.ic_cooking_colour));
        }

        customer = (CustomerFinal) getIntent().getExtras().getSerializable("customer");
        customer.setId(firebaseAuth.getUid());

        Log.d("scsdcs","scd"+customer.getId());

        dateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateTime(v);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        slide = new Slide(this, new ArrayList<String>());
        viewPager.setAdapter(slide);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                save_description(v);
            }
        });
    }

    private void chooseImage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(Form2Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //Toast.makeText(SetupActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(Form2Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {

                BringImagePicker();

            }
        } else {

            BringImagePicker();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            BringImagePicker();
        }
    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(400, 200)
                .start(Form2Activity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                pictures.add(mainImageURI);
                slide.added(mainImageURI.toString());
                choose.setVisibility(View.GONE);
                viewPager.setCurrentItem(pictures.size() - 1);
                //viewPager.setAdapter(new Slide(getApplicationContext(), pictures));
                //photo.setImageURI(mainImageURI);

                //isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }

    public void save_description(View view) {

        String problem_description = description.getText().toString();
        String amonut_string = amount.getText().toString();
        String title_string = title.getText().toString();
        String number_string = numberOfLabourers.getText().toString();
        String time = dateTime.getText().toString();

        if (problem_description.length() == 0 || amonut_string.length() == 0 || title_string.length() == 0 || number_string.length() == 0 || time.length() == 0) {
            if (problem_description.length() == 0) {
                description.setError("Please enter a description before submitting");
            }
            if (amonut_string.length() == 0) {
                amount.setError("Please enter amount before submitting");
            }
            if (title_string.length() == 0) {
                title.setError("Please enter title before submitting");
            }
            if (number_string.length() == 0) {
                numberOfLabourers.setError("Please enter number before submitting");
            }
            if (time.length() == 0) {
                dateTime.setError("Please enter time before submitting");
            }
        } else {

            servicesFinal.setCustomerUID(firebaseAuth.getUid());
            servicesFinal.setDescription(problem_description);
            servicesFinal.setNumOfLabourers(Long.valueOf(number_string));
            servicesFinal.setCustomerAmount(Long.valueOf(amonut_string));
            servicesFinal.setServiceId(customer.getId() + "+" + String.valueOf(System.currentTimeMillis()));
            servicesFinal.setSkill(getIntent().getExtras().getString("skill"));
            servicesFinal.setDestinationLatitude(customer.getDestinationLatitude());
            servicesFinal.setDestinationLongitude(customer.getDestinationLongitude());
            servicesFinal.setStatus("incoming");
            servicesFinal.setTitle(title_string);
            servicesFinal.setStartTime(st);
            servicesFinal.setApplyable(true);
            servicesFinal.setPaid(false);
            sendToFirebase();
        }
    }

    public void selectDateTime(View view) {
        st = "";
        int mYear, mMonth, mDay, mHour, mMinute;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        st = st+year+"/"+monthOfYear+1+"/"+dayOfMonth;

                        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        dateTime.setText(st+" "+hourOfDay+":"+minute);
                                        st = st+"/"+hourOfDay+"/"+minute;
                                        Log.d("date time", st+"!");
                                        //txtTime.setText(hourOfDay + ":" + minute);
                                    }
                                }, mHour, mMinute, false);

                        timePickerDialog.show();

                        //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();


    }

    private void sendToFirebase() {
        Log.d("tag", "sending to firebase");
        final ArrayList<String> uris = new ArrayList<>();
        final HashMap<String, Object> map = new HashMap<>();

        //map.put("labourUID", "");
        map.put("customerUID", firebaseAuth.getUid());
        map.put("isApplyable",true);
        map.put("isPaid",false);
        map.put("customerAmount", servicesFinal.getCustomerAmount());
        map.put("description", servicesFinal.getDescription());
        map.put("status","incoming");
        // map.put("feedback","");
        map.put("skill", servicesFinal.getSkill());
        map.put("title",servicesFinal.getTitle());
        map.put("destinationLongitude",customer.getDestinationLongitude());
        map.put("destinationLatitude",customer.getDestinationLatitude());
        map.put("startTime",st);
        map.put("endTime",null);
        map.put("numOfLabourers",servicesFinal.getNumOfLabourers());
        //map.put("images", pictures);
        //map.put("labourResponses", new HashMap<String, Long>());


        if (pictures.size() > 0) {
            for (final Uri uri : pictures) {
                File newImageFile = new File(uri.getPath());
                try {

                    compressedImageFile = new Compressor(Form2Activity.this)
                            .setMaxHeight(200)
                            .setMaxWidth(400)
                            .setQuality(50)
                            .compressToBitmap(newImageFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] thumbData = baos.toByteArray();

                final UploadTask image_path = storageReference.child("services").child(pictures.indexOf(uri) + servicesFinal.getServiceId() + ".jpg").putBytes(thumbData);

                image_path.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Log.d("inside file", taskSnapshot.toString());
                        storageReference.child("services").child(pictures.indexOf(uri) + servicesFinal.getServiceId() + ".jpg").getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        uris.add(uri.toString());
                                        Log.d("uri", uri.toString() + "!" + uris.size());

                                        if (uris.size() == pictures.size()) {
                                            HashMap<String, Object> images = new HashMap<>();
                                            map.put("images", uris);
                                            servicesFinal.setImages(uris);
                                            Log.d("tag", "before uploading service to database");
                                            firebaseFirestore.collection("services").document(servicesFinal.getServiceId()).set(map)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            //onBackPressed();
                                                            /*Intent intent = new Intent(Form2Activity.this, CustomerHomeActivity.class);
                                                            intent.putExtra("customer", customer);
                                                            startActivity(intent);
                                                            finish();*/

                                                            /*HashMap<String, String> m = new HashMap<>();
                                                            m.put("currentService", FieldValue.arrayUnion(services.getServiceID()));*/
                                                            firebaseFirestore.collection("customer").document(customer.getId()).
                                                                    update("services",FieldValue.arrayUnion(servicesFinal.getServiceId()))
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                            customer.getServices().add(servicesFinal.getServiceId());
                                                                            if(customer.getIncomingServices() == null){
                                                                                customer.setIncomingServices(new ArrayList<>());
                                                                            }
                                                                            customer.getIncomingServices().add(servicesFinal);
                                                                            session.saveServices(servicesFinal);
                                                                            session.saveCustomer(customer);
                                                                            firebaseFirestore.collection("labourer").whereArrayContains("skill",servicesFinal.getSkill())
                                                                                    .get()
                                                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                                        @Override
                                                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                                            int i =0;
                                                                                            for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                                                                                i++;
                                                                                                String token = documentSnapshot.getString("token");
                                                                                                Retrofit retrofit = new Retrofit.Builder()
                                                                                                        .baseUrl("https://labourondemand-8e636.firebaseapp.com/api/")
                                                                                                        .addConverterFactory(GsonConverterFactory.create())
                                                                                                        .build();

                                                                                                Api api = retrofit.create(Api.class);
                                                                                                String title = "A job is available";
                                                                                                String body = "Job: "+servicesFinal.getTitle();
                                                                                                Call<ResponseBody> call = api.sendNotification(token,title,body);
                                                                                                if(i == queryDocumentSnapshots.size()){
                                                                                                    onBackPressed();
                                                                                                }
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
                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Log.d(TAG,"Could not get toke!");
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
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });
                                            /*firebaseFirestore.collection("services").document(services.getServiceID()).set(images, SetOptions.merge())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("done", uris.toString());
                                                            ///////////
                                                            firebaseFirestore.collection("services").document(services.getServiceID()).set(map, SetOptions.merge())
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                            HashMap<String, String> m = new HashMap<>();
                                                                            m.put("currentService", services.getServiceID());
                                                                            firebaseFirestore.collection("customer").document(firebaseAuth.getUid()).set(m, SetOptions.merge())
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            Log.d("success", "!");

                                                                                            //TODO: directly go to DashboardActivity
                                                                                            Intent intent = new Intent(Form2Activity.this, CustomerMainActivity.class);
                                                                                            intent.putExtra("currentService", services.getServiceID());
                                                                                            intent.putExtra("services", services);
                                                                                            intent.putExtra("customer", customer);
                                                                                            startActivity(intent);
                                                                                            finish();
                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Log.d("error", e.toString());
                                                                                        }
                                                                                    });


                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.d(TAG, "error : " + e.toString());
                                                                        }
                                                                    });

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d("failure 1", e.toString());
                                                        }
                                                    });*/
                                        }

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("failure 2", e.toString());

                                        Toast.makeText(Form2Activity.this, "(IMAGE Error uri) : " + e.toString(), Toast.LENGTH_LONG).show();
                                    }
                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("failure 3", e.toString());
                        Toast.makeText(Form2Activity.this, "(IMAGE Error) : " + e, Toast.LENGTH_LONG).show();
                    }
                });

            }

        } else {
            map.put("images", new ArrayList<String>());
        }


    }
}
