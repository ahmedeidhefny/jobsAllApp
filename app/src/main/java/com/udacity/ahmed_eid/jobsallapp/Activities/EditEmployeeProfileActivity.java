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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.ahmed_eid.jobsallapp.Model.Employee;
import com.udacity.ahmed_eid.jobsallapp.R;
import com.udacity.ahmed_eid.jobsallapp.Utilites.AppConstants;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;

public class EditEmployeeProfileActivity extends AppCompatActivity {

    @BindView(R.id.emp_name)
    EditText empName;
    @BindView(R.id.job_title)
    EditText jobTitle;
    @BindView(R.id.empSpinner)
    MaterialSpinner empSpinner;
    @BindView(R.id.gender_radio_group)
    RadioGroup genderRadioGroup;
    @BindView(R.id.male_radioBtn)
    RadioButton maleRadioBtn;
    @BindView(R.id.female_radioBtn)
    RadioButton femaleRadioBtn;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.nationality)
    EditText nationality;
    @BindView(R.id.country)
    EditText country;
    @BindView(R.id.city)
    EditText city;
    @BindView(R.id.marital_status)
    EditText maritalStatus;
    @BindView(R.id.birthOfDate)
    EditText birthOfDate;
    @BindView(R.id.miritary_status)
    EditText miritaryStatus;
    @BindView(R.id.summery)
    EditText summery;
    @BindView(R.id.employee_goToApp_btn)
    RelativeLayout editBtn;
    @BindView(R.id.editText)
    TextView editText;
    @BindView(R.id.categoryName)
    TextView categoryName;

    private String category;
    private String gender;
    private String[] categories;
    private String employeeId;
    private String userType = "Employee";
    private String empImage, employeeCV;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_register);
        ButterKnife.bind(this);
        editText.setText("Edit Profile");
        empSpinner.setHint("Change Category...");
        categoryName.setVisibility(View.VISIBLE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        receiveDataFromEmployeeProfile();
        setSpinnerAndAdapter();
    }

    private void setSpinnerAndAdapter() {
        categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, categories);
        categoryAdapter.setDropDownViewResource(R.layout.item_spinner);
        empSpinner.setAdapter(categoryAdapter);
        empSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                empSpinner.setError(null);
                if (empSpinner.getSelectedItem() != null) {
                    category = (String) empSpinner.getSelectedItem();
                    categoryName.setText(category);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                empSpinner.setError("Error");
            }
        });
    }

    private void receiveDataFromEmployeeProfile() {
        Intent intent = getIntent();
        if (intent == null) {
            showErrorMassage();
            return;
        }
        if (intent.hasExtra(AppConstants.INTENT_EmployeeOrCompanyUserKey)) {
            Employee employee = intent.getParcelableExtra(AppConstants.INTENT_EmployeeOrCompanyUserKey);
            populateUI(employee);
        } else {
            showErrorMassage();
        }
    }

    private void populateUI(Employee employee) {
        empName.setText(employee.getEmployeeName());
        city.setText(employee.getEmpCity());
        country.setText(employee.getEmpCountry());
        summery.setText(employee.getAboutMeSummery());
        miritaryStatus.setText(employee.getMilitaryStatus());
        maritalStatus.setText(employee.getMaritalStatus());
        nationality.setText(employee.getNationality());
        birthOfDate.setText(employee.getBirthOfDate());
        phone.setText(employee.getPhone());
        jobTitle.setText(employee.getJobTitle());
        this.gender = employee.getGender();

        if (gender.equals("Male")) {
            maleRadioBtn.setChecked(true);
        } else if (gender.equals("Female")) {
            femaleRadioBtn.setChecked(true);
        }

        this.category = employee.getEmpCategory();
        categoryName.setText("Category:   " + category);

        this.employeeId = employee.getUserId();
        this.empImage = employee.getEmployeeImage();
        this.employeeCV = employee.getEmployeeResumeFile();

    }

    private void showErrorMassage() {
        Toast.makeText(this, R.string.massage_user_error, Toast.LENGTH_SHORT).show();
        finish();
    }

    @OnClick({R.id.birthOfDate, R.id.employee_goToApp_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.birthOfDate:
                showCalendarAndGetDate();
                break;
            case R.id.employee_goToApp_btn:
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
                birthOfDate.setText(year + "/" + month + "/" + day);
            }
        }, year, month, day);
        pickerDialog.show();
    }

    private void editProfile() {
        String name = empName.getText().toString();
        String cit = city.getText().toString();
        String con = country.getText().toString();
        String sum = summery.getText().toString();
        String miritary = miritaryStatus.getText().toString();
        String marital = maritalStatus.getText().toString();
        String nat = nationality.getText().toString();
        String date = birthOfDate.getText().toString();
        String phoneNum = phone.getText().toString();
        String title = jobTitle.getText().toString();

        int checkedRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.male_radioBtn) {
            this.gender = "Male";
        } else if (checkedRadioButtonId == R.id.female_radioBtn) {
            this.gender = "Female";
        } else {
            Toast.makeText(this, R.string.massage_notSeclect_gender, Toast.LENGTH_LONG).show();
        }

        Employee employee = new Employee(employeeId,empImage, employeeCV, name, userType, title, phoneNum, gender, nat, con, cit, date, sum, category, miritary, marital);

        mDatabase.child("Users").child(employeeId).setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditEmployeeProfileActivity.this, "Edit Your Profile Successfully..!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(EditEmployeeProfileActivity.this, "Please, Load Your Page To Update Data..!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditEmployeeProfileActivity.this, "Filed To Edit Your Profile..!", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

    }
}
