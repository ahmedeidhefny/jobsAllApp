package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.udacity.ahmed_eid.jobsallapp.Fragments.MyCompanyProfileFragment;
import com.udacity.ahmed_eid.jobsallapp.R;
import com.udacity.ahmed_eid.jobsallapp.Utilites.AppConstants;

public class CompanyProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        setTitle("Profile");
        receiveCompanyIdFromAdapter();
    }

    private void receiveCompanyIdFromAdapter() {
        Intent intent = getIntent();
        if (intent == null) {
            showErrorMassage();
            return;
        }
        if (intent.hasExtra(AppConstants.INTENT_CompanyIdKey)) {
            String companyId = intent.getStringExtra(AppConstants.INTENT_CompanyIdKey);
            addFragment(companyId);
        } else {
            showErrorMassage();
        }
    }

    private void addFragment(String companyId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        MyCompanyProfileFragment profileFragment = new MyCompanyProfileFragment();
        profileFragment.setCompanyId(companyId);
        fragmentManager.beginTransaction().replace(R.id.profileCompanyContainer_layout, profileFragment)
                .addToBackStack(null).commit();
    }


    private void showErrorMassage() {
        Toast.makeText(this, R.string.massage_data_error, Toast.LENGTH_SHORT).show();
        finish();
    }
}


