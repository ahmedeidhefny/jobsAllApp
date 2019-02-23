package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.ahmed_eid.jobsallapp.Model.Job;
import com.udacity.ahmed_eid.jobsallapp.R;
import com.udacity.ahmed_eid.jobsallapp.Utilites.AppConstants;


import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;


public class EditJobActivity extends AppCompatActivity {

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
    @BindView(R.id.salary)
    EditText salary;
    @BindView(R.id.degree_level)
    EditText degreeLevel;
    @BindView(R.id.jobTypeSpinner)
    MaterialSpinner jobTypeSpinner;
    @BindView(R.id.job_gender_radio_group)
    RadioGroup jobGenderRadioGroup;
    @BindView(R.id.nationality)
    EditText nationality;
    @BindView(R.id.applied_age)
    EditText appliedAge;
    @BindView(R.id.jobCountry)
    EditText jobCountry;
    @BindView(R.id.jobCity)
    EditText jobCity;
    @BindView(R.id.expiry_date)
    EditText expiryDate;
    @BindView(R.id.job_description)
    EditText jobDescription;
    @BindView(R.id.job_requirement)
    EditText jobRequirement;
    @BindView(R.id.post_job_btn)
    RelativeLayout editJobBtn;
    @BindView(R.id.text_btn)
    TextView textEditBtn;
    @BindView(R.id.male_radioBtn)
    RadioButton maleRadioBtn;
    @BindView(R.id.female_radioBtn)
    RadioButton femaleRadioBtn;
    @BindView(R.id.both_radioBtn)
    RadioButton bothRadioBtn;
    @BindView(R.id.categoryName)
    TextView categoryNameText;
    @BindView(R.id.job_type_text)
    TextView jobTypeText;
    private Job job = null;
    private String[] categories;
    private String[] jobTypes;
    private String category = null;
    private String jobType = null;
    private String gender = null;
    private String companyId = null;
    private String jobId = null;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_job);
        ButterKnife.bind(this);
        categoryNameText.setVisibility(View.VISIBLE);
        jobTypeText.setVisibility(View.VISIBLE);
        textEditBtn.setText("Edit Job");
        jobSpinner.setHint("Change Category...");
        jobTypeSpinner.setHint("Change Job Type...");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setSpinnersWithAdapters();
        receiveJobFromJobDetails();
        populateUI(job);
    }

    private void setSpinnersWithAdapters() {
        categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, categories);
        categoryAdapter.setDropDownViewResource(R.layout.item_spinner);
        jobSpinner.setAdapter(categoryAdapter);
        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                jobSpinner.setError(null);
                if (jobSpinner.getSelectedItem() != null) {
                    category = (String) jobSpinner.getSelectedItem();
                    categoryNameText.setText(category);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                jobSpinner.setError("Error");
            }
        });
        //---------------------------------------------------------------------
        jobTypes = getResources().getStringArray(R.array.jobTypes);
        ArrayAdapter<String> jobTypeAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, jobTypes);
        jobTypeAdapter.setDropDownViewResource(R.layout.item_spinner);
        jobTypeSpinner.setAdapter(jobTypeAdapter);
        jobTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (jobTypeSpinner.getSelectedItem() != null){
                    jobType = (String) jobTypeSpinner.getSelectedItem();
                    jobTypeText.setText(jobType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void receiveJobFromJobDetails() {
        Intent intent = getIntent();
        if (intent == null) {
            showErrorMassage();
            return;
        }
        if (intent.hasExtra(AppConstants.INTENT_JobDetailsKey)) {
            job = intent.getParcelableExtra(AppConstants.INTENT_JobDetailsKey);
        } else {
            showErrorMassage();
        }
    }

    private void populateUI(Job job) {
        if (job != null) {
            jobTitle.setText(job.getTitle());
            jobCity.setText(job.getCity());
            jobCountry.setText(job.getCountry());
            jobDescription.setText(job.getJobDescription());
            jobRequirement.setText(job.getJobRequirement());
            jobVacanciesNum.setText(job.getVacanciesNum());
            max.setText(job.getYearMax());
            min.setText(job.getYearMin());
            degreeLevel.setText(job.getDegreeLevel());
            nationality.setText(job.getNationality());
            salary.setText(job.getSalary());
            appliedAge.setText(job.getAge());
            expiryDate.setText(job.getExpireDate());

            this.gender = job.getGender();
            if (gender.equals("Male")) {
                maleRadioBtn.setChecked(true);
            } else if (gender.equals("Female")) {
                femaleRadioBtn.setChecked(true);
            } else if (gender.equals("Male & Female")) {
                bothRadioBtn.setChecked(true);
            }
            this.category = job.getCategory();
            categoryNameText.setText("Category:   "+category);
            //Toast.makeText(EditJobActivity.this, "" + category, Toast.LENGTH_SHORT).show();
            this.jobType = job.getJobType();
            jobTypeText.setText("Job Type:   "+jobType);
            companyId = job.getCompanyId();
            jobId = job.getJobId();
        }
    }

    private void showErrorMassage() {
        Toast.makeText(this, R.string.massage_job_error, Toast.LENGTH_SHORT).show();
        finish();
    }


    private void writeNewJobData() {
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
            this.gender = "Male";
        } else if (checkedRadioButtonId == R.id.female_radioBtn) {
            this.gender = "Female";
        } else if (checkedRadioButtonId == R.id.both_radioBtn) {
            this.gender = "Male & Female";
        } else {
            Toast.makeText(this, R.string.massage_notSeclect_gender, Toast.LENGTH_LONG).show();
        }
        Job job = new Job(jobId, companyId, title, category, gender, age, sal, exDate, vanNum, jMin, jMax, level, jobType, nat, country, city, des, req);
        mDatabase.child("Jobs").child(jobId).setValue(job);
        finish();

    }


    @OnClick({R.id.expiry_date, R.id.post_job_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.expiry_date:
                showCalendarAndGetDate();
                break;
            case R.id.post_job_btn:
                writeNewJobData();
                break;
        }
    }

    private void showCalendarAndGetDate() {
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
