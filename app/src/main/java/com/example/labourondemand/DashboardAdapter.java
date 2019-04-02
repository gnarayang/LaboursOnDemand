package com.example.labourondemand;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

class   DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ServicesFinal> servicesArrayList;
    private ArrayList<Labourer> labourers;
    private LabourerMainActivity labourerMainActivity;
    private CustomerMainActivity customerMainActivity;
    private Integer type;
    private ServicesFinal service;
    private Services services;
    private Customer customer;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, tags, landmark, distance;
        public CircleImageView photo;
        public ConstraintLayout constraintLayout;
        public Button accept;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.item_service_tv_title);
            tags = view.findViewById(R.id.item_service_tv_tags);
            landmark = view.findViewById(R.id.item_service_tv_landmark);
            distance = view.findViewById(R.id.item_service_tv_distance);
            photo = view.findViewById(R.id.item_service_civ_photo);
            constraintLayout = view.findViewById(R.id.item_service_cl);
            accept = view.findViewById(R.id.item_service_btn_accept);
        }
    }


    public DashboardAdapter(Context context, ArrayList<ServicesFinal> servicesArrayList, Integer type) {
        this.context = context;
        this.servicesArrayList = servicesArrayList;
        this.type = type;
    }

    public DashboardAdapter(Context context, Integer type, ArrayList<Labourer> labourers, Services service){
        this.context = context;
        this.type = type;
        this.labourers = labourers;
        this.services = service;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_labourer, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final DashboardAdapter.MyViewHolder holder, final int position) {





        holder.accept.setText("Accepted");
        final ServicesFinal service = servicesArrayList.get(position);
        holder.name.setText(service.getCustomer().getName().toString());
        holder.landmark.setText("234");
        Glide.with(context).load(service.getCustomer().getImage()).into(holder.photo);
        service.getRating();
        holder.tags.setText("1");
//            holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d("dashboard labour", service.getServiceId()+"!");
//                    Intent intent = new Intent(context, DetailServiceActivity.class);
//                    intent.putExtra("service", service);
//                    context.startActivity(intent);
//                }
//            });


//            final Labourer labourer = labourers.get(position);
//            Glide.with(context).load(labourer.getImage()).into(holder.photo);
//            holder.name.setText(labourer.getName());
//            System.out.println("--------------------------------------------------------------------------------------------------------------------------"+labourer.getName());
//            holder.landmark.setText( String.valueOf(labourer.getCurrentServicePrice()));
//            holder.accept.setText("Accept");
//            if(labourer.getAverageRating() == null){
//                holder.tags.setText("No Rating");
//            }else {
//                holder.tags.setText(String.valueOf(labourer.getAverageRating()));
//            }
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to Review Activity
                Intent intent = new Intent(context,DetailAcceptedServiceActivity.class);
                intent.putExtra("service",service);
                Log.d("customer dashboard", service.getCustomer()+"!");
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        if(type == 0) {
            return servicesArrayList.size();
        }else{
            return labourers.size();
        }
    }

    public void added(ServicesFinal c){
        Log.d("added @ adapter", servicesArrayList.size()+"s");
        servicesArrayList.add(c);
        notifyItemInserted(servicesArrayList.indexOf(c));
    }

    public void addedFromCustomer(Labourer labourer){
        Log.d("addedFromCustomer ", labourers.size()+"s");
        labourers.add(labourer);
        notifyItemInserted(labourers.indexOf(labourer));
    }

    public void setServiceAndCustomer(Services service, Customer customer){
        this.services = service;
        this.customer = customer;
    }
}