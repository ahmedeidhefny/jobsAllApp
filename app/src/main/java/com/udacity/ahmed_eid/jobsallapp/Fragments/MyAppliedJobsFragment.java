package com.udacity.ahmed_eid.jobsallapp.Fragments;

import android.content.Context;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.ahmed_eid.jobsallapp.Activities.JobDetailsActivity;
import com.udacity.ahmed_eid.jobsallapp.Adapters.JobAdapter;
import com.udacity.ahmed_eid.jobsallapp.Model.AppliedJob;
import com.udacity.ahmed_eid.jobsallapp.Model.Job;
import com.udacity.ahmed_eid.jobsallapp.Model.SavedJob;
import com.udacity.ahmed_eid.jobsallapp.R;

import java.util.ArrayList;


public class MyAppliedJobsFragment extends Fragment {
    private RecyclerView appliedJobRecycler;
    private FirebaseAuth mAuth ;
    private DatabaseReference mDatabase ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.fragment_my_applied_jobs, container, false);

        appliedJobRecycler = myView.findViewById(R.id.appliedPersons_recycler);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference() ;
        readAppliedJobs();
        return myView;
    }

    private void readAppliedJobs() {
        String empId = mAuth.getCurrentUser().getUid();
        final ArrayList<AppliedJob> appliedJobs  = new ArrayList<>();
        Query query = mDatabase.child("ApplyJobs").child(empId).orderByChild("empId").equalTo(empId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        AppliedJob appliedJob = snapshot.getValue(AppliedJob.class);
                       appliedJobs.add(appliedJob);
                    }
                   readJobsThisUser(appliedJobs);
                }else {
                    Toast.makeText(getActivity(), "Not Found Applied Jobs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String massage = error.getMessage();
                Toast.makeText(getActivity(), "Error: " + massage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void readJobsThisUser(final ArrayList<AppliedJob> appliedJobs) {
        final ArrayList<Job> jobs =new ArrayList<>();
        for (final AppliedJob appliedJob : appliedJobs){
            String jobId = appliedJob.getJobId();
            Query query = mDatabase.child("Jobs").orderByChild("jobId").equalTo(jobId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Job job = snapshot.getValue(Job.class);
                            jobs.add(job);
                        }
                        int jobSize = jobs.size();
                        int appliedJobSize = appliedJobs.size();
                        if (appliedJobSize == jobSize){
                            setDataToRecyclerAdapter(jobs);
                        }
                    }else {
                        Toast.makeText(getActivity(), "Not Found a Jobs", Toast.LENGTH_SHORT).show();
                        Log.e("applayed Frag","Not Found a Jobs");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    String massage = error.getMessage();
                    Toast.makeText(getActivity(), "Error: " + massage, Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    public void setDataToRecyclerAdapter(ArrayList<Job> jobs) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        appliedJobRecycler.setLayoutManager(manager);
        JobAdapter jobAdapter = new JobAdapter(getActivity(), jobs);
        appliedJobRecycler.setAdapter(jobAdapter);
    }
}
