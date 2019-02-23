package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.udacity.ahmed_eid.jobsallapp.Utilites.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity {
    @BindView(R.id.categoryRecycler)
    RecyclerView categoryRecycler;
    private String categoryName;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        receiveCategoryFromAdapter();
        setTitle(categoryName+" Jobs");
        populateUI();
    }

    private void populateUI() {
        final ArrayList<Job> jobs = new ArrayList<>();
        Query query = mDatabase.child("Jobs").orderByChild("category").equalTo(categoryName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Job job = snapshot.getValue(Job.class);
                        jobs.add(job);
                    }
                    setRecyclerAndAdapter(jobs);
                } else
                    Toast.makeText(CategoryActivity.this, "Not Found Jobs", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String massage = databaseError.getMessage();
                Toast.makeText(CategoryActivity.this, "Error: " + massage, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setRecyclerAndAdapter(ArrayList<Job> jobs) {
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        categoryRecycler.setLayoutManager(manager);
        JobAdapter adapter = new JobAdapter(getApplicationContext(), jobs);
        categoryRecycler.setAdapter(adapter);
    }

    private void receiveCategoryFromAdapter() {
        Intent intent = getIntent();
        if (intent == null) {
            showErrorMassage();
            return;
        }
        if (intent.hasExtra(AppConstants.INTENT_CategoryAdapterKey)) {
            categoryName = intent.getStringExtra(AppConstants.INTENT_CategoryAdapterKey);
        } else {
            showErrorMassage();
        }
    }

    private void showErrorMassage() {
        Toast.makeText(this, R.string.massage_job_error, Toast.LENGTH_SHORT).show();
        finish();
    }
}
