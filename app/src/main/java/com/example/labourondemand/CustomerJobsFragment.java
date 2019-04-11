package com.example.labourondemand;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerJobsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomerJobsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerJobsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CustomerJobsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerJobsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerJobsFragment newInstance(String param1, String param2) {
        CustomerJobsFragment fragment = new CustomerJobsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            customer = (CustomerFinal) bundle.getSerializable("customer");
            currentService = (ServicesFinal) bundle.getSerializable("service");
            Log.d(TAG, "onCreate: bundle recieved");
        }
    }

    private CustomerFinal customer;
    private ServicesFinal currentService;
    private RecyclerView recyclerView;
    private CustomerJobsAdapter customerJobsAdapter;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private ImageView skillPic;
    private Button done, sortPrice, sortRating;
    private TextView jobTitle, jobDescription, startTime, startDate;
    private ImageView noLabourerImage;
    private TextView noLabourertv;
    private SessionManager sessionManager;
    private TextView noResponse;

    private CollectionReference geoFirestoreRefCarpenter;
    private GeoFirestore geoFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_jobs, container, false);

        geoFirestoreRefCarpenter = FirebaseFirestore.getInstance().collection(currentService.getSkill() + "Location");
        geoFirestore = new GeoFirestore(geoFirestoreRefCarpenter);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        sessionManager = new SessionManager(view.getContext());
