package com.example.labourondemand;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerHistoryAdapter extends RecyclerView.Adapter<CustomerHistoryAdapter.ViewHolder> {

    private static final String TAG = "LabourerHistoryRVAdapter";

    private Context context;
    private ArrayList<ServicesFinal> services;
    private View view;
    //    private ServicesFinal services;
    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView jobImage, labourerImage;
        TextView day, date, time, jobcost, jobTitle;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobImage = itemView.findViewById(R.id.job_image);
            labourerImage = itemView.findViewById(R.id.labour_image);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            jobcost = itemView.findViewById(R.id.job_cost);
            jobTitle = itemView.findViewById(R.id.job_title);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public CustomerHistoryAdapter(Context context, ArrayList<ServicesFinal> services) {
        this.services = services;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomerHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_labourer_history_item, parent, false);
        CustomerHistoryAdapter.ViewHolder holder = new CustomerHistoryAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHistoryAdapter.ViewHolder viewHolder, int position) {
        ServicesFinal service = services.get(viewHolder.getAdapterPosition());

        viewHolder.jobcost.setText(String.valueOf(service.getCustomerAmount()));
        viewHolder.date.setText(service.getEndTime());
        viewHolder.jobTitle.setText(service.getTitle());
        Glide.with(context).load(service.getImages().get(0)).into(viewHolder.labourerImage);

        if(service.getSkill().equals("Carpenter"))
        {
            viewHolder.jobImage.setImageDrawable(context.getDrawable(R.drawable.ic_carpenter_tools_colour));
        }else if(service.getSkill().equals("Plumber"))
        {
            viewHolder.jobImage.setImageDrawable(context.getDrawable(R.drawable.ic_plumber_tools));

        }else if(service.getSkill().equals("Electrician"))
        {
            viewHolder.jobImage.setImageDrawable(context.getDrawable(R.drawable.ic_electric_colour));

        }else if(service.getSkill().equals("Painter"))
        {
            viewHolder.jobImage.setImageDrawable(context.getDrawable(R.drawable.ic_paint_roller));

        }else if(service.getSkill().equals("Constructor"))
        {
            viewHolder.jobImage.setImageDrawable(context.getDrawable(R.drawable.ic_construction_colour));

        }else if(service.getSkill().equals("Chef"))
        {
            viewHolder.jobImage.setImageDrawable(context.getDrawable(R.drawable.ic_cooking_colour));

        }
        viewHolder.time.setText(service.getStartTime());
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public void added(ServicesFinal servicesFinal){
        Log.d("added @ adapter", services.size()+"s");
        services.add(servicesFinal);
        notifyItemInserted(services.indexOf(servicesFinal));
    }

}
