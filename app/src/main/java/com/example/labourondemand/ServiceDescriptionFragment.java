package com.example.labourondemand;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.example.labourondemand.R.drawable.ic_plumber_tools;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ServiceDescriptionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ServiceDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceDescriptionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ServiceDescriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceDescriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceDescriptionFragment newInstance(String param1, String param2) {
        ServiceDescriptionFragment fragment = new ServiceDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private ServicesFinal services;
    private LabourerFinal labourerFinal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            services = (ServicesFinal) bundle.getSerializable("services");
        }
    }

    private TextView tags, description, skill, startTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_description, container, false);

        skill = view.findViewById(R.id.service_description_tv_skill);
        description = view.findViewById(R.id.service_description_tv_description);
        tags = view.findViewById(R.id.service_description_tv_tags);
        startTime = view.findViewById(R.id.service_description_tv_start_time_fill);
        skill.setText(services.getSkill().toString());
        description.setText(services.getDescription().toString());
        tags.setText(services.getTitle());
        startTime.setText(services.getStartTime());
        if(services.getSkill().equals("Carpenter")) {
            skill.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,R.drawable.ic_carpenter_tools_colour);
        }
        if(services.getSkill().equals("Plumber")) {
            skill.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,ic_plumber_tools);
        }
        if(services.getSkill().equals("Electrician")) {
            skill.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,R.drawable.ic_electric_colour);
        }
        if(services.getSkill().equals("Painter")) {
            skill.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,R.drawable.ic_paint_roller);
        }
        if(services.getSkill().equals("Constructor")) {
            skill.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,R.drawable.ic_construction_colour);
        }
        if(services.getSkill().equals("Chef")) {
            skill.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,R.drawable.ic_cooking_colour);
        }
        TextView distance = view.findViewById(R.id.fragment_card_view_jobs_tv_distance);
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