package com.udacity.ahmed_eid.jobsallapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.udacity.ahmed_eid.jobsallapp.Adapters.JobAdapter;
import com.udacity.ahmed_eid.jobsallapp.Model.Job;
import com.udacity.ahmed_eid.jobsallapp.R;
import com.udacity.ahmed_eid.jobsallapp.Utilites.AppConstants;

import java.util.ArrayList;


public class MyAddedJobsFragment extends Fragment {
    private RecyclerView myAddedJobsRecycler;
    DatabaseReference mDatabase;
    String companyId ;

    //FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.fragment_my_added_jobs, container, false);
        myAddedJobsRecycler = myView.findViewById(R.id.myAddedJobsRecycler);
        if(savedInstanceState !=null){
            companyId = savedInstanceState.getString(AppConstants.SaveInstance_MyJobs_UserKey);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Jobs");
        //mAuth = FirebaseAuth.getInstance();
        readAllMyAddedJobs();
        return myView;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppConstants.SaveInstance_MyJobs_UserKey,companyId);
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }


    private void readAllMyAddedJobs() {
        final ArrayList<Job> jobs = new ArrayList<>();
        //String companyId = mAuth.getCurrentUser().getUid();
        Query query = mDatabase.orderByChild("companyId").equalTo(companyId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Job job = snapshot.getValue(Job.class);
                        jobs.add(job);
                    }
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    myAddedJobsRecycler.setLayoutManager(manager);
                    JobAdapter jobAdapter = new JobAdapter(getActivity(), jobs);
                    myAddedJobsRecycler.setAdapter(jobAdapter);
                } else {
                    Toast.makeText(getActivity(), "Not Found Jobs", Toast.LENGTH_LONG).show();
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
