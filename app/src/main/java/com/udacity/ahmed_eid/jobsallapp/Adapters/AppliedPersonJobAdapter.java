package com.udacity.ahmed_eid.jobsallapp.Adapters;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.udacity.ahmed_eid.jobsallapp.Activities.EmployeeProfileActivity;
import com.udacity.ahmed_eid.jobsallapp.Fragments.MyEmployeeProfileFragment;
import com.udacity.ahmed_eid.jobsallapp.Model.AppliedJob;
import com.udacity.ahmed_eid.jobsallapp.R;
import com.udacity.ahmed_eid.jobsallapp.Utilites.AppConstants;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppliedPersonJobAdapter extends RecyclerView.Adapter<AppliedPersonJobAdapter.AppliedPersonViewHolder> {

    Context mContext;
    ArrayList<AppliedJob> appliedJobs;

    public AppliedPersonJobAdapter(Context mContext, ArrayList<AppliedJob> appliedJobs) {
        this.mContext = mContext;
        this.appliedJobs = appliedJobs;
    }

    @NonNull
    @Override
    public AppliedPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(mContext).inflate(R.layout.item_applied_person, parent, false);
        return new AppliedPersonViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppliedPersonViewHolder holder, int position) {
        final AppliedJob appliedJob = appliedJobs.get(position);
        holder.empName.setText(appliedJob.getEmpName());
        //holder.empTitle.setText(appliedJob.getEmpJobTitle());
        Glide.with(mContext)
                .load(appliedJob.getEmpImage())
                .error(R.drawable.user_profile_default)
                .into(holder.empImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EmployeeProfileActivity.class);
                intent.putExtra(AppConstants.INTENT_employeeIdKey, appliedJob.getEmpId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appliedJobs.size();
    }

    public class AppliedPersonViewHolder extends RecyclerView.ViewHolder {
        TextView empName, empTitle;
        CircleImageView empImage;
        View itemView;

        public AppliedPersonViewHolder(View itemView) {
            super(itemView);
            empName = itemView.findViewById(R.id.itemApplied_user_name);
            empTitle = itemView.findViewById(R.id.itemApplied_user_job);
            empImage = itemView.findViewById(R.id.itemApplied_user_image);
            this.itemView = itemView;
        }
    }
}
