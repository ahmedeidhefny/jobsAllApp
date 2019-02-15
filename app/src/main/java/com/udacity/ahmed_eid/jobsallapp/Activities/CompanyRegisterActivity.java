package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.ahmed_eid.jobsallapp.Model.Company;
import com.udacity.ahmed_eid.jobsallapp.Model.User;
import com.udacity.ahmed_eid.jobsallapp.R;
import com.udacity.ahmed_eid.jobsallapp.Utilites.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

public class CompanyRegisterActivity extends AppCompatActivity {

    private static final String TAG = "CompanyRegisterActivity";
    @BindView(R.id.websiteLink)
    EditText websiteLink;
    @BindView(R.id.company_name)
    EditText companyName;
    @BindView(R.id.foundedDate)
    EditText foundedDate;
    @BindView(R.id.compCountry)
    EditText compCountry;
    @BindView(R.id.compCity)
    EditText compCity;
    @BindView(R.id.company_profile)
    EditText companyProfile;
    @BindView(R.id.Company_goToApp_btn)
    RelativeLayout CompanyGoToAppBtn;
    @BindView(R.id.compSpinner)
    MaterialSpinner spinner;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private static final String userType = "Company";

    private ArrayAdapter<String> adapter;
    private String array[] = {
            "programming", "IT jobs", "engineering", "econimic", "IS programing"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_register);
        ButterKnife.bind(this);
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spinner, array);
        adapter.setDropDownViewResource(R.layout.item_spinner);
        spinner.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }



    @OnClick(R.id.Company_goToApp_btn)
    public void onViewClicked() {
        registerCompany();
    }

    private void registerCompany() {
        String compName = companyName.getText().toString();
        String website = websiteLink.getText().toString();
        String city = compCity.getText().toString();
        String country = compCountry.getText().toString();
        String profile = companyProfile.getText().toString();
        String cFounderDate = foundedDate.getText().toString();

        String userId = mAuth.getCurrentUser().getUid();

        if (!TextUtils.isEmpty(compName) && !TextUtils.isEmpty(website) && !TextUtils.isEmpty(profile) && !TextUtils.isEmpty(country) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(cFounderDate)) {
            Company company = new Company(userId,compName, userType, website, "programming", cFounderDate, city, country, profile);
            mDatabase.child(userId).setValue(company);
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
