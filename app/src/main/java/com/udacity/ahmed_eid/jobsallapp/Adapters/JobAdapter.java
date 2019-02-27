package com.udacity.ahmed_eid.jobsallapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.ahmed_eid.jobsallapp.Activities.AddNewJobActivity;
import com.udacity.ahmed_eid.jobsallapp.Activities.CompanyProfileActivity;
import com.udacity.ahmed_eid.jobsallapp.Activities.JobDetailsActivity;
import com.udacity.ahmed_eid.jobsallapp.Model.Company;
import com.udacity.ahmed_eid.jobsallapp.Model.Job;
import com.udacity.ahmed_eid.jobsallapp.R;
import com.udacity.ahmed_eid.jobsallapp.Utilites.AppConstants;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private Context mContext;
    private ArrayList<Job> jobs;
    private DatabaseReference mDatabase;
    private static final String TAG = "Job Adapter";


    public JobAdapter(Context mContext, ArrayList<Job> jobs) {
        this.mContext = mContext;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        this.jobs = jobs;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(mContext).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        final Job job = jobs.get(position);
        String jobName = job.getTitle();
        String jobNameUpper = jobName.substring(0, 1).toUpperCase() + jobName.substring(1);
        holder.jobTitle.setText(jobNameUpper);
        holder.location.setText(job.getCountry() + " - " + job.getCity());
        readCompanyData(job, holder.compName, holder.image, holder.viewBtn);
    }

    private void readCompanyData(final Job job, final TextView compName, final CircleImageView image, final RelativeLayout viewBtn) {
        final String companyId = job.getCompanyId();
        Query query = mDatabase.orderByChild("userId").equalTo(companyId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Company company = snapshot.getValue(Company.class);
                        String comName = company.getCompName();
                        compName.setText(comName);
                        compName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(mContext, CompanyProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(AppConstants.INTENT_CompanyIdKey,companyId);
                                mContext.startActivity(intent);
                            }
                        });
                        String logo = company.getCompLogo();
                        if (!TextUtils.isEmpty(logo)) {
                            Glide.with(mContext)
                                    .load(logo)
                                    .error(R.drawable.default_logo)
                                    .into(image);
                        }else {
                            image.setImageResource(R.drawable.default_logo);
                        }
                        goToDetailsActivity(job, viewBtn, comName, logo);
                    }
                } else {
                    Log.e(TAG, "data of company is empty");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String massage = error.getMessage();
                Toast.makeText(mContext, "Error: " + massage, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Error: " + massage);
            }
        });
    }

    private void goToDetailsActivity(final Job job, RelativeLayout viewBtn, final String comName, final String logo) {
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailJobIntent = new Intent(mContext, JobDetailsActivity.class);
                detailJobIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                detailJobIntent.putExtra(AppConstants.INTENT_JobAdapterKey, job);
                detailJobIntent.putExtra(AppConstants.INTENT_JobAdapterCompNameKey, comName);
                detailJobIntent.putExtra(AppConstants.INTENT_JobAdapterCompLogoKey, logo);
                mContext.startActivity(detailJobIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public class JobViewHolder extends RecyclerView.ViewHolder {
        TextView compName, jobTitle, location;
        RelativeLayout viewBtn;
        CircleImageView image;

        public JobViewHolder(View itemView) {
            super(itemView);
            compName = itemView.findViewById(R.id.item_company_name);
            jobTitle = itemView.findViewById(R.id.item_job_name);
            location = itemView.findViewById(R.id.item_job_location);
            viewBtn = itemView.findViewById(R.id.job_view_btn);
            image = itemView.findViewById(R.id.item_company_logo);
        }
    }
}
