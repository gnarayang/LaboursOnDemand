package com.example.labourondemand;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.example.labourondemand.R.drawable.ic_carpenter_tools_colour;
import static com.example.labourondemand.R.drawable.ic_plumber_tools;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CardVIewJobs.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CardVIewJobs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardVIewJobs extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private CustomerFinal customerFinal;
    private LabourerFinal labourerFinal;
    private long distance;
    public CardVIewJobs() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardVIewJobs.
     */
    // TODO: Rename and change types and number of parameters
    public static CardVIewJobs newInstance(String param1, String param2) {
        CardVIewJobs fragment = new CardVIewJobs();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    String display;
    private ServicesFinal servicesFinal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            Log.d(TAG, "onCreate: having fun");
            // display = bundle.getString("key", "Error");
            servicesFinal = (ServicesFinal) bundle.get("services");
            labourerFinal = (LabourerFinal) bundle.get("labourer");
            distance = (long) bundle.getDouble("distance");
            Log.d(TAG, "onCreate: servicesFinal: " + servicesFinal.toString()+"!");
        }

    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_card_view_jobs, container, false);

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            display = bundle.getString("key","Error");
        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

//        Bundle bundle = this.getArguments();
//        if(bundle != null) {
//            servicesFinal = (ServicesFinal) bundle.getSerializable("services");
//            //display = bundle.getString("key","Error");
//        }
        //if(servicesFinal != null)
        // {
        Log.d("service passed",  servicesFinal.toString()+"!");
        //}
        TextView amount = view.findViewById(R.id.fragment_card_view_jobs_tv_customer_amount);
        amount.setText(servicesFinal.getCustomerAmount().toString());
        customerFinal = (CustomerFinal)fetchCustomer(servicesFinal.getCustomerUID());
        TextView dis = view.findViewById(R.id.fragment_card_view_jobs_tv_distance);
        distance = Math.round(distance);
        dis.setText(String.valueOf(distance) + " kms");
        //Log.d("NULL?",servicesFinal.getCustomerUID());
        Log.d("Service info", servicesFinal.getCustomerUID());

        //servicesFinal.setCustomer(fetchCustomer(servicesFinal.getCustomerUID()));


//        Log.d("Customer in cardView",customerFinal.toString());
//        TextView name = view.findViewById(R.id.fragment_card_view_jobs_tv_name);
//        name.setText(customerFinal.getName());
//        TextView title = view.findViewById(R.id.fragment_card_view_jobs_tv_title);
//        title.setText(servicesFinal.getTitle());

        View cardVIewJobs = view.findViewById(R.id.fragment_card_view_jobs_cv);

        cardVIewJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DetailServiceActivity.class);
                intent.putExtra("services",servicesFinal);
                intent.putExtra("labourer",labourerFinal);
                startActivity(intent);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    CustomerFinal fetchCustomer(String CustomerUID){
        firebaseFirestore.collection("customer").document(CustomerUID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //labourer = new Labourer();
                        if (documentSnapshot.getData() != null) {
                            customerFinal = (CustomerFinal) documentSnapshot.toObject(CustomerFinal.class);
                            Log.d("fetched", documentSnapshot.getData().toString() + "!");
                            Log.d("Customer in cardView",customerFinal.toString());
                            TextView name = view.findViewById(R.id.fragment_card_view_jobs_tv_name);
                            name.setText(customerFinal.getName());
                            TextView title = view.findViewById(R.id.fragment_card_view_jobs_tv_title);
                            title.setText(servicesFinal.getTitle());
                            CircleImageView image = view.findViewById(R.id.fragment_card_view_jobs_civ_photo);
                            if(customerFinal.getImage() != "null"){
                                Glide.with(view.getContext()).load(customerFinal.getImage()).into(image);
                            }
                            if(servicesFinal.getSkill().equals("Carpenter")) {
                                title.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_carpenter_tools_colour,0,0,0);
                            }
                            if(servicesFinal.getSkill().equals("Plumber")) {
                                title.setCompoundDrawablesRelativeWithIntrinsicBounds(ic_plumber_tools,0,0,0);
                            }
                            if(servicesFinal.getSkill().equals("Electrician")) {
                                title.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_electric_colour,0,0,0);
                            }
                            if(servicesFinal.getSkill().equals("Painter")) {
                                title.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_paint_roller,0,0,0);
                            }
                            if(servicesFinal.getSkill().equals("Constructor")) {
                                title.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_construction_colour,0,0,0);
                            }
                            if(servicesFinal.getSkill().equals("Chef")) {
                                title.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_cooking_colour,0,0,0);
                            }

                        } else {
                            Log.d("not fetched", "null");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("onFailure","null");
                    }
                });
        return customerFinal;
    }

}