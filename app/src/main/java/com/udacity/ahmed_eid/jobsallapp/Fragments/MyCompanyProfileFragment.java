package com.udacity.ahmed_eid.jobsallapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.ahmed_eid.jobsallapp.Model.Company;
import com.udacity.ahmed_eid.jobsallapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MyCompanyProfileFragment extends Fragment {


    @BindView(R.id.profile_comp_name)
    TextView profileCompName;
    @BindView(R.id.profile_comp_category)
    TextView profileCompCategory;
    @BindView(R.id.profile_comp_website)
    TextView profileCompWebsite;
    @BindView(R.id.profile_comp_address)
    TextView profileCompAddress;
    @BindView(R.id.profile_comp_foundedDate)
    TextView profileCompFoundedDate;
    @BindView(R.id.profile_comp_profile)
    TextView profileCompProfile;
    Unbinder unbinder;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    @BindView(R.id.profile_comp_container_layout)
    FrameLayout profileCompContainerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_company_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        readCmpanyData();
        return view;
    }

    private void readCmpanyData() {
        String compId = mAuth.getCurrentUser().getUid();
        Query query = mDatabase.orderByChild("userId").equalTo(compId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Company company = snapshot.getValue(Company.class);
                        populateUI(company);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void populateUI(Company company) {
        profileCompAddress.setText(company.getCompCountry() + " - " + company.getCompCity());
        profileCompCategory.setText(company.getCompCategory());
        profileCompFoundedDate.append(company.getCompFounderDate());
        profileCompProfile.setText(company.getCompProfile());
        profileCompWebsite.setText(company.getCompWebsite());
        profileCompName.setText(company.getCompName());
        String compId = company.getUserId();
        MyAddedJobsFragment fragment = new MyAddedJobsFragment();
        fragment.setCompanyId(compId);
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.profile_comp_container_layout,fragment).commit();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
