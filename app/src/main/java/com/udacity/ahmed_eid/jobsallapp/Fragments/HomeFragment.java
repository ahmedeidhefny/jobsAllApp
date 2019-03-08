package com.udacity.ahmed_eid.jobsallapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.ahmed_eid.jobsallapp.Adapters.JobAdapter;
import com.udacity.ahmed_eid.jobsallapp.Model.Job;
import com.udacity.ahmed_eid.jobsallapp.R;
import com.udacity.ahmed_eid.jobsallapp.Widget.Widget_MangeDataHelper;

import java.util.ArrayList;
import java.util.Collections;


public class HomeFragment extends Fragment {

    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private EditText search_ET;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = myView.findViewById(R.id.homeRecycler);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Jobs");
        search_ET = getActivity().findViewById(R.id.search_EText);
        search_ET.setVisibility(View.VISIBLE);
        readAllJobs();
        return myView;
    }

    private void readAllJobs() {
        final ArrayList<Job> jobs = new ArrayList<>();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Job job = snapshot.getValue(Job.class);
                        jobs.add(job);
                    }
                    setRecyclerAdapter(jobs);
                    saveDataToShardPreferenceByWidget(jobs);
                } else {
                    Toast.makeText(getActivity(), R.string.massage_not_found_job, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String massage = error.getMessage();
                Toast.makeText(getActivity(), "Error: " + massage, Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.app_bar_search) {
            String searchText = search_ET.getText().toString();
            if (!TextUtils.isEmpty(searchText)) {
                searchInDatabase(searchText);
            } else {

                Toast.makeText(getActivity(), R.string.massage_search_enter_job, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void searchInDatabase(final String searchText) {
        String textLowerCase = searchText.toLowerCase();
        final ArrayList<Job> jobs = new ArrayList<>();
        Query query = mDatabase.orderByChild("title").startAt(textLowerCase).endAt(textLowerCase + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Job job = snapshot.getValue(Job.class);
                        jobs.add(job);
                    }
                    setRecyclerAdapter(jobs);
                } else {
                    Toast.makeText(getActivity(), searchText + " Not Found !", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String massage = error.getMessage();
                Toast.makeText(getActivity(), "Error: " + massage, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setRecyclerAdapter(ArrayList<Job> jobs) {
        Collections.reverse(jobs);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        JobAdapter jobAdapter = new JobAdapter(getActivity(), jobs);
        recyclerView.setAdapter(jobAdapter);
    }

    private void saveDataToShardPreferenceByWidget(ArrayList<Job> jobs) {
        //Widget_MangeDataHelper mangeDataHelper = new Widget_MangeDataHelper(getActivity());
       // mangeDataHelper.setDataWidget(jobs);
    }
}
