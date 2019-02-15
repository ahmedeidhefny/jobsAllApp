package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.ahmed_eid.jobsallapp.Model.Job;
import com.udacity.ahmed_eid.jobsallapp.R;
import com.udacity.ahmed_eid.jobsallapp.Utilites.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JobDetailsActivity extends AppCompatActivity {


    private static final String TAG = "JobDetailsActivity";
    @BindView(R.id.job_details_title)
    TextView jobDetailsTitle;
    @BindView(R.id.job_details_compName)
    TextView jobDetailsCompName;
    @BindView(R.id.jb_details_address)
    TextView jbDetailsAddress;
    @BindView(R.id.job_salary)
    TextView jobSalary;
    @BindView(R.id.job_desc_cat)
    TextView jobDescCat;
    @BindView(R.id.job_desc_vanc_num)
    TextView jobDescVancNum;
    @BindView(R.id.jb_desc_exp)
    TextView jbDescExp;
    @BindView(R.id.job_desc_degree)
    TextView jobDescDegree;
    @BindView(R.id.job_desc_national)
    TextView jobDescNational;
    @BindView(R.id.job_desc_gender)
    TextView jobDescGender;
    @BindView(R.id.job_desc_empl_type)
    TextView jobDescEmplType;
    @BindView(R.id.job_desc_age)
    TextView jobDescAge;
    @BindView(R.id.job_desc_expiryDate)
    TextView jobDescExpiryDate;
    @BindView(R.id.job_details_des)
    TextView jobDetailsDes;
    @BindView(R.id.job_details_req)
    TextView jobDetailsReq;

    @BindView(R.id.apply_btn)
    RelativeLayout applyBtn;

    private Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        ButterKnife.bind(this);
        receiveJobFromAdapter();
    }

    private void receiveJobFromAdapter() {
        Intent intent = getIntent();
        if (intent == null) {
            showErrorMassage();
            return;
        }
        if (intent.hasExtra(AppConstants.INTENT_JobAdapterKey)) {
            job = intent.getParcelableExtra(AppConstants.INTENT_JobAdapterKey);
            populateUI(job);
        } else {
            showErrorMassage();
        }
        if (intent.hasExtra(AppConstants.INTENT_JobAdapterCompNameKey)) {
            String comName = intent.getStringExtra(AppConstants.INTENT_JobAdapterCompNameKey);
            jobDetailsCompName.setText(comName);
        }else{
            Log.e(TAG,"Error:companyName is empty");
        }
    }

    private void showErrorMassage() {
        Toast.makeText(this, R.string.massage_user_error, Toast.LENGTH_SHORT).show();
        finish();
    }


    private void populateUI(Job job) {
        jobDetailsDes.setText(job.getJobDescription());
        jobDetailsReq.setText(job.getJobRequirement());
        jobDetailsTitle.setText(job.getTitle());
        jobDescCat.setText(job.getCategory());
        jobDescDegree.setText(job.getDegreeLevel());
        jobDescEmplType.setText(job.getJobType());
        jobDescNational.setText(job.getNationality());
        jbDetailsAddress.setText(job.getCountry() + " - " + job.getCity());
        jobDescVancNum.setText(job.getVacanciesNum());
        jbDescExp.setText(job.getYearMin() + " - " + job.getYearMax());
        jobSalary.setText(job.getSalary());
        jobDescExpiryDate.setText(job.getExpireDate());
        jobDescAge.setText(job.getAge());
        jobDescGender.setText(job.getGender());

    }

    @OnClick(R.id.apply_btn)
    public void onViewClicked() {
    }
}
