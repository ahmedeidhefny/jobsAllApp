package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.ahmed_eid.jobsallapp.Model.Employee;
import com.udacity.ahmed_eid.jobsallapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EmployeeRegisterActivity extends AppCompatActivity {

    private static final String TAG = "EmployeeRegisterActivity";
    @BindView(R.id.empSpinner)
    MaterialSpinner empSpinner;
    @BindView(R.id.gender_radio_group)
    RadioGroup genderRadioGroup;
    @BindView(R.id.job_title)
    EditText jobTitle;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.country)
    EditText country;
    @BindView(R.id.city)
    EditText city;
    @BindView(R.id.birthOfDate)
    EditText birthOfDate;
    @BindView(R.id.summery)
    EditText summery;
    @BindView(R.id.employee_goToApp_btn)
    RelativeLayout employeeGoToAppBtn;
    @BindView(R.id.emp_name)
    EditText empName;
    @BindView(R.id.nationality)
    EditText nationality;
    @BindView(R.id.marital_status)
    EditText maritalStatus;
    @BindView(R.id.miritary_status)
    EditText miritaryStatus;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static final String userType = "Employee";

    private String gender = null;
    private String empCategory = null;
    private ArrayAdapter<String> adapter;
    String[] categories = {
            "programming", "IT jobs", "engineering", "econimic", "IS programing"
    };//= getResources().getStringArray(R.array.categories);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_register);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spinner, categories);
        adapter.setDropDownViewResource(R.layout.item_spinner);
        empSpinner.setAdapter(adapter);
        empSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // empCategory = (String) empSpinner.getItemAtPosition(i);
                //Toast.makeText(EmployeeRegisterActivity.this, empCategory, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

//    private void receiveUserFromInitialRegister() {
//        Intent intent = getIntent();
//        if (intent == null) {
//            showErrorMassage();
//            return;
//        }
//        if (intent.hasExtra(AppConstants.INTENT_EmployeeOrCompanyUserKey)) {
//            user = intent.getParcelableExtra(AppConstants.INTENT_EmployeeOrCompanyUserKey);
//        } else {
//            showErrorMassage();
//        }
//    }
//
//    private void showErrorMassage() {
//        Toast.makeText(this, R.string.massage_user_error, Toast.LENGTH_SHORT).show();
//        finish();
//    }

    @OnClick({R.id.birthOfDate, R.id.employee_goToApp_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.birthOfDate:
                break;
            case R.id.employee_goToApp_btn:
                registerEmployee();
                break;
        }
    }

    private void registerEmployee() {
        String jobTile = jobTitle.getText().toString();
        String empCity = city.getText().toString();
        String empCountry = country.getText().toString();
        String empSummery = summery.getText().toString();
        String birthDate = birthOfDate.getText().toString();
        String empPhone = phone.getText().toString();
        String empyName = empName.getText().toString();
        String nat = nationality.getText().toString();
        String marital = maritalStatus.getText().toString();
        String miritary = miritaryStatus.getText().toString();

        int checkedRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.male_radioBtn) {
            gender = "Male";
        } else if (checkedRadioButtonId == R.id.female_radioBtn) {
            gender = "Female";
        } else {
            Toast.makeText(this, R.string.massage_notSeclect_gender, Toast.LENGTH_LONG).show();
        }

        String userId = mAuth.getCurrentUser().getUid();

        if (!TextUtils.isEmpty(jobTile) && !TextUtils.isEmpty(empSummery) && !TextUtils.isEmpty(empCountry) && !TextUtils.isEmpty(empCity) && !TextUtils.isEmpty(birthDate) && !TextUtils.isEmpty(empPhone)) {
            Employee employee = new Employee(userId, empyName, userType, jobTile, empPhone,nat, gender, empCountry, empCity, birthDate, empSummery, "programming",miritary,marital);
            mDatabase.child(userId).setValue(employee);
            goToMainActivity();
            Toast.makeText(getApplicationContext(), "YourDataSaved:Successfully..", Toast.LENGTH_LONG).show();
        }
    }

    private void goToMainActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), MainScreenWithNavigation.class);
        startActivity(mainIntent);
        finish();
    }

}