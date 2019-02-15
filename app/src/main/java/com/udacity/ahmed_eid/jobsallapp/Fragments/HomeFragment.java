package com.udacity.ahmed_eid.jobsallapp.Fragments;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.ahmed_eid.jobsallapp.Adapters.JobAdapter;
import com.udacity.ahmed_eid.jobsallapp.Model.Company;
import com.udacity.ahmed_eid.jobsallapp.Model.Job;
import com.udacity.ahmed_eid.jobsallapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {

    DatabaseReference mDatabase;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = myView.findViewById(R.id.homeRecycler);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Jobs");
        readAllJobs();
        return myView;
    }

    private void readAllJobs() {
        final ArrayList<Job> jobs = new ArrayList<>();
        //final ArrayList<Company> companies = new ArrayList<>();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Job job = snapshot.getValue(Job.class);
                        jobs.add(job);
                    }
                   // readCompaniesAddedThisJobs(jobs);
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(manager);
                    JobAdapter jobAdapter = new JobAdapter(getActivity(), jobs);
                    recyclerView.setAdapter(jobAdapter);
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

    private void readCompaniesAddedThisJobs(final ArrayList<Job> jobs) {
        final ArrayList<Company> companies = new ArrayList<>();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        for (Job job : jobs) {
            Query query = mDatabase.orderByChild("compId").equalTo(job.getCompanyId());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Company company = snapshot.getValue(Company.class);
                            companies.add(company);
                        }
                        int jobSize = jobs.size();
                        int companySize = companies.size();
                        Log.e("size","jobArr"+jobSize+"compArr"+companySize);
                        if (jobSize == companySize) {
                            LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(manager);
                            //JobAdapter jobAdapter = new JobAdapter(getActivity(), jobs, companies);
                            //recyclerView.setAdapter(jobAdapter);
                        }
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


}
