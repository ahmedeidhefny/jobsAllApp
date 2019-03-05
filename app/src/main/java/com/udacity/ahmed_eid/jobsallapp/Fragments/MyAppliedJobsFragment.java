package com.udacity.ahmed_eid.jobsallapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.udacity.ahmed_eid.jobsallapp.Adapters.JobAdapter;
import com.udacity.ahmed_eid.jobsallapp.Model.AppliedJob;
import com.udacity.ahmed_eid.jobsallapp.Model.Job;
import com.udacity.ahmed_eid.jobsallapp.R;

import java.util.ArrayList;


public class MyAppliedJobsFragment extends Fragment {
    private static final String TAG = "MyAppliedJobsFragment";
    private RecyclerView appliedJobRecycler;
    private FirebaseAuth mAuth ;
    private DatabaseReference mDatabase ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().findViewById(R.id.search_EText).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.fragment_my_applied_jobs, container, false);

        appliedJobRecycler = myView.findViewById(R.id.appliedPersons_recycler);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference() ;
        readAppliedJobs();
        return myView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.app_bar_search);
        item.setVisible(false);
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
                }else { Toast.makeText(getActivity(), R.string.massage_not_found_job, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), R.string.massage_not_found_job, Toast.LENGTH_SHORT).show();
                        Log.e(TAG,"Not Found a Jobs");
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

    private void setDataToRecyclerAdapter(ArrayList<Job> jobs) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        appliedJobRecycler.setLayoutManager(manager);
        JobAdapter jobAdapter = new JobAdapter(getActivity(), jobs);
        appliedJobRecycler.setAdapter(jobAdapter);
    }
}
