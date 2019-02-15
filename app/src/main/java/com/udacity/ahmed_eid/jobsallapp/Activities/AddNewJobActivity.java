package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.ahmed_eid.jobsallapp.Model.Job;
import com.udacity.ahmed_eid.jobsallapp.R;

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

    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> jobTypeAdapter;
    private String categories[] = {
            "programming", "IT jobs", "engineering", "econimic", "IS programing"
    };

    private String jobTypeArr[] = {
            "FullTime", "PartTime", "Freelance"
    };

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String category = "programming";
    private String jobType = "FullTime";
    private String gender = null;

    @OnClick(R.id.post_job_btn)
    public void onViewClicked() {
        addNewJob();
    }


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
        String title = jobTitle.getText().toString();
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

        if (!TextUtils.isEmpty(userId)) {
            Job job = new Job(mDatabase.getKey(), userId, title, category, gender, age, sal, exDate, vanNum, jMin, jMax, level, jobType, nat, country, city, des, req);
            mDatabase.setValue(job);
            goToMainActivity();
            Toast.makeText(getApplicationContext(), "Post New Job: Successfully..", Toast.LENGTH_LONG).show();
        }
    }

    public void setSpinnerAdapter() {
        categoryAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spinner, categories);
        categoryAdapter.setDropDownViewResource(R.layout.item_spinner);
        jobSpinner.setAdapter(categoryAdapter);
        //----------------------------------------
        jobTypeAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spinner, jobTypeArr);
        jobTypeAdapter.setDropDownViewResource(R.layout.item_spinner);
        jobTypeSpinner.setAdapter(jobTypeAdapter);
    }

    private void goToMainActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), MainScreenWithNavigation.class);
        startActivity(mainIntent);
        finish();
    }


}
