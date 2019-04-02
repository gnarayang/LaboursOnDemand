package com.example.labourondemand;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ServiceAmountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ServiceAmountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppliedServiceAmountFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AppliedServiceAmountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceAmountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppliedServiceAmountFragment newInstance(String param1, String param2) {
        AppliedServiceAmountFragment fragment = new AppliedServiceAmountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            services = (ServicesFinal)bundle.getSerializable("services");
        }
    }

    private ServicesFinal services;
    private TextView customerAmount;
    private TextView labourerAmount;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_applied_service_amount, container, false);

        customerAmount = view.findViewById(R.id.service_amount_tv_customer_amount);
        labourerAmount = view.findViewById(R.id.service_amount_tiet_labourer_amount);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        Log.d("service id",services.getServiceId().toString());
        customerAmount.setText(String.valueOf(services.getCustomerAmount()));

        labourerAmount.setText(String.valueOf(services.getLabourerResponses().get(firebaseAuth.getUid())));

        //  labourerAmount.setText(String.valueOf(ser));
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//
//                if(labourerAmount.getText().toString() != null) {
//
//                    HashMap<String, HashMap<String, Long>> map = new HashMap<>();
//                    HashMap<String, Long> m = new HashMap<>();
//                    m.put(firebaseAuth.getUid(), Long.valueOf(labourerAmount.getText().toString()));
//
//                    //TODO: updating labourResponse ;
//                    map.put("labourerResponses", m);
//                    services.setCustomerAmount(Long.valueOf(labourerAmount.getText().toString()));
//                    final String sid = services.getServiceID();
//                    //TODO:services.setLabourerResponses(m);
//                    Log.d("tag",sid+"!"+m.toString());
//                    firebaseFirestore.collection("services").document(sid).set(map,SetOptions.merge())
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    HashMap<String, String> lab = new HashMap<>();
//                                    lab.put("currentService", sid);
//
//                                    /*firebaseFirestore.collection("labourer").document(firebaseAuth.getUid())
//                                            .set(lab, SetOptions.merge())
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    Intent intent = new Intent(view.getContext(), LabourerMainActivity.class);
//                                                    intent.putExtra("currentService", sid);
//                                                    Toast.makeText(v.getContext(),"Update",Toast.LENGTH_LONG).show();
//                                                    //startActivity(intent);
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Log.d("tag",e.toString());
//                                                }
//                                            });*/
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                        }
//                    });
//                }else{
//
//                    labourerAmount.setError("Give an amount and then submit");
//
//                }
//            }
//        });

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
}
