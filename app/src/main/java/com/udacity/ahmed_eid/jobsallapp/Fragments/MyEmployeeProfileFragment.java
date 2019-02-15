package com.udacity.ahmed_eid.jobsallapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.ahmed_eid.jobsallapp.Model.Employee;
import com.udacity.ahmed_eid.jobsallapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MyEmployeeProfileFragment extends Fragment {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    @BindView(R.id.profile_emp_name)
    TextView profileEmpName;
    @BindView(R.id.profile_emp_jobTitle)
    TextView profileEmpJobTitle;
    @BindView(R.id.profile_emp_summ)
    TextView profileEmpSumm;
    @BindView(R.id.profile_emp_address)
    TextView profileEmpAddress;
    @BindView(R.id.portofilo_btn)
    Button portofiloBtn;
    @BindView(R.id.contactMe_btn)
    Button contactMeBtn;
    Unbinder unbinder;
    @BindView(R.id.profile_emp_gender)
    TextView profileEmpGender;
    @BindView(R.id.profile_emp_military)
    TextView profileEmpMilitary;
    @BindView(R.id.profile_emp_marital)
    TextView profileEmpMarital;
    @BindView(R.id.profile_emp_birthDate)
    TextView profileEmpBirthDate;
    @BindView(R.id.profile_emp_national)
    TextView profileEmpNational;
    @BindView(R.id.profile_emp_country)
    TextView profileEmpCountry;
    @BindView(R.id.profile_emp_city)
    TextView profileEmpCity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.fragment_my_employee_profile, container, false);
        unbinder = ButterKnife.bind(this, myView);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        readUserData();
        return myView;
    }

    private void readUserData() {
        String userId = mAuth.getCurrentUser().getUid();
        Query query = mDatabase.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Employee employee = snapshot.getValue(Employee.class);
                        populateUI(employee);
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

    private void populateUI(Employee employee) {
        profileEmpName.setText(employee.getEmployeeName());
        profileEmpAddress.setText(employee.getEmpCountry() + " - " + employee.getEmpCity());
        profileEmpJobTitle.setText(employee.getJobTitle());
        profileEmpSumm.setText(employee.getAboutMeSummery());
        profileEmpBirthDate.setText(employee.getBirthOfDate());
        profileEmpCity.setText(employee.getEmpCity());
        profileEmpCountry.setText(employee.getEmpCountry());
        profileEmpMarital.setText(employee.getMaritalStatus());
        profileEmpMilitary.setText(employee.getMilitaryStatus());
        profileEmpGender.setText(employee.getGender());
        profileEmpNational.setText(employee.getNationality());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.portofilo_btn, R.id.contactMe_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.portofilo_btn:
                break;
            case R.id.contactMe_btn:
                break;
        }
    }
}