//        Spinner spin = (Spinner) view.findViewById(R.id.spinner);
//        spin.setOnItemSelectedListener();
//
//        //Creating the ArrayAdapter instance having the country list
//        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //Setting the ArrayAdapter data on the Spinner
//        spin.setAdapter(aa);

        /*Spinner spin = (Spinner) view.findViewById(R.id.spinner);
        spin.setOnItemSelectedListener();*/
        noResponse = view.findViewById(R.id.jobs_tv_empty_text);
        jobTitle = view.findViewById(R.id.customer_jobs_title);
        jobDescription = view.findViewById(R.id.customer_jobs_jobDescription);
        startTime = view.findViewById(R.id.customer_jobs_start_time_tv);
        startDate = view.findViewById(R.id.customer_jobs_start_time1_tv);

        jobTitle.setText(currentService.getTitle());
        jobDescription.setText(currentService.getDescription());

        if(customer.getIncomingServices().indexOf(currentService)>=0)
        {
          Log.d("incom",currentService.toString()+"!");
        } else{
            Log.d("incom -1",currentService.toString()+"!");
        }

        //show correct date and time
        StringTokenizer tokenizer = new StringTokenizer(currentService.getStartTime(), "/");
        String stTime = "", stDate = "";

        String year = "", month = "", day = "";

        if(tokenizer.hasMoreTokens())
            year = tokenizer.nextToken();
        if(tokenizer.hasMoreTokens())
            month = tokenizer.nextToken();
        if(tokenizer.hasMoreTokens())
            day = tokenizer.nextToken();

        stDate += (day + " ");

        switch(month) {
            case "1":
                stDate += "Jan";
                break;
            case "2":
                stDate += "Feb";
                break;
            case "3":
                stDate += "Mar";
                break;
            case "4":
                stDate += "Apr";
                break;
            case "5":
                stDate += "May";
                break;
            case "6":
                stDate += "June";
                break;
            case "7":
                stDate += "July";
                break;
            case "8":
                stDate += "Aug";
                break;
            case "9":
                stDate += "Sep";
                break;
            case "10":
                stDate += "Oct";
                break;
            case "11":
                stDate += "Nov";
                break;
            case "12":
                stDate += "Dec";
                break;
            default:
                stDate += "Inv";
        }


        stDate += (" " + year);


        stDate += (" " + year);

        if(tokenizer.hasMoreTokens())
            stTime += (tokenizer.nextToken() + ":");
        if(tokenizer.hasMoreTokens())
            stTime += (tokenizer.nextToken());


        startTime.setText(stTime);
        startDate.setText(stDate);

        skillPic = view.findViewById(R.id.customer_jobs_toolbox);

        //add right job type image
        if(currentService.getSkill().equals("Carpenter"))
        {
            skillPic.setImageDrawable(view.getContext().getDrawable(R.drawable.ic_carpenter_tools_colour));
        }else if(currentService.getSkill().equals("Plumber"))
        {
            skillPic.setImageDrawable(view.getContext().getDrawable(R.drawable.ic_plumber_tools));
        }else if(currentService.getSkill().equals("Electrician"))
        {
            skillPic.setImageDrawable(view.getContext().getDrawable(R.drawable.ic_electric_colour));
        }else if(currentService.getSkill().equals("Painter"))
        {
            skillPic.setImageDrawable(view.getContext().getDrawable(R.drawable.ic_paint_roller));
        }else if(currentService.getSkill().equals("Constructor"))
        {
            skillPic.setImageDrawable(view.getContext().getDrawable(R.drawable.ic_construction_colour));
        }else if(currentService.getSkill().equals("Chef"))
        {
            skillPic.setImageDrawable(view.getContext().getDrawable(R.drawable.ic_cooking_colour));
        }
       /* //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);*/

        done = view.findViewById(R.id.customer_jobs_done_btn);
        sortPrice = view.findViewById(R.id.customer_jobs_sort_btn);
        sortRating = view.findViewById(R.id.customer_jobs_sort1_btn);
        Log.d("currentService", currentService.toString() + "!");
        Log.d("customerinFragment", customer.toString() + "!");

        recyclerView = view.findViewById(R.id.customer_jobs_rv);
        if (currentService.getLabourers() == null) {
            currentService.setLabourers(new ArrayList<>());
        }

        customerJobsAdapter = new CustomerJobsAdapter(getActivity(), currentService, customer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(customerJobsAdapter);
        recyclerView.setHasFixedSize(false);

        sortPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customerJobsAdapter.getItemCount() != 0)
                    sortLabourerBasedOnPrice();
            }
        });

        sortRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customerJobsAdapter.getItemCount() != 0)
                    sortLabourerBasedOnRating();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("service in done",currentService.toString()+"!");
                Log.d("service from adapter",customerJobsAdapter.getService().toString());
                String st = "";
                int mYear, mMonth, mDay, mHour, mMinute;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                st = st+mYear+"/"+mMonth+"/"+mDay;
                st = st+"/"+mHour+"/"+mMinute;
                if(customerJobsAdapter.isDone()){
                   Log.d("isDONE",customerJobsAdapter.getService().getCustomerUID()+"!");

                    firebaseFirestore.collection("services").document(currentService.getServiceId())
                            .update("endTime",st)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    customer = sessionManager.getCustomer(customer.getId());

                                    if(customer.getIncomingServices().indexOf(currentService)>=0)
                                    {
                                        customer.getIncomingServices().remove(currentService);
                                        Log.d("incomDONE",currentService.toString()+"!");
                                    } else{
                                        Log.d("incomDONE -1",currentService.toString()+"!");
                                    }

                                    firebaseFirestore.collection("customer").document(customer.getId())
                                            .update("notPaidService",currentService.getServiceId(),
                                                    "notReviewedService",currentService.getServiceId())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    customer.setNotPaidService(currentService.getServiceId());
                                                    customer.setNotReviewedService(currentService.getServiceId());
                                                    sessionManager.saveCustomer(customer);
                                                    Intent intent = new Intent(view.getContext(),PaymentActivity.class);
                                                    intent.putExtra("services",customerJobsAdapter.getService());
                                                    intent.putExtra("customer",customer);
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("No Failure222",e.toString()+"!");

                                                }
                                            });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("No Failure111",e.toString()+"!");
                                }
                            });
                }else{
                    Toast.makeText(getContext(),"Labourer havent been assigned",Toast.LENGTH_LONG).show();
                }


            }
        });


        //for no labourer response
        noLabourerImage = view.findViewById(R.id.customer_jobs_iv_no_job);
        noLabourertv = view.findViewById(R.id.customer_jobs_tv_no_job);


        firebaseFirestore.collection("services").document(currentService.getServiceId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot snapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }


                        if (snapshot != null && snapshot.exists()) {
                            Log.d(TAG, "Current data: " + snapshot.getData());
                            Log.d("snapshotListen JOB frag",snapshot.getData()+"!");
                            ServicesFinal updatedService = snapshot.toObject(ServicesFinal.class);
                            updatedService.setServiceId(snapshot.getId());
                            updatedService.setApplyable(snapshot.getBoolean("isApplyable"));
                            updatedService.setPaid(snapshot.getBoolean("isPaid"));
                            customerJobsAdapter.clear();
                            customerJobsAdapter.setService(updatedService);

                            if (updatedService.getLabourerResponses() != null) {

                                noResponse.setVisibility(View.GONE);


                                //bad labourer image
                                noLabourerImage.setVisibility(View.GONE);
                                noLabourertv.setVisibility(View.GONE);


                                for (String s : updatedService.getLabourerResponses().keySet()) {

                                    firebaseFirestore.collection("labourer").document(s)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                    LabourerFinal labourerFinal = documentSnapshot.toObject(LabourerFinal.class);
                                                    labourerFinal.setId(documentSnapshot.getId());
                                                    labourerFinal.setPrice(updatedService.getLabourerResponses().get(labourerFinal.getId()));

                                                    if(labourerFinal.getAverageRating() == null)
                                                    {
                                                        labourerFinal.setAverageRating(0.0);
                                                    }

                                                    Log.d("labourer",labourerFinal.getAverageRating()+"1");
                                                    String skill = currentService.getSkill()+"Location";
                                                    geoFirestoreRefCarpenter.document(labourerFinal.getId())
                                                            .get()
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                    ArrayList<Double> g = (ArrayList<Double>) documentSnapshot.get("l");
                                                                    Log.d("geo",g+"!");
                                                                    GeoPoint geoPoint = new GeoPoint(g.get(0),g.get(1));
                                                                    labourerFinal.setCurrentLocation(geoPoint);
                                                                    customerJobsAdapter.addLabourer(labourerFinal);
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.d(TAG, "failure11111"+e.toString());

                                                                }
                                                            });

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "failure"+e.toString());

                                                }
                                            });
                                }
                            }
                            else{
                                noResponse.setVisibility(View.VISIBLE);
                            }
                            /*ArrayList<LabourerFinal> labourersToBeAdded = updatedService.getLabourers();
                            labourersToBeAdded.removeAll(currentService.getLabourers());
                            for(int i = 0; i < labourersToBeAdded.size(); i++) {
                                customerJobsAdapter.addLabourer(labourersToBeAdded.get(i));
                            }*/
                        } else {
                            noLabourerImage.setVisibility(View.VISIBLE);
                            noLabourertv.setVisibility(View.VISIBLE);
                            Log.d(TAG, "Current data: null");
                        }

                    }
                });

        return view;
    }

    void sortLabourerBasedOnPrice() {
        ArrayList<LabourerFinal> labourers = customerJobsAdapter.getLabourers();
        currentService = customerJobsAdapter.getService();

        for(LabourerFinal labourer : labourers)
        {
            Log.d("lab before price sort",labourer.getPrice()+"!");
        }

        for(int i = 0; i < labourers.size(); i++) {
            int min = i;
            for(int j = i+1; j < labourers.size(); j++) {
                /*if(currentService.getLabourerResponses().get(labourers.get(i).getId()) < currentService.getLabourerResponses().get(labourers.get(i).getId())) {
                    min = i;
                }*/
                if(labourers.get(j).getPrice() < labourers.get(min).getPrice()) {
                    min = j;
                }
            }
            if(i != min) {
                Log.d("not equal",min+"###"+i);
                LabourerFinal tempLabourer = labourers.get(min);
                labourers.set(min, labourers.get(i));
                labourers.set(i, tempLabourer);
            }

            //Long temp = currentService.getLabourerResponses().
        }

        for(LabourerFinal labourer : labourers)
        {
            Log.d("labouer a price sort",labourer.getPrice()+"!");
        }

        customerJobsAdapter.setLabourers(labourers);
    }

    void sortLabourerBasedOnRating() {
        ArrayList<LabourerFinal> labourers = customerJobsAdapter.getLabourers();
        currentService = customerJobsAdapter.getService();

        for(LabourerFinal labourer : labourers)
        {
            Log.d("labouerbefore sort",labourer.getAverageRating()+"!");
        }
        for(int i = 0; i < labourers.size(); i++) {
            int max = i;
            for(int j = i+1; j < labourers.size(); j++) {
                if(labourers.get(j).getAverageRating() > labourers.get(max).getAverageRating()) {
                    max = j;
                }
                Log.d("max",max+"###");
            }
            if(i != max) {
                Log.d("not equal",max+"###"+i);
                LabourerFinal tempLabourer = labourers.get(max);
                labourers.set(max, labourers.get(i));
                labourers.set(i, tempLabourer);
            }
        }
        for(LabourerFinal labourer : labourers)
        {
            Log.d("labouer",labourer.getAverageRating()+"!");
        }
        customerJobsAdapter.setLabourers(labourers);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
