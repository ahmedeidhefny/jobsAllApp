package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.udacity.ahmed_eid.jobsallapp.Adapters.AppliedPersonJobAdapter;
import com.udacity.ahmed_eid.jobsallapp.Model.AppliedJob;
import com.udacity.ahmed_eid.jobsallapp.Model.Employee;
import com.udacity.ahmed_eid.jobsallapp.Model.Job;
import com.udacity.ahmed_eid.jobsallapp.Model.SavedJob;
import com.udacity.ahmed_eid.jobsallapp.R;
import com.udacity.ahmed_eid.jobsallapp.Utilites.AppConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class JobDetailsActivity extends AppCompatActivity {

    private Boolean mApplyJob = false;
    private Boolean mSaveJob = false;
    private String companyId = null;
    private String jobId = null;

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
    @BindView(R.id.text_applyBtn)
    TextView textApplyBtn;
    @BindView(R.id.job_details_compLogo)
    CircleImageView jobDetailsCompLogo;
    @BindView(R.id.layout_saveJob_btn)
    RelativeLayout layoutSaveJobBtn;
    @BindView(R.id.white_saveJob_btn)
    ImageView whiteSaveJobBtn;
    @BindView(R.id.yellow_saveJob_btn)
    ImageView yellowSaveJobBtn;
    @BindView(R.id.jobDetails_applied_recycler)
    RecyclerView jobDetailsAppliedRecycler;
    @BindView(R.id.appliedPersons_Layout)
    LinearLayout appliedPersonsLayout;

    private Job job;
    private String userType = null;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private AlertDialog.Builder alertBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        alertBuilder = new AlertDialog.Builder(this);
        getUserType();
        receiveJobFromAdapter();
        //startActivity(getIntent());
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
        if (intent.hasExtra(AppConstants.INTENT_JobAdapterCompNameKey)
                && intent.hasExtra(AppConstants.INTENT_JobAdapterCompLogoKey)) {
            String comName = intent.getStringExtra(AppConstants.INTENT_JobAdapterCompNameKey);
            if (!TextUtils.isEmpty(comName)) {
                jobDetailsCompName.setText(comName);
                jobDetailsCompName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), CompanyProfileActivity.class);
                        intent.putExtra(AppConstants.INTENT_CompanyIdKey, companyId);
                        startActivity(intent);
                    }
                });
            }
            String compLogo = intent.getStringExtra(AppConstants.INTENT_JobAdapterCompLogoKey);
            if (!TextUtils.isEmpty(compLogo)) {
                Glide.with(this)
                        .load(compLogo)
                        .error(R.drawable.default_logo)
                        .into(jobDetailsCompLogo);
            } else {
                jobDetailsCompLogo.setImageResource(R.drawable.default_logo);
            }
        } else {
            Log.e(TAG, "Error:companyName and logo is empty");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details_activity, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String userId = mAuth.getCurrentUser().getUid();
        if (!userId.equals(companyId)) {
            menu.findItem(R.id.detail_edit).setVisible(false);
            menu.findItem(R.id.detail_delete).setVisible(false);
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.detail_edit) {
            editJob();
            return true;
        } else if (item.getItemId() == R.id.detail_delete) {
            alertBuilder.setTitle(getString(R.string.alertDialog_deleteJob_title));
            alertBuilder.setIcon(R.drawable.delete);
            alertBuilder.setMessage(getString(R.string.alertDialog_deleteJob_massage));
            alertBuilder.setPositiveButton(R.string.alertDialog_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteJob(jobId);
                }
            });
            alertBuilder.setNegativeButton(R.string.alertDialog_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertBuilder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteJob(String jobId) {
        if (jobId != null) {
            Query query = mDatabase.child("Jobs").orderByChild("jobId").equalTo(jobId);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue();
                            goToMainActivity();
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    String massage = databaseError.getMessage();
                    Toast.makeText(JobDetailsActivity.this, "Error: " + massage, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Log.e(TAG, "DeleteJob: " + "jobId is null");
        }

    }

    private void goToMainActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), MainScreenWithNavigation.class);
        startActivity(mainIntent);
        finish();
    }

    private void editJob() {
        Intent editIntent = new Intent(getApplicationContext(), EditJobActivity.class);
        editIntent.putExtra(AppConstants.INTENT_JobDetailsKey, job);
        startActivity(editIntent);
        finish();
    }


    private void populateUI(Job job) {
        jobDetailsDes.setText(job.getJobDescription());
        jobDetailsReq.setText(job.getJobRequirement());
        //------
        String jobName = job.getTitle();
        String jobNameUpper = jobName.substring(0, 1).toUpperCase() + jobName.substring(1);
        jobDetailsTitle.setText(jobNameUpper);
        //-----
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

        companyId = job.getCompanyId();
        jobId = job.getJobId();
        setApplyToJobBtn(jobId);
        setSaveJobBtn(jobId);
        readAppliedUsersInThisJobInRecycler(jobId);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userType.equals("Employee")) {
                    handleApplyToJob(jobId);
                } else {
                    Toast.makeText(JobDetailsActivity.this, R.string.massage_company_notVaild, Toast.LENGTH_SHORT).show();
                }
            }
        });

        layoutSaveJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSaveJobBtn(jobId);
            }
        });
    }

    private void readAppliedUsersInThisJobInRecycler(final String jobId) {
        final ArrayList<AppliedJob> appliedJobsArr = new ArrayList<>();
        mDatabase.child("ApplyJobs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot appliedJobs : snapshot.getChildren()) {
                        if (appliedJobs.getKey().equals(jobId)) {
                            AppliedJob appliedJob = appliedJobs.getValue(AppliedJob.class);
                            // Log.e(TAG, "" + appliedJob.getEmpImage());
                            appliedJobsArr.add(appliedJob);
                        }
                    }
                }
                setRecyclerView(appliedJobsArr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String massage = error.getMessage();
                Toast.makeText(JobDetailsActivity.this, "Error: " + massage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setRecyclerView(ArrayList<AppliedJob> appliedJobsArr) {
        if (appliedJobsArr.size() != 0 && !appliedJobsArr.isEmpty()) {
            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            jobDetailsAppliedRecycler.setLayoutManager(manager);
            AppliedPersonJobAdapter adapter = new AppliedPersonJobAdapter(getApplicationContext(), appliedJobsArr);
            jobDetailsAppliedRecycler.setAdapter(adapter);
        }

    }

    private void getUserType() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("Users").child(userId).child("userType").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userType = dataSnapshot.getValue(String.class);
//                    if (userType.equals("Company")){
//                        applyBtn.setVisibility(View.INVISIBLE);
//                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "getUserType:onCancelled " + databaseError.toException());
                }
            });
        }
    }

    private void setSaveJobBtn(String jobId) {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child("SavedJobs").child(userId).child(jobId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    yellowSaveJobBtn.setVisibility(View.VISIBLE);
                    whiteSaveJobBtn.setVisibility(View.GONE);
                } else {
                    yellowSaveJobBtn.setVisibility(View.GONE);
                    whiteSaveJobBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String massage = error.getMessage();
                Toast.makeText(JobDetailsActivity.this, "Error: " + massage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleSaveJobBtn(final String jobId) {
        mSaveJob = true;
        final String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child("SavedJobs").child(userId).child(jobId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mSaveJob) {
                    if (dataSnapshot.exists()) {
                        mDatabase.child("SavedJobs").child(userId).child(jobId).removeValue();
                        yellowSaveJobBtn.setVisibility(View.GONE);
                        whiteSaveJobBtn.setVisibility(View.VISIBLE);
                        mSaveJob = false;
                    } else {
                        SavedJob savedJob = new SavedJob(jobId, userId);
                        mDatabase.child("SavedJobs").child(userId).child(jobId).setValue(savedJob);
                        yellowSaveJobBtn.setVisibility(View.VISIBLE);
                        whiteSaveJobBtn.setVisibility(View.GONE);
                        mSaveJob = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String massage = error.getMessage();
                Toast.makeText(JobDetailsActivity.this, "Error: " + massage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setApplyToJobBtn(String jobId) {
        mDatabase.child("ApplyJobs").child(mAuth.getCurrentUser().getUid()).child(jobId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    backgroundApplyBtnExistsToggle();
                } else {
                    backgroundApplyBtnNOTExistsToggle();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String massage = error.getMessage();
                Toast.makeText(getApplicationContext(), "Error: " + massage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleApplyToJob(final String jobId) {
        mApplyJob = true;
        final String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child("ApplyJobs").child(userId).child(jobId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mApplyJob) {
                    if (dataSnapshot.exists()) {
                        mDatabase.child("ApplyJobs").child(userId).child(jobId).removeValue();
                        backgroundApplyBtnNOTExistsToggle();
                        readAppliedUsersInThisJobInRecycler(jobId);
                        mApplyJob = false;
                    } else {
                        readUserData(jobId, userId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String massage = error.getMessage();
                Toast.makeText(JobDetailsActivity.this, "Error: " + massage, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void readUserData(final String jobId, final String userId) {
        Query query = mDatabase.child("Users").orderByChild("userId").equalTo(mAuth.getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Employee employee = snapshot.getValue(Employee.class);
                        String name = employee.getEmployeeName();
                        String job = employee.getJobTitle();
                        String image = employee.getEmployeeImage();
                        AppliedJob appliedJob = new AppliedJob(jobId, userId, name, image, job);
                        mDatabase.child("ApplyJobs").child(userId).child(jobId).setValue(appliedJob);
                        backgroundApplyBtnExistsToggle();
                        readAppliedUsersInThisJobInRecycler(jobId);
                        mApplyJob = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void backgroundApplyBtnExistsToggle() {
        textApplyBtn.setText(getString(R.string.after_apply_btn_text));
        applyBtn.setBackgroundColor(Color.parseColor("#dc1125"));
    }

    private void backgroundApplyBtnNOTExistsToggle() {
        textApplyBtn.setText(getString(R.string.apply_btn_text));
        applyBtn.setBackgroundColor(Color.parseColor("#6AB819"));
    }

    private void showErrorMassage() {
        Toast.makeText(this, R.string.massage_job_error, Toast.LENGTH_SHORT).show();
        finish();
    }


}
