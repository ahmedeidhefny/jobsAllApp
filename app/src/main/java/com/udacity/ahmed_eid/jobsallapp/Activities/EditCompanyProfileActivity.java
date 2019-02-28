package com.udacity.ahmed_eid.jobsallapp.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.ahmed_eid.jobsallapp.Model.Company;
import com.udacity.ahmed_eid.jobsallapp.Model.Employee;
import com.udacity.ahmed_eid.jobsallapp.R;
import com.udacity.ahmed_eid.jobsallapp.Utilites.AppConstants;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EditCompanyProfileActivity extends AppCompatActivity {

    @BindView(R.id.company_name)
    EditText companyName;
    @BindView(R.id.websiteLink)
    EditText websiteLink;
    @BindView(R.id.categoryName)
    TextView categoryName;
    @BindView(R.id.compSpinner)
    MaterialSpinner compSpinner;
    @BindView(R.id.foundedDate)
    EditText foundedDate;
    @BindView(R.id.compCountry)
    EditText compCountry;
    @BindView(R.id.compCity)
    EditText compCity;
    @BindView(R.id.company_profile)
    EditText companyProfile;
    @BindView(R.id.edit_text)
    TextView editText;
    @BindView(R.id.Company_goToApp_btn)
    RelativeLayout CompanyGoToAppBtn;

    DatabaseReference mDatabase;
    private String category;
    private String[] categories;
    private String companyId;
    private String userType = "Company";
    private String compImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_register);
        ButterKnife.bind(this);
        editText.setText("Edit Profile");
        compSpinner.setHint("Change Category...");
        categoryName.setVisibility(View.VISIBLE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        receiveDataFromCompanyProfileActivity();
        setSpinnerAndAdapter();
    }

    private void setSpinnerAndAdapter() {
        categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, categories);
        categoryAdapter.setDropDownViewResource(R.layout.item_spinner);
        compSpinner.setAdapter(categoryAdapter);
        compSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                compSpinner.setError(null);
                if (compSpinner.getSelectedItem() != null) {
                    category = (String) compSpinner.getSelectedItem();
                    categoryName.setText(category);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                compSpinner.setError("Error");
            }
        });
    }

    private void receiveDataFromCompanyProfileActivity() {
        Intent intent = getIntent();
        if (intent == null) {
            showErrorMassage();
            return;
        }
        if (intent.hasExtra(AppConstants.INTENT_EmployeeOrCompanyUserKey)) {
            Company company = intent.getParcelableExtra(AppConstants.INTENT_EmployeeOrCompanyUserKey);
            populateUI(company);
        } else {
            showErrorMassage();
        }
    }

    private void populateUI(Company company) {
        companyName.setText(company.getCompName());
        companyProfile.setText(company.getCompProfile());
        compCity.setText(company.getCompCity());
        compCountry.setText(company.getCompCountry());
        foundedDate.setText(company.getCompFounderDate());
        websiteLink.setText(company.getCompWebsite());
        this.compImage = company.getCompLogo();
        this.category = company.getCompCategory();
        categoryName.setText("Category:   " + category);
        this.companyId = company.getUserId();
    }

    private void showErrorMassage() {
        Toast.makeText(this, R.string.massage_user_error, Toast.LENGTH_SHORT).show();
        finish();
    }

    @OnClick({R.id.foundedDate, R.id.Company_goToApp_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.foundedDate:
                showCalendarAndGetDate();
                break;
            case R.id.Company_goToApp_btn:
                editProfile();
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
                foundedDate.setText(year + "/" + month + "/" + day);
            }
        }, year, month, day);
        pickerDialog.show();
    }

    private void editProfile() {
        String name = companyName.getText().toString();
        String country = compCountry.getText().toString();
        String city = compCity.getText().toString();
        String profile = companyProfile.getText().toString();
        String date = foundedDate.getText().toString();
        String link = websiteLink.getText().toString();

        Company company = new Company(companyId, compImage, name, userType, link, category, date, city, country, profile);
        mDatabase.child("Users").child(companyId).setValue(company).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditCompanyProfileActivity.this, "Edit Your Profile Successfully..!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(EditCompanyProfileActivity.this, "Please, Load Your Page To Update Data..!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditCompanyProfileActivity.this, "Filed To Edit Your Profile..!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
