package com.example.labourondemand;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LabourerAdapter extends RecyclerView.Adapter<LabourerAdapter.MyViewHolder> {
    private Context context;
    private ServicesFinal servicesFinals;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CircleImageView photo;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.item_review_labourer_tv);
            photo = view.findViewById(R.id.item_review_labourer_civ);
        }
    }


    public LabourerAdapter(Context context, ServicesFinal servicesFinals) {
        this.context = context;
        this.servicesFinals = servicesFinals;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.item_review_labourer, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final LabourerAdapter.MyViewHolder holder, final int position) {

        LabourerFinal labourer = servicesFinals.getSelectedLabourers().get(position);
        holder.name.setText(labourer.getName());

        Glide.with(context).load(labourer.getImage()).into(holder.photo);
    }

    @Override
    public int getItemCount() {
        if(servicesFinals.getSelectedLabourers() == null)
        {
            servicesFinals.setSelectedLabourers(new ArrayList<>());
        }
        return this.servicesFinals.getSelectedLabourers().size();
    }

    public void added(LabourerFinal c){
        Log.d("added @ adapter", servicesFinals.getSelectedLabourers().size()+"s");
        servicesFinals.getSelectedLabourers().add(c);
        notifyItemInserted(servicesFinals.getSelectedLabourers().indexOf(c));
    }

    public ServicesFinal getService(){
        return servicesFinals;
    }

  /*  public void added(Services c){
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
        this.service = service;
        this.customer = customer;
    }*/
}