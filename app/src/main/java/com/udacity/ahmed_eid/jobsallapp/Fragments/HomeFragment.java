package com.udacity.ahmed_eid.jobsallapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
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
    private EditText search_ET;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.app_bar_search) {
            String searchText = search_ET.getText().toString();
            if (!TextUtils.isEmpty(searchText)) {
                searchInDatabase(searchText);
            } else {
                Toast.makeText(getActivity(), "Enter Job Name !", Toast.LENGTH_SHORT).show();
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


    public void setRecyclerAdapter(ArrayList<Job> jobs) {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        JobAdapter jobAdapter = new JobAdapter(getActivity(), jobs);
        recyclerView.setAdapter(jobAdapter);
    }
}
