package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.udacity.ahmed_eid.jobsallapp.Fragments.MyEmployeeProfileFragment;
import com.udacity.ahmed_eid.jobsallapp.R;
import com.udacity.ahmed_eid.jobsallapp.Utilites.AppConstants;

public class EmployeeProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);
        setTitle("Profile");
        receiveEmpolyeeIdFromAdapter();
    }

    private void receiveEmpolyeeIdFromAdapter() {
        Intent intent = getIntent();
        if (intent == null) {
            showErrorMassage();
            return;
        }
        if (intent.hasExtra(AppConstants.INTENT_employeeIdKey)) {
            String employeeId = intent.getStringExtra(AppConstants.INTENT_employeeIdKey);
            addFragment(employeeId);
        } else {
            showErrorMassage();
        }
    }

    private void addFragment(String employeeId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        MyEmployeeProfileFragment profileFragment = new MyEmployeeProfileFragment();
        profileFragment.setEmployeeId(employeeId);
        fragmentManager.beginTransaction().replace(R.id.profileEmpContainer_layout, profileFragment)
                .addToBackStack(null).commit();
    }


    private void showErrorMassage() {
        Toast.makeText(this, "NO Found Data", Toast.LENGTH_SHORT).show();
        finish();
    }
}
