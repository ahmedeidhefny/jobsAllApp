package com.udacity.ahmed_eid.jobsallapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.udacity.ahmed_eid.jobsallapp.Model.AppliedJob;
import com.udacity.ahmed_eid.jobsallapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppliedPersonJobAdapter extends RecyclerView.Adapter<AppliedPersonJobAdapter.AppliedPersonViewHolder>{

    Context mContext ;
    ArrayList<AppliedJob> appliedJobs;

    public AppliedPersonJobAdapter(Context mContext, ArrayList<AppliedJob> appliedJobs) {
        this.mContext = mContext;
        this.appliedJobs = appliedJobs;
    }

    @NonNull
    @Override
    public AppliedPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(mContext).inflate(R.layout.item_applied_person,parent,false);
        return new AppliedPersonViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppliedPersonViewHolder holder, int position) {
        AppliedJob appliedJob = appliedJobs.get(position);
        holder.empName.setText(appliedJob.getEmpName());
        //holder.empTitle.setText(appliedJob.getEmpJobTitle());
        Glide.with(mContext)
                .load(appliedJob.getEmpImage())
                .error(R.drawable.user_profile_default)
                .into(holder.empImage);
    }

    @Override
    public int getItemCount() {
        return appliedJobs.size();
    }

    public class AppliedPersonViewHolder extends RecyclerView.ViewHolder {
        TextView empName, empTitle;
        CircleImageView empImage;

        public AppliedPersonViewHolder(View itemView) {
            super(itemView);
            empName = itemView.findViewById(R.id.itemApplied_user_name);
            empTitle = itemView.findViewById(R.id.itemApplied_user_job);
            empImage = itemView.findViewById(R.id.itemApplied_user_image);
        }
    }
}
