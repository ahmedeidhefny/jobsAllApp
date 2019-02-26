package com.udacity.ahmed_eid.jobsallapp.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.udacity.ahmed_eid.jobsallapp.Activities.ContactMeActivity;
import com.udacity.ahmed_eid.jobsallapp.Activities.MyResumeActivity;
import com.udacity.ahmed_eid.jobsallapp.Model.Employee;
import com.udacity.ahmed_eid.jobsallapp.R;
import com.udacity.ahmed_eid.jobsallapp.Utilites.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class MyEmployeeProfileFragment extends Fragment {

    private static final String TAG = "MyEmployeeProfileFragment";

    @BindView(R.id.profile_emp_name)
    TextView profileEmpName;
    @BindView(R.id.profile_emp_jobTitle)
    TextView profileEmpJobTitle;
    @BindView(R.id.profile_emp_summ)
    TextView profileEmpSumm;
    @BindView(R.id.profile_emp_address)
    TextView profileEmpAddress;
    @BindView(R.id.myResume_btn)
    Button myResumeBtn;
    @BindView(R.id.contactMe_btn)
    Button contactMeBtn;
    Unbinder unbinder;
    @BindView(R.id.profile_emp_gender)
    TextView profileEmpGender;
    @BindView(R.id.profile_emp_military)
    TextView profileEmpMilitary;
    @BindView(R.id.profile_emp_marital)
    TextView profileEmpMarital;
    @BindView(R.id.profile_emp_birthDate)
    TextView profileEmpBirthDate;
    @BindView(R.id.profile_emp_national)
    TextView profileEmpNational;
    @BindView(R.id.profile_emp_country)
    TextView profileEmpCountry;
    @BindView(R.id.profile_emp_city)
    TextView profileEmpCity;
    @BindView(R.id.emp_image)
    CircleImageView empImage;
    @BindView(R.id.emp_add_icon)
    CircleImageView empAddIcon;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private Uri imageUri = null;

    private String employeeId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().findViewById(R.id.search_EText).setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.fragment_my_employee_profile, container, false);
        unbinder = ButterKnife.bind(this, myView);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        employeeId = mAuth.getCurrentUser().getUid();
        readUserData();
        return myView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.app_bar_search);
        item.setVisible(false);
    }

    private void readUserData() {
        Query query = mDatabase.child("Users").orderByChild("userId").equalTo(employeeId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Employee employee = snapshot.getValue(Employee.class);
                        populateUI(employee);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String massage = error.getMessage();
                Toast.makeText(getActivity(), "Error: " + massage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateUI(Employee employee) {
        profileEmpName.setText(employee.getEmployeeName());
        profileEmpAddress.setText(employee.getEmpCountry() + " - " + employee.getEmpCity());
        profileEmpJobTitle.setText(employee.getJobTitle());
        profileEmpSumm.setText(employee.getAboutMeSummery());
        profileEmpBirthDate.setText(employee.getBirthOfDate());
        profileEmpCity.setText(employee.getEmpCity());
        profileEmpCountry.setText(employee.getEmpCountry());
        profileEmpMarital.setText(employee.getMaritalStatus());
        profileEmpMilitary.setText(employee.getMilitaryStatus());
        profileEmpNational.setText(employee.getNationality());
        String sex = employee.getGender();
        profileEmpGender.setText(sex);
        String image = employee.getEmployeeImage();
        if (!TextUtils.isEmpty(image)) {
            Glide.with(getActivity())
                    .load(image)
                    .error(R.drawable.user_profile_default)
                    .into(empImage);
        } else {
            if (sex.equals("Male")) {
                empImage.setImageResource(R.drawable.male);
            } else if (sex.equals("Female")) {
                empImage.setImageResource(R.drawable.female);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.myResume_btn, R.id.contactMe_btn, R.id.emp_image, R.id.emp_add_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.myResume_btn:
                Intent myResumeIntent = new Intent(getActivity(), MyResumeActivity.class);
                startActivity(myResumeIntent);
                break;
            case R.id.contactMe_btn:
                Intent contactMeIntent = new Intent(getActivity(), ContactMeActivity.class);
                contactMeIntent.putExtra(AppConstants.INTENT_employeeIdKey, employeeId);
                startActivity(contactMeIntent);
                break;
            case R.id.emp_add_icon:
                pickImage();
                break;
        }
    }


    private void pickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission is denied", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                BuildImagePicker();
            }

        } else {
            BuildImagePicker();
        }
    }

    private void BuildImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(getContext(), MyEmployeeProfileFragment.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                empImage.setImageURI(imageUri);
                uploadImage();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void uploadImage() {
        String employee_id = mAuth.getCurrentUser().getUid();
        final StorageReference image_pathRef = mStorageRef.child("UsersProfileImages").child("EmployeeImages").child(employee_id + ".jpg");
        image_pathRef.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return image_pathRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Toast.makeText(getActivity(), "Uploaded The Image Successfully.. ", Toast.LENGTH_SHORT).show();
                    writeImageRefInRealtimeDatabase(downloadUri.toString());
                } else {
                    String error = task.getException().getMessage();
                    Log.e(TAG, "Error: " + error);
                    Toast.makeText(getActivity(), "Filed to Uploaded The Image..", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private void writeImageRefInRealtimeDatabase(String downloadUrl) {
        String employee_id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Users").child(employee_id).child("employeeImage").setValue(downloadUrl);
    }


}
