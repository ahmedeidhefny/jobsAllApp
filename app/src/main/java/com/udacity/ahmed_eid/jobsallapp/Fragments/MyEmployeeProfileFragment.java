package com.udacity.ahmed_eid.jobsallapp.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.udacity.ahmed_eid.jobsallapp.Model.Employee;
import com.udacity.ahmed_eid.jobsallapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class MyEmployeeProfileFragment extends Fragment {

    @BindView(R.id.profile_emp_name)
    TextView profileEmpName;
    @BindView(R.id.profile_emp_jobTitle)
    TextView profileEmpJobTitle;
    @BindView(R.id.profile_emp_summ)
    TextView profileEmpSumm;
    @BindView(R.id.profile_emp_address)
    TextView profileEmpAddress;
    @BindView(R.id.portofilo_btn)
    Button portofiloBtn;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.fragment_my_employee_profile, container, false);
        unbinder = ButterKnife.bind(this, myView);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        readUserData();
        return myView;
    }

    private void readUserData() {
        String userId = mAuth.getCurrentUser().getUid();
        Query query = mDatabase.orderByChild("userId").equalTo(userId);
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
        String image = employee.getEmployeeImage() ;
        if (!TextUtils.isEmpty(image)){
            Glide.with(getActivity())
                    .load(image)
                    .error(R.drawable.male)
                    .into(empImage);
        }else {
            if (sex.equals("Male")) {
                empImage.setImageResource(R.drawable.male);
            }else if(sex.equals("Female")){
                empImage.setImageResource(R.drawable.female);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.portofilo_btn, R.id.contactMe_btn, R.id.emp_image, R.id.emp_add_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.portofilo_btn:
                String userId = mAuth.getCurrentUser().getUid();
               // mDatabase.child(userId).child("maritalStatus").removeValue();
                mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Toast.makeText(getActivity(),""+snapshot, Toast.LENGTH_SHORT).show();;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            case R.id.contactMe_btn:
                break;
            case R.id.emp_image:
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
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Toast.makeText(getActivity(), "downloadUri: " + downloadUri, Toast.LENGTH_SHORT).show();
                    writeImageRefInRealtimeDatabase(downloadUri.toString());
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void writeImageRefInRealtimeDatabase(String downloadUrl) {
        String employee_id = mAuth.getCurrentUser().getUid();
        mDatabase.child(employee_id).child("employeeImage").setValue(downloadUrl);
    }


}
