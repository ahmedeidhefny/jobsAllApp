package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.ahmed_eid.jobsallapp.Model.Job;
import com.udacity.ahmed_eid.jobsallapp.R;

import java.text.DateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

public class AddNewJobActivity extends AppCompatActivity {

    @BindView(R.id.job_title)
    EditText jobTitle;
    @BindView(R.id.jobSpinner)
    MaterialSpinner jobSpinner;
    @BindView(R.id.job_vacancies_num)
    EditText jobVacanciesNum;
    @BindView(R.id.min)
    EditText min;
    @BindView(R.id.max)
    EditText max;
    @BindView(R.id.degree_level)
    EditText degreeLevel;
    @BindView(R.id.jobTypeSpinner)
    MaterialSpinner jobTypeSpinner;
    @BindView(R.id.nationality)
    EditText nationality;
    @BindView(R.id.jobCountry)
    EditText jobCountry;
    @BindView(R.id.jobCity)
    EditText jobCity;
    @BindView(R.id.job_description)
    EditText jobDescription;
    @BindView(R.id.job_requirement)
    EditText jobRequirement;
    @BindView(R.id.post_job_btn)
    RelativeLayout postJobBtn;
    @BindView(R.id.salary)
    EditText salary;
    @BindView(R.id.applied_age)
    EditText appliedAge;
    @BindView(R.id.expiry_date)
    EditText expiryDate;
    @BindView(R.id.job_gender_radio_group)
    RadioGroup jobGenderRadioGroup;

    private String[] categories;
    private String[] jobTypes;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String category = null;
    private String jobType = null;
    private String gender = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_job);
        ButterKnife.bind(this);
        setSpinnerAdapter();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Jobs").push();

    }

    private void addNewJob() {
        String title= jobTitle.getText().toString();
        String titleLowerCase = title.toLowerCase() ;
        String city = jobCity.getText().toString();
        String country = jobCountry.getText().toString();
        String des = jobDescription.getText().toString();
        String req = jobRequirement.getText().toString();
        String vanNum = jobVacanciesNum.getText().toString();
        String jMax = max.getText().toString();
        String jMin = min.getText().toString();
        String level = degreeLevel.getText().toString();
        String nat = nationality.getText().toString();
        String sal = salary.getText().toString();
        String age = appliedAge.getText().toString();
        String exDate = expiryDate.getText().toString();

        int checkedRadioButtonId = jobGenderRadioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.male_radioBtn) {
            gender = "Male";
        } else if (checkedRadioButtonId == R.id.female_radioBtn) {
            gender = "Female";
        } else if (checkedRadioButtonId == R.id.both_radioBtn) {
            gender = "Male & Female";
        } else {
            Toast.makeText(this, R.string.massage_notSeclect_gender, Toast.LENGTH_LONG).show();
        }


        String userId = mAuth.getCurrentUser().getUid();

        if (category != null && jobType != null && !TextUtils.isEmpty(userId)) {
            Job job = new Job(mDatabase.getKey(), userId, titleLowerCase, category, gender, age, sal, exDate, vanNum, jMin, jMax, level, jobType, nat, country, city, des, req);
            mDatabase.setValue(job);
            goToMainActivity();
            Toast.makeText(getApplicationContext(), "Post New Job: Successfully..", Toast.LENGTH_LONG).show();
        }
    }

    public void setSpinnerAdapter() {
        categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spinner, categories);
        categoryAdapter.setDropDownViewResource(R.layout.item_spinner);
        jobSpinner.setAdapter(categoryAdapter);
        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jobSpinner.setError(null);
                category = (String) jobSpinner.getSelectedItem();
                Toast.makeText(AddNewJobActivity.this, "" + category, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                jobSpinner.setError("Error");
            }
        });
        //----------------------------------------
        jobTypes = getResources().getStringArray(R.array.jobTypes);
        ArrayAdapter<String> jobTypeAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spinner, jobTypes);
        jobTypeAdapter.setDropDownViewResource(R.layout.item_spinner);
        jobTypeSpinner.setAdapter(jobTypeAdapter);

        jobTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jobType = (String) jobTypeSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void goToMainActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), MainScreenWithNavigation.class);
        startActivity(mainIntent);
        finish();
    }


    @OnClick({R.id.expiry_date, R.id.post_job_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.expiry_date:
                showCalenderAndGetDate();
                break;
            case R.id.post_job_btn:
                addNewJob();
                break;
        }
    }

    private void showCalenderAndGetDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                expiryDate.setText(year + "/" + month + "/" + day);
            }
        }, year, month, day);
        pickerDialog.show();
    }
}
