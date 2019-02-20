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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.ahmed_eid.jobsallapp.Model.Company;
import com.udacity.ahmed_eid.jobsallapp.R;

import java.util.Calendar;

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
    private String[] categories;
    private String category = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_register);
        ButterKnife.bind(this);
        categories = getResources().getStringArray(R.array.categories);
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spinner, categories);
        adapter.setDropDownViewResource(R.layout.item_spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = (String) spinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }


    @OnClick({R.id.Company_goToApp_btn, R.id.foundedDate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.foundedDate:
                showCalenderAndGetDate();
                break;
            case R.id.Company_goToApp_btn:
                registerCompany();
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
                foundedDate.setText(year + "/" + month + "/" + day);
            }
        }, year, month, day);
        pickerDialog.show();
    }

    private void registerCompany() {
        String compName = companyName.getText().toString();
        String website = websiteLink.getText().toString();
        String city = compCity.getText().toString();
        String country = compCountry.getText().toString();
        String profile = companyProfile.getText().toString();
        String cFounderDate = foundedDate.getText().toString();


        String userId = mAuth.getCurrentUser().getUid();

        Toast.makeText(this, "" + category, Toast.LENGTH_SHORT).show();
        if (category != null && !TextUtils.isEmpty(compName) && !TextUtils.isEmpty(website) && !TextUtils.isEmpty(profile) && !TextUtils.isEmpty(country) && !TextUtils.isEmpty(city) && !TextUtils.isEmpty(cFounderDate)) {
            Company company = new Company(userId, compName, userType, website, category, cFounderDate, city, country, profile);
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
